package com.kanchi.periyava.Model;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.kanchi.periyava.Activity.MainActivity;
import com.kanchi.periyava.Fragments.Radio;

public class AlarmReceiver extends BroadcastReceiver {
  public String TAG = "AlarmReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {

    if (MainActivity.radiostate) {
      //Sending the Message
      android.os.Message msg = android.os.Message.obtain();
      msg.what = ConstValues.RADIO_GET_PLAYLIST;
      MainActivity.getFlowHandler().sendMessage(msg);
    }

  }
}
