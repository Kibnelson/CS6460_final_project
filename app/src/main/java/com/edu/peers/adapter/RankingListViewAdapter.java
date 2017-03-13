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
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.models.Quiz;
import com.edu.peers.views.RankingQuizListView;

import java.util.List;


public class RankingListViewAdapter extends BaseAdapter {

  private final RankingQuizListView mContext;

  private final List<Quiz> items;

  public RankingListViewAdapter(RankingQuizListView c,
                                List<Quiz> items) {
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
    Quiz item = items.get(position);
    TextView textView = (TextView) homeListItem
        .findViewById(R.id.listitem_label);
    TextView textViewOne = (TextView) homeListItem
        .findViewById(R.id.listitem_labeltwo);
    textView.setText(item.getName());
//    textViewOne.setText(item.getSubject()+" | "+calculateMarks(item));
    textViewOne.setText(item.getSubject());

    return homeListItem;
  }

  public String calculateMarks(Quiz quiz) {
    String totalMarks;
    int correctExpectedMarks=0;
    int selectedExpectedMarks=0;
    List<Questions> questionsList = quiz.getQuestions();
    for (Questions questions:questionsList){
      List<Input> choices =questions.getChoices();
      for (int y=0;y<choices.size();y++){
        Input input =choices.get(y);
        if (input.getAnswer()){
          correctExpectedMarks++;

        }

        if (input.getAnswer()&& input.getSelected()){
          selectedExpectedMarks++;

        }

      }
    }

    totalMarks=""+selectedExpectedMarks+"/"+correctExpectedMarks;

    return totalMarks;
  }
}