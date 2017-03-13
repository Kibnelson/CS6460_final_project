package com.edu.peers.adapter;
/**
 * Created by nelson on 3/16/15.
 */

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.peers.R;
import com.edu.peers.models.Input;
import com.edu.peers.models.User;
import com.edu.peers.views.DocumentView;

import java.util.List;
import java.util.Map;


public class CommentsExpandableListViewAdapter extends
                                          SimpleExpandableListAdapterComments {

  private static final int[] EMPTY_STATE_SET = {};
  private static final int[] GROUP_EXPANDED_STATE_SET =
      {android.R.attr.state_expanded};
  private static final int[][] GROUP_STATE_SETS = {
      EMPTY_STATE_SET, // 0
      GROUP_EXPANDED_STATE_SET // 1
  };
  private static final String NAME = "NAME";

  private List<? extends Map<String, ?>> mGroupData;
  private int mExpandedGroupLayout;
  private int mCollapsedGroupLayout;
  private String[] mGroupFrom;
  private int[] mGroupTo;
  private List<? extends List<? extends Map<String, Input>>> mChildData;
  private int mChildLayout;
  private int mLastChildLayout;
  private String[] mChildFrom;
  private int[] mChildTo;
  private DocumentView mContext;
  private OnDataChangeListener mOnDataChangeListener;
  private TextView groupPositionTextView;
  private TextView subject;
  private TextView user;

  public CommentsExpandableListViewAdapter(DocumentView context,
                                              List<? extends Map<String, ?>> groupData,
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

  public View getGroupView(int groupPosition,
                           boolean isExpanded,
                           View convertView,
                           ViewGroup parent) {
    View v = super.getGroupView(groupPosition, isExpanded, convertView, parent);
    View ind = v.findViewById(R.id.explist_indicator);
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
    return v;
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
    TextView    user = (TextView) v.findViewById(R.id.user);
    TextView    date = (TextView) v.findViewById(R.id.date);

    Input input=mChildData.get(groupPosition).get(childPosition).get(NAME);


    if (input!=null) {

      date.setText(input.getDate());

      User user1 = input.getUser();

      if (user1 != null){
        if (user1.getRole().equalsIgnoreCase("Instructor"))
          user.setText(
              "Instructor: " + user1.getFirstName() + " " +user1.getLastName());
        else
          user.setText(
              "Student: " + user1.getFirstName() + " " + user1.getLastName());

      }

    }

    int index=childPosition+1;

    groupPositionChild.setText(""+index);


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

    bindViewChild(v, mChildData.get(groupPosition).get(childPosition), mChildFrom, mChildTo, groupPosition, childPosition);
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
  public void onDataChanged(int size) {

  }
}
