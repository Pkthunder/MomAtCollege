package androiddev.jared.momatcollege;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ViewClass extends ActionBarActivity {

    private TextView mClassName;
    private TextView mTeacherName;
    private TextView mLocation;
    private TextView mfrequency;
    private TextView mTimeOfDay;
    private Button showTeacherNotes;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        //Displays all the information about the class selected
        mClassName = (TextView) findViewById(R.id.class_name);
        mTeacherName = (TextView) findViewById(R.id.teacherName);
        mLocation = (TextView) findViewById(R.id.location);
        mfrequency = (TextView) findViewById(R.id.frequency);
        mTimeOfDay = (TextView) findViewById(R.id.timeOfDay);
        showTeacherNotes = (Button) findViewById(R.id.teacher_notes_btn);

        lv = (ListView) findViewById(R.id.class_info_list);

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
        Calendar mCal = Calendar.getInstance(Locale.US);

        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, mCal.getTimeInMillis());
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);

    }

    public void homework(){
        //DONE: Intent to navigate to add a homework

        Intent intent = new Intent(ViewClass.this, AddTask.class);
        Intent currIntent = getIntent();
        int classId = currIntent.getIntExtra("classId", 0);
        intent.putExtra("classId", classId);
        String locale = currIntent.getStringExtra("classLocale");
        intent.putExtra("classLocale", locale);
        startActivity(intent);

    }

    private boolean getClassDatabaseData(int classId) {
        ClassDbHelper mHelper = new ClassDbHelper(getApplicationContext());
        SQLiteDatabase mDb = mHelper.getWritableDatabase();

        final Cursor c = mDb.rawQuery(ClassDbHelper.classSelectById(classId), null);

        if (c.moveToFirst()) {
            //Class Name
            mClassName.setText(
                    c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[1])) );

            //Teacher Name
            mTeacherName.setText( "Teacher: " +
                    c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[3])) );

            //Class Location
            mLocation.setText( "Location: " +
                    c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[2])) );

            //Days of Week (Frequency)
            mfrequency.setText( "When: " +
                    ClassDbHelper.formatDaysOfWeek(
                            c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[5])) ));

            mTimeOfDay.setText( "Time: " +
                    ClassDbHelper.formatTime(
                            c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[6]))));

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

                    /*
                    DateFormat dateFromDb = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    DateFormat dateToDisplay = new SimpleDateFormat("MMM dd, yyyy at HH:mm");
                    String mDueDate = "Error loading date...";
                    try {
                        mDueDate = dateToDisplay.format( dateFromDb.parse(
                                c.getString(c.getColumnIndex(ClassDbHelper.TASK_FIELDS[5])) ));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    */
                    String mDueDate = c.getString(c.getColumnIndex(ClassDbHelper.TASK_FIELDS[5]));
                    msg = "Due Date:\n\t" + mDueDate + "\n\n";
                    msg += "Notes:\n\t";
                    msg += c.getString(c.getColumnIndex(ClassDbHelper.TASK_FIELDS[4]));
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
