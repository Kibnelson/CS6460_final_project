package com.edu.peers.network;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Utils;
import com.edu.peers.views.SchoolCensus;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by nelson on 5/11/15.
 */

public class VolleyManager extends BroadcastReceiver {

  private final SchoolCensus schoolCensus;
  RequestQueue queue;
  private Context context;
  private ScheduledExecutorService scheduledExecutorService;
  private ProgressDialog progressDialog;
  private Runnable checkData = new Runnable() {
    @Override
    public void run() {

      try {

        // pullServerData();

        Thread.sleep(10000);
      } catch (Exception e) {
      }
    }
  };

  public VolleyManager(Context context, SchoolCensus schoolCensus) {
    this.context = context;
    queue = Volley.newRequestQueue(context);
    this.schoolCensus = schoolCensus;

  }

  public static void largeLog(String tag, String content) {
    if (content.length() > 4000) {
      Log.d(tag, content.substring(0, 4000));
      largeLog(tag, content.substring(4000));
    } else {
      Log.d(tag, content);
    }
  }

  @Override
  public void onReceive(Context context, Intent intent) {

    this.context = context;
    ConnectivityManager
        connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

    NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


  }

  public void startService() {
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    scheduledExecutorService.scheduleWithFixedDelay(checkData, 0, 60, TimeUnit.SECONDS);

  }

  public void postData(String url, final Map<String, String> data) {

    StringRequest
        sr = new StringRequest(
        Request.Method.GET, url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {

              Log.d(Constants.TAG, "Completed " + response);

            } catch (Exception e) {
            }
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.e(Constants.TAG, "Error " + error.getMessage());
      }
    }) {

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/x-www-form-urlencoded");
        return params;
      }
    };

    queue.add(sr);
  }

  public void sendJsonObjectRequestRequest(final Context context, String url, final JSONObject data,
                                           final int tag, final int updateIndex) {

    if (Constants.LOGIN_REQUEST == tag) {
      progressDialog = schoolCensus.showProgessDialog(context, "Loading ...");
      progressDialog.show();
    }



    JsonObjectRequest notificationsRequest = new JsonObjectRequest
        (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {

            Log.i(Constants.TAG, "RESPONSE" + response);

            if (progressDialog != null) {
              progressDialog.dismiss();
            }

            switch (tag) {

              default:
            }
          }
        }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

            Log.i(Constants.TAG, "RESPONSE ERROR" + error.getMessage());
            if (progressDialog != null) {
              progressDialog.dismiss();
            
            }

            switch (tag) {

              default:
            }


          }
        });
    notificationsRequest.setTag(tag);
    notificationsRequest.getTag();
    notificationsRequest.setRetryPolicy(new DefaultRetryPolicy(
        5000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    queue.add(notificationsRequest);
  }

  public void upload(String url, final JSONObject data) {

    StringRequest
        sr = new StringRequest(
        Request.Method.POST, url,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            try {

              Log.d(Constants.TAG, "Completed " + response);

            } catch (Exception e) {
            }
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
//        progressDialog.hide();
        Log.e(Constants.TAG, "Error " + error.getMessage());
      }
    }) {
      @Override
      protected Map<String, String> getParams() {



      Map<String, String> feedbackMap = Utils.asMap(data);

        Log.e(Constants.TAG, "feedbackMap " + feedbackMap.toString());
        return feedbackMap;
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        return params;
      }
    };

    queue.add(sr);
  }


}
