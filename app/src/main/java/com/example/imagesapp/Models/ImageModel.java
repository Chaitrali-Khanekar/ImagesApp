package com.example.imagesapp.Models;

import java.io.Serializable;

/**
 * Created by Chaitrali Khanekar on 29/11/2019.
 */
public class ImageModel implements Serializable {
    public String getImagepath() {
        return imagepath;
    }

    public String getImgtitle() {
        return imgtitle;
    }

    public void setImgtitle(String imgtitle) {
        this.imgtitle = imgtitle;
    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public ImageModel(int pageid, String imagepath, String imgtitle) {
        this.pageid=pageid;
        this.imagepath = imagepath;
        this.imgtitle = imgtitle;

    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
    int pageid;
    String imagepath="";
    String imgtitle="";
}
