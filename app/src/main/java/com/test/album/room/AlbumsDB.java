package com.test.album.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.test.album.model.Album;


/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
@Database(entities = {Album.class}, version = AlbumsDB.VERSION)
public abstract class AlbumsDB extends RoomDatabase {

    static final int VERSION = 1;
    public static final String DB_NAME = "album_db";

    public abstract AlbumsDAO getAlbumsDao();

}