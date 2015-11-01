package com.mahaperivaya.Model;

import java.util.Date;

import android.graphics.Bitmap;
import android.os.Bundle;

/**
 * Created by m84098 on 9/14/15.
 */
public class UserProfile {

	public String username;
	public String emailid;
	public int profileid;
	public boolean ispasswordreset;
  public String access_token;

	// Satsang
	public boolean isjoinedsatsang;
	public int satsangid;

	// Japam
	public boolean isjoinedjapam;
	public int japam_count;
	public String japam_last_updated_date;
	public int japam_count_over_all;
	public int japam_count_satsang;

	private Bundle arguments;
	private Bitmap image;

	public boolean isLoggedIn = true;

	private static UserProfile userProfile;

	public static UserProfile getInstance() {
		if (userProfile == null) {
			userProfile = new UserProfile();
		}

		return userProfile;
	}

	public static UserProfile getUserProfile() {
		return userProfile;
	}


  public UserProfile() {
  }

  public void clearAll() {
    japam_last_updated_date =  emailid = username="";
    satsangid = profileid = japam_count = japam_count_over_all = japam_count_satsang = 0;
    isjoinedjapam = isjoinedsatsang = ispasswordreset = false;
    isLoggedIn = false;
  }

}
