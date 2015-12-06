package com.mahaperivaya.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mahaperivaya.Adapters.PhotoVideoBookAdapter;
import com.mahaperivaya.Interface.ServerCallback;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.ReceivePhotoVideo;

import org.json.JSONObject;

/**
 * Created by m84098 on 9/14/15.
 */
public class PhotoVideoBook extends AppBaseFragement {
  public static String TAG = "PhotoVideoBook";
  private PhotoVideoBookAdapter photoVideoBookAdapter;
  View rootView;
  String option;
  ExpandableListView expandableListView;
  Context context;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    option = bundle.getString(ConstValues.VIDEO_PHOTO_OPTION);

    rootView = inflater.inflate(R.layout.photo_video_books, container, false);
    expandableListView = (ExpandableListView) rootView.findViewById(R.id.expListView);
    context = container.getContext();


    ServerCallback serverCallback = new ServerCallback() {
      @Override
      public void onSuccess(JSONObject response) {
        ReceivePhotoVideo receivePhotoVideoList = new Gson().fromJson(response.toString(),
            ReceivePhotoVideo.class);

        if (receivePhotoVideoList.isSuccess()) {
          ReceivePhotoVideo.Category category = null;
          for (int iCount = 0; iCount < receivePhotoVideoList.data.size(); iCount++) {
            if (option.equalsIgnoreCase(receivePhotoVideoList.data.get(iCount).categorytype)) {
              category = receivePhotoVideoList.data.get(iCount);
              getActivity().setTitle(category.categorytitle);
              break;
            }
          }
          if (category != null) {
            photoVideoBookAdapter = new PhotoVideoBookAdapter(getBaseActivity(), category);
            expandableListView.setAdapter(photoVideoBookAdapter);
            expandableListView.setClickable(true);
            //expandableListView.setGroupIndicator(null);
            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
              @Override
              public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ReceivePhotoVideo.Category.Categories categories = (ReceivePhotoVideo.Category.Categories) v.getTag();
                if (!TextUtils.isEmpty(categories.link)) {
                  android.os.Message msg = android.os.Message.obtain();
                  msg.what = ConstValues.WEB_PAGE;
                  msg.obj = (Object)categories.link;
                  getBaseActivity().getFlowHandler().sendMessage(msg);
                  //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(categories.link)));
                }
                return false;
              }
            });
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
              @Override
              public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ReceivePhotoVideo.Category.Categories.SubCategories subCategories = (ReceivePhotoVideo.Category.Categories.SubCategories) v.getTag();
                if (!TextUtils.isEmpty(subCategories.link)) {
                  android.os.Message msg = android.os.Message.obtain();
                  switch(option) {
                    case ConstValues.CONST_VIDEO:
                      msg.what = ConstValues.VIDEO_OPEN;
                      break;
                    default:
                      msg.what = ConstValues.WEB_PAGE;
                      break;
                  }

                  msg.obj = (Object)subCategories.link;
                  getBaseActivity().getFlowHandler().sendMessage(msg);
                  //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(subCategories.link)));
                }
                return false;
              }
            });
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


}
