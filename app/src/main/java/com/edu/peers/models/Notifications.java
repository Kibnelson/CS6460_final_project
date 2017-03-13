package com.edu.peers.models;

import java.util.UUID;

/**
 * Created by nelson on 28/02/2017.
 */

public class Notifications {

  private String uuid;
  private String message;
  private Boolean read;

  public Notifications(){ this.uuid= UUID.randomUUID().toString();}

  public Notifications(String message){
    this.message=message;
    this.uuid= UUID.randomUUID().toString();
    read=false;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public Boolean getRead() {
    return read;
  }

  public void setRead(Boolean read) {
    this.read = read;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
