package com.edu.peers.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.edu.peers.others.Constants;

/**
 * Created by nelson on 5/12/15.
 */

public class PersistenDataManager {

  SharedPreferences sharedPreferences;

  public PersistenDataManager(Context context) {
    sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
  }

  public String getPersistentData(String key) {
    return sharedPreferences.getString(key, null);

  }


  public boolean addPersistentData(String key, String value) {
    boolean response = false;
    if (!sharedPreferences.contains(key)) {
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putString(key, value);
      response = editor.commit();
    }
    return response;
  }


}
