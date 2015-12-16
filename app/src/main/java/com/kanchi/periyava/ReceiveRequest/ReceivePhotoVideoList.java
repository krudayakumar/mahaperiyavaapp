package com.kanchi.periyava.ReceiveRequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by m84098 on 9/27/15.
 */

public class ReceivePhotoVideoList {

  @SerializedName("status")
  public String status;

  @SerializedName("code")
  public int code;

  @SerializedName("data")
  public ArrayList<PhotoVideoDetails> data;

  @SuppressWarnings("serial")
  public static class PhotoVideoDetails implements Serializable {

    @SerializedName("title")
    public String title;

    @SerializedName("type")
    public String type;

    @SerializedName("sublinks")
    public ArrayList<SubLinks> sublinks;

    @SuppressWarnings("serial")
    public static class SubLinks implements Serializable {
      @SerializedName("subtitle")
      public String subtitle;

      @SerializedName("link")
      public String link;
    }
  }

  public boolean isSuccess() {
    return status.equalsIgnoreCase("success") ? true : false;
  }

}
