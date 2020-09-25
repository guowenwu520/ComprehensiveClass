package com.example.vaporwaveVideo.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.vaporwaveVideo.entity.VideoInfoEntity;
import com.example.vaporwaveVideo.interfacecall.OnDataCompleteCall;
import com.example.vaporwaveVideo.util.TimeUitl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class VideoData {
    private static final String[] sLocalVideoColumns = {
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.ARTIST,
            MediaStore.Video.Media.ALBUM,
            MediaStore.Video.Media.RESOLUTION,
            MediaStore.Video.Media.DESCRIPTION,
            MediaStore.Video.Media.IS_PRIVATE,
            MediaStore.Video.Media.TAGS,
            MediaStore.Video.Media.CATEGORY,
            MediaStore.Video.Media.LANGUAGE,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.MINI_THUMB_MAGIC,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BOOKMARK
    };
    private static final String[] sLocalVideoThumbnailColumns = {
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Thumbnails.VIDEO_ID,
            MediaStore.Video.Thumbnails.KIND,
            MediaStore.Video.Thumbnails.WIDTH,
            MediaStore.Video.Thumbnails.HEIGHT
    };


    public static void initVideoData(final Context context, final OnDataCompleteCall<ArrayList<VideoInfoEntity>> onDataCompleteCall) {
        new AsyncTask<Void,Void,Void>(){
            ArrayList<VideoInfoEntity> mVideoInfos = new ArrayList<>();
            @Override
            protected Void doInBackground(Void... voids) {
                ContentResolver contentResolver = context.getContentResolver();

                Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, sLocalVideoColumns,
                        null , null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        VideoInfoEntity videoInfo = new VideoInfoEntity();

                        int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                        String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                        String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                         String resolution = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.RESOLUTION));
                        videoInfo.setData(data);
                        videoInfo.setSize(size);
                        videoInfo.setDisplayName(displayName);
                        videoInfo.setTitle(title);
                        videoInfo.setMimeType(mimeType);
                        videoInfo.setDuration(duration);
                        videoInfo.setResolution(resolution);
                        mVideoInfos.add(videoInfo);
                    } while (cursor.moveToNext());

                    cursor.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                onDataCompleteCall.DataFinish(context,mVideoInfos);
            }
        }.execute();

}

    public static void VideoSingData(final Context context, final String Videourl, final OnDataCompleteCall<VideoInfoEntity> onDataCompleteCall) {
        final String[] imgUrl = new String[1];
        new AsyncTask<Void,Void,Void>(){
            VideoInfoEntity videoInfo=new VideoInfoEntity();
            @Override
            protected Void doInBackground(Void... voids) {

                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(Videourl);
             long duration= Long.parseLong(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
             String data=Videourl;
             String   title=media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    videoInfo.setData(data);
                    videoInfo.setTitle(title);
                    videoInfo.setDuration(duration);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                onDataCompleteCall.DataFinish(context,videoInfo);
                saveVideo(context,new File(Videourl));
            }
        }.execute();

    }

    public static void saveVideo(Context context, File file) {
        //是否添加到相册
        ContentResolver localContentResolver = context.getContentResolver();
        ContentValues localContentValues = getVideoContentValues(context, file, System.currentTimeMillis());
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri));
    }

    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource( paramFile.getAbsolutePath());
        long duration= Long.parseLong(media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/mp4");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        localContentValues.put("duration",duration);
        return localContentValues;
}
    private static String  createThumbnailFile(Bitmap bitmap) throws IOException {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "CameraImg");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = TimeUitl.getTimeString(new Date().getTime(),"yyyyMMdd_HHmmss");
        String imageFileName = "IMG_" + timeStamp;
        String suffix = ".png";
        File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mediaFile));
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
        bos.flush();
        bos.close();
       return  mediaStorageDir + File.separator + imageFileName + suffix;
    }
}
