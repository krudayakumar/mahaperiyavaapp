package com.kanchi.periyava.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.kanchi.periyava.Activity.MainActivity;
import com.kanchi.periyava.Activity.MBaseActivity;
import com.kanchi.periyava.Interface.DialogActionCallback;

/**
 * Created by m84098 on 10/4/15.
 */
public class AppBaseFragement extends Fragment {

  public void showFragment(Fragment fragment, Bundle args, int layoutId, boolean addToBackStack,
                           String fragmentTag) {
    getBaseActivity().showFragment(fragment, args, layoutId, addToBackStack, fragmentTag);
  }

  public void ShowMessage(MBaseActivity.MessageDisplay msgDisplay, final DialogActionCallback callback) {
    getBaseActivity().ShowMessage(msgDisplay, callback);
  }

  public void ShowSnackBar(Context context, View view, String title, String actiontext,
                           View.OnClickListener listener) {
    getBaseActivity().ShowSnackBar(context, view, title, actiontext, listener);
  }

  public MainActivity getBaseActivity() {
    return ((MainActivity) getActivity());
  }

}


