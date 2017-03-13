package com.edu.peers.models;

/**
 * Created by nelson on 07/03/2017.
 */

public class UserStatistics {

  public String date;
  public UserStatisticsEntry value;

  public UserStatistics(String date,float value,String uuid, String category){

    this.date=date;
    this.value= new UserStatisticsEntry(value,uuid,category);
  }



}
