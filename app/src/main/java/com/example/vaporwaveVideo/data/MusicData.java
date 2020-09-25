package com.example.vaporwaveVideo.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.example.vaporwaveVideo.entity.MusicInfoEntity;

import java.util.ArrayList;

public class MusicData {
    private static final Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static final String[] MUSIC_PROJECTION = new String[] {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE
    };
    private static final String SELECTION = "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 ";
    private static final String SORT_ORDER = MediaStore.Audio.Media.DATA;

    private ArrayList<MusicInfoEntity> getMusicInfo(Context context) {
        ArrayList<MusicInfoEntity> musicInfos = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(URI, MUSIC_PROJECTION, SELECTION, null, SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicInfoEntity musicInfo = new MusicInfoEntity();
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));

                musicInfo.setId(id);
                if (!TextUtils.isEmpty(title)) {
                    musicInfo.setTitle(title);
                }
                if (!TextUtils.isEmpty(data)) {
                    musicInfo.setData(data);
                }
                if (!TextUtils.isEmpty(album)) {
                    musicInfo.setAlbum(album);
                }
                if (!TextUtils.isEmpty(artist)) {
                    musicInfo.setArtist(artist);
                }
                if (duration != 0) {
                    musicInfo.setDuration(duration);
                }
                if (size != 0) {
                    musicInfo.setSize(size);
                }
                musicInfos.add(musicInfo);
            }
            return musicInfos;
        }
        return musicInfos;
    }
}
