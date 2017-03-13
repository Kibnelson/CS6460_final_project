package com.edu.peers.adapter;
/**
 * Created by nelson on 3/16/15.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.User;
import com.edu.peers.others.Constants;
import com.edu.peers.views.RankingUserQuizListView;

import java.util.List;


public class RankingUserListViewAdapter extends BaseAdapter {

  private final RankingUserQuizListView mContext;

  private final List<Quiz> items;

  public RankingUserListViewAdapter(RankingUserQuizListView c,
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
    User item = items.get(position).getUser();
    TextView textView = (TextView) homeListItem
        .findViewById(R.id.listitem_label);
    TextView textViewOne = (TextView) homeListItem
        .findViewById(R.id.listitem_labeltwo);

    int index=position+1;

    textView.setText(index+" "+item.getFirstName() +" "+item.getLastName());

    Log.i(Constants.TAG,"items.get(position)items.get(position)===="+items.get(position).getMarks());
    Log.i(Constants.TAG,"items.get(position)items.get(position)===="+calculateMarks(items.get(position)));

    textViewOne.setText(calculateMarks(items.get(position)));
//    textViewOne.setText(item.getSubject()+" | "+calculateMarks(items.get(position)));

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