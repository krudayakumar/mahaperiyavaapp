package com.kanchi.periyava.old.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kanchi.periyava.old.Adapters.PhotoVideoBookAdapter;
import com.kanchi.periyava.old.Interface.ServerCallback;
import com.kanchi.periyava.old.Model.ConstValues;
import com.kanchi.periyava.old.Model.UserProfile;
import com.kanchi.periyava.R;
import com.kanchi.periyava.old.ReceiveRequest.ReceivePhotoVideo;

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
                  msg.obj = (Object) categories.link;
                  getFlowHandler().sendMessage(msg);
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
                    if(option == ConstValues.CONST_VIDEO)
                      msg.what = ConstValues.VIDEO_OPEN;
                     else
                      msg.what = ConstValues.WEB_PAGE;
                  msg.obj = (Object) subCategories.link;
                  getFlowHandler().sendMessage(msg);
                  //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(subCategories.link)));
                }
                return false;
              }
            });
          }


        } else {
          android.os.Message msg = android.os.Message.obtain();
          msg.what = ConstValues.ERROR_DEFAULT;
          getFlowHandler().sendMessage(msg);
        }


      }

      @Override
      public void onError(VolleyError error) {
        error.printStackTrace();
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.ERROR_DEFAULT;
        getFlowHandler().sendMessage(msg);
      }
    };

    //Sending the Message
    android.os.Message msg = android.os.Message.obtain();
    msg.what = ConstValues.PHOTO_VIDEO_LIST_SERVER_REQUEST;
    msg.obj = (Object) serverCallback;
    getFlowHandler().sendMessage(msg);
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
          getFlowHandler().sendMessage(msg);
          return true;
        }
        return false;
      }


    });
  }


}
