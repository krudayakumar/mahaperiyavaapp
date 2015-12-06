/*
 * Copyright 2015 Steven Mulder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mahaperivaya.Activity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mahaperivaya.Component.CircularImageView;
import com.mahaperivaya.Fragments.About;
import com.mahaperivaya.Fragments.Dashboard;
import com.mahaperivaya.Fragments.Japam;
import com.mahaperivaya.Fragments.Login;
import com.mahaperivaya.Fragments.NewSatsang;
import com.mahaperivaya.Fragments.PhotoVideoBook;
import com.mahaperivaya.Fragments.Radio;
import com.mahaperivaya.Fragments.Registration;
import com.mahaperivaya.Fragments.SatsangList;
import com.mahaperivaya.Fragments.SetPassword;
import com.mahaperivaya.Fragments.Setting;
import com.mahaperivaya.Fragments.WebPage;
import com.mahaperivaya.Fragments.Welcome;
import com.mahaperivaya.Interface.DialogActionCallback;
import com.mahaperivaya.Interface.ServerCallback;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.GeneralSetting;
import com.mahaperivaya.Model.PreferenceData;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.GeneralReceiveRequest;
import com.mahaperivaya.ReceiveRequest.ReceiveGeneralSettings;
import com.mahaperivaya.ReceiveRequest.ReceiveJapamDetails;
import com.mahaperivaya.ReceiveRequest.ReceiveJoinSatsang;
import com.mahaperivaya.ReceiveRequest.ReceiveLogin;
import com.mahaperivaya.ReceiveRequest.ReceiveSatsangList;
import com.mahaperivaya.SendRequest.SendLogin;
import com.mahaperivaya.Service.ServerRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends MBaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private static final long DRAWER_CLOSE_DELAY_MS = 350;
  private static final String NAV_ITEM_ID = "navItemId";
  public static final String WEBURL = "URL";

  private final com.mahaperivaya.Fragments.Dashboard mDashboard = new com.mahaperivaya.Fragments.Dashboard();
  private final WebPage mWebPage = new WebPage();
  private final About mAbout = new About();
  private MediaPlayer player;
  private final Handler mDrawerActionHandler = new Handler();
  private DrawerLayout mDrawerLayout;
  private TextView username, emailid;
  CircularImageView userimage;
  private ActionBarDrawerToggle mDrawerToggle;
  private int mNavItemId;
  Context context;
  FloatingActionButton radio;
  public static boolean radiostate = false;
  public static Menu globalmenu;
  // Progress dialog
  private ProgressDialog pDialog;
  private NavigationView navigationView;
  private static Toolbar toolbar;
  private Menu menu;

  public enum MenuOptions {
    GENERAL(0),
    ADD_NEW(1),
    SAVE(2),
    EDIT(3),
    DELETE(4),
    RADIO(5),
    FEEDBACK(6);
    int option;

    private MenuOptions(int _option) {
      option = _option;
    }

    public int getOption() {
      return option;
    }
  }

  ;

  public static void getMenuVisible(MenuOptions options) {
    getMenuOption(options).setVisible(true);
  }

  public static MenuItem getMenuOption(MenuOptions options) {
    return globalmenu.getItem(options.getOption());
  }

  public static void setMenuOption(Menu menu) {
    globalmenu = menu;
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    context = this;
    initComponents(savedInstanceState);

    initializeMediaPlayer();
    UserProfile.getInstance();
    PreferenceData.getInstance(context);
    GeneralSetting.getInstance();

    flowCallBackHandler();
    Message msg = Message.obtain();
    msg.what = ConstValues.WELCOME;
    getFlowHandler().sendMessage(msg);

  }


  public void getVisibilityToolBar(boolean visibility) {
    toolbar.setVisibility(visibility == true ? View.VISIBLE : View.GONE);
  }

  private void initComponents(Bundle savedInstanceState) {
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    /*// load saved navigation state if present
    if (null == savedInstanceState) {
      mNavItemId = R.id.dashboard;
    } else {
      mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
    }*/

    // listen for navigation events
    navigationView = (NavigationView) findViewById(R.id.navigation);
    navigationView.setNavigationItemSelectedListener(this);

    // select the correct nav menu item
    //navigationView.getMenu().findItem(mNavItemId).setChecked(true);

    // set up the hamburger icon to open and close the drawer
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open,
        R.string.close);
    mDrawerLayout.setDrawerListener(mDrawerToggle);
    mDrawerToggle.syncState();
    username = (TextView) findViewById(R.id.username);
    emailid = (TextView) findViewById(R.id.emailid);
    //userimage = (CircularImageView) findViewById(R.id.userimage);

    navigate(mNavItemId);


  }

  public void featuresEnable() {
    if (UserProfile.getUserProfile().isLoggedIn) {
      username.setVisibility(View.VISIBLE);
      emailid.setVisibility(View.VISIBLE);
      username.setText(UserProfile.getUserProfile().username);
      emailid.setText(UserProfile.getUserProfile().emailid);
      username.setVisibility(View.GONE);
      emailid.setVisibility(View.GONE);
      //userimage.setVisibility(View.GONE);
      navigationView.getMenu().findItem(R.id.signin).setVisible(false);
      navigationView.getMenu().findItem(R.id.signout).setVisible(false);
      //navigationView.getMenu().findItem(R.id.setting).setVisible(false);
      navigationView.getMenu().findItem(R.id.japam).setVisible(false);
    } else {
      username.setVisibility(View.GONE);
      emailid.setVisibility(View.GONE);
      //userimage.setVisibility(View.GONE);

      navigationView.getMenu().findItem(R.id.signin).setVisible(false);
      navigationView.getMenu().findItem(R.id.signout).setVisible(false);
      navigationView.getMenu().findItem(R.id.japam).setVisible(false);
      //navigationView.getMenu().findItem(R.id.setting).setVisible(false);
    }

  }

  //Enabling disabling hamburg menu.
  public void setDrawerState(boolean isEnabled) {
    if (isEnabled) {
      mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
      //mDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
      mDrawerToggle.setDrawerIndicatorEnabled(true);
      mDrawerToggle.syncState();

    } else {
      mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
      mDrawerToggle.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
      mDrawerToggle.setDrawerIndicatorEnabled(false);
      mDrawerToggle.syncState();
    }
  }


  private static Handler flowHandler;
  private static int currentState;

  private void flowCallBackHandler() {
    flowHandler = new Handler(new Handler.Callback() {
      @Override
      public boolean handleMessage(Message msg) {

        currentState = msg.what;
        //Screen Mode & Visisbility of ToolBar
        if (msg.what == ConstValues.WELCOME) {
          getScreenMode(true);
        } else {
          getScreenMode(false);
        }

        //Menu Setting;
        MenuItem item;

        Bundle bundle = new Bundle();
        //Activity Starting
        switch (msg.what) {
          case ConstValues.WELCOME:
            setDrawerState(false);
            getVisibilityToolBar(false);
            showFragment(new Welcome(), null, R.id.content, true, Welcome.TAG);
            break;

          case ConstValues.ABOUT_PEETHAM:
            setTitle(getResources().getString(R.string.lbl_about_peetham));
            bundle = new Bundle();
            bundle.putString(WEBURL, getResources().getString(R.string.base_url)+"about_peetham.php");
            showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
            break;

          case ConstValues.ABOUT_ACHARAYAS: {
            setTitle(getResources().getString(R.string.lbl_about_acharayas));
            bundle = new Bundle();
            bundle.putString(WEBURL, getResources().getString(R.string.base_url)+"about_acharays.php");
            showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
          }
            break;


          case ConstValues.ABOUT_US: {
            setTitle(getResources().getString(R.string.lbl_about_us));
            bundle = new Bundle();
            bundle.putString(WEBURL, getResources().getString(R.string.base_url)+"about_us.php");
            showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
          }
          break;

          case ConstValues.FEEDBACK: {
            setTitle(getResources().getString(R.string.lbl_feedback));
            bundle = new Bundle();
            bundle.putString(WEBURL, getResources().getString(R.string.base_url)+"about_acharays.php");
            showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
          }
          break;
          case ConstValues.RADIO: {
            setTitle(getResources().getString(R.string.lbl_online_radio));
            showFragment(new Radio(), null, R.id.content, true, Radio.TAG);
            break;
          }
          case ConstValues.RADIO_RUN_STOP: {
            MenuItem actionRestart = (MenuItem) menu.findItem(R.id.radio);
            onOptionsItemSelected(actionRestart);
            break;
          }





          case ConstValues.LOGIN:
            setDrawerState(false);
            getVisibilityToolBar(false);
            showFragment(new Login(), null, R.id.content, true, Login.TAG);
            break;
          case ConstValues.LOGIN_SERVE_REQUEST: {
            final SendLogin loginsendrequest = (SendLogin) msg.obj;
            getServerRequestSend().executeRequest(ServerRequest.SendServerRequest.LOGIN, (SendLogin) msg.obj, new ServerCallback() {
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

          case ConstValues.LOGIN_SUCCESSFUL: {
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.DASHBOARD_LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            break;
          }
          case ConstValues.LOGIN_ERROR: {
            ReceiveLogin receiveLogin = new Gson().fromJson(((JSONObject) msg.obj).toString(), ReceiveLogin.class);
            ShowSnackBar(context, getWindow().getDecorView(), receiveLogin.message, null, null);
            break;
          }

          case ConstValues.DASHBOARD_WITHOUT_LOGIN: {
            setDrawerState(true);
            getVisibilityToolBar(true);
            UserProfile.getUserProfile().clearAll();
            featuresEnable();
            showFragment(new Dashboard(), null, R.id.content, true, Dashboard.TAG);
            break;
          }
          case ConstValues.DASHBOARD_LOGIN: {
            setDrawerState(true);
            featuresEnable();
            showFragment(new Dashboard(), null, R.id.content, true, Dashboard.TAG);
            break;
          }


          //Set Password Screen.
          case ConstValues.SET_PASSWORD:
            showFragment(new SetPassword(), null, R.id.content, true, SetPassword.TAG);

            break;
          case ConstValues.SET_PASSWORD_SERVER_REQUEST: {
            getServerRequestSend().executeRequest(
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
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.message, null, null);
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.DASHBOARD_LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            break;
          }
          case ConstValues.SET_PASSWORD_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.message, null, null);
            break;
          }
          //Registration Flow
          case ConstValues.REGISTER:
            showFragment(new Registration(), null, R.id.content, true, Registration.TAG);
            break;

          case ConstValues.REGISTER_SERVER_REQUEST: {
            getServerRequestSend().executeRequest(
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
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.message, null, null);
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            break;
          }
          case ConstValues.REGISTER_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            View view = findViewById(R.id.content);
            ShowSnackBar(context, view, generalReceiveRequest.message, null, null);
          }
          break;

          case ConstValues.FORGOT_PASSWORD: {
            getServerRequestSend().executeRequest(
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
            ShowSnackBar(context, getWindow().getDecorView(), getResources().getString(R.string.msg_password_mail_email), null, null);
            break;
          }
          case ConstValues.FORGOT_PASSWORD_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }
          break;

          case ConstValues.DEIVATHIN_KURAL: {
            setTitle(getResources().getString(R.string.lbl_devithin_kural));
            bundle = new Bundle();
            bundle.putString(WEBURL, getResources().getString(R.string.link_devithinkural));
            showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
          }
          break;

          case ConstValues.SATSANG_LIST_LOGIN:
          case ConstValues.SATSANG_LIST_WITHOUT_LOGIN: {
            showFragment(new SatsangList(), null, R.id.content, true, SatsangList.TAG);
          }
          break;

          case ConstValues.SATSANG_LIST_SERVER_REQUEST: {
            getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.GET_SATSANG_LIST, null, (ServerCallback) msg.obj);

          }
          break;
          case ConstValues.SATSANG_LIST_SERVER_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }
          break;
          case ConstValues.NEW_SATSANG: {
            bundle = new Bundle();
            bundle.putSerializable(ConstValues.SATSANG_OPTION, "NEW");
            showFragment(new NewSatsang(), bundle, R.id.content, true, NewSatsang.TAG);
          }
          break;
          case ConstValues.NEW_SATSANG_SAVE: {
            getServerRequestSend().executeRequest(
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
            ShowSnackBar(context, getWindow().getDecorView(), getResources().getString(R.string.msg_successfully_satsang_saved), null, null);
            break;
          }
          case ConstValues.NEW_SATSANG_SAVE_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }

          break;

          case ConstValues.EDIT_SATSANG: {
            bundle = new Bundle();
            bundle.putSerializable(ConstValues.SATSANG_OPTION, "EDIT");
            bundle.putSerializable(ConstValues.SATSANG_DATA, (ReceiveSatsangList.Data) msg.obj);
            showFragment(new NewSatsang(), bundle, R.id.content, true, NewSatsang.TAG);
          }
          break;
          case ConstValues.COUNTRY_LIST: {
            getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.COUNTRY_LIST, null, (ServerCallback) msg.obj);
          }
          break;

          case ConstValues.JAPAM_SERVER_REQUEST: {
            ArrayList<Object> value = (ArrayList<Object>) msg.obj;
            getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.UPDATE_JAPAN_COUNT, value.get(0), (ServerCallback) value.get(1));
          }
          break;

          case ConstValues.JAPAM_SUCCESS: {
            ShowSnackBar(context, getWindow().getDecorView(), getResources().getString(R.string.msg_successfully_japam_updated), null, null);
            break;
          }
          case ConstValues.JAPAM_ERROR: {
            ReceiveJapamDetails generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), ReceiveJapamDetails.class);
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }
          break;
          case ConstValues.PHOTO_LIST: {
            bundle = new Bundle();
            bundle.putString(ConstValues.VIDEO_PHOTO_OPTION, ConstValues.CONST_PHOTO);
            showFragment(new PhotoVideoBook(), bundle, R.id.content, true, PhotoVideoBook.TAG);
          }
          break;

          case ConstValues.VIDEO_LIST: {
            bundle = new Bundle();
            bundle.putString(ConstValues.VIDEO_PHOTO_OPTION, ConstValues.CONST_VIDEO);
            showFragment(new PhotoVideoBook(), bundle, R.id.content, true, PhotoVideoBook.TAG);

          }
          break;
          case ConstValues.BOOKS_LIST: {
            bundle = new Bundle();
            bundle.putString(ConstValues.VIDEO_PHOTO_OPTION, ConstValues.CONST_BOOKS);
            showFragment(new PhotoVideoBook(), bundle, R.id.content, true, PhotoVideoBook.TAG);
          }
          break;
          case ConstValues.PHOTO_VIDEO_LIST_SERVER_REQUEST: {
            getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.PHOTO_VIDEO_LIST, null, (ServerCallback) msg.obj);
          }
          break;
          case ConstValues.JOIN_SATSANG_SERVER_REQUEST: {
            getServerRequestSend().executeRequest(
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
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.message, null, null);
            break;
          }
          case ConstValues.JOIN_SATSANG_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.message, null, null);
          }
          case ConstValues.JOIN_SATSANG_ALREADY_JOINED: {

            ShowSnackBar(context, getWindow().getDecorView(), "Currently you are associated with this satsang", null, null);
          }
          break;

          case ConstValues.WEB_PAGE: {
            bundle = new Bundle();
            bundle.putString(WEBURL, (String) msg.obj);
            showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
          }
          break;

          case ConstValues.VIDEO_OPEN: {
            bundle = new Bundle();
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((String) msg.obj)));
          }
          break;


          case ConstValues.GENERAL_SETTING_SERVER_REQUEST: {
            getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.GENERAL_SETTINGS, msg.obj, new ServerCallback() {
                  @Override
                  public void onSuccess(JSONObject response) {

                    ReceiveGeneralSettings generalReceiveRequest = new Gson().fromJson(response.toString(),
                        ReceiveGeneralSettings.class);

                    if (generalReceiveRequest.isSuccess()) {

                      PreferenceData.getInstance(context).setValue(PreferenceData.PREFVALUES.GENERAL_SETTINGS.toString(), new Gson().toJson(generalReceiveRequest.data).toString());
                    } else {
                      new Gson().toJson(generalReceiveRequest.data).toString();
                      String strData = "";
                      strData = (String) PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.GENERAL_SETTINGS.toString(), (Object) strData);
                      if(!TextUtils.isEmpty(strData)) {
                        GeneralSetting.setInstance(new Gson().fromJson(response.toString(),
                            GeneralSetting.class));
                      }
                      Message msgtmp = Message.obtain();
                      msgtmp.obj = (Object) response;
                      msgtmp.what = ConstValues.ERROR_DEFAULT;
                      getFlowHandler().sendMessage(msgtmp);
                    }


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

          //Default Error

          //Default Error
          case ConstValues.ERROR_DEFAULT: {
            ShowSnackBar(context, getWindow().getDecorView(), getResources().getString(R.string.err_default), null, null);
            break;
          }

          //Internet Error
          case ConstValues.ERROR_INTERNET_CONNECTION: {
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            ShowSnackBar(context, getWindow().getDecorView(), getResources().getString(R.string.err_connect_to_internet), "Settings", new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
              }
            });
            break;
          }
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
    setMenuOption(menu);
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.settting_menu, menu);
    //setMenuVisibile();
    return super.onCreateOptionsMenu(menu);

  }

  /**
   * Performs the actual navigation logic, updating the main content fragment.
   */
  private void navigate(final int itemId) {
    Bundle bundle = new Bundle();
    MenuItem item;
    Message msg = Message.obtain();
    switch (itemId) {
      case R.id.about_peetham:
        msg.what = ConstValues.ABOUT_PEETHAM;
        getFlowHandler().sendMessage(msg);
        break;
      case R.id.about_acharays:
        msg.what = ConstValues.ABOUT_ACHARAYAS;
        getFlowHandler().sendMessage(msg);
        break;

      case R.id.aboutus:
        msg.what = ConstValues.ABOUT_US;
        getFlowHandler().sendMessage(msg);
        break;

      case R.id.feedback:
        msg.what = ConstValues.FEEDBACK;
        getFlowHandler().sendMessage(msg);
        break;
      case R.id.exit:
        android.os.Process.killProcess(android.os.Process.myPid());
        break;
      case R.id.signin:
        msg.what = ConstValues.LOGIN;
        getFlowHandler().sendMessage(msg);
        break;
      case R.id.signout:
        super.ShowMessage(MessageDisplay.DO_YOU_WANT_EXIT, new DialogActionCallback() {
          @Override
          public void onOKClick(String errorCode) {
            Message msg = Message.obtain();
            msg.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;
            getFlowHandler().sendMessage(msg);
          }

          @Override
          public void onCancelClick(String errorCode) {

          }
        });

        break;
      case R.id.dashboard:
        if (UserProfile.getUserProfile().isLoggedIn) {
          msg.what = ConstValues.DASHBOARD_LOGIN;

        } else {
          msg.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;

        }
        getFlowHandler().sendMessage(msg);
        break;
      case R.id.books:
        msg.what = ConstValues.BOOKS_LIST;
        getFlowHandler().sendMessage(msg);
        break;
      /*case R.id.devithinkural:
        msg.what = ConstValues.DEIVATHIN_KURAL;
        getFlowHandler().sendMessage(msg);
        break;*/
      case R.id.satsang:
        if (UserProfile.getUserProfile().isLoggedIn) {
          msg.what = ConstValues.SATSANG_LIST_LOGIN;

        } else {
          msg.what = ConstValues.SATSANG_LIST_WITHOUT_LOGIN;

        }
        getFlowHandler().sendMessage(msg);

        break;
      case R.id.japam:
        if (UserProfile.getInstance().isjoinedsatsang == true) {
          super.showFragment(new Japam(), null, R.id.content, true, Japam.TAG);
        } else {
          ShowSnackBar(context, getWindow().getDecorView(), getResources().getString(R.string.err_not_joined), null, null);
        }
        break;
      case R.id.radio:
        item = globalmenu.findItem(R.id.radio);
        item.setVisible(true);
        msg.what = ConstValues.RADIO;
        getFlowHandler().sendMessage(msg);
        break;
      case R.id.gallery:
        msg.what = ConstValues.PHOTO_LIST;
        getFlowHandler().sendMessage(msg);
        break;
      case R.id.videos:
        msg.what = ConstValues.VIDEO_LIST;
        getFlowHandler().sendMessage(msg);
        break;

      /*case R.id.setting:
        super.showFragment(new Setting(), null, R.id.content, true, Setting.TAG);
        break;

      case R.id.about:
        super.showFragment(mAbout, null, R.id.content, true, mAbout.TAG);
        break;
        */
      default:
        // ignore
        break;
    }
  }

  /**
   * Handles clicks on the navigation menu.
   */
  @Override
  public boolean onNavigationItemSelected(final MenuItem menuItem) {
    // update highlighted item in the navigation menu
    menuItem.setChecked(true);
    mNavItemId = menuItem.getItemId();

    // allow some time after closing the drawer before performing real
    // navigation
    // so the user can see what is happening
    mDrawerLayout.closeDrawer(GravityCompat.START);
    mDrawerActionHandler.postDelayed(new Runnable() {
      @Override
      public void run() {
        navigate(menuItem.getItemId());
      }
    }, DRAWER_CLOSE_DELAY_MS);
    return true;
  }

  @Override
  public void onConfigurationChanged(final Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  public void RunRadio(MenuItem item){
    Radio frg = (Radio) getFragmentManager()
        .findFragmentByTag(Radio.TAG);
    if (radiostate == false) {
      startPlaying();
      radiostate = true;
      showProgressDialog();
      item.setIcon(R.drawable.ic_stop);
    } else {
      stopPlaying();
      radiostate = false;
      item.setIcon(R.drawable.ic_play);
    }
    if(frg!=null) {
      frg.setRadioButtonState(radiostate);
    }

  }
  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {

    switch (item.getItemId()) {
      case R.id.radio:
        RunRadio(item);
        break;
      case R.id.feedback:

        // TODO Auto-generated method stub
        takeScreenshot();

        File F = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        Uri uri = Uri.fromFile(F);
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("vnd.android.cursor.item/email");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
            new String[]{GeneralSetting.getInstance().feedbackemailid});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
            getResources().getString(R.string.lbl_feedback));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
            getResources().getString(R.string.lbl_feedback));
        emailIntent.setType("image/png");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
        break;
      case android.R.id.home: {
        return mDrawerToggle.onOptionsItemSelected(item);

      }
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
        && keyCode == KeyEvent.KEYCODE_BACK
        && event.getRepeatCount() == 0) {
      // Take care of calling this method on earlier versions of
      // the platform where it doesn't exist.
      getFragmentManager().popBackStack();
      onBackPressed();
    }

    return super.onKeyDown(keyCode, event);
  }

  @Override
  public void onBackPressed() {
    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mDrawerLayout.closeDrawer(GravityCompat.START);
    }
  }

  @Override
  protected void onSaveInstanceState(final Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(NAV_ITEM_ID, mNavItemId);
  }

  private void takeScreenshot() {
    Date now = new Date();

    try {
      // image naming and path to include sd card appending name you
      // choose for file
      String mPath = Environment.getExternalStorageDirectory() + "/screenshot.png";

      // create bitmap screen capture
      View v1 = getWindow().getDecorView().getRootView();
      v1.setDrawingCacheEnabled(true);
      Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
      v1.setDrawingCacheEnabled(false);

      File imageFile = new File(mPath);

      FileOutputStream outputStream = new FileOutputStream(imageFile);
      int quality = 100;
      bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
      outputStream.flush();
      outputStream.close();

    } catch (Throwable e) {
      // Several error may come out with file handling or OOM
      e.printStackTrace();
    }
  }


  private void startPlaying() {
    player.prepareAsync();

    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      public void onPrepared(MediaPlayer mp) {
        player.start();
        hideProgressDialog();
      }
    });

  }

  private void stopPlaying() {
    if (player.isPlaying()) {
      player.stop();
      player.release();
      MenuItem item = globalmenu.findItem(R.id.radio);
      item.setIcon(R.drawable.ic_play);
      initializeMediaPlayer();
    }

  }

  private void initializeMediaPlayer() {
    player = new MediaPlayer();
    try {
      player.setDataSource(getResources().getString(R.string.link_radio));
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalStateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

      public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //playSeekBar.setSecondaryProgress(percent);
        Log.i("Buffering", "" + percent);

      }
    });
  }

  private void showProgressDialog() {
    pDialog = new ProgressDialog(context);
    pDialog.setCancelable(false);
    pDialog.setMessage("Buffering ...");
    if (!pDialog.isShowing()) {
      pDialog.show();
    }

  }

  private void hideProgressDialog() {

    if (pDialog.isShowing())
      pDialog.dismiss();
  }


  public ServerRequest getServerRequestSend() {
    return ServerRequest.getInstance(context, getWindow().getDecorView());
  }

  public static Toast currentToast;
  /**
   * Use a custom display for Toasts.
   *
   * @param message
   */
  public static void customToast(Context context, String message) {
    // Avoid creating a queue of toasts
    if (currentToast != null) {
      // Dismiss the current showing Toast
      currentToast.cancel();
    }
    //Retrieve the layout Inflater
    LayoutInflater inflater = (LayoutInflater)
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //Assign the custom layout to view
    View layout = inflater.inflate(R.layout.custom_toast, null);
    //Return the application context
    currentToast = new Toast(context.getApplicationContext());
    //Set toast gravity to center
    currentToast.setGravity(Gravity.BOTTOM | Gravity.LEFT|Gravity.FILL_HORIZONTAL, 0, 0);
    //Set toast duration
    currentToast.setDuration(Toast.LENGTH_LONG);
    //Set the custom layout to Toast
    currentToast.setView(layout);
    //Get the TextView for the message of the Toast
    TextView text = (TextView) layout.findViewById(R.id.text);
    //Set the custom text for the message of the Toast
    text.setText(message);
    //Display toast
    currentToast.show();
    // Check if the layout is visible - just to be sure
    if (layout != null) {
      // Touch listener for the layout
      // This will listen for any touch event on the screen
      layout.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          // No need to check the event, just dismiss the toast if it is showing
          if (currentToast != null) {
            currentToast.cancel();
            // we return True if the listener has consumed the event
            return true;
          }
          return false;
        }
      });
    }
  }

}
