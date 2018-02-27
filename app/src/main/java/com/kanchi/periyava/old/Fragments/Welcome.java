package com.kanchi.periyava.old.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kanchi.periyava.BuildConfig;
import com.kanchi.periyava.old.Interface.ServerCallback;
import com.kanchi.periyava.old.Model.ConstValues;
import com.kanchi.periyava.old.Model.GeneralSetting;
import com.kanchi.periyava.old.Model.PreferenceData;
import com.kanchi.periyava.old.Model.UserProfile;
import com.kanchi.periyava.R;
import com.kanchi.periyava.old.ReceiveRequest.ReceiveGeneralSettings;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by m84098 on 9/3/15.
 */
public class Welcome extends AppBaseFragement {
  public static String TAG = "Welcome";
  Context context;
  View rootView;

  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.welcome, container, false);
    context = container.getContext();
    final String versionName = BuildConfig.VERSION_NAME;
    final String buildDate = new SimpleDateFormat("EEE, MMM dd yyyy hh:mm:ss").format(new Date(BuildConfig.TIMESTAMP));
    System.out.println(new SimpleDateFormat("yyyymmddhhmmss").format(new Date(BuildConfig.TIMESTAMP)));
    ((TextView) (rootView.findViewById(R.id.version))).setText((BuildConfig.DEBUG == true ? "Debug" : "Release") + " Ver " + versionName);
    final Thread splashThread = new Thread() {
      public void run() {

        try {

          // Thread will sleep for 2 seconds
          sleep(2 * 1000);
          android.os.Message msg = Message.obtain();
          msg.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;
          getFlowHandler().sendMessage(msg);


        } catch (Exception e) {
          Log.d(TAG, e.getMessage().toString());
        }
      }
    };
    ServerCallback serverCallback = new ServerCallback() {
      @Override
      public void onSuccess(JSONObject response) {

        ReceiveGeneralSettings generalReceiveRequest = new Gson().fromJson(response.toString(),
            ReceiveGeneralSettings.class);


        if (generalReceiveRequest.isSuccess()) {
          GeneralSetting.getGeneralSetting().radioplaylisturl = generalReceiveRequest.data.radioplaylisturl;
          GeneralSetting.getGeneralSetting().radiourl = generalReceiveRequest.data.radiourl;
          GeneralSetting.getGeneralSetting().radiourl_india = generalReceiveRequest.data.radiourl_india;
          GeneralSetting.getGeneralSetting().radiourl_others = generalReceiveRequest.data.radiourl_others;
          GeneralSetting.getGeneralSetting().direct_radiourl_india = generalReceiveRequest.data.direct_radiourl_india;
          GeneralSetting.getGeneralSetting().direct_radiourl_others = generalReceiveRequest.data.direct_radiourl_others;
          GeneralSetting.getGeneralSetting().feedbackemailid = generalReceiveRequest.data.feedbackemailid;
          GeneralSetting.getGeneralSetting().version = generalReceiveRequest.data.version;
          GeneralSetting.getGeneralSetting().forceupgrade = generalReceiveRequest.data.forceupgrade;
          GeneralSetting.getGeneralSetting().isappmsg = generalReceiveRequest.data.isappmsg;
          GeneralSetting.getGeneralSetting().message = generalReceiveRequest.data.message;

          GeneralSetting.getGeneralSetting().maintenancemode = generalReceiveRequest.data.maintenancemode;
          GeneralSetting.getGeneralSetting().playstoreurl = generalReceiveRequest.data.playstoreurl;
          Log.d(TAG, "forceupgrade:" + GeneralSetting.getGeneralSetting().forceupgrade);
          Log.d(TAG, "maintenancemode:" + GeneralSetting.getGeneralSetting().maintenancemode);


          if (TextUtils.isEmpty((String) PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.DIRECT_RADIOURL_INDIA.toString(), new String()))) {
            PreferenceData.getInstance(context).setValue(
                PreferenceData.PREFVALUES.DIRECT_RADIOURL_INDIA.toString(), generalReceiveRequest.data.direct_radiourl_india);

          }

          if (TextUtils.isEmpty((String) PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.DIRECT_RADIOURL_OTHERS.toString(), new String()))) {
            PreferenceData.getInstance(context).setValue(
                PreferenceData.PREFVALUES.DIRECT_RADIOURL_OTHERS.toString(), generalReceiveRequest.data.direct_radiourl_others);

          }


          if (GeneralSetting.getGeneralSetting().forceupgrade && GeneralSetting.getGeneralSetting().version.equalsIgnoreCase(versionName) == false) {
            showUpgradeMaintanence(true);
          } else if (GeneralSetting.getGeneralSetting().maintenancemode) {
            showUpgradeMaintanence(false);
          } else {
            if (GeneralSetting.getGeneralSetting().isappmsg) {
              Message msg = Message.obtain();
              msg.what = ConstValues.SHOW_MSG;
              msg.obj = TextUtils.isEmpty(GeneralSetting.getGeneralSetting().message) ? "" : GeneralSetting.getGeneralSetting().message;
              getFlowHandler().sendMessage(msg);
            }
            splashThread.start();
          }


        } else {
          new Gson().toJson(generalReceiveRequest.data).toString();
          String strData = "";
          strData = (String) PreferenceData.getInstance(context).getValue(PreferenceData.PREFVALUES.GENERAL_SETTINGS.toString(), (Object) strData);
          if (!TextUtils.isEmpty(strData)) {
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
        msg.what = ConstValues.ERROR_INTERNET_CONNECTION;
        getFlowHandler().sendMessage(msg);

      }
    };

    //Sending the Message
    android.os.Message msg = android.os.Message.obtain();
    msg.what = ConstValues.GENERAL_SETTING_SERVER_REQUEST;
    msg.obj = (Object) serverCallback;
    getFlowHandler().sendMessage(msg);


    initCompontents();

   /* Message msg = Message.obtain();
    msg.what = ConstValues.GENERAL_SETTING_SERVER_REQUEST;
    getFlowHandler().sendMessage(msg);*/
    return rootView;
  }


  private void initCompontents() {


    ((TextView) rootView.findViewById(R.id.tvSignInRegister))
        .setText(Html.fromHtml(getResources().getString(R.string.lbl_already_login)));
    rootView.findViewById(R.id.tvSignInRegister).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setRegistrationClick(v);

      }
    });

    rootView.findViewById(R.id.later).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setRegistrationClick(v);

      }
    });

  }

  private void showUpgradeMaintanence(final boolean isUpgrade) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
    String strOkButton = isUpgrade ? "Goto Play Store" : "Close";

    builder.setPositiveButton(strOkButton,
        new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            if (isUpgrade) {
              String strPlayStoreUrl = getBaseActivity().getResources().getString(R.string.link_playstore);
              Intent i = new Intent(android.content.Intent.ACTION_VIEW);
              if (!TextUtils.isEmpty(GeneralSetting.getGeneralSetting().playstoreurl)) {
                strPlayStoreUrl = GeneralSetting.getGeneralSetting().playstoreurl;
              }
              i.setData(Uri.parse(strPlayStoreUrl));
              startActivity(i);
            } else {
              getBaseActivity().finish();
            }
            dialog.dismiss();

          }
        });
    builder.setTitle(getResources().getString(R.string.app_name));
    builder.setMessage(isUpgrade ? "New Version Available" : "Application in Maintenance Mode");
    AlertDialog alert = builder.create();
    alert.setCancelable(false);
    alert.show();
    alert.getButton(alert.BUTTON_POSITIVE)
        .setTextColor(getResources().getColor(R.color.primary));
  }

  private void setRegistrationClick(View v) {
    android.os.Message msg = Message.obtain();
    UserProfile.getInstance().clearAll();
    switch (v.getId()) {
      case R.id.tvSignInRegister:
        msg.what = ConstValues.LOGIN;
        break;
      case R.id.later:
        msg.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;
        break;
    }
    getFlowHandler().sendMessage(msg);

  }



}
