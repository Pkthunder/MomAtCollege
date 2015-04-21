package androiddev.jared.momatcollege;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewClass extends ActionBarActivity {

    private TextView mClassName;
    private TextView mTeacherName;
    private TextView mLocation;
    private TextView mfrequency;
    //private Button showTeacherNotes;
    private TextView showTeacherNotes;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        //TODO: Read from database and get values to plug into template

        //Get class selected from MainActivity
        Intent intent = getIntent();
        String text = intent.getStringExtra("listItem");
        int classId = intent.getIntExtra("classId", 0);

        //Displays all the information about the class selected
        mClassName = (TextView) findViewById(R.id.class_name);
        //mClassName.setText(mClassName.getText() + text + " (" + classId + ")");

        mTeacherName = (TextView) findViewById(R.id.teacherName);
       // mTeacherName.setText(mTeacherName.getText() + " Guanling Chen");

        mLocation = (TextView) findViewById(R.id.location);
       // mLocation.setText(mLocation.getText() + " Olsen 402");

        mfrequency = (TextView) findViewById(R.id.frequency);
       // mfrequency.setText(mfrequency.getText() + "Mon Wed 10-11");

        //showTeacherNotes = (Button) findViewById(R.id.teacher_notes_btn);
        showTeacherNotes = (TextView) findViewById(R.id.teacher_notes_btn);


        //select the list
        lv = (ListView) findViewById(R.id.class_info_list);

        //Populates Class with dummy assignments
       /* final List<String> class_list = new ArrayList<String>();
        class_list.add("Assignment 1");
        class_list.add("Assignment 2");
        class_list.add("Assignment 3");
        class_list.add("Assignment 4");
        class_list.add("Assignment 5");
        class_list.add("Exam 1");
        class_list.add("Assignment 6");
        class_list.add("Assignment 7");
        class_list.add("Assignment 8");
        class_list.add("Assignment 9");
        class_list.add("Assignment 10");
        class_list.add("Exam 2");


        //list adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, class_list);

        lv.setAdapter(arrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //TODO: When each menu item is clicked, navigate to that Homework or Exam info
                //this could also be a popup that gives necessary info

                AlertDialog.Builder alert = new AlertDialog.Builder(ViewClass.this);

                //Loops through array and saves every element into x
                String x = "";
                for( int i=0 ; i<class_list.size() ; i++ ){
                    x = x + class_list.get(i) + "\n";
                }

                // displays x (Just the list of Assignments) as the alert body
                alert.setMessage(x);

                // sets title to be the assignment selected
                String assignNo = ((TextView)view).getText().toString();
                alert.setTitle(assignNo);
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface inter, int arg){
                        //Doing nothing here just dismisses dialog
                    }
                });
                alert.setCancelable(true);
                alert.create().show();


            }
        }); */

    }

    @Override
    public void onResume() {
        super.onResume();

        //Gets the classId passed to Activity by the intent
        Intent intent = getIntent();
        int classId = intent.getIntExtra("classId", 0);

        //Gets data from database
        if (!getClassDatabaseData(classId)) {
            Toast.makeText(getApplicationContext(), "Error Loading Data...", Toast.LENGTH_LONG).show();
        }
        //TODO: Finish implementation of task list
        getTaskDatabaseData(classId);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //DONE: Switch cases will add a new homework, test, and schedule
        switch (item.getItemId()) {
            case R.id.add_a_class:
                addAClass();
                return true;
            case R.id.view_schedule:
                schedule();
                return true;
            case R.id.add_a_homework:
                homework();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addAClass(){
        //DONE: Intent to navigate to add a class
        Intent intent = new Intent(ViewClass.this, AddTask.class);
        startActivity(intent);
    }

    public void schedule(){
        //DONE: Intent to navigate to view schedule

        Intent intent = new Intent(ViewClass.this, AddCalendar.class);
        startActivity(intent);

    }

    public void homework(){
        //DONE: Intent to navigate to add a homework

        Intent intent = new Intent(ViewClass.this, AddTask.class);
        Intent currIntent = getIntent();
        int classId = currIntent.getIntExtra("classId", 0);
        intent.putExtra("classId", classId);
        startActivity(intent);

    }

    private boolean getClassDatabaseData(int classId) {
        ClassDbHelper mHelper = new ClassDbHelper(getApplicationContext());
        SQLiteDatabase mDb = mHelper.getWritableDatabase();

        final Cursor c = mDb.rawQuery(ClassDbHelper.classSelectById(classId), null);

        if (c.moveToFirst()) {
            //Class Name
            mClassName.setText( mClassName.getText() + " " +
                    c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[1])) );

            //Teacher Name
            mTeacherName.setText( mTeacherName.getText() + " " +
                    c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[3])) );

            //Class Location
            mLocation.setText( mLocation.getText() + " " +
                    c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[2])) );

            //Days of Week (Frequency)
            mfrequency.setText( mfrequency.getText() + " " +
                    ClassDbHelper.formatDaysOfWeek(
                            c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[5]))
                    ) );

            showTeacherNotes.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewClass.this);
                    String msg = c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[4]));
                    alert.setMessage(msg);
                    alert.setTitle("Teacher Notes");
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface inter, int arg){
                            //Doing nothing here just dismisses dialog
                        }
                    });
                    alert.setCancelable(true);
                    alert.create().show();
                }
            });
        } else {
            return false;
        }

        return true;
    }

    private boolean getTaskDatabaseData( int classId ) {
        ClassDbHelper mHelper = new ClassDbHelper(getApplicationContext());
        SQLiteDatabase mDb = mHelper.getWritableDatabase();

        final Cursor c = mDb.rawQuery(ClassDbHelper.taskSelectById(classId), null);
        //TODO: Create and use a custom ListView  - Temporary one for now
        final List<String> taskList = new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, taskList);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                taskList.add(
                        c.getString(c.getColumnIndex(ClassDbHelper.TASK_FIELDS[3])) );

                c.moveToNext();
            }
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewClass.this);
                    String msg = "";

                    c.moveToPosition(position);
                    msg += c.getString(c.getColumnIndex(ClassDbHelper.TASK_FIELDS[3])) + "\n";
                    msg += c.getString(c.getColumnIndex(ClassDbHelper.TASK_FIELDS[4])) + "\n";
                    msg += c.getString(c.getColumnIndex(ClassDbHelper.TASK_FIELDS[5]));
                    alert.setMessage(msg);

                    alert.setTitle(c.getString(c.getColumnIndex(ClassDbHelper.TASK_FIELDS[3])));
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface inter, int arg){
                            //Doing nothing here just dismisses dialog
                        }
                    });
                    alert.setCancelable(true);
                    alert.create().show();
                }
            });
        } else {
            return false;
        }

        return true;
    }

}
