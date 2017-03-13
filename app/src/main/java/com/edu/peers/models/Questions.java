package com.edu.peers.models;

import com.edu.peers.others.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nelson on 28/02/2017.
 */

public class Questions {
  private String uuid;
  private String index;
  private String questionInput;
  private String questionVoice;
  private String questionWriting;
  private List<Input> answers;
  private List<String> selectAnswers;
  private List<Input> choices;
  private List<Input> userSelections;
  private String points;
  private String duration;
  private List<User> users;
  private String date;
  private User user;
  private List<Input> responses;
  private String  questionType;
  private User  selectedUser;


  public Questions(){
    this.date= Utils.getCurrentDate();
    this.uuid= UUID.randomUUID().toString();
  }

  public Questions(String questionInput,String questionWriting,String questionVoice, List<Input> answers,List<Input> choices, User user){
    this.questionInput=questionInput;
    this.questionWriting=questionWriting;
    this.questionVoice=questionVoice;
    this.answers=answers;
    this.choices=choices;
    this.date= Utils.getCurrentDate();
    this.uuid= UUID.randomUUID().toString();
    this.user=user;
  }
  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }


  public List<Input> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Input> answers) {
    this.answers = answers;
  }

  public List<Input> getChoices() {
    return choices;
  }

  public void setChoices(List<Input> choices) {
    this.choices = choices;
  }

  public String getPoints() {
    return points;
  }

  public void setPoints(String points) {
    this.points = points;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
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

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public List<String> getSelectAnswers() {
    return selectAnswers;
  }

  public void setSelectAnswers(List<String> selectAnswers) {
    this.selectAnswers = selectAnswers;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Input> getResponses() {
    if (responses==null)
      responses= new ArrayList<>();
    return responses;
  }

  public void setResponses(List<Input> responses) {
    this.responses = responses;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getQuestionType() {
    return questionType;
  }

  public void setQuestionType(String questionType) {
    this.questionType = questionType;
  }

  public User getSelectedUser() {
    return selectedUser;
  }

  public void setSelectedUser(User selectedUser) {
    this.selectedUser = selectedUser;
  }

  public List<Input> getUserSelections() {
    return userSelections;
  }

  public void setUserSelections(List<Input> userSelections) {
    this.userSelections = userSelections;
  }
}
