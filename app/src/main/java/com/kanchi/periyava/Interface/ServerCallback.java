package com.kanchi.periyava.Interface;

import org.json.JSONObject;

import com.android.volley.VolleyError;

/**
 * Created by m84098 on 9/12/15.
 */
public interface ServerCallback {
  public void onSuccess(JSONObject response);

  public void onError(VolleyError error);
}
