package com.kanchi.periyava.old.Model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kanchi.periyava.old.Activity.MainActivity;
import com.kanchi.periyava.BuildConfig;
import com.kanchi.periyava.old.Fragments.About;
import com.kanchi.periyava.old.Fragments.AboutAcharayas;
import com.kanchi.periyava.old.Fragments.Dashboard;
import com.kanchi.periyava.old.Fragments.Login;
import com.kanchi.periyava.old.Fragments.NewSatsang;
import com.kanchi.periyava.old.Fragments.PhotoVideoBook;
import com.kanchi.periyava.old.Fragments.Radio;
import com.kanchi.periyava.old.Fragments.RadioSelector;
import com.kanchi.periyava.old.Fragments.Registration;
import com.kanchi.periyava.old.Fragments.SatsangList;
import com.kanchi.periyava.old.Fragments.SetPassword;
import com.kanchi.periyava.old.Fragments.WebPage;
import com.kanchi.periyava.old.Fragments.Welcome;
import com.kanchi.periyava.old.Interface.ServerCallback;
import com.kanchi.periyava.R;
import com.kanchi.periyava.old.ReceiveRequest.GeneralReceiveRequest;
import com.kanchi.periyava.old.ReceiveRequest.ReceiveJapamDetails;
import com.kanchi.periyava.old.ReceiveRequest.ReceiveJoinSatsang;
import com.kanchi.periyava.old.ReceiveRequest.ReceiveLogin;
import com.kanchi.periyava.old.ReceiveRequest.ReceiveSatsangList;
import com.kanchi.periyava.old.SendRequest.SendLogin;
import com.kanchi.periyava.old.Service.ServerRequest;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by m84098 on 2/15/18.
 */

public class MessageHandler {
  private static String TAG = MessageHandler.class.getSimpleName();
  private static MessageHandler messageHandler;
  private static Handler flowHandler;
  private static int currentState;
  private static MainActivity activity;
  private static Context context;

  private static MainActivity getActivity() {
    return activity;
  }
  public static MessageHandler getInstance(MainActivity _activity) {
    if (messageHandler == null) {
      activity = _activity;
      messageHandler = new MessageHandler();
      context = activity.getApplicationContext();
      flowCallBackHandler();
    }

    return messageHandler;
  }

  private static void flowCallBackHandler() {
    flowHandler = new Handler(new Handler.Callback() {
      @Override
      public boolean handleMessage(Message msg) {

        currentState = msg.what;
        //Screen Mode & Visisbility of ToolBar
        if (msg.what == ConstValues.WELCOME) {
          getActivity().getScreenMode(true);
        } else {
          getActivity().getScreenMode(false);
        }

        //Menu Setting;
        MenuItem item;

        Bundle bundle = new Bundle();
        //getActivity() Starting
        switch (msg.what) {
          case ConstValues.WELCOME:
            getActivity().setDrawerState(false);
            getActivity().getVisibilityToolBar(false);
            getActivity().showFragment(new Welcome(), null, R.id.content, true, Welcome.TAG);
            break;

          case ConstValues.ABOUT_PEETHAM:
            bundle = new Bundle();
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_about_peetham));
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.about_peetham);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_peetam_content);
            getActivity().showFragment(new About(), bundle, R.id.content, true, About.TAG);
            break;

          case ConstValues.ABOUT_ACHARAYAS: {
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_about_acharayas));
            getActivity().showFragment(new AboutAcharayas(), bundle, R.id.content, true, AboutAcharayas.TAG);
          }
          break;
          case ConstValues.ABOUT_ACHARAYAS_68: {
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_about_acharayas));
            bundle = new Bundle();
            bundle.putInt(ConstValues.CONST_ABOUT_TITLE, R.string.lbl_about_acharaya68_fullname);
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.acharaya68);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_acharaya68_content);
            getActivity().showFragment(new About(), bundle, R.id.content, true, About.TAG);
            break;
          }
          case ConstValues.ABOUT_ACHARAYAS_69: {
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_about_acharayas));
            bundle = new Bundle();
            bundle.putInt(ConstValues.CONST_ABOUT_TITLE, R.string.lbl_about_acharaya69_fullname);
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.acharaya69);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_acharaya69_content);
            getActivity().showFragment(new About(), bundle, R.id.content, true, About.TAG);
            break;
          }
          case ConstValues.ABOUT_ACHARAYAS_70: {
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_about_acharayas));
            bundle = new Bundle();
            bundle.putInt(ConstValues.CONST_ABOUT_TITLE, R.string.lbl_about_acharaya70_fullname);
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.acharaya70);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_acharaya70_content);
            getActivity().showFragment(new About(), bundle, R.id.content, true, About.TAG);
            break;
          }
          case ConstValues.ABOUT_US: {
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_about_us));
            bundle = new Bundle();
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.about_us);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_content);
            getActivity().showFragment(new About(), bundle, R.id.content, true, About.TAG);
          }
          break;

          case ConstValues.HELP_FEEDBACK: {
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_feedback));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + GeneralSetting.getInstance().feedbackemailid));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback" + "(" + (BuildConfig.DEBUG == true ? "Debug" : "Release") + " Ver " + BuildConfig.VERSION_NAME + ")");
            getActivity().startActivity(intent);
          }
          break;
          case ConstValues.RADIOSELECTOR: {

            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_online_radio));
            bundle = new Bundle();
            bundle.putString("SERVER_SELECTION", String.valueOf(msg.obj));
            getActivity().showFragment(new Radio(), bundle, R.id.content, true, Radio.TAG);
            break;
          }
          case ConstValues.RADIO: {
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_online_radio));
            getActivity().showFragment(new RadioSelector(), null, R.id.content, true, Radio.TAG);
            break;
          }
          case ConstValues.RADIO_PLAY: {
            getActivity().startPlaying("INDIA", null);
            break;
          }
          case ConstValues.RADIO_RUN_STOP: {
            MenuItem actionRestart = (MenuItem) getActivity().menu.findItem(R.id.radio);
            String URL = getActivity().getResources().getString(R.string.link_radio_others);
            String strOption = (String) msg.obj;
            int isAlternative = msg.arg1;
            String tmpUrl = "";
            if (strOption.equalsIgnoreCase("INDIA")) {

              tmpUrl = (String) PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.DIRECT_RADIOURL_INDIA_URL1.toString(), new String());
              if (TextUtils.isEmpty(tmpUrl)) {
                tmpUrl = (String) PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.DIRECT_RADIOURL_INDIA_URL2.toString(), new String());
              }
              URL = tmpUrl;

            } else {

              tmpUrl = (String) PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.DIRECT_RADIOURL_OTHERS_URL1.toString(), new String());
              if (TextUtils.isEmpty(tmpUrl)) {
                tmpUrl = (String) PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.DIRECT_RADIOURL_OTHERS_URL2.toString(), new String());
              }

              URL = tmpUrl;

            }
            Log.d(TAG, "URL=" + URL);
            getActivity().RunRadio(strOption, actionRestart, Uri.parse(URL));
            break;
          }
          case ConstValues.RADIO_SCHEDULE_LIST: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.RADIO_SCHEDULE_LIST, null, (ServerCallback) msg.obj);

          }
          case ConstValues.RADIO_GET_PLAYLIST: {
            break;
          }
          case ConstValues.LOGIN:
            getActivity().setDrawerState(false);
            getActivity().getVisibilityToolBar(false);
            getActivity().showFragment(new Login(), null, R.id.content, true, Login.TAG);
            break;
          case ConstValues.LOGIN_SERVE_REQUEST: {
            final SendLogin loginsendrequest = (SendLogin) msg.obj;
            getActivity().getServerRequestSend().executeRequest(ServerRequest.SendServerRequest.LOGIN, (SendLogin) msg.obj, new ServerCallback() {
              @Override
              public void onSuccess(JSONObject response) {
                ReceiveLogin receiveLogin = new Gson().fromJson(response.toString(), ReceiveLogin.class);
                Message msgtmp = Message.obtain();
                msgtmp.obj = (Object) response;
                if (receiveLogin.isSuccess()) {
                  UserProfile.getUserProfile().username = receiveLogin.data.username;
                  UserProfile.getUserProfile().profileid = receiveLogin.data.profileid;
                  UserProfile.getUserProfile().emailid = loginsendrequest.emailid;
                  UserProfile.getUserProfile().isjoinedjapam = receiveLogin.data.isjoinedjapam == 0 ? false : true;
                  UserProfile.getUserProfile().isjoinedsatsang = receiveLogin.data.isjoinedsatsang == 0 ? false : true;
                  UserProfile.getUserProfile().ispasswordreset = receiveLogin.data.ispasswordreset == 0 ? false : true;
                  UserProfile.getUserProfile().satsangid = receiveLogin.data.satsangid;
                  UserProfile.getUserProfile().japam_count = receiveLogin.data.japam_count;
                  UserProfile.getUserProfile().japam_last_updated_date = receiveLogin.data.japam_last_updated_date;
                  UserProfile.getUserProfile().japam_count_satsang = receiveLogin.data.japam_count_satsang;
                  UserProfile.getUserProfile().japam_count_over_all = receiveLogin.data.japam_count_over_all;
                  UserProfile.getUserProfile().isLoggedIn = true;

                  if (UserProfile.getUserProfile().ispasswordreset == true) {
                    msgtmp.what = ConstValues.SET_PASSWORD;
                  } else {
                    msgtmp.what = ConstValues.LOGIN_SUCCESSFUL;
                  }
                } else {
                  msgtmp.what = ConstValues.LOGIN_ERROR;
                }
                getFlowHandler().sendMessage(msgtmp);
              }

              @Override
              public void onError(VolleyError error) {
                Message msg = Message.obtain();
                msg.what = ConstValues.ERROR_DEFAULT;
                getFlowHandler().sendMessage(msg);
              }
            });
            break;
          }
          case ConstValues.GENERAL_SETTING_SERVER_REQUEST: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.GENERAL_SETTINGS, null, (ServerCallback) msg.obj, false);
          }
          break;
          case ConstValues.GENERAL_SETTING_RADIO_SERVER_URL_REQUEST: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.RADIO_SERVER_URL, null, new ServerCallback() {
                  @Override
                  public void onSuccess(JSONObject response) {
                    Log.d(TAG, response.toString());
                  }

                  @Override
                  public void onError(VolleyError error) {

                  }
                }, false);
          }
          break;
          case ConstValues.LOGIN_SUCCESSFUL: {
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.DASHBOARD_LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            break;
          }
          case ConstValues.LOGIN_ERROR: {
            ReceiveLogin receiveLogin = new Gson().fromJson(((JSONObject) msg.obj).toString(), ReceiveLogin.class);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), receiveLogin.message, null, null);
            break;
          }

          case ConstValues.DASHBOARD_WITHOUT_LOGIN: {
            getActivity().setDrawerState(true);
            getActivity().getVisibilityToolBar(true);
            UserProfile.getUserProfile().clearAll();
            getActivity().featuresEnable();
            getActivity().showFragment(new Dashboard(), null, R.id.content, true, Dashboard.TAG);
            break;
          }
          case ConstValues.DASHBOARD_LOGIN: {
            getActivity().setDrawerState(true);
            getActivity().featuresEnable();
            getActivity().showFragment(new Dashboard(), null, R.id.content, true, Dashboard.TAG);
            break;
          }


          //Set Password Screen.
          case ConstValues.SET_PASSWORD:
            getActivity().showFragment(new SetPassword(), null, R.id.content, true, SetPassword.TAG);

            break;
          case ConstValues.SET_PASSWORD_SERVER_REQUEST: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.SET_PASSWORD, msg.obj, new ServerCallback() {
                  @Override
                  public void onSuccess(JSONObject response) {
                    GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(response.toString(),
                        GeneralReceiveRequest.class);
                    Message msgtmp = Message.obtain();
                    msgtmp.obj = (Object) response;

                    if (generalReceiveRequest.isSuccess()) {
                      msgtmp.what = ConstValues.SET_PASSWORD_SUCCESS;
                    } else {
                      msgtmp.what = ConstValues.SET_PASSWORD_ERROR;
                    }
                    getFlowHandler().sendMessage(msgtmp);

                  }

                  @Override
                  public void onError(VolleyError error) {
                    error.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = ConstValues.ERROR_DEFAULT;
                    getFlowHandler().sendMessage(msg);
                  }
                });
            break;
          }
          case ConstValues.SET_PASSWORD_SUCCESS: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), generalReceiveRequest.message, null, null);
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.DASHBOARD_LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            break;
          }
          case ConstValues.SET_PASSWORD_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), generalReceiveRequest.message, null, null);
            break;
          }
          //Registration Flow
          case ConstValues.REGISTER:
            getActivity().showFragment(new Registration(), null, R.id.content, true, Registration.TAG);
            break;

          case ConstValues.REGISTER_SERVER_REQUEST: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.REGISTER, msg.obj, new ServerCallback() {
                  @Override
                  public void onSuccess(JSONObject response) {
                    GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(response.toString(),
                        GeneralReceiveRequest.class);
                    Message msgtmp = Message.obtain();
                    msgtmp.obj = (Object) response;

                    if (generalReceiveRequest.isSuccess()) {
                      msgtmp.what = ConstValues.REGISTER_SUCCESS;
                    } else {
                      msgtmp.what = ConstValues.REGISTER_ERROR;
                    }
                    getFlowHandler().sendMessage(msgtmp);

                  }

                  @Override
                  public void onError(VolleyError error) {
                    error.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = ConstValues.ERROR_DEFAULT;
                    getFlowHandler().sendMessage(msg);
                  }
                });
          }
          break;

          case ConstValues.REGISTER_SUCCESS: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), generalReceiveRequest.message, null, null);
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            break;
          }
          case ConstValues.REGISTER_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            View view = getActivity().findViewById(R.id.content);
            getActivity().ShowSnackBar(context, view, generalReceiveRequest.message, null, null);
          }
          break;

          case ConstValues.FORGOT_PASSWORD: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.FORGOT_PASSWORD, msg.obj, new ServerCallback() {
                  @Override
                  public void onSuccess(JSONObject response) {
                    GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(response.toString(),
                        GeneralReceiveRequest.class);
                    Message msgtmp = Message.obtain();
                    msgtmp.obj = (Object) response;
                    if (generalReceiveRequest.isSuccess()) {
                      msgtmp.what = ConstValues.FORGOT_PASSWORD_SUCCESS;
                    } else {
                      msgtmp.what = ConstValues.FORGOT_PASSWORD_ERROR;
                    }
                    getFlowHandler().sendMessage(msgtmp);

                  }

                  @Override
                  public void onError(VolleyError error) {
                    error.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = ConstValues.ERROR_DEFAULT;
                    getFlowHandler().sendMessage(msg);
                  }
                });
          }
          break;

          case ConstValues.FORGOT_PASSWORD_SUCCESS: {
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.msg_password_mail_email), null, null);
            break;
          }
          case ConstValues.FORGOT_PASSWORD_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }
          break;

          case ConstValues.DEIVATHIN_KURAL: {
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_devithin_kural));
            bundle = new Bundle();
            bundle.putString(getActivity().WEBURL, getActivity().getResources().getString(R.string.link_devithinkural));
            getActivity().showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
          }
          break;

          case ConstValues.SATSANG_LIST_LOGIN:
          case ConstValues.SATSANG_LIST_WITHOUT_LOGIN: {
            getActivity().showFragment(new SatsangList(), null, R.id.content, true, SatsangList.TAG);
          }
          break;

          case ConstValues.SATSANG_LIST_SERVER_REQUEST: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.GET_SATSANG_LIST, null, (ServerCallback) msg.obj);

          }
          break;
          case ConstValues.SATSANG_LIST_SERVER_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }
          break;
          case ConstValues.NEW_SATSANG: {
            bundle = new Bundle();
            bundle.putSerializable(ConstValues.SATSANG_OPTION, "NEW");
            getActivity().showFragment(new NewSatsang(), bundle, R.id.content, true, NewSatsang.TAG);
          }
          break;
          case ConstValues.NEW_SATSANG_SAVE: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.NEW_SATSANG, msg.obj, new ServerCallback() {
                  @Override
                  public void onSuccess(JSONObject response) {
                    GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(response.toString(),
                        GeneralReceiveRequest.class);
                    Message msgtmp = Message.obtain();
                    msgtmp.obj = (Object) response;
                    if (generalReceiveRequest.isSuccess()) {
                      msgtmp.what = ConstValues.NEW_SATSANG_SAVE_SUCCESS;
                    } else {
                      msgtmp.what = ConstValues.NEW_SATSANG_SAVE_ERROR;
                    }
                    getFlowHandler().sendMessage(msgtmp);

                  }

                  @Override
                  public void onError(VolleyError error) {
                    error.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = ConstValues.ERROR_DEFAULT;
                    getFlowHandler().sendMessage(msg);
                  }
                });
          }

          break;
          case ConstValues.NEW_SATSANG_SAVE_SUCCESS: {
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.SATSANG_LIST_LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.msg_successfully_satsang_saved), null, null);
            break;
          }
          case ConstValues.NEW_SATSANG_SAVE_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }

          break;

          case ConstValues.EDIT_SATSANG: {
            bundle = new Bundle();
            bundle.putSerializable(ConstValues.SATSANG_OPTION, "EDIT");
            bundle.putSerializable(ConstValues.SATSANG_DATA, (ReceiveSatsangList.Data) msg.obj);
            getActivity().showFragment(new NewSatsang(), bundle, R.id.content, true, NewSatsang.TAG);
          }
          break;
          case ConstValues.COUNTRY_LIST: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.COUNTRY_LIST, null, (ServerCallback) msg.obj);
          }
          break;

          case ConstValues.JAPAM_SERVER_REQUEST: {
            ArrayList<Object> value = (ArrayList<Object>) msg.obj;
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.UPDATE_JAPAN_COUNT, value.get(0), (ServerCallback) value.get(1));
          }
          break;

          case ConstValues.JAPAM_SUCCESS: {
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.msg_successfully_japam_updated), null, null);
            break;
          }
          case ConstValues.JAPAM_ERROR: {
            ReceiveJapamDetails generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), ReceiveJapamDetails.class);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }
          break;
          case ConstValues.PHOTO_LIST: {
            bundle = new Bundle();
            bundle.putString(ConstValues.VIDEO_PHOTO_OPTION, ConstValues.CONST_PHOTO);
            getActivity().showFragment(new PhotoVideoBook(), bundle, R.id.content, true, PhotoVideoBook.TAG);
          }
          break;

          case ConstValues.VIDEO_LIST: {
            bundle = new Bundle();
            bundle.putString(ConstValues.VIDEO_PHOTO_OPTION, ConstValues.CONST_VIDEO);
            getActivity().showFragment(new PhotoVideoBook(), bundle, R.id.content, true, PhotoVideoBook.TAG);

          }
          break;
          case ConstValues.BOOKS_LIST: {
            bundle = new Bundle();
            bundle.putString(ConstValues.VIDEO_PHOTO_OPTION, ConstValues.CONST_BOOKS);
            getActivity().showFragment(new PhotoVideoBook(), bundle, R.id.content, true, PhotoVideoBook.TAG);
          }
          break;
          case ConstValues.PHOTO_VIDEO_LIST_SERVER_REQUEST: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.PHOTO_VIDEO_LIST, null, (ServerCallback) msg.obj);
          }
          break;
          case ConstValues.JOIN_SATSANG_SERVER_REQUEST: {
            getActivity().getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.JOIN_SATSANG, msg.obj, new ServerCallback() {
                  @Override
                  public void onSuccess(JSONObject response) {
                    GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(response.toString(),
                        GeneralReceiveRequest.class);
                    Message msgtmp = Message.obtain();
                    msgtmp.obj = (Object) response;
                    if (generalReceiveRequest.isSuccess()) {
                      msgtmp.what = ConstValues.JOIN_SATSANG_SUCCESS;
                    } else {
                      msgtmp.what = ConstValues.JOIN_SATSANG_ERROR;
                    }
                    getFlowHandler().sendMessage(msgtmp);

                  }

                  @Override
                  public void onError(VolleyError error) {
                    error.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = ConstValues.ERROR_DEFAULT;
                    getFlowHandler().sendMessage(msg);
                  }
                });
          }
          break;
          case ConstValues.JOIN_SATSANG_SUCCESS: {
            ReceiveJoinSatsang generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), ReceiveJoinSatsang.class);
            UserProfile.getUserProfile().isjoinedsatsang = true;
            UserProfile.getUserProfile().satsangid = generalReceiveRequest.data.satsangid;
            UserProfile.getUserProfile().japam_count = generalReceiveRequest.data.japam_count;
            UserProfile.getUserProfile().japam_last_updated_date = generalReceiveRequest.data.japam_last_updated_date;
            UserProfile.getUserProfile().japam_count_satsang = generalReceiveRequest.data.japam_count_satsang;
            UserProfile.getUserProfile().japam_count_over_all = generalReceiveRequest.data.japam_count_over_all;
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), generalReceiveRequest.message, null, null);
            break;
          }
          case ConstValues.JOIN_SATSANG_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }
          case ConstValues.JOIN_SATSANG_ALREADY_JOINED: {

            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), "Currently you are associated with this satsang", null, null);
          }
          break;

          case ConstValues.WEB_PAGE: {
            bundle = new Bundle();
            bundle.putString(getActivity().WEBURL, (String) msg.obj);
            getActivity().showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
          }
          break;

          case ConstValues.VIDEO_OPEN: {
            bundle = new Bundle();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) msg.obj)));
          }
          break;
          //Default Error
          case ConstValues.ERROR_DEFAULT: {
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.err_default), null, null);
            break;
          }

          //Internet Error
          case ConstValues.ERROR_INTERNET_CONNECTION: {
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            getActivity().radiostate = false;
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), getActivity().getResources().getString(R.string.err_connect_to_internet), "Settings", new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
              }
            });
            break;
          }
          case ConstValues.SHOW_MSG: {
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;
            String strMessage = (String) msg.obj;
            getActivity().ShowSnackBar(context, getActivity().getWindow().getDecorView(), strMessage, null, null);
            break;
          }

          case ConstValues.PRIVACY_POLICY: {
            getActivity().setTitle(getActivity().getResources().getString(R.string.lbl_privacy_policy));
            bundle = new Bundle();
            bundle.putString(getActivity().WEBURL, getActivity().getResources().getString(R.string.link_privacy_policy));
            getActivity().showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
          }
          break;

          default:
            break;
        }

        return true;
      }

    }

    );

  }

  public static Handler getFlowHandler() {
    return flowHandler;
  }

}
