package com.bluedot.bluedot_vale;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

public class ItemAdapter extends AppCompatActivity {

    private String image;
    private String text;
    private String uid;

    public String getImage() {
        return image;
    }
    public String getText() {
        return text;
    }

    public String getUid(){ return uid; }

    public void setImage(String image) {
        this.image = image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



}
