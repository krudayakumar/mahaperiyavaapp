package com.kanchi.periyava.Model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by m84098 on 9/15/15.
 */
public class NetworkConnection {
  public static boolean isConnected(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnected()) {
      return true;
    }
    return false;
  }
}
