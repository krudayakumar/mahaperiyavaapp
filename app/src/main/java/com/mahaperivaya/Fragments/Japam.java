package com.mahaperivaya.Fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;

/**
 * Created by m84098 on 9/3/15.
 */
public class Japam extends AppBaseFragement {
	public static String TAG = "Japam";

	TextView japam_count_over_all, japam_count_satsang, japam_count, japam_last_updated_date;
	EditText edCount, edDate;

	View rootView;

	FloatingActionButton next;
	Calendar japam_update_date = Calendar.getInstance();
	DatePickerDialog.OnDateSetListener japam_date = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			japam_update_date.set(Calendar.YEAR, year);
			japam_update_date.set(Calendar.MONTH, monthOfYear);
			japam_update_date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateLabel();
		}
	};

	private void updateLabel() {
		String myFormat = "dd/MMM/yyyy"; // "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		edDate.setText(sdf.format(japam_update_date.getTime()));

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.japam, container, false);
		getActivity().setTitle(getResources().getString(R.string.lbl_japam));
		init();
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		getView().setFocusableInTouchMode(true);
		getView().requestFocus();
		getView().setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
					// handle back button's click listener
					android.os.Message msg = android.os.Message.obtain();
					if (UserProfile.getUserProfile().isLoggedIn) {
						msg.what = ConstValues.DASHBOARD_LOGIN;
					} else {
						msg.what = ConstValues.DASHBOARD_WITHOUT_LOGIN;
					}
					getBaseActivity().getFlowHandler().sendMessage(msg);
					return true;
				}
				return false;
			}


		});
	}

	private void init() {
		japam_count_over_all = (TextView) rootView.findViewById(R.id.japam_count_over_all);
		japam_count_satsang = (TextView) rootView.findViewById(R.id.japam_count_satsang);
		japam_count = (TextView) rootView.findViewById(R.id.japam_count);
		japam_last_updated_date = (TextView) rootView.findViewById(R.id.japam_last_updated_date);

		japam_count_over_all.setText("" + UserProfile.getUserProfile().japam_count_over_all);
		japam_count_satsang.setText("" + UserProfile.getUserProfile().japam_count_satsang);
		japam_count.setText("" + UserProfile.getUserProfile().japam_count);
		japam_last_updated_date.setText(UserProfile.getUserProfile().japam_last_updated_date);

		edCount = (EditText) rootView.findViewById(R.id.edCount);
		edDate = (EditText) rootView.findViewById(R.id.edDate);
		edDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), japam_date,
						japam_update_date.get(Calendar.YEAR), japam_update_date.get(Calendar.MONTH),
						japam_update_date.get(Calendar.DAY_OF_MONTH));

				japam_update_date.add(Calendar.MONTH, 7);

				datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
				datePickerDialog.getDatePicker().setMaxDate(japam_update_date.getTimeInMillis());
				datePickerDialog.show();
			}
		});

		next = (FloatingActionButton) rootView.findViewById(R.id.next);
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TextInputLayout layout = (TextInputLayout) getActivity()
						.findViewById(R.id.layoutCount);
				if (Integer.parseInt(edCount.getText().toString()) > 0
						&& Integer.parseInt(edCount.getText().toString()) < Integer.MAX_VALUE) {
					layout.setError(getResources().getString(R.string.err_enter_right_count));
					return;
				} else {
					layout.setError("");
				}

				layout = (TextInputLayout) getActivity().findViewById(R.id.layoutDate);
				if (TextUtils.isEmpty(edDate.getText().toString())) {
					layout.setError(
							getResources().getString(R.string.err_field_should_not_be_empty));
					return;
				} else {
					layout.setError("");
				}

			}
		});
	}

}
