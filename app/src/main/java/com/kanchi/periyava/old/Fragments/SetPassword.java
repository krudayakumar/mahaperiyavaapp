package com.kanchi.periyava.old.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.kanchi.periyava.old.Activity.MainActivity;
import com.kanchi.periyava.old.Model.ConstValues;
import com.kanchi.periyava.old.Model.UserProfile;
import com.kanchi.periyava.R;
import com.kanchi.periyava.old.SendRequest.SendPasswordReset;

/**
 * Created by m84098 on 9/3/15.
 */
public class SetPassword extends AppBaseFragement {
  public static String TAG = "SetPassword";
  private static final int RC_SIGN_IN = 0;

  // Google client to communicate with Google

  private boolean signedInUser;

  private boolean mIntentInProgress;
  private EditText username, email, password, confirmpassword;
  private FloatingActionButton next;
  private ImageView image;
  private Context context;
  View rootView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.setpassword, container, false);
    getActivity().setTitle(getResources().getString(R.string.lbl_set_password));
    setHasOptionsMenu(true);
    initComponent();
    return rootView;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Do something that differs the Activity's menu here
    super.onCreateOptionsMenu(menu, inflater);
    getBaseActivity().setMenuOption(menu);
    getBaseActivity().getMenuVisible(MainActivity.MenuOptions.SAVE);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save:
        saveClick();
        break;
      default:
        break;
    }

    return false;
  }

  private void initComponent() {
    password = (EditText) rootView.findViewById(R.id.edPassword);
    confirmpassword = (EditText) rootView.findViewById(R.id.edConfirmPassword);

  }


  private void saveClick() {
    TextInputLayout textInputLayout;

    textInputLayout = (TextInputLayout) rootView.findViewById(R.id.layoutPassword);
    if (TextUtils.isEmpty(password.getText())) {

      textInputLayout.setError(
          getResources().getString(R.string.err_field_should_not_be_empty));
      return;
    }
    //Confirm password.
    textInputLayout.setError(null);
    textInputLayout = (TextInputLayout) rootView.findViewById(R.id.layoutConfirmPassword);
    if (TextUtils.isEmpty(confirmpassword.getText())) {

      textInputLayout.setError(
          getResources().getString(R.string.err_field_should_not_be_empty));
      return;
    }

    if (confirmpassword.getText().toString().equals(password.getText().toString()) == false) {
      textInputLayout.setError(
          getResources().getString(R.string.err_password_not_same));
      return;
    }
    textInputLayout.setError(null);
    SendPasswordReset sendPasswordReset = new SendPasswordReset();
    sendPasswordReset.profileid = UserProfile.getUserProfile().profileid;
    sendPasswordReset.password = password.getText().toString();

    android.os.Message msg = android.os.Message.obtain();
    msg.what = ConstValues.SET_PASSWORD_SERVER_REQUEST;
    msg.obj = (Object) sendPasswordReset;
    getFlowHandler().sendMessage(msg);

  }
}


