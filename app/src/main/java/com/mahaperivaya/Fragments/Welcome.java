package com.mahaperivaya.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;

/**
 * Created by m84098 on 9/3/15.
 */
public class Welcome extends AppBaseFragement {
  public static String TAG = "Welcome";
  Context context;
  View rootView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.welcome, container, false);
    context = container.getContext();

    initCompontents();
   /* Message msg = Message.obtain();
    msg.what = ConstValues.GENERAL_SETTING_SERVER_REQUEST;
    getBaseActivity().getFlowHandler().sendMessage(msg);*/
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
    MainActivity.getFlowHandler().sendMessage(msg);

  }
}
