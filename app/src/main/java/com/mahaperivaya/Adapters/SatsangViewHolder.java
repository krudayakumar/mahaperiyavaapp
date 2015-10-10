package com.mahaperivaya.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mahaperivaya.Activity.MainActivity;
import com.mahaperivaya.Model.ConstValues;
import com.mahaperivaya.Model.UserProfile;
import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.ReceiveSatsangList;

/**
 * Created by ess on 17/08/14.
 */
public class SatsangViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;

    public TextView satsangtitle, satsangcontent, satsanglocation;
    public Button satsangjoin;
    public ReceiveSatsangList.Data Data;

    public SatsangViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        satsangtitle = (TextView) itemView.findViewById(R.id.satsangtitle);
        satsangcontent = (TextView) itemView.findViewById(R.id.satsangcontent);
        satsanglocation = (TextView) itemView.findViewById(R.id.satsanglocation);
        satsangjoin = (Button) itemView.findViewById(R.id.satsangjoin);
        if(UserProfile.getUserProfile().isLoggedIn == false) {
            satsangjoin.setVisibility(View.GONE);
        }

        satsangjoin.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context,"Joining this satsang group", Toast.LENGTH_SHORT).show();

        switch (v.getId()) {
            case R.id.satsangjoin:
                android.os.Message msg = android.os.Message.obtain();
                msg.what = ConstValues.EDIT_SATSANG;
                msg.obj = (Object) Data;
                ((MainActivity)((Activity)context)).getFlowHandler().sendMessage(msg);
                break;
            default:
                break;
        }

    }
}
