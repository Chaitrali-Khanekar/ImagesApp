package com.example.imagesapp.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Chaitrali Khanekar on 30/11/2019.
 */

@Entity(tableName = "imagetable")
public class ImagetableClass {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @ColumnInfo(name = "title")
    private String title;

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    @ColumnInfo(name = "pageid")
    private int pageid;

    private String image;

    public ImagetableClass(int pageid,String title, String image) {
        this.pageid = pageid;
        this.title = title;
        this.image = image;
    }
}
