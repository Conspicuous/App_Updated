package com.example.smartgoals.navigator_0;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.example.smartgoals.navigator_0.db.TaskDBAdapter;
import com.example.smartgoals.navigator_0.util.HelperUtil;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    public int dummy_progress = 75;

    protected Button btn_dataentry;
    protected Button btn_rewards;
    public long TotalCount;
    public long FinishedCount;
    public long UnFininishedCount;
    Cursor parentCursor;
    //
//    Fragment fr;
    TaskDBAdapter db = new TaskDBAdapter(this);
    private boolean isFragmentDisplayed = false;
//    FragmentManager fm;
//    FragmentTransaction fragmentTransaction;
    // TODO https://codelabs.developers.google.com/codelabs/android-navigation/#0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

//Implement the basic bottom navigation view that starts the relevant activity

        //TODO: Configure Data based on Database


        //TODO: Configure buttons based on presence of goal
        /*
         * Use TaskDBAdapter
         *
         *
         * */

        // FragmentManager fragmentManager = getSupportFragmentManager();

// TODO: Configure
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                        switch (item.getItemId()) {
                            case R.id.create_new_goal_button:
                                //ONLY DISPLAY THIS IF NO GOAL

                                // Toast.makeText(getApplicationContext(),"Hello New Goal!",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), com.example.smartgoals.navigator_0.DataEntryScreen.class);
                                startActivity(intent);
                                break;
                            case R.id.update_goal_button:
                                Intent intent1 = new Intent(getApplicationContext(), com.example.smartgoals.navigator_0.DataEntryScreen.class);
                                startActivity(intent1);

                                //startActivity(new Intent("com.example.smartgoals.navigator_0.DataEntryScreen"));
                                break;
                            case R.id.rewards_button:

                                Intent intent2 = new Intent(getApplicationContext(), RewardsScreen.class);
                                startActivity(intent2);
                                //startActivity(new Intent("com.example.smartgoals.navigator_0.RewardsScreen"));
                                break;
                        }
                        return true;
                    }
                });

//IF NO GOAL IS PRESENT, Remove the Option to see Badges and Goal Update
        if (!isDataPresent()) {

            bottomNavigationView.getMenu().removeItem(R.id.update_goal_button);
            bottomNavigationView.getMenu().removeItem(R.id.rewards_button);
            bottomNavigationView.getMenu().removeItem(R.id.navigation_dashboard); //not on homescreen ui
        } else {
            bottomNavigationView.getMenu().removeItem(R.id.create_new_goal_button);
            bottomNavigationView.getMenu().removeItem(R.id.navigation_dashboard);// this isn't on either homescreen layout
        }

        /*Display Brand Logo on Toolbar, right side*/
        //toolbar=getSupportActionBar();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        //DATABASE ACCESS AND STUFF
        createFragments();

//if database not empty


    }

    public void createFragments() {
        if (dummy_progress != 0) {
            long SubtaskCountResults[] = SubtaskCounts();
            long TotalSubtasks = SubtaskCountResults[0];
            long CompletedSubtasks = SubtaskCountResults[1];


            displayGoalProgressFragment();
            displaySubtaskProgressFragment((int) CompletedSubtasks, (int) TotalSubtasks);

        }
    }


    public long[] SubtaskCounts() { //note the [] is to return an array
//        ArrayList<Integer> SubtaskCounts = new ArrayList<Integer>(); //For multiple return values
        TaskDBAdapter db = new TaskDBAdapter(this);
        long TotalCount;//Total Subtasks
        long CompletedCount;//Completed Subtasks
        long[] Subtasks = new long[2];
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if (!f.exists()) {
                f.mkdirs();
                f.createNewFile();
                HelperUtil.CopyDB(getBaseContext().getAssets().open("TaskDB.db"), new FileOutputStream(destPath + "/TaskDB.db"));
            }


            db.openRead();


//
            parentCursor = db.getParentTask();
            long parentID = 0;


            if (parentCursor.moveToFirst())
                parentID = parentCursor.getInt(0);


            Log.d("Values", String.valueOf(parentID));

            Cursor All = db.getAllData();
            Log.d("ActivityCursor", DatabaseUtils.dumpCursorToString(All)); //OK

            TotalCount = db.getTotalSubtaskCount(parentID);

            CompletedCount = db.getFinishedSubtaskCount(parentID);


            db.close();
            Subtasks[0] = TotalCount;
            Subtasks[1] = CompletedCount;
            Log.d("Total Subtasks IN ARRAY", String.valueOf(Subtasks[0]));
            Log.d("Completed Subtasks IN A", String.valueOf(Subtasks[1]));


        } catch (Exception e) {
            Log.d("dataentry", e.getMessage());
            e.printStackTrace();
            Log.d("helloo", "help");
            db.close();

        }


        Log.d("Total Subtasks IN ARRAY", String.valueOf(Subtasks[0]));

        return Subtasks;
    }

// Generate Fragments

    public void displayGoalProgressFragment() {
        GoalProgressBarFragment goalProgressBarFragment = GoalProgressBarFragment.newInstance(dummy_progress);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        fragmentTransaction.add(R.id.Goal_Progress_Container,
                goalProgressBarFragment).addToBackStack(null).commit();

// Set boolean flag to indicate fragment is open.
        isFragmentDisplayed = true;
    }

    //TODO: Update these with actual subtasks
    public void displaySubtaskProgressFragment(int FinishedCount, int TotalCount) {
        //Log.d("FinishedAgain",String.valueOf(TotalCount));
        SubtaskProgressBarFragment subtaskProgressBarFragment = SubtaskProgressBarFragment.newInstance(FinishedCount, TotalCount);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.Subtask_Progress_Container,
                subtaskProgressBarFragment).addToBackStack(null).commit();

// Set boolean flag to indicate fragment is open.
        isFragmentDisplayed = true;
    }

    public void updateFragment() {
        // Get the FragmentManager.
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Check to see if the fragment is already showing.
        GoalProgressBarFragment simpleFragment = (GoalProgressBarFragment) fragmentManager
                .findFragmentById(R.id.Goal_Progress_Container);
        if (simpleFragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.remove(simpleFragment).commit();
        }

        // Set boolean flag to indicate fragment is closed.
        isFragmentDisplayed = false;
    }

    public void closeFragment() {
        // Get the FragmentManager.
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Check to see if the fragment is already showing.
        GoalProgressBarFragment simpleFragment = (GoalProgressBarFragment) fragmentManager
                .findFragmentById(R.id.Goal_Progress_Container);
        if (simpleFragment != null) {
            // Create and commit the transaction to remove the fragment.
            FragmentTransaction fragmentTransaction =
                    fragmentManager.beginTransaction();
            fragmentTransaction.remove(simpleFragment).commit();
        }

        // Set boolean flag to indicate fragment is closed.
        isFragmentDisplayed = false;
    }

    public boolean isDataPresent() {
        boolean isDataPresent = false;
        db.openRead();
        parentCursor = db.getParentTask();
        long parentID = 0;

        if (parentCursor.moveToFirst())
            isDataPresent = true;

        db.close();

        return isDataPresent;
    }


//TODO: Hide fragments and frame layout if no current goal
    //SEE PAGE 83


    //TODO: Arrow with completion date (poll from the data entry screen)
}


