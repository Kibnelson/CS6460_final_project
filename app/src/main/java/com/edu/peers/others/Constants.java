package com.edu.peers.others;

/**
 * Created by nelson on 3/16/15.
 */

public class Constants {

  public static final int USER_REGISTRATION_VIEW = 5001;
  public static final String STUDENT_ROLE = "Student";
  public static final String INSTRUCTOR_ROLE = "Instructor";



  public static final String PUBLIC = "PUBLIC";
  public static final String PRIVATE = "PRIVATE";

  // Categories for user statistics
  public static final String QUIZ_CATEGORY = "QUIZ_CATEGORY";
  public static final String QUESTIONS_CATEGORY = "QUESTIONS_CATEGORY";
  public static final String CONTENT_CATEGORY = "CONTENT_CATEGORY";
  public static final String COMMENTS_CATEGORY = "COMMENTS_CATEGORY";
  public static final String QUESTIONS_RESPONSE_CATEGORY = "QUESTIONS_RESPONSE_CATEGORY";

  public static final String QUESTIONS = "QUESTIONS";

  public static final String NOTIFICATIONS = "NOTIFICATIONS";
  public static final String CONTENT_FILES = "efa41919-6efb-4d3a-9ebd-84c85f81b47f1";
  public static final String CONTENT_FILES_COMMENTS = "efa41919-6efb-4d3a-9ebd-84c85f81b47f2";
  public static final String QUESTIONS_COMMENTS = "efa41919-6efb-4d3a-9ebd-84c85f81b47f=3";



  public static final int QUIZ_LIST_VIEW = 6001;
  public static final int QUIZ_LIST_VIEW_CONTENT = 6002;

  ////////////////////////////

  public static final int NON = -1;


  public static final int LOGIN_REQUEST = 101;

  public static final int UPDATE_USERS = 102;

  public static final int UPDATE_SCHOOLS = 103;

  public static final int UPDATE_STUDENT = 104;
  public static final int ADD_STUDENT = 105;
  public static final int PULL_STUDENT = 106;
  public static final int ADD_ATTENDANCE = 107;
  public static final int DEMO_REGISTER_STUDENT = 108;
  public static final int DEMO_UPDATE_STUDENT = 109;

  public static final int UPDATE_ON_DOC_STARTED = 991;
  public static final int UPDATE_ON_DOC_DONE = 992;
  public static final int UPDATE_ON_DOC_NOT_DONE = 993;
  public static final int SCHOOL_CHANGED = 201;
  public static final int STUDENT_CHANGED = 202;
  public static final int USER_CHANGED = 203;

  public static final int SCHOOL_DATA = 1;
  public static final int TEACHER_DATA = 2;
  public static final int USERS_DATA = 3;
  public static final int CLASS_ROOM_DATA = 4;
  public static final int STUDENT_DATA = 5;
  public static final int SCHOOL_DATA_LIST = 6;
  public static final int TEACHER_DATA_LIST = 7;
  public static final int ATTENDANCE_DATA_LIST = 8;
  public static final int STUDENT_SUMMARY_VIEW = 9;
  public static final int STUDENT_GRID_VIEW = 10;
  public static final int STUDENT_REPORT_VIEW = 11;
  public static final int USERS_SERVER_DATA = 101;

  public static final int SCHOOLS_SERVER_DATA = 102;
  public static final int SCHOOLS_PULL_COMMENTS = 103;

  public static final int USER_PROFILE = 204;
  public static final int SCHOOL_DATA_PROFILE = 205;

  public static final int REFRESH_MAP_DATA = 206;

  public static final int DATE_PICKER_STUDENT_REGISTRATION_VIEW = 301;
  public static final int DATE_PICKER_STUDENT_UPDATE_VIEW = 302;
  public static final int DATE_PICKER_MEDICATION_LIST_VIEW = 303;

  public static final int BLOCK_CHAIN_TOKEN=401;
  public static final int BLOCK_CHAIN_REGISTER=402;
  public static final int BLOCK_CHAIN_TRANSFER=403;



  // public static final String SERVER = "http://derp-api-v2.mybluemix.net/api/";
  //  public static final String SERVER = "http://derp-api-v02.mybluemix.net/api/";

//  public static final String SERVER1 = "http://derp-api-v01.mybluemix.net/api/";
//  public static final String SERVER = " http://derp-api-v01v.mybluemix.net/api/";


  // public static final String SERVER = "http://derp-api-v2.mybluemix.net/api/";
  //  public static final String SERVER = "http://derp-api-v02.mybluemix.net/api/";

  public static final String SERVER = "http://derp-api-v01.mybluemix.net/api/";
  public static final String SERVER_SCHOOL_DEMO = "http://kedn-api.mybluemix.net/api/";


  public static final String SERVER_STUDENT_REGISTER = "students/register/";
  public static final String SERVER_STUDENT_UPDATE = "students/update";
  public static final String SERVER_LOGIN = "user/login";
  public static final String SCHOOL_USER_PROFILE = "user/users";
  public static final String SCHOOL_PROFILE = "school/schools/";

  public static final String SERVER_GET_USERS = "accounts/login";
  public static final String SERVER_UPDATE_USERS = "accounts/updateUsers";
  public static final String SERVER_UPDATE_SCHOOL = "school/updateSchool";
  public static final String SERVER_UPDATE_STUDENT = "updateStudent";
  public static final String SERVER_ADD_STUDENT = "enrolStudentOne";
  public static final String SERVER_PULL_STUDENT = "getStudents";
  public static final String SERVER_ADD_ATTENDANCE = "addAttendance";
  public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
  public static final String SERVER_PULL_COMMENTS = "getCommentsData";
  public static final String CAMERA_MESSAGE = "Sorry, your device hardware does not support camera" ;
//  public static final String BLOCK_CHAIN = "http://192.168.43.50:3000/chain";
  public static final String BLOCK_CHAIN = "http://192.168.43.50:3000/chaincode";
  public static final String QUIZ = "QUIZ";
  public static final String GRADEBOOK = "GRADEBOOK";
//  public static final String BLOCK_CHAIN = "http://10.0.1.4:3000/chaincode";

  // Cloudant
  public static String SIGNIN_KEY = "SIGN_IN_DATA";
  public static String ENVIRONMENT_KEY = "ENVIRONMENT_PHOTOS_DATA";
  public static String UPDATE_DOC_KEY = "UPDATE_DOC_KEY";
  public static String UPDATED = "UPDATED";
  public static String CHANGED = "CHANGED";
  public static String OPENED = "OPENED";
  public static String CLOSED = "CLOSED";
  // MODELS
  public static String USERS = "USERS";
  public static String SCHOOL = "SCHOOL";
  public static String LOCATION = "LOCATION";
  public static String LOGGED_IN_USER = "LOGGED_IN_USER";
  public static String COUNTY = "COUNTY";
  public static String SUB_COUNTY = "SUB_COUNTY";
  public static String CONSTITUENCY = "CONSTITUENCY";
  public static String WARD = "WARD";
  public static String ZONE = "ZONE";
  public static String TAG = "SchoolCensus";
  public static String PREFS_NAME = "SchoolCensusPersistentFile";
  public static String PREFS_KEY = "SchoolCensusPersistentData";

  public static String Enrolment_Role = "Enrolment";
  public static String Attendance_Role = "Attendance";
  public static String EMIS_Role = "EMIS";
  public static String Pictures_Role = "Picture";
  public static String Approver_Role = "Approver";
  public static String Reports_Role = "Reports";


  public static String PRIVILEGE_ADD = "Add";
  public static String PRIVILEGE_DELETE = "Delete";
  public static String PRIVILEGE_APPROVE = "Approve";
  public static String PRIVILEGE_UPDATE = "Update";
  public static String PRIVILEGE_VIEW = "View";

  public static String Attendance = "Attendance";
  public static String Enrolment = "Enrolment";
  public static String EMIS = "EMIS";
  public static String ImageDataCapture = "Pictures";
  public static String ChangePassowrd = "Change Password";
  public static String Toilets="Toilets";
  public static String Water="Water";
  public static String Electricity="Electricity";
  public static String EMISForm="EMISForm";
  public static String Classroom="Classroom";
  public static String Others="Others";

  public static String Pictures="Pictures";

  public static String FileName="FileName.png";

  public static String SchoolHomeGridView="SchoolHomeGridView";

  public static String ImageDataCaptures="ImageDataCapture";

  public static String ImageDataCaptureGrid="ImageDataCaptureGrid";

  public static String StudentGridView="StudentGridView";
  public static String StudentSummaryView="StudentSummaryView";
  public static String StudentRegistrationView="StudentRegistrationView";
  public static String StudentAttendanceWizardView="StudentAttendanceWizardView";
  public static String StudentReportsView="StudentReportsView";
  public static String emis="com.derp.emis";

  public static String StudentProfileView="StudentProfileView";
  public static String StudentRegistrationWizardView="StudentRegistrationWizardView";
  public static String StudentWizardWizardView="StudentWizardWizardView";
  public static String StudentsGradeHomeGridView="StudentsGradeHomeGridView";
  public static String StudentTransferWizardView="StudentTransferWizardView";

  public static String StudentsHomeGridView="StudentsHomeGridView";
  public static String Error="There was an error processing your request, please try again.";





  // Transactions Options (Heading)
  public static String Transfer="Transfer"; //  InTransfer
  public static String Registration="Registration"; //  --> Registered
  public static String Promotion="Promotion";
  public static String Enrollment="Enrollment"; // --> Active/Enrolled
  public static String Pending= "Pending";
  public static String Update= "Update";


//  public static String Completed="Completed";
//  public static String Droppedout="Dropped out";




  // States
  public static String Registration_State="Registered"; //  InTransfer
  public static String Transfer_State="InTransfer"; //  --> Registered
  public static String Promotion_State="Promotion";
  public static String Enrollment_State="Enrolled"; // --> Active/Enrolled
  public static String Pending_State= "Pending";
  public static String Update_State= "Update";



  //View
  public static String StudentsSummaryView="StudentsSummaryView";
  public static String InstructorsSummaryView="InstructorsSummaryView";
  public static String QuizListViewContent="QuizListViewContent";
  public static String QuizListView="QuizListView";
  public static String QuestionsListViewContent="QuestionsListViewContent";
  public static String QuestionsListView="QuestionListView";
  public static String LearningContentListView="LearningContentListView";
  public static float STATISTICS=10;
}
