package com.kanchi.periyava.old.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kanchi.periyava.old.Activity.MainActivity;
import com.kanchi.periyava.old.Adapters.SatsangListAdapter;
import com.kanchi.periyava.old.Interface.ServerCallback;
import com.kanchi.periyava.old.Model.ConstValues;
import com.kanchi.periyava.old.Model.UserProfile;
import com.kanchi.periyava.R;
import com.kanchi.periyava.old.ReceiveRequest.GeneralReceiveRequest;
import com.kanchi.periyava.old.ReceiveRequest.ReceiveSatsangList;

import org.json.JSONObject;

/**
 * Created by m84098 on 9/14/15.
 */
public class SatsangList extends AppBaseFragement {
  public static String TAG = "ReceiveSatsangList";
  private RecyclerView recyclerView;
  private SatsangListAdapter satsangListAdapter;
  View rootView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    getActivity().setTitle(getResources().getString(R.string.lbl_satsang));
    rootView = inflater.inflate(R.layout.satsanglist, container, false);
    recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
    recyclerView.setLayoutManager(layoutManager);
    setHasOptionsMenu(true);
    ServerCallback serverCallback = new ServerCallback() {
      @Override
      public void onSuccess(JSONObject response) {
        GeneralReceiveRequest generalReceive = new Gson().fromJson(response.toString(),
            GeneralReceiveRequest.class);

        if (generalReceive.isSuccess()) {
          ReceiveSatsangList generalReceiveRequest = new Gson().fromJson(response.toString(),
              ReceiveSatsangList.class);
          satsangListAdapter = new SatsangListAdapter(getBaseActivity(), generalReceiveRequest.data);
          recyclerView.setAdapter(satsangListAdapter);


        } else {
          android.os.Message msg = android.os.Message.obtain();
          msg.what = ConstValues.SATSANG_LIST_SERVER_ERROR;
          msg.obj = (Object) response;
          getFlowHandler().sendMessage(msg);
        }


      }

      @Override
      public void onError(VolleyError error) {
        error.printStackTrace();
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.ERROR_DEFAULT;
        getFlowHandler().sendMessage(msg);
      }
    };

    //Sending the Message
    android.os.Message msg = android.os.Message.obtain();
    msg.what = ConstValues.SATSANG_LIST_SERVER_REQUEST;
    msg.obj = (Object) serverCallback;
    getFlowHandler().sendMessage(msg);

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
          getFlowHandler().sendMessage(msg);
          return true;
        }
        return false;
      }


    });
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Do something that differs the Activity's menu here
    super.onCreateOptionsMenu(menu, inflater);
    if (UserProfile.getUserProfile().isLoggedIn) {
      getBaseActivity().getMenuOption(MainActivity.MenuOptions.ADD_NEW).setVisible(true);
      getBaseActivity().getMenuOption(MainActivity.MenuOptions.ADD_NEW).setTitle(getResources().getString(R.string.lbl_new_satsang));
    }


    // Associate searchable configuration with the SearchView
    // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    /*SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
    // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });*/
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.addnew: {
        //Sending the Message
        android.os.Message msg = android.os.Message.obtain();
        msg.what = ConstValues.NEW_SATSANG;
        getFlowHandler().sendMessage(msg);
      }
      break;
      default:
        break;
    }

    return false;
  }


  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {

  }
}
