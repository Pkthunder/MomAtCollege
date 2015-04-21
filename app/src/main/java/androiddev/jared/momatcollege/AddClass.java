package androiddev.jared.momatcollege;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class AddClass extends ActionBarActivity {

    private static final String TAG = AddClass.class.getSimpleName();

    //////////////////////////////////////////////////////////  Days of the Week Picker Variables   /////////////////////////////////////////////////////////////
    private TextView mMonPicker = null;
    private TextView mTuePicker = null;
    private TextView mWedPicker = null;
    private TextView mThrPicker = null;
    private TextView mFriPicker = null;
    private TextView mSatPicker = null;
    private TextView mSunPicker = null;
    private boolean[] weekdaySelections = { false, false, false, false, false, false, false };
    private Drawable weekdayBorder = null;

    //////////////////////////////////////////////////////////  Date Picker Dialog Variables   /////////////////////////////////////////////////////////////
    private EditText fromDateText;
    private EditText toDateText;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private DateFormat mDateFormat;

    //////////////////////////////////////////////////////////  Date Picker Dialog Variables   /////////////////////////////////////////////////////////////
    private EditText fromTimeText;
    private EditText toTimeText;
    private TimePickerDialog fromTimePickerDialog;
    private TimePickerDialog toTimePickerDialog;
    private DateFormat mTimeFormat;

    private Calendar startDateTime;
    private Calendar endDateTime;
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        //////////////////////////////////////////////////////////  Days of the week picker functionality   /////////////////////////////////////////////////////////////
        setWeekdayPicker();

        //////////////////////////////////////////////////////////  Date Picker Dialog Functionality   //////////////////////////////////////////////////////////////
        mDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        mTimeFormat = new SimpleDateFormat("h:mm a");

        fromDateText = (EditText) findViewById(R.id.fromDateEditText);
        fromDateText.setInputType(InputType.TYPE_NULL);
        toDateText = (EditText) findViewById(R.id.toDateEditText);
        toDateText.setInputType(InputType.TYPE_NULL);

        fromTimeText = (EditText) findViewById(R.id.fromTimeEditText);
        fromTimeText.setInputType(InputType.TYPE_NULL);
        toTimeText = (EditText) findViewById(R.id.toTimeEditText);
        toTimeText.setInputType(InputType.TYPE_NULL);

        setDateDialogs();
        setTimeDialogs();

        ///////////////////////////////////////////////////////////////////  Database Functionality   /////////////////////////////////////////////////////////////////
        final ClassDbHelper mDbHelper = new ClassDbHelper(getApplicationContext());
        startDateTime = Calendar.getInstance();
        endDateTime = Calendar.getInstance();

        Button doneBtn = (Button) findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                ContentValues alarmValues = new ContentValues();
                if ( !addDatabaseEntry(values) ) {
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                } else {

                    Log.i(TAG, values.toString());

                    long newRowId = mDb.insert(ClassDbHelper.CLASS_TABLE_NAME, null, values);
                    //just using errorMsg variable, there is no error
                    Toast.makeText(getApplicationContext(), errorMsg + " (id:" + newRowId + ") Successfully Added!", Toast.LENGTH_LONG).show();

                    CheckBox alarmBoolean = (CheckBox) findViewById(R.id.alarmInput);
                    if(alarmBoolean.isChecked()) {


                        addAlarmDatabaseEntry(alarmValues);
                        Log.i(TAG, alarmValues.toString());


                        Log.i(TAG, "HERE");
                        long newAlarmRowID = mDb.insert(ClassDbHelper.ALARM_TABLE_NAME, null, alarmValues);
                        String s = String.valueOf(newAlarmRowID);
                        Log.i(TAG, s);
                    }
                }
                finish();
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
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
        getMenuInflater().inflate(R.menu.menu_add_class, menu);
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

    private void toggleWeekdayClick(TextView weekday) {
        if (weekday.getBackground() == null) {
            weekday.setBackground(weekdayBorder);
        } else {
            weekday.setBackground(null);
        }
    }

    private void setWeekdayPicker() {
        weekdayBorder = getResources().getDrawable(R.drawable.text_view_border);

        mMonPicker = (TextView) findViewById(R.id.textMon);
        mMonPicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWeekdayClick(mMonPicker);
                weekdaySelections[0] = !(weekdaySelections[0]);
            }
        });
        mTuePicker = (TextView) findViewById(R.id.textTue);
        mTuePicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWeekdayClick(mTuePicker);
                weekdaySelections[1] = !(weekdaySelections[1]);
            }
        });
        mWedPicker = (TextView) findViewById(R.id.textWed);
        mWedPicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWeekdayClick(mWedPicker);
                weekdaySelections[2] = !(weekdaySelections[2]);
            }
        });
        mThrPicker = (TextView) findViewById(R.id.textThr);
        mThrPicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWeekdayClick(mThrPicker);
                weekdaySelections[3] = !(weekdaySelections[3]);
            }
        });
        mFriPicker = (TextView) findViewById(R.id.textFri);
        mFriPicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWeekdayClick(mFriPicker);
                weekdaySelections[4] = !(weekdaySelections[4]);
            }
        });
        mSatPicker = (TextView) findViewById(R.id.textSat);
        mSatPicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWeekdayClick(mSatPicker);
                weekdaySelections[5] = !(weekdaySelections[5]);
            }
        });
        mSunPicker = (TextView) findViewById(R.id.textSun);
        mSunPicker.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleWeekdayClick(mSunPicker);
                weekdaySelections[6] = !(weekdaySelections[6]);
            }
        });
    }

    private void setDateDialogs() {
        //Establishes a date picker dialog
        Calendar mCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(year, monthOfYear, dayOfMonth);
                fromDateText.setText(mDateFormat.format(mDate.getTime()));
                startDateTime.set(Calendar.YEAR, year);
                startDateTime.set(Calendar.MONTH, monthOfYear);
                startDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(year, monthOfYear, dayOfMonth);
                toDateText.setText(mDateFormat.format(mDate.getTime()));
                endDateTime.set(Calendar.YEAR, year);
                endDateTime.set(Calendar.MONTH, monthOfYear);
                endDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        //binds click handler
        fromDateText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });
        toDateText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });
    }

    private void setTimeDialogs() {
        //Establishes a time picker dialog
        Calendar mCalendar = Calendar.getInstance();
        fromTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(0, 0, 0, hourOfDay, minute);
                fromTimeText.setText(mTimeFormat.format(mDate.getTime()));
                startDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                startDateTime.set(Calendar.MINUTE, minute);
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);

        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(0, 0, 0, hourOfDay, minute);
                toTimeText.setText(mTimeFormat.format(mDate.getTime()));
                endDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endDateTime.set(Calendar.MINUTE, minute);
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);

        //binds click handler
        fromTimeText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTimePickerDialog.show();
            }
        });
        toTimeText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTimePickerDialog.show();
            }
        });
    }

    private boolean addDatabaseEntry( ContentValues values ) {

        //Class Name
        EditText className = (EditText) findViewById(R.id.classNameInput);
        if (className.getText().toString().equals("")) {
            errorMsg = "Please Enter a Class Name";
            return false;
        }
        values.put(ClassDbHelper.CLASS_FIELDS[1], className.getText().toString());

        //Location
        EditText location = (EditText) findViewById(R.id.locationInput);
        if (location.getText().toString().equals("")) {
            errorMsg = "Please Enter a Class Location";
            return false;
        }
        values.put(ClassDbHelper.CLASS_FIELDS[2], location.getText().toString());

        //Teacher Name
        EditText teacherName = (EditText) findViewById(R.id.teacherNameInput);
        if (teacherName.getText().toString().equals("")) {
            errorMsg = "Please Enter a Teacher's Name";
            return false;
        }
        values.put(ClassDbHelper.CLASS_FIELDS[3], teacherName.getText().toString());

        //Teacher Notes
        EditText teacherNotes = (EditText) findViewById(R.id.teacherNotesInput);
        values.put(ClassDbHelper.CLASS_FIELDS[4], teacherNotes.getText().toString());

        //Frequency
        String[] weekday_key = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
        String frequency = "";
        for (int i=0; i<7; i++) {
            if (weekdaySelections[i]) {
                frequency += weekday_key[i];
            }
        }
        if (frequency.equals("")) {
            errorMsg = "Please select the Weekdays your class occurs";
            return false;
        }
        values.put(ClassDbHelper.CLASS_FIELDS[5], frequency);

        //Start Date and Time && End Date and Time
        if( fromTimeText.getText().toString().equals("") ) {
            errorMsg = "Please enter a start time";
            return false;
        }
        if( toTimeText.getText().toString().equals("") ) {
            errorMsg = "Please enter a end time";
            return false;
        }
        if( fromDateText.getText().toString().equals("") ) {
            errorMsg = "Please enter a start date";
            return false;
        }
        if( toDateText.getText().toString().equals("") ) {
            errorMsg = "Please enter a end date";
            return false;
        }
        if (!(startDateTime.before(endDateTime))) {
            errorMsg = "Please check your start and end date!";
            return false;
        }
        DateFormat mFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        values.put(ClassDbHelper.CLASS_FIELDS[6], mFormat.format(startDateTime.getTime()));
        values.put(ClassDbHelper.CLASS_FIELDS[7], mFormat.format(endDateTime.getTime()));

        //Class Type
        Spinner classType = (Spinner) findViewById(R.id.classTypeInput);
        if ( classType.getSelectedItemPosition() == 0 ) {
            errorMsg = "Please select a class type";
            return false;
        }
        values.put(ClassDbHelper.CLASS_FIELDS[8], classType.getSelectedItem().toString());

        //Alarms Boolean
        CheckBox alarmBoolean = (CheckBox) findViewById(R.id.alarmInput);
        values.put(ClassDbHelper.CLASS_FIELDS[9], alarmBoolean.isChecked());

        //Not an error, just using the variable
        errorMsg = className.getText().toString();
        return true;
    }


    private boolean addAlarmDatabaseEntry( ContentValues alarmValues ){
        //Class Name
        EditText className = (EditText) findViewById(R.id.classNameInput);
        if (className.getText().toString().equals("")) {
            errorMsg = "Please Enter a Class Name";
            return false;
        }
        alarmValues.put(ClassDbHelper.ALARM_FIELDS[1], className.getText().toString());


        alarmValues.put(ClassDbHelper.ALARM_FIELDS[2], "Alarm name");
        alarmValues.put(ClassDbHelper.ALARM_FIELDS[3], 7);
        alarmValues.put(ClassDbHelper.ALARM_FIELDS[4], 35);
        alarmValues.put(ClassDbHelper.ALARM_FIELDS[5], "days of the week");
        alarmValues.put(ClassDbHelper.ALARM_FIELDS[6], "weekly or no");
        alarmValues.put(ClassDbHelper.ALARM_FIELDS[7], true);

        return true;
    }


}
