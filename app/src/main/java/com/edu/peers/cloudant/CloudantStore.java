package com.edu.peers.cloudant;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cloudant.sync.documentstore.DocumentBodyFactory;
import com.cloudant.sync.documentstore.DocumentRevision;
import com.cloudant.sync.documentstore.DocumentStore;
import com.cloudant.sync.documentstore.DocumentStoreException;
import com.cloudant.sync.documentstore.DocumentStoreNotOpenedException;
import com.cloudant.sync.event.Subscribe;
import com.cloudant.sync.event.notifications.ReplicationCompleted;
import com.cloudant.sync.event.notifications.ReplicationErrored;
import com.cloudant.sync.replication.Replicator;
import com.cloudant.sync.replication.ReplicatorBuilder;
import com.edu.peers.managers.PersistenDataManager;
import com.edu.peers.others.Constants;
import com.edu.peers.views.SchoolCensus;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static java.util.UUID.randomUUID;

//import com.cloudant.sync.datastore.BasicDocumentRevision;
//import com.cloudant.sync.datastore.Datastore;
//import com.cloudant.sync.datastore.DatastoreManager;
//import com.cloudant.sync.datastore.DocumentBody;
//import com.cloudant.sync.datastore.MutableDocumentRevision;
//import com.cloudant.sync.datastore.UnsavedStreamAttachment;
//import com.cloudant.sync.query.IndexManager;
//import com.cloudant.sync.replication.Replication;


/**
 * Created by nelson on 1/28/15.
 */

public class CloudantStore implements CloudantSaveListener {

  private String dataStoreName;
  private String dataStoreManagerDirectory;
  private String databaseName;
  private String apiKey;
  private String apiPassword;
  private String host;
  private SchoolCensus schoolCensus;
  private Context context;
  private DocumentStore mDocumentStore;
  private Handler mHandler;
  private URI uri;
  private Replicator mPullReplicator;
  private Replicator mPushReplicator;
//  private Replicator mPullReplicator;

  public CloudantStore() {
  }

  public static boolean deleteDirectory(File path) {
    return (path.delete());
  }

  public void startCloudant() {
    this.mHandler = new Handler(Looper.getMainLooper());
    loadCloudantProperties();
    createDataStore();
//    deleteLocalDatastore();

  }

  void stopAllReplications() {

    if (this.mPushReplicator != null) {
      this.mPushReplicator.stop();
    }

    if (this.mPullReplicator != null) {

      this.mPullReplicator.stop();


    }

  }

  void createDataStore() {
    File path = context.getDir(
        dataStoreManagerDirectory,
        Context.MODE_PRIVATE
    );

    try {
      this.mDocumentStore = DocumentStore.getInstance(new File(path, dataStoreName));
    } catch (DocumentStoreNotOpenedException e) {
      Log.e(Constants.TAG, "Unable to open DocumentStore", e);
    }

    Log.d(Constants.TAG, "Set up database at " + path.getAbsolutePath());

    // Set up the replicator objects from the app's settings.
    try {
      this.reloadReplicationSettings();
    } catch (URISyntaxException e) {
      Log.e(Constants.TAG, "Unable to construct remote URI from configuration", e);
    }

    // Allow us to switch code called by the ReplicationListener into
    // the main thread so the UI can update safely.
    this.mHandler = new Handler(Looper.getMainLooper());

    Log.d(Constants.TAG, "TasksModel set up " + path.getAbsolutePath());


  }


  /**
   * <p>Stops running replications and reloads the replication settings from the app's
   * preferences.</p>
   */
  public void reloadReplicationSettings()
      throws URISyntaxException {

    // Stop running replications before reloading the replication
    // settings.
    // The stop() method instructs the replicator to stop ongoing
    // processes, and to stop making changes to the DocumentStore. Therefore,
    // we don't clear the listeners because their complete() methods
    // still need to be called once the replications have stopped
    // for the UI to be updated correctly with any changes made before
    // the replication was stopped.
    this.stopAllReplications();

    // Set up the new replicator objects
    URI uri = this.createServerURI();

    mPullReplicator = ReplicatorBuilder.pull().to(mDocumentStore).from(uri).build();
    mPushReplicator = ReplicatorBuilder.push().from(mDocumentStore).to(uri).build();

    mPushReplicator.getEventBus().register(this);
    mPullReplicator.getEventBus().register(this);

    Log.d(Constants.TAG, "Set up replicators for URI:" + uri.toString());
  }


  public void pullPicturesForSchool(String schoolId) {

//    if (Utils.networkAvailable(context)) {
//
//      Map<String, String> parameters = new HashMap<String, String>();
//      parameters.put("schoolId", schoolId);
//      PullFilter pullFilter = new PullFilter("schoolPictures/schoolAllDocs", parameters);
//      Replicator replicator = ReplicatorBuilder.pull()
//          .from(uri)
//          .to(this.environementDataStore)
//          .filter(pullFilter)
//          .build();
//
//
//      replicator.start();
//    }
  }


  private URI createServerURI()
      throws URISyntaxException {

    String url = "https://" + apiKey + ":" + apiPassword + "@" + host + "/" + databaseName;

    return new URI(url);
  }

  public synchronized void deleteDocumentToDataSTore(String docId) {

    try {
      DocumentRevision retrieved = mDocumentStore.database().read(docId);

      mDocumentStore.database().delete(retrieved);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public synchronized void updateDocumentToDataSTore(JSONObject data, String docId) {

    try {

      DocumentRevision retrieved = mDocumentStore.database().read(docId);
      retrieved.setBody(DocumentBodyFactory.create(new EventDocument(data).asMap()));
      // Note that "updated" is the new DocumentRevision with a new revision ID
      mDocumentStore.database().update(retrieved);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public synchronized void addDocumentToDataSTore(JSONObject data) {

    try {
      String key = getDocumentId();
      if (key != null) {
        // get doc from store using the key
        updateDocumentToDataSTore(data, key);

      } else {
        // No doc added to cloudant add a new one and store the key to shared preference
        UUID uuid = randomUUID();
        storeDocument(data, uuid.toString());


      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean containsDocumentKey(String key) throws DocumentStoreException {
    return mDocumentStore.database().contains(key);
  }

  public synchronized void addDocument(JSONObject data, String key) throws Exception {

    if (mDocumentStore.database().getDocumentCount()>0 && mDocumentStore.database().contains(key)) {
      // get doc from store using the key
      updateDocumentToDataSTore(data, key);

    } else {

      // No doc added to cloudant add a new one and store the key to shared preference
      storeDocument(data, key);
//        updateDocumentToDataSTore(data, key);

    }


  }

  private String getDocumentId() {
    PersistenDataManager persistenDataManager = new PersistenDataManager(context);

    String key = persistenDataManager.getPersistentData(Constants.PREFS_KEY);
    return key;
  }

  public DocumentRevision storeDocument(JSONObject data, String uuid) {
    DocumentRevision documentRevision = null;
    try {

      // Create a document with a document id as the constructor argument
      DocumentRevision rev = new DocumentRevision(uuid);

      rev.setBody(DocumentBodyFactory.create(new EventDocument(data).asMap()));
//      rev.getAttachments()
//      Attachment attachment= rev.getAttachments().get("");
//      attachment.g

      documentRevision = mDocumentStore.database().create(rev);


    } catch (Exception e) {
      e.printStackTrace();
    }

    return documentRevision;
  }

  public DocumentRevision readDocument() {
    DocumentRevision retrieved = null;
    try {
      PersistenDataManager persistenDataManager = new PersistenDataManager(context);

      String key = persistenDataManager.getPersistentData(Constants.PREFS_KEY);
      retrieved = mDocumentStore.database().read(key);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return retrieved;
  }

  public DocumentRevision getDocument(String key) {


    DocumentRevision retrieved = null;
    try {
      retrieved = mDocumentStore.database().read(key);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return retrieved;
  }

  public List<DocumentRevision> getDocument(List<String> key) {


    List<DocumentRevision> retrieved = null;
    try {
      retrieved = mDocumentStore.database().read(key);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return retrieved;
  }

  private void loadCloudantProperties() {
    Properties props = new Properties();
    AssetManager am = this.context.getAssets();
    try {
      String cloudantPropsFile = "cloudant.properties";
      InputStream mappingStream = am.open(cloudantPropsFile);
      props.load(mappingStream);
      this.dataStoreName = props.getProperty("DATASTORE_NAME");
      this.dataStoreManagerDirectory = props.getProperty("DATASTORE_MANAGER_DIR");

      this.apiKey = props.getProperty("apiKey");
      this.apiPassword = props.getProperty("apiSecret");
      this.databaseName = props.getProperty("dbName");
      this.host = props.getProperty("host");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public int getDocumentCount() {
    int number = 0;
    try {
      number = this.mDocumentStore.database().getDocumentCount();
    } catch (DocumentStoreException e) {
      e.printStackTrace();
    }
    return number;
  }


  public void pushData() {

//
//    if (mPushReplicator.getState() == Replicator.State.COMPLETE
//        || mPushReplicator.getState() == Replicator.State.PENDING
//        || mPushReplicator.getState() == Replicator.State.STOPPED
//        || mPushReplicator.getState() == Replicator.State.ERROR) {
//      try {
//
//
//        mPushReplicator.stop();
//
//        CountDownLatch latch =new CountDownLatch(1);
//        Listener listener=new Listener(latch, CloudantStore.this, schoolCensus);
//        mPushReplicator.getEventBus()
//            .register(listener);
//
//
////        mPushReplicator.start();
//
//
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//
//    }

  }


//  public void pullDataFilter(final Replicator.Filter filter) {
//
//
//    if (Utils.networkAvailable(context)) {
//
//      PullReplication pull = new PullReplication();
//      pull.source = uri;
//      pull.target = mDatastore;
//      pull.filter = filter;
//      mPullReplicator = ReplicatorFactory.oneway(pull);
//
//      if (mPullReplicator.getState() == Replicator.State.COMPLETE
//          || mPullReplicator.getState() == Replicator.State.PENDING) {
//        try {
//          mPullReplicator.getEventBus()
//              .register(new Listener(new CountDownLatch(1), CloudantStore.this, schoolCensus));
//          mPullReplicator.start();
//
//
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//
//      }
//    }
//  }


  public void pullData() {

//    if (Utils.networkAvailable(context)) {
//
//
//
//
//      if (mPullReplicator.getState() == Replicator.State.COMPLETE
//          || mPullReplicator.getState() == Replicator.State.PENDING) {
//        try {
//          mPullReplicator.getEventBus()
//              .register(new Listener(new CountDownLatch(1), CloudantStore.this, schoolCensus));
//          mPullReplicator.start();
//
//
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//
//      }
//    }
  }

  public void deleteLocalDatastore() {

    try {
      mDocumentStore.delete();
      createDataStore();
    } catch (Exception ee) {
      ee.printStackTrace();
    }

  }


  public void setContext(Context context) {
    this.context = context;
  }

  public void setSchoolCensus(SchoolCensus schoolCensus) {
    this.schoolCensus = schoolCensus;
  }



  //
  // REPLICATIONLISTENER IMPLEMENTATION
  //

  /**
   * Calls the TodoActivity's replicationComplete method on the main thread, as the complete()
   * callback will probably come from a replicator worker thread.
   */
  @Subscribe
  public void complete(ReplicationCompleted rc) {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        Log.i(Constants.TAG, "Replication ReplicationCompleted:");
      }
    });
  }

  /**
   * Calls the TodoActivity's replicationComplete method on the main thread, as the error() callback
   * will probably come from a replicator worker thread.
   */
  @Subscribe
  public void error(ReplicationErrored re) {
    Log.e(Constants.TAG, "Replication error:", re.errorInfo);
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        Log.e(Constants.TAG, "Replication error:");

      }
    });
  }


}
