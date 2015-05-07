package androiddev.jared.momatcollege;


import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddTask extends ActionBarActivity {

    private static final String PREFS_NAME = "MomAtCollegePrefs";

    private EditText dueDateText;
    private EditText dueTimeText;
    private DatePickerDialog dueDatePickerDialog;
    private TimePickerDialog dueTimePickerDialog;
    private DateFormat mDateFormat;
    private DateFormat mTimeFormat;
    private Calendar mDueDate;
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        ////////////////////////////////////////////////////////////////////////// Time Date Picker Dialog Functionality ///////////////////////////////////////////////////////////////////////////////
        mDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        mTimeFormat = new SimpleDateFormat("h:mm a");

        dueDateText = (EditText) findViewById(R.id.taskDueDateEditText);
        dueDateText.setInputType(InputType.TYPE_NULL);
        dueTimeText = (EditText) findViewById(R.id.taskDueTimeEditText);
        dueTimeText.setInputType(InputType.TYPE_NULL);

        setUpDateTimeDialog();

        final ClassDbHelper mDbHelper = new ClassDbHelper(getApplicationContext());
        mDueDate = Calendar.getInstance();

        Button doneBtn = (Button) findViewById(R.id.taskDoneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get writable database
                SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                //validate user input, true if valid
                if ( !addDatabaseEntry(values) ) {
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
                else {
                    //create instance of GoogleCalendarHelper to store data into calendar content provider
                    //creates an event that will represent the task the user is entering
                    GoogleCalendarHelper mHelper = new GoogleCalendarHelper();
                    ContentResolver cr = getContentResolver();
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    long savedCalId = settings.getLong("calId", -1);
                    if ( savedCalId == -1 ) {
                        //ERROR
                        Toast.makeText(getApplicationContext(), "Internal Error, Please Restart App", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    //get the current locale of the user
                    Intent currIntent = getIntent();
                    String locale = currIntent.getStringExtra("classLocale");
                    String weekDay = mDueDate.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
                    //adds class data to calendar content provider
                    long calEventId = mHelper.createNewEventOnCalendar(getApplicationContext(), cr, mDueDate, null,
                            weekDay, values.getAsString(ClassDbHelper.TASK_FIELDS[3]), locale, savedCalId);
                    //adds newly create event id to database
                    values.put(ClassDbHelper.TASK_FIELDS[7], calEventId);

                    //adds all task data to database
                    long newRowId = mDb.insert(ClassDbHelper.TASK_TABLE_NAME, null, values);
                    //just using errorMsg variable, there is no error
                    Toast.makeText(getApplicationContext(), errorMsg + " (id:" + newRowId + ") Successfully Added!", Toast.LENGTH_LONG).show();

                    //clears notification if one exists (after FloatingPrompt triggers one)
                    NotificationManager mNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mNotMgr.cancel(2345);

                    finish();
                }
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.taskCancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //establishes the Date/Time dialog pickers
    private void setUpDateTimeDialog() {
        Calendar mCalendar = Calendar.getInstance();
        dueDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(year, monthOfYear, dayOfMonth);
                dueDateText.setText(mDateFormat.format(mDate.getTime()));
                mDueDate.set(Calendar.YEAR, year);
                mDueDate.set(Calendar.MONTH, monthOfYear);
                mDueDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        dueTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(0, 0, 0, hourOfDay, minute);
                dueTimeText.setText(mTimeFormat.format(mDate.getTime()));
                mDueDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mDueDate.set(Calendar.MINUTE, minute);
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);

        //binds click handler
        dueDateText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dueDatePickerDialog.show();
            }
        });
        dueTimeText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dueTimePickerDialog.show();
            }
        });
    }

    //validates user's input, returns true if valid
    //and populates a ContentValues object to be
    //eventually stored in our database
    private boolean addDatabaseEntry(ContentValues values) {

        //Class Id
        Intent currIntent = getIntent();
        int classId = currIntent.getIntExtra("classId", 0);
        values.put(ClassDbHelper.TASK_FIELDS[1], classId);

        //Task Type
        Spinner taskType = (Spinner) findViewById(R.id.taskTypeInput);
        if ( taskType.getSelectedItemPosition() == 0 ) {
            errorMsg = "Please select a class type";
            return false;
        }
        values.put(ClassDbHelper.TASK_FIELDS[2], taskType.getSelectedItemPosition());

        //Task Name
        EditText taskName = (EditText) findViewById(R.id.taskNameInput);
        if (taskName.getText().toString().equals("")) {
            errorMsg = "Please Enter a Task Name";
            return false;
        }
        values.put(ClassDbHelper.TASK_FIELDS[3], taskName.getText().toString());

        //Task Notes
        EditText teacherNotes = (EditText) findViewById(R.id.taskNotesInput);
        values.put(ClassDbHelper.TASK_FIELDS[4], teacherNotes.getText().toString());

        //Due Date
        if (dueDateText.getText().toString().equals("")) {
            errorMsg = "Please enter a due date";
            return false;
        }
        if (dueTimeText.getText().toString().equals("")) {
            errorMsg = "Please enter a due time";
            return false;
        }
        DateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        values.put(ClassDbHelper.TASK_FIELDS[5], mFormat.format(mDueDate.getTime()));

        //Task Complete Boolean
        //TODO: create this logic
        //temporary hardcoded
        values.put(ClassDbHelper.TASK_FIELDS[6], 0);

        errorMsg = taskType.getSelectedItem().toString() + " " + taskName.getText().toString();
        return true;
    }
}