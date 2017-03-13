package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class User {

  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String role;
  private String gender;
  private String facephoto;
  private List<String> notificationsId;

  // For teachers n Students
  private List<UserStatistics> quizzesDone;

  // For teachers n Students
  private List<UserStatistics> quizzesCreated;

  // For teachers n Students
  private List<UserStatistics> questionsAsked;

  // For teachers n Students
  private List<UserStatistics> questionsAnswered;

  // For teachers n Students
  private List<UserStatistics> contentUpload;

  // For teachers n Students
  private List<UserStatistics> comments;

  // For teachers n Students
  private List<Notifications> notificationsList;


  public User() {
  }

  public User(String firstName, String lastName, String role, String gender, String username,
              String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
    this.gender = gender;
    this.username = username;
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFacephoto() {
    return facephoto;
  }

  public void setFacephoto(String facephoto) {
    this.facephoto = facephoto;
  }

  public List<UserStatistics> getQuizzesDone() {
    if (quizzesDone==null)
      quizzesDone= new ArrayList<>();
    return quizzesDone;
  }

  public void setQuizzesDone(List<UserStatistics> quizzesDone) {
    this.quizzesDone = quizzesDone;
  }

  public List<UserStatistics> getQuizzesCreated() {
    if (quizzesCreated==null)
      quizzesCreated= new ArrayList<>();
    return quizzesCreated;
  }

  public void setQuizzesCreated(List<UserStatistics> quizzesCreated) {
    this.quizzesCreated = quizzesCreated;
  }

  public List<UserStatistics> getQuestionsAsked() {
    if (questionsAsked==null)
      questionsAsked= new ArrayList<>();
    return questionsAsked;
  }

  public void setQuestionsAsked(List<UserStatistics> questionsAsked) {
    this.questionsAsked = questionsAsked;
  }

  public List<UserStatistics> getQuestionsAnswered() {
    if (questionsAnswered==null)
      questionsAnswered= new ArrayList<>();
    return questionsAnswered;
  }

  public void setQuestionsAnswered(
      List<UserStatistics> questionsAnswered) {

    this.questionsAnswered = questionsAnswered;
  }

  public List<UserStatistics> getContentUpload() {
    if (contentUpload == null) {
      contentUpload = new ArrayList<>();
    }

    return contentUpload;
  }

  public void setContentUpload(List<UserStatistics> contentUpload) {
    this.contentUpload = contentUpload;
  }

  public List<UserStatistics> getComments() {
    if (comments == null) {
      comments = new ArrayList<>();
    }
    return comments;
  }

  public void setComments(List<UserStatistics> comments) {
    this.comments = comments;
  }

  public List<String> getNotificationsId() {
    if (notificationsId == null) {
      notificationsId = new ArrayList<>();
    }
    return notificationsId;
  }

  public void setNotificationsId(List<String> notificationsId) {
    this.notificationsId = notificationsId;
  }

  public List<Notifications> getNotificationsList() {
    if (notificationsList == null) {
      notificationsList = new ArrayList<>();
    }
    return notificationsList;
  }

  public void setNotificationsList(
      List<Notifications> notificationsList) {
    this.notificationsList = notificationsList;
  }
}
