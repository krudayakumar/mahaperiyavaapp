package com.kanchi.periyava.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kanchi.periyava.R;

/**
 * Created by m84098 on 9/14/15.
 */
public class Satsang extends Fragment {
  public static String TAG = "ReceiveSatsangList";
  //RecyclerView
  private RecyclerView mRecyclerView;
  private RecyclerView.Adapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    Bundle bundle = getArguments();
    View rootView = inflater.inflate(R.layout.webpage, container, false);

		/*setContentView(R.layout.my_activity);
    mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		// specify an adapter (see also next example)
		mAdapter = new MyAdapter(myDataset);
		mRecyclerView.setAdapter(mAdapter);*/

    return rootView;

  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
   /* mRecyclerView = (RecyclerView) getView().findViewById(R.id.my_recycler_view);

    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    mRecyclerView.setHasFixedSize(true);

    // use a linear layout manager
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);

    // specify an adapter (see also next example)
    mAdapter = new MyAdapter(myDataset);
    mRecyclerView.setAdapter(mAdapter);*/
  }
}
