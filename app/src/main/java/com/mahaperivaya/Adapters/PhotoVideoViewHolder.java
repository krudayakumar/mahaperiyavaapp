package com.mahaperivaya.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.ReceivePhotoVideoList;

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

    switch (type) {
      case ConstValues.CONST_PHOTO:
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Data.link)));
        break;
      case ConstValues.CONST_VIDEO:
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Data.link)));
        break;

    }

  }
}
