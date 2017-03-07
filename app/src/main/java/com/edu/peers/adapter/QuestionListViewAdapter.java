package com.edu.peers.adapter;
/**
 * Created by nelson on 3/16/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.models.Questions;
import com.edu.peers.views.QuestionsListView;

import java.util.List;


public class QuestionListViewAdapter extends BaseAdapter {

  private final QuestionsListView mContext;

  private final List<Questions> items;

  public QuestionListViewAdapter(QuestionsListView c,
                                 List<Questions> items) {
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
    Questions item = items.get(position);
    TextView textView = (TextView) homeListItem
        .findViewById(R.id.listitem_label);
    TextView textViewOne = (TextView) homeListItem
        .findViewById(R.id.listitem_labeltwo);
    textView.setText(item.getQuestionInput());
    textViewOne.setText(item.getDate());

    return homeListItem;
  }


}