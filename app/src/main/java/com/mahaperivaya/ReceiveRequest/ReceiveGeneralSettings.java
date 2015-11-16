package com.mahaperivaya.ReceiveRequest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m84098 on 11/8/15.
 */
public class ReceiveGeneralSettings {
  @SerializedName("status")
  public String status;

  @SerializedName("code")
  public int code;

  @SerializedName("message")
  public String message;

  @SerializedName("data")
  public Data data;

  public class Data {
    @SerializedName("version")
    public int version;

    @SerializedName("feedbackemailid")
    public String feedbackemailid;

  }
  public boolean isSuccess() {
    return status.equalsIgnoreCase("success") ? true : false;
  }
}