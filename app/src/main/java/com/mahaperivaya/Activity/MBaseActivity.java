package com.mahaperivaya.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.mahaperivaya.Interface.DialogActionCallback;
import com.mahaperivaya.R;

/**
 * Created by m84098 on 9/7/15.
 */
public abstract class MBaseActivity extends AppCompatActivity implements View.OnClickListener {
  public static String TAG = MBaseActivity.class.getSimpleName();


  public enum MessageDisplay {
    DEFAULT_ERROR,
    REGISTRATION_ACCOUNT_ALREADY,
    REGISTRATION_SUCCESSFUL,
    DO_YOU_WANT_EXIT,
    NEW_SATSANG_SUCCESSFUL,
    FORGOT_PASSWORD
  }


  public void getScreenMode(boolean isFullScreen) {

    if (isFullScreen) {
      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    } else {
      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

    }
  }

  ;

  @Override
  public void onClick(View v) {

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (getFragmentManager().getBackStackEntryCount() != 0) {
      getFragmentManager().popBackStack();
    }
    overridePendingTransition(R.animator.in_right, R.animator.out_right);
  }

  public void hideActionBar() {
    ViewGroup container = (ViewGroup) getWindow().getDecorView().getRootView();
    container.setPersistentDrawingCache(ViewGroup.PERSISTENT_ALL_CACHES);
    getSupportActionBar().hide();
  }

  public void unhideActionBar() {
    ViewGroup container = (ViewGroup) getWindow().getDecorView().getRootView();
    container.setPersistentDrawingCache(ViewGroup.PERSISTENT_ALL_CACHES);
    getSupportActionBar().show();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
  }

  public void showActivity(Class<?> cls, Context context, Bundle args,
                           boolean closePreviousActivity) {
    if (cls == null)
      return;

    Intent intent = new Intent(context, cls);

    if (args != null) {
      intent.putExtras(args);
    }
    context.startActivity(intent);
    if (closePreviousActivity) {
      ((Activity) context).finish();
    }
  }

  public void showFragment(Fragment fragment, Bundle args, int layoutId, boolean addToBackStack,
                           String fragmentTag) {
    getFragmentManager()
        .addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

          @Override
          public void onBackStackChanged() {
            // TODO Auto-generated method stub
            System.out.println("BACK STACK CHANGED");
            System.out.println(
                "COUNT: " + getSupportFragmentManager().getBackStackEntryCount());
          }

        });
    Log.i(TAG, "showFragment - " + fragment.getClass().getSimpleName());

    showChildFragment(getFragmentManager(), fragment, args, layoutId, addToBackStack,
        R.animator.fade_in, R.animator.fade_out, R.animator.fade_in, R.animator.fade_out,
        fragmentTag);
  }

  /**
   * Displays requested fragment.
   *
   * @param fragment to be displayed
   * @param args     any arguments applicable to the fragment
   */
  private void showChildFragment(FragmentManager fragmentManager, android.app.Fragment fragment,
                                 Bundle args, int layoutId, boolean addToBackStack, int enterAnim, int exitAnim,
                                 int popEnterAnim, int popExitAnim, String fragmentTag) {
    Log.i(TAG, "showChildFragment - " + fragment.getClass().getSimpleName());

    if (args != null)
      fragment.setArguments(args);

    android.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

    transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);

    // replace whatever is in the fragment_container view with this fragment
    if (fragmentTag == null)
      transaction.replace(layoutId, fragment);
    else
      transaction.replace(layoutId, fragment, fragmentTag);

    // add the transaction to the back stack so the user can navigate back
    if (addToBackStack)
      transaction.addToBackStack(null);

    // Commit the transaction
    transaction.commit();
  }

  public void ShowMessage() {

  }

  public void ShowMessage(MessageDisplay msgDisplay, final DialogActionCallback callback) {
    String strTitle;
    String strMessage;
    final String strErrorCode;
    String strOkButtonText;
    String strCancelButtonText;
    String strOkButtonURL;
    String strCancelButtonURL;

    strTitle = strMessage = strErrorCode = strOkButtonText = strCancelButtonText = strOkButtonURL = strCancelButtonURL = null;
    switch (msgDisplay) {
      case DEFAULT_ERROR:
        strMessage = getResources().getString(R.string.err_default);
        strOkButtonText = getResources().getString(R.string.action_ok);
        break;
      case REGISTRATION_ACCOUNT_ALREADY:
        strMessage = getResources().getString(R.string.msg_already_registred);
        strOkButtonText = getResources().getString(R.string.action_ok);
        break;
      case REGISTRATION_SUCCESSFUL:
        strMessage = getResources().getString(R.string.msg_successfully_registred);
        strOkButtonText = getResources().getString(R.string.action_login);
        break;
      case NEW_SATSANG_SUCCESSFUL:
        strMessage = getResources().getString(R.string.msg_successfully_satsang);
        strOkButtonText = getResources().getString(R.string.action_ok);
        break;
      case DO_YOU_WANT_EXIT:
        strMessage = getResources().getString(R.string.msg_do_you_want_exit);
        strOkButtonText = getResources().getString(R.string.action_exit);
        strCancelButtonText = getResources().getString(R.string.action_cancel);
        break;
      case FORGOT_PASSWORD:
        strMessage = getResources().getString(R.string.msg_password_mail_email);
        strOkButtonText = getResources().getString(R.string.action_ok);
        break;
      default: {
        throw new IllegalArgumentException("Invalid Message " + msgDisplay);
      }

    }

		/*
     * showErrorDialog(strTitle, strMessage, strErrorCode, strOkButtonText,
		 * strCancelButtonText, strOkButtonURL, strCancelButtonURL, callback);
		 */

    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

    if ((strTitle != null)) {
      alertDialogBuilder.setTitle(strTitle);
    }

    if (strMessage != null) {
      alertDialogBuilder.setMessage(strMessage);
    }

    if (strOkButtonText != null) {
      alertDialogBuilder.setPositiveButton(strOkButtonText,
          new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              if (callback != null) {
                callback.onOKClick(strErrorCode);
              }
              dialog.dismiss();

            }
          });
    }
    if (strCancelButtonText != null) {
      alertDialogBuilder.setNegativeButton(strCancelButtonText,
          new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              if (callback != null) {
                callback.onCancelClick(strErrorCode);
              }
              dialog.dismiss();

            }
          });
    }

    AlertDialog dialog = alertDialogBuilder.create();
    dialog.setCancelable(false);
    // dialog.setCanceledOnTouchOutside(false);
    dialog.show();
    dialog.getButton(dialog.BUTTON_NEGATIVE)
        .setTextColor(getResources().getColor(R.color.primary));
    dialog.getButton(dialog.BUTTON_POSITIVE)
        .setTextColor(getResources().getColor(R.color.primary));
  }

  public void ShowSnackBar(Context context, View view, String title, String actiontext,
                           View.OnClickListener listener) {
    InputMethodManager imm = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(
        ((Activity) context).getCurrentFocus().getWindowToken(), 0);

    Snackbar snackbar = Snackbar.make(view, title, Snackbar.LENGTH_LONG);
    if (actiontext != null && listener != null) {
      snackbar.setAction(actiontext, listener);
      snackbar.setActionTextColor(Color.RED);
    }
    snackbar.show();

  }

}
