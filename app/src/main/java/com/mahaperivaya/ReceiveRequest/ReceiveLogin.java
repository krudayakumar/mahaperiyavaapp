package com.mahaperivaya.ReceiveRequest;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m84098 on 9/27/15.
 */
public class ReceiveLogin {
	@SerializedName("status")
	public String status;

	@SerializedName("code")
	public int code;

	@SerializedName("data")
	public Data data;

	public class Data {
		@SerializedName("username")
		public String username;
		@SerializedName("profileid")
		public int profileid;
		@SerializedName("ispasswordreset")
		public boolean ispasswordreset;

		// Satsang
		@SerializedName("isjoinedsatsang")
		public boolean isjoinedsatsang;
		@SerializedName("satsangid")
		public String satsangid;

		// Japam
		@SerializedName("isjoinedjapam")
		public boolean isjoinedjapam;
		@SerializedName("japam_count")
		public int japam_count;
		@SerializedName("japam_last_updated_date")
		public String japam_last_updated_date;
		@SerializedName("japam_count_over_all")
		public int japam_count_over_all;
		@SerializedName("japam_count_satsang")
		public int japam_count_satsang;

		@SerializedName("message")
		public String message;

	}

	public boolean isSuccess() {
		return status.equalsIgnoreCase("success") ? true : false;
	}

}
