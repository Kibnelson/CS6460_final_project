package com.edu.peers.managers;

import com.google.gson.Gson;

import android.content.Context;

import com.cloudant.sync.documentstore.DocumentRevision;
import com.cloudant.sync.documentstore.DocumentStoreException;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.others.AppException;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Hash;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

  public void addDocument(UserObject userObject, String userName) throws Exception {

//    PersistenDataManager persistenDataManager = new PersistenDataManager(context);
//    String key = persistenDataManager.getPersistentData(userName);
//    if (key == null) {
//      persistenDataManager.addPersistentData(userName, userName);
//
//    }


      JSONObject jsonObject = new JSONObject(new Gson().toJson(userObject));

      cloudantStore.addDocument(jsonObject, userName);

  }

  public void updateDocument(UserObject userObject, String userName) throws Exception {

    PersistenDataManager persistenDataManager = new PersistenDataManager(context);
    String key = persistenDataManager.getPersistentData(userName);
    if (key == null) {
      persistenDataManager.addPersistentData(userName, userName);
    }
      JSONObject jsonObject = new JSONObject(new Gson().toJson(userObject));

      cloudantStore.addDocument(jsonObject, key);

  }
  public UserObject getDocumentGetDocument(String schoolId) {
    DocumentRevision basicDocumentRevision;
    UserObject
        userObject = null;
    try {
      basicDocumentRevision = cloudantStore.getDocument(schoolId);

      if (basicDocumentRevision != null) {
        userObject =
            new Gson().fromJson(basicDocumentRevision.getBody().toString(), UserObject.class);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return userObject;
  }

  public List<UserObject> getDocumentGetDocument(List<String> schoolId) {
    List<DocumentRevision> basicDocumentRevision;
    List<UserObject>
        userObjectList = new ArrayList<>();

    try {
      basicDocumentRevision = cloudantStore.getDocument(schoolId);

      for (DocumentRevision documentRevision: basicDocumentRevision){

        if (documentRevision != null) {
         UserObject userObject =
              new Gson().fromJson(documentRevision.getBody().toString(), UserObject.class);
          userObjectList.add(userObject);

        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return userObjectList;
  }


  public boolean checkIfUsernameExists(String userName) throws DocumentStoreException {
    boolean userNameExists = false;
    UserObject userObjectList = getDocumentGetDocument(Constants.USERS);


    if (userObjectList!=null) {
      for (User user : userObjectList.getUserList()) {
        if (user.getUsername().equalsIgnoreCase(userName)) {
          userNameExists = true;
        }
      }
    }

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
//    return cloudantStore.containsDocumentKey(userName);
    return userNameExists;
  }

  public int getDocumentCount() {

    return cloudantStore.getDocumentCount();

  }


}
