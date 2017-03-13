package com.edu.peers.managers;

import com.google.gson.Gson;

import android.content.Context;

import com.cloudant.sync.documentstore.DocumentRevision;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.models.QuestionsResponsesObject;
import com.edu.peers.others.AppException;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Hash;

import org.json.JSONObject;

/**
 * Created by nelson on 6/15/15.
 */

public class QuestionsResponsesManager {


  private CloudantStore cloudantStore;

  private Context context;

  public QuestionsResponsesManager(CloudantStore cloudantStore, Context context) {
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

  public void addQuestionResponse(QuestionsResponsesObject questionsResponsesObject) {

    try {
      JSONObject jsonObject = new JSONObject(new Gson().toJson(questionsResponsesObject));
      cloudantStore.addDocument(jsonObject, Constants.QUESTIONS_COMMENTS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public QuestionsResponsesObject getQuestionsResponsesObject() {
    DocumentRevision basicDocumentRevision;
    QuestionsResponsesObject
        questionsResponsesObject = null;

    try {
      basicDocumentRevision = cloudantStore.getDocument(Constants.QUESTIONS_COMMENTS);

      if (basicDocumentRevision != null) {
        questionsResponsesObject =
            new Gson().fromJson(basicDocumentRevision.getBody().toString(), QuestionsResponsesObject.class);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return questionsResponsesObject;
  }


  public int getDocumentCount() {

    return cloudantStore.getDocumentCount();

  }

}
