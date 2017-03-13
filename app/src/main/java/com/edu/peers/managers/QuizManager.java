package com.edu.peers.managers;

import com.google.gson.Gson;

import android.content.Context;

import com.cloudant.sync.documentstore.DocumentRevision;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.models.QuizObject;
import com.edu.peers.others.AppException;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Hash;

import org.json.JSONObject;

/**
 * Created by nelson on 6/15/15.
 */

public class QuizManager {


  private CloudantStore cloudantStore;

  private Context context;

  public QuizManager(CloudantStore cloudantStore, Context context) {
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

  public void addQuiz(QuizObject quizObject) {

    try {
      JSONObject jsonObject = new JSONObject(new Gson().toJson(quizObject));

      cloudantStore.addDocument(jsonObject, Constants.QUIZ);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public QuizObject getQuizObject() {
    DocumentRevision basicDocumentRevision;
    QuizObject
        quizObject = null;

    try {
      basicDocumentRevision = cloudantStore.getDocument(Constants.QUIZ);

      if (basicDocumentRevision != null) {
        quizObject =
            new Gson().fromJson(basicDocumentRevision.getBody().toString(), QuizObject.class);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return quizObject;
  }


  public int getDocumentCount() {

    return cloudantStore.getDocumentCount();

  }

}
