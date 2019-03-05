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

import com.example.smartgoals.navigator_0.db.TaskDBAdapter;
import com.example.smartgoals.navigator_0.util.HelperUtil;

import java.io.File;
import java.io.FileOutputStream;

//import com.example.smartgoals.navigator_0.db.CalendarEntries; //MAY NEEED TO REMOVE

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
    String currentDate;

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
//            currentDate=datePicker.getCurrentDate();
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

//REMOVE UN-NEEDED ITEMS (Casey Add, 3/3)
            bottomNavigationView.getMenu().removeItem(R.id.update_goal_button);
            bottomNavigationView.getMenu().removeItem(R.id.rewards_button);
            bottomNavigationView.getMenu().removeItem(R.id.create_new_goal_button);
            txtView = (TextView)findViewById(R.id.txtView);

//ANITA'S WORK CONTINUES NOW...
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
            Log.d("dataentryA", e.getMessage());
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

//CASEY ADDED for DATE
            dte_task = (EditText) findViewById(getResources().getIdentifier("dte_task" + i, "id", getPackageName())); // CASEY Added 35
//ANITA
            id_task = (TextView)findViewById( getResources().getIdentifier(  "id_task"+i, "id", getPackageName())  );
            taskname = c.getString(c.getColumnIndex("taskname"))  ; //c.getString(2)

            txt_task.setText(taskname);
            id_task.setText(Integer.toString(c.getInt(0)));
//CASEY ADDED for DATE
            dte_task.setText(c.getString(c.getColumnIndex("expected_enddate")));//CASEY Added 3/5

            parentid=c.getInt(0);
        }
        catch(Exception e) {
            Log.d("dataentryB", e.getMessage());
            e.printStackTrace();
        }

    }
    public void DisplayTask(Cursor c, int i) {
        try
        {
            EditText txt_task;
            //EditText dte_task; --> FUTURE function (adding subtask dates)
            TextView id_task;
            CheckBox cb_task;
            long id=0;
            String taskname = "";


            String date = "";//CASEY ADDED 3/5 to display saved date from database


            txt_task = (EditText)findViewById( getResources().getIdentifier(  "txt_task"+i, "id", getPackageName())  );
            //dte_task = (EditText) findViewById(getResources().getIdentifier("txt_task" + i, "id", getPackageName()));

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
                // dte_task.setText(date);
            }
        }
        catch(Exception e) {
            Log.d("dataentryC", e.getMessage());
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
        String currentDate = datePicker.getCurrentDate();
        int i=0;
        long id=0;

        try
        {
            txt_task = (EditText) findViewById(getResources().getIdentifier("txt_task" + i, "id", getPackageName()));
            id_task = (TextView) findViewById(getResources().getIdentifier("id_task" + i, "id", getPackageName()));
            cb_task = (CheckBox) findViewById(getResources().getIdentifier("cb_task" + i, "id", getPackageName()));
            dte_task = (EditText) findViewById(getResources().getIdentifier("dte_task" + i, "id", getPackageName())); //CASEY added 3/4-->for GOAL end date
            Log.d("dtetask", dte_task.getText().toString()); //CASEY ADDED 34 debugging
            // if((HelperUtil.isEmpty(txt_task)


            if (HelperUtil.isEmpty(txt_task) || HelperUtil.isEmpty(dte_task))
                alert("Please complete both the parent goal and the goal end date!");

//            if (!validateFields(txt_task))
//                alert("Please complete the parent goal!");
//            if (!validateFields(dte_task))
//                alert("Please enter goal date!");
            else
            {
                if (!HelperUtil.isEmpty(id_task))
                    id = Long.valueOf(id_task.getText().toString());



                    if (id == 0)
                {
                    id = db.insertTask(999, txt_task.getText().toString(), dte_task.getText().toString(), currentDate, HelperUtil.getIntValue(cb_task));
                        id_task.setText( Long.toString( id));
                        parentid = id;
                    } else
                        db.update(parentid, 999, txt_task.getText().toString(), dte_task.getText().toString(), currentDate, HelperUtil.getIntValue(cb_task));



                alert("saved");
            }
        }
        catch(Exception e)
        {
            Log.d("dataentryC", e.getMessage());
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
            Log.d("dataentryD", e.getMessage());
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


    //CASEY Added 3/5--> Require validation.//Future Implementation to REQUIRE valid goal and date.
//    private boolean validateFields(EditText Text) {
//        int yourDesiredLength = 4; //Don't want user entering NOTHING
//        if (Text.getText().length() < yourDesiredLength) {
//            Text.setError("Please Enter Field!");
//            Log.e("AAAA",Text.getText().toString());
//
//            return false;
//        } else {
//            Log.e("BBBB",Text.getText().toString());
//            return true;
//        }
//    }

//    private boolean validateFields(EditText Text, EditText Date_Text) {
//        int yourDesiredLength = 1; //Don't want user entering NOTHING
//        if (Goal_Text.getText().length() < yourDesiredLength) {
//            Goal_Text.setError("Please Enter a Main Goal!");
//            return false;
//        } else if (Date_Text.getText().length() < yourDesiredLength) {
//            Date_Text.setError("Please Enter a Date");
//            return false;
//        } else {
//            return true;
//        }
//    }


}




