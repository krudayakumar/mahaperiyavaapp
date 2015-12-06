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
package com.mahaperivaya.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.R;

public class Radio extends AppBaseFragement {
  public static String TAG = "Radio";
  WebView wv;
  View rootView;
  Menu menu;
  Button btnRadio;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                           Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    final View rootView = inflater.inflate(R.layout.radio, container, false);

    btnRadio = ((Button) rootView.findViewById(R.id.btnRadio));

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


    return rootView;

  }



    public void setRadioButtonState(boolean radiostate) {
    if (radiostate == true) {
      btnRadio.setBackground(getResources().getDrawable(R.drawable.ic_radio_stop));
    } else {
      btnRadio.setBackground(getResources().getDrawable(R.drawable.ic_radio_play));
    }
  }

  public void setRadioMenu(){
    if (MainActivity.radiostate == false) {
      getBaseActivity().getMenuOption(MainActivity.MenuOptions.RADIO).setVisible(false);
    } else {
      getBaseActivity().getMenuOption(MainActivity.MenuOptions.RADIO).setVisible(true);
    }

  }

  @Override
  public void onDestroyView() {
    setRadioMenu();
    super.onDestroyView();
  }

  @Override
  public void onDetach() {
    setRadioMenu();
    super.onDetach();
  }


}
