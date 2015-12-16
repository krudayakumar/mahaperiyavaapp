package com.kanchi.periyava.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by m84098 on 9/14/15.
 */
public class GeneralSetting {

  @SerializedName("version")
  public String version;

  @SerializedName("feedbackemailid")
  public String feedbackemailid = "info@kgpfoundation.org";

  private static GeneralSetting generalSetting;

  public static GeneralSetting getInstance() {
    if (generalSetting == null) {
      generalSetting = new GeneralSetting();
    }

    return generalSetting;
  }

  public static void setInstance(GeneralSetting generalsetting) {
    if (generalsetting == null) {
      generalSetting = generalsetting;
    }
  }

  public static GeneralSetting getGeneralSetting() {
    return generalSetting;
  }


  public GeneralSetting() {
  }

  public void clearAll() {
    feedbackemailid = "";
  }


}