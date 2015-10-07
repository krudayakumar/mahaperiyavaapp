package com.mahaperivaya.Adapters;

/**
 * Created by m84098 on 9/24/15.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mahaperivaya.Model.SatsangDetails;
import com.mahaperivaya.R;

public class SatsangListAdapter extends ArrayAdapter<SatsangDetails> {

	private final Activity context;
	private final SatsangDetails[] satsangDetails;

	static class ViewHolder {
		public TextView satsangtitle, satsangcontent, satsanglocation;
		public Button satsangjoin;
	}

	public SatsangListAdapter(Activity context, SatsangDetails[] satsangDetails) {
		super(context, R.layout.satsanglistitem);
		this.context = context;
		this.satsangDetails = satsangDetails;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		// reuse views
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.satsanglistitem, null);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.satsangtitle = (TextView) rowView.findViewById(R.id.satsangtitle);
			viewHolder.satsangcontent = (TextView) rowView.findViewById(R.id.satsangcontent);
			viewHolder.satsanglocation = (TextView) rowView.findViewById(R.id.satsanglocation);
			viewHolder.satsangjoin = (Button) rowView.findViewById(R.id.satsangjoin);
			rowView.setTag(viewHolder);
		}

		// fill data
		ViewHolder holder = (ViewHolder) rowView.getTag();
		SatsangDetails satsangdet = satsangDetails[position];
		holder.satsangtitle.setText(satsangdet.getName());
		holder.satsangcontent.setText(satsangdet.getDescription());
		holder.satsanglocation.setText(
				satsangdet.getCity() + "," + satsangdet.getState() + "," + satsangdet.getCountry());

		return rowView;
	}

}
