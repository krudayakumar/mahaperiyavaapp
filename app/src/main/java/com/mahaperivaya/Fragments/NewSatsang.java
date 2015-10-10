package com.mahaperivaya.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mahaperivaya.Activity.MBaseActivity;
import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Interface.DialogActionCallback;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.ReceiveSatsangList;

/**
 * Created by m84098 on 9/3/15.
 */
public class NewSatsang extends AppBaseFragement {
    public static String TAG = "NewSatsang";
    EditText name, description, personname, contactno, emailid, city, state, country;
    View rootView;

    FloatingActionButton next;
    ReceiveSatsangList.Data data;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.newsatsang, container, false);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        data= (ReceiveSatsangList.Data) bundle.getSerializable(ConstValues.DATA_SATSANG);
        getActivity().setTitle(getResources().getString(R.string.lbl_new_satsang));
        init();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        getBaseActivity().getMenuOption(MainActivity.MenuOptions.FEEDBACK);
        //if (UserProfile.getUserProfile().isLoggedIn)
        {

            getBaseActivity().getMenuOption(MainActivity.MenuOptions.ADD_NEW);
            //if(data.profileid == UserProfile.getUserProfile().profileid)
            {
                //getBaseActivity().getMenuOption().getItem(1).setVisible(true);
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save: {
                //Sending the Message
                android.os.Message msg = android.os.Message.obtain();
                msg.what = ConstValues.NEW_SATSANG_SAVE;
                getBaseActivity().getFlowHandler().sendMessage(msg);
            }
            break;
            default:
                break;
        }

        return false;
    }

    private void init() {
        name = (EditText) rootView.findViewById(R.id.edSatsangName);
        description = (EditText) rootView.findViewById(R.id.edSatsangDescription);
        personname = (EditText) rootView.findViewById(R.id.edSatsangContactPersonName);
        contactno = (EditText) rootView.findViewById(R.id.edSatsangContactNo);
        emailid = (EditText) rootView.findViewById(R.id.edSatsangEmailId);
        country = (EditText) rootView.findViewById(R.id.edSatsangCountry);
        state = (EditText) rootView.findViewById(R.id.edSatsangState);
        city = (EditText) rootView.findViewById(R.id.edSatsangCity);
        next = (FloatingActionButton) rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangName);
                if (TextUtils.isEmpty(name.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    return;
                } else {
                    layout.setError("");
                }
                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangDescription);
                if (TextUtils.isEmpty(description.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    return;
                } else {
                    layout.setError("");
                }
                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangContactPersonName);
                if (TextUtils.isEmpty(personname.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    return;
                } else {
                    layout.setError("");
                }
                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangContactNo);
                if (TextUtils.isEmpty(contactno.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    return;
                } else {
                    layout.setError("");
                }

                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangEmailId);
                if (TextUtils.isEmpty(emailid.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    return;
                } else {
                    layout.setError("");
                }

                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangCountry);
                if (TextUtils.isEmpty(country.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    return;
                } else {
                    layout.setError("");
                }

                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangState);
                if (TextUtils.isEmpty(state.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    return;
                } else {
                    layout.setError("");
                }
                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangCity);
                if (TextUtils.isEmpty(city.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    return;
                } else {
                    layout.setError("");
                }

                //Server Call
                ((MBaseActivity) getActivity()).ShowMessage(MBaseActivity.MessageDisplay.NEW_SATSANG_SUCCESSFUL, new DialogActionCallback() {
                    @Override
                    public void onOKClick(String errorCode) {

                    }

                    @Override
                    public void onCancelClick(String errorCode) {

                    }
                });

            }
        });
    }


}
