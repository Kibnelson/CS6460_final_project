package com.edu.peers.dialogs;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edu.peers.R;
import com.edu.peers.views.SchoolCensus;


/**
 * Created by nelson on 3/16/15.
 */

public class ImageViewDialog extends DialogFragment {

  private static int dataDialogOption;
  private static Bitmap image;
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
  private ImageView imageView;

  public static ImageViewDialog newInstance(int num, int dialogOption, Bitmap imageView) {
    ImageViewDialog f = new ImageViewDialog();
    dataDialogOption = dialogOption;
    image = imageView;
    Bundle args = new Bundle();
    args.putInt("num", num);
    f.setArguments(args);

    return f;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

    view = inflater.inflate(R.layout.school_imageview_layout, null);

    imageView = (ImageView) view.findViewById(R.id.picture_taken);

    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

    imageView.setImageBitmap(image);
    imageView.setVisibility(ImageView.VISIBLE);
    imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                                                            LinearLayout.LayoutParams.FILL_PARENT));

    imageView.setScaleType(ImageView.ScaleType.FIT_XY);

    return view;

  }


  @Override
  public void onStart() {
    super.onStart();

  }

}