package com.edu.peers.managers;

import com.google.gson.Gson;

import android.content.Context;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.others.AppException;
import com.edu.peers.others.Hash;
import com.edu.peers.models.UserObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nelson on 6/15/15.
 */

public class UserManager {


  private CloudantStore cloudantStore;

  private Context context;

  public UserManager(CloudantStore cloudantStore, Context context) {
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

  public void addDocument(UserObject userObject, String userName) {

    PersistenDataManager persistenDataManager = new PersistenDataManager(context);
    String key = persistenDataManager.getPersistentData(userName);
    if (key == null) {
      persistenDataManager.addPersistentData(userName, userName);
    }

    try {
      JSONObject jsonObject = new JSONObject(new Gson().toJson(userObject));

      cloudantStore.addDocument(jsonObject, key);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  public UserObject getDocumentGetDocument(String schoolId) {
    BasicDocumentRevision basicDocumentRevision;
    UserObject
        userObject = null;
    PersistenDataManager persistenDataManager = new PersistenDataManager(context);
    String key = persistenDataManager.getPersistentData(schoolId);


    try {
      basicDocumentRevision = cloudantStore.getDocument(key);

      if (basicDocumentRevision != null) {
        userObject =
            new Gson().fromJson(basicDocumentRevision.getBody().toString(), UserObject.class);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return userObject;
  }


  public boolean checkIfUsernameExists(String userName) {

//
//    boolean userNameExists = false;
//    BasicDocumentRevision basicDocumentRevision;
//
//    try {
//      basicDocumentRevision = cloudantStore.getDocument(userName);
//
//
//      if (basicDocumentRevision != null) {
//        userNameExists=true;
//
//      }
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//    return userNameExists;
//
////    PersistenDataManager persistenDataManager = new PersistenDataManager(context);
////    String key = persistenDataManager.getPersistentData(userName);
////    if (key == null) {
////      userNameExists = false;
////    } else {
////      userNameExists = true;
////    }
    return cloudantStore.containsDocumentKey(userName);
  }

  public int getDocumentCount() {

    return cloudantStore.getDocumentCount();

  }


}
