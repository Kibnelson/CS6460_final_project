package com.edu.peers.managers;

import com.google.gson.Gson;

import android.content.Context;

import com.cloudant.sync.documentstore.DocumentRevision;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.models.GradebookObject;
import com.edu.peers.others.AppException;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Hash;

import org.json.JSONObject;

/**
 * Created by nelson on 6/15/15.
 */

public class GradeBookManager {


  private CloudantStore cloudantStore;

  private Context context;

  public GradeBookManager(CloudantStore cloudantStore, Context context) {
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

  public void addGadebook(GradebookObject gradebookObject) {

    try {
      JSONObject jsonObject = new JSONObject(new Gson().toJson(gradebookObject));

      cloudantStore.addDocument(jsonObject, Constants.GRADEBOOK);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public GradebookObject getGradebookObject() {
    DocumentRevision basicDocumentRevision;
    GradebookObject
        gradebookObject = null;

    try {
      basicDocumentRevision = cloudantStore.getDocument(Constants.GRADEBOOK);

      if (basicDocumentRevision != null) {
        gradebookObject =
            new Gson().fromJson(basicDocumentRevision.getBody().toString(), GradebookObject.class);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return gradebookObject;
  }


  public int getDocumentCount() {

    return cloudantStore.getDocumentCount();

  }

}
