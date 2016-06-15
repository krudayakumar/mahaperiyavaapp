package com.kanchi.periyava.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.R;
import com.kanchi.periyava.ReceiveRequest.ReceivePhotoVideoList;

/**
 * Created by ess on 17/08/14.
 */
public class PhotoVideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  private Context context;

  public TextView title;
  public String type;

  public ReceivePhotoVideoList.PhotoVideoDetails.SubLinks Data;

  public PhotoVideoViewHolder(Context context, View itemView) {
    super(itemView);
    this.context = context;
    title = (TextView) itemView.findViewById(R.id.subtitle);
    itemView.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {

    if(type == ConstValues.CONST_PHOTO) {
      context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Data.link)));
    }else if (type == ConstValues.CONST_VIDEO) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Data.link)));
    }

  }
}
