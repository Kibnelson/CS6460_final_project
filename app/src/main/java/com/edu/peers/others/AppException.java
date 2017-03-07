package com.edu.peers.others;

/**
 * Created by nelson on 6/16/15.
 */

public class AppException extends Exception {
  public AppException() {
  }

  public AppException(String message) {
    super(message);
  }

  public AppException(String message, Throwable cause) {
    super(message, cause);
  }

  public AppException(Throwable cause) {
    super(cause);
  }
}