package com.mahaperivaya.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahaperivaya.R;

/**
 * Created by m84098 on 9/3/15.
 */
public class About extends  Fragment{
        public static String TAG = "About";
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.about, container, false);
            getActivity().setTitle(getResources().getString(R.string.lbl_about));
            return rootView;
        }
 
}
