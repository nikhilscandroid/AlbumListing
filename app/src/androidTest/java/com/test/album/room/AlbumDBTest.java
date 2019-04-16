package com.test.album.room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import com.test.album.app.AlbumApplication;
import com.test.album.model.Album;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;


/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
@RunWith(AndroidJUnit4.class)
public class AlbumDBTest {
    private AlbumsDAO userDao;
    private AlbumsDB db;

    @Before
    public void createDb() {
        Context context = AlbumApplication.getMyApplication();
        db = Room.inMemoryDatabaseBuilder(context, AlbumsDB.class).build();
        userDao = db.getAlbumsDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        Album album = new Album();
        album.setId(1);
        album.setTitle("Family Album");
        userDao.insertEntry(album);
        Album byId = userDao.getSpecifigEntryById(album.getId());
        Assert.assertEquals(byId, album);
    }
}