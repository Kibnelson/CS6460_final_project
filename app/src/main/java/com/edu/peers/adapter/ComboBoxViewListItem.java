package com.edu.peers.adapter;

/**
 * Created by nelson on 3/17/15.
 */

public class ComboBoxViewListItem {

  private String id;
  private String value;
  private String text;

  public ComboBoxViewListItem(String id, String text, String value) {
    this.id = id;
    this.text = text;
    this.value = value;
  }

  public String getID() {
    return id;
  }

  public void setID(String id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}
