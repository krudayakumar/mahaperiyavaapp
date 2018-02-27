package com.kanchi.periyava.old.Adapters;

/**
 * Created by m84098 on 9/24/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kanchi.periyava.R;
import com.kanchi.periyava.old.ReceiveRequest.ReceiveSatsangList;

import java.util.ArrayList;

public class SatsangListAdapter extends RecyclerView.Adapter<SatsangViewHolder> {

  private Context context;
  private ArrayList<ReceiveSatsangList.Data> items;

  public SatsangListAdapter(Context context, ArrayList<ReceiveSatsangList.Data> items) {
    this.context = context;
    this.items = items;
  }

  // Create new views (invoked by the layout manager)
  @Override
  public SatsangViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.satsanglistitem, parent, false);
    SatsangViewHolder viewHolder = new SatsangViewHolder(context, view);
    return viewHolder;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(SatsangViewHolder viewHolder, int position) {
    ReceiveSatsangList.Data data = items.get(position);
    viewHolder.satsangtitle.setText(data.name);
    viewHolder.satsangcontent.setText(data.description);
    viewHolder.satsanglocation.setText(
        data.city + "," + data.state + "," + data.country);
    viewHolder.Data = data;
  }

  @Override
  public int getItemCount() {
    return items.size();
  }


}
