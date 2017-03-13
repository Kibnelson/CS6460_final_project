package com.edu.peers.adapter;
/**
 * Created by nelson on 3/16/15.
 */

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.peers.R;
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.views.QuizAttemptView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QuizAttemptExpandableListViewAdapter extends
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
  public Map<Integer, TextView> integerTextViewMap= new HashMap<>();
  public TextView selectedAnswers;
  private List<? extends List<? extends Map<String, Input>>> mChildData;
  private int mChildLayout;
  private int mLastChildLayout;
  private String[] mChildFrom;
  private int[] mChildTo;
  private QuizAttemptView mContext;
  private OnDataChangeListener mOnDataChangeListener;
  private TextView groupPositionTextView;

  public QuizAttemptExpandableListViewAdapter(QuizAttemptView context,
                                              List<? extends Map<String, Questions>> groupData,
                                              int groupLayout,
                                              String[] groupFrom, int[] groupTo,
                                              List<? extends List<? extends Map<String, Input>>> childData,
                                              int childLayout, String[] childFrom, int[] childTo) {

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

    int position=groupPosition+1;
    groupPositionTextView.setText(""+position);

    integerTextViewMap.put(groupPosition,selectedAnswers);

    mContext.updateTextViewMap(groupPosition,selectedAnswers);

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

    TextView    groupPositionChild = (TextView) v.findViewById(R.id.groupPositionChild);
    groupPositionChild.setText(""+mChildData.get(groupPosition).get(childPosition).get(NAME).getPosition());
    selectedAnswers.setText(mContext.getCheckedValues( groupPosition,childPosition));


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

      explist_indicator.setChecked(mContext.isCheckCheckedValues(groupPosition,childPosition));

    explist_indicator.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

         mContext.checkBoxSelected(groupPosition,childPosition,explist_indicator.isChecked(),mChildData.get(groupPosition).get(childPosition).get(NAME));


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

    integerTextViewMap.get(groupPosition).setText(">>"+groupPosition);



  }
}
