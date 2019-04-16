package com.test.album.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.test.album.app.AlbumApplication;
import com.test.album.model.Album;
import com.test.album.model.Resource;
import com.test.album.repository.AlbumsRepository;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
public class AlbumsViewModel extends ViewModel {

    @Inject
    AlbumsRepository mRepository;
    //mediator lists because they transfer the changes of the livedata lists of the repository
    private MediatorLiveData<Resource<List<Album>>> albumsListObservable = new MediatorLiveData<Resource<List<Album>>>();

    @Inject
    public AlbumsViewModel() {
        super();
        AlbumApplication.getMyApplication().getAlbumViewModelComponent().inject(this);

        //subscribe to Livedata of the repository and pass it along to the view (activity - fragment etc)
        albumsListObservable.addSource(mRepository.getAlbumsListObservable(), new Observer<Resource<List<Album>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Album>> albums) {
                albumsListObservable.setValue(albums);
            }
        });
    }

    public void getData() {
        mRepository.fetchData();
    }

    public LiveData<Resource<List<Album>>> getAlbumsListObservable() {
        return albumsListObservable;
    }
}
