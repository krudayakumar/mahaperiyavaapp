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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mahaperivaya.Component.CircularImageView;
import com.mahaperivaya.Fragments.About;
import com.mahaperivaya.Fragments.Dashboard;
import com.mahaperivaya.Fragments.Japam;
import com.mahaperivaya.Fragments.Login;
import com.mahaperivaya.Fragments.Registration;
import com.mahaperivaya.Fragments.SatsangList;
import com.mahaperivaya.Fragments.SetPassword;
import com.mahaperivaya.Fragments.WebPage;
import com.mahaperivaya.Fragments.Welcome;
import com.mahaperivaya.Interface.DialogActionCallback;
import com.mahaperivaya.Interface.ServerCallback;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.PreferenceData;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.GeneralReceiveRequest;
import com.mahaperivaya.ReceiveRequest.ReceiveLogin;
import com.mahaperivaya.SendRequest.SendForgotPassword;
import com.mahaperivaya.SendRequest.SendLogin;
import com.mahaperivaya.SendRequest.SendPasswordReset;
import com.mahaperivaya.SendRequest.SendRegister;
import com.mahaperivaya.Service.ServerRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
  static boolean radiostate = false;
  public static Menu globalmenu;
  // Progress dialog
  private ProgressDialog pDialog;
  private NavigationView navigationView;
  private static Toolbar toolbar;

  public static Menu getGlobalMenu() {
    return globalmenu;
  }

  public static void setGlobalMenu(Menu menu) {
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
    userimage = (CircularImageView) findViewById(R.id.userimage);

    navigate(mNavItemId);


  }

  public void featuresEnable() {
    if (UserProfile.getUserProfile().isLoggedIn) {
      username.setText(UserProfile.getUserProfile().username);
      emailid.setText(UserProfile.getUserProfile().emailid);
      userimage.setVisibility(View.GONE);
      navigationView.getMenu().findItem(R.id.signin).setVisible(false);
      navigationView.getMenu().findItem(R.id.signout).setVisible(true);
    } else {
      username.setVisibility(View.GONE);
      emailid.setVisibility(View.GONE);
      userimage.setVisibility(View.GONE);
      navigationView.getMenu().findItem(R.id.signin).setVisible(true);
      navigationView.getMenu().findItem(R.id.signout).setVisible(false);
      navigationView.getMenu().findItem(R.id.japam).setVisible(false);
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


  public static enum MenuOptions {
    SAVE(0),
    RADIO(1),
    FEEDBACK(2);
    private final int id;

    private MenuOptions(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
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


        //Activity Starting
        switch (msg.what) {
          case ConstValues.WELCOME:
            setDrawerState(false);
            getVisibilityToolBar(false);
            showFragment(new Welcome(), null, R.id.content, true, Welcome.TAG);
            break;
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
                  UserProfile.getUserProfile().isjoinedjapam = receiveLogin.data.isjoinedjapam;
                  UserProfile.getUserProfile().isjoinedsatsang = receiveLogin.data.isjoinedsatsang;
                  UserProfile.getUserProfile().ispasswordreset = receiveLogin.data.ispasswordreset;
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
            ShowSnackBar(context, getWindow().getDecorView(), receiveLogin.data.message, null, null);
            break;
          }

          case ConstValues.DASHBOARD_WITHOUT_LOGIN: {
            setDrawerState(true);
            getVisibilityToolBar(true);
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
                ServerRequest.SendServerRequest.SET_PASSWORD, (SendPasswordReset) msg.obj, new ServerCallback() {
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
            ShowSnackBar(context, getWindow().getDecorView(), getResources().getString(R.string.msg_successfully_password_saved), null, null);
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.DASHBOARD_LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            break;
          }
          case ConstValues.SET_PASSWORD_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.data.message, null, null);
            break;
          }
          //Registration Flow
          case ConstValues.REGISTER:
            showFragment(new Registration(), null, R.id.content, true, Registration.TAG);
            break;

          case ConstValues.REGISTER_SERVER_REQUEST: {
            getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.REGISTER, (SendRegister) msg.obj, new ServerCallback() {
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
            ShowSnackBar(context, getWindow().getDecorView(), getResources().getString(R.string.msg_successfully_registred), null, null);
            Message msgtmp = Message.obtain();
            msgtmp.what = ConstValues.LOGIN;
            getFlowHandler().sendMessage(msgtmp);
            break;
          }
          case ConstValues.REGISTER_ERROR: {
            GeneralReceiveRequest generalReceiveRequest = new Gson().fromJson(((JSONObject) msg.obj).toString(), GeneralReceiveRequest.class);
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.data.message, null, null);
          }
          break;

          case ConstValues.FORGOT_PASSWORD: {
            getServerRequestSend().executeRequest(
                ServerRequest.SendServerRequest.FORGOT_PASSWORD, (SendForgotPassword) msg.obj, new ServerCallback() {
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
            ShowSnackBar(context, getWindow().getDecorView(), generalReceiveRequest.data.message, null, null);
          }
          break;

          case ConstValues.DEIVATHIN_KURAL: {
            setTitle(getResources().getString(R.string.lbl_devithin_kural));
            Bundle bundle = new Bundle();
            bundle.putString(WEBURL, getResources().getString(R.string.link_devithinkural));
            showFragment(new WebPage(), bundle, R.id.content, true, WebPage.TAG);
          }
          break;

          case ConstValues.SATSANG_LIST_LOGIN:
          case ConstValues.SATSANG_LIST_WITHOUT_LOGIN: {
              showFragment(new SatsangList(), null, R.id.content, true, SatsangList.TAG);
          }
          break;

          //Default Error
          case ConstValues.ERROR_DEFAULT:
            ShowSnackBar(context, getWindow().getDecorView(), getResources().getString(R.string.err_default), null, null);
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    setGlobalMenu(menu);
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

      case R.id.devithinkural:
        msg.what = ConstValues.DEIVATHIN_KURAL;
        getFlowHandler().sendMessage(msg);
        break;
      case R.id.satsang:
        if (UserProfile.getUserProfile().isLoggedIn) {
          msg.what = ConstValues.SATSANG_LIST_LOGIN;

        } else {
          msg.what = ConstValues.SATSANG_LIST_WITHOUT_LOGIN;

        }
        getFlowHandler().sendMessage(msg);

        break;
      case R.id.japam:
        if (UserProfile.getUserProfile().isLoggedIn) {

        }
        super.showFragment(new Japam(), null, R.id.content, true, Japam.TAG);
        break;
      case R.id.radio:
        item = globalmenu.findItem(R.id.radio);
        item.setVisible(true);
        break;
      case R.id.about:
        super.showFragment(mAbout, null, R.id.content, true, mAbout.TAG);
        break;
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

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {

    switch (item.getItemId()) {
      case R.id.radio:

        if (radiostate == false) {
          startPlaying();
          radiostate = true;
          showProgressDialog();
          item.setIcon(R.drawable.ic_pause);
        } else {
          stopPlaying();
          radiostate = false;
          item.setIcon(R.drawable.ic_play);
        }
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
            new String[]{getResources().getString(R.string.feedback_emailid)});
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
}
