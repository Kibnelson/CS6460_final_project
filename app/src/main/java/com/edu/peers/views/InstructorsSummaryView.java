package com.edu.peers.views;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.managers.UserManager;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.models.UserStatistics;
import com.edu.peers.others.Base64;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Utils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by nelson on 07/03/2017.
 */

public class InstructorsSummaryView extends Fragment implements
                                                  View.OnClickListener,
                                                  AdapterView.OnItemClickListener {

  public ComboBoxViewListItem[] classArray;
  public ComboBoxViewListItem[] comboBoxViewListItems;
  private ArrayList<String> sampleDataId = new ArrayList<>();
  private ArrayList<String> sampleDataRevId = new ArrayList<>();
  private SchoolCensus schoolCensus;
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
  private TextView studentName;
  private TextView tvX;
  private TextView tvY;
  private SeekBar mSeekBarX;
  private SeekBar mSeekBarY;
  private Typeface mTf;
  //  private Students selectedStudent;
  private TextView gender,role;
  private ImageView backButton;
  private MainView  mainView;
  private ArrayList<ComboBoxViewListItem> gridData = new ArrayList<>();
  private GridView gridView;
  private String classname;
  private ImageView editStudent;
  private FragmentTransaction fragmentTransaction;
  private BarChart perfomanceBarChart;
  private XAxis xAxis;
  private BarChart teachersBarChart;
  private CombinedChart cChart;
  private ArrayList<String> xValues;
  private ImageView imageView;
  private UserObject userObject;
  private ArrayList<Entry> quizzesDoneEntries,quizzesCreatedEntries,questionsAskedEntries,questionsAnsweredEntries,contentUploadEntries;
  private ImageView userEdit;
  private UserManager userManager;
  private UserObject userObjectList;
  private String username,passwordFieldStr;

  public static InstructorsSummaryView newInstance(int position) {
    InstructorsSummaryView f = new InstructorsSummaryView();
    Bundle b = new Bundle();
    f.setArguments(b);
    return f;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.mHandler = new Handler(Looper.getMainLooper());
    schoolCensus = (SchoolCensus) getActivity().getApplication();
    schoolCensus.setCurrentFragment(this);
    schoolCensus.setState(Constants.STUDENT_SUMMARY_VIEW);
    schoolCensus.setCurrentTitle(Constants.InstructorsSummaryView);
    userObject= schoolCensus.getUserObject();
    username= userObject.getUser().getUsername();
    passwordFieldStr= userObject.getUser().getPassword();
    mainView = schoolCensus.getMainView();
    mainView.enableMenuDrawer();
    mainView.hideAddButton();
    mainView.setTitle("Edu Peer > Dashboard");
    userManager = new UserManager(schoolCensus.getCloudantInstance(), getContext());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.student_summary_view, null);

    final Calendar nextYear = Calendar.getInstance();
    nextYear.add(Calendar.YEAR, 1);


    progressBar = (LinearLayout) view.findViewById(R.id.progressBarSchools);
    progressBar.setVisibility(View.GONE);


    studentName = (TextView) view.findViewById(R.id.studentName);
    gender = (TextView) view.findViewById(R.id.gender);
    role = (TextView) view.findViewById(R.id.role);

    userEdit= (ImageView) view.findViewById(R.id.userEdit);
    userEdit.setOnClickListener(this);
    imageView = (ImageView) view
        .findViewById(R.id.imageView);

    cChart = (CombinedChart) (CombinedChart) view.findViewById(R.id.combinedChart);
    cChart.setDescription("");
    cChart.setBackgroundColor(Color.WHITE);
    cChart.setDrawGridBackground(false);
    cChart.setDrawBarShadow(false);
    cChart.setDrawOrder(
        new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.LINE,
                                      CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.LINE,
                                      CombinedChart.DrawOrder.LINE});

    YAxis rightAxis = cChart.getAxisRight();
    rightAxis.setDrawGridLines(false);

    YAxis leftAxis = cChart.getAxisLeft();
    leftAxis.setDrawGridLines(true);
    Legend l = cChart.getLegend();

    // modify the legend ...
    l.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
    l.setForm(Legend.LegendForm.SQUARE);
//    l.setTypeface(tf);
    l.setTextSize(11f);
    l.setTextColor(Color.BLACK);

    ////
    LimitLine ll1 = new LimitLine(50, "Doing well");
    ll1.setLineWidth(4f);
    ll1.enableDashedLine(10f, 10f, 0f);
    ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
    ll1.setTextSize(10f);

    LimitLine ll2 = new LimitLine(10f, "Improvement needed");
    ll2.setLineWidth(4f);
    ll2.enableDashedLine(10f, 10f, 0f);
    ll2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
    ll2.setTextSize(10f);
//
//    YAxis leftAxis = weightLineChart.getAxisLeft();
    leftAxis.removeAllLimitLines();
    leftAxis.addLimitLine(ll1);
    leftAxis.addLimitLine(ll2);
    leftAxis.setAxisMaxValue(60f);
    leftAxis.setAxisMinValue(0f);
//    leftAxis.setStartAtZero(false);
//    leftAxis.enableGridDashedLine(10f, 10f, 0f);
//    leftAxis.setDrawLimitLinesBehindData(true);

    cChart.getAxisRight().setDrawLabels(false);
    cChart.getAxisRight().setDrawAxisLine(true);

    XAxis xAxis = cChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setDrawAxisLine(true);
    xAxis.setDrawGridLines(true);
    xAxis.setSpaceBetweenLabels(0);
    xAxis.resetLabelsToSkip();


    new backgroundProcess().execute();


    return view;
  }

  public void populatechart(){

    List<Date> dateList= new ArrayList<>();

    dateList.add(new DateTime().minusDays(5).toDate());

    dateList.add(new DateTime().toDate());

    populateUserData(userObject);

    drawChart(true,true,true,true,true);


  }

  private void drawChart( Boolean quizzesDoneBool, Boolean quizzesCreatedBool, Boolean questionsAskedBool,Boolean questionsAnsweredBool, Boolean contentUploadBool
  ) {



    List<UserStatistics> quizzesDone = userObject.getUser().getQuizzesDone();
    List<UserStatistics> quizzesCreated = userObject.getUser().getQuizzesCreated();
    List<UserStatistics> questionsAsked = userObject.getUser().getQuestionsAsked();
    List<UserStatistics> questionsAnswered = userObject.getUser().getQuestionsAnswered();
    List<UserStatistics> contentUpload = userObject.getUser().getContentUpload();


    quizzesDoneEntries = new ArrayList<Entry>();
    quizzesCreatedEntries = new ArrayList<Entry>();
    questionsAskedEntries = new ArrayList<Entry>();
    questionsAnsweredEntries = new ArrayList<Entry>();
    contentUploadEntries = new ArrayList<Entry>();

    xValues=new ArrayList<>();

    Log.i(Constants.TAG, "quizzesDone=========" + quizzesDone.size());
    Log.i(Constants.TAG,"quizzesCreated========="+quizzesCreated.size());
    Log.i(Constants.TAG,"questionsAsked========="+questionsAsked.size());
    Log.i(Constants.TAG,"questionsAnswered========="+questionsAnswered.size());
    Log.i(Constants.TAG,"contentUpload========="+contentUpload.size());


    for (int y = 0; y < quizzesDone.size(); y++) {
      UserStatistics weightValue = quizzesDone.get(y);
        xValues.add(Utils.convert(weightValue.date));
      quizzesDoneEntries.add(  new Entry((weightValue.value.value), y));
    }

    for (int y = 0; y < quizzesCreated.size(); y++) {
      UserStatistics weightValue = quizzesCreated.get(y);
        xValues.add(Utils.convert(weightValue.date));
      quizzesCreatedEntries.add(  new Entry((weightValue.value.value), y));
    }

    for (int y = 0; y < questionsAsked.size(); y++) {
      UserStatistics weightValue = questionsAsked.get(y);
        xValues.add(Utils.convert(weightValue.date));
      questionsAskedEntries.add(new Entry((weightValue.value.value), y));
    }

    for (int y = 0; y < questionsAnswered.size(); y++) {
      UserStatistics weightValue = questionsAnswered.get(y);
       xValues.add(Utils.convert(weightValue.date));
       questionsAnsweredEntries.add(new Entry((weightValue.value.value), y));
    }

    for (int y = 0; y < contentUpload.size(); y++) {
      UserStatistics weightValue = contentUpload.get(y);
       xValues.add(Utils.convert(weightValue.date));
       contentUploadEntries.add(new Entry((weightValue.value.value), y));
    }

    CombinedData data = new CombinedData(xValues);
    data.setData(addLineData( quizzesDoneBool,  quizzesCreatedBool,  questionsAskedBool, questionsAnsweredBool,  contentUploadBool));
    cChart.setData(data);
    cChart.invalidate();

  }


  private LineData addLineData(Boolean quizzesDoneBool, Boolean quizzesCreatedBool, Boolean questionsAskedBool, Boolean questionsAnsweredBool, Boolean contentUploadBool) {

    LineData d = new LineData();

    if (quizzesDoneBool) {
      d.addDataSet(generateQuizzesDoneData());
    }

    if (quizzesCreatedBool) {
      d.addDataSet(quizzesCreatedData());
    }

    if (questionsAskedBool) {
      d.addDataSet(questionsAskedData());
    }

    if (questionsAnsweredBool) {
      d.addDataSet(questionsAnsweredData());
    }

    if (contentUploadBool) {
      d.addDataSet(contentUploadData());
    }

    return d;
  }
  private LineDataSet quizzesCreatedData() {

    LineDataSet set = new LineDataSet(quizzesCreatedEntries, "Quiz Created");
    set.setColor(Color.GREEN);
    set.setLineWidth(2.5f);
    set.setCircleColor(Color.GREEN);
    set.setCircleSize(5f);
    set.setFillColor(Color.GREEN);
    set.setDrawCubic(true);
    set.setDrawValues(true);
    set.setValueTextSize(10f);
    set.setValueTextColor(Color.GREEN);

    set.setAxisDependency(YAxis.AxisDependency.LEFT);

    return set;
  }

  private LineDataSet questionsAskedData() {

    LineDataSet set = new LineDataSet(questionsAskedEntries, "Questions Asked");
    set.setColor(Color.BLUE);
    set.setLineWidth(2.5f);
    set.setCircleColor(Color.BLUE);
    set.setCircleSize(5f);
    set.setFillColor(Color.BLUE);
    set.setDrawCubic(true);
    set.setDrawValues(true);
    set.setValueTextSize(10f);
    set.setValueTextColor(Color.BLUE);

    set.setAxisDependency(YAxis.AxisDependency.LEFT);

    return set;
  }


  private LineDataSet questionsAnsweredData() {


    LineDataSet set = new LineDataSet(questionsAnsweredEntries, "Questions Answered");
    set.setColor(Color.BLACK);
    set.setLineWidth(2.5f);
    set.setCircleColor(Color.BLACK);
    set.setCircleSize(5f);
    set.setFillColor(Color.BLACK);
    set.setDrawCubic(true);
    set.setDrawValues(true);
    set.setValueTextSize(10f);
    set.setValueTextColor(Color.BLACK);

    set.setAxisDependency(YAxis.AxisDependency.LEFT);

    return set;
  }

  private LineDataSet contentUploadData() {


    LineDataSet set = new LineDataSet(contentUploadEntries, "Content Uploaded");
    set.setColor(Color.YELLOW);
    set.setLineWidth(2.5f);
    set.setCircleColor(Color.YELLOW);
    set.setCircleSize(5f);
    set.setFillColor(Color.YELLOW);
    set.setDrawCubic(true);
    set.setDrawValues(true);
    set.setValueTextSize(10f);
    set.setValueTextColor(Color.YELLOW);

    set.setAxisDependency(YAxis.AxisDependency.LEFT);

    return set;
  }

  private LineDataSet generateQuizzesDoneData() {

    LineDataSet set = new LineDataSet(quizzesDoneEntries, "Quizzes Done");
    set.setColor(Color.RED);
    set.setLineWidth(2.5f);
    set.setCircleColor(Color.RED);
    set.setCircleSize(5f);
    set.setFillColor(Color.RED);
    set.setDrawCubic(true);
    set.setDrawValues(true);
    set.setValueTextSize(10f);
    set.setValueTextColor(Color.RED);
    set.setAxisDependency(YAxis.AxisDependency.LEFT);

    return set;
  }


  @Override
  public void onClick(View v) {

    if (v == userEdit) {

      userObject.setEditObject(true);
      schoolCensus.setUserObject(userObject);

      Intent intent = new Intent(getContext(), RegistrationView.class);

      startActivity(intent);

    } else {
      mainView.onBackPressed();
    }

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

  }



  public void populateUserData(UserObject userObject) {

    if (userObject.getUser().getFacephoto()!=null && userObject.getUser().getFacephoto().length()>0) {

      try {
        byte[] data = Base64.decode(userObject.getUser().getFacephoto());
        Bitmap profileImage = BitmapFactory.decodeByteArray(data, 0, data.length);

        Resources res = getResources();
        RoundedBitmapDrawable dr =
            RoundedBitmapDrawableFactory.create(res, profileImage);
        dr.setCircular(true);
        imageView.setImageDrawable(dr);
//        patientImage.setImageBitmap(profileImage);
        imageView.setVisibility(View.VISIBLE);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    studentName.setText(userObject.getUser().getFirstName()+" "+userObject.getUser().getLastName());
    gender.setText(userObject.getUser().getGender());
    role.setText(userObject.getUser().getRole());


  }
  @Override
  public void onResume() {
    super.onResume();
    schoolCensus.setCurrentTitle(Constants.InstructorsSummaryView);
    mainView.showMenuDrawer();
    mainView.hideAddButton();
    mainView.setTitle("Edu Peer > Dashboard");
    userObject= schoolCensus.getUserObject();
    populateUserData(userObject);
  }

  @Override
  public void onPause() {
    super.onPause();

    mainView.showMenuDrawer();

  }

  public void loadView(Fragment currentFragment) {

    fragmentTransaction = getFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.main, currentFragment).addToBackStack("studentSummaryView")
        .commit();

  }

  private BarData setData(int count, float range) {

    String[] mMonths = new String[]{
        "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008"
    };

    ArrayList<String> xVals = new ArrayList<String>();
    for (int i = 0; i < count; i++) {
      xVals.add(mMonths[i % 12]);
    }

    ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

    for (int i = 0; i < count; i++) {
      float mult = (range + 1);
      float val = (float) (Math.random() * mult);
      yVals1.add(new BarEntry(val, i));
    }

    BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
    set1.setBarSpacePercent(35f);
    set1.setColor(Color.parseColor("#007670"));
    set1.setHighLightColor(Color.parseColor("#000000"));

    set1.setDrawValues(false);

    ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
    dataSets.add(set1);

    BarData data = new BarData(xVals, dataSets);
    // data.setValueFormatter(new MyValueFormatter());
    data.setValueTextSize(10f);
    data.setValueTypeface(mTf);

    return data;
  }

  public UserObject getUserObject() {
    return userObject;
  }

  public void setUserObject(UserObject userObject) {
    this.userObject = userObject;
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
  private class backgroundProcess extends AsyncTask<Integer, Integer, Long> {


    protected Long doInBackground(Integer... params) {

      userObjectList = userManager.getDocumentGetDocument(Constants.USERS);

      if (userObjectList!=null){
        int size=userObjectList.getUserList().size();
        for (int y=0;y<size;y++){
          User user=userObjectList.getUserList().get(y);
          if (user.getUsername().equalsIgnoreCase(username)){
            userObjectList.setPosition(y);
            userObjectList.setUser(user);
          }

        }
      }

      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {

      if (progressBar != null) {
        progressBar.setVisibility(View.GONE);
      }

      if (userObjectList != null && userObjectList.getUser().getPassword()
          .equalsIgnoreCase(passwordFieldStr)) {

        schoolCensus.setUserObject(userObjectList);

        userObject=userObjectList;

      }

      populatechart();
    }

    protected void onPreExecute() {
      if (progressBar != null) {
        progressBar.setVisibility(View.VISIBLE);
      }
    }



  }

}
