package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class UserObject {

  private User user;
  private int position;
  private List<User> userList;

  private boolean editObject;
  public UserObject(){}

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public boolean isEditObject() {
    return editObject;
  }

  public void setEditObject(boolean editObject) {
    this.editObject = editObject;
  }

  public List<User> getUserList() {
    if (userList==null)
      userList= new ArrayList<>();
    return userList;
  }

  public void setUserList(List<User> userList) {
    this.userList = userList;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }
}
