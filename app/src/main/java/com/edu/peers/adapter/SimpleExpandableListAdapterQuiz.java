/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.edu.peers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;

import java.util.List;
import java.util.Map;

/**
 * An easy adapter to map static data to group and child views defined in an XML
 * file. You can separately specify the data backing the group as a List of
 * Maps. Each entry in the ArrayList corresponds to one group in the expandable
 * list. The Maps contain the data for each row. You also specify an XML file
 * that defines the views used to display a group, and a mapping from keys in
 * the Map to specific views. This process is similar for a child, except it is
 * one-level deeper so the data backing is specified as a List<List<Map>>,
 * where the first List corresponds to the group of the child, the second List
 * corresponds to the position of the child within the group, and finally the
 * Map holds the data for that particular child.
 */
public abstract class SimpleExpandableListAdapterQuiz extends BaseExpandableListAdapter implements OnDataChangeListener   {
  private List<? extends Map<String, Questions>> mGroupData;
  private int mExpandedGroupLayout;
  private int mCollapsedGroupLayout;
  private String[] mGroupFrom;
  private int[] mGroupTo;

  private List<? extends List<? extends Map<String, Input>>> mChildData;
  private int mChildLayout;
  private int mLastChildLayout;
  private String[] mChildFrom;
  private int[] mChildTo;

  private LayoutInflater mInflater;


  public SimpleExpandableListAdapterQuiz(Context context,
                                     List<? extends Map<String, Questions>> groupData, int groupLayout,
                                     String[] groupFrom, int[] groupTo,
                                     List<? extends List<? extends Map<String, Input>>> childData,
                                     int childLayout, String[] childFrom, int[] childTo) {
    this(context, groupData, groupLayout, groupLayout, groupFrom, groupTo, childData,
         childLayout, childLayout, childFrom, childTo);
  }


  public SimpleExpandableListAdapterQuiz(Context context,
                                     List<? extends Map<String, Questions>> groupData, int expandedGroupLayout,
                                     int collapsedGroupLayout, String[] groupFrom, int[] groupTo,
                                     List<? extends List<? extends Map<String, Input>>> childData,
                                     int childLayout, String[] childFrom, int[] childTo) {
    this(context, groupData, expandedGroupLayout, collapsedGroupLayout,
         groupFrom, groupTo, childData, childLayout, childLayout,
         childFrom, childTo);
  }


  public SimpleExpandableListAdapterQuiz(Context context,
                                     List<? extends Map<String, Questions>> groupData, int expandedGroupLayout,
                                     int collapsedGroupLayout, String[] groupFrom, int[] groupTo,
                                     List<? extends List<? extends Map<String, Input>>> childData,
                                     int childLayout, int lastChildLayout, String[] childFrom,
                                     int[] childTo) {
    mGroupData = groupData;
    mExpandedGroupLayout = expandedGroupLayout;
    mCollapsedGroupLayout = collapsedGroupLayout;
    mGroupFrom = groupFrom;
    mGroupTo = groupTo;

    mChildData = childData;
    mChildLayout = childLayout;
    mLastChildLayout = lastChildLayout;
    mChildFrom = childFrom;
    mChildTo = childTo;

    mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public Object getChild(int groupPosition, int childPosition) {
    return mChildData.get(groupPosition).get(childPosition);
  }

  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                           View convertView, ViewGroup parent) {
    View v;
    if (convertView == null) {
      v = newChildView(isLastChild, parent);
    } else {
      v = convertView;
    }
    bindChildView(v, mChildData.get(groupPosition).get(childPosition), mChildFrom, mChildTo);
    return v;
  }

  /**
   * Instantiates a new View for a child.
   * @param isLastChild Whether the child is the last child within its group.
   * @param parent The eventual parent of this new View.
   * @return A new child View
   */
  public View newChildView(boolean isLastChild, ViewGroup parent) {
    return mInflater.inflate((isLastChild) ? mLastChildLayout : mChildLayout, parent, false);
  }

  private void bindGroupView(View view, Map<String, Questions> data, String[] from, int[] to) {
    int len = to.length;

    for (int i = 0; i < len; i++) {
      TextView v = (TextView)view.findViewById(to[i]);
      if (v != null) {
        v.setText(data.get(from[i]).getQuestionInput());
      }
    }
  }

  private void bindChildView(View view, Map<String, Input> data, String[] from, int[] to) {
    int len = to.length;

    for (int i = 0; i < len; i++) {
      TextView v = (TextView)view.findViewById(to[i]);
      if (v != null) {
        v.setText(data.get(from[i]).getQuestionInput());
      }
    }
  }

  public int getChildrenCount(int groupPosition) {
    return mChildData.get(groupPosition).size();
  }

  public Object getGroup(int groupPosition) {
    return mGroupData.get(groupPosition);
  }

  public int getGroupCount() {
    return mGroupData.size();
  }

  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                           ViewGroup parent) {
    View v;
    if (convertView == null) {
      v = newGroupView(isExpanded, parent);
    } else {
      v = convertView;
    }
    bindGroupView(v, mGroupData.get(groupPosition), mGroupFrom, mGroupTo);
    return v;
  }

  /**
   * Instantiates a new View for a group.
   * @param isExpanded Whether the group is currently expanded.
   * @param parent The eventual parent of this new View.
   * @return A new group View
   */
  public View newGroupView(boolean isExpanded, ViewGroup parent) {
    return mInflater.inflate((isExpanded) ? mExpandedGroupLayout : mCollapsedGroupLayout,
                             parent, false);
  }

  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  public boolean hasStableIds() {
    return true;
  }

}
