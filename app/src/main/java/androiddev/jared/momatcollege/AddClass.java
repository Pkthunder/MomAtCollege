package androiddev.jared.momatcollege;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddClass extends ActionBarActivity {

    //////////////////////////////////////////////////////////  Days of the Week Picker Variables   /////////////////////////////////////////////////////////////
    private TextView mMonPicker = null;
    private TextView mTuePicker = null;
    private TextView mWedPicker = null;
    private TextView mThrPicker = null;
    private TextView mFriPicker = null;
    private TextView mSatPicker = null;
    private TextView mSunPicker = null;
    private Boolean[] weekdaySelections = { false, false, false, false, false, false, false };
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

    private Calendar fromDateTime;
    private Calendar toDateTime;
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        //Temporary - Navigate to Add A Homework
        Button addHomework = (Button) findViewById(R.id.addHwBtn);
        addHomework.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddClass.this, AddHomework.class);
                startActivity(intent);
            }
        });

        //Temporary - Navigate to Add A Test
        Button addTest = (Button) findViewById(R.id.addTestBtn);
        addTest.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddClass.this, AddTest.class);
                startActivity(intent);
            }
        });

        //Temporary - Navigate to Add A Calendar
        Button addCalendar = (Button) findViewById(R.id.addCalBtn);
        addCalendar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddClass.this, AddCalendar.class);
                startActivity(intent);
            }
        });


        //////////////////////////////////////////////////////////  Days of the week picker functionality   /////////////////////////////////////////////////////////////
        setWeekdayPicker();

        //////////////////////////////////////////////////////////  Date Picker Dialog Functionality   /////////////////////////////////////////////////////////////
        mDateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        mTimeFormat = DateFormat.getTimeInstance(DateFormat.LONG, Locale.US);

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

        Button doneBtn = (Button) findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                if ( !addDatabaseEntry(mDb, values) ) {
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), errorMsg + " Successfully Added!", Toast.LENGTH_LONG).show();
                }
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
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(year, monthOfYear, dayOfMonth);
                toDateText.setText(mDateFormat.format(mDate.getTime()));
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
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);

        toTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(0, 0, 0, hourOfDay, minute);
                toTimeText.setText(mTimeFormat.format(mDate.getTime()));
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

    private boolean addDatabaseEntry( SQLiteDatabase mDb, ContentValues values ) {

        //Class Name
        EditText className = (EditText) findViewById(R.id.classNameInput);
        if (className.getText().toString().equals("")) {
            errorMsg = "Please Enter A Class Name";
            return false;
        }
        values.put(ClassDbHelper.VAL_NAMES[1], className.getText().toString());

        //Location
        EditText location = (EditText) findViewById(R.id.locationInput);
        if (location.getText().toString().equals("")) {
            errorMsg = "Please Enter A Class Location";
            return false;
        }
        values.put(ClassDbHelper.VAL_NAMES[2], location.getText().toString());

        //Teacher Name
        EditText teacherName = (EditText) findViewById(R.id.teacherNameInput);
        if (teacherName.getText().toString().equals("")) {
            errorMsg = "Please Enter A Teacher's Name";
            return false;
        }
        values.put(ClassDbHelper.VAL_NAMES[3], teacherName.getText().toString());

        //Teacher Notes
        EditText teacherNotes = (EditText) findViewById(R.id.teacherNotesInput);
        values.put(ClassDbHelper.VAL_NAMES[4], teacherNotes.getText().toString());

        //Frequency
        String[] weekday_key = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
        String frequency = "";
        for (int i=0; i<7; i++) {
            if (weekdaySelections[i]) {
                frequency += weekday_key[i];
            }
        }
        if (frequency.equals("")) {
            errorMsg = "Please Select The Weekdays Your Class Occurs";
            return false;
        }
        values.put(ClassDbHelper.VAL_NAMES[5], frequency);

        //Start Date and Time
            //TODO: this
            values.put(ClassDbHelper.VAL_NAMES[6], "2015-04-15 14:23:45"); //temp hardcoded

        //End Date and Time
            //TODO: this
            values.put(ClassDbHelper.VAL_NAMES[7], "2015-04-20 10:23:45"); //temp hardcoded

        //Class Type
        Spinner classType = (Spinner) findViewById(R.id.classTypeInput);
        values.put(ClassDbHelper.VAL_NAMES[8], classType.getSelectedItem().toString());

        errorMsg = className.getText().toString();
        return true;
    }
}
