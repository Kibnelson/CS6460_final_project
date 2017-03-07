package com.edu.peers.managers;

import com.google.gson.Gson;

import android.content.Context;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.others.AppException;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Hash;
import com.edu.peers.models.QuestionObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nelson on 6/15/15.
 */

public class QuestionManager {


  private CloudantStore cloudantStore;

  private Context context;

  public QuestionManager(CloudantStore cloudantStore, Context context) {
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

  public void addQuestion(QuestionObject questionObject) {

    try {
      JSONObject jsonObject = new JSONObject(new Gson().toJson(questionObject));

      cloudantStore.addDocument(jsonObject, Constants.QUIZ);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  public QuestionObject getQuestionObject() {
    BasicDocumentRevision basicDocumentRevision;
    QuestionObject
        questionObject = null;

    try {
      basicDocumentRevision = cloudantStore.getDocument(Constants.QUIZ);

      if (basicDocumentRevision != null) {
        questionObject =
            new Gson().fromJson(basicDocumentRevision.getBody().toString(), QuestionObject.class);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return questionObject;
  }


  public int getDocumentCount() {

    return cloudantStore.getDocumentCount();

  }

}
