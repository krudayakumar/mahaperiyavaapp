package com.kanchi.periyava.SendRequest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m84098 on 9/27/15.
 */
public class SendPasswordReset {


  @SerializedName("profileid")
  public long profileid;

  @SerializedName("password")
  public String password;
}
