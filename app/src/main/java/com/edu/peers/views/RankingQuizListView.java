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
import com.edu.peers.adapter.RankingListViewAdapter;
import com.edu.peers.dialogs.QuizCreationDialogView;
import com.edu.peers.managers.GradeBookManager;
import com.edu.peers.managers.QuizManager;
import com.edu.peers.models.GradebookObject;
import com.edu.peers.models.Quiz;
import com.edu.peers.models.UserObject;
import com.edu.peers.others.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


/**
 * Created by nelson on 3/16/15.
 */
public class RankingQuizListView extends Fragment implements
                                           View.OnClickListener, AdapterView.OnItemClickListener {

  public HashMap<String, ComboBoxViewListItem>
      classArray =
      new HashMap<String, ComboBoxViewListItem>();
  public ComboBoxViewListItem[] comboBoxViewListItems;
  private List<Quiz> array_sort = new ArrayList<>();
  private List<Quiz> sampleData = new ArrayList<>();
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

  public static RankingQuizListView newInstance(int position) {
    RankingQuizListView f = new RankingQuizListView();
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

    quizManager =
        new QuizManager(schoolCensus.getCloudantInstance(), getContext());

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

  public void addQuiz(Quiz quiz) {
//    new backgroundProcessSave().execute(quiz);
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

    add(sampleData);

    new backgroundProcess().execute();

    return view;
  }


  @Override
  public void onClick(View v) {

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    schoolCensus.setSelectedSchoolIndex(position);
    schoolCensus.setQuizIndex(quizIndex);

//    Intent ivE = new Intent(this, SchoolHomeGridView.class);
//    Intent ivE = new Intent(this, MapView.class);
//    startActivity(ivE);

    schoolCensus.setQuiz(sampleData.get(position));
    mainView.loadView(RankingUserQuizListView.newInstance(1));

  }

  public void add(final List<Quiz> homeItemList) {

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
          .get(i).getName()
          .toLowerCase()
          .contains(value)

          ) {

        array_sort.add(sampleData.get(i));
      }
    }
    AppendList(array_sort);
  }

  void AppendList(List<Quiz> str) {

    listView.setAdapter(new RankingListViewAdapter(this, str));
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

    new backgroundProcess().execute();
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

  public boolean checkIfListContains(List<Quiz> list, String uuid){
    boolean exists=false;
    for (Quiz quiz:list){
      if (quiz.getUuid().equalsIgnoreCase(uuid)){
        exists=true;
      }
    }

    return exists;
  }
  private class backgroundProcess extends AsyncTask<Integer, Integer, Long> {

    protected Long doInBackground(Integer... params) {
      sampleData= new ArrayList<>();
      gradebookObject = gradeBookManager.getGradebookObject();

      if (gradebookObject != null) {

        List<Quiz> quizList=gradebookObject.getQuizList();
        for (int y=0;y<quizList.size();y++){
          Quiz quiz=quizList.get(y);

          if (!checkIfListContains(sampleData,quiz.getUuid())&& quiz.getShareWork())
              sampleData.add(quiz);
        }
      }

      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {
      if (progressBar != null) {
        progressBar.setVisibility(View.GONE);
      }

      AppendList(sampleData);

      listView.invalidateViews();
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
