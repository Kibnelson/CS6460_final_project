package com.edu.peers.cloudant;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cloudant.sync.event.Subscribe;
import com.cloudant.sync.event.notifications.ReplicationCompleted;
import com.cloudant.sync.event.notifications.ReplicationErrored;
import com.edu.peers.others.Constants;
import com.edu.peers.views.SchoolCensus;

import java.util.concurrent.CountDownLatch;

/**
 * A {@code ReplicationListener} that sets a latch when it's told the replication has finished.
 */
public class ListenerPush {

  private final CountDownLatch latch;
  private final Handler mHandler;
  public CloudantStore cloudantStore;
  public int documentsReplicated;
  public int batchesReplicated;


  ListenerPush(CountDownLatch latch, CloudantStore cloudantStore, SchoolCensus schoolCensus) {
    this.latch = latch;
    this.cloudantStore = cloudantStore;
    this.mHandler = new Handler(Looper.getMainLooper());

  }

  @Subscribe
  public void complete(ReplicationCompleted event) {
    try {

      mHandler.post(new Runnable() {
        @Override
        public void run() {

//          cloudantStore.stopAllReplications();
          cloudantStore.stopPullData();
          Log.i(Constants.TAG,">>>>>>>>ReplicationCompletedReplicationCompleted>>>>>>>>>>>>");
        }


      });


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Subscribe
  public void error(ReplicationErrored event) {

    Log.i(Constants.TAG,">>>>>>>>ReplicationErroredReplicationErrored>>>>>>>>>>>>"+event);
    latch.countDown();
  }
}