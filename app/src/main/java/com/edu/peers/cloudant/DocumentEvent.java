package com.edu.peers.cloudant;

import com.edu.peers.others.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class DocumentEvent {

  private static final DocumentEvent Instance = new DocumentEvent();
  private JSONObject body;
  private HashMap<String, JSONArray> eventLists;

  private DocumentEvent() {
  }

  public static synchronized DocumentEvent getInstance() {
    return Instance;
  }

  public void openDocument(HashMap<String, JSONArray> eventLists) {

    this.eventLists = eventLists;

    body = new JSONObject();
    JSONArray collection = this.eventLists.get(EVENT_KEYS.DOC_STARTED);
    if (collection == null) {
      collection = new JSONArray();
      this.eventLists.put(EVENT_KEYS.DOC_STARTED.toString(), collection);
    }
    try {
      body.put(DOCUMENT_EVENT_KEYS.timestamp.toString(), Utils.getCurrentTimestamp());

    } catch (JSONException e) {
      e.printStackTrace();
    }
    collection.put(body);
  }


  public void addErrorMetadata(String section) {
    JSONArray collection = eventLists.get(EVENT_KEYS.ERROR_METADATA);
    if (collection == null) {
      collection = new JSONArray();
      eventLists.put(EVENT_KEYS.ERROR_METADATA.toString(), collection);

    }
    JSONObject body = new JSONObject();
    try {
      body.put("ERROR", section);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    collection.put(body);
  }

  public void addUserMetadata(String userid, String School) {
    JSONArray collection = eventLists.get(EVENT_KEYS.USER_METADATA);
    if (collection == null) {
      collection = new JSONArray();
      eventLists.put(EVENT_KEYS.USER_METADATA.toString(), collection);

    }
    JSONObject body = new JSONObject();
    try {
      body.put(DOCUMENT_EVENT_KEYS.usersId.toString(), userid);
      body.put("SchoolID", School);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    collection.put(body);
  }


  public void addStatisticsMetadata(String numberOfDocsInQueue, String section, String version) {
    JSONArray collection = eventLists.get(EVENT_KEYS.STATISTICS_METADATA);
    if (collection == null) {
      collection = new JSONArray();
      eventLists.put(EVENT_KEYS.STATISTICS_METADATA.toString(), collection);

    }
    JSONObject body = new JSONObject();
    try {
      body.put("NUMBER_DOCUMENTS", numberOfDocsInQueue);
      body.put("SECTION", section);
      body.put("APP_VERSION", version);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    collection.put(body);
  }


  public void addDeviceMetadata(String imei) {
    JSONArray collection = eventLists.get(EVENT_KEYS.DEVICE_DATA);
    if (collection == null) {
      collection = new JSONArray();
      eventLists.put(EVENT_KEYS.DEVICE_DATA.toString(), collection);
    }
    JSONObject body = new JSONObject();
    try {
      body.put(DOCUMENT_EVENT_KEYS.imei.toString(), imei);

    } catch (JSONException e) {
      e.printStackTrace();
    }

    collection.put(body);
  }

  public void addEnvironmentPhotosMetadata(String category, Integer imageSize, String userid) {
    JSONArray collection = eventLists.get(EVENT_KEYS.ENVIRONMENT_PHOTOS);
    if (collection == null) {
      collection = new JSONArray();
      eventLists.put(EVENT_KEYS.ENVIRONMENT_PHOTOS.toString(), collection);

    }
    JSONObject body = new JSONObject();
    try {
      body.put(DOCUMENT_EVENT_KEYS.category.toString(), category);
      body.put(DOCUMENT_EVENT_KEYS.imageSize.toString(), imageSize);
      body.put(DOCUMENT_EVENT_KEYS.imageSize.toString(), imageSize);
      body.put(DOCUMENT_EVENT_KEYS.imageSize.toString(), imageSize);
      body.put(DOCUMENT_EVENT_KEYS.usersId.toString(), userid);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    collection.put(body);
  }


  public void addAttendancePhotosMetadata(String grade, String stream, Integer imageSize,
                                          String userid) {
    JSONArray collection = eventLists.get(EVENT_KEYS.ATTENDANCE_PHOTOS);
    if (collection == null) {
      collection = new JSONArray();
      eventLists.put(EVENT_KEYS.ATTENDANCE_PHOTOS.toString(), collection);

    }
    JSONObject body = new JSONObject();
    try {
      body.put(DOCUMENT_EVENT_KEYS.grade.toString(), grade);
      body.put(DOCUMENT_EVENT_KEYS.stream.toString(), stream);
      body.put(DOCUMENT_EVENT_KEYS.timestamp.toString(), Utils.getCurrentTimestampLong());
      body.put(DOCUMENT_EVENT_KEYS.imageSize.toString(), imageSize);
      body.put(DOCUMENT_EVENT_KEYS.usersId.toString(), userid);
    } catch (JSONException e) {
      e.printStackTrace();
    }


    collection.put(body);
  }


}
