package com.test.album.repository;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.test.album.model.Album;
import com.test.album.model.Resource;
import com.test.album.model.Status;
import com.test.album.network.AlbumApi;
import com.test.album.room.AlbumsDAO;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
public class AlbumsRepository {
    private AlbumsDAO albumsDAO;
    private AlbumApi albumApi;
    private MutableLiveData<Resource<List<Album>>> albumsListObservable = new MutableLiveData<Resource<List<Album>>>();
    private Status pendingStatus;

    @Inject
    public AlbumsRepository(AlbumsDAO albumsDAO, AlbumApi albumApi) {
        this.albumsDAO = albumsDAO;
        this.albumApi = albumApi;

    }

    public void fetchData() {
        pendingStatus = Status.LOADING;
        loadAllAlbumsFromDB();
        getAlbums();
    }

    public MutableLiveData<Resource<List<Album>>> getAlbumsListObservable() {
        return albumsListObservable;
    }


    private void getAlbums() {
        Timber.d("getAlbums");
        albumApi.getAlbums().enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful()) {
                    pendingStatus = Status.SUCCESS;
                    addAlbumsToDB(response.body());
                } else {
                    // error case
                    setAlbumsListObservableStatus(Status.ERROR, String.valueOf(response.code()));
                    switch (response.code()) {
                        case 404:
                            Timber.d("not found");
                            break;
                        case 500:
                            Timber.d("not logged in or server broken");
                            break;
                        default:
                            Timber.d("unknown error");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Log.d("Fail", "" + t.getMessage());
                setAlbumsListObservableStatus(Status.ERROR, t.getMessage());
            }
        });
    }

    private void addAlbumsToDB(List<Album> items) {
        Timber.d("addAlbumsToDB");
        new AsyncTask<List<Album>, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(List<Album>... params) {
                boolean needsUpdate = false;
                for (Album item : params[0]) {
                    //start to insert item in db
                    Long inserted = albumsDAO.insertEntry(item); //-1 if not inserted
                    if (inserted == -1) {
                        //check if the item in db is exactly the same with the item that we wanted
                        //to insert. If not then update item
                        if (!item.equals(albumsDAO.getSpecifigEntryById(item.getId()))) {
                            int updated = albumsDAO.update(item);
                            needsUpdate = true;
                        }
                    } else {
                        needsUpdate = true;
                    }
                }
                return needsUpdate;
            }

            @Override
            protected void onPostExecute(Boolean needUpdate) {
                if (needUpdate) {
                    loadAllAlbumsFromDB();
                } else {
                    setAlbumsListObservableStatus(pendingStatus, null);
                }

            }
        }.execute(items);
    }

    private void loadAllAlbumsFromDB() {
        Timber.d("loadAllAlbumsFromDB");
        new AsyncTask<Void, Void, List<Album>>() {
            @Override
            protected List<Album> doInBackground(Void... a) {
                return albumsDAO.getAllEntries();
            }

            @Override
            protected void onPostExecute(List<Album> results) {
                //if no data is stored in db then the pendingStatus will be loading
                setAlbumsListObservableData(results, null);
            }
        }.execute();
    }

    /**
     * This method changes the observable's LiveData data without changing the status
     *
     * @param mAlbumsList the data that need to be updated
     * @param message     optional message for error
     */
    private void setAlbumsListObservableData(List<Album> mAlbumsList, String message) {
        Timber.d("setAlbumsListObservableData");
        Status loadingStatus = pendingStatus;
        if (albumsListObservable.getValue() != null) {
            loadingStatus = albumsListObservable.getValue().status;
        }
        switch (loadingStatus) {
            case LOADING:
                albumsListObservable.setValue(Resource.loading(mAlbumsList));
                break;
            case ERROR:
                albumsListObservable.setValue(Resource.error(message, mAlbumsList));
                break;
            case SUCCESS:
                albumsListObservable.setValue(Resource.success(mAlbumsList));
                break;
        }
    }

    /**
     * This method changes the observable's LiveData status without changing the data
     *
     * @param status  The new status of LiveData
     * @param message optional message for error
     */
    private void setAlbumsListObservableStatus(Status status, String message) {
        Timber.d("setAlbumsListObservableStatus" + message);
        List<Album> loadingList = null;
        if (albumsListObservable.getValue() != null) {
            loadingList = albumsListObservable.getValue().data;
        }
        switch (status) {
            case ERROR:
                albumsListObservable.setValue(Resource.error(message, loadingList));
                break;
            case LOADING:
                albumsListObservable.setValue(Resource.loading(loadingList));
                break;
            case SUCCESS:
                //extra carefull not to be null, could implement a check but not needed now
                albumsListObservable.setValue(Resource.success(loadingList));
                break;
        }
        Timber.d("Status:" + status.toString());
    }
}
