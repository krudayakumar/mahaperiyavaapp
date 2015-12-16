package com.kanchi.periyava.Fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kanchi.periyava.Adapters.AcharayasListAdapter;
import com.kanchi.periyava.Adapters.SatsangListAdapter;
import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.R;

/**
 * Created by m84098 on 9/3/15.
 */
public class AboutAcharayas extends AppBaseFragement {
  public static String TAG = "AboutAcharayas";
  View rootView;
  String strTitle;
  String strContent;
  int imgView;
  private RecyclerView recyclerView;
  private AcharayasListAdapter acharayasListAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.about_acharayas, container, false);

    Bundle bundle = getArguments();
    recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
    recyclerView.setLayoutManager(layoutManager);
    acharayasListAdapter = new AcharayasListAdapter(getBaseActivity());
    recyclerView.setAdapter(acharayasListAdapter);

    //initCompontents();
    return rootView;
  }


  private void initCompontents() {
    getActivity().setTitle(strTitle);

    ((ImageView) rootView.findViewById(R.id.imgView))
        .setImageResource(imgView);

    ((TextView) rootView.findViewById(R.id.content))
        .setText(Html.fromHtml(strContent));
  }

}
