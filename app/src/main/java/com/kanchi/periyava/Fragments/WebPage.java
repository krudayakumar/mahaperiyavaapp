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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kanchi.periyava.Activity.MainActivity;
import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.R;

public class WebPage extends AppBaseFragement {
  public static String TAG = "WebPage";
  WebView wv;
  View rootView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                           Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    final View rootView = inflater.inflate(R.layout.webpage, container, false);

    if (bundle != null) {
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


    }

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
