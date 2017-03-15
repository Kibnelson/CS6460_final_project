package com.edu.peers.dialogs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.managers.UserManager;
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.others.Base64;
import com.edu.peers.others.Constants;
import com.edu.peers.views.SchoolCensus;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by nelson on 3/16/15.
 */

public class QuestionCreationDialogView extends DialogFragment
    implements View.OnClickListener, Camera.PictureCallback {

  private static final int MESSAGE_SHOW_MSG = 1;
  private static final int MESSAGE_SHOW_IMAGE = 2;
  private static final int MESSAGE_ENROLL_FINGER = 3;
  private static final int MESSAGE_ENABLE_CONTROLS = 4;
  // Pending operations
  private static final int OPERATION_ENROLL = 1;
  private static final int OPERATION_IDENTIFY = 2;
  private static final int OPERATION_VERIFY = 3;
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
  private Button addButton;
  private EditText qName;
  private Spinner subjects;
  private EditText duration;
  private EditText instructions;
  private Button questions;
  private String selectedSubject;
  private String qNameStr;
  private String durationStr;
  private String instructionsStr;
  private ImageView voice_q;
  private ImageView write_q;
  private EditText choice_1;
  private ImageView voice_choice_1;
  private ImageView write_choice_1;
  private CheckBox checkbox_choice_1;
  private EditText choice_2;
  private ImageView voice_choice_2;
  private ImageView write_choice_2;
  private CheckBox checkbox_choice_2;
  private EditText choice_3;
  private ImageView voice_choice_3;
  private ImageView write_choice_3;
  private CheckBox checkbox_choice_3;
  private EditText choice_4;
  private ImageView voice_choice_4;
  private ImageView write_choice_4;
  private CheckBox checkbox_choice_4;
  private RecordAudioDialog recordAudioDialog;
  private ScratchPadDialog scratchPadDialog;
  private int writeIndex, voiceIndex;
  private String questionStringWriting = "", write1String = "", write2String = "",
      write3String =
          "", write4String = "";
  private String questionStringVoice = "", voice1String = "", voice2String = "", voice3String = "",
      voice4String =
          "";
  private int answerSelection1 = 0, answerSelection2 = 0, answerSelection3 = 0,
      answerSelection4 =
          0;
  private QuizCreationDialogView quizCreationDialogView;
  private UserObject userObject;
  private UserManager userManager;
  private ProgressDialog progressDialog;
  private Questions questionsObject;
  private boolean checkboxSelected=false;

  public QuestionCreationDialogView() {
    // Required empty public constructor
  }

  public static QuestionCreationDialogView newInstance(int num) {
    QuestionCreationDialogView f = new QuestionCreationDialogView();

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
    getDialog().setTitle("Create Question");
    view = inflater.inflate(R.layout.question_creation_dialog, null);

    return view;

  }

  @Override
  public void onStart() {
    super.onStart();

    this.getDialog().getWindow()
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    genderValue = null;

    schoolCensus = (SchoolCensus) getActivity().getApplication();
    userObject= schoolCensus.getUserObject();
    userManager = new UserManager(schoolCensus.getCloudantInstance(), getContext());

    qName = (EditText) view.findViewById(R.id.qName);

    voice_q = (ImageView) view.findViewById(R.id.voice_q);
    voice_q.setOnClickListener(this);

    write_q = (ImageView) view.findViewById(R.id.write_q);
    write_q.setOnClickListener(this);

    // 1 choice
    choice_1 = (EditText) view.findViewById(R.id.choice_1);

    voice_choice_1 = (ImageView) view.findViewById(R.id.voice_choice_1);
    voice_choice_1.setOnClickListener(this);

    write_choice_1 = (ImageView) view.findViewById(R.id.write_choice_1);
    write_choice_1.setOnClickListener(this);

    checkbox_choice_1 = (CheckBox) view.findViewById(R.id.checkbox_choice_1);
    checkbox_choice_1.setOnClickListener(this);

    // 2 choice
    choice_2 = (EditText) view.findViewById(R.id.choice_2);

    voice_choice_2 = (ImageView) view.findViewById(R.id.voice_choice_2);
    voice_choice_2.setOnClickListener(this);

    write_choice_2 = (ImageView) view.findViewById(R.id.write_choice_2);
    write_choice_2.setOnClickListener(this);

    checkbox_choice_2 = (CheckBox) view.findViewById(R.id.checkbox_choice_2);
    checkbox_choice_2.setOnClickListener(this);

    // 3 choice
    choice_3 = (EditText) view.findViewById(R.id.choice_3);

    voice_choice_3 = (ImageView) view.findViewById(R.id.voice_choice_3);
    voice_choice_3.setOnClickListener(this);

    write_choice_3 = (ImageView) view.findViewById(R.id.write_choice_3);
    write_choice_3.setOnClickListener(this);

    checkbox_choice_3 = (CheckBox) view.findViewById(R.id.checkbox_choice_3);
    checkbox_choice_3.setOnClickListener(this);

    // 4 choice
    choice_4 = (EditText) view.findViewById(R.id.choice_4);

    voice_choice_4 = (ImageView) view.findViewById(R.id.voice_choice_4);
    voice_choice_4.setOnClickListener(this);

    write_choice_4 = (ImageView) view.findViewById(R.id.write_choice_4);
    write_choice_4.setOnClickListener(this);

    checkbox_choice_4 = (CheckBox) view.findViewById(R.id.checkbox_choice_4);
    checkbox_choice_4.setOnClickListener(this);

    cancelButton = (Button) view.findViewById(R.id.cancel);
    cancelButton.setOnClickListener(this);

    addButton = (Button) view.findViewById(R.id.ok_button);
    addButton.setOnClickListener(this);

  }


  @Override
  public void onClick(View v) {
    /** When OK Button is clicked, dismiss the school_calendar_view_dialog */
    if (v == addButton) {


      String inputString=qName.getText().toString();


      boolean valid = true;
      StringBuilder errorBuffer = new StringBuilder();
      if (( inputString==null || inputString.length() == 0) && ( questionStringWriting.length() == 0) &&    ( questionStringVoice.length() == 0)) {
        valid = false;
        errorBuffer.append("Please enter question name\n");
      }

      if (!checkboxSelected) {
        valid = false;
        errorBuffer.append("Please choose at least one choice\n");
      }

      if (valid) {

        User user = userObject.getUser();

        List<Input> choices = new ArrayList<>();
        // 1 Choice
        Input
            input1 =
            new Input(1, choice_1.getText().toString(), write1String, voice1String, user);
        if (answerSelection1 == 1)
          input1.setAnswer(true);
        choices.add(input1);
        // 2 Choice
        Input
            input2 =
            new Input(2, choice_2.getText().toString(), write2String, voice2String, user);
        if (answerSelection2 == 2)
          input2.setAnswer(true);

        choices.add(input2);
        // 3 Choice
        Input
            input3 =
            new Input(3, choice_3.getText().toString(), write3String, voice3String, user);
        if (answerSelection3 == 3)
          input3.setAnswer(true);

        choices.add(input3);
        // 4 Choice
        Input
            input4 =
            new Input(4, choice_4.getText().toString(), write4String, voice4String, user);
        if (answerSelection4 == 4)
          input4.setAnswer(true);

        choices.add(input4);

        List<Input> answers = new ArrayList<>();

        if (answerSelection1 == 1) {
          answers
              .add(new Input(1, choice_1.getText().toString(), write1String, voice1String, user));
        }
        if (answerSelection2 == 2) {
          answers
              .add(new Input(2, choice_2.getText().toString(), write2String, voice2String, user));
        }
        if (answerSelection3 == 3) {
          answers
              .add(new Input(3, choice_3.getText().toString(), write3String, voice3String, user));
        }
        if (answerSelection4 == 4) {
          answers
              .add(new Input(4, choice_4.getText().toString(), write4String, voice4String, user));
        }

        questionsObject =
            new Questions(qName.getText().toString(), questionStringWriting, questionStringVoice,
                          answers, choices, getUserObject().getUser());

        List<User> userList =userObject.getUserList();
        int size=userList.size();

//        for (int y=0;y<size;y++){
//          User user1=userList.get(y);
//          Log.i(Constants.TAG,"user.getUsername(==="+user.getUsername());
//          Log.i(Constants.TAG,"user1.getUsername(==="+user1.getUsername());
//
//          if (user.getUsername().equalsIgnoreCase(user1.getUsername())) {
//
//            List<UserStatistics> questionsAsked = user1.getQuestionsAsked();
//            questionsAsked.add(
//                new UserStatistics(Utils.getCurrentDate(), Utils.generateNumber(),
//                                   UUID.randomUUID().toString(),
//                                   Constants.QUESTIONS_CATEGORY));
//            user1.setQuestionsAsked(questionsAsked);
//            userList.set(y, user1);
//          }
//        }
        userObject.setUserList(userList);


         userObject.setUser(user);
        new backgroundProcessSave().execute();

      } else {
        Toast.makeText(getActivity(), errorBuffer.toString(), Toast.LENGTH_LONG).show();

        return;
      }
    } else if (v == cancelButton) {
      dismiss();

    } else if (v == write_q) {
      if (questionStringWriting.length() == 0) {
        writeIndex = 1;
        openWriteDialog();
      } else {
        openWriteDialog(questionStringWriting);
      }
    } else if (v == voice_q) {
      if (questionStringVoice.length()==0){
        voiceIndex = 1;
        openRecordDialog();
      } else
        openRecordDialog(questionStringVoice);
    } else if (v == voice_choice_1) {
      if (voice1String.length()==0){
      voiceIndex = 2;
      openRecordDialog();
      } else {

        openRecordDialog(voice1String);
      }
    } else if (v == write_choice_1) {
      if (write1String.length() == 0) {
        writeIndex = 2;
        openWriteDialog();
      } else {
        openWriteDialog(write1String);
      }
    } else if (v == voice_choice_2) {
      if (voice2String.length()==0){
        voiceIndex = 3;
        openRecordDialog();
      } else {

        openRecordDialog(voice2String);
      }
    } else if (v == write_choice_2) {


      if (write2String.length() == 0) {
        writeIndex =3;
        openWriteDialog();
      } else {
        openWriteDialog(write2String);
      }


    } else if (v == voice_choice_3) {


      if (voice3String.length()==0){
        voiceIndex = 4;
        openRecordDialog();
      } else {

        openRecordDialog(voice3String);
      }


    } else if (v == write_choice_3) {

      if (write3String.length() == 0) {
        writeIndex =4;
        openWriteDialog();
      } else {
        openWriteDialog(write3String);
      }
    } else if (v == voice_choice_4) {

      if (voice4String.length()==0){
        voiceIndex = 5;
        openRecordDialog();
      } else {

        openRecordDialog(voice4String);
      }
    } else if (v == write_choice_4) {

      if (write4String.length() == 0) {
        writeIndex =5;
        openWriteDialog();
      } else {
        openWriteDialog(write4String);
      }

    } else if (v == checkbox_choice_1) {
      checkboxSelected=true;
      answerSelection1 = 1;
    } else if (v == checkbox_choice_2) {
      checkboxSelected=true;
      answerSelection2 = 2;
    } else if (v == checkbox_choice_3) {
      answerSelection3 = 3;
      checkboxSelected=true;
    } else if (v == checkbox_choice_4) {
      answerSelection4 = 4;
      checkboxSelected=true;
    }


  }

  void openRecordDialog() {
    mStackLevel++;
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    recordAudioDialog = RecordAudioDialog.newInstance(mStackLevel, 2);
    recordAudioDialog.setQuestionCreationDialogView(this);
    recordAudioDialog.show(ft, "school_calendar_view_dialog");

  }

  void openWriteDialog() {
    mStackLevel++;
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    scratchPadDialog = ScratchPadDialog.newInstance(mStackLevel, 2);
    scratchPadDialog.setQuestionCreationDialogView(this);
    scratchPadDialog.show(ft, "school_calendar_view_dialog");

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


  public QuizCreationDialogView getQuizCreationDialogView() {
    return quizCreationDialogView;
  }

  public void setQuizCreationDialogView(
      QuizCreationDialogView quizCreationDialogView) {
    this.quizCreationDialogView = quizCreationDialogView;
  }

  public UserObject getUserObject() {
    return userObject;
  }

  public void setUserObject(UserObject userObject) {
    this.userObject = userObject;
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

  public void setWriteImage(Bitmap bitmap) {
    try {

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
      byte[] byteArray = byteArrayOutputStream.toByteArray();

      String encoded = Base64.encodeBytes(byteArray);
      Log.i(Constants.TAG, "writeIndexwriteIndexwriteIndexwriteIndexwriteIndex===" + writeIndex);

      if (writeIndex == 1) {

        questionStringWriting = encoded;
        write_q.setBackgroundColor(Color.parseColor("#007670"));
      } else if (writeIndex == 2) {
        write1String = encoded;
        write_choice_1.setBackgroundColor(Color.parseColor("#007670"));
      } else if (writeIndex == 3) {
        write2String = encoded;
        write_choice_2.setBackgroundColor(Color.parseColor("#007670"));
      } else if (writeIndex == 4) {
        write3String = encoded;
        write_choice_3.setBackgroundColor(Color.parseColor("#007670"));
      } else if (writeIndex == 5) {
        write4String = encoded;
        write_choice_4.setBackgroundColor(Color.parseColor("#007670"));
      }
////    Utilities.log("~~~~~~~~ Encoded: ", encoded);
//
//    byte[] decoded = Base64.decode(encoded);
////    Utilities.log("~~~~~~~~ Decoded: ", Arrays.toString(decoded));
//
//
//      File file2 = new File(Environment.getExternalStorageDirectory() + "/hello-5.wav");
//      FileOutputStream os = new FileOutputStream(file2, true);
//      os.write(decoded);
//      os.close();
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public void setRecordFile(String mFileName) {
    try {
      File file = new File(mFileName);
      byte[] bytes = FileUtils.readFileToByteArray(file);

      String encoded = Base64.encodeBytes(bytes);

      Log.i(Constants.TAG,
            "setRecordFilesetRecordFilesetRecordFilesetRecordFilesetRecordFile===" + voiceIndex);

      if (voiceIndex == 1) {

        questionStringVoice = encoded;
        voice_q.setBackgroundColor(Color.parseColor("#007670"));
      } else if (voiceIndex == 2) {

        voice1String = encoded;
        voice_choice_1.setBackgroundColor(Color.parseColor("#007670"));
      } else if (voiceIndex == 3) {
        voice2String = encoded;
        voice_choice_2.setBackgroundColor(Color.parseColor("#007670"));
      } else if (voiceIndex == 4) {
        voice3String = encoded;
        voice_choice_3.setBackgroundColor(Color.parseColor("#007670"));
      } else if (voiceIndex == 5) {
        voice4String = encoded;
        voice_choice_4.setBackgroundColor(Color.parseColor("#007670"));
      }
////    Utilities.log("~~~~~~~~ Encoded: ", encoded);
//
//    byte[] decoded = Base64.decode(encoded);
////    Utilities.log("~~~~~~~~ Decoded: ", Arrays.toString(decoded));
//
//
//      File file2 = new File(Environment.getExternalStorageDirectory() + "/hello-5.wav");
//      FileOutputStream os = new FileOutputStream(file2, true);
//      os.write(decoded);
//      os.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  void openRecordDialog(String recordedString) {
    mStackLevel++;
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    recordAudioDialog = RecordAudioDialog.newInstance(mStackLevel, 2);
//    recordAudioDialog.setQuestionViewDialogView(this);
    recordAudioDialog.setRecordedString(recordedString);
    recordAudioDialog.show(ft, "school_calendar_view_dialog");

  }

  void openWriteDialog(String imageString) {
    mStackLevel++;
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    scratchPadDialog = ScratchPadDialog.newInstance(mStackLevel, 2);
//    scratchPadDialog.setQuestionViewDialogView(this);
    scratchPadDialog.setImageString(imageString);
    scratchPadDialog.show(ft, "school_calendar_view_dialog");

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
        userManager.addDocument(userObject,Constants.USERS);
      } catch (Exception e) {
        e.printStackTrace();
      }

      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {
      hideProgessDialog();

      completeStatisticsCreation();
    }



    protected void onPreExecute() {
      showProgessDialog();
    }

  }

  private void completeStatisticsCreation() {
    quizCreationDialogView.addQuestion(questionsObject);
    dismiss();


  }
}