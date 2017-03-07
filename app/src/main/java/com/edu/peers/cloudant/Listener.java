package com.edu.peers.cloudant;

import com.google.common.eventbus.Subscribe;

import android.os.Handler;
import android.os.Looper;

import com.cloudant.sync.notifications.ReplicationCompleted;
import com.cloudant.sync.notifications.ReplicationErrored;
import com.cloudant.sync.replication.ErrorInfo;
import com.edu.peers.views.SchoolCensus;

import java.util.concurrent.CountDownLatch;

/**
 * A {@code ReplicationListener} that sets a latch when it's told the replication has finished.
 */
public class Listener {

  private final CountDownLatch latch;
  private final Handler mHandler;
  public ErrorInfo error = null;
  public CloudantStore cloudantStore;
  public SchoolCensus schoolCensus;
  public int documentsReplicated;
  public int batchesReplicated;


  Listener(CountDownLatch latch, CloudantStore cloudantStore, SchoolCensus schoolCensus) {
    this.latch = latch;
    this.cloudantStore = cloudantStore;
    this.schoolCensus = schoolCensus;
    this.mHandler = new Handler(Looper.getMainLooper());

  }

  @Subscribe
  public void complete(ReplicationCompleted event) {
    try {

      mHandler.post(new Runnable() {
        @Override
        public void run() {

          cloudantStore.stopAllReplications();


        }


      });

      this.documentsReplicated = event.documentsReplicated;
      this.batchesReplicated = event.batchesReplicated;

      latch.countDown();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Subscribe
  public void error(ReplicationErrored event) {
    this.error = event.errorInfo;
    latch.countDown();
  }
}