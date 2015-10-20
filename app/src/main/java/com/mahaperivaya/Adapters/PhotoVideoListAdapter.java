package com.mahaperivaya.Adapters;

/**
 * Created by m84098 on 9/24/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.ReceivePhotoVideoList;

import java.util.ArrayList;

public class PhotoVideoListAdapter extends RecyclerView.Adapter<PhotoVideoViewHolder> {

  private Context context;
  private ArrayList<ReceivePhotoVideoList.PhotoVideoDetails.SubLinks> items;
  private String type;

  public PhotoVideoListAdapter(Context context, ArrayList<ReceivePhotoVideoList.PhotoVideoDetails.SubLinks> items, String type) {
    this.context = context;
    this.items = items;
    this.type = type;
  }

  // Create new views (invoked by the layout manager)
  @Override
  public PhotoVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.photovideolistitem, parent, false);
    PhotoVideoViewHolder viewHolder = new PhotoVideoViewHolder(context, view);
    return viewHolder;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(PhotoVideoViewHolder viewHolder, int position) {
    ReceivePhotoVideoList.PhotoVideoDetails.SubLinks data = items.get(position);
    viewHolder.title.setText(data.subtitle);
    viewHolder.Data = data;
    viewHolder.type = type;
  }

  @Override
  public int getItemCount() {
    return items.size();
  }


}
