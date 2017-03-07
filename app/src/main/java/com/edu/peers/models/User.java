package com.edu.peers.models;

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

  public User() {
  }

  public User(String firstName, String lastName, String role, String gender, String username, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role;
    this.gender = gender;
    this.username=username;
    this.password=password;
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
}
