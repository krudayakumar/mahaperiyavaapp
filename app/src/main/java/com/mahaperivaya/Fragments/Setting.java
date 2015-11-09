package com.mahaperivaya.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;

/**
 * Created by m84098 on 9/3/15.
 */
public class Setting extends AppBaseFragement implements View.OnClickListener {
  public static String TAG = "Setting";
  Button btnChangePassword;
  CheckBox chkSatang, chkJapam;
  View rootView;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.setting, container, false);


    getActivity().setTitle(getResources().getString(R.string.lbl_settings));
    init();
    return rootView;
  }

  private void init() {
    btnChangePassword = (Button) rootView.findViewById(R.id.changepassword);
    btnChangePassword.setOnClickListener(this);
    chkSatang = (CheckBox) rootView.findViewById(R.id.chkSatang);
    chkJapam = (CheckBox) rootView.findViewById(R.id.chkJapam);

    enableFeatureLayout();
  }

  private void enableFeatureLayout() {
    chkSatang.setChecked(UserProfile.getUserProfile().isjoinedsatsang);
    chkJapam.setChecked(UserProfile.getUserProfile().isjoinedjapam);
    /*chkSatang.setVisibility(UserProfile.getUserProfile().isjoinedsatsang == true ? View.VISIBLE : View.GONE);
    chkJapam.setVisibility(UserProfile.getUserProfile().isjoinedjapam == true ? View.VISIBLE : View.GONE);
    if(UserProfile.getUserProfile().isjoinedjapam == false && UserProfile.getUserProfile().isjoinedsatsang ==false) {
      rootView.findViewById(R.id.llfeatures).setVisibility(View.GONE);
    }else {
      rootView.findViewById(R.id.llfeatures).setVisibility(View.VISIBLE);
    }*/
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.changepassword: {
        //Sending the Message
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.SET_PASSWORD;
        getBaseActivity().getFlowHandler().sendMessage(msg);
      }
      break;
      case R.id.chkSatang:
        if(chkSatang.isChecked() == true) {

        }
        break;
      case R.id.chkJapam:
        break;


    }
  }
}
