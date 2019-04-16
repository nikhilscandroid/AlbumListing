package com.test.album.views;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.test.album.R;
import com.test.album.adapters.AlbumsAdapter;
import com.test.album.model.Album;
import com.test.album.model.Resource;
import com.test.album.model.Status;
import com.test.album.viewmodel.AlbumsViewModel;

import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
public class AlbumsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindInt(R.integer.grid_columns)
    public int columnsCount;
    @BindView(R.id.recycler_view_album_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.todo_list_empty_view)
    LinearLayout todoListEmptyView;
    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.parentlayout)
    ConstraintLayout parentLayout;

    private AlbumsViewModel viewModel;
    private GridLayoutManager mGridLayoutManager;
    private AlbumsAdapter mAlbumsAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        ButterKnife.bind(this);


        mGridLayoutManager = new GridLayoutManager(this, columnsCount);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAlbumsAdapter = new AlbumsAdapter(AlbumsActivity.this);
        mRecyclerView.setAdapter(mAlbumsAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mGridLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.line_divider));
        pullToRefresh.setOnRefreshListener(this);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        pullToRefresh.setRefreshing(true);
        viewModel = ViewModelProviders.of(this).get(AlbumsViewModel.class);


        viewModel.getAlbumsListObservable().observe(AlbumsActivity.this, new Observer<Resource<List<Album>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Album>> albums) {
                Timber.d("Status" + albums.status.toString());
                if (albums.status != Status.LOADING) {
                    pullToRefresh.setRefreshing(false);
                }
                if (albums.data != null && !compareLists(mAlbumsAdapter.getAlbumsListShown(), albums.data)) {
                    mAlbumsAdapter.setAlbumsListToShow(albums.data);
                    todoListEmptyView.setVisibility(View.GONE);
                    pullToRefresh.setRefreshing(false);
                }
            }
        });
    }

    public boolean compareLists(List<Album> baseList, List<Album> newList) {
        boolean areSame = true;
        if (baseList == null) {
            areSame = false;
        } else {
            for (Album item : newList) {
                if (!baseList.contains(item)) {
                    areSame = false;
                }
            }
        }
        return areSame;
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getData();
    }

    // swipe to refresh listener
    @Override
    public void onRefresh() {
        // Fetching data from server
        viewModel.getData();
    }


}
