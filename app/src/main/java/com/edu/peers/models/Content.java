package com.edu.peers.models;

import java.util.List;
import java.util.UUID;

/**
 * Created by nelson on 06/03/2017.
 */

public class Content {

  private String uuid;
  private String title;
  private String subject;
  private String tags;
  private List<ContentFile> contentFileList;
  private User user;
  private List<Input> inputList;


  public Content(){
    this.uuid= UUID.randomUUID().toString();
  }

  public Content(String title, String subject, List<ContentFile> contentFileList, User user){
    this.title=title;
    this.subject=subject;
    this.contentFileList=contentFileList;
    this.user=user;
    this.uuid= UUID.randomUUID().toString();
  }


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public List<Input> getInputList() {
    return inputList;
  }

  public void setInputList(List<Input> inputList) {
    this.inputList = inputList;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public List<ContentFile> getContentFileList() {
    return contentFileList;
  }

  public void setContentFileList(
      List<ContentFile> contentFileList) {
    this.contentFileList = contentFileList;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
