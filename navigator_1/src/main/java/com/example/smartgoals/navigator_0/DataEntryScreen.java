package com.example.smartgoals.navigator_0;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartgoals.navigator_0.EditTextDatePicker.*; //FOR Edit Text Date Pick
//import com.example.smartgoals.navigator_0.db.CalendarEntries; //MAY NEEED TO REMOVE

import com.example.smartgoals.navigator_0.db.TaskDBAdapter;
import com.example.smartgoals.navigator_0.util.HelperUtil;

import java.io.File;
import java.io.FileOutputStream;

public class DataEntryScreen extends  Activity implements View.OnClickListener {
    Button btnSave;
    Button btnDelete;
    Button btn_rewards;
    TaskDBAdapter db;
    long parentid=0;
    TextView txtView  ;
    Cursor parentCursor;
    BottomNavigationView bottomNavigationView ;
    EditTextDatePicker datePicker; //CASEY Added 3/5
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dataentryscreen);

            db = new TaskDBAdapter(this);
            btnSave = (Button)findViewById(R.id.btnSave);
            btnSave.setOnClickListener(  this);
            btnDelete = (Button)findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(  this);
            btn_rewards = (Button)findViewById(R.id.btn_rewards );
            btn_rewards.setOnClickListener(  this);
            datePicker = new EditTextDatePicker(this, R.id.dte_task0); //CASEY Added 3/4--> Enables Calendar Dialog Fragment onClick.

            bottomNavigationView = findViewById(R.id.navigation);
            bottomNavigationView.setOnClickListener(this);
            bottomNavigationView.setOnNavigationItemSelectedListener
                    (new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.create_new_goal_button:
                                    //ONLY DISPLAY THIS IF NO GOAL

                                    Intent intent = new Intent(getApplicationContext(), DataEntryScreen.class);
                                    startActivity(intent);
                                    break;
                                case R.id.update_goal_button:
                                    Intent intent1 = new Intent(getApplicationContext(), DataEntryScreen.class);
                                    startActivity(intent1);

                                    break;
                                case R.id.rewards_button:

                                    Intent intent2 = new Intent(getApplicationContext(), RewardsScreen.class);
                                    startActivity(intent2);
                                    break;

                                case R.id.navigation_dashboard:
                                    Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent3);
                            }
                            return true;
                        }
                    });

//REMOVE UN-NEEDED ITEMS
            bottomNavigationView.getMenu().removeItem(R.id.update_goal_button);
            bottomNavigationView.getMenu().removeItem(R.id.rewards_button);
            bottomNavigationView.getMenu().removeItem(R.id.create_new_goal_button);
            txtView = (TextView)findViewById(R.id.txtView);

            //RESUME EXECUTION
            String destPath = "/data/data/" + getPackageName() +   "/databases";
            File f = new File(destPath);
            if (!f.exists())
            {
                f.mkdirs();
                f.createNewFile();
               // HelperUtil.CopyDB(getBaseContext().getAssets().open("mydb"),  new FileOutputStream(destPath + "/MyDB"));
                HelperUtil.CopyDB(getBaseContext().getAssets().open("TaskDB.db"),  new FileOutputStream(destPath + "/TaskDB.db"));
            }
             db.open();

            parentCursor = db.getParentTask();
            if (parentCursor.moveToFirst())
                if (parentCursor != null)
                    DisplayParent(parentCursor);

             Cursor c = db.getSubtasks( parentid );//db.getAllData();

            int ii=1;
            if (c.moveToFirst())
            {
                do {
                    DisplayTask(c,ii);
                    ii++;
                    Log.d("iiii" +
                            "", String.valueOf(ii)); //CASEY Added 35
                } while (c.moveToNext());
            }


            db.close();
        }
        catch (Exception e)
        {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }
     }

    public void DisplayParent(Cursor c ) {
        try
        {
            EditText txt_task;
            EditText dte_task;//CASEY added 35
            TextView id_task;
            CheckBox cb_task;

            int i=0;
            long id=0;
            String taskname = "";

            txt_task = (EditText)findViewById( getResources().getIdentifier(  "txt_task"+i, "id", getPackageName())  );

            id_task = (TextView)findViewById( getResources().getIdentifier(  "id_task"+i, "id", getPackageName())  );
            taskname = c.getString(c.getColumnIndex("taskname"))  ; //c.getString(2)

            txt_task.setText(taskname);
            id_task.setText(Integer.toString(c.getInt(0)));


            parentid=c.getInt(0);
        }
        catch(Exception e) {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }

    }
    public void DisplayTask(Cursor c, int i) {
        try
        {
            EditText txt_task;
            EditText dte_task;
            TextView id_task;
            CheckBox cb_task;
            long id=0;
            String taskname = "";


            String date = "";//CASEY ADDED 3/5 to display saved date from database


            txt_task = (EditText)findViewById( getResources().getIdentifier(  "txt_task"+i, "id", getPackageName())  );
            dte_task = (EditText) findViewById(getResources().getIdentifier("txt_task" + i, "id", getPackageName()));

            id_task = (TextView)findViewById( getResources().getIdentifier(  "id_task"+i, "id", getPackageName())  );
            cb_task = (CheckBox)findViewById( getResources().getIdentifier(  "cb_task"+i, "id", getPackageName())  );
            taskname = c.getString(c.getColumnIndex("taskname"))  ; //c.getString(2)
            date = c.getString(c.getColumnIndex("expected_enddate"));
            Log.d("inIF", date);
            if (  taskname != null &&  taskname != "" )
            {

                txt_task.setText(taskname);
                id_task.setText(Integer.toString(c.getInt(0)));
                cb_task.setChecked( HelperUtil.getBoolValue(c.getInt(c.getColumnIndex("completed_bool"))) );
                Log.d("inIF", date);
                dte_task.setText(date);
            }
        }
        catch(Exception e) {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }

    }
    public void onClick(View view)
    {
        try
        {
            switch (view.getId())
            {
                case R.id.btnSave:
                    db.open();
                    saveParent();
                    for (int i=1;  i <=5; i++) {
                        save(  i);
                    }
                   if (db.getFinishedSubtaskCount(parentid) == db.getTotalSubtaskCount(parentid))
                    {
                       // alert("Congratulations on finishing all tasks!");
                        db.updateParentCompleted(parentid,1);
                    }
                    db.close();
                    break;
                case R.id.btnDelete:
                    db.open();
                    db.deleteAllTasks();
                    db.close();
                    showDataEntry(view);
                    break;
                case R.id.create_new_goal_button:
                    showDataEntry(view);
                    break;
                case R.id.rewards_button:
                    showRewards(view);
                    break;
                case R.id.update_goal_button:
                    showDataEntry(view);
                    break;
               // case R.id.btn_data_entry:
               //      showDataEntry(view);
                //    break;
                case R.id.btn_rewards:
                 showRewards(view);
                     break;
            }
        }
        catch(Exception e)
        {
            Log.d("ActivityInterface",e.getMessage());
        }
    }

    public void saveParent()
    {
        EditText txt_task;
        EditText dte_task; //Casey *addition*, 3/4 --> for GOAL end date --> parent holds the expected end date.
        TextView id_task;
        CheckBox cb_task;

        int i=0;
        long id=0;

        try
        {
            txt_task = (EditText) findViewById(getResources().getIdentifier("txt_task" + i, "id", getPackageName()));
            id_task = (TextView) findViewById(getResources().getIdentifier("id_task" + i, "id", getPackageName()));
            cb_task = (CheckBox) findViewById(getResources().getIdentifier("cb_task" + i, "id", getPackageName()));
            dte_task = (EditText) findViewById(getResources().getIdentifier("dte_task" + i, "id", getPackageName())); //CASEY added 3/4-->for GOAL end date
            Log.d("dtetask", dte_task.getText().toString()); //CASEY ADDED 34 debugging

            if (HelperUtil.isEmpty(txt_task))
                alert("Please complete the parent goal");
            else
            {
                if (!HelperUtil.isEmpty(id_task))
                    id = Long.valueOf(id_task.getText().toString());



                    if (id == 0)
                {
                    id = db.insertTask(999, txt_task.getText().toString(), dte_task.getText().toString(), "", HelperUtil.getIntValue(cb_task));
                        id_task.setText( Long.toString( id));
                        parentid = id;
                    } else
                        db.update(parentid, 999, txt_task.getText().toString(), dte_task.getText().toString(), "", HelperUtil.getIntValue(cb_task));



                alert("saved");
            }
        }
        catch(Exception e)
        {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }
    }
    public void save(int i)
    {
         EditText txt_task;
        EditText dte_task; //CASEY Added 3/4 for main goal ONLY
        TextView id_task;
        CheckBox cb_task;

        long id=0;

        try
        {
            txt_task = (EditText) findViewById(getResources().getIdentifier("txt_task" + i, "id", getPackageName()));
            //dte_task=findViewById(getResources().getIdentifier("dte_task"+ i,"id",getPackageName()));//CASEY Added 3/4
            id_task = (TextView) findViewById(getResources().getIdentifier("id_task" + i, "id", getPackageName()));
            cb_task = (CheckBox) findViewById(getResources().getIdentifier("cb_task" + i, "id", getPackageName()));


                    if (!HelperUtil.isEmpty(id_task))
                        id = Long.valueOf(id_task.getText().toString());

            /*ANITA Version 3/4*/
//                    if (!HelperUtil.isEmpty(txt_task) && id == 0) {
////                        id = db.insertTask(parentid, txt_task.getText().toString(), "", "",  HelperUtil.getIntValue(cb_task));
////                         id_task.setText(  Long.toString( id  )  );
////                    }

            /*CASEY Version 3/4*/
                    if (!HelperUtil.isEmpty(txt_task) && id == 0) {
                        id = db.insertTask(parentid, txt_task.getText().toString(), "", "", HelperUtil.getIntValue(cb_task));
                        id_task.setText(Long.toString(id));


                    }


            if (id > 0)
                    {
                        if (HelperUtil.isEmpty(txt_task))
                            db.deleteTask(id);
                        else
                            db.update(id, parentid, txt_task.getText().toString(), "", "", HelperUtil.getIntValue(cb_task));
                    }

            /*RESUME ANITA....*/
                     alert("saved");
                if ( HelperUtil.getIntValue(cb_task) == 1 )
                    alert("Congratulations on completing subtask: " + txt_task.getText());
        }
        catch(Exception e)
        {
            Log.d("dataentry",e.getMessage());
            e.printStackTrace();
        }
     }
    public  void alert(String msg)
    {
        Toast.makeText( this, msg,Toast.LENGTH_SHORT).show();
    }

    public void showDataEntry(View view)
    {
        startActivity(new Intent("com.scheduler.DataEntryScreen"));
    }

    public void showRewards(View view)
    {
        startActivity(new Intent("com.scheduler.RewardsScreen"));
    }

    public void onDestroy(){
        super.onDestroy();
        db.close();
    }
    public void OnResume() {
        db.open();
    }
    public void onPause() {
        super.onPause();
     db.close();
    }
}


