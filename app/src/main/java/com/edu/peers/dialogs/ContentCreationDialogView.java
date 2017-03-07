package com.edu.peers.dialogs;

import android.app.Activity;
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
import android.util.Log;
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

import com.keenfin.sfcdialog.SimpleFileChooser;
import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.adapter.ContentFileListViewAdapter;
import com.edu.peers.models.Content;
import com.edu.peers.models.ContentFile;
import com.edu.peers.others.Base64;
import com.edu.peers.others.Constants;
import com.edu.peers.views.SchoolCensus;
import com.edu.peers.views.LearningContentListView;
import com.edu.peers.views.MainView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by nelson on 3/16/15.
 */

public class ContentCreationDialogView extends DialogFragment
    implements View.OnClickListener, Camera.PictureCallback, AdapterView.OnItemClickListener {

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
  private EditText contentTitle;
  private Spinner subjects;
  private Button addFile;
  private String selectedSubject;
  private String contentTitleStr;
  private String tagsStr;
  private ListView fileList;
  private List<ContentFile> data = new ArrayList<>();
  private List<ContentFile> contentFiles = new ArrayList<>();
  private Button saveButton;
  private EditText tags;
  private MainView mainView;
  private LearningContentListView learningContentListView;

  public ContentCreationDialogView() {
    // Required empty public constructor
  }

  public static ContentCreationDialogView newInstance(int num) {
    ContentCreationDialogView f = new ContentCreationDialogView();

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
    getDialog().setTitle("Learning Materials");
    view = inflater.inflate(R.layout.content_creation_dialog, null);

    return view;

  }

  @Override
  public void onStart() {
    super.onStart();

    this.getDialog().getWindow()
        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    genderValue = null;

    schoolCensus = (SchoolCensus) getActivity().getApplication();

    mainView = schoolCensus.getMainView();

    contentTitle = (EditText) view.findViewById(R.id.contentTitle);

    tags = (EditText) view.findViewById(R.id.tags);

    subjects = (Spinner) view.findViewById(R.id.subjects);
    subjects.setOnItemSelectedListener(new OnClassSelected());

    tags = (EditText) view.findViewById(R.id.tags);

    addFile = (Button) view.findViewById(R.id.addFile);
    addFile.setOnClickListener(this);

    fileList = (ListView) view.findViewById(R.id.fileList);
    fileList.setOnItemClickListener(this);

    registerForContextMenu(fileList);

    fileList.setOnItemClickListener(this);
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
      contentTitleStr = contentTitle.getText().toString();
      tagsStr = tags.getText().toString();

      boolean valid = true;
      StringBuilder errorBuffer = new StringBuilder();
      if (contentTitleStr == null || contentTitleStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter content title \n");
      }
      if (tagsStr == null || tagsStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter atleast one tag\n");
      }

      if (classSelected && selectedSubject.length() == 0) {
        valid = false;
        errorBuffer.append("Please select subject\n");
      }

      if (contentFiles.size() == 0) {
        valid = false;
        errorBuffer.append("Please add one or more file(s)\n");
      }

      if(!classSelected) {
        valid = false;
        errorBuffer.append("Please select subject\n");
      }




      if (valid) {


        Content content = new Content(contentTitleStr,selectedSubject,contentFiles,null);

        learningContentListView.addContent(content);

        dismiss();

      } else {
        Toast.makeText(getActivity(), errorBuffer.toString(), Toast.LENGTH_LONG).show();

        return;
      }

    } else if (v == cancelButton) {

      dismiss();
    } else if (v == addFile) {
      openFileChooser();

    } else if (v == fingerPrintImage) {
      fingerSelected = 2;
      startFingerprintScanning();
    } else if (v == male) {
      genderValue = "Male";
    } else if (v == female) {
      genderValue = "Female";
    }


  }

  public void addContentfile(ContentFile contentFile) {
    contentFiles.add(contentFile);

    data.add(contentFile);
    fileList.setAdapter(new ContentFileListViewAdapter(this, data));
    fileList.setOnItemClickListener(this);
  }

  public void openFileChooser() {
    SimpleFileChooser sfcDialog = new SimpleFileChooser();

    sfcDialog.setOnChosenListener(new SimpleFileChooser.SimpleFileChooserListener() {
      @Override
      public void onFileChosen(File file) {
        // File is chosen


        if (file!=null) {

          ContentFile content = new ContentFile();
          Log.i(Constants.TAG, "File" + file.getPath());
          Log.i(Constants.TAG, "File" + file.getAbsolutePath());
          Log.i(Constants.TAG, "File" + file.getName());

          try {

            byte[] bytes = FileUtils.readFileToByteArray(file);

            String encoded = Base64.encodeBytes(bytes);
            content.setContent(encoded);
            content.setFileName(file.getName());
            content.setFilePath(file.getPath());

            addContentfile(content);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

      }

      @Override
      public void onDirectoryChosen(File directory) {
        // Directory is chosen
      }

      @Override
      public void onCancel() {
        // onCancel
      }
    });


    sfcDialog.show(mainView.getFragmentManager(), "SimpleFileChooserDialog");

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
//    schoolCensus.setQuestion(data.get(position));
//    openViewDialog();

  }

  public LearningContentListView getLearningContentListView() {
    return learningContentListView;
  }

  public void setLearningContentListView(
      LearningContentListView learningContentListView) {
    this.learningContentListView = learningContentListView;
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