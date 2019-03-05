package com.example.smartgoals.navigator_0;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class EditTextDatePicker implements View.OnClickListener, OnDateSetListener {
    EditText _editText;
    private int _day;
    private int _month;
    private int _year;
    private Context _context;

    public EditTextDatePicker(Context context, int editTextViewID) {
        Activity act = (Activity) context;
        this._editText = (EditText) act.findViewById(editTextViewID);
        this._editText.setOnClickListener(this);
        this._context = context;
    }

    //TODO: Don't allow old date

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    // updates the date in the  date EditText
    private void updateDisplay() {

        _editText.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_month + 1).append("/").append(_day + 1).append("/").append(_year).append(" "));
    }

    public String getCurrentDate() {
        //GET Current Date to save to db
        Date currentTime = Calendar.getInstance().getTime();
        String Date = currentTime.toString();

        Log.d("CurrentDate", "Date");
        return Date;
    }
}

