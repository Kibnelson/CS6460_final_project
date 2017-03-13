package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class ContentCommentsObject {

  private List<ContentComments> contentCommentsList;

  public ContentCommentsObject(){}

  public List<ContentComments> getContentCommentsList() {
    if (contentCommentsList==null)
      contentCommentsList=new ArrayList<>();
    return contentCommentsList;
  }

  public void setContentCommentsList(
      List<ContentComments> contentCommentsList) {
    this.contentCommentsList = contentCommentsList;
  }
}
