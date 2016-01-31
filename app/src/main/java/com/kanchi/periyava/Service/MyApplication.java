package com.kanchi.periyava.Service;

/**
 * Created by m84098 on 9/26/15.
 */

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.kanchi.periyava.BuildConfig;

import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {

  public static final String TAG = MyApplication.class.getSimpleName();

  private RequestQueue mRequestQueue;

  private static MyApplication mInstance;

  @Override
  public void onCreate() {
    super.onCreate();
		if (BuildConfig.DEBUG == false) {
			Fabric.with(this, new Crashlytics());
		}
    mInstance = this;
  }

  public static synchronized MyApplication getInstance() {
    return mInstance;
  }

  public RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    return mRequestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req, String tag) {
    req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    getRequestQueue().add(req);
  }

  public <T> void addToRequestQueue(Request<T> req) {
    req.setTag(TAG);
    getRequestQueue().add(req);
  }

  public void cancelPendingRequests(Object tag) {
    if (mRequestQueue != null) {
      mRequestQueue.cancelAll(tag);
    }
  }
}
