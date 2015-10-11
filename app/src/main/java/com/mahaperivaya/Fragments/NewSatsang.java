package com.mahaperivaya.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.ReceiveSatsangList;

import java.util.ArrayList;

/**
 * Created by m84098 on 9/3/15.
 */
public class NewSatsang extends AppBaseFragement {
    public static String TAG = "NewSatsang";
    LinearLayout llMainLayout;
    EditText name, description, personname, contactno, emailid, city, state, country;
    ArrayList<EditText> allControls = new ArrayList<>();
    View rootView;
    String satsangoption = "";

    FloatingActionButton next;
    ReceiveSatsangList.Data data = new ReceiveSatsangList.Data();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.newsatsang, container, false);
        setHasOptionsMenu(true);


        init();
        Bundle bundle = getArguments();
        if (bundle != null) {
            satsangoption = (String) bundle.getString(ConstValues.SATSANG_OPTION);

            if (satsangoption.equalsIgnoreCase("EDIT")) {
                data = (ReceiveSatsangList.Data) bundle.getSerializable(ConstValues.SATSANG_DATA);
                if (data != null) {
                    name.setText(data.name);
                    description.setText(data.description);
                    personname.setText(data.contactpersonname);
                    contactno.setText(data.contactno);
                    emailid.setText(data.emailid);
                    city.setText(data.city);
                    state.setText(data.state);
                    country.setText(data.country);
                }
                setEnableAllControls(false);
                getActivity().setTitle(getResources().getString(R.string.lbl_satsang) + "(" + data.name + ")");


            } else {
                getActivity().setTitle(getResources().getString(R.string.lbl_new_satsang));
                setEnableAllControls(true);

            }

        }
        return rootView;
    }

    void setEnableAllControls(boolean isEnable) {
        for (int i = 0; i < allControls.size(); i++) {
            if (isEnable) {
                (allControls.get(i)).setFocusable(true);
                (allControls.get(i)).setFocusableInTouchMode(true);
            } else {
                (allControls.get(i)).setFocusable(false);
            }

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        getBaseActivity().setMenuOption(menu);
        getBaseActivity().getMenuOption(MainActivity.MenuOptions.FEEDBACK);
        if(UserProfile.getUserProfile().isLoggedIn) {
            if (satsangoption.equalsIgnoreCase("EDIT") && UserProfile.getUserProfile().profileid == data.profileid) {
                getBaseActivity().getMenuVisible(MainActivity.MenuOptions.EDIT);
            }
            if (satsangoption.equalsIgnoreCase("NEW")) {
                getBaseActivity().getMenuVisible(MainActivity.MenuOptions.SAVE);
            }
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                    msg.what = ConstValues.SATSANG_LIST_WITHOUT_LOGIN;
                    getBaseActivity().getFlowHandler().sendMessage(msg);
                    return true;
                }
                return false;
            }


        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit: {
                getBaseActivity().getMenuOption(MainActivity.MenuOptions.EDIT).setVisible(false);
                getBaseActivity().getMenuOption(MainActivity.MenuOptions.SAVE).setVisible(true);
                setEnableAllControls(true);
            }
            break;
            case R.id.save: {
                TextInputLayout layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangName);
                if (TextUtils.isEmpty(name.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    break;
                } else {
                    layout.setError("");
                }
                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangDescription);
                if (TextUtils.isEmpty(description.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    break;
                } else {
                    layout.setError("");
                }
                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangContactPersonName);
                if (TextUtils.isEmpty(personname.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    break;
                } else {
                    layout.setError("");
                }
                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangContactNo);
                if (TextUtils.isEmpty(contactno.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    break;
                } else {
                    layout.setError("");
                }

                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangEmailId);
                if (TextUtils.isEmpty(emailid.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    break;
                } else {
                    layout.setError("");
                }

                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangCountry);
                if (TextUtils.isEmpty(country.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    break;
                } else {
                    layout.setError("");
                }

                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangState);
                if (TextUtils.isEmpty(state.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    break;
                } else {
                    layout.setError("");
                }
                layout = (TextInputLayout) getActivity().findViewById(R.id.layoutSatsangCity);
                if (TextUtils.isEmpty(city.getText().toString())) {
                    layout.setError(
                            getResources().getString(R.string.err_field_should_not_be_empty));
                    break;
                } else {
                    layout.setError("");
                }
                if (satsangoption.equalsIgnoreCase("NEW")) {
                    data.satsangid = 0;
                    data.profileid = UserProfile.getUserProfile().profileid;
                }
                data.name = name.getText().toString();
                data.description = description.getText().toString();
                data.contactpersonname = personname.getText().toString();
                data.contactno = contactno.getText().toString();
                data.emailid = emailid.getText().toString();
                data.city = city.getText().toString();
                data.state = state.getText().toString();
                data.country = country.getText().toString();
                //Sending the Message
                android.os.Message msg = android.os.Message.obtain();
                msg.what = ConstValues.NEW_SATSANG_SAVE;
                msg.obj = (Object) data;
                getBaseActivity().getFlowHandler().sendMessage(msg);
            }
            break;
            default:
                break;
        }

        return false;
    }

    private void init() {
        llMainLayout = (LinearLayout) rootView.findViewById(R.id.llmain);
        name = (EditText) rootView.findViewById(R.id.edSatsangName);
        description = (EditText) rootView.findViewById(R.id.edSatsangDescription);
        personname = (EditText) rootView.findViewById(R.id.edSatsangContactPersonName);
        contactno = (EditText) rootView.findViewById(R.id.edSatsangContactNo);
        emailid = (EditText) rootView.findViewById(R.id.edSatsangEmailId);
        country = (EditText) rootView.findViewById(R.id.edSatsangCountry);
        state = (EditText) rootView.findViewById(R.id.edSatsangState);
        city = (EditText) rootView.findViewById(R.id.edSatsangCity);
        allControls.add(name);
        allControls.add(description);
        allControls.add(personname);
        allControls.add(contactno);
        allControls.add(emailid);
        allControls.add(country);
        allControls.add(state);
        allControls.add(city);

    }


}
