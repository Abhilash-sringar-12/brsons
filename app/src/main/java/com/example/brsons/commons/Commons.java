package com.example.brsons.commons;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Commons {

    public Commons() {

    }

    /**
     * This mrthod util is used to dismiss progress dialog
     *
     * @param progressDialog
     */
    public static void dismissProgressDialog(ProgressDialog progressDialog) {

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /**
     * This method util is used to check network connection
     *
     * @param applicationContext
     * @return
     */
    public static boolean isNetworkConnected(Context applicationContext) {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
