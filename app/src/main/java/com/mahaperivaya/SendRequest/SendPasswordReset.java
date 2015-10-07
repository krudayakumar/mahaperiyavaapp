package com.mahaperivaya.SendRequest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m84098 on 9/27/15.
 */
public class SendPasswordReset {


  @SerializedName("profileid")
  public int profileid;

  @SerializedName("password")
  public String password;
}
