package androiddev.jared.momatcollege;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddHomework extends Activity {

    private EditText dueDateText;
    private EditText dueTimeText;
    private DatePickerDialog dueDatePickerDialog;
    private TimePickerDialog dueTimePickerDialog;
    private DateFormat mDateFormat;
    private DateFormat mTimeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_homework);

        ////////////////////////////////////////////////////////////////////////// Time Date Picker Dialog Functionality ///////////////////////////////////////////////////////////////////////////////
        mDateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        mTimeFormat = DateFormat.getTimeInstance(DateFormat.LONG, Locale.US);

        dueDateText = (EditText) findViewById(R.id.dueDateEditText);
        dueDateText.setInputType(InputType.TYPE_NULL);
        dueTimeText = (EditText) findViewById(R.id.dueTimeEditText);
        dueTimeText.setInputType(InputType.TYPE_NULL);

        setUpDateTimeDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_homework, menu);
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

    private void setUpDateTimeDialog() {
        Calendar mCalendar = Calendar.getInstance();
        dueDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(year, monthOfYear, dayOfMonth);
                dueDateText.setText(mDateFormat.format(mDate.getTime()));
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        dueTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(0, 0, 0, hourOfDay, minute);
                dueTimeText.setText(mTimeFormat.format(mDate.getTime()));
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
}
