package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class QuizObject {

  private List<Quiz> quizList;

  public QuizObject(){}


  public List<Quiz> getQuizList() {
    if (quizList==null)
      quizList=new ArrayList<>();

    return quizList;
  }

  public void setQuizList(List<Quiz> quizList) {
    this.quizList = quizList;
  }

}
