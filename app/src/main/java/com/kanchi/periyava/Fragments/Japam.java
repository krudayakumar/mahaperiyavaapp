package com.kanchi.periyava.Fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kanchi.periyava.Activity.MainActivity;
import com.kanchi.periyava.Interface.ServerCallback;
import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.Model.UserProfile;
import com.kanchi.periyava.R;
import com.kanchi.periyava.ReceiveRequest.ReceiveJapamDetails;
import com.kanchi.periyava.SendRequest.SendUpdateJapamCount;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by m84098 on 9/3/15.
 */
public class Japam extends AppBaseFragement {
  public static String TAG = "Japam";

  TextView japam_count_over_all, japam_count_satsang, japam_count, japam_last_updated_date;
  EditText edCount, edDate;
  ArrayList<EditText> allControls = new ArrayList<EditText>();

  View rootView;

  FloatingActionButton next;
  Calendar japam_update_date = Calendar.getInstance();
  DatePickerDialog.OnDateSetListener japam_date = new DatePickerDialog.OnDateSetListener() {
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
      japam_update_date.set(Calendar.YEAR, year);
      japam_update_date.set(Calendar.MONTH, monthOfYear);
      japam_update_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
      updateDateLabel();
    }
  };

  private void updateDateLabel() {
    String myFormat = "MM/dd/yyyy"; // "dd/MMM/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    edDate.setText(sdf.format(japam_update_date.getTime()));

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.japam, container, false);
    getActivity().setTitle(getResources().getString(R.string.lbl_japam));
    setHasOptionsMenu(true);

    init();
    setEnableAllControls(false);
    return rootView;
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
          if (UserProfile.getUserProfile().isLoggedIn) {
            msg.what = ConstValues.DASHBOARD_LOGIN;
          } else {
            msg.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;
          }
          getBaseActivity().getFlowHandler().sendMessage(msg);
          return true;
        }
        return false;
      }


    });
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Do something that differs the Activity's menu here
    super.onCreateOptionsMenu(menu, inflater);
    getBaseActivity().setMenuOption(menu);
    getBaseActivity().getMenuVisible(MainActivity.MenuOptions.EDIT);
    //getBaseActivity().getMenuVisible(MainActivity.MenuOptions.SAVE);
  }

  private boolean isInRange(int a, int b, int c) {
    return b > a ? c >= a && c <= b : c >= b && c <= a;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.edit: {
        getBaseActivity().getMenuOption(MainActivity.MenuOptions.EDIT).setVisible(false);
        getBaseActivity().getMenuOption(MainActivity.MenuOptions.SAVE).setVisible(true);
        setEnableAllControls(true);

      }
      break;
      case R.id.save: {
        TextInputLayout layout = (TextInputLayout) getActivity()
            .findViewById(R.id.layoutCount);
        if (TextUtils.isEmpty(edCount.getText().toString())) {
          layout.setError(
              getResources().getString(R.string.err_field_should_not_be_empty));
          break;
        } else {
          layout.setError("");
        }
        if (!isInRange(1, 32767, Integer.parseInt(edCount.getText().toString()))) {
          layout.setError(getResources().getString(R.string.err_enter_right_count));
          break;
        } else {
          layout.setError("");
        }

        layout = (TextInputLayout) getActivity().findViewById(R.id.layoutDate);
        if (TextUtils.isEmpty(edDate.getText().toString())) {
          layout.setError(
              getResources().getString(R.string.err_field_should_not_be_empty));
          break;
        } else {
          layout.setError("");
        }

        SendUpdateJapamCount sendUpdateJapamCount = new SendUpdateJapamCount();
        sendUpdateJapamCount.japamcount = Integer.parseInt(edCount.getText().toString());
        sendUpdateJapamCount.japamupdatedate = edDate.getText().toString();
        sendUpdateJapamCount.profileid = UserProfile.getUserProfile().profileid;
        sendUpdateJapamCount.access_token = UserProfile.getUserProfile().access_token;
        sendUpdateJapamCount.satsangid = UserProfile.getUserProfile().satsangid;

        ServerCallback serverCallback = new ServerCallback() {

          @Override
          public void onSuccess(JSONObject response) {
            ReceiveJapamDetails receiveJapamDetails = new Gson().fromJson(response.toString(),
                ReceiveJapamDetails.class);
            android.os.Message msg = android.os.Message.obtain();
            msg.obj = response;
            if (receiveJapamDetails.isSuccess()) {
              UserProfile.getUserProfile().japam_count = receiveJapamDetails.data.japam_count;
              UserProfile.getUserProfile().japam_count_over_all = receiveJapamDetails.data.japam_count_over_all;
              UserProfile.getUserProfile().japam_count_satsang = receiveJapamDetails.data.japam_count_satsang;
              UserProfile.getUserProfile().japam_last_updated_date = receiveJapamDetails.data.japam_last_updated_date;
              updateValue();
              edCount.setText("");
              edDate.setText("");
              getBaseActivity().getMenuVisible(MainActivity.MenuOptions.EDIT);
              getBaseActivity().getMenuOption(MainActivity.MenuOptions.SAVE).setVisible(false);
              setEnableAllControls(false);
              msg.what = ConstValues.JAPAM_SUCCESS;

            } else {
              edCount.setText("");
              edDate.setText("");
              getBaseActivity().getMenuVisible(MainActivity.MenuOptions.EDIT);
              getBaseActivity().getMenuOption(MainActivity.MenuOptions.SAVE).setVisible(false);
              setEnableAllControls(false);
              msg.what = ConstValues.JAPAM_ERROR;
            }
            getBaseActivity().getFlowHandler().sendMessage(msg);

          }

          @Override
          public void onError(VolleyError error) {
            error.printStackTrace();
            getBaseActivity().getMenuVisible(MainActivity.MenuOptions.EDIT);
            getBaseActivity().getMenuOption(MainActivity.MenuOptions.SAVE).setVisible(false);
            android.os.Message msg = android.os.Message.obtain();
            msg.what = ConstValues.ERROR_DEFAULT;
            getBaseActivity().getFlowHandler().sendMessage(msg);
          }
        };

        //Sending the Message
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.JAPAM_SERVER_REQUEST;
        ArrayList<Object> values = new ArrayList<Object>();
        values.add(sendUpdateJapamCount);
        values.add(serverCallback);
        msg.obj = (Object) values;
        getBaseActivity().getFlowHandler().sendMessage(msg);


      }


      break;
      default:
        break;
    }

    return false;
  }


  private void init() {
    japam_count_over_all = (TextView) rootView.findViewById(R.id.japam_count_over_all);
    japam_count_satsang = (TextView) rootView.findViewById(R.id.japam_count_satsang);
    japam_count = (TextView) rootView.findViewById(R.id.japam_count);
    japam_last_updated_date = (TextView) rootView.findViewById(R.id.japam_last_updated_date);

    updateValue();

    edCount = (EditText) rootView.findViewById(R.id.edCount);
    edDate = (EditText) rootView.findViewById(R.id.edDate);


    allControls.add(edCount);
    //allControls.add(edDate);


  }

  private void updateValue() {
    japam_count_over_all.setText("" + UserProfile.getUserProfile().japam_count_over_all);
    japam_count_satsang.setText("" + UserProfile.getUserProfile().japam_count_satsang);
    japam_count.setText("" + UserProfile.getUserProfile().japam_count);
    final String NEW_FORMAT = "dd-MMM-yyyy";
    final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // August 12, 2010
    String oldDateString = UserProfile.getUserProfile().japam_last_updated_date;
    String newDateString = "";

    SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
    Date d = null;
    try {
      d = sdf.parse(oldDateString);
      sdf.applyPattern(NEW_FORMAT);
      newDateString = sdf.format(d);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    japam_last_updated_date.setText(newDateString);
  }

  private void setEnableAllControls(boolean isEnable) {
    for (int i = 0; i < allControls.size(); i++) {
      if (isEnable) {
        edCount.setFocusable(true);
        edCount.setFocusableInTouchMode(true);
        edDate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), japam_date,
                japam_update_date.get(Calendar.YEAR), japam_update_date.get(Calendar.MONTH),
                japam_update_date.get(Calendar.DAY_OF_MONTH));

            japam_update_date.add(Calendar.MONTH, 7);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker().setMaxDate(japam_update_date.getTimeInMillis());
            datePickerDialog.show();
          }
        });
      } else {
        edCount.setFocusable(false);
        edDate.setOnClickListener(null);
      }

    }

  }


}