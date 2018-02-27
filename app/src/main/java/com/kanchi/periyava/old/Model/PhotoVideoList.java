package com.kanchi.periyava.old.Model;

import android.os.Message;

import com.kanchi.periyava.old.ReceiveRequest.ReceivePhotoVideoList;

import java.util.ArrayList;

/**
 * Created by m84098 on 9/14/15.
 */
public class PhotoVideoList {

  private static PhotoVideoList photoVideo;
  private static ArrayList<ReceivePhotoVideoList.PhotoVideoDetails> photoVideoDetailsList;

  public static PhotoVideoList getInstance() {
    if (photoVideoDetailsList == null || photoVideo == null) {
      photoVideo = new PhotoVideoList();
      Message msg = Message.obtain();
      msg.what = ConstValues.PHOTO_VIDEO_LIST_SERVER_REQUEST;
      MessageHandler.getFlowHandler().sendMessage(msg);
    }

    return photoVideo;
  }

  public static ArrayList<ReceivePhotoVideoList.PhotoVideoDetails> getPhotoVideoDetailsList() {
    return photoVideoDetailsList;
  }


  public void setPhotoVideoList(ArrayList<ReceivePhotoVideoList.PhotoVideoDetails> photoVideoDetailsList) {
    this.photoVideoDetailsList = photoVideoDetailsList;
  }

}

