package com.kanchi.periyava.old.Service;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kanchi.periyava.old.Activity.MainActivity;
import com.kanchi.periyava.old.Interface.ServerCallback;
import com.kanchi.periyava.old.Model.ConstValues;
import com.kanchi.periyava.old.Model.MessageHandler;
import com.kanchi.periyava.old.Model.NetworkConnection;
import com.kanchi.periyava.old.Model.PreferenceData;
import com.kanchi.periyava.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    SET_PASSWORD("set_password.php", "set_password.json", Request.Method.POST),
    NEW_SATSANG("new_satsang.php", "new_satsang.json", Request.Method.POST),
    GET_SATSANG_LIST("get_satsang_list.php", "get_satsang_list.json", Request.Method.GET),
    JOIN_SATSANG("join_satsang.php", "join_satsang.json", Request.Method.POST),
    COUNTRY_LIST("countries.php", "countries.json", Request.Method.POST),
    PHOTO_VIDEO_LIST("photo_video_book.php", "photo_video_book.json", Request.Method.POST),
    UPDATE_JAPAN_COUNT("update_japam_count.php", "update_japam.json", Request.Method.POST),
    ABOUT_MATAM("about_matam.php", "about_matam.json", Request.Method.POST),
    ABOUT_GURU_PARAMPARA("about_guru_parampara.php", "about_guru_parampara.json", Request.Method.POST),
    ABOUT_US("about_us.php", "about_us.json", Request.Method.POST),
    GENERAL_SETTINGS("general_settings.php", "general_setting.json", Request.Method.POST),
    RADIO_SCHEDULE_LIST("schedule_list.php", "schedule_list.json", Request.Method.POST),
    RADIO_STATUS("", "", Request.Method.GET),
    RADIO_SERVER_URL("", "", Request.Method.GET),
    OTHER_RADIO_SERVER_URL("", "", Request.Method.GET),
    INDIA_RADIO_SERVER_URL("", "", Request.Method.GET);;

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
      case ABOUT_MATAM:
        strURL = baseurl + SendServerRequest.ABOUT_MATAM.getFunctionName();
        break;
      case ABOUT_GURU_PARAMPARA:
        strURL = baseurl + SendServerRequest.ABOUT_GURU_PARAMPARA.getFunctionName();
        break;
      case ABOUT_US:
        strURL = baseurl + SendServerRequest.ABOUT_US.getFunctionName();
        break;
      case GENERAL_SETTINGS:
        strURL = baseurl + SendServerRequest.GENERAL_SETTINGS.getFunctionName();
        break;
      case RADIO_SCHEDULE_LIST:
        strURL = baseurl + SendServerRequest.RADIO_SCHEDULE_LIST.getFunctionName();
        break;
      case RADIO_STATUS:
        strURL = "https://public.radio.co/stations/sfe8bb6b1e/status";
        break;
      case RADIO_SERVER_URL:
        strURL = "http://samcloud.spacial.com/api/listen?sid=71526&rid=122395&f=aac,any&br=64000,any&m=txt";
        break;
      case OTHER_RADIO_SERVER_URL:
        strURL = (String)PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.DIRECT_RADIOURL_OTHERS.toString(), new String());
        break;
      case INDIA_RADIO_SERVER_URL:
        strURL = (String)PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.DIRECT_RADIOURL_INDIA.toString(), new String());
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
                             final ServerCallback callback, final boolean isProgressVisible) {

    if (view != null) {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    if (!NetworkConnection.isConnected(context)) {
      android.os.Message msg = android.os.Message.obtain();
      msg.what = ConstValues.ERROR_INTERNET_CONNECTION;
      MessageHandler.getFlowHandler().sendMessage(msg);
      return;
    }

    String url = getURL(sendServerRequest);
    JSONObject jsondata = null;
    if (data != null) {
      jsondata = getJSONObject(data);
      Log.d(TAG, "Data:" + jsondata.toString());
    }
    if (isProgressVisible) {
      showProgressDialog();
    }


    //Mockup
    //jsondata = null;

    JsonObjectRequest jsObjRequest = new JsonObjectRequest(sendServerRequest.getRequestType(),
        getURL(sendServerRequest), jsondata, new Response.Listener<JSONObject>() {

      @Override
      public void onResponse(JSONObject response) {
        Log.d(TAG, "Response:" + response);
        callback.onSuccess(response);
        if (isProgressVisible) {
          hideProgressDialog();
        }

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
        if (isProgressVisible) {
          hideProgressDialog();
        }


      }
    }
    ) {


      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
      }

    };
    jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    queue.add(jsObjRequest);
  }

  public void executeRequest(SendServerRequest sendServerRequest, T data,
                             final ServerCallback callback) {

    executeRequest(sendServerRequest, data,
        callback, true);
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
