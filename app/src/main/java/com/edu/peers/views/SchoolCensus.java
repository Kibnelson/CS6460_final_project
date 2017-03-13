package com.edu.peers.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.edu.peers.cloudant.CloudantStore;
import com.edu.peers.cloudant.DataAggregator;
import com.edu.peers.dialogs.CustomAlertDialogBuilder;
import com.edu.peers.models.ContentFile;
import com.edu.peers.models.Questions;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.network.VolleyManager;
import com.edu.peers.others.Constants;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by nelson on 3/16/15.
 */


public class SchoolCensus extends MultiDexApplication {

  private String currentDialogTitle;
  private String currentDialogMsg;
  private Activity currentActivity;
  private ProgressDialog nDialog;
  private double latitude;
  private double longitude;
  private int state;
  private Fragment fragmentActivity;
  private RequestQueue mRequestQueue;
  private MainView mainView;
  private JSONObject commonParams, payloadObject;
  private LoginView loginView;
  private ProgressDialog progressDialog;
  private int selectedSchoolIndex;
  private int quizIndex;
  private int selectedUserIndex;
  private int selectedClassroomIndex;
  private int selectedClassroomStreamIndex;
  private CustomAlertDialogBuilder customAlertDialogBuilder;
  private DataAggregator dataAggregator;
  private String currentTitle;
  private String userName;
  private String password;
  private Quiz quiz;
  private Questions questions;
  private String questionsUUID;
  private Questions question;
  private ContentFile contentFile;
  private String contentUUID;
  private String contentFileUUID;
  private String questionState;
  private User userQiuz=null;
  private int currentYear;


  private UserObject userObject;
  private VolleyManager volleyManager;

  public SchoolCensus() {
  }


  @Override
  public void onCreate() {
    super.onCreate();
  }

  public void initHome() {
    dataAggregator = new DataAggregator(this, this);

    volleyManager = new VolleyManager(getApplicationContext(), this);

  }

  public Activity getCurrentActivity() {
    return currentActivity;
  }

  public void setCurrentActivity(Activity activity) {
    currentActivity = activity;
  }

  public Fragment getCurrentFragment() {
    return fragmentActivity;
  }

  public void setCurrentFragment(Fragment activity) {

    fragmentActivity = activity;
  }

  public void showDialog(int dialogId) {
    currentActivity.showDialog(dialogId);
  }

  public String getCurrentDialogTitle() {
    return this.currentDialogTitle;
  }

  public void setCurrentDialogTitle(String currentDialogTitle) { // 5
    this.currentDialogTitle = currentDialogTitle;
  }

  public String getCurrentDialogMsg() {
    return this.currentDialogMsg;
  }

  public void setCurrentDialogMsg(String currentDialogMsg) { // 5
    this.currentDialogMsg = currentDialogMsg;
  }


  public void showProgress(Activity context, String title, String message) {
    nDialog =
        new ProgressDialog(
            context); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
    nDialog.setMessage(message);
    nDialog.setTitle(title);
    nDialog.setIndeterminate(false);
    nDialog.setCancelable(true);
    nDialog.show();
  }

  public void dismissProgressDialog() {
    if (nDialog != null) {
      nDialog.dismiss();
    }

  }


  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }


  public Dialog showAnimatedDialog(Context context, String message) {
    return ProgressDialog.show(context, "", message, true);
  }


  RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }
    return mRequestQueue;
  }

  public JSONObject getPayloadObject() {
    return payloadObject;
  }

  public String getPayload(String name) {
    try {
      return payloadObject.getString(name);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      Log.d(Constants.TAG, "jsonError", e);
    }
    return "";
  }

  public void putPayload(String name, String value) {
    if (payloadObject == null) {
      payloadObject = new JSONObject();
    }
    try {
      payloadObject.put(name, value);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      Log.d(Constants.TAG, "jsonError", e);
    }
  }

  public void clearPayloadObject() {
    payloadObject = new JSONObject();
  }

  public LoginView getLoginView() {
    return loginView;
  }

  public void setLoginView(LoginView loginView) {
    this.loginView = loginView;
  }


  public ProgressDialog showProgessDialog(Context context, String data) {

    progressDialog = new ProgressDialog(context);
    progressDialog.setMessage(data);
    progressDialog.show();
    return progressDialog;

  }


  public int getSelectedSchoolIndex() {
    return selectedSchoolIndex;
  }

  public void setSelectedSchoolIndex(int selectedSchoolIndex) {
    this.selectedSchoolIndex = selectedSchoolIndex;
  }

  public int getSelectedUserIndex() {
    return selectedUserIndex;
  }

  public void setSelectedUserIndex(int selectedUserIndex) {
    this.selectedUserIndex = selectedUserIndex;
  }

  public int getSelectedClassroomStreamIndex() {
    return selectedClassroomStreamIndex;
  }

  public void setSelectedClassroomStreamIndex(int selectedClassroomStreamIndex) {
    this.selectedClassroomStreamIndex = selectedClassroomStreamIndex;
  }

  public int getSelectedClassroomIndex() {
    return selectedClassroomIndex;
  }

  public void setSelectedClassroomIndex(int selectedClassroomIndex) {
    this.selectedClassroomIndex = selectedClassroomIndex;
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  public void showToast(Context context, String message) {

    Toast.makeText(context, message,
                   Toast.LENGTH_LONG).show();

  }

  public void showMessageDialogAlert(FragmentActivity activity, String message, String title) {

    customAlertDialogBuilder = new CustomAlertDialogBuilder(activity);
    customAlertDialogBuilder.setTitle(title);
    customAlertDialogBuilder.setMessage(message);
    customAlertDialogBuilder.show();//.getWindow().setLayout(300,300);
  }


  public CloudantStore getCloudantInstance() {
    if (dataAggregator == null) {
      initHome();
    }
//    initHome();
    return dataAggregator.getCloudantInstance();
  }



  public String getCurrentTitle() {
    return  currentTitle;
  }

  public void setCurrentTitle(String CurrentTitle) {
    currentTitle = CurrentTitle;
  }

  public String getUsername() {
    return userName.trim();
  }

  public void setUsername(String id) {
    userName=id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserObject getUserObject() {
    if (userObject==null)
      userObject= new UserObject();
    return userObject;
  }

  public void setUserObject(UserObject userObject) {
    this.userObject = userObject;
  }

  public MainView getMainView() {
    return mainView;
  }

  public void setMainView(MainView mainView) {
    this.mainView = mainView;
  }

  public Questions getQuestion() {
    return question;
  }

  public void setQuestion(Questions question) {
    this.question = question;
  }

  public Quiz getQuiz() {
    return quiz;
  }

  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }

  public Questions getQuestions() {
    return questions;
  }

  public void setQuestions(Questions questions) {
    this.questions = questions;
  }

  public ContentFile getContentFile() {
    return contentFile;
  }

  public void setContentFile(ContentFile contentFile) {
    this.contentFile = contentFile;
  }

  public String getContentUUID() {
    return contentUUID;
  }

  public void setContentUUID(String contentUUID) {
    this.contentUUID = contentUUID;
  }

  public String getContentFileUUID() {
    return contentFileUUID;
  }

  public void setContentFileUUID(String contentFileUUID) {
    this.contentFileUUID = contentFileUUID;
  }

  public String getQuestionsUUID() {
    return questionsUUID;
  }

  public void setQuestionsUUID(String questionsUUID) {
    this.questionsUUID = questionsUUID;
  }

  public void getUserWithUsername(String s) {
  }

  public String getQuestionState() {
    return questionState;
  }

  public void setQuestionState(String questionState) {
    this.questionState = questionState;
  }

  public int getQuizIndex() {
    return quizIndex;
  }

  public void setQuizIndex(int quizIndex) {
    this.quizIndex = quizIndex;
  }

  public User getUserQiuz() {
    return userQiuz;
  }

  public void setUserQiuz(User userQiuz) {
    this.userQiuz = userQiuz;
  }
}


