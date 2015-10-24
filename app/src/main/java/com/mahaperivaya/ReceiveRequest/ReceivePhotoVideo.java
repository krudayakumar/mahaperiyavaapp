package com.mahaperivaya.ReceiveRequest;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by m84098 on 9/27/15.
 */



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by m84098 on 9/27/15.
 */

public class ReceivePhotoVideo {

  @SerializedName("status")
  public String status;

  @SerializedName("code")
  public int code;

  @SerializedName("data")
  public ArrayList<Category> data;

  @SuppressWarnings("serial")
  public static class Category implements Serializable {

    @SerializedName("categorytitle")
    public String categorytitle;

    @SerializedName("categorytype")
    public String categorytype;

    @SerializedName("categories")
    public ArrayList<Categories> sublinks;

    @SuppressWarnings("serial")
    public static class Categories implements Serializable {
      @SerializedName("title")
      public String title;

      @SerializedName("link")
      public String link;

      @SerializedName("subcategory")
      public ArrayList<SubCategories> subcategories;

      public static class SubCategories implements Serializable {
        @SerializedName("title")
        public String title;

        @SerializedName("link")
        public String link;

      }
    }
  }

  public boolean isSuccess() {
    return status.equalsIgnoreCase("success") ? true : false;
  }

}

