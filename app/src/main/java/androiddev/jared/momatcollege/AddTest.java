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


public class AddTest extends Activity {

    private EditText testDateText;
    private EditText testTimeText;
    private DatePickerDialog testDatePickerDialog;
    private TimePickerDialog testTimePickerDialog;
    private DateFormat mDateFormat;
    private DateFormat mTimeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        ////////////////////////////////////////////////////////////////////////// Time Date Picker Dialog Functionality ///////////////////////////////////////////////////////////////////////////////
        mDateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        mTimeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.US);

        testDateText = (EditText) findViewById(R.id.testDateEditText);
        testDateText.setInputType(InputType.TYPE_NULL);
        testTimeText = (EditText) findViewById(R.id.testTimeEditText);
        testTimeText.setInputType(InputType.TYPE_NULL);

        setUpDateTimeDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_test, menu);
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
        testDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(year, monthOfYear, dayOfMonth);
                testDateText.setText(mDateFormat.format(mDate.getTime()));
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        testTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar mDate = Calendar.getInstance();
                mDate.set(0, 0, 0, hourOfDay, minute);
                testTimeText.setText(mTimeFormat.format(mDate.getTime()));
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), false);

        //binds click handler
        testDateText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDatePickerDialog.show();
            }
        });
        testTimeText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testTimePickerDialog.show();
            }
        });
    }
}
