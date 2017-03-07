package com.edu.peers.adapter;
/**
 * Created by nelson on 3/16/15.
 */

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.models.Content;
import com.edu.peers.models.ContentFile;
import com.edu.peers.models.User;
import com.edu.peers.views.LearningContentListView;

import java.util.List;
import java.util.Map;


public class ContentFileExpandableListViewAdapter extends
                                           SimpleExpandableListAdapterContentFile
    implements View.OnClickListener {

  private static final int[] EMPTY_STATE_SET = {};
  private static final int[] GROUP_EXPANDED_STATE_SET =
      {android.R.attr.state_expanded};
  private static final int[][] GROUP_STATE_SETS = {
      EMPTY_STATE_SET, // 0
      GROUP_EXPANDED_STATE_SET // 1
  };
  private static final String NAME = "NAME";

  private List<? extends Map<String, Content>> mGroupData;
  private int mExpandedGroupLayout;
  private int mCollapsedGroupLayout;
  private String[] mGroupFrom;
  private int[] mGroupTo;
  private List<? extends List<? extends Map<String, ContentFile>>> mChildData;
  private int mChildLayout;
  private int mLastChildLayout;
  private String[] mChildFrom;
  private int[] mChildTo;
  private LearningContentListView mContext;
  private OnDataChangeListener mOnDataChangeListener;
  private TextView groupPositionTextView;
  private TextView subject;
  private TextView user;

  public ContentFileExpandableListViewAdapter(LearningContentListView context,
                                              List<? extends Map<String, Content>> groupData,
                                              int groupLayout,
                                              String[] groupFrom, int[] groupTo,
                                              List<? extends List<? extends Map<String, ContentFile>>> childData,
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




    subject = (TextView) v.findViewById(R.id.subject);
    groupPositionTextView = (TextView) v.findViewById(R.id.groupPosition);
    user = (TextView) v.findViewById(R.id.user);

    int position=groupPosition+1;
    groupPositionTextView.setText(""+position);


    subject.setText(""+mGroupData.get(groupPosition).get(NAME).getSubject());

    User user1=mGroupData.get(groupPosition).get(NAME).getUser();
    if (user1!=null)
      user.setText(""+user1.getLastName()+" "+user1.getFirstName());

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

  private void bindViewGroup(View view, Map<String, Content> data, String[] from, int[] to) {
    int len = to.length;

    for (int i = 0; i < len; i++) {
      TextView v = (TextView) view.findViewById(to[i]);

      if (v != null) {
        v.setText((String) data.get(from[i]).getTitle());
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


    TextView    user = (TextView) v.findViewById(R.id.user);
    TextView    date = (TextView) v.findViewById(R.id.date);
    TextView    fileName = (TextView) v.findViewById(R.id.fileName);

    TextView    groupPositionChild = (TextView) v.findViewById(R.id.groupPositionChild);
    int index=childPosition+1;
    groupPositionChild.setText(""+index);

    date.setText(mChildData.get(groupPosition).get(childPosition).get(NAME).getDate());

    User user1=mChildData.get(groupPosition).get(childPosition).get(NAME).getUser();
    if (user1!=null)
      user.setText(""+user1.getLastName()+" "+user1.getFirstName());
    date.setText("");

    bindViewChild(v, mChildData.get(groupPosition).get(childPosition), mChildFrom, mChildTo,
                  groupPosition, childPosition);
    return v;
  }

  private void bindViewChild(View view, Map<String, ContentFile> data, String[] from, int[] to,
                             int groupPosition, int childPosition) {

    int len = to.length;

    for (int i = 0; i < len; i++) {
      TextView v = (TextView)
          view.findViewById(to[i]);
      if (v != null) {
        v.setText(data.get(from[i]).getFileName());
      }
    }
  }

  @Override
  public void onClick(View v) {

  }

  @Override
  public void onDataChanged(int groupPosition) {

  }
}
