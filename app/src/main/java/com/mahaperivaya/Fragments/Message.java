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
public class Message extends  Fragment{



        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Message newInstance(int sectionNumber) {
            Message fragment = new Message();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public Message() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.message, container, false);
            getActivity().setTitle(getResources().getString(R.string.lbl_message));
            return rootView;
        }
 
}
