package com.kanchi.periyava.Model;

import android.graphics.Bitmap;
import android.os.Bundle;

/**
 * Created by m84098 on 9/14/15.
 */
public class UserProfile {

  public String username;
  public String emailid;
  public long profileid;
  public boolean ispasswordreset;
  public String access_token;

  // Satsang
  public boolean isjoinedsatsang;
  public long satsangid;

  // Japam
  public boolean isjoinedjapam;
  public int japam_count;
  public String japam_last_updated_date;
  public int japam_count_over_all;
  public int japam_count_satsang;

  private Bundle arguments;
  private Bitmap image;

  public boolean isLoggedIn = false;

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
    japam_last_updated_date = emailid = username = "";
    satsangid = japam_count = japam_count_over_all = japam_count_satsang = 0;
    profileid = 0;
    isjoinedjapam = isjoinedsatsang = ispasswordreset = false;
    isLoggedIn = false;
  }

}
