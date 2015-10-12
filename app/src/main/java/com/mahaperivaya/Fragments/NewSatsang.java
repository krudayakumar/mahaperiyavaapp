package com.mahaperivaya.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Interface.ServerCallback;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.ReceiveCountryList;
import com.mahaperivaya.ReceiveRequest.ReceiveSatsangList;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by m84098 on 9/3/15.
 */
public class NewSatsang extends AppBaseFragement {
    public static String TAG = "NewSatsang";
    LinearLayout llMainLayout;
    EditText name, description, personname, contactno, emailid, city, country, state;
    ArrayList<EditText> allControls = new ArrayList<>();
    View rootView;
    String satsangoption = "";
    LinkedHashMap<String, ReceiveCountryList.Country> countryNameMap = new LinkedHashMap<String, ReceiveCountryList.Country>();
    LinkedHashMap<String, ReceiveCountryList.Country> countryCodeMap = new LinkedHashMap<String, ReceiveCountryList.Country>();
    List<String> countries = new ArrayList<String>();
    LinkedHashMap<String, ReceiveCountryList.Country.State> stateNameMap = new LinkedHashMap<String, ReceiveCountryList.Country.State>();
    LinkedHashMap<String, ReceiveCountryList.Country.State> stateCodeMap = new LinkedHashMap<String, ReceiveCountryList.Country.State>();
    List<String> states = new ArrayList<String>();

    FloatingActionButton next;
    ReceiveSatsangList.Data data = new ReceiveSatsangList.Data();
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.newsatsang, container, false);
        context = container.getContext();
        setHasOptionsMenu(true);
        init();

        ServerCallback serverCallback = new ServerCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                ReceiveCountryList receiveCountryList = new Gson().fromJson(response.toString(),
                        ReceiveCountryList.class);

                if (receiveCountryList.isSuccess()) {
                    for (ReceiveCountryList.Country country : receiveCountryList.data) {
                        countryNameMap.put(country.name, country);
                        countryCodeMap.put(country.code,country);
                        Log.d(TAG, country.name);
                        if(country.name.equalsIgnoreCase("india")) {
                            Log.d(TAG,  new Gson().toJson(country.states).toString());
                        }
                        countries.add(country.name);
                    }
                } else {
                    android.os.Message msg = android.os.Message.obtain();
                    msg.what = ConstValues.ERROR_DEFAULT;
                    getBaseActivity().getFlowHandler().sendMessage(msg);
                }


            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
                android.os.Message msg = android.os.Message.obtain();
                msg.what = ConstValues.ERROR_DEFAULT;
                getBaseActivity().getFlowHandler().sendMessage(msg);
            }
        };

        //Sending the Message
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.COUNTRY_LIST;
        msg.obj = (Object) serverCallback;
        getBaseActivity().getFlowHandler().sendMessage(msg);


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

                    if(countryCodeMap.get(data.country)!=null) {
                        country.setTag(countryCodeMap.get(data.country));

                        ReceiveCountryList.Country tempCountry = (ReceiveCountryList.Country) country.getTag();
                        if (tempCountry != null) {
                            country.setText(tempCountry.name);
                            for (ReceiveCountryList.Country.State state : tempCountry.states) {
                                stateNameMap.put(state.name, state);
                                stateCodeMap.put(state.code, state);
                                states.add(state.name);
                            }
                        }

                        if(tempCountry.states.size()!=0) {
                            ReceiveCountryList.Country.State tempstate = stateCodeMap.get(data.state);
                            if(tempstate!=null) {
                                state.setTag(tempstate);
                                state.setText(tempstate.name);
                            }

                        }else {
                            state.setText(data.state);
                        }

                    }


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



   /* private AdapterView.OnItemClickListener countryClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = parent.getItemAtPosition(position).toString();

        }
    };*/

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
        if (UserProfile.getUserProfile().isLoggedIn) {
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


        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state.setFocusable(false);
                displayCountryStateDialog(SelectionType.COUNTRY);

            }
        });
        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCountryStateDialog(SelectionType.STATE);
            }
        });


        allControls.add(name);
        allControls.add(description);
        allControls.add(personname);
        allControls.add(contactno);
        allControls.add(emailid);
        //allControls.add(country);
        //allControls.add(state);
        allControls.add(city);

    }

    private enum SelectionType {
        COUNTRY,
        STATE,
    }

    ;

    private void countryDialog() {


    }


    private void displayCountryStateDialog(final SelectionType selectionType) {

        //final String[] storeNameArray = new String[countryNameMap.keySet().toArray().length];
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
        //final String[] storeNames;


        switch (selectionType) {
            case COUNTRY:
                builder.setTitle(getBaseActivity().getResources().getString(R.string.lbl_help_country));
                builder.setItems(countryNameMap.keySet().toArray(new String[0]), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        country.setText(countries.get(which ));
                        country.setTag(countryNameMap.get(country.getText().toString()));
                        state.setText("");
                    }
                });

                break;
            case STATE:
                builder.setTitle(getBaseActivity().getResources().getString(R.string.lbl_help_state));

                if (TextUtils.isEmpty(country.getText().toString())) {
                    getBaseActivity().ShowSnackBar(context, getBaseActivity().getWindow().getDecorView(), getResources().getString(R.string.msg_select_country), null, null);
                    return;
                }
                ReceiveCountryList.Country selectedcountry = (ReceiveCountryList.Country)country.getTag();
                //ReceiveCountryList.Country selectedcountry = countryNameMap.get(country.getText().toString());
                if (selectedcountry.states != null) {
                    if (selectedcountry.states.size() != 0) {
                        stateCodeMap.clear();
                        stateNameMap.clear();
                        for (ReceiveCountryList.Country.State state : selectedcountry.states) {
                            stateNameMap.put(state.name, state);
                            stateCodeMap.put(state.code,state);
                            states.add(state.name);
                        }
                        builder.setItems(stateNameMap.keySet().toArray(new String[0]), new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                state.setText(states.get(which));
                                state.setTag(states.get(which ));

                            }
                        });
                    }
                } else {
                    state.setFocusable(true);
                    state.setFocusableInTouchMode(true);
                    return;
                }


                break;
        }


        AlertDialog alert = builder.create();
        //storeNames.clear();
        alert.setCancelable(false);
        alert.show();
    }

}
