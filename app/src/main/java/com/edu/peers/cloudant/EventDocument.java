package com.edu.peers.cloudant;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by nelson on 3/18/15.
 */

public class EventDocument {

  private static String TAG = "EventDocument";
  private JSONObject event_data;

  public EventDocument(JSONObject jsonData){
    this.event_data = jsonData;
  }

  public Map<String, Object> asMap() {
    HashMap<String, Object> resp = new HashMap<String, Object>();
    if(event_data != null){
      Iterator<?> keys = event_data.keys();
      while(keys.hasNext()){
        String key = (String)keys.next();
        try {
          resp.put(key, event_data.get(key));
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }
    return new Gson().fromJson(event_data.toString(), new TypeToken<HashMap<String, Object>>(){}.getType());
  }

  public byte[] asBytes() {
    if(event_data != null) return event_data.toString().getBytes();
    else return new byte[0];
  }

}
