package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class GradebookObject {

  private List<Quiz> quizList;

  public GradebookObject(){}


  public List<Quiz> getQuizList() {
    if (quizList==null)
      quizList=new ArrayList<>();

    return quizList;
  }

  public void setQuizList(List<Quiz> quizList) {
    this.quizList = quizList;
  }

}
