package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class ContentObject {

  private List<Content> contentList;

  public ContentObject(){}


  public List<Content> getContentList() {
    if (contentList==null)
      contentList=new ArrayList<>();

    return contentList;
  }

  public void setContentList(List<Content> contentList) {
    this.contentList = contentList;
  }

}
