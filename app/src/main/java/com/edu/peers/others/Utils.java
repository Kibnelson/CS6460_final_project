package com.edu.peers.others;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.edu.peers.BuildConfig;
import com.edu.peers.adapter.ComboBoxViewListItem;
import com.edu.peers.models.User;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by nelson on 1/28/15.
 */
public class Utils {

  public static String readJSONinString(String fileName, Context c) {
    try {
      InputStream is = c.getAssets().open(fileName);
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      String text = new String(buffer);
      return text;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  public static String getCurrentTimestamp() {
    return String.valueOf(System.currentTimeMillis());
  }

  public static long getCurrentTimestampLong() {
    return System.currentTimeMillis();
  }

  public static long getCurrentTimestampFromDate() {
    long timestamp = 0;

    String dtStart = "2016-10-15T09:27:37Z";

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    try {
      Date date = format.parse(dtStart);
      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + date);

      timestamp = date.getTime();

      System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + timestamp);

    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return timestamp;
  }


  public static boolean networkAvailable(Context context) {
    ConnectivityManager
        cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
  }

  public static String getIMEI(Context context) {
    TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
    return mngr.getDeviceId();
  }

  public static String md5(String s) {
    try {
      // Create MD5 Hash
      MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
      digest.update(s.getBytes());
      byte messageDigest[] = digest.digest();

      // Create Hex String
      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++) {
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
      }
      return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  public static String getCurrentDate() {

    DateTime currentDate = new DateTime();

    //DD-YY-YYYY-HH-MM-SS
    String currentDateTime = currentDate.getDayOfMonth() + "-" +
                             currentDate.getMonthOfYear() + "-" +
                             currentDate.getYear() + ":" +
                             currentDate.getHourOfDay() + "-" +
                             currentDate.getMinuteOfHour();

    return currentDateTime;
  }

  public static String getCurrentDateNoTime() {

    DateTime currentDate = new DateTime();

    //DD-YY-YYYY-HH-MM-SS
    String currentDateTime = currentDate.getDayOfMonth() + "-" +
                             currentDate.getMonthOfYear() + "-" +
                             currentDate.getYear();

    return currentDateTime;
  }


  public static String getNetworkClass(int networkType) {

    switch (networkType) {
      case TelephonyManager.NETWORK_TYPE_GPRS:
        return "gprs";
      case TelephonyManager.NETWORK_TYPE_EDGE:
        return "edge";
      case TelephonyManager.NETWORK_TYPE_CDMA:
        return "cdma";
      case TelephonyManager.NETWORK_TYPE_1xRTT:
      case TelephonyManager.NETWORK_TYPE_IDEN:
        return "2G";
      case TelephonyManager.NETWORK_TYPE_UMTS:
      case TelephonyManager.NETWORK_TYPE_EVDO_0:
      case TelephonyManager.NETWORK_TYPE_EVDO_A:
      case TelephonyManager.NETWORK_TYPE_HSDPA:
      case TelephonyManager.NETWORK_TYPE_HSUPA:
      case TelephonyManager.NETWORK_TYPE_HSPA:
      case TelephonyManager.NETWORK_TYPE_EVDO_B:
      case TelephonyManager.NETWORK_TYPE_EHRPD:
      case TelephonyManager.NETWORK_TYPE_HSPAP:
        return "3G";
      case TelephonyManager.NETWORK_TYPE_LTE:
        return "4G";
      default:
        return "?";
    }
  }

  public static int getRoatationAngle(Activity mContext, int cameraId) {
    Camera.CameraInfo info = new Camera.CameraInfo();
    Camera.getCameraInfo(cameraId, info);

//    android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
//    android.hardware.Camera.getCameraInfo(cameraId, info);
    int rotation = mContext.getWindowManager().getDefaultDisplay().getRotation();
    int degrees = 0;
    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
    }
    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360; // compensate the mirror
    } else { // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    return result;
  }

  public static void showToastMessage(Context context, String msg) {

    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
  }

  public static Bitmap rotateImage(Bitmap bitmap, Activity context) {

    int
        degree = getRoatationAngle(context, Camera.CameraInfo.CAMERA_FACING_FRONT);

    int rotation = context.getResources().getConfiguration().orientation;

    if (rotation == 3 || rotation == 1)//Vertical
    {
      degree = 180 + degree;///vertical  view
    }

    int w = bitmap.getWidth();
    int h = bitmap.getHeight();

    Matrix mtx = new Matrix();
    mtx.postRotate(degree);

    return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
  }

  public static byte[] getByteArray(Bitmap bitmap) {

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

    return byteArrayOutputStream.toByteArray();
  }

  public static String convertObjectToString(Object object) {
    return new Gson().toJson(object);
  }

  public static JsonObject convertObjectToJSONObject(Object object) {
    JsonObject jsonObject = null;
    try {

      String json = new Gson().toJson(object);

      JsonParser parser = new JsonParser();
      jsonObject = (JsonObject) parser.parse(json);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return jsonObject;
  }


  public static void sortArray(ComboBoxViewListItem[] classArray) {

    Arrays.sort(classArray, new Comparator<ComboBoxViewListItem>() {

      @Override
      public int compare(ComboBoxViewListItem object1, ComboBoxViewListItem object2) {
        return object2.getText().compareTo(object1.getText());

      }
    });
  }

  public static String getCurrentYear() {

    java.util.Date date = new java.util.Date();
    Calendar cal = new GregorianCalendar();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd hh:mm:ss z");
    sdf.setCalendar(cal);
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    return "" + year;


  }

  public static String convertDateToString(Date date) {
    Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String dateString = formatter.format(date);
    return dateString;
  }

  public static Date convertStringToDate(String dateString) {
    Date date = null;
    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    try {
      date = dt.parse("2015-08-11 09:54:23.830000");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }


  public static long compareTwoTimeStamps(long milliseconds1) {

    long diff = System.currentTimeMillis() - milliseconds1;
    long diffSeconds = diff / 1000;
    long diffMinutes = diff / (60 * 1000);
    long diffHours = diff / (60 * 60 * 1000);
    long diffDays = diff / (24 * 60 * 60 * 1000);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");

    Calendar morningTime = new GregorianCalendar();
    morningTime.set(morningTime.get(Calendar.YEAR), morningTime.get(Calendar.MONTH),
                    morningTime.get(Calendar.DAY_OF_MONTH));
    morningTime.set(Calendar.HOUR_OF_DAY, 8);
    morningTime.set(Calendar.MINUTE, 0);

    Calendar afternoonTime = new GregorianCalendar();
    afternoonTime.set(afternoonTime.get(Calendar.YEAR), afternoonTime.get(Calendar.MONTH),
                      afternoonTime.get(Calendar.DAY_OF_MONTH));
    afternoonTime.set(Calendar.HOUR_OF_DAY, 14);
    afternoonTime.set(Calendar.MINUTE, 0);

    if (milliseconds1 >= morningTime.getTimeInMillis() && milliseconds1 <= afternoonTime
        .getTimeInMillis()) {
      // Its within morning session

    } else {
      //Afternoon session

    }

    return diffHours;
  }


  public static int checkIfTimestampIsForMorning(long milliseconds1) {

    int itsTrue = 0;
    Calendar morningTimeStart = new GregorianCalendar();
    morningTimeStart.set(morningTimeStart.get(Calendar.YEAR), morningTimeStart.get(Calendar.MONTH),
                         morningTimeStart.get(Calendar.DAY_OF_MONTH));
    morningTimeStart.set(Calendar.HOUR_OF_DAY, 8);
    morningTimeStart.set(Calendar.MINUTE, 0);

    Calendar morningTimeEnd = new GregorianCalendar();
    morningTimeEnd.set(morningTimeEnd.get(Calendar.YEAR), morningTimeEnd.get(Calendar.MONTH),
                       morningTimeEnd.get(Calendar.DAY_OF_MONTH));
    morningTimeEnd.set(Calendar.HOUR_OF_DAY, 13);
    morningTimeEnd.set(Calendar.MINUTE, 0);

    Calendar afternoonTimeStart = new GregorianCalendar();
    afternoonTimeStart
        .set(afternoonTimeStart.get(Calendar.YEAR), afternoonTimeStart.get(Calendar.MONTH),
             afternoonTimeStart.get(Calendar.DAY_OF_MONTH));
    afternoonTimeStart.set(Calendar.HOUR_OF_DAY, 14);
    afternoonTimeStart.set(Calendar.MINUTE, 0);

    Calendar afternoonTimeEnd = new GregorianCalendar();
    afternoonTimeEnd.set(afternoonTimeEnd.get(Calendar.YEAR), afternoonTimeEnd.get(Calendar.MONTH),
                         afternoonTimeEnd.get(Calendar.DAY_OF_MONTH));
    afternoonTimeEnd.set(Calendar.HOUR_OF_DAY, 17);
    afternoonTimeEnd.set(Calendar.MINUTE, 0);

    if (milliseconds1 >= morningTimeStart.getTimeInMillis() && milliseconds1 <= morningTimeEnd
        .getTimeInMillis()) {
      // Its within morning session
      itsTrue = 1;
    } else if (milliseconds1 >= afternoonTimeStart.getTimeInMillis()
               && milliseconds1 <= afternoonTimeEnd.getTimeInMillis()) {
      //Afternoon session
      itsTrue = 2;
    }

    return itsTrue;
  }

  public static Map<String, String> asMap(JSONObject event_data) {
    HashMap<String, String> resp = new HashMap<String, String>();
    if (event_data != null) {
      Iterator<?> keys = event_data.keys();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        try {
          resp.put(key, "" + event_data.get(key));
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }
    return resp;
  }


  public static String getAppVersion() {

    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    return "" + versionCode + "_" + versionName;
  }

  public static int generateNumber() {
    Random r = new Random();
    int Low = 10;
    int High = 50;
    int result = r.nextInt(High - Low) + Low;

    return result;

  }

  public static String getStateFromTranscation(String transaction) {

    String state = null;

    if (transaction.equalsIgnoreCase(Constants.Registration)) {
      state = Constants.Registration_State;
    }
    if (transaction.equalsIgnoreCase(Constants.Enrollment)) {
      state = Constants.Enrollment_State;
    }
    if (transaction.equalsIgnoreCase(Constants.Transfer)) {
      state = Constants.Transfer_State;
    }
    if (transaction.equalsIgnoreCase(Constants.Promotion)) {
      state = Constants.Promotion_State;
    }
    if (transaction.equalsIgnoreCase(Constants.Pending)) {
      state = Constants.Pending_State;
    }
    if (transaction.equalsIgnoreCase(Constants.Update)) {
      state = Constants.Update_State;
    }

    return state;


  }

  public static File createImageFile() throws IOException {

    FileOutputStream out = null;

    try {
      out = new FileOutputStream(createImageFile());
//      originalImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
      // PNG is a lossless format, the compression factor (100) is ignored
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    File
        storageDir =
        new File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
    Log.i(Constants.TAG, "PATH=" + storageDir.getAbsolutePath());
    Log.i(Constants.TAG, "PATH=" + storageDir.getPath());
    if (!storageDir.exists()) {
      storageDir.mkdirs();
    }
    File image = File.createTempFile(
        timeStamp,                   /* prefix */
        ".png",                     /* suffix */
        storageDir                   /* directory */
    );
    return image;
  }


  public static String convert(String s) {
    SimpleDateFormat newformat = new SimpleDateFormat("d/MM/yy");
    try {
      if (s.contains("T")) {
        String datestring = s.split("T")[0];
        SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
        String reformattedStr = newformat.format(oldformat.parse(datestring));
        return reformattedStr;
      } else {
        if (Integer.parseInt(s.split("-")[0]) > 13) {
          SimpleDateFormat oldformat = new SimpleDateFormat("yyyy-MM-dd");
          String reformattedStr = newformat.format(oldformat.parse(s));
          return reformattedStr;
        } else {
          SimpleDateFormat oldformat = new SimpleDateFormat("MM-dd-yyyy");
          String reformattedStr = newformat.format(oldformat.parse(s));
          return reformattedStr;
        }

      }
    } catch (Exception e) {
      return null;
    }
  }

  public static User getUserWithUsername(List<User> userList, String username) {
    User userFound = null;
    for (User user : userList) {
      Log.i(Constants.TAG,"user==="+username+"===XXX====="+user.getUsername());

      if (username.contains(user.getUsername()) || username.contains(user.getFirstName())|| username.contains(user.getLastName())) {
        Log.i(Constants.TAG,"user==="+user.getUsername());

        userFound = user;
        break;
      }
    }
    return userFound;
  }
}
