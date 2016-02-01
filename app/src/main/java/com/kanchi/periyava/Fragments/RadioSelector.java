/*
 * Copyright 2015 Steven Mulder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kanchi.periyava.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.R;

public class RadioSelector extends AppBaseFragement {
	public static String TAG = "RadioSelector";
	WebView wv;
	View rootView;
	Menu menu;
	ImageButton btnRadio;
	static String strPlaylist;
	TextView tvPlayList, tvScheduleList;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		rootView = inflater.inflate(R.layout.radioserver_select, container, false);
		Log.d("inside Radio selector", "on create");

		Button IndButton, RwButton;
		// setContentView(R.layout.radioserver_select);
		IndButton = (Button) rootView.findViewById(R.id.bIndia);
		RwButton = (Button) rootView.findViewById(R.id.bRw);

		IndButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				android.os.Message msg = android.os.Message.obtain();
				Log.d("inside on click", "on Indbutton");
				msg.what = ConstValues.RADIOSELECTOR;
				Log.d(String.valueOf(msg.what), "on Indbutton");

				Uri uri = Uri.parse(getResources().getString(R.string.link_radio_india));
				msg.obj = uri;
				Log.d(String.valueOf(uri), "uri in onclick IND");
				Log.d(uri.toString(), "Radio URL");
				// msg.obj = uri.toString();
				// msg= (android.os.Message) msg.obj;
				Log.d(String.valueOf(msg), "Radio URL 2");
				getBaseActivity().getFlowHandler().sendMessage(msg);
			}
		});
		RwButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				android.os.Message msg = android.os.Message.obtain();
				msg.what = ConstValues.RADIOSELECTOR;
				Uri uri = Uri.parse(getResources().getString(R.string.link_radio_others));
				msg.obj = uri;
				Log.d(uri.toString(), "Radio URL");
				getBaseActivity().getFlowHandler().sendMessage(msg);

			}
		});

		// Toast.makeText(getActivity(), "I'm in the RadioSelector View",
		// Toast.LENGTH_LONG).show();

		return rootView;

	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
	}

	@Override
	public void onDetach() {

		super.onDetach();
	}

}
