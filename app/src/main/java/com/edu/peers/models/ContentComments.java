package com.edu.peers.models;

import java.util.UUID;

/**
 * Created by nelson on 10/03/2017.
 */

public class ContentComments {

  private String uuid;
  private String contentUUID;
  private String contentFileUUID;
  private Input input;

  public ContentComments(String contentUUID,String contentFileUUID, Input input){
    this.contentFileUUID=contentFileUUID;
    this.contentUUID=contentUUID;
    this.input=input;
    this.uuid= UUID.randomUUID().toString();
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getContentUUID() {
    return contentUUID;
  }

  public void setContentUUID(String contentUUID) {
    this.contentUUID = contentUUID;
  }

  public String getContentFileUUID() {
    return contentFileUUID;
  }

  public void setContentFileUUID(String contentFileUUID) {
    this.contentFileUUID = contentFileUUID;
  }

  public Input getInput() {
    return input;
  }

  public void setInput(Input input) {
    this.input = input;
  }
}
