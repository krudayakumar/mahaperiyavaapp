package com.mahaperivaya.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahaperivaya.R;

/**
 * Created by m84098 on 9/3/15.
 */
public class Setting extends  AppBaseFragement{
        public static String TAG = "Setting";
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.setting, container, false);

            
            getActivity().setTitle(getResources().getString(R.string.lbl_settings));
            return rootView;
        }

}
