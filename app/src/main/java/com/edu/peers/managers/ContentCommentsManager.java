package com.edu.peers.managers;

import com.google.gson.Gson;

import android.content.Context;

import com.cloudant.sync.documentstore.DocumentRevision;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.models.ContentCommentsObject;
import com.edu.peers.others.AppException;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Hash;

import org.json.JSONObject;

/**
 * Created by nelson on 6/15/15.
 */

public class ContentCommentsManager {


  private CloudantStore cloudantStore;

  private Context context;

  public ContentCommentsManager(CloudantStore cloudantStore, Context context) {
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

  public void addContentComment(ContentCommentsObject contentCommentsObject) {

    try {
      JSONObject jsonObject = new JSONObject(new Gson().toJson(contentCommentsObject));
      cloudantStore.addDocument(jsonObject, Constants.CONTENT_FILES_COMMENTS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public ContentCommentsObject getContentObjectComments() {
    DocumentRevision basicDocumentRevision;
    ContentCommentsObject
        contentCommentsObject = null;

    try {
      basicDocumentRevision = cloudantStore.getDocument(Constants.CONTENT_FILES_COMMENTS);

      if (basicDocumentRevision != null) {
        contentCommentsObject =
            new Gson().fromJson(basicDocumentRevision.getBody().toString(), ContentCommentsObject.class);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return contentCommentsObject;
  }


  public int getDocumentCount() {

    return cloudantStore.getDocumentCount();

  }

}
