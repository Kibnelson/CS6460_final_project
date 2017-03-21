package com.edu.peers.adapter;
/**
 * Created by nelson on 3/16/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.dialogs.StudentsPairDialogView;
import com.edu.peers.models.User;

import java.util.List;


public class StudentsPairListViewAdapter extends BaseAdapter {

  private final StudentsPairDialogView mContext;

  private final List<User> items;

  private User user;

  public StudentsPairListViewAdapter(StudentsPairDialogView c,
                                     List<User> items, User user1) {
    mContext = c;
    this.items = items;
    this.user = user1;
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
      homeListItem = inflater.inflate(R.layout.students_recommend_list_items,
                                      parent, false);
    } else {
      homeListItem = convertView;
    }
    User selectedUser = items.get(position);

    TextView average = (TextView) homeListItem.findViewById(R.id.average);
    TextView studentName = (TextView) homeListItem.findViewById(R.id.studentName);
    TextView difference = (TextView) homeListItem.findViewById(R.id.difference);
    ImageView recommended = (ImageView) homeListItem.findViewById(R.id.recommended);
    recommended.setVisibility(View.GONE);

    if (selectedUser != null) {

      studentName.setText(selectedUser.getFirstName() + " " + selectedUser.getLastName());
      difference.setText("" + selectedUser.getDifference());
      average.setText("" + selectedUser.getAverage());

      if (selectedUser.getRecommend() != null && selectedUser.getRecommend()) {
        recommended.setVisibility(View.VISIBLE);
      } else recommended.setVisibility(View.GONE);

    }

    difference.setVisibility(View.GONE);
    return homeListItem;
  }


}