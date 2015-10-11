package com.mahaperivaya.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.PreferenceData;
import com.mahaperivaya.R;
import com.mahaperivaya.SendRequest.SendForgotPassword;
import com.mahaperivaya.SendRequest.SendLogin;

/**
 * Created by m84098 on 9/3/15.
 */
public class Login extends AppBaseFragement {
  public static String TAG = "Login";
  private EditText password, email;
  private Button login;
  private ImageView image;
  private Context context;
  View rootView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.login, container, false);
    context = container.getContext();
    getActivity().setTitle(getResources().getString(R.string.action_login));
    getBaseActivity().getVisibilityToolBar(true);
    getBaseActivity().getScreenMode(false);
    setHasOptionsMenu(true);
    initComponent();
    return rootView;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Do something that differs the Activity's menu here
    super.onCreateOptionsMenu(menu, inflater);
    getBaseActivity().setMenuOption(menu);
    getBaseActivity().getMenuOption(MainActivity.MenuOptions.FEEDBACK);

  }


  private void initComponent() {
    email = (EditText) rootView.findViewById(R.id.edEmailId);
    password = (EditText) rootView.findViewById(R.id.edPassword);
    login = (Button) rootView.findViewById(R.id.login);
    String strEmailId = "";
    strEmailId = (String) PreferenceData.getInstance(context)
        .getValue(PreferenceData.PREFVALUES.EMAILID.toString(), strEmailId);
    if (!TextUtils.isEmpty(strEmailId)) {
      email.setText(strEmailId);
    }
    ((TextView) rootView.findViewById(R.id.register))
        .setText(Html.fromHtml(getResources().getString(R.string.lbl_do_you_want_register)));
    ((TextView) rootView.findViewById(R.id.forgotpassword))
        .setText(Html.fromHtml(getResources().getString(R.string.lbl_forgot_password)));
    rootView.findViewById(R.id.forgotpassword).setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        setRegistrationClick(v);

      }
    });
    rootView.findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        setRegistrationClick(v);

      }
    });

    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        TextInputLayout textInputLayout;
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.layoutEmailId);
        if (TextUtils.isEmpty(email.getText())) {
          textInputLayout.setError(
                  getResources().getString(R.string.err_field_should_not_be_empty));
          return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
          textInputLayout
                  .setError(getResources().getString(R.string.err_invalid_email_id));
          return;
        }
        textInputLayout.setError(null);
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.layoutPassword);
        if (TextUtils.isEmpty(password.getText())) {

          textInputLayout.setError(
                  getResources().getString(R.string.err_field_should_not_be_empty));
          return;
        }
        textInputLayout.setError(null);
        boolean ischecked = ((Switch) rootView.findViewById(R.id.remberid)).isChecked();
        if (ischecked) {
          PreferenceData.getInstance(context).setValue(
                  PreferenceData.PREFVALUES.EMAILID.toString(),
                  email.getText().toString());
        }

        SendLogin loginsendrequest = new SendLogin();
        loginsendrequest.emailid = email.getText().toString();
        loginsendrequest.password = password.getText().toString();

        //Sending the Message
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.LOGIN_SERVE_REQUEST;
        msg.obj = (Object) loginsendrequest;
        getBaseActivity().getFlowHandler().sendMessage(msg);


      }
    });
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
          msg.what = ConstValues.WELCOME;
          getBaseActivity().getFlowHandler().sendMessage(msg);
          return true;
        }
        return false;
      }


    });
  }

  private void setRegistrationClick(View v) {
    android.os.Message msg = android.os.Message.obtain();
    switch (v.getId()) {
      case R.id.forgotpassword:
        TextInputLayout textInputLayout;
        textInputLayout = (TextInputLayout) rootView.findViewById(R.id.layoutEmailId);
        if (TextUtils.isEmpty(email.getText())) {
          textInputLayout.setError(
              getResources().getString(R.string.err_field_should_not_be_empty));
          return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
          textInputLayout
              .setError(getResources().getString(R.string.err_invalid_email_id));
          return;
        }
        textInputLayout.setError(null);
        SendForgotPassword sendForgotPassword = new SendForgotPassword();
        sendForgotPassword.emailid = email.getText().toString();
        msg.what = ConstValues.FORGOT_PASSWORD;
        msg.obj = (Object) sendForgotPassword;
        break;
      case R.id.register:
        msg.what = ConstValues.REGISTER;
        break;
    }
    getBaseActivity().getFlowHandler().sendMessage(msg);

  }


}
