package com.mahaperivaya.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mahaperivaya.Model.SatsangDetails;
import com.mahaperivaya.R;

/**
 * Created by m84098 on 9/14/15.
 */
public class SatsangList extends Fragment {
	public static String TAG = "ReceiveSatsangList";
	// RecyclerView
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		View rootView = inflater.inflate(R.layout.satsanglist, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.listview);

		SatsangDetails[] values = new SatsangDetails[5];
		for (int i = 0; i < 5; i++) {
      values[i] = new SatsangDetails();
			values[i].setName("Maha Perivaya" + i);
			values[i].setDescription("Maha Perivaya Description" + i);
      values[i].setCity("Hartford" + i);
      values[i].setState("CT" + i);
      values[i].setCountry("US" + i);
		}

		ArrayAdapter<SatsangDetails> satsangListAdapter;
		satsangListAdapter = new ArrayAdapter<SatsangDetails>(getActivity(),
				R.layout.satsanglistitem, values);

		listView.setAdapter(satsangListAdapter);

		return rootView;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		/*
		 * mRecyclerView = (RecyclerView)
		 * getView().findViewById(R.id.my_recycler_view);
		 *
		 * // use this setting to improve performance if you know that changes
		 * // in content do not change the layout size of the RecyclerView
		 * mRecyclerView.setHasFixedSize(true);
		 *
		 * // use a linear layout manager mLayoutManager = new
		 * LinearLayoutManager(getActivity());
		 * mRecyclerView.setLayoutManager(mLayoutManager);
		 *
		 * // specify an adapter (see also next example) mAdapter = new
		 * MyAdapter(myDataset); mRecyclerView.setAdapter(mAdapter);
		 */
	}
}
