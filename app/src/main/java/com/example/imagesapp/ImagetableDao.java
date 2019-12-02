package com.example.imagesapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.imagesapp.Models.ImagetableClass;

import java.util.List;

/**
 * Created by Chaitrali Khanekar on 30/11/2019.
 */

@Dao
public interface ImagetableDao {
    @Query("Select * from imagetable")
    List<ImagetableClass> getimagesList();

    @Query("Select * from imagetable where pageid=:pageid")
    List<ImagetableClass> getpageidPresent(int pageid);

    @Query("SELECT * FROM imagetable LIMIT :limit OFFSET :offset")
    public List<ImagetableClass>  getimagespageList(int limit,int offset);

    @Insert
    void insertImagesData(ImagetableClass imagetableClass);

    @Delete
    void deleteImagesData(ImagetableClass imagetableClass);

}
