package com.edu.peers.models;

import com.edu.peers.others.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nelson on 28/02/2017.
 */

public class Quiz {

  private String uuid;
  private String name;
  private String subject;
  private String duration;
  private String timestamp;
  private User user;
  private String instructions;
  private List<Questions> questions;
  private Map<Integer, Map<Integer, Input>> choices = new HashMap<>();
  private String date;
  private Boolean shareWork;
  private int marks;
  private int total;



  public Quiz() {
    this.shareWork=false;
    this.date = Utils.getCurrentDate();
    this.uuid = UUID.randomUUID().toString();
  }

  public Quiz(String name, String subject, String duration, String instructions, String timestamp,
              List<Questions> questions,User user) {
    this.name = name;
    this.subject = subject;
    this.choices = new HashMap<>();
    this.duration = duration;
    this.timestamp = timestamp;
    this.instructions = instructions;
    this.questions = questions;
    this.date = Utils.getCurrentDate();
    this.uuid = UUID.randomUUID().toString();
    this.user=user;
    this.shareWork=false;
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
    if (questions == null) {
      questions = new ArrayList<>();
    }
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

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Boolean getShareWork() {
    return shareWork;
  }

  public void setShareWork(Boolean shareWork) {
    this.shareWork = shareWork;
  }

  public int getMarks() {
    return marks;
  }

  public void setMarks(int marks) {
    this.marks = marks;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }
}
