package com.mahaperivaya.SendRequest;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by m84098 on 9/27/15.
 */
public class SendUpdateJapamCount {
  @SerializedName("profileid")
  public int profileid;

  @SerializedName("japamcount")
  public int japamcount;

  @SerializedName("japamupdatedate")
  public Date japamupdatedate;

}