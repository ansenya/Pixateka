package ru.senya.pixateka.retrofit;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

public class Utils {
    public final static String BASE_URL = "http://192.168.1.110:9090/api/";
    public static String TOKEN;
    public static String SESSION_ID;

    public static void setTOKEN(String TOKEN) {
        Utils.TOKEN = TOKEN;
    }

    public static void setSessionId(String sessionId) {
        SESSION_ID = sessionId;
    }

    public static String getRealPath(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;
        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }
}
