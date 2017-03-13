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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.peers.R;
import com.edu.peers.models.Input;
import com.edu.peers.models.User;
import com.edu.peers.views.QuestionListViewContent;

import java.util.List;


public class QuestionResponsesListViewAdapter extends BaseAdapter {

  private final QuestionListViewContent mContext;

  private final List<Input> items;

  public QuestionResponsesListViewAdapter(QuestionListViewContent c,
                                          List<Input> items) {
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

  public View getView(final int position, View convertView, ViewGroup parent) {
    View homeListItem;

    if (convertView == null) {
      homeListItem = new View(mContext.getContext());
      LayoutInflater inflater = (LayoutInflater) mContext
          .getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      homeListItem = inflater.inflate(R.layout.child_row_questions_responses_inputs,
                                      parent, false);
    } else {
      homeListItem = convertView;
    }
    Input item = items.get(position);
    TextView groupPositionChild = (TextView) homeListItem
        .findViewById(R.id.groupPositionChild);
    TextView textView = (TextView) homeListItem
        .findViewById(R.id.childname);
    TextView textViewOne = (TextView) homeListItem
        .findViewById(R.id.date);
    TextView textViewUser = (TextView) homeListItem
        .findViewById(R.id.user);

    ImageView recommend = (ImageView) homeListItem
        .findViewById(R.id.recommend);
    recommend.setVisibility(View.GONE);

    LinearLayout thumbsupLayout = (LinearLayout) homeListItem
        .findViewById(R.id.thumbsupLayout);
//    thumbsupLayout.setVisibility(View.GONE);

    LinearLayout thumbsdownLayout = (LinearLayout) homeListItem
        .findViewById(R.id.thumbsdownLayout);


    if (item.getRecommended()!=null && item.getRecommended())
      recommend.setVisibility(View.VISIBLE);


    TextView thumbsup = (TextView) homeListItem
        .findViewById(R.id.thumbsup);

    thumbsup.setText(""+item.getThumbsUp());


    TextView thumbsdown = (TextView) homeListItem
        .findViewById(R.id.thumbsdown);

    thumbsdown.setText(""+item.getThumbsDown());






    int index=position+1;
    groupPositionChild.setText(""+index);
    textView.setText(item.getQuestionInput());
    textViewOne.setText(item.getDate());

    User user1=item.getUser();


    if (user1 != null){
      if (user1.getRole().equalsIgnoreCase("Instructor"))
        textViewUser.setText(
            "Instructor: " + user1.getFirstName() + " " +user1.getLastName());
      else
        textViewUser.setText(
            "Student: " + user1.getFirstName() + " " + user1.getLastName());

    }




    final ImageView voice_q = (ImageView) homeListItem.findViewById(R.id.voice_q_c);
    voice_q.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String
            questionStringVoice =items.get(position).getQuestionVoice();

        if (questionStringVoice.length() > 0) {
          mContext.openRecordDialog(questionStringVoice);
        } else {
          Toast.makeText(mContext.getContext(), "There is no recorded audio", Toast.LENGTH_LONG)
              .show();
        }

      }
    });

    ImageView write_q = (ImageView) homeListItem.findViewById(R.id.write_q_c);
    write_q.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String
            questionStringWriting =items.get(position).getQuestionWriting();

        if (questionStringWriting.length() > 0) {
          mContext.openWriteDialog(questionStringWriting);
        } else {
          Toast.makeText(mContext.getContext(), "There is no handwritten information",
                         Toast.LENGTH_LONG).show();
        }


      }
    });

    ImageView picture_q = (ImageView) homeListItem.findViewById(R.id.picture_q_c);
    picture_q.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext.getContext(), "There are no photos yet", Toast.LENGTH_LONG).show();
      }
    });


    return homeListItem;
  }


}