package com.edu.peers.models;

import com.edu.peers.others.Utils;

/**
 * Created by nelson on 28/02/2017.
 */

public class Input {
  private int position;
  private String questionInput;
  private String questionVoice;
  private String questionWriting;
  private Boolean selected;
  private String date;
  private User user;

  public Input(){
    selected=false;
  }
  public Input(int position,String questionInput,String questionWriting,String questionVoice){
    this.position=position;
    this.questionInput=questionInput;
    this.questionVoice=questionVoice;
    this.questionWriting=questionWriting;
    this.date= Utils.getCurrentDate();

  }
  public String getQuestionInput() {
    return questionInput;
  }

  public void setQuestionInput(String questionInput) {
    this.questionInput = questionInput;
  }

  public String getQuestionVoice() {
    return questionVoice;
  }

  public void setQuestionVoice(String questionVoice) {
    this.questionVoice = questionVoice;
  }

  public String getQuestionWriting() {
    return questionWriting;
  }

  public void setQuestionWriting(String questionWriting) {
    this.questionWriting = questionWriting;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
