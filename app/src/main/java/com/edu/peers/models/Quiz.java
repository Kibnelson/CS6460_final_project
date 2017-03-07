package com.edu.peers.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nelson on 28/02/2017.
 */

public class Quiz {
  private String name;
  private String subject;
  private String duration;
  private String timestamp;
  private User user;
  private String instructions;
  private List<Questions> questions;
  private Map<Integer, Map<Integer, Input>> choices = new HashMap<>();


  public Quiz(){}

  public Quiz(String name,String subject,String duration, String instructions, String timestamp,List<Questions> questions){
    this.name=name;
    this.subject=subject;
    this.choices = new HashMap<>();
    this.duration=duration;
    this.timestamp=timestamp;
    this.instructions=instructions;
    this.questions=questions;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public List<Questions> getQuestions() {
    if (questions==null)
      questions= new ArrayList<>();
    return questions;
  }

  public void setQuestions(List<Questions> questions) {
    this.questions = questions;
  }

  public Map<Integer, Map<Integer, Input>> getChoices() {
    return choices;
  }

  public void setChoices(
      Map<Integer, Map<Integer, Input>> choices) {
    this.choices = choices;
  }
}
