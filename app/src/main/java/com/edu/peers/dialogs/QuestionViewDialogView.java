package com.edu.peers.dialogs;

import android.app.Activity;
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
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.models.UserObject;
import com.edu.peers.others.Base64;
import com.edu.peers.views.SchoolCensus;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


/**
 * Created by nelson on 3/16/15.
 */

public class QuestionViewDialogView extends DialogFragment
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
  private TextView qName;
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
  private TextView choice_1;
  private ImageView voice_choice_1;
  private ImageView write_choice_1;
  private CheckBox checkbox_choice_1;
  private TextView choice_2;
  private ImageView voice_choice_2;
  private ImageView write_choice_2;
  private CheckBox checkbox_choice_2;
  private TextView choice_3;
  private ImageView voice_choice_3;
  private ImageView write_choice_3;
  private CheckBox checkbox_choice_3;
  private TextView choice_4;
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
  private Questions question;
  private UserObject userObject;
  public QuestionViewDialogView() {
    // Required empty public constructor
  }

  public static QuestionViewDialogView newInstance(int num) {
    QuestionViewDialogView f = new QuestionViewDialogView();

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
    getDialog().setTitle("View Question");
    view = inflater.inflate(R.layout.question_view_dialog, null);

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

    question = schoolCensus.getQuestion();

    qName = (TextView) view.findViewById(R.id.qName);

    voice_q = (ImageView) view.findViewById(R.id.voice_q);
    voice_q.setOnClickListener(this);

    write_q = (ImageView) view.findViewById(R.id.write_q);
    write_q.setOnClickListener(this);

    // 1 choice
    choice_1 = (TextView) view.findViewById(R.id.choice_1);

    voice_choice_1 = (ImageView) view.findViewById(R.id.voice_choice_1);
    voice_choice_1.setOnClickListener(this);

    write_choice_1 = (ImageView) view.findViewById(R.id.write_choice_1);
    write_choice_1.setOnClickListener(this);

    checkbox_choice_1 = (CheckBox) view.findViewById(R.id.checkbox_choice_1);
    checkbox_choice_1.setOnClickListener(this);
    checkbox_choice_1.setVisibility(View.GONE);

    // 2 choice
    choice_2 = (TextView) view.findViewById(R.id.choice_2);

    voice_choice_2 = (ImageView) view.findViewById(R.id.voice_choice_2);
    voice_choice_2.setOnClickListener(this);

    write_choice_2 = (ImageView) view.findViewById(R.id.write_choice_2);
    write_choice_2.setOnClickListener(this);

    checkbox_choice_2 = (CheckBox) view.findViewById(R.id.checkbox_choice_2);
    checkbox_choice_2.setOnClickListener(this);
    checkbox_choice_2.setVisibility(View.GONE);
    // 3 choice
    choice_3 = (TextView) view.findViewById(R.id.choice_3);

    voice_choice_3 = (ImageView) view.findViewById(R.id.voice_choice_3);
    voice_choice_3.setOnClickListener(this);

    write_choice_3 = (ImageView) view.findViewById(R.id.write_choice_3);
    write_choice_3.setOnClickListener(this);

    checkbox_choice_3 = (CheckBox) view.findViewById(R.id.checkbox_choice_3);
    checkbox_choice_3.setOnClickListener(this);
    checkbox_choice_3.setVisibility(View.GONE);
    // 4 choice
    choice_4 = (TextView) view.findViewById(R.id.choice_4);

    voice_choice_4 = (ImageView) view.findViewById(R.id.voice_choice_4);
    voice_choice_4.setOnClickListener(this);

    write_choice_4 = (ImageView) view.findViewById(R.id.write_choice_4);
    write_choice_4.setOnClickListener(this);

    checkbox_choice_4 = (CheckBox) view.findViewById(R.id.checkbox_choice_4);
    checkbox_choice_4.setOnClickListener(this);
    checkbox_choice_4.setVisibility(View.GONE);

    cancelButton = (Button) view.findViewById(R.id.cancel);
    cancelButton.setOnClickListener(this);

    // Update fields

    if (question != null) {
      qName.setText(question.getQuestionInput());
      if (question.getQuestionWriting().length() > 0) {
        write_q.setBackgroundColor(Color.parseColor("#007670"));
        questionStringWriting = question.getQuestionWriting();
      }
      if (question.getQuestionVoice().length() > 0) {
        voice_q.setBackgroundColor(Color.parseColor("#007670"));
        questionStringVoice = question.getQuestionVoice();
      }

      for (Input input : question.getChoices()) {

        if (input.getPosition() == 1 && input.getQuestionInput().length() > 0
            && input.getQuestionVoice().length() > 0 && input.getQuestionWriting().length() > 0) {
          choice_1.setText(input.getQuestionInput());
          write1String = input.getQuestionWriting();
          voice1String = input.getQuestionVoice();
          write_choice_1.setBackgroundColor(Color.parseColor("#007670"));
          voice_choice_1.setBackgroundColor(Color.parseColor("#007670"));
        } else if (input.getPosition() == 2 && input.getQuestionInput().length() > 0
                   && input.getQuestionVoice().length() > 0
                   && input.getQuestionWriting().length() > 0) {
          write_choice_2.setBackgroundColor(Color.parseColor("#007670"));
          voice_choice_2.setBackgroundColor(Color.parseColor("#007670"));

          write2String = input.getQuestionWriting();
          voice2String = input.getQuestionVoice();
          choice_2.setText(input.getQuestionInput());
        } else if (input.getPosition() == 3 && input.getQuestionInput().length() > 0
                   && input.getQuestionVoice().length() > 0
                   && input.getQuestionWriting().length() > 0) {
          write_choice_3.setBackgroundColor(Color.parseColor("#007670"));
          voice_choice_3.setBackgroundColor(Color.parseColor("#007670"));

          write3String = input.getQuestionWriting();
          voice3String = input.getQuestionVoice();
          choice_3.setText(input.getQuestionInput());
        } else if (input.getPosition() == 4 && input.getQuestionInput().length() > 0
                   && input.getQuestionVoice().length() > 0
                   && input.getQuestionWriting().length() > 0) {
          write4String = input.getQuestionWriting();
          voice4String = input.getQuestionVoice();
          write_choice_4.setBackgroundColor(Color.parseColor("#007670"));
          voice_choice_4.setBackgroundColor(Color.parseColor("#007670"));
          choice_4.setText(input.getQuestionInput());
        }
      }

    }
  }


  @Override
  public void onClick(View v) {
    /** When OK Button is clicked, dismiss the school_calendar_view_dialog */
    if (v == cancelButton) {
      dismiss();
    } else if (v == write_q) {

      if (questionStringWriting.length() > 0) {
        openWriteDialog(questionStringWriting);
      } else {
        Toast.makeText(getActivity(), "There is no handwritten information", Toast.LENGTH_LONG)
            .show();
      }

    } else if (v == voice_q) {
      if (questionStringVoice.length() > 0) {
        voiceIndex = 1;
        openRecordDialog(questionStringVoice);
      } else {
        Toast.makeText(getActivity(), "There is no recorded audio", Toast.LENGTH_LONG).show();
      }
    } else if (v == voice_choice_1) {
      voiceIndex = 2;
      if (voice1String.length() > 0) {

        openRecordDialog(voice1String);
      } else {
        Toast.makeText(getActivity(), "There is no recorded audio", Toast.LENGTH_LONG).show();
      }
    } else if (v == write_choice_1) {

      if (write1String.length() > 0) {
        openWriteDialog(write1String);
      } else {
        Toast.makeText(getActivity(), "There is no handwritten information", Toast.LENGTH_LONG)
            .show();
      }
    } else if (v == voice_choice_2) {
      if (voice2String.length() > 0) {

        voiceIndex = 3;
        openRecordDialog(voice2String);
      } else {
        Toast.makeText(getActivity(), "There is no recorded audio", Toast.LENGTH_LONG).show();
      }
    } else if (v == write_choice_2) {

      if (write2String.length() > 0) {
        openWriteDialog(write2String);
      } else {
        Toast.makeText(getActivity(), "There is no handwritten information", Toast.LENGTH_LONG)
            .show();
      }
    } else if (v == voice_choice_3) {
      if (voice3String.length() > 0) {
        openRecordDialog(voice3String);
      } else {
        Toast.makeText(getActivity(), "There is no recorded audio", Toast.LENGTH_LONG).show();
      }
    } else if (v == write_choice_3) {
      if (write3String.length() > 0) {
        openWriteDialog(write3String);
      } else {
        Toast.makeText(getActivity(), "There is no handwritten information", Toast.LENGTH_LONG)
            .show();
      }
    } else if (v == voice_choice_4) {
      if (voice4String.length() > 0) {
        openRecordDialog(voice4String);
      } else {
        Toast.makeText(getActivity(), "There is no recorded audio", Toast.LENGTH_LONG).show();
      }
    } else if (v == write_choice_4) {
      if (write4String.length() > 0) {
        openWriteDialog(write4String);
      } else {
        Toast.makeText(getActivity(), "There is no handwritten information", Toast.LENGTH_LONG)
            .show();
      }
    } else if (v == checkbox_choice_1) {
      answerSelection1 = 1;
    } else if (v == checkbox_choice_2) {
      answerSelection2 = 2;
    } else if (v == checkbox_choice_3) {
      answerSelection3 = 3;
    } else if (v == checkbox_choice_4) {
      answerSelection4 = 4;
    }


  }

  void openRecordDialog(String recordedString) {
    mStackLevel++;
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    recordAudioDialog = RecordAudioDialog.newInstance(mStackLevel, 2);
    recordAudioDialog.setQuestionViewDialogView(this);
    recordAudioDialog.setRecordedString(recordedString);
    recordAudioDialog.show(ft, "school_calendar_view_dialog");

  }

  void openWriteDialog(String imageString) {
    mStackLevel++;
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    scratchPadDialog = ScratchPadDialog.newInstance(mStackLevel, 2);
    scratchPadDialog.setQuestionViewDialogView(this);
    scratchPadDialog.setImageString(imageString);
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

  public QuizCreationDialogView getQuizCreationDialogView() {
    return quizCreationDialogView;
  }

  public void setQuizCreationDialogView(
      QuizCreationDialogView quizCreationDialogView) {
    this.quizCreationDialogView = quizCreationDialogView;
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

}