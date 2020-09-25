package com.example.vaporwaveVideo.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PathUitl {
    //音视频分离之后存放文件夹
    public static final String  SEPARTATE_VEDIO="TemporaryVedio";
    //视频改变背景音乐之后存放文件夹
    public static final String  MUSIC_VEDIO="MusicVedio";

    //logo临时存放文件夹
    public static final String  LOGO_BITMAP="LogoIMG";

    public static String createVdeioUrl(String type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), type);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String imageFileName = "VID_"+System.currentTimeMillis();
        String suffix = ".mp4";
        return  mediaStorageDir + File.separator + imageFileName + suffix;
    }
    public static String createVdeioUrl(String type,String name){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), type);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String imageFileName = "VID_"+System.currentTimeMillis()+name;
        String suffix = ".mp4";
        return  mediaStorageDir + File.separator + imageFileName + suffix;
    }
    public static String createLogoUrl(Bitmap bitmap,String type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), type);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String imageFileName = "IMG_"+TimeUitl.getTimeString(new Date().getTime(),"yyyyMMddHHmmss");
        String suffix = ".png";
        try {
            File myCaptureFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  mediaStorageDir + File.separator + imageFileName + suffix;
    }
    /**
     79      * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     80      */
     @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

             final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

           // DocumentProvider
          if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                      // ExternalStorageProvider
                    if (isExternalStorageDocument(uri)) {
                             final String docId = DocumentsContract.getDocumentId(uri);
                              final String[] split = docId.split(":");
                          final String type = split[0];

                           if ("primary".equalsIgnoreCase(type)) {
                                      return Environment.getExternalStorageDirectory() + "/" + split[1];
                                 }
                           }
                    // DownloadsProvider
                       else if (isDownloadsDocument(uri)) {

                         final String id = DocumentsContract.getDocumentId(uri);
                             final Uri contentUri = ContentUris.withAppendedId(
                                         Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                            return getDataColumn(context, contentUri, null, null);
                     }
                   // MediaProvider
                   else if (isMediaDocument(uri)) {
                              final String docId = DocumentsContract.getDocumentId(uri);
                                final String[] split = docId.split(":");
                               final String type = split[0];

                             Uri contentUri = null;

                                      contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                             final String selection = "_id=?";
                                 final String[] selectionArgs = new String[]{split[1]};

                            return getDataColumn(context, contentUri, selection, selectionArgs);
                            }
                  }
               // MediaStore (and general)
               else if ("content".equalsIgnoreCase(uri.getScheme())) {
                      return getDataColumn(context, uri, null, null);
                    }
               // File
              else if ("file".equalsIgnoreCase(uri.getScheme())) {
                     return uri.getPath();
                   }
                return null;
     }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
                return "com.android.externalstorage.documents".equals(uri.getAuthority());
     }
    public static boolean isDownloadsDocument(Uri uri) {
                return "com.android.providers.downloads.documents".equals(uri.getAuthority());
     }
    public static boolean isMediaDocument(Uri uri) {
                return "com.android.providers.media.documents".equals(uri.getAuthority());
     }
}
