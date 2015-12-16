package com.kanchi.periyava.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kanchi.periyava.Activity.MainActivity;
import com.kanchi.periyava.Component.CircularImageView;
import com.kanchi.periyava.Model.ConstValues;
import com.kanchi.periyava.Model.UserProfile;
import com.kanchi.periyava.R;
import com.kanchi.periyava.ReceiveRequest.ReceiveSatsangList;
import com.kanchi.periyava.SendRequest.SendJoinSatsang;

/**
 * Created by ess on 17/08/14.
 */
public class AcharayasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  public String TAG = "AcharayasViewHolder";
  private Context context;

  public TextView name;
  public ImageView acharayaimage;
  public int contentid;
  public int acharaya;

  public AcharayasViewHolder(Context context, View itemView) {
    super(itemView);
    this.context = context;
    name = (TextView) itemView.findViewById(R.id.acharaya_name);
    acharayaimage = (ImageView) itemView.findViewById(R.id.acharaya_image);
    acharayaimage.setOnClickListener(this);
    name.setOnClickListener(this);
    itemView.setOnClickListener(this);
  }


  @Override
  public void onClick(View v) {
    android.os.Message msg = android.os.Message.obtain();
    switch (acharaya) {
      default:
      case 68:
        msg.what = ConstValues.ABOUT_ACHARAYAS_68;
        break;
      case 69:
        msg.what = ConstValues.ABOUT_ACHARAYAS_69;
        break;
      case 70:
        msg.what = ConstValues.ABOUT_ACHARAYAS_70;
        break;
    }
    MainActivity.getFlowHandler().sendMessage(msg);
  }
}
