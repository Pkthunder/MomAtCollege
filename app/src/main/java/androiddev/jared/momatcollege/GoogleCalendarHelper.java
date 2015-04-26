package androiddev.jared.momatcollege;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * Created by Jared on 4/23/2015.
 * Methods to access the Google Calendar Content Provider
 */
public class GoogleCalendarHelper {
    private static final String TAG = GoogleCalendarHelper.class.getSimpleName();

    private static final String[] GCH_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.OWNER_ACCOUNT
    };

    private static final int GCH_PROJ_ID = 0;
    private static final int GCH_PROJ_ACCOUNT = 1;
    private static final int GCH_PROJ_DISPLAY = 2;
    private static final int GCH_PROJ_OWNER = 3;

    private static final String GCH_ACCOUNT_NAME = "androiddev.jared.momatcollege";
    private static final String GCH_CAL_NAME = "MomAtCollege Calendar";
    private static final String GCH_OWNER_ACC = "jared_perreault@yahoo.com";

    public long createCalendar(ContentResolver cr) {
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Calendars.ACCOUNT_NAME, GCH_ACCOUNT_NAME);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, GCH_CAL_NAME);
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, GCH_CAL_NAME);
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0x000ff000);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, GCH_OWNER_ACC);
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().getDisplayName());
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);


        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();

        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, GCH_ACCOUNT_NAME);
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");

        Uri uri = cr.insert(builder.build(), values);
        long eventId = new Long(uri.getLastPathSegment());

        return eventId;
    }

    public long getCalendarId(ContentResolver cr) {
        String[] projection = new String[]{CalendarContract.Calendars._ID};

        String selection = CalendarContract.Calendars.ACCOUNT_NAME + " = ? " +
            CalendarContract.Calendars.NAME + " = ? ";

        String[] selArgs = new String[] { GCH_ACCOUNT_NAME, GCH_CAL_NAME};
        Cursor cursor =
                cr.query(
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

    public long createNewEventOnCalendar(Context context, ContentResolver cr, Calendar startDateTime, Calendar endDateTime,
                                    String daysOfWeek, String nameOfEvent, String locOfEvent, long savedCalId) {

        ContentValues values = new ContentValues();

        //Adding a Task
        if ( endDateTime == null ) {
            values.put(CalendarContract.Events.DESCRIPTION,
                    nameOfEvent + " is due " + startDateTime.getTime().toString());

            values.put(CalendarContract.Events.DURATION, "PT10M");
        }
        //Adding a Class
        else {
            //recurrence rule
            String byDay = "";
            String[] days = ClassDbHelper.formatDaysOfWeek((daysOfWeek)).split(" ");
            for (int i = 0; i < days.length; i++) {
                byDay += days[i];
            }
            values.put(CalendarContract.Events.RRULE,
                    "FREQ=DAILY;BYDAY=" + byDay + ";WKST=SU");
            values.put(CalendarContract.Events.DESCRIPTION,
                    "This is a class added my MomAtCollege!");

            //calculate duration
            int startHour = startDateTime.get(Calendar.HOUR_OF_DAY);
            int startMin = startDateTime.get(Calendar.MINUTE);
            int endHour = endDateTime.get(Calendar.HOUR_OF_DAY);
            int endMin = endDateTime.get(Calendar.MINUTE);
            int hourDuration = Math.abs(endHour - startHour);
            int minDuration = Math.abs(endMin - startMin);
            String eventDuration = "PT" + String.valueOf(hourDuration) + "H" + String.valueOf(minDuration) + "M";
            values.put(CalendarContract.Events.DURATION, eventDuration);
        }

        values.put(CalendarContract.Events.DTSTART, startDateTime.getTimeInMillis());
        //values.put(CalendarContract.Events.DTEND, endDateTime.getTimeInMillis());
        //values.put(CalendarContract.Events.DURATION, "PT1H");

        values.put(CalendarContract.Events.TITLE, nameOfEvent);
        values.put(CalendarContract.Events.EVENT_LOCATION, locOfEvent);
        values.put(CalendarContract.Events.CALENDAR_ID, savedCalId);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());

        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS,
                CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.ALL_DAY, 0);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventId = new Long(uri.getLastPathSegment());
        Toast.makeText(context, "Created Event! (id:" + eventId + ")", Toast.LENGTH_LONG).show();

        return eventId;
    }

}
