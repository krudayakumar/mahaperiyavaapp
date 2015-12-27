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
package com.kanchi.periyava.Activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioTrack;
import android.media.MediaCodec;
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
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.FrameworkSampleSource;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.SampleSource;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.VideoSurfaceView;
import com.google.gson.Gson;
import com.kanchi.periyava.BuildConfig;
import com.kanchi.periyava.Component.CircularImageView;
import com.kanchi.periyava.Fragments.About;
import com.kanchi.periyava.Fragments.AboutAcharayas;
import com.kanchi.periyava.Fragments.Dashboard;
import com.kanchi.periyava.Fragments.HelpFeedback;
import com.kanchi.periyava.Fragments.Japam;
import com.kanchi.periyava.Fragments.Login;
import com.kanchi.periyava.Fragments.NewSatsang;
import com.kanchi.periyava.Fragments.PhotoVideoBook;
import com.kanchi.periyava.Fragments.Radio;
import com.kanchi.periyava.Fragments.Registration;
import com.kanchi.periyava.Fragments.SatsangList;
import com.kanchi.periyava.Fragments.SetPassword;
import com.kanchi.periyava.Fragments.WebPage;
import com.kanchi.periyava.Fragments.Welcome;
import com.kanchi.periyava.Interface.DialogActionCallback;
import com.kanchi.periyava.Interface.ServerCallback;
import com.kanchi.periyava.Model.AlarmReceiver;
import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.Model.GeneralSetting;
import com.kanchi.periyava.Model.NetworkConnection;
import com.kanchi.periyava.Model.PreferenceData;
import com.kanchi.periyava.Model.RadioStatus;
import com.kanchi.periyava.Model.UserProfile;
import com.kanchi.periyava.R;
import com.kanchi.periyava.ReceiveRequest.GeneralReceiveRequest;
import com.kanchi.periyava.ReceiveRequest.ReceiveGeneralSettings;
import com.kanchi.periyava.ReceiveRequest.ReceiveJapamDetails;
import com.kanchi.periyava.ReceiveRequest.ReceiveJoinSatsang;
import com.kanchi.periyava.ReceiveRequest.ReceiveLogin;
import com.kanchi.periyava.ReceiveRequest.ReceiveSatsangList;
import com.kanchi.periyava.SendRequest.SendLogin;
import com.kanchi.periyava.Service.ServerRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends MBaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private static final long DRAWER_CLOSE_DELAY_MS = 350;
  private static final String NAV_ITEM_ID = "navItemId";
  public static final String WEBURL = "URL";

  private final com.kanchi.periyava.Fragments.Dashboard mDashboard = new com.kanchi.periyava.Fragments.Dashboard();
  private final WebPage mWebPage = new WebPage();
  private final About mAbout = new About();
  private ExoPlayer player;
  private final Handler mainHandler = new Handler();
  private TrackRenderer videoRenderer;
  private TrackRenderer audioRenderer;

  private VideoSurfaceView surfaceView;
  private boolean isPlaying = false;
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
  private PendingIntent pendingIntent;




  public enum LANGUAGE {
    ENGLISH("en"),
    TAMIL("ta");
    String language;

    private LANGUAGE(String _language) {
      this.language = _language;
    }

    public String getLanguage() {
      return this.language;
    }

  }

  ;


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
    loadLanguage(LANGUAGE.ENGLISH);
    setContentView(R.layout.activity_main);
    context = this;

    initComponents(savedInstanceState);

    /* Retrieve a PendingIntent that will perform a broadcast */
    Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

    UserProfile.getInstance();
    PreferenceData.getInstance(context);
    GeneralSetting.getInstance();

    flowCallBackHandler();
    Message msg = Message.obtain();
    msg.what = ConstValues.WELCOME;
    getFlowHandler().sendMessage(msg);


    //preparePlayer();

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


  public void loadLanguage(LANGUAGE language) {

    Locale locale = new Locale(language.getLanguage());
    Locale.setDefault(locale);
    Configuration config = new Configuration();
    config.locale = locale;
    getBaseContext().getResources().updateConfiguration(config,
        getBaseContext().getResources().getDisplayMetrics());

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
            bundle = new Bundle();
            setTitle(getResources().getString(R.string.lbl_about_peetham));
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.about_peetham);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_peetam_content);
            showFragment(new About(), bundle, R.id.content, true, About.TAG);
            break;

          case ConstValues.ABOUT_ACHARAYAS: {
            setTitle(getResources().getString(R.string.lbl_about_acharayas));
            showFragment(new AboutAcharayas(), bundle, R.id.content, true, AboutAcharayas.TAG);
          }
          break;
          case ConstValues.ABOUT_ACHARAYAS_68: {
            setTitle(getResources().getString(R.string.lbl_about_acharayas));
            bundle = new Bundle();
            bundle.putInt(ConstValues.CONST_ABOUT_TITLE, R.string.lbl_about_acharaya68_fullname);
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.acharaya68);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_acharaya68_content);
            showFragment(new About(), bundle, R.id.content, true, About.TAG);
            break;
          }
          case ConstValues.ABOUT_ACHARAYAS_69: {
            setTitle(getResources().getString(R.string.lbl_about_acharayas));
            bundle = new Bundle();
            bundle.putInt(ConstValues.CONST_ABOUT_TITLE, R.string.lbl_about_acharaya69_fullname);
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.acharaya69);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_acharaya69_content);
            showFragment(new About(), bundle, R.id.content, true, About.TAG);
            break;
          }
          case ConstValues.ABOUT_ACHARAYAS_70: {
            setTitle(getResources().getString(R.string.lbl_about_acharayas));
            bundle = new Bundle();
            bundle.putInt(ConstValues.CONST_ABOUT_TITLE, R.string.lbl_about_acharaya70_fullname);
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.acharaya70);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_acharaya70_content);
            showFragment(new About(), bundle, R.id.content, true, About.TAG);
            break;
          }
          case ConstValues.ABOUT_US: {
            setTitle(getResources().getString(R.string.lbl_about_us));
            bundle = new Bundle();
            bundle.putInt(ConstValues.CONST_ABOUT_IMAGE, R.drawable.about_us);
            bundle.putInt(ConstValues.CONST_ABOUT_CONTENT, R.string.lbl_about_content);
            showFragment(new About(), bundle, R.id.content, true, About.TAG);
          }
          break;

          case ConstValues.HELP_FEEDBACK: {
            setTitle(getResources().getString(R.string.lbl_feedback));
           /* bundle = new Bundle();
            bundle.putString(WEBURL, getResources().getString(R.string.base_url) + "general_help.php");
            showFragment(new HelpFeedback(), bundle, R.id.content, true, HelpFeedback.TAG);
            */

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + GeneralSetting.getInstance().feedbackemailid));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback" + "(" + (BuildConfig.DEBUG == true ? "Debug" : "Release") + " Ver " + BuildConfig.VERSION_NAME + ")");
            startActivity(intent);
          }
          break;
          case ConstValues.RADIO: {
            setTitle(getResources().getString(R.string.lbl_online_radio));
            showFragment(new Radio(), null, R.id.content, true, Radio.TAG);

            /*bundle = new Bundle();
            bundle.putString(WEBURL, getResources().getString(R.string.base_url)+"radio.php");
            showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);*/

            break;
          }
          case ConstValues.RADIO_RUN_STOP: {
            MenuItem actionRestart = (MenuItem) menu.findItem(R.id.radio);
            onOptionsItemSelected(actionRestart);
            break;
          }
          case ConstValues.RADIO_SCHEDULE_LIST: {
            getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.RADIO_SCHEDULE_LIST, null, (ServerCallback) msg.obj);

          }
          case ConstValues.RADIO_GET_PLAYLIST: {

            Radio radio = (Radio) ((Activity) (context)).getFragmentManager()
                .findFragmentByTag(Radio.TAG);
            if (MainActivity.radiostate && radio != null) {
              //Sending the Message
              getServerRequestSend().executeRequest(
                  ServerRequest.SendServerRequest.RADIO_STATUS, null, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                      RadioStatus radioStatus = new Gson().fromJson(response.toString(),
                          RadioStatus.class);

                      Radio radio = (Radio) getFragmentManager()
                          .findFragmentByTag(Radio.TAG);
                      if (radio != null) {
                        radio.setPlaylist(radioStatus.current_track.title);
                      }
                    }

                    @Override
                    public void onError(VolleyError error) {
                      error.printStackTrace();
                      Message msg = Message.obtain();
                      msg.what = ConstValues.ERROR_DEFAULT;
                      getFlowHandler().sendMessage(msg);
                    }
                  }, false);
            }
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
          case ConstValues.GENERAL_SETTING_SERVER_REQUEST: {
            getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.GENERAL_SETTINGS, null, (ServerCallback) msg.obj, false);
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
            radiostate = false;
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
        msg.what = ConstValues.HELP_FEEDBACK;
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
        if (!NetworkConnection.isConnected(context)) {
          msg.what = ConstValues.ERROR_INTERNET_CONNECTION;
          MainActivity.getFlowHandler().sendMessage(msg);

        } else {
          item = globalmenu.findItem(R.id.radio);
          item.setVisible(true);
          msg.what = ConstValues.RADIO;
          getFlowHandler().sendMessage(msg);
        }
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

  public void RunRadio(MenuItem item) {

    if (radiostate == false) {
      startPlaying();
      showProgressDialog();
      item.setIcon(R.drawable.ic_stop);
    } else {
      stopPlaying();
      item.setIcon(R.drawable.ic_play);
    }


  }

  private void setRadioScreenButtonState() {
    Radio frg = (Radio) getFragmentManager()
        .findFragmentByTag(Radio.TAG);
    if (frg != null) {
      frg.setRadioButtonState(radiostate);
    }
  }


  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {

    switch (item.getItemId()) {
      case R.id.radio:
        RunRadio(item);
        break;
      case R.id.share:
      case R.id.feedback:

        // TODO Auto-generated method stub
        takeScreenshot();
        Uri uri = null;
        try {
          File F = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
          uri = Uri.fromFile(F);
        } catch (Exception e) {
          e.printStackTrace();
        }
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (uri != null) {
          emailIntent.setType("image/png");
          emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        startActivity(Intent.createChooser(emailIntent, "Share Using..."));
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

  void startPlaying() {
    Uri uri = Uri.parse(getResources().getString(R.string.link_radio));
    final int numRenderers = 2;

    // Build the sample source
    SampleSource sampleSource =
        new FrameworkSampleSource(context, uri, /* headers */ null, numRenderers);
    FrameworkSampleSource frameworkSampleSource;


    // Build the track renderers
    TrackRenderer videoRenderer = new MediaCodecVideoTrackRenderer(sampleSource, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
    TrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource);

    // Build the ExoPlayer and start playback
    player = ExoPlayer.Factory.newInstance(numRenderers);
    player.prepare(audioRenderer);


    // Pass the surface to the video renderer.
    //exoPlayer.sendMessage(videoRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, surface);

    player.setPlayWhenReady(true);

    player.addListener(new ExoPlayer.Listener() {
      @Override
      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {


        switch (player.getPlaybackState()) {
          case ExoPlayer.STATE_READY:
            radiostate = true;
            startAlaram();
            android.os.Message msg = android.os.Message.obtain();
            msg.what = ConstValues.RADIO_GET_PLAYLIST;
            MainActivity.getFlowHandler().sendMessage(msg);
            hideProgressDialog();

            break;
          case ExoPlayer.STATE_ENDED:
            radiostate = true;
            cancelAlaram();
            break;
        }
        setRadioScreenButtonState();
      }

      @Override
      public void onPlayWhenReadyCommitted() {

      }

      @Override
      public void onPlayerError(ExoPlaybackException error) {
        ShowSnackBar(context, getWindow().getDecorView(), "Unable to Play Radio", null, null);
        hideProgressDialog();
        radiostate = false;
        cancelAlaram();
        error.getStackTrace();
      }
    });

  }

  void stopPlaying() {
    // Don't forget to release when done!
    player.release();
    cancelAlaram();
    radiostate = false;
    setRadioScreenButtonState();
  }





  public ServerRequest getServerRequestSend() {
    return ServerRequest.getInstance(context, getWindow().getDecorView());
  }


  //Progress Dialogbox
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


  //Alaram handling
  public void startAlaram() {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(context, AlarmReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 6000 * 5,
        pendingIntent);
    //Toast.makeText(this, "Alarm Started", Toast.LENGTH_SHORT).show();
  }

  public void cancelAlaram() {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(context, AlarmReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    alarmManager.cancel(pendingIntent);

    Radio radio = (Radio) getFragmentManager()
        .findFragmentByTag(Radio.TAG);
    if (radio != null) {
      radio.setPlaylist("");
    }
    //Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
  }

}
