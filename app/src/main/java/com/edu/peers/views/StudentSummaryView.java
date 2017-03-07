package com.edu.peers.views;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.others.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by nelson on 3/16/15.
 */
public class StudentSummaryView extends Fragment implements
                                                 View.OnClickListener,
                                                 AdapterView.OnItemClickListener {

  private static String ARG_POSITION;
  public ComboBoxViewListItem[] classArray;
  public ComboBoxViewListItem[] comboBoxViewListItems;
  //  private ArrayList<SchoolResponse> array_sort = new ArrayList<>();
//  private ArrayList<SchoolResponse> sampleData = new ArrayList<>();
  private ArrayList<String> sampleDataId = new ArrayList<>();
  private ArrayList<String> sampleDataRevId = new ArrayList<>();
  private SchoolCensus schoolCensus;
  private TextView title;
  //  private ArrayList<SchoolResponse> schoolResponseArrayList;
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
  //  private Students selectedStudent;
  private TextView studentGender;
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

  public static StudentSummaryView newInstance(int position) {
    StudentSummaryView f = new StudentSummaryView();
    Bundle b = new Bundle();
    b.putInt(ARG_POSITION, position);
    f.setArguments(b);
    return f;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.mHandler = new Handler(Looper.getMainLooper());

    int position = getArguments().getInt(ARG_POSITION);

    schoolCensus = (SchoolCensus) getActivity().getApplication();
    schoolCensus.setCurrentFragment(this);
    schoolCensus.setState(Constants.STUDENT_SUMMARY_VIEW);

    mainView = schoolCensus.getMainView();
    mainView.enableMenuDrawer();

//    schoolHomeGridView = schoolCensus.getSchoolDashboardView();
//    schoolHomeGridView.showMenuDrawer();
//    schoolDashboardView.setTag(2);
//    schoolDashboardView.showDeleteButton();
//    SchoolResponse schoolResponse = schoolCensus.getCurrentSchool();

//    selectedStudent =
//        schoolResponse.getSchool().getStudents().get(schoolCensus.getSelectedStudentIndex());

//    signInResponse = schoolCensus.getSignInResponse();

//    student = schoolCensus.getEnrolment().students;
//    student  = signInResponse.getSchools().get(schoolCensus.getSelectedSchoolIndex())
//        .getClassroomses().get(schoolCensus.getSelectedClassroomIndex()).getStreamses().get(schoolCensus.getSelectedClassroomStreamIndex()).getStudents().get(schoolCensus.getSelectedStudentIndex());

//
//
//
//    classname=signInResponse.getSchools().get(schoolCensus.getSelectedSchoolIndex())
//        .getClassroomses().get(schoolCensus.getSelectedClassroomIndex()).name+" "+signInResponse.getSchools().get(schoolCensus.getSelectedSchoolIndex())
//        .getClassroomses().get(schoolCensus.getSelectedClassroomIndex()).getClassStreams().get(schoolCensus.getSelectedClassroomStreamIndex()).name;

//    schoolHomeGridView
//        .setTitle(student.personDetails.first_name + " " + student.personDetails.last_name);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.student_summary_view, null);

//    editStudent = (ImageView) view.findViewById(R.id.editStudent);
//    editStudent.setOnClickListener(this);

    final Calendar nextYear = Calendar.getInstance();
    nextYear.add(Calendar.YEAR, 1);

//    pickdate = (Button) view.findViewById(R.id.pickdate);
//
//    pickdate.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        dialogView =
//            (CalendarPickerView) getLayoutInflater(getArguments())
//                .inflate(R.layout.school_calendar_view_dialog, null, false);
//
//        Date today = new Date();
//        dialogView.init(new Date(), nextYear.getTime()) //
//            .inMode(CalendarPickerView.SelectionMode.RANGE) //
//            .withSelectedDate(today);
//
//        theDialog =
//            new AlertDialog.Builder(getActivity()).setTitle("Select Date Range")
//                .setView(dialogView)
//                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
//                  @Override
//                  public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                    selectedDates = dialogView.getSelectedDates();
//
//                    setSelectedDates(selectedDates);
//
//                  }
//
//
//                })
//                .create();
//        theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//          @Override
//          public void onShow(DialogInterface dialogInterface) {
//            Log.d("TAG", "onShow: fix the dimens!");
//            dialogView.fixDialogDimens();
//          }
//        });
//        theDialog.show();
//      }
//    });

//    spinner = (Spinner) view.findViewById(R.id.students_class);
//
//    spinner.setOnItemSelectedListener(new OnClassSelected());
//
//    comboBoxViewListItems = new ComboBoxViewListItem[2];
//
//    comboBoxViewListItems[0] = new ComboBoxViewListItem("1", "Science", "Science");
//
//    comboBoxViewListItems[1] = new ComboBoxViewListItem("1", "Maths", "Math");
//
//    comboBoxViewAdapter = new ComboBoxViewAdapter(getActivity(),
//                                                  android.R.layout.simple_spinner_item,
//                                                  comboBoxViewListItems);
//
//    spinner.setAdapter(comboBoxViewAdapter);
//    spinner.setSelection(0);

    studentName = (TextView) view.findViewById(R.id.studentName);
    studentGender = (TextView) view.findViewById(R.id.studentGender);
    studentClass = (TextView) view.findViewById(R.id.studentClass);

//    studentName.setText(student.personDetails.first_name + " " + student.personDetails.last_name);
//    studentGender.setText(student.personDetails.sex);
//    studentClass.setText("Reg No: " + student.studentId + " | ");

//    editStudent = (ImageView) view
//        .findViewById(R.id.editStudent);

//    editStudent.setOnClickListener(this);
    ImageView imageView = (ImageView) view
        .findViewById(R.id.profileImage);

    perfomanceBarChart = (BarChart) view.findViewById(R.id.performanceIndicatorBarChart);

    perfomanceBarChart.setDescription("");
// scaling can now only be done on x- and y-axis separately
    perfomanceBarChart.setPinchZoom(false);
    perfomanceBarChart.setDrawBarShadow(false);
    perfomanceBarChart.setDrawGridBackground(false);
    xAxis = perfomanceBarChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//    xAxis.setLabelsToSkip(0);
    xAxis.setDrawGridLines(false);
    perfomanceBarChart.getAxisLeft().setDrawGridLines(false);
    perfomanceBarChart.getLegend().setEnabled(false);
    perfomanceBarChart.setHighlightIndicatorEnabled(false);

    perfomanceBarChart.getAxisRight().setDrawAxisLine(false);
    perfomanceBarChart.getAxisRight().setDrawLabels(false);
    perfomanceBarChart.getAxisRight().setDrawAxisLine(false);

    perfomanceBarChart.setData(setData(5, 4));
//    perfomanceBarChart.setOnChartValueSelectedListener(this);


//    teachersBarChart.setOnChartValueSelectedListener(this);

//    String image = selectedStudent.getFacePhoto();
//
//    if (image != null) {
//      try {
////        byte[] bitmapImageString=android.util.Base64.decode(item.getFacePhoto(),android.util.Base64.DEFAULT);
//        byte[] bitmapImageString = Base64.decode(image);
//        Bitmap
//            originalImage =
//            BitmapFactory.decodeByteArray(bitmapImageString, 0, bitmapImageString.length);
////        imageView.setImageBitmap(originalImage);
//
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }

//    gridData.clear();
//    gridData.add(new ComboBoxViewListItem("1", "% Present", "75%"));
//    gridData.add(new ComboBoxViewListItem("1", "% Absent", "65%"));
//
//    gridView = (GridView) view.findViewById(R.id.studentSummaryGridview);
//
//    gridView.setAdapter(new ReportsGridViewAdapter(this, gridData));
//
//    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//      public void onItemClick(AdapterView<?> parent, View v,
//                              int position, long id) {
//
//        v.setSelected(true);
//      }
//    });
//
//    new Handler().post(new Runnable() {
//      @Override
//      public void run() {
//        gridView.performItemClick(
//            gridView.getChildAt(0),
//            0,
//            gridView.getAdapter().getItemId(0));
//      }
//    });
//
//    // Get a reference for the week view in the layout.
//    mWeekView = (WeekView) view.findViewById(R.id.weekView);
//
//    // Show a toast message about the touched event.
//    mWeekView.setOnEventClickListener(this);
//
//    // The week view has infinite scrolling horizontally. We have to provide the events of a
//    // month every time the month changes on the week view.
//    mWeekView.setMonthChangeListener(this);
//
//    // Set long press listener for events.
//    mWeekView.setEventLongPressListener(this);
//
//    // Set up a date time interpreter to interpret how the date and time will be formatted in
//    // the week view. This is optional.
//    setupDateTimeInterpreter(false);

//     mChart = (BarChart) view.findViewById(R.id.chart);
//    mChart = (LineChart) view.findViewById(R.id.chart);
//    tvX = (TextView) view.findViewById(R.id.tvXMax);
//    tvY = (TextView) view.findViewById(R.id.tvYMax);
//
//    mSeekBarX = (SeekBar) view.findViewById(R.id.seekBar1);
//    mSeekBarY = (SeekBar) view.findViewById(R.id.seekBar2);
//
//    mSeekBarX.setProgress(45);
//    mSeekBarY.setProgress(100);
//
////    mSeekBarY.setOnSeekBarChangeListener(this);
////    mSeekBarX.setOnSeekBarChangeListener(this);
//
////    mChart.setOnChartGestureListener(this);
////    mChart.setOnChartValueSelectedListener(this);
//
//    // no description text
//    mChart.setDescription("");
//    mChart.setNoDataTextDescription("You need to provide data for the chart.");
//
//    // enable value highlighting
//    mChart.setHighlightEnabled(true);
//
//    // enable touch gestures
//    mChart.setTouchEnabled(true);
//
//    // enable scaling and dragging
//    mChart.setDragEnabled(true);
//    mChart.setScaleEnabled(true);
//    // mChart.setScaleXEnabled(true);
//    // mChart.setScaleYEnabled(true);
//
//    // if disabled, scaling can be done on x- and y-axis separately
//    mChart.setPinchZoom(true);
//
//    // set an alternative background color
//    // mChart.setBackgroundColor(Color.GRAY);
//
//    // create a custom MarkerView (extend MarkerView) and specify the layout
//    // to use for it
////    MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
////
////    // set the marker to the chart
////    mChart.setMarkerView(mv);
//
//    // enable/disable highlight indicators (the lines that indicate the
//    // highlighted Entry)
//    mChart.setHighlightIndicatorEnabled(false);
//
//    // x-axis limit line
////        LimitLine llXAxis = new LimitLine(10f, "Index 10");
////        llXAxis.setLineWidth(4f);
////        llXAxis.enableDashedLine(10f, 10f, 0f);
////        llXAxis.setLabelPosition(LimitLabelPosition.POS_RIGHT);
////        llXAxis.setTextSize(10f);
////
////        XAxis xAxis = mChart.getXAxis();
////        xAxis.addLimitLine(llXAxis);
//
//    LimitLine ll1 = new LimitLine(130f, "Upper Limit");
//    ll1.setLineWidth(4f);
//    ll1.enableDashedLine(10f, 10f, 0f);
//    ll1.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
//    ll1.setTextSize(10f);
//
//    LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//    ll2.setLineWidth(4f);
//    ll2.enableDashedLine(10f, 10f, 0f);
//    ll2.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
//    ll2.setTextSize(10f);
//
//    YAxis leftAxis = mChart.getAxisLeft();
//    leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//    leftAxis.addLimitLine(ll1);
//    leftAxis.addLimitLine(ll2);
//    leftAxis.setAxisMaxValue(220f);
//    leftAxis.setAxisMinValue(-50f);
//    leftAxis.setStartAtZero(false);
//    leftAxis.enableGridDashedLine(10f, 10f, 0f);
//
//    // limit lines are drawn behind data (and not on top)
//    leftAxis.setDrawLimitLinesBehindData(true);
//
//    mChart.getAxisRight().setEnabled(false);
//
//    // add data
//    setData(45, 100);
//
////        mChart.setVisibleXRange(20);
////        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
////        mChart.centerViewTo(20, 50, AxisDependency.LEFT);
//
////    mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
////        mChart.invalidate();
//
//    // get the legend (only possible after setting data)
//    Legend l = mChart.getLegend();
//
//    // modify the legend ...
//    // l.setPosition(LegendPosition.LEFT_OF_CHART);
//    l.setForm(Legend.LegendForm.LINE);
//
//    // // dont forget to refresh the drawing
//    // mChart.invalidate();

    return view;
  }

  private void setSelectedDates(List<Date> selectedDates) {

    StringBuilder dateRange = new StringBuilder();

    if (selectedDates.size() > 0) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

      dateRange.append(dateFormat.format(selectedDates.get(0)).toString().toString());
      dateRange.append(" - ");
      dateRange.append(
          dateFormat.format(selectedDates.get(selectedDates.size() - 1)).toString().toString());

    }

    pickdate.setText(dateRange);


  }

  @Override
  public void onClick(View v) {

    if (v == editStudent) {

//      StudentRegistrationView studentRegistrationView = StudentRegistrationView.newInstance(1);
//      studentRegistrationView.updateRecord = true;
//      loadView(studentRegistrationView);

    } else {
      mainView.onBackPressed();
    }

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//    schoolCensus.setCurrentSchool(
//        sampleData.get(position).getSchool().getSchoolDetailsObject().getSchoolName());
//    schoolCensus.setCurrentSchoolId(sampleDataId.get(position));
//    schoolCensus.setSchoolRevId(sampleDataRevId.get(position));

//    Intent ivE = new Intent(this, SchoolDashboardView.class);
//
//    startActivity(ivE);

  }


  @Override
  public void onResume() {
    super.onResume();
    schoolCensus.setCurrentTitle(Constants.StudentSummaryView);
//    schoolHomeGridView.showMenuDrawer();
  }

  @Override
  public void onPause() {
    super.onPause();

    mainView.showMenuDrawer();

  }

  public void deleteEntry() {
//    SchoolResponse schoolResponse = schoolCensus.getCurrentSchool();

    schoolCensus.setState(Constants.STUDENT_GRID_VIEW);


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

}
