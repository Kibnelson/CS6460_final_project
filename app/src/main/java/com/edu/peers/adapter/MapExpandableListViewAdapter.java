package com.edu.peers.adapter;
/**
 * Created by nelson on 3/16/15.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.models.Input;
import com.edu.peers.views.DocumentView;

import java.util.List;
import java.util.Map;


public class MapExpandableListViewAdapter extends
                                          SimpleExpandableListAdapter {

  private static final int[] EMPTY_STATE_SET = {};
  private static final int[] GROUP_EXPANDED_STATE_SET =
      {android.R.attr.state_expanded};
  private static final int[][] GROUP_STATE_SETS = {
      EMPTY_STATE_SET, // 0
      GROUP_EXPANDED_STATE_SET // 1
  };
  private DocumentView mContext;
  private List<Input> schoolList;

  public MapExpandableListViewAdapter(DocumentView context,
                                      List<? extends Map<String, ?>> groupData, int groupLayout,
                                      String[] groupFrom, int[] groupTo,
                                      List<? extends List<? extends Map<String, ?>>> childData,
                                      int childLayout, String[] childFrom, int[] childTo,
                                      List<Input> schoolList) {

    super(context.getContext(), groupData, groupLayout, groupFrom, groupTo, childData,
          childLayout, childFrom, childTo);
    this.mContext = context;
    this.schoolList = schoolList;
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
    View homeListItem;

    if (convertView == null) {
      homeListItem = new View(mContext.getContext());
      LayoutInflater inflater = (LayoutInflater) mContext.getActivity()
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      homeListItem = inflater.inflate(R.layout.school_map_drawer_list_items,
                                      parent, false);
    } else {
      homeListItem = convertView;
    }

    Input item = schoolList.get(childPosition);
    TextView textView = (TextView) homeListItem
        .findViewById(R.id.header_title);
    TextView textViewOne = (TextView) homeListItem
        .findViewById(R.id.more_title);
    textView.setText(item.getQuestionInput());


    int index = childPosition + 1;

    textViewOne.setText("" + index);

    return homeListItem;

  }

}
