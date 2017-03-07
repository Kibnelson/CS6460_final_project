package com.edu.peers.cloudant;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;

import com.cloudant.sync.datastore.BasicDocumentRevision;
import com.cloudant.sync.datastore.Datastore;
import com.cloudant.sync.datastore.DatastoreManager;
import com.cloudant.sync.datastore.DocumentBody;
import com.cloudant.sync.datastore.MutableDocumentRevision;
import com.cloudant.sync.datastore.UnsavedStreamAttachment;
import com.cloudant.sync.query.IndexManager;
import com.cloudant.sync.replication.Replication;
import com.edu.peers.managers.PersistenDataManager;
import com.edu.peers.others.Constants;
import com.edu.peers.views.SchoolCensus;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static java.util.UUID.randomUUID;


/**
 * Created by nelson on 1/28/15.
 */

public class CloudantStore implements CloudantSaveListener {

  private IndexManager genericIndexManager;
  private String dataStoreName,environementDataStoreName;
  private String dataStoreManagerDirectory,environementDataStoreNameDirectory;
  private String databaseName;
  private String apiKey;
  private String apiPassword;
  private String host;
//  private Replicator mPushReplicator;
  private SchoolCensus schoolCensus;
  private DatastoreManager manager,managerEnvironment;
  private Context context;
  private Datastore mDatastore;
  private Datastore environementDataStore;
  private Handler mHandler;
  private URI uri;
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

//    if (this.mPushReplicator != null) {
//      this.mPushReplicator.stop();
//    }
//
//    if (this.mPullReplicator != null) {
//
//      this.mPullReplicator.stop();
//
//
//    }

  }

  void createDataStore() {
    File path = context.getDir(
        dataStoreManagerDirectory,
        Context.MODE_PRIVATE
    );

    File pathEnvironment = context.getDir(
        environementDataStoreNameDirectory,
        Context.MODE_PRIVATE
    );



    manager = new DatastoreManager(path.getAbsolutePath());
    managerEnvironment = new DatastoreManager(pathEnvironment.getAbsolutePath());

    try {
      replicationSettings();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

  }


  void replicationSettings()
      throws URISyntaxException {

    this.stopAllReplications();
    uri = this.createServerURI();
    try {
      this.mDatastore = manager.openDatastore(dataStoreName);
      this.environementDataStore = manager.openDatastore(environementDataStoreName);

//      mPushReplicator = ReplicatorBuilder.push().from(environementDataStore).to(uri).build();


//      mPushReplicator.start();


//      mPullReplicator = ReplicatorBuilder.pull().from(uri).to(environementDataStore).build();
//
//      mPullReplicator.start();

      // Test
      genericIndexManager = new IndexManager(mDatastore);

      if (genericIndexManager.listIndexes().containsKey("basic")) {
        genericIndexManager.deleteIndexNamed("basic");
      }

      List<Object> fieldValues = Arrays.<Object>asList();

      genericIndexManager.ensureIndexed(fieldValues,
                                        "basic");


    } catch (Exception e) {
      e.printStackTrace();
    }

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



  public IndexManager getIndexManager() {
    return new IndexManager(mDatastore);
  }

  private URI createServerURI()
      throws URISyntaxException {

    String url = "https://" + apiKey + ":" + apiPassword + "@" + host + "/" + databaseName;

    return new URI(url);
  }

  public synchronized void deleteDocumentToDataSTore(String docId) {

    try {
      BasicDocumentRevision retrieved = mDatastore.getDocument(docId);

      mDatastore.deleteDocumentFromRevision(retrieved);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public synchronized void updateDocumentToDataSTore(JSONObject data, String docId) {



    try {

      BasicDocumentRevision retrieved = mDatastore.getDocument(docId);

      MutableDocumentRevision update = retrieved.mutableCopy();

      update.body = new EventDocument(data);

//      mDatastore.deleteDocumentFromRevision(retrieved);
//      mDatastore.createDocumentFromRevision(update);
      mDatastore.updateDocumentFromRevision(update);

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
  public boolean containsDocumentKey(String key) {
    return mDatastore.containsDocument(key);
  }

  public synchronized void addDocument(JSONObject data, String key) {

    try {
      if (mDatastore.containsDocument(key)) {
        // get doc from store using the key
        updateDocumentToDataSTore(data, key);

      } else {
        // No doc added to cloudant add a new one and store the key to shared preference

        storeDocument(data, key);
        updateDocumentToDataSTore(data, key);


      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String getDocumentId() {
    PersistenDataManager persistenDataManager = new PersistenDataManager(context);

    String key = persistenDataManager.getPersistentData(Constants.PREFS_KEY);
    return key;
  }

  public BasicDocumentRevision storeDocument(JSONObject data, String uuid) {
    BasicDocumentRevision basicDocumentRevision = null;
    try {

      MutableDocumentRevision revision = new MutableDocumentRevision();

      revision.docId = uuid.toString();
      revision.body = new EventDocument(data);

      basicDocumentRevision = mDatastore.createDocumentFromRevision(revision);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return basicDocumentRevision;
  }

  public BasicDocumentRevision readDocument() {
    BasicDocumentRevision basicDocumentRevision = null;
    try {
      PersistenDataManager persistenDataManager = new PersistenDataManager(context);

      String key = persistenDataManager.getPersistentData(Constants.PREFS_KEY);

      if (key != null) {
        // get doc from store using the key
        basicDocumentRevision = mDatastore.getDocument(key);
      } else {

        UUID uuid = randomUUID();

        //  basicDocumentRevision = openDocument(uuid.toString());

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return basicDocumentRevision;
  }

  public BasicDocumentRevision getDocument(String key) {
    BasicDocumentRevision basicDocumentRevision = null;
    try {

      if (key != null) {
        // get doc from store using the key
        basicDocumentRevision = mDatastore.getDocument(key);
      } else {

        UUID uuid = randomUUID();

        basicDocumentRevision = mDatastore.getDocument(uuid.toString());

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return basicDocumentRevision;
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

      this.environementDataStoreName= props.getProperty("DATASTORE_NAME_ENVIRONMENT");
      this.environementDataStoreNameDirectory = props.getProperty("DATASTORE_MANAGER_DIR_ENVIRONMENT");
      this.apiKey = props.getProperty("apiKey");
      this.apiPassword = props.getProperty("apiSecret");
      this.databaseName = props.getProperty("dbName");
      this.host = props.getProperty("host");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public int getDocumentCount() {
    int number = this.mDatastore.getDocumentCount();
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






  public void pullDataFilter(final Replication.Filter filter) {




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
  }



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
      manager.deleteDatastore(mDatastore.getDatastoreName());
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

  public IndexManager getGenericIndexManager() {
    return genericIndexManager;
  }


  public synchronized void addDocumentToLocalDataSTore(JSONObject data, byte[] imageData) {

    try {
      DocumentBody body = new EventDocument(data);
      MutableDocumentRevision revision = new MutableDocumentRevision();
      revision.body = body;
//      UnsavedFileAttachment att1 = new UnsavedFileAttachment(filtere,
//                                                             "image/png");

      UnsavedStreamAttachment att1 = new UnsavedStreamAttachment(
          new ByteArrayInputStream(imageData), Constants.FileName, "image/jpeg");

      revision.attachments.put(att1.name, att1);

      this.environementDataStore.createDocumentFromRevision(revision);

      pushData();


    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public synchronized void addDocumentToLocalDataSTore(JSONObject data) {

    try {
      DocumentBody body = new EventDocument(data);
      MutableDocumentRevision revision = new MutableDocumentRevision();
      revision.body = body;

      this.environementDataStore.createDocumentFromRevision(revision);

      pushData();


    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public synchronized  List<BasicDocumentRevision> getAllDocuments() {

    int pageSize = environementDataStore.getDocumentCount();

    return  this.environementDataStore.getAllDocuments(0, pageSize, true);

  }





}
