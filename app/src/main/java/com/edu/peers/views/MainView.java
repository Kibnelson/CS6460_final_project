package com.edu.peers.views;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.adapter.MainViewExpandableListViewAdapter;
import com.edu.peers.cloudant.EventAggregator;
import com.edu.peers.managers.NotificationManager;
import com.edu.peers.managers.UserManager;
import com.edu.peers.models.MenuObject;
import com.edu.peers.models.NotificationObject;
import com.edu.peers.models.Notifications;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.others.Constants;
import com.edu.peers.services.BackgroundService;
import com.edu.peers.services.ServiceCallbacks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by nelson on 7/21/15.
 */

public class MainView extends FragmentActivity
    implements View.OnClickListener, AdapterView.OnItemClickListener,
               ExpandableListView.OnChildClickListener,
               ExpandableListView.OnGroupClickListener, ServiceCallbacks {


  final String schoolMainList[] = {
      "SCHOOL LIST"
  };
  public ComboBoxViewListItem[] subCountiesList, measuresList;
  public String[] schools;
  public Marker[] markers;
  public boolean spinnerCountyTouched, spinnerMeasuresTouched;
  public Button refreshButton, editButton1, transferButton;
  private ArrayList<String> menuData = new ArrayList<>();
  private Fragment currentFragment = null;
  private ComboBoxViewAdapter subCountyViewAdapter, measuresViewAdapter;
  private String selectedCounty, selectedMeasure;
  private Spinner spinnerCounty;
  private AutoCompleteTextView searchField;
  private FragmentTransaction fragmentTransaction;
  private SchoolCensus schoolCensus;
  private Button addButton;
  private Button logoutButton;
  private LinearLayout addButtonDivider;
  private ImageView menuDrawerButton;
  private Button editButton;
  private TextView title;
  private List<MenuObject> schoolList;
  private int mStackLevel;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private ExpandableListView mapListview;
  private BackgroundService backgroundService;
  private boolean bound = false;
  private GoogleMap googleMap;
  private ListView navDrawerList;
  private ExpandableListView expandableListView;
  private Button countyReportButton;
  private Spinner spinnerMeasures;
  private ProgressBar downloadProgress;
  private ImageView saveButton;
  private boolean mIsCameraReady = false;
  private boolean mIsPermissionDenied = false;
  private View.OnTouchListener onCountyMeasuresTouchListener = new View.OnTouchListener() {


    @Override
    public boolean onTouch(View v, MotionEvent event) {

      spinnerMeasuresTouched = true;
      ((ComboBoxViewAdapter) subCountyViewAdapter).setSelected(true);

      ((ComboBoxViewAdapter) subCountyViewAdapter).notifyDataSetChanged();
      return false;
    }
  };
  private ProgressDialog progressDialog;
  private EventAggregator eventAggregator;
  private UserObject userObject;
  private Button notifCount;
  private int mNotifCount = 0;
  private RelativeLayout main_widget;
  private ImageView icon;
  private int userView = -1;
  private ScheduledExecutorService scheduledExecutorService;
  private NotificationManager notificationManager;
  private UserManager userManager;
  private int totalNotifications = 0;
  private TextView txt_count;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.school_map);

    schoolCensus = (SchoolCensus) getApplication();
    schoolCensus.setMainView(this);

    notificationManager =
        new NotificationManager(schoolCensus.getCloudantInstance(), getApplicationContext());
    userManager =
        new UserManager(schoolCensus.getCloudantInstance(), getApplicationContext());

    main_widget = (RelativeLayout) findViewById(R.id.main_widget);

    icon = (ImageView) findViewById(R.id.icon);
    icon.setOnClickListener(this);

    refreshButton = (Button) findViewById(R.id.refresh);
    refreshButton.setVisibility(View.GONE);
    refreshButton.setOnClickListener(this);

    transferButton = (Button) findViewById(R.id.transfer);
    transferButton.setVisibility(View.GONE);
    transferButton.setOnClickListener(this);

    editButton1 = (Button) findViewById(R.id.edit);
    editButton1.setVisibility(View.GONE);
    editButton1.setOnClickListener(this);

    editButton = (Button) findViewById(R.id.editItem);
    editButton.setVisibility(View.GONE);
    editButton.setOnClickListener(this);

    addButton = (Button) findViewById(R.id.add);
    addButton.setVisibility(View.GONE);
    addButton.setOnClickListener(this);

    saveButton = (ImageView) findViewById(R.id.save);
    saveButton.setVisibility(View.GONE);
    saveButton.setOnClickListener(this);

    addButton = (Button) findViewById(R.id.add);
    addButton.setVisibility(View.GONE);
    addButton.setOnClickListener(this);

    downloadProgress = (ProgressBar) findViewById(R.id.downloadProgress);

    downloadProgress.setVisibility(View.GONE);
    downloadProgress.setOnClickListener(this);

    fragmentTransaction = getSupportFragmentManager().beginTransaction();
    logoutButton = (Button) findViewById(R.id.logout);
    logoutButton.setOnClickListener(this);
    logoutButton.setVisibility(View.VISIBLE);
    addButtonDivider = (LinearLayout) findViewById(R.id.add_divider);

    title = (TextView) findViewById(R.id.title);
    txt_count = (TextView) findViewById(R.id.txt_count);

    schoolList = new ArrayList<>();
//    schoolList.add();

    setHomeTitle();

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    menuDrawerButton = (ImageView) findViewById(R.id.menu_drawer);

    menuDrawerButton.setOnClickListener(this);

    mapListview = (ExpandableListView) findViewById(R.id.mapSchoolList);

    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    menuDrawerButton.setVisibility(View.GONE);

    countyReportButton = (Button) findViewById(R.id.countyReportsButton);

    countyReportButton.setOnClickListener(this);

//    Intent intent = new Intent(this, BackgroundService.class);
//
//    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
//
//    startService(new Intent(this, BackgroundService.class));

    // schoolCensus.setSelectedSchoolIndex(0);

    menuData.clear();

    userObject = schoolCensus.getUserObject();

    if (userObject.getUser().getRole().equalsIgnoreCase(Constants.INSTRUCTOR_ROLE)) {

      userView = 1;
      menuData.add("Dashboard");
      menuData.add("Quizzes");
      menuData.add("Public Questions");
      menuData.add("Private Questions");
      menuData.add("Students Grade Book");
      menuData.add("Learning material");
      menuData.add("General Notifications");
      menuData.add("Personal Notifications");
      menuData.add("Seat arrangement");
      menuData.add("Ranking");

    } else if (userObject.getUser().getRole().equalsIgnoreCase(Constants.STUDENT_ROLE)) {
      userView = 2;
      menuData.add("Dashboard");
      menuData.add("Quizzes");
      menuData.add("Public Questions");
      menuData.add("Private Questions");
      menuData.add("Grade Book");
      menuData.add("Learning material");
      menuData.add("General Notifications");
      menuData.add("Personal Notifications");
      menuData.add("Ranking");
    }
    eventAggregator =
        new EventAggregator(schoolCensus.getCloudantInstance(),
                            getApplicationContext());

    setDrawerAdapter();

    mDrawerLayout.closeDrawer(expandableListView);

    setAddButtonTag(2);

    startService();

    if (userView == 1) {

      loadView(InstructorsSummaryView.newInstance(0));

    } else if (userView == 2) {

      loadView(StudentsSummaryView.newInstance(0));

    }


  }

  public void setDrawerAdapter() {

    expandableListView = (ExpandableListView) findViewById(R.id.drawer);

    List mainList = null, subList = null;

    mainList = createGroupList(menuData);
    subList = createGroupListInner(menuData);
    MainViewExpandableListViewAdapter adapter =
        new MainViewExpandableListViewAdapter(
            this,
            mainList,
            R.layout.group_row,
            new String[]{"menu"},
            new int[]{R.id.titleName},
            subList,
            R.layout.child_row,
            new String[]{"shadeName"},
            new int[]{R.id.childname}, schoolList
        );

    mapListview.setTag(1);

    expandableListView.setTag(1);

    expandableListView.setAdapter(adapter);

    expandableListView.setOnChildClickListener(this);

    expandableListView.setOnGroupClickListener(this);


  }

  public void setSchoolList() {

    expandableListView = (ExpandableListView) findViewById(R.id.drawer);

    List mainList, subList;

    mainList = createGroupList(schoolMainList);

    subList = createChildSchoolList(schoolList);

    MainViewExpandableListViewAdapter adapter =
        new MainViewExpandableListViewAdapter(
            this,
            mainList,
            R.layout.group_row,
            new String[]{"menu"},
            new int[]{R.id.titleName},
            subList,
            R.layout.child_row,
            new String[]{"shadeName"},
            new int[]{R.id.childname}, schoolList
        );

    mapListview.setTag(2);
    expandableListView.setTag(2);
    mapListview.setAdapter(adapter);

    mapListview.setOnChildClickListener(this);

    mapListview.setOnGroupClickListener(this);


  }

  public void setHomeTitle() {
    title.setText("Edu Peer");
  }


  @Override
  public void onClick(View v) {
    if (v == menuDrawerButton) {

      closeOpenDrawer();

    } else if (v == icon) {

      Toast t = Toast.makeText(getApplicationContext(),
                               "Please use the side menu to choose notifications option",
                               Toast.LENGTH_SHORT);
      t.show();


    } else if (v == logoutButton) {

      logout();
    } else if (v == addButton) {

      int tag = (int) addButton.getTag();
      if (tag == 1) {
        QuizListView quizListView = (QuizListView) schoolCensus.getCurrentFragment();
        quizListView.openDialog(-1);
      } else if (tag == 2) {
        QuestionsListView quizListView = (QuestionsListView) schoolCensus.getCurrentFragment();
        quizListView.openDialog(-1);
      } else if (tag == 3) {
        QuestionListViewContent
            quizListView =
            (QuestionListViewContent) schoolCensus.getCurrentFragment();
        quizListView.openDialog(-1);

      } else if (tag == 4) {
        LearningContentListView
            quizListView =
            (LearningContentListView) schoolCensus.getCurrentFragment();
        quizListView.openDialog(-1);

      }


    }


  }

  private void logout() {
    Intent intent = new Intent(getApplicationContext(), LoginView.class);
    startActivity(intent);
  }


  public void disableMenuDrawer() {

    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    mapListview.setTag(2);
    expandableListView.setTag(2);

  }

  public void enableMenuDrawer() {

    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    mapListview.setTag(1);
    expandableListView.setTag(1);
    showMenuDrawer();

  }

  private void closeOpenDrawer() {

    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    if (mDrawerLayout.isDrawerOpen(expandableListView)) {
      mDrawerLayout.closeDrawer(expandableListView);
    } else {
      mDrawerLayout.openDrawer(expandableListView);
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

  }

  @Override
  public boolean onChildClick(ExpandableListView parent, View v, int position,
                              int childPosition, long id) {

    int tag = (int) parent.getTag();

    if (tag == 1) {

//      if (schoolCensus.getRole() == 1) {
//
//        if (childPosition == 0) {
//          schoolCensus.showToast(getApplicationContext(), "Sorry, not Available at the moment");
//        } else if (childPosition == 1) {
//          closeOpenDrawer();
//          loadView(SchoolStudentReport.newInstance(1));
//        } else {
//          schoolCensus.showToast(getApplicationContext(), "Sorry, not Available at the moment");
//        }
//
//      } else if (schoolCensus.getRole() == 2) {
//
//        if (childPosition == 0) {
//          closeOpenDrawer();
//          loadView(SchoolStudentReport.newInstance(1));
//        } else {
//          schoolCensus.showToast(getApplicationContext(), "Sorry, not Available at the moment");
//        }
//      }

    } else if (tag == 2) {

      Marker marker = markers[childPosition];

      schoolCensus.setSelectedSchoolIndex(childPosition);

      marker.showInfoWindow();
      LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
      centerMap(marker, latLng);


    }
    return false;
  }

  @Override
  public boolean onGroupClick(ExpandableListView parent, View v, int position, long id) {

    int tag = (int) parent.getTag();

    schoolCensus.setUserQiuz(null);


    if (userObject.getUser().getRole().equalsIgnoreCase(Constants.INSTRUCTOR_ROLE)) {

      if (position == 0) {

        closeOpenDrawer();
        Intent intent = new Intent(getApplicationContext(), MainView.class);
        startActivity(intent);

      } else if (position == 1) {

        closeOpenDrawer();
        loadView(QuizListView.newInstance(1));

      } else if (position == 2) {

        schoolCensus.setQuestionState(Constants.PUBLIC);
        closeOpenDrawer();
        loadView(QuestionsListView.newInstance(1));

      } else if (position == 3) {

        schoolCensus.setQuestionState(Constants.PRIVATE);
        closeOpenDrawer();
        loadView(QuestionsListView.newInstance(1));

      } else if (position == 4) {

        closeOpenDrawer();

        loadView(StudentsListView.newInstance(1));

      } else if (position == 5) {

        closeOpenDrawer();
        loadView(LearningContentListView.newInstance(1));

      } else if (position == 6) {
        schoolCensus.setQuestionState(Constants.PUBLIC);
        closeOpenDrawer();

        loadView(NotificationsListView.newInstance(1));

      } else if (position == 7) {
        schoolCensus.setQuestionState(Constants.PRIVATE);
        closeOpenDrawer();
        loadView(NotificationsListView.newInstance(1));

      }else if (position == 8) {
        // Seat arrangement

      }else if (position == 9) {
        // Seat arrangement
        closeOpenDrawer();
        loadView(RankingQuizListView.newInstance(1));
      }


    } else if (userObject.getUser().getRole().equalsIgnoreCase(Constants.STUDENT_ROLE)) {

      if (position == 0) {

        closeOpenDrawer();
        Intent intent = new Intent(getApplicationContext(), MainView.class);
        startActivity(intent);

      } else if (position == 1) {

        closeOpenDrawer();
        loadView(QuizListView.newInstance(1));

      } else if (position == 2) {

        schoolCensus.setQuestionState(Constants.PUBLIC);
        closeOpenDrawer();
        loadView(QuestionsListView.newInstance(1));

      } else if (position == 3) {

        schoolCensus.setQuestionState(Constants.PRIVATE);
        closeOpenDrawer();
        loadView(QuestionsListView.newInstance(1));

      } else if (position == 4) {

        closeOpenDrawer();

        loadView(GradebookQuizListView.newInstance(1));

      } else if (position == 5) {

        closeOpenDrawer();
        loadView(LearningContentListView.newInstance(1));

      } else if (position == 6) {
        schoolCensus.setQuestionState(Constants.PUBLIC);
        closeOpenDrawer();
        loadView(NotificationsListView.newInstance(1));

      } else if (position == 7) {
        schoolCensus.setQuestionState(Constants.PRIVATE);
        closeOpenDrawer();
        loadView(NotificationsListView.newInstance(1));

      }else if (position == 8) {
        // Seat arrangement
        closeOpenDrawer();
        loadView(RankingQuizListView.newInstance(1));
      }


    }

    return true;
  }

  public void loadView(Fragment currentFragment) {

    refreshButton.setVisibility(View.GONE);
    fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.main, currentFragment).addToBackStack("my_fragment").commit();

  }

  @Override
  public void onBackPressed() {

    if (schoolCensus.getCurrentTitle().equalsIgnoreCase(Constants.InstructorsSummaryView)
        || schoolCensus.getCurrentTitle().equalsIgnoreCase(Constants.StudentsSummaryView)) {
      logout();
    } else if (schoolCensus.getCurrentTitle().equalsIgnoreCase(Constants.QuizListViewContent)) {

      loadView(QuizListView.newInstance(1));

    } else if (schoolCensus.getCurrentTitle()
                   .equalsIgnoreCase(Constants.QuizListView) || schoolCensus.getCurrentTitle()
                   .equalsIgnoreCase(Constants.LearningContentListView)) {

      if (userView == 1) {

        loadView(InstructorsSummaryView.newInstance(0));

      } else if (userView == 2) {

        loadView(StudentsSummaryView.newInstance(0));

      }

    } else if (schoolCensus.getCurrentTitle().equalsIgnoreCase(Constants.StudentGridView)) {

//        loadView(StudentsGradesHomeGridView.newInstance(1));

    } else if (schoolCensus.getCurrentTitle()
        .equalsIgnoreCase(Constants.StudentsGradeHomeGridView)) {

//        loadView(StudentsHomeGridView.newInstance(1));

    } else if (schoolCensus.getCurrentTitle()
        .equalsIgnoreCase(Constants.StudentsGradeHomeGridView)) {

//        loadView(StudentsHomeGridView.newInstance(1));

    } else if (schoolCensus.getCurrentTitle().equalsIgnoreCase(Constants.StudentsHomeGridView)) {

//        loadView(MainHomeGridView.newInstance(1));

    } else if (schoolCensus.getCurrentTitle()
        .equalsIgnoreCase(Constants.StudentTransferWizardView)) {

//        loadView(StudentsHomeGridView.newInstance(1));

    } else {
      super.onBackPressed();
    }

  }

  public void showMenuDrawer() {

    menuDrawerButton.setVisibility(View.VISIBLE);
    menuDrawerButton.setOnClickListener(this);
    editButton.setVisibility(View.GONE);

  }

  public void hideMenuDrawer() {

    menuDrawerButton.setVisibility(View.GONE);
    editButton.setVisibility(View.GONE);

  }

  public void showEditButton() {
    editButton1.setVisibility(View.VISIBLE);
    editButton1.setOnClickListener(this);
  }

  public void hideEditButton() {
    editButton1.setVisibility(View.GONE);
  }

  public void setTitle(String name) {

    title.setText(name);

  }

  public void showAddButton() {
//    addButton.setImageResource(R.drawable.plus_white);
    addButton.setVisibility(View.VISIBLE);
  }


  @Override
  public void onStop() {

    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    menuDrawerButton.setVisibility(View.GONE);

    super.onStop();

  }


  @Override
  public void onDestroy() {

    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    menuDrawerButton.setVisibility(View.GONE);

    super.onDestroy();

  }

  public void hideAddButton() {
    addButton.setVisibility(View.GONE);
  }

  public void setAddButtonTag(int number) {

    addButton.setTag(number);

  }

  public void showSaveButton(int number) {
    saveButton.setTag(number);
    saveButton.setVisibility(View.GONE);
    saveButton.setOnClickListener(this);
  }

  public void hideSaveButton() {

    saveButton.setVisibility(View.GONE);
  }


  public void showRefreshButton() {

    refreshButton.setVisibility(View.GONE);
    refreshButton.setOnClickListener(this);
  }

  public void showProfileButton() {

    editButton1.setVisibility(View.GONE);
    transferButton.setVisibility(View.VISIBLE);
  }

  public void hideProfileButton() {

    editButton1.setVisibility(View.GONE);
    transferButton.setVisibility(View.GONE);
  }

  public void hideRefreshButton() {

    refreshButton.setVisibility(View.GONE);
  }

  public void showDownloadProgressBar() {

//    if (progressDialog == null) {
    progressDialog =
        new ProgressDialog(
            MainView.this); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
    progressDialog.setMessage("Please wait...");
    progressDialog.setTitle("");
    progressDialog.setCancelable(true);
//    }
    progressDialog.show();
//    downloadProgress.setVisibility(View.VISIBLE);
//    downloadProgress.setOnClickListener(this);
  }


  public void hideDownloadProgressBar() {

    if (progressDialog != null) {
      progressDialog.hide();
    }
//    downloadProgress.setVisibility(View.GONE);
  }

  public void centerMap(Marker fenceMarker, LatLng latLng) {

    if (fenceMarker != null) {
      LatLngBounds center;
      LatLng fence = fenceMarker.getPosition();
      if (fence.latitude < latLng.latitude || fence.longitude < latLng.longitude) {
        center = new LatLngBounds(fenceMarker.getPosition(), latLng);
      } else {
        center = new LatLngBounds(latLng, fenceMarker.getPosition());
      }
      //googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(center, 1));
      googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

    }
  }

  private List createGroupList(String data[]) {
    ArrayList result = new ArrayList();
    for (int i = 0; i < data.length; ++i) {
      HashMap m = new HashMap();
      m.put("menu", data[i]);
      result.add(m);
    }
    return (List) result;
  }

  @Override
  public void onResume() {

    super.onResume();

    refreshButton.setVisibility(View.GONE);


  }


  private List createGroupList(List<String> data) {
    ArrayList result = new ArrayList();
    for (int i = 0; i < data.size(); ++i) {
      HashMap m = new HashMap();
      m.put("menu", data.get(i));
      result.add(m);
    }

    return (List) result;
  }

  private List createGroupListInner(List<String> data) {

    ArrayList result1 = new ArrayList();
    for (int i = 0; i < data.size(); ++i) {
      ArrayList secList = new ArrayList();

      result1.add(secList);
    }
    return result1;

  }


  private List createChildList(String dataSubList[][]) {
    ArrayList result = new ArrayList();
    for (int i = 0; i < dataSubList.length; ++i) {
      ArrayList secList = new ArrayList();
      for (int n = 0; n < dataSubList[i].length; n += 2) {
        HashMap child = new HashMap();
        child.put("shadeName", dataSubList[i][n]);
        secList.add(child);
      }
      result.add(secList);
    }
    return result;
  }

  private List createChildSchoolList(List<MenuObject> schoolList) {
    ArrayList result = new ArrayList();

    for (int i = 0; i < 1; ++i) {
      ArrayList secList = new ArrayList();
      for (int n = 0; n < schoolList.size(); n++) {
        HashMap child = new HashMap();
        child.put("shadeName", schoolList.get(n).name);
        secList.add(child);
      }
      result.add(secList);

    }
    return result;
  }


  @Override
  public void uploadData() {
//    schoolCensus.uploadCloudantData();
//    pull image data if there are some added for this school using school filter

  }


  public void closeDrawer() {

    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    menuDrawerButton.setVisibility(View.GONE);
    setHomeTitle();

    setSchoolList();


  }

  @Override
  public void onStart() {
    super.onStart();
  }


  public Runnable timeChecker = new Runnable() {
    @Override
    public void run() {

      try {
        checkNotifications();
      } catch (Exception e) {
      }
    }


  };

  public void startService() {
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    scheduledExecutorService.scheduleWithFixedDelay(timeChecker, 0, 20, TimeUnit.SECONDS);
  }

  private void checkNotifications() {

    new backgroundProcessSave().execute();

  }

  private void updateNotificationCount() {
    txt_count.setText("" + totalNotifications);
  }

  private class backgroundProcessSave extends AsyncTask<Quiz, Quiz, Long> {

    protected Long doInBackground(Quiz... params) {

      try {

        int personalQuestions = 0;
        int generalQuestions = 0;
        NotificationObject notificationObject = notificationManager.getNotificationObject();
        UserObject userObject1 = userManager.getDocumentGetDocument(Constants.USERS);

        for (User user : userObject1.getUserList()) {

          if (user.getUsername()
              .equalsIgnoreCase(schoolCensus.getUserObject().getUser().getUsername())) {
            for (Notifications notifications : user.getNotificationsList()) {

              if (notifications!=null && notifications.getRead()!=null) {
                 if (!notifications.getRead()) {
                  personalQuestions++;
                }
              }

            }
          }
        }
        for (Notifications notifications : notificationObject.getNotificationsList()) {
          if (notifications!=null && notifications.getRead()!=null) {
            if (!notifications.getRead()) {
              generalQuestions++;
            }
          }

        }

        totalNotifications = personalQuestions + generalQuestions;


      } catch (Exception e) {
        e.printStackTrace();
      }

      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {
      updateNotificationCount();


    }


    protected void onPreExecute() {
    }

  }

}
