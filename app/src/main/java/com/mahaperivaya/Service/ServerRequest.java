package com.mahaperivaya.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Interface.ServerCallback;
import com.mahaperivaya.Model.NetworkConnection;
import com.mahaperivaya.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by m84098 on 9/26/15.
 */
public class ServerRequest<T> {
  String TAG = "ServerRequest";
  private static RequestQueue queue;
  private static Context context;
  String baseurl = "http://kgpfoundation.org/Satsang1/";
  //String baseurl = "http://192.168.1.8:8001/";
  private boolean isMocked = false;
  // Progress dialog
  private ProgressDialog pDialog;
  private static View view;

  public enum SendServerRequest {
    LOGIN("login.php", "login.json", Request.Method.POST),
    LOGOUT("logout", "logut.json", Request.Method.POST),
    REGISTER("register.php", "register.json", Request.Method.POST),
    PASSWORD_RESET("password_reset.php", "password_rest.json", Request.Method.POST),
    FORGOT_PASSWORD("forgot_password.php", "forgot_password.json", Request.Method.POST),
    SET_PASSWORD("set_password", "set_password.json", Request.Method.POST),
    NEW_SATSANG("new_satsang.php", "new_satsang.json", Request.Method.POST),
    GET_SATSANG_LIST("get_satsang_list.php", "get_satsang_list.json", Request.Method.GET),
    JOIN_SATSANG("join_satsang.php", "join_satsang.json", Request.Method.POST),
    COUNTRY_LIST("countries.php", "countries.json", Request.Method.POST),
    PHOTO_VIDEO_LIST("photo_video.php", "photo_video.json", Request.Method.POST),
    UPDATE_JAPAN_COUNT("update_japam_count.php", "update_japam.json", Request.Method.GET);

    private String functionname = null;
    private int requesttype;
    private String mockfilename = null;

    private SendServerRequest(String functionname, String mockfilename, int requesttype) {
      this.functionname = functionname;
      this.requesttype = requesttype;
      this.mockfilename = mockfilename;
    }

        /* public String getFunctionName() {
             return this.mockfilename;
         }*/

    public String getFunctionName() {
      return this.functionname;
    }

    public int getRequestType() {
      return this.requesttype;
    }

  }

  private static ServerRequest serverRequest = null;

  public static ServerRequest getInstance(Context _context, View _view) {
    if (serverRequest == null) {

      serverRequest = new ServerRequest(_context);

    }
    context = _context;
    view = _view;
    return serverRequest;
  }


  private ServerRequest(Context contxt) {
    queue = Volley.newRequestQueue(contxt);
  }

  private String getURL(SendServerRequest sendServerRequest) {
    String strURL = null;

    switch (sendServerRequest) {
      case LOGIN:
        strURL = baseurl + SendServerRequest.LOGIN.getFunctionName();
        break;
      case LOGOUT:
        strURL = baseurl + SendServerRequest.LOGOUT.getFunctionName();
        break;
      case REGISTER:
        strURL = baseurl + SendServerRequest.REGISTER.getFunctionName();
        break;
      case SET_PASSWORD:
        strURL = baseurl + SendServerRequest.SET_PASSWORD.getFunctionName();
        break;
      case PASSWORD_RESET:
        strURL = baseurl + SendServerRequest.PASSWORD_RESET.getFunctionName();
        break;
      case FORGOT_PASSWORD:
        strURL = baseurl + SendServerRequest.FORGOT_PASSWORD.getFunctionName();
        break;
      case NEW_SATSANG:
        strURL = baseurl + SendServerRequest.NEW_SATSANG.getFunctionName();
        break;
      case GET_SATSANG_LIST:
        strURL = baseurl + SendServerRequest.GET_SATSANG_LIST.getFunctionName();
        break;
      case JOIN_SATSANG:
        strURL = baseurl + SendServerRequest.JOIN_SATSANG.getFunctionName();
        break;
      case UPDATE_JAPAN_COUNT:
        strURL = baseurl + SendServerRequest.UPDATE_JAPAN_COUNT.getFunctionName();
        break;
      case PHOTO_VIDEO_LIST:
        strURL = baseurl + SendServerRequest.PHOTO_VIDEO_LIST.getFunctionName();
        break;
      case COUNTRY_LIST:
        strURL = baseurl + SendServerRequest.COUNTRY_LIST.getFunctionName();
        break;
    }
    Log.d(TAG, "URL:" + strURL);
    return strURL;
  }

  private JSONObject getJSONObject(Object data) {
    try {
      return new JSONObject(new Gson().toJson(data));
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void executeRequest(SendServerRequest sendServerRequest, T data,
                             final ServerCallback callback) {

    if (view != null) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    if (!NetworkConnection.isConnected(context)) {
      ((MainActivity) context).ShowSnackBar(context, view, context.getResources().getString(R.string.err_connect_to_internet), null, null);
      return;
    }

    String url = getURL(sendServerRequest);
    JSONObject jsondata = null;
    if (data != null) {
      jsondata = getJSONObject(data);
      Log.d(TAG, "Data:" + jsondata.toString());
    }
    showProgressDialog();


    //Mockup
    //jsondata = null;

    JsonObjectRequest jsObjRequest = new JsonObjectRequest(sendServerRequest.getRequestType(),
        getURL(sendServerRequest), jsondata, new Response.Listener<JSONObject>() {

      @Override
      public void onResponse(JSONObject response) {
        Log.d(TAG, "Response:" + response);
        callback.onSuccess(response);
        hideProgressDialog();
      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        // TODO Auto-generated method stub
        error.printStackTrace();
        if (error instanceof NoConnectionError) {
          ((MainActivity) context).ShowSnackBar(context, view, context.getResources().getString(R.string.err_default), null, null);
        }
        callback.onError(error);
        hideProgressDialog();


      }
    }
    ) {
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", jsondata.toString());
                Log.d(TAG,"Params:"+params);
                return params;
            }*/
             /* @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");

                return headers;
            }*/

    };
    jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    queue.add(jsObjRequest);
  }

  private void showProgressDialog() {
    pDialog = new ProgressDialog(context);
    pDialog.setCancelable(false);
    pDialog.setMessage("Please Wait ...");
    if (!pDialog.isShowing()) {
      pDialog.show();
    }

  }

  private void hideProgressDialog() {

    if (pDialog.isShowing())
      pDialog.dismiss();
  }

}
