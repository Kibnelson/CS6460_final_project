package com.edu.peers.cloudant;

import android.content.Context;

import com.edu.peers.views.SchoolCensus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nelson on 3/18/15.
 */

public class DataAggregator {

  private final CloudantStore cloudantStore;
  private final HashMap<String, JSONArray> eventLists = new HashMap<>();

  public DataAggregator(Context context, SchoolCensus schoolCensus) {

    cloudantStore = new CloudantStore();
    cloudantStore.setContext(context);
    cloudantStore.setSchoolCensus(schoolCensus);
    cloudantStore.startCloudant();
    cloudantStore.pushData();
    cloudantStore.pullData();
  }


  public void closeDocument(Object key) {

    JSONArray collection = eventLists.get(key);
    if (collection == null) {
      collection = new JSONArray();
      eventLists.put(key.toString(), collection);
    }

  }

  public Map<String, JSONArray> asMap() {

    return eventLists;
  }

  public JSONObject buildJSONDocument() {
    JSONObject events = new JSONObject();
    try {

      for (String key : eventLists.keySet()) {
        events.put(key, eventLists.get(key));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return events;
  }

  public synchronized void sendToCloudant(JSONObject retMap) {

    try {

      cloudantStore.addDocumentToDataSTore(retMap);


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public synchronized void updateDataCloudant(JSONObject retMap, String docId) {
    try {

      cloudantStore.updateDocumentToDataSTore(retMap, docId);


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public synchronized void deleteDataCloudant(String docId) {
    try {

      cloudantStore.deleteDocumentToDataSTore(docId);


    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public CloudantStore getCloudantInstance() {

    return cloudantStore;
  }
}
