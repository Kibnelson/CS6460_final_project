package com.edu.peers.models;

import java.util.UUID;

/**
 * Created by nelson on 10/03/2017.
 */

public class QuestionsResponses {

  private String uuid;
  private String questionUUID;
  private Input input;

  public QuestionsResponses(String questionUUID, Input input){
    this.questionUUID=questionUUID;
    this.input=input;
    this.uuid= UUID.randomUUID().toString();
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }


  public Input getInput() {
    return input;
  }

  public void setInput(Input input) {
    this.input = input;
  }

  public String getQuestionUUID() {
    return questionUUID;
  }

  public void setQuestionUUID(String questionUUID) {
    this.questionUUID = questionUUID;
  }
}
