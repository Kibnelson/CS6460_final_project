package com.edu.peers.models;

import com.edu.peers.others.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by nelson on 06/03/2017.
 */

public class ContentFile {

  private String uuid;
  private String content;
  private String name;
  private String fileName;
  private String filePath;
  private User user;
  private List<Input> inputList;
  private String date;


  public ContentFile(){
    this.date= Utils.getCurrentDate();
    this.uuid= UUID.randomUUID().toString();
  }

  public ContentFile( String content, User user){
    this.content=content;
    this.user=user;
    this.date= Utils.getCurrentDate();
    this.uuid= UUID.randomUUID().toString();
  }


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public List<Input> getInputList() {
    if (inputList==null)
      inputList= new ArrayList<>();

    return inputList;
  }

  public void setInputList(List<Input> inputList) {
    this.inputList = inputList;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
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
}
