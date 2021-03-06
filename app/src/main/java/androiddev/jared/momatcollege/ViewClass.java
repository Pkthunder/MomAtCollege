package androiddev.jared.momatcollege;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
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
    private Button saveClassLocation;
    LocationManager locMgr;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        //retrieves the desired classId for database query
        Intent currIntent = getIntent();
        final int classId = currIntent.getIntExtra("classId", 0);
        if ( classId == 0 ) {
            Toast.makeText(getApplicationContext(), "Invalid ClassId", Toast.LENGTH_LONG).show();
            finish();
        }

        //Displays all the information about the class selected
        mClassName = (TextView) findViewById(R.id.class_name);
        mTeacherName = (TextView) findViewById(R.id.teacherName);
        mLocation = (TextView) findViewById(R.id.location);
        mfrequency = (TextView) findViewById(R.id.frequency);
        mTimeOfDay = (TextView) findViewById(R.id.timeOfDay);
        showTeacherNotes = (Button) findViewById(R.id.teacher_notes_btn);
        saveClassLocation = (Button) findViewById(R.id.save_location);

        //initialize instance of LocationManager
        locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Click handler for Save Location button
        //stores user's current location to be used as a baseline
        //for later location comparisons
        saveClassLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Kyle's code
                Location location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    Toast.makeText(getApplicationContext(), "No last known location. ", Toast.LENGTH_LONG).show();
                    return;
                }
                //gets instance of writable database
                //trys to update the longitude and latitude of the current class
                ClassDbHelper mDbHelper = new ClassDbHelper(getApplicationContext());
                SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

                try {
                    mDb.rawQuery("UPDATE " + ClassDbHelper.CLASS_TABLE_NAME +
                            " SET " + ClassDbHelper.CLASS_FIELDS[13] + " = " + String.valueOf(location.getLongitude()) +
                            ", " + ClassDbHelper.CLASS_FIELDS[14] + " = " + String.valueOf(location.getLatitude()) +
                            " WHERE " + ClassDbHelper.CLASS_FIELDS[0] + " = " + String.valueOf(classId), null );

                    Toast.makeText(getApplicationContext(), "Location Saved Successfully\n" +
                           String.valueOf(location.getLongitude() + ", " + String.valueOf(location.getLatitude())),
                           Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        lv = (ListView) findViewById(R.id.class_info_list);
    }

    @Override
    public void onResume() {
        super.onResume();

        //populates all class data Text Views AND
        //populates list view of all current tasks assigned to the current classId
        //data is retrieved from database query
        //this is done in onResume to ensure the most recent data

        //Gets the classId passed to Activity by the intent
        Intent intent = getIntent();
        int classId = intent.getIntExtra("classId", 0);
        if ( classId == 0 ) {
            Toast.makeText(getApplicationContext(), "Invalid ClassId", Toast.LENGTH_LONG).show();
            finish();
        }

        //Gets class data from database, by using the classId by through the intent
        if (!getClassDatabaseData(classId)) {
            Toast.makeText(getApplicationContext(), "Error Loading Data...", Toast.LENGTH_LONG).show();
        }

        //gets all tasks data from database assigned to the current classId
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

    //Navigation Functions
    public void addAClass(){
        //DONE: Intent to navigate to add a class
        Intent intent = new Intent(ViewClass.this, AddClass.class);
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

    //retrieves all class data from database with the corresponding classId
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

    //retrieves all tasks (and data) that are assigned to the current classId
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

                    //code below always threw an exception, unsure why....
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
