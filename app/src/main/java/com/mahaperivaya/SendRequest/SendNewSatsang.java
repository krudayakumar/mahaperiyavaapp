package com.mahaperivaya.SendRequest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m84098 on 9/27/15.
 */
public class SendNewSatsang {

	@SerializedName("profileid")
	public int profileid;

	@SerializedName("name")
	public String name;

	@SerializedName("description")
	public String description;

	@SerializedName("contactpersonname")
	public String contactpersonname;

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
