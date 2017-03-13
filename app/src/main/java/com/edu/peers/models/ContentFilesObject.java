package com.edu.peers.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nelson on 27/02/2017.
 */

public class ContentFilesObject {

  private List<ContentFile> contentFileList;

  public ContentFilesObject(){}

  public List<ContentFile> getContentFileList() {
    if (contentFileList==null)
      contentFileList=new ArrayList<>();

    return contentFileList;
  }

  public void setContentFileList(List<ContentFile> contentFileList) {
    this.contentFileList = contentFileList;
  }
}
