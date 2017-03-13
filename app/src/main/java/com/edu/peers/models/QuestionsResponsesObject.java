package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class QuestionsResponsesObject {

  private List<QuestionsResponses> questionsResponsesList;

  public QuestionsResponsesObject(){}


  public List<QuestionsResponses> getQuestionsResponsesList() {
    if (questionsResponsesList==null)
      questionsResponsesList= new ArrayList<>();
    return questionsResponsesList;
  }

  public void setQuestionsResponsesList(
      List<QuestionsResponses> questionsResponsesList) {
    this.questionsResponsesList = questionsResponsesList;
  }
}
