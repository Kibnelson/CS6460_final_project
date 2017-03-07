package com.edu.peers.dialogs;

/**
 * Created by nelson on 8/17/15.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.edu.peers.R;

public class CustomAlertDialogBuilder extends AlertDialog.Builder {

  private final Context mContext;
  private TextView mTitle;
  private TextView mMessage;

  public CustomAlertDialogBuilder(Context context) {
    super(context);
    mContext = context;

    View customTitle = View.inflate(mContext, R.layout.alert_dialog_title, null);
    mTitle = (TextView) customTitle.findViewById(R.id.title_alert);
    setCustomTitle(customTitle);

    View customMessage = View.inflate(mContext, R.layout.alert_dialog_message, null);
    mMessage = (TextView) customMessage.findViewById(R.id.message_alert);
    mMessage.setTextColor(Color.BLACK);
    setView(customMessage);

//   setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//      @Override
//      public void onClick(DialogInterface dialog, int which) {
//        // do nothing (will close dialog)
//      }
//    });

    setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        // do something
      }
    });


  }

  @Override
  public CustomAlertDialogBuilder setTitle(int textResId) {
    mTitle.setText(textResId);
    return this;
  }

  @Override
  public CustomAlertDialogBuilder setTitle(CharSequence text) {
    mTitle.setText(text);
    return this;
  }

  @Override
  public CustomAlertDialogBuilder setMessage(int textResId) {
    mMessage.setText(textResId);
    return this;
  }

  @Override
  public CustomAlertDialogBuilder setMessage(CharSequence text) {
    mMessage.setText(text);
    return this;
  }

}
