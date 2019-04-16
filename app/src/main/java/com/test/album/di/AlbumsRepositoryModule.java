package com.test.album.di;


import com.test.album.network.AlbumApi;
import com.test.album.repository.AlbumsRepository;
import com.test.album.room.AlbumsDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */

@Module(includes = {RoomDbModule.class, RetrofitModule.class})
public class AlbumsRepositoryModule {

    @Provides
    @Singleton
    public AlbumsRepository getAlbumsRepository(AlbumsDAO albumsDAO, AlbumApi albumApi) {
        return new AlbumsRepository(albumsDAO, albumApi);
    }
}
