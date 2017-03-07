package com.edu.peers.dialogs;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.edu.peers.R;
import com.edu.peers.others.Base64;
import com.edu.peers.others.Utils;
import com.edu.peers.views.RegistrationView;
import com.edu.peers.views.SchoolCensus;

import java.io.IOException;

//import com.edu.peers.views.StudentRegistrationView;


/**
 * Created by nelson on 3/16/15.
 */

public class CameraViewDialog extends DialogFragment
    implements View.OnClickListener, Camera.PictureCallback, TextureView.SurfaceTextureListener {

  private static int dataDialogOption;
  private int cameraFrontOrBack;
  private Button cancelButton;
  private Camera camera;
  private TextureView mTextureView;
  private float rotation = 0;
  private Camera.CameraInfo mBackCameraInfo;
  private Button captureButton;
  private String bitmapImageString;
  private View view;
  private LinearLayout progressBarlayout;
  private boolean pictureTaken = false;
  private Bitmap originalImage;
  private SchoolCensus schoolCensus;
  private byte[] originalImagedata;
  private RegistrationView registrationView;
  private Camera.CameraInfo cameraInfo;

  public static CameraViewDialog newInstance(int num, int dialogOption) {
    CameraViewDialog f = new CameraViewDialog();
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

    view = inflater.inflate(R.layout.school_camera_layout, null);
    progressBarlayout = (LinearLayout) view.findViewById(R.id.progressBar);
    progressBarlayout.setVisibility(View.GONE);
    schoolCensus = (SchoolCensus) getActivity().getApplication();

    return view;

  }

  @Override
  public void onClick(View v) {
    /** When OK Button is clicked, dismiss the school_calendar_view_dialog */
    if (v == cancelButton) {

      switch (dataDialogOption) {
        case 1:

          if (bitmapImageString!=null)
          registrationView.setTakenImage(originalImage, bitmapImageString);

          break;

        case 2:
//          studentRegistrationView = (StudentRegistrationView) schoolCensus.getCurrentFragment();
//          studentRegistrationView.setParameters(camera.getParameters());
//          if (bitmapImageString!=null)
//          studentRegistrationView.setTakenImage(originalImage, bitmapImageString);

          break;



        default:


      }
      dismiss();

    } else if (v == captureButton) {

      if (pictureTaken) {
        captureButton.setText("Capture");
        pictureTaken = false;
        camera.startPreview();
        cancelButton.setVisibility(View.GONE);
      } else {
        camera.takePicture(null, null, this);
        progressBarlayout.setVisibility(View.VISIBLE);
      }


    }
  }

  @Override
  public void onStart() {
    super.onStart();

    mTextureView = (TextureView) this.getDialog().findViewById(R.id.surfaceView1);
    mTextureView.setSurfaceTextureListener(this);

    cancelButton = (Button) this.getDialog().findViewById(R.id.cancel_button);
    cancelButton.setOnClickListener(this);

    cancelButton.setVisibility(View.GONE);

    captureButton = (Button) this.getDialog().findViewById(R.id.take_button);
    captureButton.setOnClickListener(this);


  }

  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
                                        int height) {


    if (cameraFrontOrBack == 1) {
      camera = Camera.open(findFirstBackFacingCamera());
    } else {
      camera = Camera.open(findFirstFrontFacingCamera());
    }

    Resources r = getResources();
    int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 45,
                                            r.getDisplayMetrics());

    int h = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 35,
                                            r.getDisplayMetrics());

    Camera.Size previewSize = camera.getParameters().getPreviewSize();
//        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(
//                w, h, Gravity.CENTER));

//    mTextureView.setLayoutParams(new FrameLayout.LayoutParams(
//        previewSize.width, previewSize.height, Gravity.CENTER));

    try {
      camera.setPreviewTexture(surface);
    } catch (IOException ignored) {
    }

    camera.startPreview();
    int
        angleToRotate =
        getRoatationAngle(getActivity(), Camera.CameraInfo.CAMERA_FACING_FRONT);
    camera.setDisplayOrientation(angleToRotate);

  }
  public  int getRoatationAngle(Activity mContext, int cameraId) {
//    Camera.CameraInfo info = new Camera.CameraInfo();
//    Camera.getCameraInfo(cameraId, info);

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
    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (cameraInfo.orientation + degrees) % 360;
      result = (360 - result) % 360; // compensate the mirror
    } else { // back-facing
      result = (cameraInfo.orientation - degrees + 360) % 360;
    }
    return result;
  }
  private int findFirstFrontFacingCamera() {
    int cameraId = -1;
    // search for the first front facing camera
    int numberOfCameras = Camera.getNumberOfCameras();
    for (int i = 0; i < numberOfCameras; i++) {
      cameraInfo = new Camera.CameraInfo();
      Camera.getCameraInfo(i, cameraInfo);
      if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        cameraId = i;
        break;
      }
    }
    return cameraId;
  }

  private int findFirstBackFacingCamera() {
    int cameraId = -1;
    // search for the first front facing camera
    int numberOfCameras = Camera.getNumberOfCameras();
    for (int i = 0; i < numberOfCameras; i++) {
      Camera.CameraInfo info = new Camera.CameraInfo();
      Camera.getCameraInfo(i, info);
      if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
        cameraId = i;
        break;
      }
    }
    return cameraId;
  }

  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
                                          int height) {
    // Ignored, the Camera does all the work for us
  }

  @Override
  public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    camera.stopPreview();
    camera.release();
    return true;
  }

  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    // Called whenever a new frame is available and displayed in the
    // TextureView
    rotation += 1.0f;
    if (rotation > 360) {
      rotation = 0;
    }
    //mTextureView.setRotation(rotation);
  }

//  public void cameraDisplayRotation() {
//    final int rotation = this.getDialog().getOwnerActivity().getWindowManager()
//        .getDefaultDisplay().getRotation();
//    int degrees = 0;
//    switch (rotation) {
//      case Surface.ROTATION_0:
//        degrees = 0;
//        break;
//      case Surface.ROTATION_90:
//        degrees = 90;
//        break;
//      case Surface.ROTATION_180:
//        degrees = 180;
//        break;
//      case Surface.ROTATION_270:
//        degrees = 270;
//        break;
//    }
//
//    final int displayOrientation = (mBackCameraInfo.orientation
//                                    - degrees + 360) % 360;
//    camera.setDisplayOrientation(displayOrientation);
//    int rotate = (mBackCameraInfo.orientation - degrees + 360) % 360;
//
//    Camera.Parameters params = camera.getParameters();
//    params.setRotation(rotate);
//    camera.setParameters(params);
//
//
//  }

  private Pair<Camera.CameraInfo, Integer> getBackCamera() {
    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    final int numberOfCameras = Camera.getNumberOfCameras();

    for (int i = 0; i < numberOfCameras; ++i) {
      Camera.getCameraInfo(i, cameraInfo);
      if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
        return new Pair<Camera.CameraInfo, Integer>(cameraInfo,
                                                    i);
      }
    }
    return null;
  }

  @Override
  public void onPictureTaken(byte[] data, Camera camera) {

//    ExifInterface ei = new ExifInterface(photoPath);
//    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                           ExifInterface.ORIENTATION_UNDEFINED)

    originalImagedata=data;
    originalImage = BitmapFactory.decodeByteArray(data, 0, data.length);

    int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();

    switch (rotation) {

      case ExifInterface.ORIENTATION_UNDEFINED:
        originalImage = rotateBitmap(originalImage, -90);
        break;

      case ExifInterface.ORIENTATION_ROTATE_90:
        originalImage = rotateBitmap(originalImage, 90);
        break;

      case ExifInterface.ORIENTATION_ROTATE_180:
        originalImage = rotateBitmap(originalImage, 180);
        break;
      // etc.
    }


//    bitmapImage = Utils.rotateImage(originalImage, getActivity());

//    bitmapImageString =
//        android.util.Base64
//            .encodeToString(Utils.getByteArray(originalImage), android.util.Base64.);
    try {

      bitmapImageString = Base64.encodeBytes(Utils.getByteArray(originalImage), Base64.GZIP);
    } catch (IOException e) {
      e.printStackTrace();
    }

    progressBarlayout.setVisibility(View.GONE);

    pictureTaken = true;
    captureButton.setText("Retake");

    cancelButton.setVisibility(View.VISIBLE);

//    imageView.setImageBitmap(originalImage);
//    imageView.setVisibility(ImageView.VISIBLE);
//    cameraViewDialog.dismiss();
  }


  public int getCameraFrontOrBack() {
    return cameraFrontOrBack;
  }

  public void setCameraFrontOrBack(int cameraFrontOrBack) {
    this.cameraFrontOrBack = cameraFrontOrBack;
  }


  public void setStudentRegistrationView(){
//      StudentRegistrationView studentRegistrationView) {
//    this.studentRegistrationView = studentRegistrationView;
  }

  public RegistrationView getRegistrationView() {
    return registrationView;
  }

  public void setRegistrationView(RegistrationView registrationView) {
    this.registrationView = registrationView;
  }
}