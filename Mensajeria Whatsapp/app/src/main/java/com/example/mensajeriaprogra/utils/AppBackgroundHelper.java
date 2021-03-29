package com.example.mensajeriaprogra.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.example.mensajeriaprogra.providers.AuthProvider;
import com.example.mensajeriaprogra.providers.UsersProvider;

import java.util.List;

public class AppBackgroundHelper {

    public static void online(Context context, boolean status){
        UsersProvider usersProvider = new UsersProvider();
        AuthProvider authProvider=new AuthProvider();

        if(authProvider.getId()!=null){
            if (isApplicationSentToBackground(context)){
                usersProvider.updateOnline(authProvider.getId(),status);

            }
            else if (status){
                usersProvider.updateOnline(authProvider.getId(),status);
            }
        }


    }

    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}

