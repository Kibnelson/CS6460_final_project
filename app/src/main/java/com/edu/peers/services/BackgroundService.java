package com.edu.peers.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by nelson on 2/12/15.
 */
public class BackgroundService extends Service {

  private static final String TAG = "TimeTableService";
  // Binder given to clients
  public final IBinder binder = new LocalBinder();
  public ScheduledExecutorService scheduledExecutorService;
  // Registered callbacks
  public ServiceCallbacks serviceCallbacks;
  public Runnable timeChecker = new Runnable() {
    @Override
    public void run() {

      try {
///        checkNewDataToUpload();
      } catch (Exception e) {
      }
    }
  };

  public void startService() {
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    scheduledExecutorService.scheduleWithFixedDelay(timeChecker, 0, 20, TimeUnit.SECONDS);
  }

  public void startBackgroundService() {
    try {
      startService();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public IBinder onBind(Intent intent) {
    startBackgroundService();
    return binder;
  }

  public void setCallbacks(ServiceCallbacks callbacks) {
    serviceCallbacks = callbacks;
  }

  public void checkNewDataToUpload() {

//    serviceCallbacks.uploadData();
  }

  // Class used for the client Binder.
  public class LocalBinder extends Binder {

    public BackgroundService getService() {
      return BackgroundService.this;
    }
  }


}
