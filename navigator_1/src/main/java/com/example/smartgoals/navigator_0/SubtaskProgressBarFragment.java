package com.example.smartgoals.navigator_0;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smartgoals.navigator_0.db.TaskDBAdapter;

public class SubtaskProgressBarFragment extends Fragment {


    TaskDBAdapter db;
    private ProgressBar progressBar;
    private double TEST_SUBTASKS_COMPLETE = 4;
    private double TESTS_SUBSTASKS_INCOMPLETE = 1;
    private double TEST_SUBTASKS_TOTAL = TEST_SUBTASKS_COMPLETE + TESTS_SUBSTASKS_INCOMPLETE;
    private int Test_Subtasks_Total = (int) TEST_SUBTASKS_TOTAL;
    private int TestSubTasksComplete = (int) (TEST_SUBTASKS_COMPLETE);
    private String complete_subtasks = String.valueOf(TestSubTasksComplete);
    public SubtaskProgressBarFragment() {
    }


    public static SubtaskProgressBarFragment newInstance(int CompletedSubtasks, int TotalSubtasks) {
        SubtaskProgressBarFragment S = new SubtaskProgressBarFragment();
        Bundle args = new Bundle();
        args.putInt("CompletedSubtasks", CompletedSubtasks);
        args.putInt("TotalSubtasks", TotalSubtasks);
        S.setArguments(args);
        return S;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TaskDBAdapter db;
//        Log.e("Test", "Hello");
//        db = new TaskDBAdapter(getActivity().getApplicationContext());


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /*n this method we are already assuming that there is data and are getting subtask information ONLY. Main activity should NOT
    call the fragment on create if no goal. Similarly should not display buttons. In goal progress, same thing.
       */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Bundle args = getArguments();
        double CompletedSubtasks = args.getInt("CompletedSubtasks", 0);
        double TotalSubtasks = args.getInt("TotalSubtasks", 0);

        Log.d("CompletedSubtasksS", String.valueOf(CompletedSubtasks));
        Log.d("TotalSubtasks", String.valueOf(TotalSubtasks));


        View view = inflater.inflate(R.layout.fragment_subtasks_progress_bar, container, false);
        progressBar = view.findViewById(R.id.SubtasksCompleted); //Gets the handle to subtasks completed progress bar
        int PERCENT_COMPLETE = (int) Math.round((CompletedSubtasks / TotalSubtasks) * 100);


        Log.d("PercentCOMPLETE", String.valueOf(PERCENT_COMPLETE));


        progressBar.setProgress(PERCENT_COMPLETE);


        String subtasksprogress = getString(R.string.subtasks_complete_fragment_text, String.valueOf(Math.round(TotalSubtasks))); //replaces the

        String complete_subtasks = String.valueOf(CompletedSubtasks);
        TextView textView = view.findViewById(R.id.subtasks_completed_of_total_text);
        //Log.d("subtasks", "Value: " + (subtasksprogress));

        String conCat = complete_subtasks + ' ' + subtasksprogress;
        textView.setText(conCat);

        return view;
    }

}
