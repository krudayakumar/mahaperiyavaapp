package com.mahaperivaya.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;
import com.mahaperivaya.SendRequest.SendRegister;

/**
 * Created by m84098 on 10/4/15.
 */
public class Registration extends AppBaseFragement {
  public static String TAG = "Registration";
  private EditText username, email, password, confirmpassword;
  private FloatingActionButton next;
  private ImageView image;
  private Context context;
  View rootView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.registration, container, false);
    getActivity().setTitle(getResources().getString(R.string.lbl_registration));
    setHasOptionsMenu(true);
    initComponent();
    getBaseActivity().getScreenMode(false);
    return rootView;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    getBaseActivity().setMenuOption(menu);
    getBaseActivity().getMenuVisible(MainActivity.MenuOptions.FEEDBACK);
    getBaseActivity().getMenuVisible(MainActivity.MenuOptions.SAVE);
  }

  @Override
  public void onResume() {
    super.onResume();

    getView().setFocusableInTouchMode(true);
    getView().requestFocus();
    getView().setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
          // handle back button's click listener
          android.os.Message msg = android.os.Message.obtain();
          msg.what = ConstValues.LOGIN;
          getBaseActivity().getFlowHandler().sendMessage(msg);
          return true;
        }
        return false;
      }


    });
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save: {
        TextInputLayout textInputLayout;
        //email id
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.layoutEmailId);
        if (TextUtils.isEmpty(email.getText())) {
          textInputLayout.setError(
              getResources().getString(R.string.err_field_should_not_be_empty));
          break;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
          textInputLayout
              .setError(getResources().getString(R.string.err_invalid_email_id));
          break;
        }

        //username
        textInputLayout.setError(null);
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.layoutPersonName);
        if (TextUtils.isEmpty(username.getText())) {

          textInputLayout.setError(
              getResources().getString(R.string.err_field_should_not_be_empty));
          break;
        }

        //Password
        textInputLayout.setError(null);
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.layoutPassword);
        if (TextUtils.isEmpty(password.getText())) {

          textInputLayout.setError(
              getResources().getString(R.string.err_field_should_not_be_empty));
          break;
        }
        //Confirm password.
        textInputLayout.setError(null);
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.layoutConfirmPassword);
        if (TextUtils.isEmpty(confirmpassword.getText())) {

          textInputLayout.setError(
              getResources().getString(R.string.err_field_should_not_be_empty));
          break;
        }

        if (confirmpassword.getText().toString().equals(password.getText().toString()) == false) {
          textInputLayout.setError(
              getResources().getString(R.string.err_password_not_same));
          break;
        }
        textInputLayout.setError(null);
        SendRegister sendRegister = new SendRegister();
        sendRegister.emailid = email.getText().toString();
        sendRegister.password = password.getText().toString();
        sendRegister.username = username.getText().toString();

        //Sending the Message
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.REGISTER_SERVER_REQUEST;
        msg.obj = (Object) sendRegister;
        getBaseActivity().getFlowHandler().sendMessage(msg);


        break;
      }
      default:
        break;
    }

    return false;
  }


  private void initComponent() {
    username = (EditText) rootView.findViewById(R.id.edPersonName);
    email = (EditText) rootView.findViewById(R.id.edEmailId);
    password = (EditText) rootView.findViewById(R.id.edPassword);
    confirmpassword = (EditText) rootView.findViewById(R.id.edConfirmPassword);

  }
}
