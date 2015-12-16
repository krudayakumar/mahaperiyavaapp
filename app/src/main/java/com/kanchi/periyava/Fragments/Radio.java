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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kanchi.periyava.Activity.MainActivity;
import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.R;

import java.util.Random;

public class Radio extends AppBaseFragement {
  public static String TAG = "Radio";
  WebView wv;
  View rootView;
  Menu menu;
  ImageButton btnRadio;
  static String strPlaylist;
  TextView tv;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                           Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    rootView = inflater.inflate(R.layout.radio, container, false);

    btnRadio = ((ImageButton) rootView.findViewById(R.id.btnRadio));
    strPlaylist = "";
    tv = ((TextView) rootView.findViewById(R.id.playlist));
    setRadioButtonState(MainActivity.radiostate);
    getBaseActivity().getMenuOption(MainActivity.MenuOptions.RADIO).setVisible(false);
    btnRadio.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.RADIO_RUN_STOP;
        MainActivity.getFlowHandler().sendMessage(msg);
      }
    });
    if (getBaseActivity().radiostate) {
      android.os.Message msg = android.os.Message.obtain();
      msg.what = ConstValues.RADIO_GET_PLAYLIST;
      MainActivity.getFlowHandler().sendMessage(msg);
      getBaseActivity().startAlaram();
    }
    return rootView;

  }


  public void setRadioButtonState(boolean radiostate) {
    if (radiostate == true) {
      btnRadio.setBackground(getResources().getDrawable(R.drawable.ic_stop));
    } else {
      btnRadio.setBackground(getResources().getDrawable(R.drawable.ic_play));
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
      tv.setText(strPlaylist + "              " + strPlaylist + "              " + strPlaylist);
      this.strPlaylist = strPlaylist;
      tv.setSelected(true);
      tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
      tv.setSingleLine(true);
    }
    Random rnd = new Random();
    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    tv.setTextColor(color);
  }

}
