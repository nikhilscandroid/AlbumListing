package com.test.album.di;


import com.test.album.network.AlbumApi;
import com.test.album.room.AlbumsDAO;
import com.test.album.viewmodel.AlbumsViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */

@Singleton
@Component(modules = {AppModule.class, RetrofitModule.class, AlbumsRepositoryModule.class, RoomDbModule.class})
public interface AppComponent {
    void inject(AlbumsViewModel viewModel);

    AlbumApi getAlbumApi();

    AlbumsDAO getAlbumDAO();
}