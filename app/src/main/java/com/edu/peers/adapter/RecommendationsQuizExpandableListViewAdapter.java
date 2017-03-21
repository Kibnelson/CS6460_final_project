package com.edu.peers.adapter;
/**
 * Created by nelson on 3/16/15.
 */

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.peers.R;
import com.edu.peers.dialogs.RecommendationsDialogView;
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.User;
import com.edu.peers.others.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecommendationsQuizExpandableListViewAdapter extends
                                                    SimpleExpandableListAdapterQuiz
    implements View.OnClickListener {

  private static final int[] EMPTY_STATE_SET = {};
  private static final int[] GROUP_EXPANDED_STATE_SET =
      {android.R.attr.state_expanded};
  private static final int[][] GROUP_STATE_SETS = {
      EMPTY_STATE_SET, // 0
      GROUP_EXPANDED_STATE_SET // 1
  };
  private static final String NAME = "NAME";

  private List<? extends Map<String, Questions>> mGroupData;
  private int mExpandedGroupLayout;
  private int mCollapsedGroupLayout;
  private String[] mGroupFrom;
  private int[] mGroupTo;
  public Map<Integer, TextView> integerTextViewMap = new HashMap<>();
  public TextView selectedAnswers;
  private List<? extends List<? extends Map<String, Input>>> mChildData;
  private int mChildLayout;
  private int mLastChildLayout;
  private String[] mChildFrom;
  private int[] mChildTo;
  private RecommendationsDialogView mContext;
  private OnDataChangeListener mOnDataChangeListener;
  private TextView groupPositionTextView;
  private LinearLayout selectedAnswersLayout;
  private LinearLayout recommendLayout;
  private LinearLayout voteLayout;
  private User loggedInUser;
  private TextView thumbsupValue, thumbsdownValue;
  private LinearLayout recommendedLayout;
  private List<Quiz> quizList;

  public RecommendationsQuizExpandableListViewAdapter(RecommendationsDialogView context,
                                                      List<? extends Map<String, Questions>> groupData,
                                                      int groupLayout,
                                                      String[] groupFrom, int[] groupTo,
                                                      List<? extends List<? extends Map<String, Input>>> childData,
                                                      int childLayout, String[] childFrom, int[] childTo,
                                                      User loggedInUser, List<Quiz> quizList) {

    super(context.getContext(), groupData, groupLayout, groupFrom, groupTo, childData,
          childLayout, childFrom, childTo);
    mGroupData = groupData;
    mGroupFrom = groupFrom;
    mGroupTo = groupTo;

    mChildData = childData;
    mChildLayout = childLayout;
    mChildFrom = childFrom;
    mChildTo = childTo;
    this.mContext = context;
    this.loggedInUser = loggedInUser;
    this.quizList=quizList;
  }

  @Override
  public View getGroupView(final int groupPosition,
                           boolean isExpanded,
                           View convertView,
                           ViewGroup parent) {
    View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);
    View ind = v.findViewById(R.id.explist_indicator);

    selectedAnswers = (TextView) v.findViewById(R.id.selectedAnswers);
    groupPositionTextView = (TextView) v.findViewById(R.id.groupPosition);
    selectedAnswersLayout = (LinearLayout) v.findViewById(R.id.selectedAnswersLayout);
    int position = groupPosition + 1;
//    groupPositionTextView.setText("By: "+mGroupData.get(groupPosition).get(NAME).getUser().getFirstName()+" "+mGroupData.get(groupPosition).get(NAME).getUser().getLastName() +":");
//    groupPositionTextView.setText("By: "+userQuiz.getUser().getFirstName()+" "+userQuiz.getUser().getLastName() +":");
    groupPositionTextView.setText("By: "+quizList.get(groupPosition).getUser().getFirstName()+" "+quizList.get(groupPosition).getUser().getLastName() +":");



    integerTextViewMap.put(groupPosition, selectedAnswers);

//    mContext.updateTextViewMap(groupPosition, selectedAnswers);

    final ImageView voice_q = (ImageView) v.findViewById(R.id.voice_q);
    voice_q.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String questionStringVoice = mGroupData.get(groupPosition).get(NAME).getQuestionVoice();

        if (questionStringVoice.length() > 0) {
          mContext.openRecordDialog(questionStringVoice);
        } else {
          Toast.makeText(mContext.getContext(), "There is no recorded audio", Toast.LENGTH_LONG)
              .show();
        }

      }
    });

    ImageView write_q = (ImageView) v.findViewById(R.id.write_q);
    write_q.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String questionStringWriting = mGroupData.get(groupPosition).get(NAME).getQuestionWriting();

        if (questionStringWriting.length() > 0) {
          mContext.openWriteDialog(questionStringWriting);
        } else {
          Toast.makeText(mContext.getContext(), "There is no handwritten information",
                         Toast.LENGTH_LONG).show();
        }


      }
    });

    ImageView picture_q = (ImageView) v.findViewById(R.id.picture_q);
    picture_q.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext.getContext(), "There are no photos yet", Toast.LENGTH_LONG).show();
      }
    });

    ImageView explist_indicator = (ImageView) v.findViewById(R.id.explist_indicator);
    explist_indicator.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mContext.onGroupExpanded(groupPosition);
      }
    });

    if (ind != null) {
      ImageView indicator = (ImageView) ind;
      if (getChildrenCount(groupPosition) == 0) {
        indicator.setVisibility(View.INVISIBLE);
      } else {
        indicator.setVisibility(View.VISIBLE);
        int stateSetIndex = (isExpanded ? 1 : 0);
        Drawable drawable = indicator.getDrawable();
        drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
      }
    }

    selectedAnswersLayout.setVisibility(View.GONE);

    bindViewGroup(v, mGroupData.get(groupPosition), mGroupFrom, mGroupTo);
    return v;
  }

  private void bindViewGroup(View view, Map<String, Questions> data, String[] from, int[] to) {
    int len = to.length;

    for (int i = 0; i < len; i++) {
      TextView v = (TextView) view.findViewById(to[i]);

      if (v != null) {
        v.setText((String) data.get(from[i]).getQuestionInput());
      }
    }
  }

  @Override
  public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                           View convertView, ViewGroup parent) {

    View v;
    if (convertView == null) {
      v = newChildView(isLastChild, parent);
    } else {
      v = convertView;
    }

    Input input = mChildData.get(groupPosition).get(childPosition).get(NAME);

    recommendLayout = (LinearLayout) v.findViewById(R.id.recommendLayout);
    recommendLayout.setVisibility(View.GONE);

    recommendedLayout= (LinearLayout) v.findViewById(R.id.recommendedLayout);
    recommendedLayout.setVisibility(View.GONE);


    voteLayout = (LinearLayout) v.findViewById(R.id.voteLayout);
    thumbsupValue = (TextView) v.findViewById(R.id.thumbsupValue);
    thumbsdownValue = (TextView) v.findViewById(R.id.thumbsdownValue);

    thumbsdownValue.setText(""+input.getThumbsDown());
    thumbsupValue.setText(""+input.getThumbsUp());


    voteLayout.setVisibility(View.GONE);

    if (loggedInUser.getRole().equalsIgnoreCase(Constants.INSTRUCTOR_ROLE)) {
      recommendLayout.setVisibility(View.VISIBLE);
    } else if (loggedInUser.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE)) {

        if (input.getSelected() && input.getAnswer()) {
          voteLayout.setVisibility(View.VISIBLE);
        }

    }


    ImageView recommend = (ImageView) v.findViewById(R.id.recommend);
    recommend.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


//          mContext.recommendClicked(groupPosition, childPosition, true,
//                                    mChildData.get(groupPosition).get(childPosition).get(NAME));
          Toast.makeText(mContext.getContext(), "Recommend clicked", Toast.LENGTH_LONG)
              .show();


      }
    });

    ImageView thumbsup = (ImageView) v.findViewById(R.id.thumbsup);
    thumbsup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

//        if (loggedInUser.getUsername().equalsIgnoreCase(userQuiz.getUsername())) {
//
//          Toast.makeText(mContext.getContext(), "This feature is not allowed because its your own test results", Toast.LENGTH_LONG)
//              .show();
//
//        } else {
//          mContext.thumbsUpClicked(groupPosition, childPosition, true,
//                                   mChildData.get(groupPosition).get(childPosition).get(NAME));

          Toast.makeText(mContext.getContext(), "Thumbs up clicked", Toast.LENGTH_LONG)
              .show();
//        }

      }
    });
    recommend.setVisibility(View.GONE);

    ImageView thumbsdown = (ImageView) v.findViewById(R.id.thumbsdown);
    thumbsdown.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

//        if (loggedInUser.getUsername().equalsIgnoreCase(userQuiz.getUsername())) {
//
//          Toast.makeText(mContext.getContext(), "This feature is not allowed because its your own test results", Toast.LENGTH_LONG)
//              .show();
//
//        } else {
//          mContext.thumbsDownClicked(groupPosition, childPosition, true,
//                                     mChildData.get(groupPosition).get(childPosition).get(NAME));
//
//          Toast.makeText(mContext.getContext(), "Thumbs down clicked", Toast.LENGTH_LONG)
//              .show();
//
//        }
      }
    });

    LinearLayout gradesLayout = (LinearLayout) v.findViewById(R.id.gradesLayout);

    LinearLayout recommendOptionsLayout = (LinearLayout) v.findViewById(R.id.recommendOptionsLayout);
    recommendOptionsLayout.setVisibility(View.GONE);




    gradesLayout.setVisibility(View.VISIBLE);
    ImageView wrong = (ImageView) v.findViewById(R.id.wrong);
    wrong.setVisibility(View.GONE);
    ImageView tick = (ImageView) v.findViewById(R.id.tick);
    tick.setVisibility(View.GONE);
    ImageView recommendOptions = (ImageView) v.findViewById(R.id.recommendOptions);
//    recommendOptions.setVisibility(View.GONE);
    recommendOptions.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        // Get the same quiz for another person
        //

//        mContext.getRecommendations(groupPosition,childPosition,mChildData.get(groupPosition).get(childPosition).get(NAME), mGroupData.get(groupPosition).get(NAME));

          Toast.makeText(mContext.getContext(), "Recommender clicked", Toast.LENGTH_LONG)
              .show();
      }
    });

    if (input.getAnswer()) {
      tick.setVisibility(View.VISIBLE);
    }
    if (input.getSelected() && input.getAnswer()) {
      tick.setVisibility(View.VISIBLE);
      recommend.setVisibility(View.VISIBLE);
    }

    if (input.getSelected() && !input.getAnswer()) {
      wrong.setVisibility(View.VISIBLE);
      // Show recomender ICOn
//      recommendOptionsLayout.setVisibility(View.VISIBLE);
    }


    if (input.getRecommended()) {
      if (loggedInUser.getRole().equalsIgnoreCase(Constants.INSTRUCTOR_ROLE)) {
          recommend.setBackgroundColor(Color.parseColor("#007670"));
      } else if (loggedInUser.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE)) {
          recommendedLayout.setVisibility(View.VISIBLE);

      }


    }

    TextView groupPositionChild = (TextView) v.findViewById(R.id.groupPositionChild);
    groupPositionChild
        .setText("# " + mChildData.get(groupPosition).get(childPosition).get(NAME).getPosition());
//    selectedAnswers.setText(mContext.getCheckedValues(groupPosition, childPosition));

    final ImageView voice_q = (ImageView) v.findViewById(R.id.voice_q_c);
    voice_q.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String
            questionStringVoice =
            mChildData.get(groupPosition).get(childPosition).get(NAME).getQuestionVoice();

        if (questionStringVoice.length() > 0) {
          mContext.openRecordDialog(questionStringVoice);
        } else {
          Toast.makeText(mContext.getContext(), "There is no recorded audio", Toast.LENGTH_LONG)
              .show();
        }

      }
    });

    ImageView write_q = (ImageView) v.findViewById(R.id.write_q_c);
    write_q.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        String
            questionStringWriting =
            mChildData.get(groupPosition).get(childPosition).get(NAME).getQuestionWriting();

        if (questionStringWriting.length() > 0) {
          mContext.openWriteDialog(questionStringWriting);
        } else {
          Toast.makeText(mContext.getContext(), "There is no handwritten information",
                         Toast.LENGTH_LONG).show();
        }


      }
    });

    ImageView picture_q = (ImageView) v.findViewById(R.id.picture_q_c);
    picture_q.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext.getContext(), "There are no photos yet", Toast.LENGTH_LONG).show();
      }
    });

    final CheckBox explist_indicator = (CheckBox) v.findViewById(R.id.childCheckbox);

    explist_indicator.setVisibility(View.GONE);

    explist_indicator.setChecked(mContext.isCheckCheckedValues(groupPosition, childPosition,quizList.get(groupPosition)));

    explist_indicator.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

//         mContext.checkBoxSelected(groupPosition,childPosition,explist_indicator.isChecked(),mChildData.get(groupPosition).get(childPosition).get(NAME));

      }
    });

    bindViewChild(v, mChildData.get(groupPosition).get(childPosition), mChildFrom, mChildTo,
                  groupPosition, childPosition);
    return v;
  }

  private void bindViewChild(View view, Map<String, Input> data, String[] from, int[] to,
                             int groupPosition, int childPosition) {

    int len = to.length;

    for (int i = 0; i < len; i++) {
      TextView v = (TextView)
          view.findViewById(to[i]);
      if (v != null) {
        v.setText(data.get(from[i]).getQuestionInput());
      }
    }
  }

  @Override
  public void onClick(View v) {

  }

  @Override
  public void onDataChanged(int groupPosition) {

    integerTextViewMap.get(groupPosition).setText(">>" + groupPosition);


  }

  public User getLoggedInUser() {
    return loggedInUser;
  }

  public void setLoggedInUser(User loggedInUser) {
    this.loggedInUser = loggedInUser;
  }
}
