package com.edu.peers.dialogs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.adapter.QuestionsListViewAdapter;
import com.edu.peers.managers.NotificationManager;
import com.edu.peers.managers.UserManager;
import com.edu.peers.models.NotificationObject;
import com.edu.peers.models.Notifications;
import com.edu.peers.models.Questions;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.models.UserStatistics;
import com.edu.peers.others.Base64;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Utils;
import com.edu.peers.views.QuizListView;
import com.edu.peers.views.SchoolCensus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by nelson on 3/16/15.
 */

public class QuizCreationDialogView extends DialogFragment
    implements View.OnClickListener, Camera.PictureCallback, AdapterView.OnItemClickListener {

  private static final int MESSAGE_SHOW_MSG = 1;
  private static final int MESSAGE_SHOW_IMAGE = 2;
  private static final int MESSAGE_ENROLL_FINGER = 3;
  private static Bitmap mBitmapFP = null;
  TextView message;
  ComboBoxViewListItem[] schoolArray;
  Activity activity;
  private Button cancelButton;
  private TextView title;
  private EditText firstName;
  private EditText lastName;
  private ComboBoxViewListItem[] classArray;
  private SchoolCensus schoolCensus;
  private String fingerPrintImageStringData;
  private ImageView imageView;
  private int mStackLevel;
  private Bitmap bitmapImage = null;
  private View view;
  private CameraViewDialog cameraViewDialog;
  private String bitmapImageString;
  private TextView mTxtMessage;
  private int mPendingOperation = 0;
  private Camera.Parameters parameters;
  private Object m_OperationObj;
  private ImageView fingerPrintImage;
  private final Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {

      switch (msg.what) {
        case MESSAGE_SHOW_MSG:
          String showMsg = (String) msg.obj;
          mTxtMessage.setText(showMsg);
          break;
        case MESSAGE_SHOW_IMAGE:

          fingerPrintImage.setImageBitmap(mBitmapFP);

          break;
        case MESSAGE_ENROLL_FINGER:
          StartEnrollWithUsername();
          break;


      }
    }
  };
  private int fingerSelected = 0;
  private Spinner spinnerStreams;
  private ComboBoxViewAdapter comboBoxViewAdapterStreams;
  private ComboBoxViewListItem[] classStreams;
  private Button addButton;
  private boolean classSelected = false;
  private String selectedStream;
  private EditText studentNumber;
  private String selectedClass, selectedClassName, selectedClassRevId, genderValue;
  private RadioButton male, female;
  private String firstNameStr;
  private String lastNameStr;
  private String studentNumberStr;
  private ComboBoxViewAdapter comboBoxViewAdapter;
  private View.OnTouchListener onClassesTouchListener = new View.OnTouchListener() {


    @Override
    public boolean onTouch(View v, MotionEvent event) {

      classSelected = true;

      ((ComboBoxViewAdapter) comboBoxViewAdapter).setSelected(true);

      ((ComboBoxViewAdapter) comboBoxViewAdapter).notifyDataSetChanged();
      return false;
    }
  };
  private AdapterView spinner;
  private View.OnTouchListener onStreamsTouchListener = new View.OnTouchListener() {

    @Override
    public boolean onTouch(View v, MotionEvent event) {

      ((ComboBoxViewAdapter) comboBoxViewAdapterStreams).setSelected(true);

      ((ComboBoxViewAdapter) comboBoxViewAdapterStreams).notifyDataSetChanged();
      return false;
    }
  };
  private EditText qName;
  private Spinner subjects;
  private EditText duration;
  private EditText instructions;
  private Button questions;
  private String selectedSubject;
  private String qNameStr;
  private String durationStr;
  private String instructionsStr;
  private ListView questions_list;
  private List<Questions> data = new ArrayList<>();
  private List<Questions> questionsArray = new ArrayList<>();
  private QuizListView quizListView;
  private Button saveButton;
  private UserObject userObject;
  private UserManager userManager;
  private ProgressDialog progressDialog;
  private Quiz quiz;
  private NotificationManager notificationManager;
  private NotificationObject notificationObject;

  public QuizCreationDialogView() {
    // Required empty public constructor
  }

  public static QuizCreationDialogView newInstance(int num) {
    QuizCreationDialogView f = new QuizCreationDialogView();

    // Supply num input as an argument.
    Bundle args = new Bundle();
    args.putInt("num", num);
    f.setArguments(args);

    return f;
  }

  private static Bitmap rotate(Bitmap bitmap, int degree) {
    int w = bitmap.getWidth();
    int h = bitmap.getHeight();

    Matrix mtx = new Matrix();
    mtx.postRotate(degree);

    return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
//    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    getDialog().setTitle("Create Quiz");
    view = inflater.inflate(R.layout.quiz_creation_dialog, null);

    return view;

  }

  @Override
  public void onStart() {
    super.onStart();

    this.getDialog().getWindow()
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    genderValue = null;

    schoolCensus = (SchoolCensus) getActivity().getApplication();

    userObject = schoolCensus.getUserObject();
    notificationManager = new NotificationManager(schoolCensus.getCloudantInstance(), getContext());
    notificationObject = notificationManager.getNotificationObject();
    if (notificationObject == null) {
      notificationObject = new NotificationObject();
    }

    userManager = new UserManager(schoolCensus.getCloudantInstance(), getContext());
    quizListView = (QuizListView) schoolCensus.getCurrentFragment();

    qName = (EditText) view.findViewById(R.id.qName);

    subjects = (Spinner) view.findViewById(R.id.subjects);
    subjects.setOnItemSelectedListener(new OnClassSelected());

    duration = (EditText) view.findViewById(R.id.duration);
    instructions = (EditText) view.findViewById(R.id.instructions);

    questions = (Button) view.findViewById(R.id.questions);
    questions.setOnClickListener(this);

    questions_list = (ListView) view.findViewById(R.id.questions_list);
    questions_list.setOnItemClickListener(this);
//    questions_list.setAdapter(new QuizListViewAdapter(this, sampleData));

    registerForContextMenu(questions_list);

    questions_list.setOnItemClickListener(this);
    cancelButton = (Button) view.findViewById(R.id.cancel);
    cancelButton.setOnClickListener(this);

    saveButton = (Button) view.findViewById(R.id.saveButton);
    saveButton.setOnClickListener(this);

    try {

      classArray = new ComboBoxViewListItem[2];

      classArray[0] =
          new ComboBoxViewListItem("0", "Maths", "Maths");
      classArray[1] =
          new ComboBoxViewListItem("1", "Science", "Science");

      comboBoxViewAdapter = new ComboBoxViewAdapter(getActivity(),
                                                    android.R.layout.simple_spinner_item,
                                                    classArray);

      subjects.setOnTouchListener(onClassesTouchListener);

      comboBoxViewAdapter.setHint("Select Subject");
      subjects.setAdapter(comboBoxViewAdapter);

//      if (classArray.length > 0) {
//        reloadStreamsData(classArray[0].getText());
//      }

    } catch (Exception ex) {
      ex.printStackTrace();

    }


  }


  @Override
  public void onClick(View v) {
    /** When OK Button is clicked, dismiss the school_calendar_view_dialog */
    if (v == saveButton) {
      qNameStr = qName.getText().toString();
      durationStr = duration.getText().toString();
      instructionsStr = instructions.getText().toString();

      boolean valid = true;
      StringBuilder errorBuffer = new StringBuilder();
      if (qNameStr == null || qNameStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter quiz name\n");
      }
      if (durationStr == null || durationStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter duration\n");
      }

      if (instructionsStr == null || instructionsStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter quiz instructions\n");
      }

      if (classSelected && selectedSubject.length() == 0) {
        valid = false;
        errorBuffer.append("Please select subject\n");
      }

      if (questionsArray.size() == 0) {
        valid = false;
        errorBuffer.append("Please add one or more question\n");
      }

      if (valid) {

        quiz =
            new Quiz(qNameStr, selectedSubject, durationStr, instructionsStr,
                     Utils.getCurrentDate(), questionsArray, userObject.getUser());

        User user = userObject.getUser();

        List<User> userList = userObject.getUserList();
        int size = userList.size();
        for (int y = 0; y < size; y++) {

          User user1 = userList.get(y);

          if (user.getUsername().equalsIgnoreCase(user1.getUsername())) {
            List<UserStatistics> quizzesCreated = user1.getQuizzesCreated();
            quizzesCreated.add(new UserStatistics(Utils.getCurrentDate(), Utils.generateNumber(),
                                                  UUID.randomUUID().toString(),
                                                  Constants.QUIZ_CATEGORY));
            user1.setQuizzesCreated(quizzesCreated);
            userList.set(y, user1);
          }
        }
        userObject.setUserList(userList);
        notificationObject.getNotificationsList().add(new Notifications(
            "New quiz posted by:" + userObject.getUser().getFirstName() + " " + userObject.getUser()
                .getLastName()));

        new backgroundProcessSave().execute();
      } else {
        Toast.makeText(getActivity(), errorBuffer.toString(), Toast.LENGTH_LONG).show();

        return;
      }

    } else if (v == cancelButton) {

      dismiss();
    } else if (v == questions) {
      openDialog();

    } else if (v == fingerPrintImage) {
      fingerSelected = 2;
      startFingerprintScanning();
    } else if (v == male) {
      genderValue = "Male";
    } else if (v == female) {
      genderValue = "Female";
    }


  }

  private void completeQuizCreation() {
    quizListView.addQuiz(quiz);
    dismiss();
  }

  private ProgressDialog showProgessDialog() {

    progressDialog = new ProgressDialog(getContext());
    progressDialog.setMessage("Please wait");
    progressDialog.show();
    return progressDialog;


  }

  private void hideProgessDialog() {

    progressDialog.dismiss();
    progressDialog.hide();

  }


  private class backgroundProcessSave extends AsyncTask<Quiz, Quiz, Long> {

    protected Long doInBackground(Quiz... params) {

      try {

        userManager.addDocument(userObject, Constants.USERS);
        notificationManager.addNotification(notificationObject);

      } catch (Exception e) {
        e.printStackTrace();
      }

      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {
      hideProgessDialog();

      completeQuizCreation();
    }


    protected void onPreExecute() {
      showProgessDialog();
    }

  }

  public void addQuestion(Questions question) {
    questionsArray.add(question);

    data.add(question);
    questions_list.setAdapter(new QuestionsListViewAdapter(this, data));
    questions_list.setOnItemClickListener(this);
  }

  void openDialog() {
    schoolCensus.setQuestion(null);
    schoolCensus.setState(Constants.STUDENT_DATA);

    schoolCensus.setCurrentActivity(getActivity());
    final QuestionCreationDialogView
        newFragment = QuestionCreationDialogView.newInstance(mStackLevel);

    newFragment.setQuizCreationDialogView(this);
    mHandler.post(new Runnable() {
      @Override
      public void run() {

        mStackLevel++;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        newFragment.show(ft, "StudentDataDialog111");

      }
    });

  }

  void openViewDialog() {
    schoolCensus.setState(Constants.STUDENT_DATA);
    schoolCensus.setCurrentActivity(getActivity());
    final QuestionViewDialogView
        newFragment = QuestionViewDialogView.newInstance(mStackLevel);
    newFragment.setQuizCreationDialogView(this);
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        mStackLevel++;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        newFragment.show(ft, "StudentDataDialog111");

      }
    });

  }


  void openCamera() {
//    mStackLevel++;
//    FragmentTransaction ft = getFragmentManager().beginTransaction();
//    cameraViewDialog = CameraViewDialog.newInstance(mStackLevel, 2);
//    cameraViewDialog.setStudentRegistrationDialogView(this);
//    cameraViewDialog.show(ft, "school_calendar_view_dialog");

  }

  @Override
  public void onPictureTaken(byte[] data, Camera camera) {

    Bitmap originalImage = BitmapFactory.decodeByteArray(data, 0, data.length);
//    bitmapImage = Utils.rotateImage(originalImage, getActivity());

//    bitmapImageString=android.util.Base64.encodeToString(data,android.util.Base64.DEFAULT);
    try {
      bitmapImageString = Base64.encodeBytes(data, Base64.GZIP);
    } catch (IOException e) {
      e.printStackTrace();
    }
    imageView.setImageBitmap(originalImage);
    imageView.setVisibility(ImageView.VISIBLE);
    cameraViewDialog.dismiss();
  }

  private File getDir() {
    File sdDir = Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    return new File(sdDir, "CameraAPIDemo");
  }

  Camera.Parameters getParameters() {
    return parameters;
  }

  public void setParameters(Camera.Parameters parameters) {
    this.parameters = parameters;
  }

  void startFingerprintScanning() {

  }

  private void StartEnrollWithUsername() {

  }


  //  private String sendData() {
//
//    String response = null;
//
//    SchoolResponse schoolResponse = schoolCensus.getCurrentSchool();
//    boolean found = false;
//
//    Students
//        studentsNew =
//        new Students(firstNameStr, lastNameStr, studentNumberStr, fingerPrintImageStringData, "",
//                     genderValue, selectedClass, selectedStream);
//
//    if (schoolResponse != null) {
//      Schools schools = schoolResponse.getSchool();
//      ArrayList<Students> studentsArrayList = schoolResponse.getSchool().getStudents();
//
//      if (studentsArrayList != null) {
//
//        int y = studentsArrayList.size();
//
//        for (int t = 0; t < y; t++) {
//
//          Students students = studentsArrayList.get(t);
//          if (students.getStudentId().equalsIgnoreCase(studentNumberStr)) {
//            found = true;
//
//            // studentsArrayList.set(t, studentsNew);
//          }
//
//        }
//
//      } else {
//
//      }
//
//      if (!found) {
//
//        schools.setStudents(studentsNew);
//        schoolCensus.setState(Constants.STUDENT_DATA);
//
//        schoolCensus.openDocument(DataTypes.School);
//
//        schools.setType(Schools.SCHOOL_FIELDS.School.toString());
//
//        schoolCensus.addSchoolsDataToDocument(DataTypes.School, schools);
//
//        dismiss();
//        schoolCensus
//            .updateDocumentToCloudant(DataTypes.School, schoolCensus.getCurrentSchool().getId());
//
//
//      } else {
//
//        response = "Student already exists";
//        // schools.setClassroomsArrayList(classroomsArrayList);
//
//      }
//
//
//    }
//
//    schoolCensus.setSchoolData(null);
//
//    return response;
//  }
//
  private void getClassData() {
//
//    try {
//
//      SchoolResponse schoolResponse = schoolCensus.getCurrentSchool();
//
//      ArrayList<Grades> classroomsArrayLis = schoolResponse.getSchool().getClassrooms();
//
//      int size = classroomsArrayLis.size();
//      classArray = new ComboBoxViewListItem[size];
//
//      for (int y = 0; y < size; y++) {
//
//        classArray[y] =
//            new ComboBoxViewListItem("" + y,
//                                     classroomsArrayLis.get(y).getClassName(),
//                                     "");
//
//
//      }
//
//      comboBoxViewAdapter = new ComboBoxViewAdapter(getActivity(),
//                                                    android.R.layout.simple_spinner_item,
//                                                    classArray);
//
//      spinner.setOnTouchListener(onClassesTouchListener);
//
//      comboBoxViewAdapter.setHint("All Classes");
//      spinner.setAdapter(comboBoxViewAdapter);
//
//      if (classArray.length > 0) {
//        reloadStreamsData(classArray[0].getText());
//      }
//
//    } catch (Exception ex) {
//      Log.i(Constants.TAG, "creating secret questions list error", ex);
//      ex.printStackTrace();
//
//    }
  }

  private void reloadStreamsData(String className) {

    classSelected = false;
//    SchoolResponse schoolResponse = schoolCensus.getCurrentSchool();
//
//    ArrayList<Grades> classroomsArrayLis = schoolResponse.getSchool().getClassrooms();
//
//    int size = classroomsArrayLis.size();
//    int inner_size = -1;
//
//    for (int y = 0; y < size; y++) {
//
//      if (classroomsArrayLis.get(y).getClassName().equalsIgnoreCase(className)) {
//
//        ArrayList<ClassroomStreams>
//            classroomStreamsArrayList =
//            classroomsArrayLis.get(y).getClassroomStreams();
//
//        inner_size = classroomStreamsArrayList.size();
//        classStreams = new ComboBoxViewListItem[inner_size];
//
//        for (int t = 0; t < inner_size; t++) {
//
//          classStreams[t] =
//              new ComboBoxViewListItem("" + t,
//                                       classroomStreamsArrayList.get(t).getStreamName(),
//                                       "");
//        }
//
//        break;
//      }
//
//
//    }

    comboBoxViewAdapterStreams = new ComboBoxViewAdapter(getActivity(),
                                                         android.R.layout.simple_spinner_item,
                                                         classStreams);
    spinnerStreams.setOnTouchListener(onStreamsTouchListener);

    comboBoxViewAdapterStreams.setHint("All Streams");

    spinnerStreams.setAdapter(comboBoxViewAdapterStreams);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    schoolCensus.setQuestion(data.get(position));
    openViewDialog();

  }


  private class OnStreamSelected implements
                                 AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

      selectedStream = classStreams[pos].getText();
    }

    public void onNothingSelected(AdapterView parent) {
      // Do nothing.
    }
  }

  private class OnClassSelected implements
                                AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

      selectedSubject = classArray[pos].getText();
    }

    public void onNothingSelected(AdapterView parent) {
      // Do nothing.
    }
  }

  private class backgroundProcess extends AsyncTask<Integer, Integer, Long> {

    int option;
    String response;

    protected Long doInBackground(Integer... params) {
//      option = params[0];
//      response = sendData();

      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {

      if (response != null) {
        Toast.makeText(getActivity(), response,
                       Toast.LENGTH_LONG).show();
      }

    }

    protected void onPreExecute() {

    }
  }


}