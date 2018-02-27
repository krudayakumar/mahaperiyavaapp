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

import java.util.ArrayList;

public class AcharayasListAdapter extends RecyclerView.Adapter<AcharayasViewHolder> {

  public class AcharayaData {
    int name;
    int imageid;
    int contentid;
    int acharaya;

    AcharayaData(int acharaya, int name,
                 int imageid
    ) {

      this.imageid = imageid;
      this.name = name;
      this.acharaya = acharaya;
    }
  }

  private Context context;
  private ArrayList<AcharayaData> items;

  public AcharayasListAdapter(Context context) {
    this.context = context;
    items = new ArrayList<AcharayaData>();
    this.items.add(new AcharayaData(68, R.string.lbl_about_acharaya68_name, R.drawable.acharaya68));
    this.items.add(new AcharayaData(69, R.string.lbl_about_acharaya69_name, R.drawable.acharaya69));
    this.items.add(new AcharayaData(70, R.string.lbl_about_acharaya70_name, R.drawable.acharaya70));
  }

  // Create new views (invoked by the layout manager)
  @Override
  public AcharayasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.about_acharayas_listitem, parent, false);
    AcharayasViewHolder viewHolder = new AcharayasViewHolder(context, view);
    return viewHolder;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(AcharayasViewHolder viewHolder, int position) {
    AcharayaData data = items.get(position);
    viewHolder.name.setText(context.getResources().getString(data.name));
    viewHolder.acharayaimage.setImageDrawable(context.getResources().getDrawable(data.imageid));
    viewHolder.acharaya = data.acharaya;

  }

  @Override
  public int getItemCount() {
    return items.size();
  }


}
