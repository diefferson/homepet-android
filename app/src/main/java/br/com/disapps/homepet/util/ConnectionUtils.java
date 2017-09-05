package br.com.disapps.homepet.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by diefferson.santos on 23/08/17.
 */
public class ConnectionUtils {

     static int TYPE_WIFI = 1;
     static int TYPE_MOBILE = 2;
     static int TYPE_NOT_CONNECTED = 0;

     static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }


    public static boolean isOnline(Context context) {
        int status = getConnectivityStatus(context);
        return (status == TYPE_WIFI || status == TYPE_MOBILE);
    }
}
