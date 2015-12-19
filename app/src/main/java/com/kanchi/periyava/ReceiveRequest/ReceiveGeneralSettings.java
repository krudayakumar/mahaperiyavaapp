package com.kanchi.periyava.ReceiveRequest;

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
    public String version;

    @SerializedName("forceupgrade")
    public boolean forceupgrade;

    @SerializedName("maintenancemode")
    public boolean maintenancemode;

    @SerializedName("playstoreurl")
    public String playstoreurl;

    @SerializedName("feedbackemailid")
    public String feedbackemailid;

    @SerializedName("radiourl")
    public String radiourl;

    @SerializedName("radioplaylisturl")
    public String radioplaylisturl;

  }

  public boolean isSuccess() {
    return status.equalsIgnoreCase("success") ? true : false;
  }
}
