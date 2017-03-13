package com.edu.peers.adapter;
/**
 * Created by nelson on 3/16/15.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.models.Notifications;
import com.edu.peers.views.NotificationsListView;

import java.util.List;


public class NotificationListViewAdapter extends BaseAdapter {

  private final NotificationsListView mContext;

  private final List<Notifications> items;

  public NotificationListViewAdapter(NotificationsListView c,
                                     List<Notifications> items) {
    mContext = c;
    this.items = items;
  }

  public int getCount() {
    return items.size();
  }

  public Object getItem(int position) {
    return items.get(position);
  }

  public long getItemId(int position) {
    return 0;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    View homeListItem;

    if (convertView == null) {
      homeListItem = new View(mContext.getContext());
      LayoutInflater inflater = (LayoutInflater) mContext
          .getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      homeListItem = inflater.inflate(R.layout.schools_list_items,
                                      parent, false);
    } else {
      homeListItem = convertView;
    }
    Notifications item = items.get(position);
    TextView textView = (TextView) homeListItem
        .findViewById(R.id.listitem_label);
    TextView textViewOne = (TextView) homeListItem
        .findViewById(R.id.listitem_labeltwo);


    textView.setText(item.getMessage());
    textViewOne.setText("");

    if (item.getRead()!=null) {
      if (item.getRead()) {
        textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
      } else
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

    }
    return homeListItem;
  }


}