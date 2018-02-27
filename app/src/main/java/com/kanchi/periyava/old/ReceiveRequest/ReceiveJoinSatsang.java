package com.kanchi.periyava.old.ReceiveRequest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m84098 on 11/8/15.
 */
public class ReceiveJoinSatsang {
  @SerializedName("status")
  public String status;

  @SerializedName("code")
  public int code;

  @SerializedName("message")
  public String message;

  @SerializedName("data")
  public Data data;

  public class Data {
    @SerializedName("satsangid")
    public long satsangid;
    @SerializedName("japam_count")
    public int japam_count;
    @SerializedName("japam_last_updated_date")
    public String japam_last_updated_date;
    @SerializedName("japam_count_over_all")
    public int japam_count_over_all;
    @SerializedName("japam_count_satsang")
    public int japam_count_satsang;

  }

}
