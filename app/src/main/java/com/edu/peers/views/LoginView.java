package com.edu.peers.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.edu.peers.R;
import com.edu.peers.managers.UserManager;
import com.edu.peers.models.User;
import com.edu.peers.models.UserObject;
import com.edu.peers.others.Constants;

import java.util.ArrayList;
import java.util.List;


public class LoginView extends FragmentActivity
    implements View.OnClickListener {

  int selected;


  private SchoolCensus schoolCensus;
  private Button signInButton;
  private EditText usernameField, passwordField;
  private TextView message;
  private ProgressBar progressBar;
  private String usernameFieldStr;
  private String passwordFieldStr;
  private TextView newTabOneText;
  private TextView newTabTwoText;
  private ViewFlipper flipper;
  private GestureDetector gestureDetector;
  private ImageView leftFingerPrintImage;
  private UserManager userManager;
  private ProgressDialog progressDialog;
  private Button registerButton;
  private UserObject userObject;
  private List<User> userList= new ArrayList<>();
  private UserObject userObjectList;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    requestWindowFeature(Window.FEATURE_NO_TITLE);

    schoolCensus = (SchoolCensus) getApplication();

    schoolCensus.initHome();
    schoolCensus.setState(Constants.NON);

    setContentView(R.layout.peer_login_view);
    TextView textView = (TextView) findViewById(R.id.txtLink);
    textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    usernameField = (EditText) findViewById(R.id.username_field);
    passwordField = (EditText) findViewById(R.id.password_field);

    message = (TextView) findViewById(R.id.message);

    signInButton = (Button) findViewById(R.id.ok_button);
    signInButton.setOnClickListener(this);

    registerButton = (Button) findViewById(R.id.registerButton);
    registerButton.setOnClickListener(this);

    schoolCensus.setCurrentActivity(this);
    progressBar = (ProgressBar) findViewById(R.id.progressBar1);

    newTabOneText = (TextView) findViewById(R.id.tab_one_text);
    newTabOneText.setOnClickListener(this);
    newTabOneText.setVisibility(View.GONE);

    newTabTwoText = (TextView) findViewById(R.id.tab_two_text);
    newTabTwoText.setOnClickListener(this);
    newTabTwoText.setVisibility(View.GONE);

    gestureDetector = new GestureDetector(this, new MyGestureDetector());

    flipper = (ViewFlipper) findViewById(R.id.flipper);

    flipper.setInAnimation(inFromRightAnimation());
    flipper.setOutAnimation(outToLeftAnimation());
    flipper.setDisplayedChild(0);
    selected = 0;
    setGradientDrawableUnSelected(newTabTwoText);
    setGradientDrawableSelected(newTabOneText);

    progressBar.setVisibility(View.INVISIBLE);


    userManager = new UserManager(schoolCensus.getCloudantInstance(), this);
  }

  public void setGradientDrawableSelected(TextView field) {

    field.setTag(1);
    field.setBackground(getResources().getDrawable(R.drawable.school_tab_select_background_color));

  }

  public void setGradientDrawableUnSelected(TextView field) {
    field.setTag(0);
    field
        .setBackground(getResources().getDrawable(R.drawable.school_tab_unselect_background_color));
  }

  @Override
  protected void onStart() {
    super.onStart();


  }


  @Override
  public void onClick(View v) {

    if (v == newTabOneText) {
      if (selected == 1) {
        flipper.setInAnimation(inFromRightAnimation());
        flipper.setOutAnimation(outToLeftAnimation());
        flipper.setDisplayedChild(0);
        selected = 0;
        setSelectedBg();
      } else {
        return;
      }


    } else if (v == newTabTwoText) {

      if (selected == 0) {
        flipper.setInAnimation(inFromLeftAnimation());
        flipper.setOutAnimation(outToRightAnimation());
        flipper.setDisplayedChild(1);
        selected = 1;
        setSelectedBg();
      } else {
        return;
      }


    } else if (v == signInButton) {

      usernameFieldStr = usernameField.getText().toString().trim();
      passwordFieldStr = passwordField.getText().toString().trim();

      boolean valid = true;
      StringBuilder errorBuffer = new StringBuilder();

      if (usernameFieldStr == null || usernameFieldStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter username\n");
      }
      if (passwordFieldStr == null || passwordFieldStr.length() == 0) {
        valid = false;
        errorBuffer.append("Please enter password\n");
      }

      if (valid) {

        schoolCensus.setUsername(usernameFieldStr);
        schoolCensus.setPassword(passwordFieldStr);
        if (schoolCensus.getCloudantInstance().getDocumentCount() > 0) {

          new backgroundProcess().execute(1);

        } else {

          new backgroundProcess().execute(1);

        }

      } else {
        message.setText(errorBuffer.toString());
      }


    } else if (v == registerButton) {
      schoolCensus.setUserObject(null);
      Intent intent = new Intent(LoginView.this, RegistrationView.class);
      startActivity(intent);
    }

    progressBar.setVisibility(View.INVISIBLE);

  }

  private ProgressDialog showProgessDialog() {

    progressDialog = new ProgressDialog(LoginView.this);
    progressDialog.setMessage("Please wait");
    progressDialog.show();
    return progressDialog;


  }

  private void hideProgessDialog() {

    progressDialog.dismiss();
    progressDialog.hide();

  }

  public void reEnableSignInButton() {

    signInButton.setEnabled(true);
  }

  public void openSchoolList(int size) {

    signInButton.setEnabled(true);
    schoolCensus.dismissProgressDialog();
    Intent intent = null;
    intent = new Intent(LoginView.this, MainView.class);
    startActivity(intent);

  }

  protected Animation inFromRightAnimation() {

    Animation inFromRight = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, +1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
    inFromRight.setDuration(500);
    inFromRight.setInterpolator(new AccelerateInterpolator());
    return inFromRight;
  }

  protected Animation outToLeftAnimation() {
    Animation outtoLeft = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, -1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
    outtoLeft.setDuration(500);
    outtoLeft.setInterpolator(new AccelerateInterpolator());
    return outtoLeft;
  }

  protected Animation inFromLeftAnimation() {
    Animation inFromLeft = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, -1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
    inFromLeft.setDuration(500);
    inFromLeft.setInterpolator(new AccelerateInterpolator());
    return inFromLeft;
  }

  protected Animation outToRightAnimation() {
    Animation outtoRight = new TranslateAnimation(
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, +1.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f,
        Animation.RELATIVE_TO_PARENT, 0.0f);
    outtoRight.setDuration(500);
    outtoRight.setInterpolator(new AccelerateInterpolator());
    return outtoRight;
  }

  private void increaseSelected() {
    selected++;
    if (selected == 2) {
      selected = 0;
    }
  }

  private void dereaseSelected() {
    selected--;
    if (selected == -1) {
      selected = 1;
    }
  }

  private void setSelectedBg() {
    Resources res = getResources();
    switch (selected) {
      case 0:

        setGradientDrawableUnSelected(newTabTwoText);
        setGradientDrawableSelected(newTabOneText);
        break;
      case 1:

        setGradientDrawableUnSelected(newTabOneText);
        setGradientDrawableSelected(newTabTwoText);
        break;
      default:
        return;
    }

  }

  private String login() {

    String respose = null;

    usernameFieldStr = null;
    passwordFieldStr = null;
    return respose;
  }


  public void refreshDataList() {
    progressBar.setVisibility(View.INVISIBLE);
  }


  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (gestureDetector != null) {
      gestureDetector.onTouchEvent(ev);
    }
    return super.dispatchTouchEvent(ev);
  }

  @Override
  public void onResume() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }

    super.onResume();

  }

  @Override
  public void onBackPressed() {
    moveTaskToBack(true);
  }


  private class backgroundProcess extends AsyncTask<Integer, Integer, Long> {


    protected Long doInBackground(Integer... params) {

      userObjectList = userManager.getDocumentGetDocument(Constants.USERS);

      int size=userObjectList.getUserList().size();
      for (int y=0;y<size;y++){
        User user=userObjectList.getUserList().get(y);
        if (user.getUsername().equalsIgnoreCase(usernameFieldStr)){
          userObjectList.setPosition(y);
          userObjectList.setUser(user);
        }

      }

      long totalSize = 0;
      return totalSize;
    }


    protected void onPostExecute(Long result) {

      hideProgessDialog();

      if (userObjectList != null && userObjectList.getUser().getPassword()
          .equalsIgnoreCase(passwordFieldStr)) {

        schoolCensus.setUserObject(userObjectList);
        openSchoolList(1);

      } else {

        Toast.makeText(getApplicationContext(), "Wrong credentials, please register first or try again", Toast.LENGTH_LONG).show();


      }

    }

    protected void onPreExecute() {
      showProgessDialog();
    }
  }



  private class MyGestureDetector implements GestureDetector.OnGestureListener {

    final float scale = getResources().getDisplayMetrics().density;
    private final int SWIPE_MIN_DISTANCE = (int) (120 * scale + 0.5f);
    private final int SWIPE_MAX_OFF_PATH = (int) (250 * scale + 0.5f);
    private final int SWIPE_THRESHOLD_VELOCITY = (int) (400 * scale + 0.5f);

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

      if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
        return false;
      }
      if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
          && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
        flipper.setInAnimation(inFromRightAnimation());
        flipper.setOutAnimation(outToLeftAnimation());
        flipper.showNext();
        increaseSelected();
      } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                 && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
        flipper.setInAnimation(inFromLeftAnimation());
        flipper.setOutAnimation(outToRightAnimation());
        flipper.showPrevious();
        dereaseSelected();
      }
      setSelectedBg();
      return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
      // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX, float distanceY) {
      // TODO Auto-generated method stub
      return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
      // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
      // TODO Auto-generated method stub
      return false;
    }
  }

}