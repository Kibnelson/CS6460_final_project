package com.edu.peers.models;

/**
 * Created by nelson on 07/03/2017.
 */

public class UserStatisticsEntry {

  public float value;
  public String uuid;
  public String category;

  public UserStatisticsEntry(float value,String uuid, String category){
    this.value=value;
    this.uuid=uuid;
    this.category=category;
  }


}
