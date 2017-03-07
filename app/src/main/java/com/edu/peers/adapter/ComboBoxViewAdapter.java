package com.edu.peers.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edu.peers.R;


public class ComboBoxViewAdapter extends ArrayAdapter<ComboBoxViewListItem> {


  private final Context context;
  private final ComboBoxViewListItem[] values;
  private String hint = null;
  private boolean selected = false;

  public ComboBoxViewAdapter(Context context, int textViewResourceId,
                             ComboBoxViewListItem[] values) {
    super(context, textViewResourceId, values);
    this.context = context;
    this.values = values;


  }

  public int getCount() {
    return values.length;
  }

  public ComboBoxViewListItem getItem(int position) {
    return values[position];
  }

  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    TextView label = new TextView(context);
    label.setTextColor(Color.BLACK);

    if (!selected && hint != null) {
      label.setText(getHint());
    } else {
      label.setText(values[position].getText());
    }
    return label;
  }

  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
    View homeListItem;

    if (convertView == null) {
      homeListItem = new View(context);
      LayoutInflater inflater = (LayoutInflater) context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      homeListItem = inflater.inflate(R.layout.school_combo_item, parent, false);
    } else {
      homeListItem = convertView;
    }
    ComboBoxViewListItem item = values[position];
    TextView textView = (TextView) homeListItem
        .findViewById(R.id.listitem_label);

    textView.setText(item.getText());

    return homeListItem;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }
}