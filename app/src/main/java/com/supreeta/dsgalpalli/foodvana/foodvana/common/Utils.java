package com.supreeta.dsgalpalli.foodvana.foodvana.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
public class Utils {

    public static String getURL(String page) {
        return String.format("%s/%s", SERVER_URL, page);
    }

    public static int getCurrentUserId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int userId = preferences.getInt(KEY_LOGIN_USERID, 0);
        return userId;
    }

    public static String convertInputStream(InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream);
        StringBuilder builder = new StringBuilder();
        int chr;
        try {
            while ((chr = reader.read()) != -1) {
                builder.append((char)chr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}


