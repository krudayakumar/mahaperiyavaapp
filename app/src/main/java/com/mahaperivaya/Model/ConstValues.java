/**
 * Copyright (c) 2012-2014 Kofax. Use of this code is with permission pursuant to Kofax license terms.
 */

package com.mahaperivaya.Model;

/// The ConstValues class provides constant values and the static ApplicationState class.

/**
 * This class is responsible for providing constant values to the Application
 * and ApplicationState classes. It provides the current status of the
 * application.
 */

public class ConstValues {

  // final
  public final static int WELCOME = 100;
  public final static int LOGIN = 101;
  public final static int LOGIN_SERVE_REQUEST = LOGIN+ 1;
  public final static int LOGIN_SUCCESSFUL = LOGIN+ 2;
  public final static int LOGIN_ERROR = LOGIN+ 3;


  public final static int REGISTER = 110;
  public final static int REGISTER_SERVER_REQUEST = REGISTER+ 1;
  public final static int REGISTER_SUCCESS= REGISTER+ 2;
  public final static int REGISTER_ERROR = REGISTER+ 3;


  public final static int FORGOT_PASSWORD = 120;
  public final static int FORGOT_PASSWORD_SERVER_REQUEST = FORGOT_PASSWORD+ 1;
  public final static int FORGOT_PASSWORD_SUCCESS= FORGOT_PASSWORD+ 2;
  public final static int FORGOT_PASSWORD_ERROR = FORGOT_PASSWORD+ 3;


  public final static int SET_PASSWORD = 130;
  public final static int SET_PASSWORD_SERVER_REQUEST = SET_PASSWORD+ 1;
  public final static int SET_PASSWORD_SUCCESS= SET_PASSWORD+ 2;
  public final static int SET_PASSWORD_ERROR = SET_PASSWORD+ 3;
  public final static int SET_PASSWORD_SUCCESS_LOGIN= SET_PASSWORD+ 2;

  public final static int DASHBOARD = 140;
  public final static int DASHBOARD_WITHOUT_LOGIN = DASHBOARD+ 1;
  public final static int DASHBOARD_LOGIN = DASHBOARD+ 2;


  public final static int DEIVATHIN_KURAL = 150;

  public final static int SATSANG = 160;
  public final static int SATSANG_LIST_LOGIN = SATSANG + 1;
  public final static int SATSANG_LIST_WITHOUT_LOGIN = SATSANG + 2;
  public final static int SATSANG_LIST_SERVER_REQUEST = SATSANG + 3;
  public final static int SATSANG_LIST_SERVER_SUCCESS = SATSANG + 4;
  public final static int SATSANG_LIST_SERVER_ERROR = SATSANG + 5;


  public final static int NEW_SATSANG = 170;
  public final static int NEW_SATSANG_SERVER_REQUEST = NEW_SATSANG + 1;
  public final static int NEW_SATSANG_SUCCESS= NEW_SATSANG+ 2;
  public final static int NEW_SATSANG_ERROR = NEW_SATSANG+ 3;
  public final static int NEW_SATSANG_SAVE = NEW_SATSANG+ 4;
  public final static int NEW_SATSANG_SAVE_SUCCESS = NEW_SATSANG+ 5;
  public final static int NEW_SATSANG_SAVE_ERROR = NEW_SATSANG+ 6;

  public final static int EDIT_SATSANG = 180;
  public final static int EDIT_SATSANG_SERVER_REQUEST = NEW_SATSANG + 1;
  public final static int EDIT_SATSANG_SUCCESS= NEW_SATSANG+ 2;
  public final static int EDIT_SATSANG_ERROR = NEW_SATSANG+ 3;




  public final static int COUNTRY_LIST = 190;
  public final static int COUNTRY_LIST_SERVER_REQUEST = COUNTRY_LIST + 1;
  public final static int COUNTRY_LIST_SUCCESS= COUNTRY_LIST+ 2;
  public final static int COUNTRY_LIST_ERROR = COUNTRY_LIST+ 3;


  public final static int JAPAM = 200;
  public final static int JAPAM_SERVER_REQUEST = JAPAM + 1;
  public final static int JAPAM_SUCCESS= JAPAM+ 2;
  public final static int JAPAM_ERROR = JAPAM+ 3;


  public final static int PHOTO_VIDEO_LIST = 210;
  public final static int PHOTO_VIDEO_LIST_SERVER_REQUEST = PHOTO_VIDEO_LIST + 1;
  public final static int PHOTO_VIDEO_LIST_SUCCESS= PHOTO_VIDEO_LIST+ 2;
  public final static int PHOTO_VIDEO_LIST_ERROR = PHOTO_VIDEO_LIST+ 3;


  public final static int PHOTO_LIST = 220;

  public final static int VIDEO_LIST = 230;
  public final static int VIDEO_OPEN = VIDEO_LIST+1;




  public final static int JOIN_SATSANG = 240;
  public final static int JOIN_SATSANG_SERVER_REQUEST = JOIN_SATSANG + 1;
  public final static int JOIN_SATSANG_SUCCESS= JOIN_SATSANG+ 2;
  public final static int JOIN_SATSANG_ERROR = JOIN_SATSANG+ 3;

  public final static int BOOKS_LIST = 250;

  public final static int WEB_PAGE = 260;

  public final static int ERROR = 500;
  public final static int ERROR_DEFAULT = ERROR+ 1;

  public final static String SATSANG_DATA ="SATSANG_DATA";
  public final static String SATSANG_OPTION="SATSANG_OPTION";
  public final static String VIDEO_PHOTO_OPTION="VIDEO_PHOTO_OPTION";

  public final static String CONST_VIDEO="VIDEOS";
  public final static String CONST_PHOTO="PHOTO";
  public final static String CONST_BOOKS="BOOKS";

}
