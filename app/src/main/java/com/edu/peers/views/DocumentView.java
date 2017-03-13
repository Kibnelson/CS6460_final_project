package com.edu.peers.views;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.peers.R;
import com.edu.peers.adapter.CommentsExpandableListViewAdapter;
import com.edu.peers.dialogs.CommentDialogView;
import com.edu.peers.dialogs.RecordAudioDialog;
import com.edu.peers.dialogs.ScratchPadDialog;
import com.edu.peers.managers.ContentCommentsManager;
import com.edu.peers.managers.ContentManager;
import com.edu.peers.models.Content;
import com.edu.peers.models.ContentComments;
import com.edu.peers.models.ContentCommentsObject;
import com.edu.peers.models.ContentFile;
import com.edu.peers.models.ContentObject;
import com.edu.peers.models.Input;
import com.edu.peers.others.Base64;
import com.edu.peers.others.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by nelson on 3/16/15.
 */
public class DocumentView extends Fragment implements
                                           View.OnClickListener,
                                           ExpandableListView.OnChildClickListener,
                                           ExpandableListView.OnGroupClickListener,
                                           AdapterView.OnItemClickListener {

  private SchoolCensus schoolCensus;
  private View view;
  private TextView documentName;
  private GridView documentsGridview;
  private ImageView documentView;
  private ExpandableListView documentStatusList;
  final String schoolMainList[] = {
      "Comments"
  };

  private static final String NAME = "NAME";
  private ProgressDialog progressDialog;
  private ImageView personImage;
  private List<Input> events;
  private String personId;
  private LinearLayout approved;
  private LinearLayout approveButtons;
  private Button info;
  private ContentFile contentFile;
  private com.github.barteksc.pdfviewer.PDFView pdfView;
  private Button commentButton;
  private Handler mHandler;
  private int mStackLevel = 0;
  private RecordAudioDialog recordAudioDialog;
  private ScratchPadDialog scratchPadDialog;
  private ArrayList<Map<String, String>> groupData;
  private ArrayList<List<Map<String, Input>>> childData;
  private Button closeDocument;
  private LearningContentListView learningContentListView;
  private MainView mainView;
  private ContentManager contentManager;
  private ContentCommentsManager contentCommentsManager;
  private ContentObject contentObject;
  private List<Content> sampleData;
  private LinearLayout progressBar;
  private String contentFileUUID = null;
  private String contentUUID = null;
  private Content contentFound = null;
  private ContentFile contentFileFound;
  private ContentCommentsObject contentObjectComments;
  private List<ContentComments> contentCommentsList;

  public static DocumentView newInstance(int position) {
    DocumentView f = new DocumentView();
    Bundle b = new Bundle();
    f.setArguments(b);
    return f;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    schoolCensus = (SchoolCensus) getActivity().getApplication();
    schoolCensus.setCurrentFragment(this);
    contentFile = schoolCensus.getContentFile();
    schoolCensus.getMainView().hideAddButton();
    mainView = schoolCensus.getMainView();
    contentManager =
        new ContentManager(schoolCensus.getCloudantInstance(), getContext());
    contentCommentsManager =
        new ContentCommentsManager(schoolCensus.getCloudantInstance(), getContext());

    contentFileUUID = schoolCensus.getContentFileUUID();
    contentUUID = schoolCensus.getContentUUID();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

    view = inflater.inflate(R.layout.document_view, null);
    commentButton = (Button) view.findViewById(R.id.commentButton);
    commentButton.setOnClickListener(this);
    progressBar = (LinearLayout) view.findViewById(R.id.progressBarSchools);
    progressBar.setVisibility(View.GONE);
    closeDocument = (Button) view.findViewById(R.id.closeDocument);
    closeDocument.setOnClickListener(this);
    this.mHandler = new Handler(Looper.getMainLooper());
    documentName = (TextView) view.findViewById(R.id.documentName);
    pdfView = (com.github.barteksc.pdfviewer.PDFView) view.findViewById(R.id.pdfviewer);
    documentView = (ImageView) view.findViewById(R.id.imageView);

    new backgroundProcess().execute();

    return view;
  }

  public void loadDocument() {
    if (contentFile != null) {
      documentStatusList = (ExpandableListView) view.findViewById(R.id.documentStatusList);

      setDocumentList();

      documentName.setText(contentFile.getFileName());

      if (contentFile.getFileName().contains("pdf")) {
        documentView.setVisibility(View.GONE);
        pdfView.setVisibility(View.VISIBLE);

        String content = contentFile.getContent();

        String mFileName = getActivity().getExternalCacheDir().getAbsolutePath();

        mFileName += "/test----1111----" + contentFile.getFileName();

        try {

          File file2 = new File(mFileName);
          FileOutputStream os = new FileOutputStream(file2, true);
          os.write(Base64.decode(content));
          os.close();

          pdfView.fromFile(file2)
              .enableSwipe(true)
              .swipeHorizontal(false)
              .enableDoubletap(true)
              .defaultPage(0)
              .enableAnnotationRendering(false)
              .password(null)
              .scrollHandle(null)
              .load();

        } catch (IOException e) {
          e.printStackTrace();
        }
      } else if (contentFile.getFileName().contains("png") || contentFile.getFileName()
          .contains("jpg")) {
        documentView.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.GONE);
        byte[] decodedString = new byte[0];
        try {
          decodedString = Base64.decode(contentFile.getContent());
          Bitmap
              decodedByte =
              BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
          documentView.setImageBitmap(decodedByte);
        } catch (IOException e) {
          e.printStackTrace();
        }


      }
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

  private List createChildSchoolList(List<Input> schoolList) {
    ArrayList result = new ArrayList();

    for (int i = 0; i < 1; ++i) {
      ArrayList secList = new ArrayList();
      for (int n = 0; n < schoolList.size(); n++) {
        HashMap child = new HashMap();
        child.put("shadeName", "");

//        schoolList.get(n).getUploadedBy().getPersonCredential().getUsername()
        secList.add(child);
      }
      result.add(secList);

    }
    return result;
  }

  public void setDocumentList() {

    groupData = new ArrayList<Map<String, String>>();
    childData = new ArrayList<List<Map<String, Input>>>();
    // add data in group and child list
    for (int i = 0; i < 1; i++) {
      Map<String, String> curGroupMap = new HashMap<String, String>();
      groupData.add(curGroupMap);
      curGroupMap.put(NAME, "Comments");

      List<Map<String, Input>> children = new ArrayList<Map<String, Input>>();

      for (int k = 0; k < events.size(); k++) {

        Input input = events.get(k);

        //  String[][] childItems = {{}, {}, {}, {}, {}};
        if (input != null) {
          Map<String, Input> curChildMap = new HashMap<String, Input>();
          children.add(curChildMap);
          curChildMap.put(NAME, input);

        }

      }

      childData.add(children);
    }

    String groupFrom[] = {NAME};
    int groupTo[] = {R.id.titleName};
    String childFrom[] = {NAME};
    int childTo[] = {R.id.childname};

    CommentsExpandableListViewAdapter adapter =
        new CommentsExpandableListViewAdapter(
            this,
            groupData,
            R.layout.group_row, groupFrom, groupTo,
            childData,
            R.layout.child_row_comments,
            childFrom, childTo
        );

//    listView.setAdapter(adapter);
//
//    listView.setOnChildClickListener(this);
//
//    listView.setOnGroupClickListener(this);
//
//    List mainList, subList;
//
//    mainList = createGroupList(schoolMainList);
//
//    subList = createChildSchoolList(events);
//
//    adapter = new CommentsExpandableListViewAdapter(
//        this,
//        mainList,
//        R.layout.group_row,
//        new String[]{"menu"},
//        new int[]{R.id.titleName},
//        subList,
//        R.layout.child_row_comments,
//        new String[]{"shadeName"},
//        new int[]{R.id.childname}, events
//    );

    documentStatusList.setTag(2);

    documentStatusList.setAdapter(adapter);

    documentStatusList.setOnChildClickListener(this);

    documentStatusList.setOnGroupClickListener(this);


  }

  @Override
  public void onClick(View v) {

    if (v == commentButton) {
      // Open another dialog for comments

      openDialog();

    } else if (v == closeDocument) {
      mainView.loadView(LearningContentListView.newInstance(1));
    }
  }

  void openDialog() {

    schoolCensus.setState(Constants.STUDENT_DATA);

    schoolCensus.setCurrentActivity(getActivity());
    schoolCensus.setCurrentFragment(this);
    final CommentDialogView
        newFragment = CommentDialogView.newInstance(mStackLevel);
    newFragment.setDocumentView(this);
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
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

  }


  @Override
  public void onResume() {
    super.onResume();

  }

  @Override
  public void onPause() {
    super.onPause();

  }

  @Override
  public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1,
                              long l) {

    Input documents = events.get(i1);

    return false;
  }

  @Override
  public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
    return false;
  }

  public String getPersonId() {
    return personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }

  public void addInput(Input input) {
    events.add(input);
    setDocumentList();
    contentFile.setInputList(events);

    new backgroundProcessSave().execute(input);
  }

  private class MyBrowser extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      view.loadUrl(url);
      return true;
    }
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


  private class backgroundProcess extends AsyncTask<Integer, Integer, Long> {

    protected Long doInBackground(Integer... params) {

      contentObject = contentManager.getContentObject();

      contentObjectComments = contentCommentsManager.getContentObjectComments();
      events = new ArrayList<>();

      if (contentObjectComments != null) {
        contentCommentsList = contentObjectComments.getContentCommentsList();

        for (ContentComments contentComments : contentCommentsList) {
          events.add(contentComments.getInput());

        }
      }

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

      loadDocument();

    }

    protected void onPreExecute() {
      if (progressBar != null) {
        progressBar.setVisibility(View.VISIBLE);
      }
    }


  }


  public class backgroundProcessSave extends AsyncTask<Input, Input, Long> {


    protected Long doInBackground(Input... params) {

      Input input = params[0];

      if (contentUUID != null && contentFileUUID != null) {

        if (contentObjectComments==null)
          contentObjectComments =new ContentCommentsObject();

        List<ContentComments> contentCommentsList = contentObjectComments.getContentCommentsList();
        contentCommentsList.add(new ContentComments(contentUUID, contentFileUUID, input));
        contentObjectComments.setContentCommentsList(contentCommentsList);
        contentCommentsManager.addContentComment(contentObjectComments);


      }

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
}
