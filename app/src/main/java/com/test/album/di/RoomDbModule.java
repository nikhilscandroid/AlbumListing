package com.test.album.di;

import android.arch.persistence.room.Room;

import com.test.album.app.AlbumApplication;
import com.test.album.room.AlbumsDAO;
import com.test.album.room.AlbumsDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
@Module(includes = AppModule.class)
public class RoomDbModule {
    private AlbumsDB albumsDB;

    public RoomDbModule(AlbumApplication mApplication) {
        albumsDB = Room.databaseBuilder(mApplication, AlbumsDB.class, AlbumsDB.DB_NAME).build();
    }

    @Singleton
    @Provides
    AlbumsDB providesRoomDatabase() {
        return albumsDB;
    }

    @Singleton
    @Provides
    AlbumsDAO providesProductDao(AlbumsDB albumsDB) {
        return albumsDB.getAlbumsDao();
    }
}
