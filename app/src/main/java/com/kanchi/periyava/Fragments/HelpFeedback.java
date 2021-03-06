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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.kanchi.periyava.BuildConfig;
import com.kanchi.periyava.Model.GeneralSetting;
import com.kanchi.periyava.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelpFeedback extends AppBaseFragement {
  public static String TAG = "HelpFeedback";
  WebView wv;
  View rootView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                           Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    final View rootView = inflater.inflate(R.layout.help_feedback, container, false);

    Button btnFeedback = (Button) rootView.findViewById(R.id.feedback);
    btnFeedback.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + GeneralSetting.getInstance().feedbackemailid));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");

        intent.putExtra(Intent.EXTRA_TEXT, (BuildConfig.DEBUG == true ? "Debug" : "Release") + " Ver " + BuildConfig.VERSION_NAME + "\nDate:" + new SimpleDateFormat("EEE, MMM dd yyyy hh:mm:ss").format(new Date(BuildConfig.TIMESTAMP)));
        startActivity(intent);
      }
    });




    /*if (bundle != null) {
      String strURL = bundle.getString(MainActivity.WEBURL);
      Log.d(TAG, "URL::" + strURL);
      wv = (WebView) rootView.findViewById(R.id.webView);
      wv.getSettings().setJavaScriptEnabled(true);
      wv.loadUrl(strURL);

      wv.setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
          view.loadUrl(url);
          return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
          // Do something
          android.os.Message msg = android.os.Message.obtain();
          msg.what = ConstValues.ERROR_INTERNET_CONNECTION;
          MainActivity.getFlowHandler().sendMessage(msg);
        }
      });


    }*/

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

          getFragmentManager().popBackStack();

          return true;
        }
        return false;
      }


    });
  }
}
