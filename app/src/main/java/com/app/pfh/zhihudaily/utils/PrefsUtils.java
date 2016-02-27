package com.app.pfh.zhihudaily.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.pfh.zhihudaily.model.Content;

public class PrefsUtils {
    /**
     *
     * @param context
     * @param id
     * @param isRead true表示已读 false表示未读
     */

    public static void changeItemState(Context context,String id,Boolean isRead){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(id,isRead).commit();
    }


    public static Boolean getItemState(Context context,String id){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(id,false);
    }


}
