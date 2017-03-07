package com.edu.peers.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.dialogs.CameraViewDialog;
import com.edu.peers.managers.UserManager;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.others.Base64;
import com.edu.peers.others.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by nelson on 3/16/15.
 */
public class UserRegistrationView extends FragmentActivity implements
                                                           View.OnClickListener,
                                                           Camera.PictureCallback,
                                                           AdapterView.OnItemClickListener {

  private static String ARG_POSITION;
  public ComboBoxViewListItem[] classArray;
  public ComboBoxViewListItem[] comboBoxViewListItems;
  public String countyText;
  TextView message;
  private ArrayList<String> sampleDataId = new ArrayList<>();
  private ArrayList<String> sampleDataRevId = new ArrayList<>();
  private long totalSize = 0;
  private Button helpButton;
  private Button homeButton;
  private Button signOutButton;
  private ListView listView;
  private Handler mHandler;
  private LinearLayout progressBar;
  private View view;
  private Spinner spinner;
  private int year, day, month;
  private Button btnChangeDate;
  private List<Date> selectedDates;
  private Button pickdate;
  private ComboBoxViewAdapter comboBoxViewAdapter;
  private String selectedLesson;
  private TextView studentName, studentNo, county, studentClass;
  private TextView tvX;
  private TextView tvY;
  private SeekBar mSeekBarX;
  private SeekBar mSeekBarY;
  private Typeface mTf;
  private LineChart mChart;
  private TextView studentGender;
  private ImageView backButton;
  private ArrayList<ComboBoxViewListItem> gridData = new ArrayList<>();
  private GridView gridView;
  private EditText firstName;
  private EditText lastName;
  private ImageView imageView;
  private EditText studentNumber;
  private Spinner spinnerStreams;
  private TextView mTxtMessage;
  private ImageView fingerPrintImage;
  private ComboBoxViewListItem[] classStreams;
  private int mStackLevel;
  private CameraViewDialog cameraViewDialog;
  private String bitmapImageString;
  private boolean classSelected = false;
  private View.OnTouchListener onClassesTouchListener = new View.OnTouchListener() {


    @Override
    public boolean onTouch(View v, MotionEvent event) {

      classSelected = true;

      ((ComboBoxViewAdapter) comboBoxViewAdapter).setSelected(true);

      ((ComboBoxViewAdapter) comboBoxViewAdapter).notifyDataSetChanged();
      return false;
    }
  };
  private EditText schoolYearOfRegistration;

  private Button okButton = null;
  private Button cancelButton = null;
  private TextView title = null;
  private EditText schoolName = null;
  private EditText number = null;
  private SchoolCensus schoolCensus;
  private Activity activity;
  private ComboBoxViewListItem[] categoryArray;
  private String schoolCategory;
  private String nameStr;
  private String numberStr;
  private String schoolGirlsStr, schoolBoysStr, locationCountyStr, locationSubCountyStr,
      locationConstituencyStr, locationZoneStr, locationWardStr, schoolNameStr, schoolAddressStr,
      schoolYearOfRegistrationStr, schoolRegistrationNumberStr, schoolTSCNumberStr,
      schoolKnecNumberStr;
  private String locationResidenceStr, schoolStatusStr, schoolCategoryStr, schoolTypeStr,
      schoolAccomondationStr;
  private boolean locationAsalAreaStr;
  private ComboBoxViewAdapter comboBoxViewAdapterStreams;
  private String selectedStream;
  private Camera.Parameters parameters;
  private Spinner spinnerGender;
  private ComboBoxViewListItem[] genderArray;
  private ComboBoxViewAdapter genderViewAdapter;
  private ImageView deleteButton;


  private View.OnTouchListener onStreamsTouchListener = new View.OnTouchListener() {

    @Override
    public boolean onTouch(View v, MotionEvent event) {

      ((ComboBoxViewAdapter) comboBoxViewAdapterStreams).setSelected(true);

      ((ComboBoxViewAdapter) comboBoxViewAdapterStreams).notifyDataSetChanged();
      return false;
    }
  };
  private View.OnTouchListener onGenderTouchListener = new View.OnTouchListener() {

    @Override
    public boolean onTouch(View v, MotionEvent event) {

      ((ComboBoxViewAdapter) genderViewAdapter).setSelected(true);

      ((ComboBoxViewAdapter) genderViewAdapter).notifyDataSetChanged();
      return false;
    }
  };
  private UserManager userManager;
  private EditText fName, lName, username, password;
  private RadioButton instructor, student, male, female;
  private String selectedGender;
  private String selectedRole;
  private String fNameStr, lNameStr, usernameStr, passwordStr;
  private UserObject userObject;
  private String bitmapFaceImageString;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_registration);

    this.mHandler = new Handler(Looper.getMainLooper());
    schoolCensus = (SchoolCensus) getApplication();
    schoolCensus.setState(Constants.USER_REGISTRATION_VIEW);

    userManager = new UserManager(schoolCensus.getCloudantInstance(), this);

    backButton = (ImageView) findViewById(R.id.menu_drawer);
    deleteButton = (ImageView) findViewById(R.id.delete);
    imageView = (ImageView) findViewById(R.id.face_photo);
    imageView.setOnClickListener(this);
    okButton = (Button) findViewById(R.id.ok_button);
    okButton.setOnClickListener(this);
    okButton.setOnClickListener(this);
    cancelButton = (Button) findViewById(R.id.cancel_button);
    cancelButton.setOnClickListener(this);

    title = (TextView) findViewById(R.id.title);
    title.setText("User Registration");

    showBackButton();
    showDeleteButton();

    fName = (EditText) findViewById(R.id.fName);
    lName = (EditText) findViewById(R.id.lName);
    username = (EditText) findViewById(R.id.username);
    password = (EditText) findViewById(R.id.password);

    instructor = (RadioButton) findViewById(R.id.instructor);
    instructor.setOnClickListener(this);

    student = (RadioButton) findViewById(R.id.student);
    student.setOnClickListener(this);

    male = (RadioButton) findViewById(R.id.male);
    male.setOnClickListener(this);

    female = (RadioButton) findViewById(R.id.female);
    female.setOnClickListener(this);

  }


  @Override
  public void onClick(View v) {
    if (v == male || v == female) {

      if (male.isChecked()) {
        selectedGender = "Male";
      }

      if (female.isChecked()) {
        selectedGender = "Female";
      }

    } else if (v == student || v == instructor) {

      if (student.isChecked()) {
        selectedRole = "Student";
      }

      if (instructor.isChecked()) {
        selectedRole = "Instructor";
      }

    } else if (v == backButton) {
      onBackPressed();
    } else if (v == okButton) {

      fNameStr = fName.getText().toString();
      lNameStr = lName.getText().toString();
      usernameStr = username.getText().toString();
      passwordStr = password.getText().toString();

      boolean valid = true;
      StringBuffer errorBuffer = new StringBuffer();
      if (fNameStr == null || fNameStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter first name\n");
      }
      if (lNameStr == null || lNameStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter last name\n");
      }

      if (usernameStr == null || usernameStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter username\n");
      } else if (userManager.checkIfUsernameExists(usernameStr)) {
        valid = false;
        errorBuffer.append("Username already exists, please try another one\n");
      }
      if (passwordStr == null || passwordStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter password\n");
      }

      if (selectedRole == null || selectedRole.length() == 0) {
        valid = false;
        errorBuffer.append("Please select role\n");
      }

      if (selectedGender == null || selectedGender.length() == 0) {
        valid = false;
        errorBuffer.append("Please select gender\n");
      }

      userManager = new UserManager(schoolCensus.getCloudantInstance(), this);
      Log.i(Constants.TAG, "DATA-=====" + userManager.getDocumentCount());

      if (valid) {

        userObject = schoolCensus.getUserObject();

        User
            user =
            new User(fNameStr, lNameStr, selectedRole, selectedGender, usernameStr, passwordStr);
        userObject.setUser(user);

        userManager.addDocument(userObject, usernameStr);

        onBackPressed();


      } else {

        Toast.makeText(getApplicationContext(), errorBuffer.toString(), Toast.LENGTH_LONG).show();

      }


    } else if (v == cancelButton) {
      onBackPressed();
    } else if (v == imageView) {

      openCamera();

    }
  }

  void openCamera() {

////    FragmentActivity fragmentActivity = (FragmentActivity) this;
//    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
////    RecipeRemoveDialogFragment recipeDialogFragment = new RecipeRemoveDialogFragment();
////    recipeDialogFragment.show(fragmentManager, "recipeDialogFragment");

    PackageManager pm = getPackageManager();
    FragmentManager fm = getSupportFragmentManager();
    if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
      mStackLevel++;
      android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
      cameraViewDialog = CameraViewDialog.newInstance(mStackLevel, 1);
      cameraViewDialog.setUserRegistrationView(this);
      cameraViewDialog.setCameraFrontOrBack(2);
      cameraViewDialog.show(fm, "school_calendar_view_dialog");

    } else {

//      schoolCensus.showMessageDialogAlert(getApplicationContext(), Constants.CAMERA_MESSAGE, "Info");
    }


  }

  void deleteConfirmation(final String docId) {
    new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Confirmation")
        .setMessage("Are you sure you want to delete the record?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
//            schoolCensus.setState(Constants.SCHOOL_DATA_LIST);
//            schoolCensus.deleteDocumentCloudant(docId);
          }
        })
        .setNegativeButton("No", null)
        .show();
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

  }

  @Override
  public void onResume() {
    super.onResume();

  }

  public void setTakenImage(Bitmap originalImage, String bitmapImageString) {
    bitmapFaceImageString = bitmapImageString;

    Resources res = getResources();
    RoundedBitmapDrawable dr =
        RoundedBitmapDrawableFactory.create(res, originalImage);
    //dr.setCornerRadius(Math.max(originalImage.getWidth(), originalImage.getHeight()) / 2.0f);
    dr.setCircular(true);
    imageView.setImageDrawable(dr);

//    facePhotoView.setImageDrawable(dr);

    if (cameraViewDialog != null) {
      cameraViewDialog.dismiss();
    }

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

  private void getClassData() {

    try {

//      SchoolResponse schoolResponse = schoolCensus.getCurrentSchool();

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
//      comboBoxViewAdapter.setHint("Select Class");
//      spinner.setAdapter(comboBoxViewAdapter);
//
//      if (classArray.length > 0) {
//        reloadStreamsData(classArray[0].getText());
//      }

    } catch (Exception ex) {
      ex.printStackTrace();

    }

    genderArray = new ComboBoxViewListItem[2];

    genderArray[0] =
        new ComboBoxViewListItem("" + 0, "Male",
                                 "");
    genderArray[1] =
        new ComboBoxViewListItem("" + 0, "Female",
                                 "");

    genderViewAdapter = new ComboBoxViewAdapter(getApplicationContext(),
                                                android.R.layout.simple_spinner_item,
                                                genderArray);

    spinnerGender.setOnTouchListener(onGenderTouchListener);

    genderViewAdapter.setHint("Select Gender");
    spinnerGender.setAdapter(genderViewAdapter);


  }

  private void reloadStreamsData(String className) {

    classSelected = false;
//    SchoolResponse schoolResponse = schoolCensus.getCurrentSchool();

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

    comboBoxViewAdapterStreams = new ComboBoxViewAdapter(getApplicationContext(),
                                                         android.R.layout.simple_spinner_item,
                                                         classStreams);
    spinnerStreams.setOnTouchListener(onStreamsTouchListener);

    comboBoxViewAdapterStreams.setHint("Select Stream");

    spinnerStreams.setAdapter(comboBoxViewAdapterStreams);
  }

  public void showBackButton() {

    backButton.setImageResource(R.drawable.school_back_black);
    backButton.setVisibility(View.VISIBLE);
    backButton.setOnClickListener(this);

//    deleteButton.setOnClickListener(this);
//    deleteButton.setVisibility(View.VISIBLE);

  }

  public void showDeleteButton() {
//    deleteButton.setOnClickListener(this);
//    deleteButton.setVisibility(View.VISIBLE);

  }

  private class OnClassSelected implements
                                AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

      if (comboBoxViewListItems != null) {
        selectedLesson = comboBoxViewListItems[pos].getText();
      }

    }

    public void onNothingSelected(AdapterView parent) {
      // Do nothing.
    }
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

  private class OnGenderSelected implements
                                 AdapterView.OnItemSelectedListener {


    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

//      selectedStream = classStreams[pos].getText();
    }

    public void onNothingSelected(AdapterView parent) {
      // Do nothing.
    }
  }
}
