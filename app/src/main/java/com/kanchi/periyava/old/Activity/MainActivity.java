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
package com.kanchi.periyava.old.Activity;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/*
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.TrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.Util;
import com.google.gson.Gson;
*/
import com.kanchi.periyava.old.Component.CircularImageView;
import com.kanchi.periyava.old.Fragments.About;
import com.kanchi.periyava.old.Fragments.Japam;
import com.kanchi.periyava.old.Fragments.Radio;
import com.kanchi.periyava.old.Fragments.WebPage;
import com.kanchi.periyava.old.Interface.DialogActionCallback;
import com.kanchi.periyava.old.Model.AlarmReceiver;
import com.kanchi.periyava.old.Model.ConstValues;
import com.kanchi.periyava.old.Model.GeneralSetting;
import com.kanchi.periyava.old.Model.MessageHandler;
import com.kanchi.periyava.old.Model.NetworkConnection;
import com.kanchi.periyava.old.Model.PreferenceData;
import com.kanchi.periyava.old.Model.UserProfile;
import com.kanchi.periyava.R;
import com.kanchi.periyava.old.Service.ServerRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends MBaseActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private static final long DRAWER_CLOSE_DELAY_MS = 350;
  private static final String NAV_ITEM_ID = "navItemId";
  public static final String WEBURL = "URL";

  private final com.kanchi.periyava.old.Fragments.Dashboard mDashboard = new com.kanchi.periyava.old.Fragments.Dashboard();
  private final WebPage mWebPage = new WebPage();
  private final About mAbout = new About();
  //private ExoPlayer player;
  private final Handler mainHandler = new Handler();


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
  public Menu menu;
  private PendingIntent pendingIntent;
  private Uri uri;
  android.os.Message msg;

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

  };


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
    if (android.os.Build.VERSION.SDK_INT > 9) {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);
    }
    initComponents(savedInstanceState);

    /* Retrieve a PendingIntent that will perform a broadcast */
    Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

    UserProfile.getInstance();
    PreferenceData.getInstance(context);
    GeneralSetting.getInstance();

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

      case R.id.privacy:
        msg.what = ConstValues.PRIVACY_POLICY;
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
          getFlowHandler().sendMessage(msg);

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

  public void RunRadio(String type, MenuItem item, Uri uri) {
    Log.d(String.valueOf(item), "menuitem Runradio");
    Log.d(String.valueOf(uri), "uriRunradio");
    if (radiostate == false) {
      // initialize buttons

      Log.d("inside Runradio", "function");
      Log.d(String.valueOf(uri), "URI parameter");
      startPlaying(type, uri);
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
        // RunRadio(item);
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

  public void startPlaying(final String type, Uri uri) {

   /* final int numRenderers = 2;

    final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    final int BUFFER_SEGMENT_COUNT = 256;
    Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);

    final Handler handler = new Handler();
    String userAgent = Util.getUserAgent(this, "ExoPlayerDemo");

    // Build the video and audio renderers.
    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(handler, null);
    DataSource dataSource = new DefaultUriDataSource(context, bandwidthMeter, userAgent);
    ExtractorSampleSource sampleSource = new ExtractorSampleSource(
        Uri.parse(String.valueOf(uri)), dataSource, allocator,
        BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);


    // Build the track renderers
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
            getFlowHandler().sendMessage(msg);
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

        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.RADIOSELECTOR;
        msg.obj = (String) type;
        msg.arg1 = 1;
        getFlowHandler().sendMessage(msg);
      }
    });
*/
  }

  void stopPlaying() {
    // Don't forget to release when done!
   /* if (player != null) {
      player.release();
      cancelAlaram();
      radiostate = false;
      setRadioScreenButtonState();
    }*/
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
    if (pDialog != null) {
      if (pDialog.isShowing())
        pDialog.dismiss();
    }
  }


  //Alaram handling
  public void startAlaram() {
    /*
     * AlarmManager alarmManager = (AlarmManager)
		 * context.getSystemService(Context.ALARM_SERVICE); Intent intent = new
		 * Intent(context, AlarmReceiver.class); PendingIntent pendingIntent =
		 * PendingIntent.getBroadcast(context, 0, intent, 0);
		 * alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
		 * System.currentTimeMillis(), 6000 * 5, pendingIntent);
		 * //Toast.makeText(this, "Alarm Started", Toast.LENGTH_SHORT).show();
		 */
  }

  public void cancelAlaram() {
    /*
		 * AlarmManager alarmManager = (AlarmManager)
		 * context.getSystemService(Context.ALARM_SERVICE); Intent intent = new
		 * Intent(context, AlarmReceiver.class); PendingIntent pendingIntent =
		 * PendingIntent.getBroadcast(context, 0, intent, 0);
		 * alarmManager.cancel(pendingIntent);
		 *
		 * Radio radio = (Radio) getFragmentManager()
		 * .findFragmentByTag(Radio.TAG); if (radio != null) {
		 * radio.setPlaylist(""); } //Toast.makeText(this, "Alarm Canceled",
		 * Toast.LENGTH_SHORT).show();
		 */
  }

  public Handler getFlowHandler() {
    return MessageHandler.getInstance(this).getFlowHandler();
  }
}
