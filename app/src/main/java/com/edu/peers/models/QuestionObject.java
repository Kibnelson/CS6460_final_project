package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class QuestionObject {

  private List<Questions> questionsList;

  public QuestionObject(){}


  public List<Questions> getQuestionsList() {
    if (questionsList==null)
      questionsList=new ArrayList<>();

    return questionsList;
  }

  public void setQuestionsList(List<Questions> questionsList) {
    this.questionsList = questionsList;
  }

}
