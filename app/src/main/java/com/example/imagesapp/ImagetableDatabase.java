package com.example.imagesapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.imagesapp.Models.ImagetableClass;

/**
 * Created by Chaitrali Khanekar on 30/11/2019.
 */

@Database(entities = ImagetableClass.class,exportSchema = false,version = 1)
public abstract class ImagetableDatabase extends RoomDatabase {

    private static final String DB_NAME="images_db";
    private static ImagetableDatabase instance;

    public static synchronized ImagetableDatabase getInstance(Context context)
    {
        if(instance==null)
        {
            instance= Room.databaseBuilder(context.getApplicationContext(),ImagetableDatabase.class,DB_NAME).allowMainThreadQueries().build();

        }return instance;
    }

    public abstract ImagetableDao imagetableDao();
}
