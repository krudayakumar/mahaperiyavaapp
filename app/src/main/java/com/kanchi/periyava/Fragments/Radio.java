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
package com.kanchi.periyava.Fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kanchi.periyava.Activity.MainActivity;
import com.kanchi.periyava.Interface.ServerCallback;
import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.R;
import com.kanchi.periyava.ReceiveRequest.GeneralReceiveRequest;
import com.kanchi.periyava.ReceiveRequest.ReceiveScheduleList;

import org.json.JSONObject;

import java.util.Random;

public class Radio extends AppBaseFragement {
  public static String TAG = "Radio";
  WebView wv;
  View rootView;
  Menu menu;
  ImageButton btnRadio;
  static String strPlaylist;
  TextView tvPlayList, tvScheduleList;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                           Bundle savedInstanceState) {
		final Bundle bundle = getArguments();

		rootView = inflater.inflate(R.layout.radio, container, false);

    btnRadio = ((ImageButton) rootView.findViewById(R.id.btnRadio));
    strPlaylist = "";
    tvPlayList = ((TextView) rootView.findViewById(R.id.playlist));
    tvScheduleList = ((TextView) rootView.findViewById(R.id.schedulelist));
    ServerCallback serverCallback = new ServerCallback() {
      @Override
      public void onSuccess(JSONObject response) {
        GeneralReceiveRequest generalReceive = new Gson().fromJson(response.toString(),
            GeneralReceiveRequest.class);

        if (generalReceive.isSuccess()) {
          ReceiveScheduleList receiveScheduleList = new Gson().fromJson(response.toString(),
              ReceiveScheduleList.class);
          tvScheduleList.setText(receiveScheduleList.data);
        } else {
          android.os.Message msg = android.os.Message.obtain();
          msg.what = ConstValues.ERROR_DEFAULT;
          msg.obj = (Object) response;

          getBaseActivity().getFlowHandler().sendMessage(msg);
          tvScheduleList.setText("");
        }


      }

      @Override
      public void onError(VolleyError error) {
        error.printStackTrace();
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.ERROR_DEFAULT;
        getBaseActivity().radiostate = false;
        getBaseActivity().getFlowHandler().sendMessage(msg);
      }
    };

    //Sending the Message
    android.os.Message msg = android.os.Message.obtain();
    msg.what = ConstValues.RADIO_SCHEDULE_LIST;
    msg.obj = (Object) serverCallback;
    getBaseActivity().getFlowHandler().sendMessage(msg);



    setRadioButtonState(MainActivity.radiostate);
		// getBaseActivity().getMenuOption(MainActivity.MenuOptions.RADIO).setVisible(false);
    btnRadio.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.RADIO_RUN_STOP;
				msg.obj = bundle.getString("URL");
				Log.d((String) msg.obj, "inside radio");
        MainActivity.getFlowHandler().sendMessage(msg);
      }
    });


    if (getBaseActivity().radiostate) {
      android.os.Message msg1 = android.os.Message.obtain();
      msg1.what = ConstValues.RADIO_GET_PLAYLIST;
      MainActivity.getFlowHandler().sendMessage(msg1);
      getBaseActivity().startAlaram();
    }
    return rootView;

  }


  public void setRadioButtonState(boolean radiostate) {
    int sdk = android.os.Build.VERSION.SDK_INT;
    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
      if (radiostate == true) {
        btnRadio.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_stop));
      } else {
        btnRadio.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_play));
      }
    } else {
      if (radiostate == true) {
        btnRadio.setBackground(getResources().getDrawable(R.drawable.ic_stop));
      } else {
        btnRadio.setBackground(getResources().getDrawable(R.drawable.ic_play));
      }
    }
  }


  public void setRadioMenu() {
    if (MainActivity.radiostate == false) {
      getBaseActivity().getMenuOption(MainActivity.MenuOptions.RADIO).setVisible(false);
    } else {
      getBaseActivity().getMenuOption(MainActivity.MenuOptions.RADIO).setVisible(true);
    }

  }

  @Override
  public void onDestroyView() {
    setRadioMenu();
    getBaseActivity().cancelAlaram();
    super.onDestroyView();
  }

  @Override
  public void onDetach() {
    setRadioMenu();
    getBaseActivity().cancelAlaram();
    super.onDetach();
  }

  public void setPlaylist(String strPlaylist) {
    if (rootView != null && this.strPlaylist.equalsIgnoreCase(strPlaylist) == false) {
      tvPlayList.setText(strPlaylist + "              " + strPlaylist + "              " + strPlaylist);
      this.strPlaylist = strPlaylist;
      tvPlayList.setSelected(true);
      tvPlayList.setEllipsize(TextUtils.TruncateAt.MARQUEE);
      tvPlayList.setSingleLine(true);
    }
    Random rnd = new Random();
    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    tvPlayList.setTextColor(color);
  }

}
