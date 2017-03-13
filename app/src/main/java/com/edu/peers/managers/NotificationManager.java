package com.edu.peers.managers;

import com.google.gson.Gson;

import android.content.Context;

import com.cloudant.sync.documentstore.DocumentRevision;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.models.NotificationObject;
import com.edu.peers.others.AppException;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Hash;

import org.json.JSONObject;

/**
 * Created by nelson on 6/15/15.
 */

public class NotificationManager {


  private CloudantStore cloudantStore;

  private Context context;

  public NotificationManager(CloudantStore cloudantStore, Context context) {
    this.cloudantStore = cloudantStore;
    this.context = context;
  }

  /**
   * Authenticate a User
   *
   * @throws AppException App Exception
   */
  public static boolean authenticate(String clearPassword, String encryptedPassword)
      throws AppException {

    if (Hash.checkPassword(clearPassword, encryptedPassword)) {
      return true;
    }

    return false;
  }

  public void addNotification(NotificationObject notificationObject) {

    try {
      JSONObject jsonObject = new JSONObject(new Gson().toJson(notificationObject));

      cloudantStore.addDocument(jsonObject, Constants.NOTIFICATIONS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public NotificationObject getNotificationObject() {
    DocumentRevision basicDocumentRevision;
    NotificationObject
        notificationObject = null;

    try {
      basicDocumentRevision = cloudantStore.getDocument(Constants.NOTIFICATIONS);

      if (basicDocumentRevision != null) {
        notificationObject =
            new Gson().fromJson(basicDocumentRevision.getBody().toString(), NotificationObject.class);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return notificationObject;
  }


  public int getDocumentCount() {

    return cloudantStore.getDocumentCount();

  }

}
