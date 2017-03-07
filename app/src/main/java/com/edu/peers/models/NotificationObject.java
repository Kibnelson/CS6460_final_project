package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class NotificationObject {

  private List<Notifications> notificationsList;

  public NotificationObject() {
  }


  public List<Notifications> getNotificationsList() {
    if (notificationsList == null) {
      notificationsList = new ArrayList<>();
    }

    return notificationsList;
  }

  public void setNotificationsList(
      List<Notifications> notificationsList) {
    this.notificationsList = notificationsList;
  }
}
