package com.edu.peers.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.adapter.PairingListViewAdapter;
import com.edu.peers.dialogs.QuizCreationDialogView;
import com.edu.peers.dialogs.StudentsPairDialogView;
import com.edu.peers.managers.GradeBookManager;
import com.edu.peers.managers.QuizManager;
import com.edu.peers.managers.UserManager;
import com.edu.peers.models.GradebookObject;
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.others.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * Created by nelson on 3/16/15.
 */
public class StudentsPairListView extends Fragment implements
                                           View.OnClickListener, AdapterView.OnItemClickListener {

  public HashMap<String, ComboBoxViewListItem>
      classArray =
      new HashMap<String, ComboBoxViewListItem>();
  public ComboBoxViewListItem[] comboBoxViewListItems;
  private List<User> array_sort = new ArrayList<>();
  private List<User> sampleData = new ArrayList<>();
  private SchoolCensus schoolCensus;
  private TextView title;
  private long totalSize = 0;
  private ListView listView;
  private Handler mHandler;
  private LinearLayout progressBar;
  private View view;
  private Spinner spinner;
  private ComboBoxViewAdapter comboBoxViewAdapter;
  private String selectedLocation;
  private ImageView logoutButton;
  private static String ARG_POSITION;
  private View.OnTouchListener onSubjectTouchListener = new View.OnTouchListener() {

    @Override
    public boolean onTouch(View v, MotionEvent event) {

      ((ComboBoxViewAdapter) comboBoxViewAdapter).setSelected(true);

      ((ComboBoxViewAdapter) comboBoxViewAdapter).notifyDataSetChanged();
      return false;
    }
  };
  private MainView mainView;
  private int mStackLevel;
  private QuizManager quizManager;
  private GradeBookManager gradeBookManager;
  private GradebookObject gradebookObject;
  private UserObject userObject;
  private int quizIndex;
  private User pairUser;
  private User unPairUser;
  private List<User> userList;
  private User userQuiz;
  private UserManager userManager;
  private User removedUser;

  public static StudentsPairListView newInstance(int position) {
    StudentsPairListView f = new StudentsPairListView();
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
    schoolCensus.initHome();
    schoolCensus.setState(Constants.STUDENT_SUMMARY_VIEW);

    schoolCensus.setCurrentTitle(Constants.QuizListView);

    userObject =schoolCensus.getUserObject();
   userQuiz= schoolCensus.getSelectedUser();

    quizManager =
        new QuizManager(schoolCensus.getCloudantInstance(), getContext());
    userManager = new UserManager(schoolCensus.getCloudantInstance(), getContext());

    gradeBookManager =
        new GradeBookManager(schoolCensus.getCloudantInstance(), getContext());

    mainView = schoolCensus.getMainView();
    mainView.hideAddButton();

  }

  void openDialog(final int index) {

    schoolCensus.setState(Constants.STUDENT_DATA);

    schoolCensus.setCurrentActivity(getActivity());
    schoolCensus.setCurrentFragment(this);

    mHandler.post(new Runnable() {
      @Override
      public void run() {

        mStackLevel++;

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        QuizCreationDialogView
            newFragment = QuizCreationDialogView.newInstance(mStackLevel);
        newFragment.show(ft, "StudentDataDialog111");

      }
    });

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.quiz_list_view, null);

    spinner = (Spinner) view.findViewById(R.id.county);

    loadSpinnerData();
    spinner.setOnItemSelectedListener(new OnStudentSelected());
    spinner.setOnTouchListener(onSubjectTouchListener);
    spinner.setVisibility(View.GONE);

    progressBar = (LinearLayout) view.findViewById(R.id.progressBarSchools);

    progressBar.setVisibility(View.GONE);

    listView = (ListView) view.findViewById(R.id.schools_list_view);

    registerForContextMenu(listView);

    listView.setOnItemClickListener(this);

    for (User user:userObject.getUserList()){

      if (user.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE)){
        sampleData.add(user);

      }

    }
    AppendList(sampleData);


    return view;
  }
  public void updateList() {
    userObject =schoolCensus.getUserObject();
    sampleData= new ArrayList<>();

    for (User user:userObject.getUserList()){

      if (user.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE)){
        sampleData.add(user);

      }

    }
    AppendList(sampleData);

  }

  @Override
  public void onClick(View v) {

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//    schoolCensus.setSelectedSchoolIndex(position);
//    schoolCensus.setQuizIndex(quizIndex);
//
////    Intent ivE = new Intent(this, SchoolHomeGridView.class);
////    Intent ivE = new Intent(this, MapView.class);
////    startActivity(ivE);
//
//    schoolCensus.setQuiz(sampleData.get(position));
//    mainView.loadView(GradebookQuizListViewContent.newInstance(1));

  }

  public void add(final List<User> homeItemList) {

    final EditText editText = (EditText) view.findViewById(R.id.searchField);

    editText.addTextChangedListener(new TextWatcher() {
      public void afterTextChanged(Editable s) {
        // Abstract Method of TextWatcher Interface.
      }

      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {
        // Abstract Method of TextWatcher Interface.
      }

      public void onTextChanged(CharSequence s, int start, int before,
                                int count) {

        filterListData(
            editText.getText().toString().toLowerCase()
                .trim());

      }
    });


  }

  private void filterListData(String value) {

    int size = sampleData.size();
    if (size > 0) {
      array_sort.clear();
    }

    for (int i = 0; i < size; i++) {

      if (sampleData
          .get(i).getUsername()
          .toLowerCase()
          .contains(value)

          ) {

        array_sort.add(sampleData.get(i));
      }
    }
    AppendList(array_sort);
  }

  void AppendList(List<User> str) {

    listView.setAdapter(new PairingListViewAdapter(this, str));
  }

  private void loadSpinnerData() {

    sampleData.clear();

    classArray.put("0",
                   new ComboBoxViewListItem("0", "Maths", "Maths"));
    classArray.put("1",
                   new ComboBoxViewListItem("1", "Science", "Science"));

    if (classArray != null) {
      comboBoxViewAdapter = new ComboBoxViewAdapter(getContext(),
                                                    android.R.layout.simple_spinner_item,
                                                    convertToModel(classArray.values()));
      comboBoxViewAdapter.setHint("Select Subject");
    }

    spinner.setAdapter(comboBoxViewAdapter);
    spinner.setSelection(0);
  }

  public ComboBoxViewListItem[] convertToModel(Collection<ComboBoxViewListItem> data) {

    comboBoxViewListItems = new ComboBoxViewListItem[data.size()];
    int i = 0;

    for (ComboBoxViewListItem comboBoxViewListItem : data) {
      comboBoxViewListItems[i++] = comboBoxViewListItem;
    }

    return comboBoxViewListItems;
  }


  @Override
  public void onResume() {
    super.onResume();
    schoolCensus.setCurrentTitle(Constants.QuizListView);
    schoolCensus.setCurrentFragment(this);

    add(sampleData);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);

    AdapterView.AdapterContextMenuInfo
        info =
        (AdapterView.AdapterContextMenuInfo) menuInfo;

    if (v.getId() == R.id.schools_list_view) {
      schoolCensus.setSelectedSchoolIndex(info.position);
//      Intent ivE = new Intent(this, SchoolRegistrationView.class);
//      startActivity(ivE);

    }
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {

    AdapterView.AdapterContextMenuInfo
        info =
        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    int index = info.position;
    switch (item.getItemId()) {

      case R.id.edit:
        openNewSchoolDialog(index);
        return true;
      case R.id.delete:
//        deleteConfirmation(sampleData.get(index).id.toString());
        return true;
      default:
        return super.onContextItemSelected(item);
    }


  }

  void openNewSchoolDialog(int index) {
    schoolCensus.setCurrentDialogTitle("School Data");
    schoolCensus.setCurrentDialogMsg("School Data");
    schoolCensus.showDialog(Constants.SCHOOL_DATA);
  }

  void deleteConfirmation(final String docId) {
    new AlertDialog.Builder(getContext())
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Confirmation")
        .setMessage("Are you sure you want to delete the record?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            schoolCensus.setState(Constants.SCHOOL_DATA_LIST);
//            schoolCensus.deleteDocumentCloudant(docId);
          }
        })
        .setNegativeButton("No", null)
        .show();
  }


  public void studentsUnpair(User user) {
    unPairUser=user;
    unPairUser.setPaired(false);



    new backgroundProcessSave().execute();

  }
  public void studentsPair(User user) {

    pairUser=user;
    schoolCensus.setSelectedUser(pairUser);

    new backgroundProcess().execute(pairUser);

  }


  public int calculateMarks(Quiz quiz, int option) {
    int totalMarks=0;
    int correctExpectedMarks=0;
    int selectedExpectedMarks=0;
    List<Questions> questionsList = quiz.getQuestions();
    for (Questions questions:questionsList){
      List<Input> choices =questions.getChoices();
      for (int y=0;y<choices.size();y++){
        Input input =choices.get(y);
        if (input.getAnswer()){
          correctExpectedMarks++;
        }

        if (input.getAnswer()&& input.getSelected()){
          selectedExpectedMarks++;
        }

      }
    }


//    totalMarks=""+selectedExpectedMarks+"/"+correctExpectedMarks;
    if (option==1)
      totalMarks=selectedExpectedMarks;
    else  if (option==2)
      totalMarks=correctExpectedMarks;



    return totalMarks;
  }

  public float averageForUSer( List<Quiz> quizList, User user){
    float totalMarks=0;
    float total=0;
    for (int y=0;y<quizList.size();y++){
      Quiz quiz=quizList.get(y);
      if (quiz.getUser().getUsername().equalsIgnoreCase(user.getUsername())){
        totalMarks+= quiz.getMarks();
        total+= quiz.getTotal();
      }

    }
    float totalValue=0;
    if (total!=0)
     totalValue=(totalMarks/total);


    return totalValue;
  }
  public boolean check( List<User> userList, User selectedUser) {


    boolean matching=false;
    for (int y=0;y<userList.size();y++) {
      User user = userList.get(y);

      if (user.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE))
      {

        if (user.getUsername().equalsIgnoreCase(selectedUser.getUsername())){
          matching=true;

        }
      }
    }

    return matching;

  }
    public List<User>  getBiggestDifference( List<User> userList, User selectedUser){
    int totalMarks=0;
    int total=0;
    List<User> userListStudentsOnly = new ArrayList<>();
    List<User> foundStudents = new ArrayList<>();
    for (int y=0;y<userList.size();y++) {
      User user = userList.get(y);

      if (user.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE) && !user.getUsername().equalsIgnoreCase(selectedUser.getUsername()))
      {
        userListStudentsOnly.add(user);
      }



    }

//    List<User> ascendingList=userListStudentsOnly;
    List<User> descendingList=userListStudentsOnly;
//    Collections.sort(ascendingList, new Comparator<User>(){
//      public int compare(User user1, User user2) {
//        // ## Ascending order
////          return quiz1.getMarks().compareToIgnoreCase(quiz2.getMarks()); // To compare string values
//           return Float.valueOf(user1.getAverage()).compareTo(user2.getAverage()); // To compare integer values
//
//        // ## Descending order
//        // return emp2.getFirstName().compareToIgnoreCase(emp1.getFirstName()); // To compare string values
////        return Float.valueOf(user2.getAverage()).compareTo(user1.getAverage()); // To compare integer values
//      }
//    });

    Collections.sort(descendingList, new Comparator<User>(){
      public int compare(User user1, User user2) {
        // ## Ascending order
//          return quiz1.getMarks().compareToIgnoreCase(quiz2.getMarks()); // To compare string values
//        return Float.valueOf(user1.getAverage()).compareTo(user2.getAverage()); // To compare integer values

        // ## Descending order
        // return emp2.getFirstName().compareToIgnoreCase(emp1.getFirstName()); // To compare string values
        return Float.valueOf(user2.getAverage()).compareTo(user1.getAverage()); // To compare integer values
      }
    });

    float currentAverage=selectedUser.getAverage();

    float max=descendingList.get(0).getAverage();

    float min=descendingList.get((descendingList.size()-1)).getAverage();

    float val;
    User selected=null,selected1=null;
    if (currentAverage==max)
    {
       val=max-min;
      // Its the user with min value
      selected=descendingList.get((descendingList.size()-1));

    } else  if (currentAverage==min)
    {
       val=max-min;
      // Its the user with max value
      selected=descendingList.get(0);

    }else  {

      float innerValue1;
      float innerValue2;

      innerValue1=currentAverage-min;
      innerValue2=max-currentAverage;

      if (Math.abs(innerValue1)>Math.abs(innerValue2)) {
        selected = descendingList.get((descendingList.size() - 1));
        selected.setRecommend(true);
      }
      if (Math.abs(innerValue1)<Math.abs(innerValue2)){
        selected=descendingList.get(0);
        selected.setRecommend(true);
      }
      if (Math.abs(innerValue1)==Math.abs(innerValue2)){
        selected=descendingList.get(0);
        selected1=descendingList.get((descendingList.size()-1));

        selected.setRecommend(true);
        selected1.setRecommend(true);

      }

    }

    if (selected!=null){
      foundStudents.add(selected);
      Log.i(Constants.TAG,"selected========="+selected.getUsername());
    }

    if (selected1!=null) {
      foundStudents.add(selected1);
      Log.i(Constants.TAG, "selected1=========" + selected1.getUsername());
    }

    return foundStudents;
  }
  private class backgroundProcessSave extends AsyncTask<Quiz, Quiz, Long> {

    protected Long doInBackground(Quiz... params) {

      try {

        List<User> userList = userManager.getDocumentGetDocument(Constants.USERS).getUserList();

        int size = userList.size();


        for (int y = 0; y < size; y++) {
          User user1 = userList.get(y);
          if (user1.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE)) {

            if (user1.getUsername().equalsIgnoreCase(unPairUser.getPairedWith())) {
             removedUser= user1;
            }

          }
        }

        for (int y = 0; y < size; y++) {
          User user1 = userList.get(y);
          if (user1.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE)) {

            if (user1.getUsername().equalsIgnoreCase(unPairUser.getUsername())) {
              unPairUser.setPairedWith(null);
              unPairUser.setPaired(false);
              userList.set(y, unPairUser);
            }

            if (user1.getUsername().equalsIgnoreCase(removedUser.getUsername())) {
              removedUser.setPaired(false);
              removedUser.setPairedWith(null);
              userList.set(y, removedUser);
            }
          }
        }



        userObject.setUserList(userList);
        schoolCensus.setUserObject(userObject);
        userManager.addDocument(userObject, Constants.USERS);



      } catch (Exception e) {
        e.printStackTrace();
      }

      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {
      if (progressBar != null) {
        progressBar.setVisibility(View.GONE);
      }
      updateList();
    }


    protected void onPreExecute() {
      if (progressBar != null) {
        progressBar.setVisibility(View.VISIBLE);
      }
    }

  }

  private class backgroundProcess extends AsyncTask<User, User, Long> {

    protected Long doInBackground(User... params) {


      User useUser=params[0];


      sampleData= new ArrayList<>();
      gradebookObject = gradeBookManager.getGradebookObject();

      if (gradebookObject != null) {

        List<Quiz> quizList=gradebookObject.getQuizList();
        for (int y=0;y<quizList.size();y++){
          Quiz quiz=quizList.get(y);
          quiz.setMarks(calculateMarks(quiz,1));
          quiz.setTotal(calculateMarks(quiz,2));
          quizList.set(y,quiz);
        }

        useUser.setAverage(averageForUSer(quizList,useUser));
//        useUser.setAverage(1);

        userList=userObject.getUserList();

        for (int y=0;y<userList.size();y++){
          User user=userList.get(y);

          if (user.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE) && !user.getUsername().equalsIgnoreCase(useUser.getUsername()))
          {
          user.setAverage(averageForUSer(quizList,user));
//
//            int k=y+2;
//            user.setAverage(k);
          userList.set(0,user);
          }
        }

        float currentAverage=useUser.getAverage();

        List<User> list=getBiggestDifference(userList,useUser);

        int userIndex=-1;
        for (int y=0;y<userList.size();y++){
          User user = userList.get(y);

          if (user.getRole().equalsIgnoreCase(Constants.STUDENT_ROLE) && !user.getUsername().equalsIgnoreCase(useUser.getUsername())) {

            if (currentAverage > user.getAverage()) {
              float current = currentAverage - user.getAverage();
              user.setDifference(current);
//              if (current > currentDiff) {
//                userIndex=y;
//                currentDiff = current;
//              }
            } else if (currentAverage < user.getAverage()) {
              float current = user.getAverage() - currentAverage;
              user.setDifference(current);
//              if (current > currentDiff){
//                userIndex=y;
//                currentDiff = current;
//              }

            }

            if (check(list,user)) {
              user.setRecommend(true);
            } else user.setRecommend(false);
            userList.set(y, user);
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

      schoolCensus.setUserList(userList);

      FragmentTransaction ft = getFragmentManager().beginTransaction();

      StudentsPairDialogView
          newFragment = StudentsPairDialogView.newInstance(mStackLevel);
      newFragment.setStudentsPairListView(StudentsPairListView.this);
      newFragment.show(ft, "StudentDataDialog111");
    }

    protected void onPreExecute() {
      if (progressBar != null) {
        progressBar.setVisibility(View.VISIBLE);
      }
    }

  }

//
//  private class backgroundProcessSave extends AsyncTask<Quiz, Quiz, Long> {
//
//    protected Long doInBackground(Quiz... params) {
//
//      if (quizObject == null) {
//        quizObject = new QuizObject();
//      }
//
//      List<Quiz> quListList = quizObject.getQuizList();
//
//      quListList.add(params[0]);
//
//      quizObject.setQuizList(quListList);
//
//      quizManager.addQuiz(quizObject);
//
//      long totalSize = 0;
//      return totalSize;
//    }
//
//
//    protected void onPostExecute(Long result) {
//      if (progressBar != null) {
//        progressBar.setVisibility(View.GONE);
//      }
//
//      updateList();
//
//    }
//
//    protected void onPreExecute() {
//      if (progressBar != null) {
//        progressBar.setVisibility(View.VISIBLE);
//      }
//    }
//
//  }
//
//  public void updateList() {
//    sampleData = quizObject.getQuizList();
//    listView.setAdapter(new GradebookListViewAdapter(this, sampleData));
//
//  }

  private class OnStudentSelected implements
                                  AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {

      if (comboBoxViewListItems != null) {
        selectedLocation = comboBoxViewListItems[pos].getText();
      }

    }

    public void onNothingSelected(AdapterView parent) {
      // Do nothing.
    }
  }


}
