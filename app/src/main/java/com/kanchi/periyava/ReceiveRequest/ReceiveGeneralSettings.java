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

    @SerializedName("isappmsg")
    public boolean isappmsg;

    @SerializedName("message")
    public String message;

    @SerializedName("playstoreurl")
    public String playstoreurl;

    @SerializedName("feedbackemailid")
    public String feedbackemailid;

    @SerializedName("radiourl")
    public String radiourl;

    @SerializedName("direct_radiourl_india")
    public String direct_radiourl_india;

    @SerializedName("direct_radiourl_others")
    public String direct_radiourl_others;

    @SerializedName("radiourl_india")
		public String radiourl_india;

		@SerializedName("radiourl_others")
		public String radiourl_others;

    @SerializedName("radioplaylisturl")
    public String radioplaylisturl;

  }

  public boolean isSuccess() {
    return status.equalsIgnoreCase("success") ? true : false;
  }
}
