package com.edu.peers.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.edu.peers.views.SchoolCensus;

import java.util.Calendar;

/**
 * Created by nelson on 3/3/16.
 */

public class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

  private SchoolCensus schoolCensus;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    schoolCensus = (SchoolCensus) getActivity().getApplication();
    // Use the current date as the default date in the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    return new DatePickerDialog(getActivity(), this, year, month, day);
  }

  public void onDateSet(DatePicker view, int year, int month, int day) {

//    Integer datePickerStatus=schoolCensus.getDatePickerStatus();
//
//    switch (datePickerStatus) {
//
//      case Constants.DATE_PICKER_STUDENT_REGISTRATION_VIEW:
//        StudentRegistrationWizardView patientUpdateView =(StudentRegistrationWizardView)schoolCensus.getCurrentFragment();
//        String monthVal=""+month;
//        if (String.valueOf(month).length()==1)
//          monthVal="0"+month;
//
//        String dayVal=""+day;
//        if (String.valueOf(day).length()==1)
//          dayVal="0"+day;
//        patientUpdateView.setDate(year+"-"+monthVal+"-"+dayVal);
//        break;
//      default:
//    }




  }



}