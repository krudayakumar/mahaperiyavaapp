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

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kanchi.periyava.Activity.MainActivity;
import com.kanchi.periyava.Model.GeneralSetting;
import com.kanchi.periyava.Model.PreferenceData;
import com.kanchi.periyava.Model.UserProfile;
import com.kanchi.periyava.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Dashboard extends AppBaseFragement {
  public static String TAG = "MainActivity";
  View rootView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.dashboard_main, container, false);
    getActivity().setTitle(getResources().getString(R.string.app_name));
    setHasOptionsMenu(true);
    if(!TextUtils.isEmpty(GeneralSetting.getGeneralSetting().direct_radiourl_india)) {
      new GetAsyncRadioURL("INDIA").execute(GeneralSetting.getGeneralSetting().direct_radiourl_india);
    }
    if(!TextUtils.isEmpty(GeneralSetting.getGeneralSetting().direct_radiourl_others)) {
      new GetAsyncRadioURL("OTHERS").execute(GeneralSetting.getGeneralSetting().direct_radiourl_others);
    }

    //init();
    return rootView;
  }


  private void init() {
    if (UserProfile.getUserProfile().isLoggedIn == false) {
      return;
    }
    rootView.findViewById(R.id.japaminfo).setVisibility(UserProfile.getUserProfile().isLoggedIn == true ? View.VISIBLE : View.GONE);
    ((TextView) rootView.findViewById(R.id.japam_count_over_all)).setText("" + UserProfile.getUserProfile().japam_count_over_all);
    ((TextView) rootView.findViewById(R.id.japam_count_satsang)).setText(UserProfile.getInstance().isjoinedsatsang == false ? "Not Joined to Satsang" : "" + UserProfile.getUserProfile().japam_count_satsang);
    ((TextView) rootView.findViewById(R.id.japam_count)).setText("" + UserProfile.getUserProfile().japam_count);


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

  private class GetAsyncRadioURL extends AsyncTask<String, Void, String> {
    String URLType = "";
    ArrayList<String> strURLList = new ArrayList<>();

    public GetAsyncRadioURL(String type) {
      this.URLType = type;
    }


    @Override
    protected void onPreExecute() {
      super.onPreExecute();


    }

    @Override
    protected String doInBackground(String... params) {

      // @BadSkillz codes with same changes
      try {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(params[0]);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        BufferedHttpEntity buf = new BufferedHttpEntity(entity);

        InputStream is = buf.getContent();

        BufferedReader r = new BufferedReader(new InputStreamReader(is));

        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
          strURLList.add(line);
          total.append(line + "\n");
        }
        String result = total.toString();
        Log.i("Get URL", "Downloaded string: " + result);
        return result;
      } catch (Exception e) {
        Log.e("Get Url", "Error in downloading: " + e.toString());
      }
      return null;
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
      if (URLType.compareToIgnoreCase("INDIA") == 0) {
        PreferenceData.getInstance(getActivity()).setValue(
            PreferenceData.PREFVALUES.DIRECT_RADIOURL_INDIA_URL1.toString(), strURLList.get(0));
        PreferenceData.getInstance(getActivity()).setValue(
            PreferenceData.PREFVALUES.DIRECT_RADIOURL_INDIA_URL2.toString(), strURLList.get(1));
      }

      if (URLType.compareToIgnoreCase("OTHERS") == 0) {
        PreferenceData.getInstance(getActivity()).setValue(
            PreferenceData.PREFVALUES.DIRECT_RADIOURL_OTHERS_URL1.toString(), strURLList.get(0));
        PreferenceData.getInstance(getActivity()).setValue(
            PreferenceData.PREFVALUES.DIRECT_RADIOURL_OTHERS_URL2.toString(), strURLList.get(1));

      }

    }
  }

}
