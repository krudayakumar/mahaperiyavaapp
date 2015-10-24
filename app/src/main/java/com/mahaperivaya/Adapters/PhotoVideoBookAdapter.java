package com.mahaperivaya.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahaperivaya.R;
import com.mahaperivaya.ReceiveRequest.ReceivePhotoVideo;

/**
 * Created by m84098 on 10/24/15.
 */
public class PhotoVideoBookAdapter extends BaseExpandableListAdapter {
  Context context;
  ReceivePhotoVideo.Category category;
  public static String TAG = "PhotoVideoBookAdapter";

  public PhotoVideoBookAdapter(Context context, ReceivePhotoVideo.Category category) {
    this.category = category;
    this.context = context;
  }

  @Override
  public int getGroupCount() {
    return category.sublinks.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    int iCount = 0;

    if (category.sublinks.get(groupPosition).subcategories != null) {
      iCount = category.sublinks.get(groupPosition).subcategories.size();
    }
    Log.d(TAG, "Count :" + iCount + category.sublinks.get(groupPosition).subcategories);
    return iCount;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return category.sublinks.get(groupPosition);
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {

    return category.sublinks.get(groupPosition).subcategories.get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    ImageView indicator;

    ReceivePhotoVideo.Category.Categories categories = (ReceivePhotoVideo.Category.Categories) getGroup(groupPosition);
    if (convertView == null) {
      LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
      convertView = layoutInflater.inflate(R.layout.list_group, parent, false);
      //convertView = layoutInflater.inflate(R.layout.list_group, null);
    }
    /*indicator  = (ImageView) convertView.findViewById(R.id.indicator);
    if ( getChildrenCount( groupPosition ) == 0 ) {
      indicator.setVisibility( View.INVISIBLE );
    } else {
      indicator.setVisibility( View.VISIBLE );
      indicator.setImageResource( isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float );
    }*/


    TextView txtTitle = (TextView) convertView.findViewById(R.id.listTitle);
    txtTitle.setText(categories.title);
    convertView.setTag(categories);
    return convertView;
  }


  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

    ReceivePhotoVideo.Category.Categories.SubCategories subCategories = (ReceivePhotoVideo.Category.Categories.SubCategories) getChild(groupPosition, childPosition);
    if (convertView == null) {
      LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
      convertView = layoutInflater.inflate(R.layout.list_item, parent, false);
      //convertView = layoutInflater.inflate(R.layout.list_item, null);
    }
    TextView txtTitle = (TextView) convertView.findViewById(R.id.expandedListItem);
    txtTitle.setText(subCategories.title);
    convertView.setTag(subCategories);
    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }
}
