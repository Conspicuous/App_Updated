package com.example.smartgoals.navigator_0.db;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDBAdapter {
     static final String TAG = "TaskDBAdapter";

    static final String DATABASE_NAME = "TaskDB.db";
    static final String DATABASE_TABLE = "task";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE =
            " create table task ( id  integer primary key autoincrement,  " +
                    "            parentid integer  , " +
                    "            taskname TEXT ,  " +
                    "            descriptionTEXT  , " +
                    "            expected_enddate TEXT , " +
                    "            completed_date TEXT , " +
                    "            completed_bool  INTEGER  )  ;";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public TaskDBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database ");
            db.execSQL("DROP TABLE IF EXISTS task");
            onCreate(db);
        }
    }

    //---opens the database---
    public TaskDBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public TaskDBAdapter openRead() throws SQLException
    {
        db = DBHelper.getReadableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }

    public void updateParentCompleted(long id, int completed_bool)
    {
        db.update("task",
                getContent( completed_bool ),
                "id =" + id, null)  ;
    }
    private ContentValues getContent(  int completed_bool )
    {
        ContentValues dbRow = new ContentValues();
        dbRow.put("completed_bool", completed_bool);
        return dbRow;
    }

    private ContentValues getContent(long parentid, String taskname,
                                     String expected_enddate,
                                     String completed_date, int completed_bool )
    {
        ContentValues dbRow = new ContentValues();
        dbRow.put("parentid", parentid);
        dbRow.put("taskname",taskname);
        dbRow.put("expected_enddate", expected_enddate);
        dbRow.put("completed_date", completed_date);
        dbRow.put("completed_bool", completed_bool);
        return dbRow;
    }
    public long insertTask(String taskname)
    {

        return  insertTask(0, taskname, "", "", 0);
    }

    public long insertTask(long parentid, String taskname, String expected_enddate, String completed_date, int completed_bool )
    {
        return db.insert("task", null, getContent(parentid, taskname, expected_enddate, completed_date, completed_bool));
    }



    public Cursor getSubtasks(long parentid)
    {
        return db.rawQuery("select * from task where parentid=" +parentid ,null); //id, taskname, expected_enddate, completed_date, completed_bool
    }
    public Cursor getAllData()
    {
        return db.rawQuery("select * from task;",null);
    }

    public void update(long id,long parentid, String taskname, String expected_enddate, String completed_date, int completed_bool)
    {
        db.update("task",
                getContent(parentid, taskname, expected_enddate, completed_date, completed_bool ),
                "id =" + id, null)  ;
    }


    //---deletes a particular contact---
    public boolean deleteTask(long id)
    {
        return db.delete(DATABASE_TABLE,  "id=" + id, null) > 0;
    }
    public boolean deleteAllTasks()
    {
        return db.delete(DATABASE_TABLE,  null, null) > 0;
    }

    public Cursor getTask(long id) throws SQLException
    {
        Cursor mCursor = db.rawQuery("select * from task where id =" +id +";",null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor getParentTask() throws SQLException
    {
        Cursor mCursor = db.rawQuery("select * from task where   parentid=999;",null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor getAllTasks(long id) throws SQLException
    {
        Cursor mCursor = db.rawQuery("select distinct * from task where id =" +id +" or parentid=" + id +" order by id asc;",null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long getRecentParentid() throws SQLException
    {
        long parentid=0;
        Cursor mCursor = db.rawQuery("select  id from task where  parentid=999;",null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
            parentid = mCursor.getInt(0);
        }
        return parentid;
    }

    public long getParentid(String taskname) throws SQLException
    {
        long parentid=0;
        Cursor c = db.rawQuery("select parentid from task where taskname=" + taskname +";",null);

        if (c != null) {
            c.moveToFirst();
            parentid = c.getInt(0);
        }
        return parentid;
    }

    public int getTotalSubtaskCount(long parentid) throws SQLException
    {
        Cursor c = db.rawQuery("select count(*) from task where parentid=" + parentid +";",null);
        int count=0;
        if (c != null) {
            c.moveToFirst();
            count = c.getInt(0);

        }
        return count;
    }

    public int getFinishedSubtaskCount(long parentid) throws SQLException
    {
        Cursor c = db.rawQuery("select count(*) from task where parentid=" + parentid +" and completed_bool=1;",null);
        int count=0;
        if (c != null) {
            c.moveToFirst();
            count = c.getInt(0);

        }
        return count;
    }

    public int getUnfinishedSubtaskCount(long parentid) throws SQLException
    {
        Cursor c = db.rawQuery("select count(*) from task where parentid=" + parentid +" and completed_bool=0;",null);
        int count=0;
        if (c != null) {
            c.moveToFirst();
            count = c.getInt(0);

        }
        return count;
    }

}

