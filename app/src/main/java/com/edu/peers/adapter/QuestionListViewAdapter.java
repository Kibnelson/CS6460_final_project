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
import com.edu.peers.models.User;
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
      homeListItem = inflater.inflate(R.layout.questions_list_items,
                                      parent, false);
    } else {
      homeListItem = convertView;
    }
    Questions questions = items.get(position);
    TextView date = (TextView) homeListItem
        .findViewById(R.id.date);
    TextView user = (TextView) homeListItem
        .findViewById(R.id.user);
    TextView question = (TextView) homeListItem
        .findViewById(R.id.question);

    User user1=questions.getUser();


    if (user1 != null){
      if (user1.getRole().equalsIgnoreCase("Instructor"))
        user.setText(
            "Instructor: " + user1.getFirstName() + " " +user1.getLastName());
      else
        user.setText(
            "Student: " + user1.getFirstName() + " " + user1.getLastName());

    }

    question.setText(questions.getQuestionInput());
    date.setText(questions.getDate());

    return homeListItem;
  }


}