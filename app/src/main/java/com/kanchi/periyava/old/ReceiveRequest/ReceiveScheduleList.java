package com.kanchi.periyava.old.ReceiveRequest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m84098 on 9/27/15.
 */
public class ReceiveScheduleList {
  @SerializedName("status")
  public String status;

  @SerializedName("code")
  public int code;

  @SerializedName("message")
  public String message;

  @SerializedName("data")
  public String data;


  public boolean isSuccess() {
    return status.equalsIgnoreCase("success") ? true : false;
  }

}
