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
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.User;
import com.edu.peers.others.Constants;
import com.edu.peers.views.StudentsPairListView;

import java.util.List;


public class PairingListViewAdapter extends BaseAdapter {

  private final StudentsPairListView mContext;

  private final List<User> items;

  public PairingListViewAdapter(StudentsPairListView c,
                                List<User> items) {
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
      homeListItem = inflater.inflate(R.layout.pair_list_items,
                                      parent, false);
    } else {
      homeListItem = convertView;
    }
    User item = items.get(position);


    TextView groupPositionChild = (TextView) homeListItem
        .findViewById(R.id.groupPositionChild);
    TextView studentsName = (TextView) homeListItem
        .findViewById(R.id.childname);

    TextView pairedWith = (TextView) homeListItem
        .findViewById(R.id.pairedWith);

    ImageView studentsPair = (ImageView) homeListItem
        .findViewById(R.id.studentsPair);
    studentsPair.setVisibility(View.GONE);

    studentsPair.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        mContext.studentsPair(items.get(position));

      }
    });

    ImageView studentsUnpair = (ImageView) homeListItem
        .findViewById(R.id.studentsUnpair);
    studentsUnpair.setVisibility(View.GONE);
    studentsUnpair.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          mContext.studentsUnpair(items.get(position));
      }
    });

    Log.i(Constants.TAG,"item.getPaired()==="+item.getPaired());
    Log.i(Constants.TAG,"item.getPairedWith()==="+item.getPairedWith());

    if ( item.getPaired()!=null && item.getPaired() && item.getPairedWith()!=null && item.getPairedWith().length()>0 ){
      studentsUnpair.setVisibility(View.VISIBLE);
      if (item.getPairedWith()!=null){
        User user=getUser(item.getPairedWith());
      pairedWith.setText( user.getFirstName()+" "+user.getLastName());

    }
    } else {
      studentsPair.setVisibility(View.VISIBLE);
      pairedWith.setText("------");
    }

    int index=position+1;
    groupPositionChild.setText(""+index+": ");
    studentsName.setText(item.getFirstName()+" "+item.getLastName());

    return homeListItem;
  }
  public User getUser(String usernmae) {

    int size=items.size();
    User removedUser=null;

    for (int y = 0; y < size; y++) {
      User user1 = items.get(y);
      if (user1.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE)) {

        if (user1.getUsername().equalsIgnoreCase(usernmae)) {
          removedUser = user1;
        }

      }
    }
    return removedUser;
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