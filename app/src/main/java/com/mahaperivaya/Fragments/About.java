package com.mahaperivaya.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mahaperivaya.Adapters.SatsangListAdapter;
import com.mahaperivaya.Interface.ServerCallback;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.GeneralReceiveRequest;
import com.mahaperivaya.ReceiveRequest.ReceiveSatsangList;

import org.json.JSONObject;

/**
 * Created by m84098 on 9/3/15.
 */
public class About extends  AppBaseFragement{
        public static String TAG = "About";
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.about, container, false);
            getActivity().setTitle(getResources().getString(R.string.lbl_about));
          ServerCallback serverCallback = new ServerCallback() {
            @Override
            public void onSuccess(JSONObject response) {
              GeneralReceiveRequest generalReceive = new Gson().fromJson(response.toString(),
                  GeneralReceiveRequest.class);

              if (generalReceive.isSuccess()) {


              } else {
                            }


            }

            @Override
            public void onError(VolleyError error) {
              error.printStackTrace();
              android.os.Message msg = android.os.Message.obtain();
              msg.what = ConstValues.ERROR_DEFAULT;
              getBaseActivity().getFlowHandler().sendMessage(msg);
            }
          };

          //Sending the Message
          android.os.Message msg = android.os.Message.obtain();
          //msg.what = ConstValues.;
          msg.obj = (Object) serverCallback;
          getBaseActivity().getFlowHandler().sendMessage(msg);


          return rootView;
        }

}
