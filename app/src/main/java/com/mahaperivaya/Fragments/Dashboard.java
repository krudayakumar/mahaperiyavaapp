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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dashboard extends AppBaseFragement {
  public static String TAG = "MainActivity";
  View rootView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.dashboard_main, container, false);
    getActivity().setTitle(getResources().getString(R.string.lbl_dashboard));
    setHasOptionsMenu(true);
    //init();
    return rootView;
  }


  private void init() {
    if(UserProfile.getUserProfile().isLoggedIn == false) {
      return;
    }
    rootView.findViewById(R.id.japaminfo).setVisibility(UserProfile.getUserProfile().isLoggedIn == true ? View.VISIBLE : View.GONE);
    ((TextView) rootView.findViewById(R.id.japam_count_over_all)).setText("" + UserProfile.getUserProfile().japam_count_over_all);
    ((TextView) rootView.findViewById(R.id.japam_count_satsang)).setText(UserProfile.getInstance().isjoinedsatsang == false ? "Not Joined to Satsang":  "" + UserProfile.getUserProfile().japam_count_satsang);
    ((TextView) rootView.findViewById(R.id.japam_count)).setText("" + UserProfile.getUserProfile().japam_count);


    final String NEW_FORMAT = "dd-MMM-yyyy";
    final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";

  // August 12, 2010
    String oldDateString = UserProfile.getUserProfile().japam_last_updated_date;
    String newDateString="";

    SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
    Date d = null;
    try {
      d = sdf.parse(oldDateString);
      sdf.applyPattern(NEW_FORMAT);
      newDateString = sdf.format(d);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    ((TextView) rootView.findViewById(R.id.japam_last_updated_date)).setText(newDateString);
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Do something that differs the Activity's menu here
    super.onCreateOptionsMenu(menu, inflater);
    getBaseActivity().setMenuOption(menu);
    getBaseActivity().getMenuOption(MainActivity.MenuOptions.FEEDBACK);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save:
        //saveClick();
        break;
      default:
        break;
    }

    return false;
  }
}
