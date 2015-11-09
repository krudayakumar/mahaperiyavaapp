package com.mahaperivaya.SendRequest;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by m84098 on 9/27/15.
 */
public class SendUpdateJapamCount {
  @SerializedName("profileid")
  public long profileid;

  @SerializedName("access_token")
  public String access_token;

  @SerializedName("japamcount")
  public int japamcount;

  @SerializedName("japamupdateddate")
  public String japamupdatedate;

  @SerializedName("satsangid")
  public long satsangid;


}
