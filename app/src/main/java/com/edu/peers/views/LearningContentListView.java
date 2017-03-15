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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.adapter.ComboBoxViewAdapter;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.adapter.ContentFileExpandableListViewAdapter;
import com.edu.peers.adapter.ContentListViewAdapter;
import com.edu.peers.dialogs.ContentCreationDialogView;
import com.edu.peers.managers.ContentManager;
import com.edu.peers.models.Content;
import com.edu.peers.models.ContentFile;
import com.edu.peers.models.ContentObject;
import com.edu.peers.others.Base64;
import com.edu.peers.others.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by nelson on 3/16/15.
 */
public class LearningContentListView extends Fragment implements
                                           View.OnClickListener,   ExpandableListView.OnChildClickListener,
                                           ExpandableListView.OnGroupClickListener,AdapterView.OnItemClickListener {

  public HashMap<String, ComboBoxViewListItem>
      classArray =
      new HashMap<String, ComboBoxViewListItem>();
  public ComboBoxViewListItem[] comboBoxViewListItems;
  private List<Content> array_sort = new ArrayList<>();
  private List<Content> sampleData = new ArrayList<>();
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
  private ContentManager contentManager;
  private ContentObject contentObject;
  public List<Map<String, Content>> groupData = new ArrayList<Map<String, Content>>();
  public List<List<Map<String, ContentFile>>> childData = new ArrayList<List<Map<String, ContentFile>>>();
  private static final String NAME = "NAME";
  private ContentFileExpandableListViewAdapter adapter;
  private String mFileName;

  public static LearningContentListView newInstance(int position) {
    LearningContentListView f = new LearningContentListView();
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
    schoolCensus.setCurrentTitle(Constants.LearningContentListView);
    contentManager =
        new ContentManager(schoolCensus.getCloudantInstance(), getContext());

    mainView = schoolCensus.getMainView();
    mainView.showAddButton();
    mainView.setAddButtonTag(4);

    mainView.setTitle("Edu Peer > Learning Content");

  }

  void openDialog(final int index) {

    schoolCensus.setState(Constants.STUDENT_DATA);

    schoolCensus.setCurrentActivity(getActivity());
    schoolCensus.setCurrentFragment(this);
    final ContentCreationDialogView
        newFragment = ContentCreationDialogView.newInstance(mStackLevel);
    newFragment.setLearningContentListView(this);
    mHandler.post(new Runnable() {
      @Override
      public void run() {

        mStackLevel++;

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        newFragment.show(ft, "StudentDataDialog111");

      }
    });

  }

  public void setDrawerAdapter() {

    if (contentObject!=null) {

      List<Content> questionsList = contentObject.getContentList();

      groupData = new ArrayList<Map<String, Content>>();
      childData = new ArrayList<List<Map<String, ContentFile>>>();
      // add data in group and child list
      for (int i = 0; i < questionsList.size(); i++) {
        Map<String, Content> curGroupMap = new HashMap<String, Content>();
        groupData.add(curGroupMap);
        curGroupMap.put(NAME, questionsList.get(i));

        List<Map<String, ContentFile>> children = new ArrayList<Map<String, ContentFile>>();

        //  String[][] childItems = {{}, {}, {}, {}, {}};
        if (questionsList.get(i).getContentFileList() != null) {
          for (int j = 0; j < questionsList.get(i).getContentFileList().size(); j++) {
            Map<String, ContentFile> curChildMap = new HashMap<String, ContentFile>();
            children.add(curChildMap);
            curChildMap.put(NAME, questionsList.get(i).getContentFileList().get(j));
          }
        }

        childData.add(children);
      }

      String groupFrom[] = {NAME};
      int groupTo[] = {R.id.titleName};
      String childFrom[] = {NAME};
      int childTo[] = {R.id.childname};

      adapter =
          new ContentFileExpandableListViewAdapter(
              this,
              groupData,
              R.layout.group_row_content, groupFrom, groupTo,
              childData,
              R.layout.child_row_content,
              childFrom, childTo
          );

      listView.setAdapter(adapter);

      listView.setOnChildClickListener(this);

      listView.setOnGroupClickListener(this);

    }
  }

  public void addContent(Content content) {





    new backgroundProcessSave().execute(content);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.content_list_view, null);

    spinner = (Spinner) view.findViewById(R.id.county);

    loadSpinnerData();
    spinner.setOnItemSelectedListener(new OnStudentSelected());
    spinner.setOnTouchListener(onSubjectTouchListener);
    spinner.setVisibility(View.GONE);

    progressBar = (LinearLayout) view.findViewById(R.id.progressBarSchools);

    progressBar.setVisibility(View.GONE);

    listView = (ExpandableListView) view.findViewById(R.id.contentList);

    listView.setOnItemClickListener(this);

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



  }


  public void add(final List<Content> homeItemList) {

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
          .get(i).getTitle()
          .toLowerCase()
          .contains(value)

          ) {

        array_sort.add(sampleData.get(i));
      }
    }
    AppendList(array_sort);
  }

  void AppendList(List<Content> str) {
//    listView.setAdapter(new ContentListViewAdapter(this, str));
    setDrawerAdapter();
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
    schoolCensus.setCurrentTitle(Constants.LearningContentListView);
    schoolCensus.setCurrentFragment(this);

    mainView = schoolCensus.getMainView();
    mainView.showAddButton();
    mainView.setAddButtonTag(4);

    mainView.setTitle("Edu Peer > Learning Content");

    add(sampleData);
    new backgroundProcess().execute();
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);

//    AdapterView.AdapterContextMenuInfo
//        info =
//        (AdapterView.AdapterContextMenuInfo) menuInfo;
//
//    if (v.getId() == R.id.schools_list_view) {
//      schoolCensus.setSelectedSchoolIndex(info.position);
////      Intent ivE = new Intent(this, SchoolRegistrationView.class);
////      startActivity(ivE);
//
//    }
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

  @Override
  public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                              int childPosition, long id) {

    ContentFile contentFile= childData.get(groupPosition).get(childPosition).get(NAME);
    schoolCensus.setContentFile(contentFile);
    schoolCensus.setContentFileUUID(contentFile.getUuid());
    schoolCensus.setContentUUID(groupData.get(groupPosition).get(NAME).getUuid());

    mainView.loadView(DocumentView.newInstance(1));

//    openWriteDialog(contentFile.getContent(),contentFile.getFileName());


    return false;
  }
  public void openDialog(String content, String fileName) {


    // Record to the external cache directory for visibility
    mFileName = getActivity().getExternalCacheDir().getAbsolutePath();
    mFileName += "/test----1111----"+fileName;


    byte[] decodedString = new byte[0];
    try {

      Log.i(Constants.TAG,""+mFileName);
      FileOutputStream fos = new FileOutputStream(mFileName);
      fos.write(Base64.decode(content));
      fos.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
    return false;
  }

  private class backgroundProcess extends AsyncTask<Integer, Integer, Long> {

    protected Long doInBackground(Integer... params) {

      contentObject = contentManager.getContentObject();

      if (contentObject != null) {
        sampleData = contentObject.getContentList();
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


  private class backgroundProcessSave extends AsyncTask<Content, Content, Long> {

    protected Long doInBackground(Content... params) {

      if (contentObject == null) {
        contentObject = new ContentObject();
      }

      List<Content> quListList = contentObject.getContentList();

      quListList.add(params[0]);

      contentObject.setContentList(quListList);
//      contentObject.setContentList(new ArrayList<Content>());


      contentManager.addContent(contentObject);

      contentObject=contentManager.getContentObject();
      if (contentObject==null)
        contentObject= new ContentObject();

      sampleData = contentObject.getContentList();

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

  public void updateList() {
    sampleData = contentObject.getContentList();


    listView.setAdapter(new ContentListViewAdapter(this, sampleData));

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


}
