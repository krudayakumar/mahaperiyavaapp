package com.mahaperivaya.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Adapters.PhotoVideoListAdapter;
import com.mahaperivaya.Interface.ServerCallback;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.ReceivePhotoVideoList;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by m84098 on 9/14/15.
 */
public class PhotoVideoList extends AppBaseFragement {
  public static String TAG = "PhotoVideoList";
  private RecyclerView recyclerView;
  private PhotoVideoListAdapter photoVideoListAdapter;
  View rootView;
  String option;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    option = bundle.getString(ConstValues.VIDEO_PHOTO_OPTION);

    rootView = inflater.inflate(R.layout.photo_video, container, false);
    recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
    recyclerView.setLayoutManager(layoutManager);
    setHasOptionsMenu(true);
    ServerCallback serverCallback = new ServerCallback() {
      @Override
      public void onSuccess(JSONObject response) {
        ReceivePhotoVideoList receivePhotoVideoList = new Gson().fromJson(response.toString(),
            ReceivePhotoVideoList.class);

        if (receivePhotoVideoList.isSuccess()) {
          ArrayList<ReceivePhotoVideoList.PhotoVideoDetails.SubLinks> subLinks = new ArrayList<ReceivePhotoVideoList.PhotoVideoDetails.SubLinks>();

          for (int iCount = 0; iCount < receivePhotoVideoList.data.size(); iCount++) {
            if (option.equalsIgnoreCase(receivePhotoVideoList.data.get(iCount).type)) {
              subLinks = receivePhotoVideoList.data.get(iCount).sublinks;
              break;
            }
          }
          if (subLinks.size() != 0) {
            photoVideoListAdapter = new PhotoVideoListAdapter(getBaseActivity(), subLinks, option);
            recyclerView.setAdapter(photoVideoListAdapter);
          }


        } else {
          android.os.Message msg = android.os.Message.obtain();
          msg.what = ConstValues.ERROR_DEFAULT;
          getBaseActivity().getFlowHandler().sendMessage(msg);
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
    msg.what = ConstValues.PHOTO_VIDEO_LIST_SERVER_REQUEST;
    msg.obj = (Object) serverCallback;
    getBaseActivity().getFlowHandler().sendMessage(msg);
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
          // handle back button's click listener
          android.os.Message msg = android.os.Message.obtain();
          if (UserProfile.getUserProfile().isLoggedIn) {
            msg.what = ConstValues.DASHBOARD_LOGIN;
          } else {
            msg.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;
          }
          getBaseActivity().getFlowHandler().sendMessage(msg);
          return true;
        }
        return false;
      }


    });
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Do something that differs the Activity's menu here
    super.onCreateOptionsMenu(menu, inflater);
    if (UserProfile.getUserProfile().isLoggedIn) {
      getBaseActivity().getMenuOption(MainActivity.MenuOptions.ADD_NEW).setVisible(true);
      getBaseActivity().getMenuOption(MainActivity.MenuOptions.ADD_NEW).setTitle(getResources().getString(R.string.lbl_new_satsang));
    }

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.addnew: {
        //Sending the Message
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.NEW_SATSANG;
        getBaseActivity().getFlowHandler().sendMessage(msg);
      }
      break;
      default:
        break;
    }

    return false;
  }


  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

  }
}
