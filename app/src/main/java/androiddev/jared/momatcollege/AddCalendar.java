package androiddev.jared.momatcollege;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


public class AddCalendar extends ActionBarActivity {

    private static final String TAG = AddCalendar.class.getSimpleName();

    private Button addClassButton;
    private Button delete_class_button;

    private long lastEventID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_calendar);

        addClassButton = (Button) findViewById(R.id.add_class_button);
        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataForAddClass();
            }
        });

        delete_class_button = (Button) findViewById(R.id.delete_class_button);
        delete_class_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClass();
            }
        });




        //calendarCheck();
         calendarCreate();

        //checkForEvent();
        //modifyDisplayName();
        //addEventHopefully();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_calendar, menu);
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


    public void calendarCheck() {

        String[] projection =
                new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.ACCOUNT_TYPE};

        Cursor calCursor = getContentResolver().
                query(CalendarContract.Calendars.CONTENT_URI,
                        projection,
                        CalendarContract.Calendars.VISIBLE + " = 1",
                        null,
                        CalendarContract.Calendars._ID + " ASC");
        if (calCursor.moveToFirst()) {
            do {
                long id = calCursor.getLong(0);
                String displayName = calCursor.getString(1);

                Log.i(TAG, displayName);
                Log.i(TAG, "0000000000");
                Log.i(TAG, calCursor.getString(0) );

            } while (calCursor.moveToNext());
        }
    }

    public void calendarCreate()
    {
        ContentValues values = new ContentValues();

        //values for the calendar
        values.put(
                CalendarContract.Calendars.ACCOUNT_NAME,
                "MY_ACCOUNT_NAME");
        values.put(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(
                CalendarContract.Calendars.NAME,
                "MY_ACCOUNT_NAME");
        values.put(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                "MY_ACCOUNT_NAME");
        values.put(
                CalendarContract.Calendars.CALENDAR_COLOR,
                0xffff0000);
        values.put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(
                CalendarContract.Calendars.OWNER_ACCOUNT,
                "some.account@googlemail.com");
        values.put(
                CalendarContract.Calendars.CALENDAR_TIME_ZONE,
                "Europe/Berlin");
        values.put(
                CalendarContract.Calendars.SYNC_EVENTS,
                1);
        Uri.Builder builder =
                CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_NAME,
                "MY_ACCOUNT_NAME");
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(
                CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");

        //adds the calendar
        Uri uri = getContentResolver().insert(builder.build(), values);

        Log.i(TAG, "MADE IT HERE");
    }

    private long getCalendarId() {
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection =
                CalendarContract.Calendars.ACCOUNT_NAME +
                        " = ? " +
                        CalendarContract.Calendars.ACCOUNT_TYPE +
                        " = ? ";
        // use the same values as above:
        String[] selArgs =
                new String[]{
                        "MY_ACCOUNT_NAME",
                        CalendarContract.ACCOUNT_TYPE_LOCAL};
        Cursor cursor =
                getContentResolver().
                        query(
                                CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                selection,
                                selArgs,
                                null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }

    public void addEventHopefully()
    {

        long calId = getCalendarId();
        if (calId == -1) {
            // no calendar account; react meaningfully
            return;
        }
        Calendar cal = new GregorianCalendar(2012, 11, 14);
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long start = cal.getTimeInMillis();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, start);
        values.put(CalendarContract.Events.DTEND, start);
        values.put(CalendarContract.Events.RRULE,
                "FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
        values.put(CalendarContract.Events.TITLE, "Some title");
        values.put(CalendarContract.Events.EVENT_LOCATION, "Münster");
        values.put(CalendarContract.Events.CALENDAR_ID, calId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Berlin");
        values.put(CalendarContract.Events.DESCRIPTION,
                "The agenda or some description of the event");
// reasonable defaults exist:
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS,
                CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        values.put(CalendarContract.Events.ORGANIZER, "some.mail@some.address.com");
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
        values.put(CalendarContract.Events.GUESTS_CAN_MODIFY, 1);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        Uri uri =
                getContentResolver().
                        insert(CalendarContract.Events.CONTENT_URI, values);
        long eventId = new Long(uri.getLastPathSegment());

    }

    public void checkForEvent()
    {

        long begin = 1;// starting time in milliseconds
        long end = 1000000000;// ending time in milliseconds
        String[] proj =
                new String[]{
                        CalendarContract.Instances._ID,
                        CalendarContract.Instances.BEGIN,
                        CalendarContract.Instances.END,
                        CalendarContract.Instances.EVENT_ID};
        Cursor cursor =
                CalendarContract.Instances.query(getContentResolver(), proj, begin, end);
        if (cursor.getCount() > 0) {
            Log.i(TAG, "CONFLICT FOUND");
        }
        else
        {
            Log.i(TAG, "NO CONFLICT ");
        }
    }

    public void modifyDisplayName(){
        long calID = 2;
        ContentValues values = new ContentValues();
// The new display name for the calendar
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "Dalton's Calendar");
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, calID);
        int rows = getContentResolver().update(updateUri, values, null, null);
        Log.i(TAG, "Rows updated: " + rows);
    }

    public void addEvent( int calendar_id, String title, String description, long startMillis, long endMillis ){

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, calendar_id);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName(Locale.US)  );
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        //need to save event id somewhere

        lastEventID = eventID;

        String event_id = String.valueOf(eventID);

        addClassButton.setText(event_id);

        Log.i(TAG, event_id);

    }

    public void getDataForAddClass() {

        //all of the variables that will be passed to making a new event
        int calendar_id = 1;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, 4, 7, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, 4, 14, 8, 45);
        endMillis = endTime.getTimeInMillis();

        addEvent( calendar_id, "Event Title", "description of event", startMillis, endMillis );



        Log.i(TAG, "GETDATAFORADDCLASS");
    }

    public void deleteClass(){

        long eventID = lastEventID;

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = getContentResolver().delete(deleteUri, null, null);
        Log.i(TAG, "Rows deleted: " + rows);
    }


}
