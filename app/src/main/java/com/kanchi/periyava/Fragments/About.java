package com.kanchi.periyava.Fragments;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kanchi.periyava.Activity.MainActivity;
import com.kanchi.periyava.Interface.ServerCallback;
import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.R;
import com.kanchi.periyava.ReceiveRequest.GeneralReceiveRequest;

import org.json.JSONObject;

/**
 * Created by m84098 on 9/3/15.
 */
public class About extends AppBaseFragement {
  public static String TAG = "About";
  View rootView;
  String strTitle;
  String strContent;
  int imgView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.about, container, false);

    Bundle bundle = getArguments();

    if (bundle != null) {
      strTitle = (bundle.getInt(ConstValues.CONST_ABOUT_TITLE) == 0) ? "" : getResources().getString(bundle.getInt(ConstValues.CONST_ABOUT_TITLE));
      imgView = bundle.getInt(ConstValues.CONST_ABOUT_IMAGE);
      strContent = getResources().getString(bundle.getInt(ConstValues.CONST_ABOUT_CONTENT));
    }

    initCompontents();
    return rootView;
  }


  private void initCompontents() {
    TextView tv = ((TextView) rootView.findViewById(R.id.title));
    if (TextUtils.isEmpty(strTitle)) {
      tv.setVisibility(View.GONE);
    } else {
      tv.setVisibility(View.VISIBLE);
      tv.setText(Html.fromHtml(strTitle));
    }

    ((ImageView) rootView.findViewById(R.id.imgView))
        .setImageResource(imgView);

    String strJustifyContent = " <html><body style=\"text-align:justify\"><font size=\"4\">" + strContent + "</font></body></Html>";
    WebView webView = (WebView) rootView.findViewById(R.id.content);
    webView.loadData(strJustifyContent, "text/html", "utf-8");

    /*TextView txtContent = (TextView) rootView.findViewById(R.id.content);
    txtContent.setText(Html.fromHtml(strJustifyContent));
*/

  }

}
