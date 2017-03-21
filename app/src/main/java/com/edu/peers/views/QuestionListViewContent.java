package com.edu.peers.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.adapter.QuestionResponsesListViewAdapter;
import com.edu.peers.adapter.QuizExpandableListViewAdapter;
import com.edu.peers.dialogs.QuestionResponseCreationDialogView;
import com.edu.peers.dialogs.RecordAudioDialog;
import com.edu.peers.dialogs.ScratchPadDialog;
import com.edu.peers.managers.QuestionManager;
import com.edu.peers.managers.QuestionsResponsesManager;
import com.edu.peers.models.Input;
import com.edu.peers.models.Questions;
import com.edu.peers.models.QuestionsResponses;
import com.edu.peers.models.QuestionsResponsesObject;
import com.edu.peers.models.UserObject;
import com.edu.peers.others.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by nelson on 3/16/15.
 */
public class QuestionListViewContent extends Fragment implements
                                                      View.OnClickListener,
                                                      ExpandableListView.OnChildClickListener,
                                                      ExpandableListView.OnGroupClickListener,
                                                      AdapterView.OnItemClickListener {

  public HashMap<String, ComboBoxViewListItem>
      classArray =
      new HashMap<String, ComboBoxViewListItem>();
  public ComboBoxViewListItem[] comboBoxViewListItems;
  private List<Input> array_sort = new ArrayList<>();
//  private List<Input> sampleData = new ArrayList<>();
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
  private Questions questions;
  private RecordAudioDialog recordAudioDialog;
  private ScratchPadDialog scratchPadDialog;
  public List<Map<String, Questions>> groupData = new ArrayList<Map<String, Questions>>();
  public List<List<Map<String, Input>>> childData = new ArrayList<List<Map<String, Input>>>();
  private QuizExpandableListViewAdapter adapter;
  private TextView input;
  private ImageView voice_choice_1;
  private ImageView write_choice_1;
  private String voiceInputString, writeInputString;
  private List<Input> questionsList;
  private List<QuestionsResponses> questionsResponsesList;
  private QuestionManager questionManager;
  private QuestionsResponsesManager questionsResponsesManager;
  private String questionsUUID;
  private QuestionsResponsesObject questionsResponsesObject;
  private int inputPosition;
  private UserObject userObject;


  public static QuestionListViewContent newInstance(int position) {
    QuestionListViewContent f = new QuestionListViewContent();
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
    schoolCensus.setCurrentTitle(Constants.QuestionsListViewContent);

   userObject= schoolCensus.getUserObject();

    questionManager =
        new QuestionManager(schoolCensus.getCloudantInstance(), getContext());

    questionsResponsesManager =
        new QuestionsResponsesManager(schoolCensus.getCloudantInstance(), getContext());


    questions = schoolCensus.getQuestions();
    questionsUUID = schoolCensus.getQuestionsUUID();
//    sampleData = questions.getResponses();
//    questionsList = questions.getResponses();

    questionsList= new ArrayList<>();
    questionsResponsesList= new ArrayList<>();

    mainView = schoolCensus.getMainView();
    mainView.showAddButton();
    mainView.setAddButtonTag(3);
    mainView.setTitle("Edu Peer > Questions > View");


  }

  void openDialog(final int index) {

    schoolCensus.setState(Constants.STUDENT_DATA);

    schoolCensus.setCurrentActivity(getActivity());
    schoolCensus.setCurrentFragment(this);

    final QuestionResponseCreationDialogView
        newFragment = QuestionResponseCreationDialogView.newInstance(mStackLevel);
    newFragment.setQuestionListViewContent(this);
    mHandler.post(new Runnable() {
      @Override
      public void run() {

        mStackLevel++;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        newFragment.show(ft, "StudentDataDialog111");

      }
    });

  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.question_list_view_content, null);

    listView = (ListView) view.findViewById(R.id.questions_list_view);
    progressBar = (LinearLayout) view.findViewById(R.id.progressBarSchools);
    progressBar.setVisibility(View.GONE);
    input = (TextView) view.findViewById(R.id.input);
    voice_choice_1 = (ImageView) view.findViewById(R.id.voice_choice_1);
    voice_choice_1.setOnClickListener(this);
    write_choice_1 = (ImageView) view.findViewById(R.id.write_choice_1);
    write_choice_1.setOnClickListener(this);
    createdBy = (TextView) view.findViewById(R.id.createdBy);
    dateCreated = (TextView) view.findViewById(R.id.dateCreated);

    if (questions != null) {

      input.setText(questions.getQuestionInput());
      voiceInputString = questions.getQuestionVoice();
      if (voiceInputString.length() > 0) {
        voice_choice_1.setVisibility(View.VISIBLE);
      } else {
        voice_choice_1.setVisibility(View.GONE);
      }

      writeInputString = questions.getQuestionWriting();
      if (writeInputString.length() > 0) {
        write_choice_1.setVisibility(View.VISIBLE);

      } else {
        write_choice_1.setVisibility(View.GONE);
      }

      dateCreated.setText(questions.getDate());
      if (questions.getUser() != null) {
        if (questions.getUser().getRole().equalsIgnoreCase("Instructor"))
          createdBy.setText(
              "Instructor: " + questions.getUser().getFirstName() + " " + questions.getUser().getLastName());
        else
          createdBy.setText(
              "Student: " + questions.getUser().getFirstName() + " " + questions.getUser().getLastName());

      } else {
        createdBy.setText("Instructor ");
      }
    }

    new backgroundProcess().execute();

    return view;
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

  @Override
  public void onClick(View view) {
    if (write_choice_1 == view) {

      if (writeInputString.length() > 0) {
        openWriteDialog(writeInputString);
      } else {
        Toast.makeText(getContext(), "There is no handwritten information",
                       Toast.LENGTH_LONG).show();
      }


    } else if (voice_choice_1 == view) {

      if (voiceInputString.length() > 0) {
        openRecordDialog(voiceInputString);
      } else {
        Toast.makeText(getContext(), "There is no recorded audio", Toast.LENGTH_LONG)
            .show();
      }

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
    schoolCensus.setCurrentTitle(Constants.QuestionsListViewContent);
    schoolCensus.setCurrentFragment(this);

  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);

    AdapterView.AdapterContextMenuInfo
        info =
        (AdapterView.AdapterContextMenuInfo) menuInfo;

    if (v.getId() == R.id.questions_list_view) {

      if (userObject.getUser().getRole().equalsIgnoreCase("Instructor")){


        menu.setHeaderTitle(questionsList.get(info.position).getQuestionInput());
        MenuInflater inflater = this.getActivity().getMenuInflater();
        MenuItem item = menu.findItem(R.id.recommend);

        inflater.inflate(R.menu.menu_instructors, menu);
      }
      else{


        menu.setHeaderTitle(questionsList.get(info.position).getQuestionInput());
        MenuInflater inflater = this.getActivity().getMenuInflater();
        MenuItem item = menu.findItem(R.id.recommend);
        inflater.inflate(R.menu.menu_students, menu);

      }



    }
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {

    AdapterView.AdapterContextMenuInfo
        info =
        (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
    int index = info.position;
    inputPosition=index;
    QuestionsResponses input=questionsResponsesList.get(info.position);
    switch (item.getItemId()) {

      case R.id.recommend:

        input.getInput().setRecommended(true);
        new backgroundProcessSaveUpdate().execute(input);
        return true;
      case R.id.thumbsup:
        int current=input.getInput().getThumbsUp();
        int newNumber=current+1;
        input.getInput().setThumbsUp(newNumber);
        new backgroundProcessSaveUpdate().execute(input);
        return true;
      case R.id.thumbsdown:
        current=input.getInput().getThumbsDown();
        newNumber=current-1;
        input.getInput().setThumbsDown(newNumber);
        new backgroundProcessSaveUpdate().execute(input);
        return true;

      default:
        return super.onContextItemSelected(item);
    }


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




  private class backgroundProcess extends AsyncTask<Integer, Integer, Long> {

    protected Long doInBackground(Integer... params) {
      questionsList= new ArrayList<>();
      questionsResponsesList =new ArrayList<>();

      questionsResponsesObject= questionsResponsesManager.getQuestionsResponsesObject();
      if (questionsResponsesObject!=null) {
        List<QuestionsResponses>
            questionsResponsesList1 =
            questionsResponsesObject.getQuestionsResponsesList();

        for (QuestionsResponses questionsResponses:questionsResponsesList1){
          if (questionsResponses.getQuestionUUID().equalsIgnoreCase(questionsUUID))
          {
            questionsList.add(questionsResponses.getInput());
            questionsResponsesList.add(questionsResponses);
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
      if (comboBoxViewAdapter != null) {
        spinner.setAdapter(comboBoxViewAdapter);
        spinner.setSelection(0);

      }

      loadDataToView();


      listView.invalidateViews();
    }

    protected void onPreExecute() {
      if (progressBar != null) {
        progressBar.setVisibility(View.VISIBLE);
      }
    }

  }

  private void loadDataToView() {

    listView.setAdapter(new QuestionResponsesListViewAdapter(this, questionsList));
    registerForContextMenu(listView);
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


  public void thumbsUpClicked(Input input, int inputPosition) {

    QuestionsResponses questionsResponses=questionsResponsesList.get(inputPosition);
    int current=questionsResponses.getInput().getThumbsUp();
    int  newNumber=current+1;
    questionsResponses.getInput().setThumbsUp(newNumber);
    new backgroundProcessSaveUpdate().execute(questionsResponses);



//    this.groupPosition=groupPosition;
//    this.childPosition=childPosition;
//    int current= quiz.getQuestions().get(groupPosition).getChoices().get(childPosition).getThumbsUp();
//    int newNumber=current+1;
//    quiz.getQuestions().get(groupPosition).getChoices().get(childPosition).setThumbsUp(newNumber);
//
//    schoolCensus.setQuiz(quiz);
//
//    Log.i(Constants.TAG, "thumbsUpClickedthumbsUpClickedthumbsUpClicked==" + quiz.getQuestions().get(groupPosition).getChoices().get(childPosition).getThumbsUp());
//
//
//    new backgroundProcessSave().execute(quiz);


  }

  public void thumbsDownClicked(Input input, int inputPosition) {


    QuestionsResponses questionsResponses=questionsResponsesList.get(inputPosition);
    int current=questionsResponses.getInput().getThumbsDown();
    int  newNumber=current-1;
    questionsResponses.getInput().setThumbsDown(newNumber);
    new backgroundProcessSaveUpdate().execute(questionsResponses);



//    int current= quiz.getQuestions().get(groupPosition).getChoices().get(childPosition).getThumbsDown();
//    int newNumber=current-1;
//    quiz.getQuestions().get(groupPosition).getChoices().get(childPosition).setThumbsDown(newNumber);
//
//    schoolCensus.setQuiz(quiz);
//
//    new backgroundProcessSave().execute(quiz);


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

  public void addInput(Input input){
    questionsList.add(input);
    listView.setAdapter(new QuestionResponsesListViewAdapter(this, questionsList));
    new backgroundProcessSave().execute(input);
  }
  private class backgroundProcessSave extends AsyncTask<Input, Input, Long> {

    protected Long doInBackground(Input... params) {

      Input  input=params[0];

      questionsResponsesObject= questionsResponsesManager.getQuestionsResponsesObject();

      if (questionsResponsesObject==null) {
        questionsResponsesObject= new QuestionsResponsesObject();
      }
      List<QuestionsResponses>
          questionsResponsesList =
          questionsResponsesObject.getQuestionsResponsesList();

      questionsResponsesList.add( new QuestionsResponses(questionsUUID,input));

      questionsResponsesObject.setQuestionsResponsesList(questionsResponsesList);
      questionsResponsesManager.addQuestionResponse(questionsResponsesObject);


      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {
      if (progressBar != null) {
        progressBar.setVisibility(View.GONE);
      }

    }

    protected void onPreExecute() {
      if (progressBar != null) {
        progressBar.setVisibility(View.VISIBLE);
      }
    }

  }
  private class backgroundProcessSaveUpdate extends AsyncTask<QuestionsResponses, QuestionsResponses, Long> {

    protected Long doInBackground(QuestionsResponses... params) {

      QuestionsResponses  input=params[0];

      questionsResponsesObject= questionsResponsesManager.getQuestionsResponsesObject();

      if (questionsResponsesObject==null) {
        questionsResponsesObject= new QuestionsResponsesObject();
      }
      List<QuestionsResponses>
          questionsResponsesList =
          questionsResponsesObject.getQuestionsResponsesList();

//      questionsResponsesList.add( new QuestionsResponses(questionsUUID,input));

      questionsResponsesList.set(inputPosition,input);


      questionsResponsesObject.setQuestionsResponsesList(questionsResponsesList);
      questionsResponsesManager.addQuestionResponse(questionsResponsesObject);


      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {
      if (progressBar != null) {
        progressBar.setVisibility(View.GONE);
      }

      new backgroundProcess().execute();

    }

    protected void onPreExecute() {
      if (progressBar != null) {
        progressBar.setVisibility(View.VISIBLE);
      }
    }

  }

}
