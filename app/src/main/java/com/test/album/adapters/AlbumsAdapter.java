package com.test.album.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.album.R;
import com.test.album.model.Album;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ResultsHolder> {
    private List<Album> albumsItemsList;
    private Context context;

    public AlbumsAdapter(Context context) {
        this.context = context;
    }

    public void setAlbumsListToShow(List<Album> albumsItemsList) {
        this.albumsItemsList = albumsItemsList;
        sortListAscending();
        notifyDataSetChanged();
    }

    public List<Album> getAlbumsListShown() {
        return albumsItemsList;
    }

    @Override
    public ResultsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.albums_item, parent, false);

        return new ResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsHolder holder, int position) {
        Album albumItem = albumsItemsList.get(position);
        holder.albumTitle.setText(albumItem.getTitle());


    }

    @Override
    public int getItemCount() {
        if (albumsItemsList == null) return 0;
        return albumsItemsList.size();
    }


    public class ResultsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textview_album_title)
        public TextView albumTitle;

        public ResultsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private void sortListAscending() {
        Collections.sort(albumsItemsList, new Comparator<Album>() {
            @Override
            public int compare(Album valueOne, Album valueTwo) {
                return valueOne.getTitle().compareTo(valueTwo.getTitle());
            }
        });
    }
}