package com.example.smartgoals.navigator_0;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class GoalProgressBarFragment extends Fragment {

    private ProgressBar progressBar;
    public GoalProgressBarFragment() {
    }

    //TODO pull in date as string, set the arg as a date time
    public static GoalProgressBarFragment newInstance(String EndDate, String StartDate) {
        int GoalProgressPercent;
        int total_days;
        int days_elapsed;

        //THESE Should be passed in.
//        EndDate="03/06/2019";
//        StartDate="03/1/2019";


        GoalProgressBarFragment G = new GoalProgressBarFragment();
        Bundle args = new Bundle();
        args.putString("EndDate", EndDate);
        args.putString("StartDate", StartDate);
        G.setArguments(args);
        return G;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//     View view = inflater.inflate(R.layout.fragment_progress_bars, container, false);
        View view = inflater.inflate(R.layout.goal_progress_bar_fragment, container, false);

        Bundle args = getArguments();
        String EndDate = args.getString("EndDate", "03/05/2019");
        String StartDate = args.getString("StartDate", "03/02/2019");
        Log.d("Made It Here", "Here");
        Log.d("StartDateInOnCreate", EndDate);
        int PercentComplete = DateCalcuate(StartDate, EndDate);
        Log.d("PercentCOMPLETEE", String.valueOf(PercentComplete));

        progressBar = view.findViewById(R.id.Goal_ProgressBar_PercentCompleted);

        progressBar.setProgress(PercentComplete);
        String percentComplete = getString(R.string.Percent_Complete, PercentComplete);
        String percent = getString(R.string.percent_sign);
        String complete = getString(R.string.Complete);
        TextView textView = view.findViewById(R.id.Percent_Complete_Text);
        String conCat = percentComplete + percent + ' ' + complete;
        textView.setText(conCat);


        return view;
    }


    public int DateCalcuate(String startDate, String endDate) {

        int ProgressPercent = 0;//IF Didn't work
        double elapsed_days;
        double total_days;
        long so_far;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        Date enddatE = null;
        Date startdatE = null;
        Log.d("UhOh", String.valueOf(startDate));
        Log.d("UhOh", String.valueOf(endDate));
//        Log.d("UhOh",String.valueOf(startdatE));
//        Log.d("UhOH",String.valueOf(enddatE));
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");

        Date currentDate = new Date();//Current Date


//        Date dateS = simpleDateFormat.parse(startDate);
        SimpleDateFormat srcDf = new SimpleDateFormat("MM/dd/yyyy");

        // parse the date string into Date object
        Date date = null;
        Log.d("UHNO", String.valueOf(date));

        try {
            startdatE = srcDf.parse(startDate);
            enddatE = srcDf.parse(endDate);

            Log.v("UhOh", String.valueOf(startdatE));
        } catch (ParseException e) {
            Log.d("UHNOO", "Ohno");
            e.printStackTrace();


        } finally {

            Log.v("UHWHA", "idk");
            long difference = enddatE.getTime() - startdatE.getTime(); //--> total das
            so_far = currentDate.getTime() - startdatE.getTime();
            total_days = difference / daysInMilli;
            elapsed_days = so_far / daysInMilli;
//
            ProgressPercent = ((int) Math.round((elapsed_days / total_days) * 100));
            return ProgressPercent;
        }


//        DateFormat destDf = new SimpleDateFormat("dd-mm-yyyy");
//        dateStr = destDf.format(date);
// format the date into another format


    }
}


//TODO: Poll the data entry



/*
//TODO: Add ticks between subtasks in second progress bar (e.g. 4 subtasks results in 4 ticks).
https://www.codeproject.com/Articles/875924/Bar-Clock

for ticks between subtasks in second progress bar (e.g. 4 subtasks results in 4 ticks).
    // what is the distance between each tick mark?
    float increment = (float) getWidth() / getMax();

// draw the tick marks
for (int i = 0; i <= getMax(); i++)
        {
        float x = i * increment;

        canvas.drawLine(x, nMiddle - 20.0f, x, nMiddle + 20.0f, m_paintLine);
        }*/
