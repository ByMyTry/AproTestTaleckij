package com.taleckij_anton.apro_task_taleckij.cars_db.DataModels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.Nullable;

public class Photo {
    @Nullable
    Bitmap bitmapPhoto;
    @Nullable
    byte[] bytesPhoto;

    @Nullable
    public Bitmap getBitmapPhoto() {
        if(bitmapPhoto != null)
            return  bitmapPhoto;
        if(bytesPhoto != null)
            return getByteArrayAsBitmap(bytesPhoto);
        return null;
    }

    public void setBitmapPhoto(@Nullable Bitmap bitmapPhoto) {
        this.bitmapPhoto = bitmapPhoto;
    }

    public boolean isBitmapPhotoNull(){
        return bitmapPhoto == null;
    }

    @Nullable
    public byte[] getBytesPhoto() {
        if(bytesPhoto != null)
            return  bytesPhoto;
        if(bitmapPhoto != null)
            return getBitmapAsByteArray(bitmapPhoto);
        return null;
    }

    public void setBytesPhoto(@Nullable byte[] bytesPhoto) {
        this.bytesPhoto = bytesPhoto;
    }

    public Photo(@Nullable Bitmap bitmapPhoto, @Nullable byte[] bytesPhoto) {
        this.bitmapPhoto = bitmapPhoto;
        this.bytesPhoto = bytesPhoto;
    }

    public static Bitmap getByteArrayAsBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //TODO седлать автоопределение форматов(jpeg, png, ...)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
