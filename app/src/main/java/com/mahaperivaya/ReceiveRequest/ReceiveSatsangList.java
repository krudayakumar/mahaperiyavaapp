package com.mahaperivaya.ReceiveRequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by m84098 on 9/27/15.
 */

public class ReceiveSatsangList  {

	@SerializedName("status")
	public String status;

	@SerializedName("code")
	public int code;

	@SerializedName("data")
	public ArrayList<Data> data;

  @SerializedName("message")
  public String message;

	@SuppressWarnings("serial")
	public static class Data implements Serializable{

		@SerializedName("satsangid")
		public long satsangid;

		@SerializedName("profileid")
		public long profileid;

		@SerializedName("name")
		public String name;

    @SerializedName("operationtype")
    public String operationtype;

		@SerializedName("description")
		public String description;

		@SerializedName("contactname")
		public String contactname;

		@SerializedName("contactno")
		public String contactno;

		@SerializedName("emailid")
		public String emailid;

		@SerializedName("city")
		public String city;

		@SerializedName("state")
		public String state;

		@SerializedName("country")
		public String country;


	}
	public boolean isSuccess() {
		return status.equalsIgnoreCase("success") ? true : false;
	}

}
