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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartgoals.navigator_0.db.TaskDBAdapter;
import com.example.smartgoals.navigator_0.util.HelperUtil;

import java.io.File;
import java.io.FileOutputStream;

public class RewardsScreen extends Activity implements View.OnClickListener  {
    TaskDBAdapter db;
    long parentid=0;
    TextView txtView  ;
    Cursor parentCursor;
    BottomNavigationView bottomNavigationView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewardsscreen);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.create_new_goal_button:
                                //ONLY DISPLAY THIS IF NO GOAL

                                // Toast.makeText(getApplicationContext(),"Hello New Goal!",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), DataEntryScreen.class);
                                startActivity(intent);
                                // startActivity(new Intent("com.example.smartgoals.navigator_0.DataEntryScreen"));
                                break;
                            case R.id.update_goal_button:
                                Intent intent1 = new Intent(getApplicationContext(), DataEntryScreen.class);
                                startActivity(intent1);

                                //startActivity(new Intent("com.example.smartgoals.navigator_0.DataEntryScreen"));
                                break;
                            case R.id.rewards_button:

                                Intent intent2 = new Intent(getApplicationContext(), RewardsScreen.class);
                                startActivity(intent2);
                                //startActivity(new Intent("com.example.smartgoals.navigator_0.RewardsScreen"));
                                break;

                            case R.id.navigation_dashboard:
                                Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent3);
                        }
                        return true;
                    }
                });

        // bottomNavigationView.getMenu().removeItem(R.id.update_goal_button);
        bottomNavigationView.getMenu().removeItem(R.id.rewards_button);
        bottomNavigationView.getMenu().removeItem(R.id.create_new_goal_button); //not on homescreen ui

        String destPath = "/data/data/" + getPackageName() +   "/databases";
        File f = new File(destPath);
        try {

            db = new TaskDBAdapter(this);
        if (!f.exists())
        {
            f.mkdirs();
            f.createNewFile();
            HelperUtil.CopyDB(getBaseContext().getAssets().open("TaskDB.db"),  new FileOutputStream(destPath + "/TaskDB.db"));
        }
         db.openRead();
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
            } while (c.moveToNext());
        }
        db.close();
            }
catch(Exception e)
            {
              Log.d("rewards",e.getMessage());
               e.printStackTrace();
            }
    }

    public void DisplayParent(Cursor c ) {
        try
        {
            TextView txt_task;
            ImageView iv_task;

            int i=0;
            long id=0;

            txt_task = (TextView)findViewById( getResources().getIdentifier(  "txt_task"+i, "id", getPackageName())  );
              iv_task = (ImageView)findViewById( getResources().getIdentifier(  "badge_"+i, "id", getPackageName())  );

            txt_task.setText(  c.getString(c.getColumnIndex("taskname")) );
            parentid=c.getInt(0);
            if ( HelperUtil.getBoolValue(c.getInt(c.getColumnIndex("completed_bool")) ))
                iv_task.setVisibility(View.VISIBLE);
            else {
                iv_task.setImageResource(R.drawable.bluecircle);
                iv_task.setVisibility(View.VISIBLE);
            }
        }
        catch(Exception e) {
            Log.d("rewards",e.getMessage());
            e.printStackTrace();
        }

    }
    public void DisplayTask(Cursor c, int i) {
        try
        {
            TextView txt_task;
            ImageView iv_task;
            String taskname = "";
            long id=0;


            txt_task = (TextView)findViewById( getResources().getIdentifier(  "txt_task"+i, "id", getPackageName())  );
            iv_task = (ImageView)findViewById( getResources().getIdentifier(  "badge_"+i, "id", getPackageName())  );
            taskname = c.getString(c.getColumnIndex("taskname"));

            if ( taskname != "" )
            {

                txt_task.setText( taskname );
                if ( HelperUtil.getBoolValue(c.getInt(c.getColumnIndex("completed_bool")) ))
                    iv_task.setVisibility(View.VISIBLE);
                else {
                    iv_task.setImageResource(R.drawable.bluecircle);
                    iv_task.setVisibility(View.VISIBLE);
                }
                //iv_task.setVisibility(View.INVISIBLE);

            }
        }
        catch(Exception e) {
            Log.d("rewards",e.getMessage());
            e.printStackTrace();
        }

    }
    public void onClick(View view)
    {
        try
        {
            switch (view.getId())
            {


            }
        }
        catch(Exception e)
        {
            Log.d("ActivityInterface",e.getMessage());
        }
    }

    public void showDataEntry(View view)
    {
        startActivity(new Intent("com.scheduler.DataEntryScreen"));
    }

    public  void alert(String msg)
    {
        Toast.makeText( this, msg,Toast.LENGTH_SHORT).show();
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
