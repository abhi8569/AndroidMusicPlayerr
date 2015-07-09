package com.abhi8569.musicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abishek on 06-07-2015.
 */
public class SongManager {

    // SDCard Path
    final String MEDIA_PATH = new String("/sdcard/");
    static Cursor cursor;
    static ArrayList<AlbumArt> albumArtList;
    // Constructor
    public SongManager() {

    }

    public static Cursor populateQueries(Context context) {

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,     // context id/ uri id of the file

        };

        cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);

       albumArtList = new ArrayList<AlbumArt>();
        while (cursor.moveToNext()) {
            long albumID = cursor.getInt(0);
            final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albumID);

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
            } catch (Exception exception) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.speaker_icon);
            }
            albumArtList.add(new AlbumArt(bitmap, albumID));
        }

        // the last parameter sorts the data alphanumerically

        return cursor;
    }

    public static Bitmap getAlbumArt(Context context,long _albumID)
    {
        for(AlbumArt a: albumArtList)
        {
            if(a.getAlbumID()==_albumID)
                return a.getImage();
        }
        return BitmapFactory.decodeResource(context.getResources(),R.drawable.speaker_icon);
    }
}
