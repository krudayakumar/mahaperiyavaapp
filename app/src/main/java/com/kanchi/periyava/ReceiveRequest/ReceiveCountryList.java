package com.kanchi.periyava.ReceiveRequest;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by m84098 on 9/27/15.
 */

public class ReceiveCountryList {

  @SerializedName("status")
  public String status;

  @SerializedName("code")
  public int code;

  @SerializedName("data")
  public ArrayList<Country> data;

  @SuppressWarnings("serial")
  public static class Country {


    @SerializedName("name")
    public String name;

    @SerializedName("code")
    public String code;

    @SerializedName("states")
    public ArrayList<State> states;

    public static class State {

      @SerializedName("name")
      public String name;

      @SerializedName("code")
      public String code;
    }

  }

  public boolean isSuccess() {
    return status.equalsIgnoreCase("success") ? true : false;
  }

}
