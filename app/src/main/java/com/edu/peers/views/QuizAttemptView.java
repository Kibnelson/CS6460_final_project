package com.edu.peers.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.adapter.QuizAttemptExpandableListViewAdapter;
import com.edu.peers.dialogs.QuizCreationDialogView;
import com.edu.peers.dialogs.RecordAudioDialog;
import com.edu.peers.dialogs.ScratchPadDialog;
import com.edu.peers.managers.GradeBookManager;
import com.edu.peers.managers.UserManager;
import com.edu.peers.models.GradebookObject;
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.models.UserStatistics;
import com.edu.peers.others.Constants;
import com.edu.peers.others.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by nelson on 3/16/15.
 */
public class QuizAttemptView extends Fragment implements
                                              View.OnClickListener,
                                              ExpandableListView.OnChildClickListener,
                                              ExpandableListView.OnGroupClickListener,
                                              AdapterView.OnItemClickListener {

  public HashMap<String, ComboBoxViewListItem>
      classArray =
      new HashMap<String, ComboBoxViewListItem>();
  public ComboBoxViewListItem[] comboBoxViewListItems;
  private List<Quiz> array_sort = new ArrayList<>();
  private List<Quiz> sampleData = new ArrayList<>();
  private SchoolCensus schoolCensus;
  private TextView title;
  private long totalSize = 0;
  private ExpandableListView listView;
  private Handler mHandler;
  private LinearLayout progressBar;
  private View view;
  private Spinner spinner;
  private ComboBoxViewAdapter comboBoxViewAdapter;
  private String selectedLocation;
  private ImageView logoutButton;
  private static String ARG_POSITION;
  public Map<Integer, TextView> questionChoices = new HashMap<>();

  public Map<Integer, Map<Integer, Input>> choices = new HashMap<>();
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
  private TextView quizName, duration, subject, instructions, dateCreated, createdBy;
  private Quiz quiz;
  private RecordAudioDialog recordAudioDialog;
  private ScratchPadDialog scratchPadDialog;
  public List<Map<String, Questions>> groupData = new ArrayList<Map<String, Questions>>();
  public List<List<Map<String, Input>>> childData = new ArrayList<List<Map<String, Input>>>();
  private QuizAttemptExpandableListViewAdapter adapter;
  private Button attemptQuiz;
  private Button submitQuiz;
  private GradeBookManager gradeBookManager;
  private UserObject userObject;
  private GradebookObject gradebookObject;
  private ProgressDialog progressDialog;
  private RadioButton yes, no;
  private boolean shareWork = false;
  private UserManager userManager;
  private boolean answerChecked=false;
  private boolean shareChecked=false;


  public static QuizAttemptView newInstance(int position) {
    QuizAttemptView f = new QuizAttemptView();
    Bundle b = new Bundle();
    b.putInt(ARG_POSITION, position);
    f.setArguments(b);
    return f;
  }

  private static final String NAME = "NAME";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.mHandler = new Handler(Looper.getMainLooper());

    int position = getArguments().getInt(ARG_POSITION);

    schoolCensus = (SchoolCensus) getActivity().getApplication();
    schoolCensus.setCurrentFragment(this);
    schoolCensus.initHome();
    schoolCensus.setState(Constants.STUDENT_SUMMARY_VIEW);
    schoolCensus.setCurrentTitle(Constants.QuizListViewContent);
    quiz = schoolCensus.getQuiz();

    userObject = schoolCensus.getUserObject();

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

    view = inflater.inflate(R.layout.quiz_list_view_attempt, null);

    listView = (ExpandableListView) view.findViewById(R.id.quiz);

//    listView.setAdapter(new QuizListViewAdapter(this, sampleData));

    listView.setOnItemClickListener(this);
    userManager = new UserManager(schoolCensus.getCloudantInstance(), getContext());

    quizName = (TextView) view.findViewById(R.id.quizName);
    duration = (TextView) view.findViewById(R.id.duration);
    subject = (TextView) view.findViewById(R.id.subject);
    instructions = (TextView) view.findViewById(R.id.instructions);
    createdBy = (TextView) view.findViewById(R.id.createdBy);
    dateCreated = (TextView) view.findViewById(R.id.dateCreated);
    submitQuiz = (Button) view.findViewById(R.id.submitQuiz);
    submitQuiz.setOnClickListener(this);

    yes = (RadioButton) view.findViewById(R.id.yes);
    yes.setOnClickListener(this);

    no = (RadioButton) view.findViewById(R.id.no);
    no.setOnClickListener(this);

    if (quiz != null) {

      quizName.setText(quiz.getName());
      duration.setText(quiz.getDuration());
      subject.setText(quiz.getSubject());
      instructions.setText(quiz.getInstructions());
      dateCreated.setText(quiz.getTimestamp());
      if (quiz.getUser() != null) {
        if (quiz.getUser().getRole().equalsIgnoreCase("Instructor")) {
          createdBy.setText(
              "Instructor: " + quiz.getUser().getFirstName() + " " + quiz.getUser().getLastName());
        } else {
          createdBy.setText(
              "Student: " + quiz.getUser().getFirstName() + " " + quiz.getUser().getLastName());
        }


      }
    }

    setDrawerAdapter();

    return view;
  }


  public void setDrawerAdapter() {

    List<Questions> questionsList = quiz.getQuestions();

    groupData = new ArrayList<Map<String, Questions>>();
    childData = new ArrayList<List<Map<String, Input>>>();
    // add data in group and child list
    for (int i = 0; i < questionsList.size(); i++) {
      Map<String, Questions> curGroupMap = new HashMap<String, Questions>();
      groupData.add(curGroupMap);
      curGroupMap.put(NAME, questionsList.get(i));

      List<Map<String, Input>> children = new ArrayList<Map<String, Input>>();

      //  String[][] childItems = {{}, {}, {}, {}, {}};
      if (questionsList.get(i).getChoices() != null) {
        for (int j = 0; j < questionsList.get(i).getChoices().size(); j++) {
          Map<String, Input> curChildMap = new HashMap<String, Input>();
          children.add(curChildMap);
          curChildMap.put(NAME, questionsList.get(i).getChoices().get(j));
        }
      }

      childData.add(children);
    }

    String groupFrom[] = {NAME};
    int groupTo[] = {R.id.titleName};
    String childFrom[] = {NAME};
    int childTo[] = {R.id.childname};

    adapter =
        new QuizAttemptExpandableListViewAdapter(
            this,
            groupData,
            R.layout.group_row_questions, groupFrom, groupTo,
            childData,
            R.layout.child_row_questions,
            childFrom, childTo
        );

    listView.setAdapter(adapter);

    listView.setOnChildClickListener(this);

    listView.setOnGroupClickListener(this);


  }
  @Override
  public void onClick(View v) {
    if (v == yes) {
      shareChecked=true;
      shareWork = true;
    } else if (v == no) {
      shareChecked=true;
      shareWork = false;
    } else if (v == submitQuiz) {
      // Submit Quiz

      if (answerChecked&& shareChecked) {

        quiz.setShareWork(shareWork);
        quiz.setUser(userObject.getUser());
        User user = userObject.getUser();

        List<User> userList = userObject.getUserList();
        int size = userList.size();
        for (int y = 0; y < size; y++) {
          User user1 = userList.get(y);
          if (user.getUsername().equalsIgnoreCase(user1.getUsername())) {
            List<UserStatistics> questionsAsked = user1.getQuizzesDone();
            questionsAsked.add(
                new UserStatistics(Utils.getCurrentDate(), Utils.generateNumber(),
                                   UUID.randomUUID().toString(),
                                   Constants.QUIZ_CATEGORY));
            user1.setQuizzesDone(questionsAsked);
            userList.set(y, user1);

          }

        }
        userObject.setUserList(userList);

        new backgroundProcess().execute(1);

      } else {

        schoolCensus.showMessageDialogAlert(getActivity(),"Please choose option in share work and select your choices","Info");

      }
//      choices =quiz.getChoices();
//
//      quiz.getChoices();
//
//      List<Questions> questionsList= quiz.getQuestions();
//
//      for (int y=0;y<questionsList.size();y++){
//
//        Questions questions=questionsList.get(y);
//
//        Log.i(Constants.TAG,"1====questions===================="+questions.getQuestionInput());
//
//        for (int t=0;t<questions.getChoices().size();t++){
//
//          Input input=questions.getChoices().get(t);
//
//          Log.i(Constants.TAG,"1====Answer====="+input.getAnswer());
//          Log.i(Constants.TAG,"1=====Input===="+input.getQuestionInput());
//          Log.i(Constants.TAG,"1====Selected====="+input.getSelected());
//
//
//          Input input1=choices.get(y).get(t);
//          if (input1!=null){
//
//          Log.i(Constants.TAG,"1====Selected====="+ choices.get(y).get(t).getSelected());
//          Log.i(Constants.TAG,"1====S-INPUT====="+ choices.get(y).get(t).getQuestionInput());
//
//          } else
//            Log.i(Constants.TAG,"1====NUL=====");
//
//        }
//      }
//

//
//
//      for (Map.Entry<Integer,  Map<Integer, Input>> group : choices.entrySet()) {
//
//        Map<Integer, Input> choicesValues = choices.get(group.getKey());
//
//        for (Map.Entry<Integer, Input> entry : choicesValues.entrySet()) {
//
//          Input input = choicesValues.get(entry.getKey());
//
//
//          Log.i(Constants.TAG,"1========="+input.getSelected());
//
//          Log.i(Constants.TAG,"1========="+input.getQuestionInput());
//        }
//
//
//
//      }

    }

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    schoolCensus.setSelectedSchoolIndex(position);
//    Intent ivE = new Intent(this, SchoolHomeGridView.class);
//    Intent ivE = new Intent(this, MapView.class);
//    startActivity(ivE);

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
    schoolCensus.setCurrentTitle(Constants.QuizListViewContent);
    mainView.showMenuDrawer();
    schoolCensus.setCurrentFragment(this);

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
//    if (index != -1) {
//     schoolCensus.setSchoolData(sampleData.get(index));
//    } else {
//      schoolCensus.setSchoolData(null);
//    }
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

  public void onGroupExpanded(int groupPosition) {

    if (listView.isGroupExpanded(groupPosition)) {
      listView.collapseGroup(groupPosition);
    } else {
      listView.expandGroup(groupPosition);
    }
  }


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

  @Override
  public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                              int childPosition, long id) {
    return false;
  }

  @Override
  public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
    return false;
  }


  public void openRecordDialog(String recordedString) {
    mStackLevel++;
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    recordAudioDialog = RecordAudioDialog.newInstance(mStackLevel, 2);
//    recordAudioDialog.setQuestionViewDialogView(this);
    recordAudioDialog.setRecordedString(recordedString);
    recordAudioDialog.show(ft, "school_calendar_view_dialog");

  }

  public void openWriteDialog(String imageString) {
    mStackLevel++;
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    scratchPadDialog = ScratchPadDialog.newInstance(mStackLevel, 2);
//    scratchPadDialog.setQuestionViewDialogView(this);
    scratchPadDialog.setImageString(imageString);
    scratchPadDialog.show(ft, "school_calendar_view_dialog");

  }


  public void checkBoxSelected1(int groupPosition, int childPosition, Boolean checked) {

    List<Input> choices = quiz.getQuestions().get(groupPosition).getChoices();
    ;
    for (int y = 0; y < choices.size(); y++) {
      Input input = choices.get(y);

      if (y == childPosition && checked) {
        input.setSelected(true);
        choices.set(y, input);
      } else if (y == childPosition && !checked && input.getSelected()) {
        input.setSelected(false);
        choices.set(y, input);
      }
    }
    quiz.getQuestions().get(groupPosition).setChoices(choices);


  }


  public void checkBoxSelected(int groupPosition, int childPosition, Boolean checked, Input value) {


    answerChecked=true;
    choices = quiz.getChoices();

    checkBoxSelected1(groupPosition, childPosition, checked);

    Map<Integer, Input> choicesValues = choices.get(groupPosition);
    if (choicesValues == null) {
      choicesValues = new HashMap<>();
    }

    if (choicesValues.containsKey(childPosition)) {
      choicesValues.remove(childPosition);
    }

    checkCheckedValues(groupPosition, childPosition);

    Input input = choicesValues.get(childPosition);
    if (input == null) {
      input = value;
    }

    input.setSelected(checked);

    choicesValues.put(childPosition, input);

    if (!checked) {
      choicesValues.remove(childPosition);
    }

    choices.put(groupPosition, choicesValues);

    checkCheckedValues(groupPosition, childPosition);

    quiz.setChoices(choices);


  }


  public boolean isCheckCheckedValues(final int groupPosition, int childPosition) {
    choices = quiz.getChoices();

    Map<Integer, Input> choicesValues = choices.get(groupPosition);
    if (choicesValues == null) {
      choicesValues = new HashMap<>();
    }

    Input input = choicesValues.get(childPosition);

    if (input == null) {
      input = new Input();
    }

    return input.getSelected();

  }

  public void checkCheckedValues(final int groupPosition, int childPosition) {

    String value = "";

    Map<Integer, Input> choicesValues = choices.get(groupPosition);
    if (choicesValues == null) {
      choicesValues = new HashMap<>();
    }

    for (Map.Entry<Integer, Input> entry : choicesValues.entrySet()) {

      if (entry.getValue().getSelected()) {
        if (value.length() > 0) {
          value += ",";
        }
        value += entry.getValue().getPosition();
      }

    }

    final String finalValue = value;
    mHandler.post(new Runnable() {
      @Override
      public void run() {

        adapter.onDataChanged(groupPosition);

        adapter.notifyDataSetChanged();

      }
    });

  }


  public String getCheckedValues(final int groupPosition, int childPosition) {

    String value = "";

    Map<Integer, Input> choicesValues = choices.get(groupPosition);
    if (choicesValues == null) {
      choicesValues = new HashMap<>();
    }

    for (Map.Entry<Integer, Input> entry : choicesValues.entrySet()) {

      if (entry.getValue().getSelected()) {
        if (value.length() > 0) {
          value += ",";
        }
        value += entry.getValue().getPosition();
      }

    }
    return value;

  }


  public void updateTextViewMap(int groupPosition, TextView selectedAnswers) {
    questionChoices.put(groupPosition, selectedAnswers);
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

  private class backgroundProcess extends AsyncTask<Integer, Integer, Long> {

    protected Long doInBackground(Integer... params) {

      gradebookObject = gradeBookManager.getGradebookObject();

      if (gradebookObject == null) {
        gradebookObject = new GradebookObject();
      }

      gradebookObject.getQuizList().add(quiz);

      gradeBookManager.addGadebook(gradebookObject);

      try {
        userManager.addDocument(userObject, Constants.USERS);
      } catch (Exception e) {
        e.printStackTrace();
      }

      long totalSize = 0;
      return totalSize;
    }

    protected void onPostExecute(Long result) {
      hideProgessDialog();
      schoolCensus.showMessageDialogAlert(getActivity(),
                                          "Quiz successfully submitted, please proceed to gradebook to view your results",
                                          "Info");
      mainView.loadView(QuizListViewContent.newInstance(1));

    }

    protected void onPreExecute() {
      showProgessDialog();
    }

  }


}
