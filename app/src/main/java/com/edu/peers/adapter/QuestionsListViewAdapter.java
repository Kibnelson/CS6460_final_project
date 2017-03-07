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
import com.edu.peers.dialogs.QuizCreationDialogView;
import com.edu.peers.models.Questions;

import java.util.List;


public class QuestionsListViewAdapter extends BaseAdapter {

  private final QuizCreationDialogView mContext;

  private final List<Questions> items;

  public QuestionsListViewAdapter(QuizCreationDialogView c,
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
    int index=position+1;
    textView.setText("Question "+index+" : "+item.getQuestionInput());
    textViewOne.setText(item.getDate());

    return homeListItem;
  }


}