package com.edu.peers.dialogs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.edu.peers.R;
import com.edu.peers.others.Base64;
import com.edu.peers.others.Utils;
import com.edu.peers.views.SchoolCensus;
import com.edu.peers.views.RegistrationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//import com.edu.peers.views.StudentRegistrationView;


/**
 * Created by nelson on 3/16/15.
 */

public class RecordAudioDialog extends DialogFragment
    implements View.OnClickListener, VoiceView.OnRecordListener {

  private static int dataDialogOption;
  private int cameraFrontOrBack;
  private Button cancelButton;
  private Camera camera;
  private float rotation = 0;
  private Camera.CameraInfo mBackCameraInfo;
  private Button captureButton;
  private String bitmapImageString;
  private View view;
  private boolean pictureTaken = false;
  private Bitmap originalImage;
  private SchoolCensus schoolCensus;
  //  private StudentRegistrationView studentRegistrationView;
  private byte[] originalImagedata;
  private RegistrationView registrationView;
  private Camera.CameraInfo cameraInfo;

  private static final String LOG_TAG = "AudioRecordTest";
  private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
  private static String mFileName = null;
  private QuestionCreationDialogView questionCreationDialogView;
  private OneQuestionCreationDialogView oneQuestionCreationDialogView;
  private QuestionViewDialogView questionViewDialogView;
  private QuestionResponseCreationDialogView questionResponseCreationDialogView;
  private String recordedString = null;
  private MediaRecorder mRecorder = null;

  private MediaPlayer mPlayer = null;

  // Requesting permission to RECORD_AUDIO
  private boolean permissionToRecordAccepted = false;
  private String[] permissions = {Manifest.permission.RECORD_AUDIO};

  public boolean mStartRecording = true;
  public boolean mStartPlaying = true;
  private Button playbutton;
  private VoiceView mVoiceView;
  private Handler mHandler;
  private CommentDialogView commentDialogView;


  public static RecordAudioDialog newInstance(int num, int dialogOption) {
    RecordAudioDialog f = new RecordAudioDialog();
    dataDialogOption = dialogOption;
    Bundle args = new Bundle();
    args.putInt("num", num);
    f.setArguments(args);

    return f;
  }

  public static Bitmap rotateBitmap(Bitmap source, float angle) {
    Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    mHandler = new Handler(Looper.getMainLooper());
    view = inflater.inflate(R.layout.record_audio_layout, null);
    schoolCensus = (SchoolCensus) getActivity().getApplication();

    // Record to the external cache directory for visibility
    mFileName = getActivity().getExternalCacheDir().getAbsolutePath();
    mFileName += "/audiorecordtest.3gp";

    ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

    return view;

  }

  @Override
  public void onClick(View v) {
    /** When OK Button is clicked, dismiss the school_calendar_view_dialog */
    if (v == cancelButton) {
      if (recordedString == null && questionCreationDialogView!=null)
       questionCreationDialogView.setRecordFile(mFileName);
      else if (oneQuestionCreationDialogView!=null)
        oneQuestionCreationDialogView.setRecordFile(mFileName);
      else if (recordedString != null)
        dismiss();
      else if (commentDialogView!=null)
        commentDialogView.setRecordFile(mFileName);
      else
      questionResponseCreationDialogView.setRecordFile(mFileName);


      dismiss();

    } else if (v == captureButton) {

//      String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
////      int color = getResources().getColor(R.color.colorPrimaryDark);
//      int requestCode = 0;
//      AndroidAudioRecorder.with(getActivity())
//          // Required
//          .setFilePath(filePath)
////          .setColor(color)
//          .setRequestCode(requestCode)
//
//          // Optional
//          .setSource(AudioSource.MIC)
//          .setChannel(AudioChannel.STEREO)
//          .setSampleRate(AudioSampleRate.HZ_48000)
//          .setAutoStart(true)
//          .setKeepDisplayOn(true)
//
//          // Start recording
//          .record();

      onRecord(mStartRecording);

      if (mStartRecording) {

        mVoiceView.mState = mVoiceView.STATE_RECORDING;
        mVoiceView.invalidate();
        playbutton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);

        captureButton.setText("Stop");
      } else {
        playbutton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        mVoiceView.mState = mVoiceView.STATE_NORMAL;
        mVoiceView.invalidate();
        captureButton.setText("Record");
      }
      mStartRecording = !mStartRecording;


    } else if (v == playbutton) {

      onPlay(mStartPlaying);
      if (mStartPlaying) {
        playbutton.setText("Stop");
      } else {
        playbutton.setText("Play");
      }
      mStartPlaying = !mStartPlaying;


    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (mRecorder != null) {
      mRecorder.release();
      mRecorder = null;
    }

    if (mPlayer != null) {
      mPlayer.release();
      mPlayer = null;
    }
  }

  private void stopPlaying() {
    mPlayer.release();
    mPlayer = null;
  }

  private void startRecording() {
    mRecorder = new MediaRecorder();
    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    mRecorder.setOutputFile(mFileName);
    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

    try {
      mRecorder.prepare();
    } catch (IOException e) {
      Log.e(LOG_TAG, "prepare() failed");
    }

    mRecorder.start();

    mHandler.post(new Runnable() {
      @Override
      public void run() {
        float
            radius =
            (float) Math.log10(Math.max(1, mRecorder.getMaxAmplitude() - 500)) * ScreenUtils
                .dp2px(getContext(), 20);
//        mTextView.setText(String.valueOf(radius));
        mVoiceView.animateRadius(radius);
        if (mStartRecording) {
          mHandler.postDelayed(this, 50);
        }
      }
    });

  }

  private void stopRecording() {
    mRecorder.stop();
    mRecorder.release();
    mRecorder = null;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case REQUEST_RECORD_AUDIO_PERMISSION:
        permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        break;
    }
//    if (!permissionToRecordAccepted )
//      finish();

  }

  private void onRecord(boolean start) {
    if (start) {
      startRecording();
    } else {
      stopRecording();
    }
  }

  private void onPlay(boolean start) {
    if (start) {
      startPlaying();
    } else {
      stopPlaying();
    }
  }

  private void startPlaying() {
    mPlayer = new MediaPlayer();
    try {
      mPlayer.setDataSource(mFileName);
      mPlayer.prepare();
      mPlayer.start();
    } catch (IOException e) {
      Log.e(LOG_TAG, "prepare() failed");
    }
  }

  @Override
  public void onStart() {
    super.onStart();

    cancelButton = (Button) this.getDialog().findViewById(R.id.cancel_button);
    cancelButton.setOnClickListener(this);

    mVoiceView = (VoiceView) this.getDialog().findViewById(R.id.voiceview);
    mVoiceView.setOnRecordListener(this);

    captureButton = (Button) this.getDialog().findViewById(R.id.take_button);
    captureButton.setOnClickListener(this);

    playbutton = (Button) this.getDialog().findViewById(R.id.playbutton);
    playbutton.setOnClickListener(this);
    playbutton.setVisibility(View.GONE);
    cancelButton.setVisibility(View.GONE);

    if (recordedString != null) {
      playbutton.setVisibility(View.VISIBLE);
      cancelButton.setVisibility(View.VISIBLE);
      captureButton.setVisibility(View.GONE);
      cancelButton.setText("Ok");

      // Convert the base64 and store as mFilename
      try {
        mFileName = getActivity().getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest" + Utils.generateNumber()+ "-.3gp";


        byte[] decoded = Base64.decode(recordedString);

        File file2 = new File(mFileName);
        FileOutputStream os = new FileOutputStream(file2, true);
        os.write(decoded);
        os.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onRecordStart() {
    Log.d("TAG", "onRecordStart");

  }

  @Override
  public void onRecordFinish() {
    Log.d("TAG", "onRecordFinish");

  }

  public QuestionCreationDialogView getQuestionCreationDialogView() {
    return questionCreationDialogView;
  }

  public void setQuestionCreationDialogView(
      QuestionCreationDialogView questionCreationDialogView) {
    this.questionCreationDialogView = questionCreationDialogView;
  }

  public QuestionViewDialogView getQuestionViewDialogView() {
    return questionViewDialogView;
  }

  public void setQuestionViewDialogView(
      QuestionViewDialogView questionViewDialogView) {
    this.questionViewDialogView = questionViewDialogView;
  }

  public String getRecordedString() {
    return recordedString;
  }

  public void setRecordedString(String recordedString) {
    this.recordedString = recordedString;
  }

  public OneQuestionCreationDialogView getOneQuestionCreationDialogView() {
    return oneQuestionCreationDialogView;
  }

  public void setOneQuestionCreationDialogView(
      OneQuestionCreationDialogView oneQuestionCreationDialogView) {
    this.oneQuestionCreationDialogView = oneQuestionCreationDialogView;
  }

  public QuestionResponseCreationDialogView getQuestionResponseCreationDialogView() {
    return questionResponseCreationDialogView;
  }

  public void setQuestionResponseCreationDialogView(
      QuestionResponseCreationDialogView questionResponseCreationDialogView) {
    this.questionResponseCreationDialogView = questionResponseCreationDialogView;
  }

  public CommentDialogView getCommentDialogView() {
    return commentDialogView;
  }

  public void setCommentDialogView(CommentDialogView commentDialogView) {
    this.commentDialogView = commentDialogView;
  }
}