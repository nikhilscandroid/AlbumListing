package com.test.album.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.test.album.model.Album;

import java.util.List;


/**
 * Created by Nikhil Chindarkar on 17-04-2019.
 */
@Dao
public interface AlbumsDAO {
    //it will return -1 if the item was already in db and not inserted
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertEntry(Album entry);

    @Query("SELECT * FROM Album WHERE id = :id")
    Album getSpecifigEntryById(int id);

    // Gets all entries in the database
    @Query("SELECT * FROM Album")
    List<Album> getAllEntries();

    @Update(onConflict = OnConflictStrategy.IGNORE)
    int update(Album entry);


}
