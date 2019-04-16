package com.test.album.network;


import com.test.album.model.Album;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */

public interface AlbumApi {
    @GET("albums")
    Call<List<Album>> getAlbums();
}
