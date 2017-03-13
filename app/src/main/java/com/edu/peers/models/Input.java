package com.edu.peers.models;

import com.edu.peers.others.Utils;

import java.util.UUID;

/**
 * Created by nelson on 28/02/2017.
 */

public class Input {
  private String uuid;
  private int position;
  private String questionInput;
  private String questionVoice;
  private String questionWriting;
  private Boolean selected;
  private Boolean recommended=false;
  private int thumbsUp;
  private int thumbsDown;
  private String date;
  private User user;
  private Boolean answer;

  public Input(){
    this.selected=false;
    this.answer=false;
    this.date= Utils.getCurrentDate();
    this.uuid= UUID.randomUUID().toString();
  }
  public Input(int position,String questionInput,String questionWriting,String questionVoice, User user){
    this.selected=false;
    this.answer=false;
    this.position=position;
    this.questionInput=questionInput;
    this.questionVoice=questionVoice;
    this.questionWriting=questionWriting;
    this.user=user;
    this.date= Utils.getCurrentDate();
    this.uuid= UUID.randomUUID().toString();

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

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Boolean getRecommended() {
    return recommended;
  }

  public void setRecommended(Boolean recommended) {
    this.recommended = recommended;
  }

  public int getThumbsUp() {
    return thumbsUp;
  }

  public void setThumbsUp(int thumbsUp) {
    this.thumbsUp = thumbsUp;
  }

  public int getThumbsDown() {
    return thumbsDown;
  }

  public void setThumbsDown(int thumbsDown) {
    this.thumbsDown = thumbsDown;
  }

  public Boolean getAnswer() {
    return answer;
  }

  public void setAnswer(Boolean answer) {
    this.answer = answer;
  }
}
