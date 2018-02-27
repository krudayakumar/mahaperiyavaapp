package com.kanchi.periyava.old.Model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kanchi.periyava.old.Activity.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
  public String TAG = "AlarmReceiver";

  @Override
  public void onReceive(Context context, Intent intent) {

    if (MainActivity.radiostate) {
      //Sending the Message
      android.os.Message msg = android.os.Message.obtain();
      msg.what = ConstValues.RADIO_GET_PLAYLIST;
      MessageHandler.getFlowHandler().sendMessage(msg);
    }

  }
}
