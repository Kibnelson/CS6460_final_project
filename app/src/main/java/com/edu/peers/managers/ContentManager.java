package com.edu.peers.managers;

import com.google.gson.Gson;

import android.content.Context;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.others.AppException;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Hash;
import com.edu.peers.models.ContentObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nelson on 6/15/15.
 */

public class ContentManager {


  private CloudantStore cloudantStore;

  private Context context;

  public ContentManager(CloudantStore cloudantStore, Context context) {
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

  public void addContent(ContentObject contentObject) {

    try {
      JSONObject jsonObject = new JSONObject(new Gson().toJson(contentObject));

      cloudantStore.addDocument(jsonObject, Constants.QUIZ);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  public ContentObject getContentObject() {
    BasicDocumentRevision basicDocumentRevision;
    ContentObject
        contentObject = null;

    try {
      basicDocumentRevision = cloudantStore.getDocument(Constants.QUIZ);

      if (basicDocumentRevision != null) {
        contentObject =
            new Gson().fromJson(basicDocumentRevision.getBody().toString(), ContentObject.class);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return contentObject;
  }


  public int getDocumentCount() {

    return cloudantStore.getDocumentCount();

  }

}
