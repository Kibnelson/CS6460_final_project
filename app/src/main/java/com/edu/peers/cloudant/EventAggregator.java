package com.edu.peers.cloudant;

import android.content.Context;

import com.edu.peers.others.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EventAggregator {

    private String TAG = "EventAggregator";
    private HashMap<String, JSONArray> eventLists = new HashMap<String, JSONArray>();
    private DocumentEvent docEvent;
    private Context appContext;
    private CloudantStore cloudantStore;
    private String imei;

    public EventAggregator(CloudantStore cloudantStore,Context context) {
        this.appContext = context;
        this.cloudantStore = cloudantStore;
//        this.imei = Utils.getIMEI(context);
    }

    public Object openDocument() {

        docEvent = DocumentEvent.getInstance();
        docEvent.openDocument(eventLists);

        return docEvent;

    }

    public void closeDocument() {

        JSONArray collection = eventLists.get(EVENT_KEYS.DOC_ENDED);
        if (collection == null) {
            collection = new JSONArray();
            eventLists.put(EVENT_KEYS.DOC_ENDED.toString(), collection);
        }

        JSONObject body = new JSONObject();

        try {
            body.put(DOCUMENT_EVENT_KEYS.timestamp.toString(), Utils.getCurrentTimestamp());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        collection.put(body);


    }

    public Map<String, JSONArray> asMap() {

        return eventLists;
    }

    public JSONObject buildJSONDocument(String typeDoc, String schoolId,String category,String type) {


        JSONObject parentDoc = new JSONObject();
        JSONObject events = new JSONObject();


        try {

            for (String key : eventLists.keySet()) {
                events.put(key, eventLists.get(key));
            }
            parentDoc.put(typeDoc, events);

            parentDoc.put("schoolId", schoolId);
            parentDoc.put("category", category);
            parentDoc.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parentDoc;

    }



    public int getDocCount() {

    return cloudantStore.getDocumentCount();


    }

    public void pushData() {

         cloudantStore.pushData();


    }



}
