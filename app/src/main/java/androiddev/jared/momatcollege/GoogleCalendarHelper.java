package androiddev.jared.momatcollege;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * Created by Jared on 4/23/2015.
 * Methods to access the Google Calendar Content Provider
 */
public class GoogleCalendarHelper {
    //private static final String TAG = GoogleCalendarHelper.class.getSimpleName();

    private static final String GCH_ACCOUNT_NAME = "androiddev.jared.momatcollege";
    private static final String GCH_CAL_NAME = "MomAtCollege Calendar";
    private static final String GCH_OWNER_ACC = "jared_perreault@yahoo.com";

    public long createCalendar(ContentResolver cr) {
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Calendars.ACCOUNT_NAME, GCH_ACCOUNT_NAME);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, GCH_CAL_NAME);
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, GCH_CAL_NAME);
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, 0x00ffff00);
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

    public long createNewEventOnCalendar(Context context, ContentResolver cr, Calendar startDateTime, Calendar endDateTime,
                                    String daysOfWeek, String nameOfEvent, String locOfEvent, long savedCalId) {

        ContentValues values = new ContentValues();

        //Adding a Task
        if ( endDateTime == null ) {
            values.put(CalendarContract.Events.DESCRIPTION,
                    nameOfEvent + " is due at " + startDateTime.getTime().toString());

            values.put(CalendarContract.Events.DURATION, "PT10M");
            values.put(CalendarContract.Events.EVENT_COLOR, Color.rgb(35, 35, 69));
        }
        //Adding a Class
        else {
            //recurrence rule
            //Creating BYDAY value
            String byDay = "";
            String[] days = ClassDbHelper.formatDaysOfWeek((daysOfWeek)).split(" ");
            for (int i = 0; i < days.length; i++) {
                byDay += days[i];
            }

            //Creating UNTIL value
            String endYear = String.valueOf(endDateTime.get(Calendar.YEAR));
            String endMonth = String.valueOf(endDateTime.get(Calendar.MONTH));
            String endDay = String.valueOf(endDateTime.get(Calendar.DAY_OF_MONTH));
            if ( endMonth.length() == 1 ) { endMonth = "0" + endMonth; }
            if ( endDay.length() == 1 ) { endDay = "0" + endDay; }

            values.put(CalendarContract.Events.RRULE,
                    "FREQ=DAILY;BYDAY=" + byDay + ";WKST=SU;UNTIL=" + endYear + endMonth + endDay + ";");
            values.put(CalendarContract.Events.DESCRIPTION,
                    "This Class was Added by MomAtCollege!");

            //calculate duration
            int startHour = startDateTime.get(Calendar.HOUR_OF_DAY);
            int startMin = startDateTime.get(Calendar.MINUTE);
            int endHour = endDateTime.get(Calendar.HOUR_OF_DAY);
            int endMin = endDateTime.get(Calendar.MINUTE);
            int hourDuration = Math.abs(endHour - startHour);
            int minDuration = Math.abs(endMin - startMin);
            String eventDuration = "PT" + String.valueOf(hourDuration) + "H" + String.valueOf(minDuration) + "M";
            values.put(CalendarContract.Events.DURATION, eventDuration);
            values.put(CalendarContract.Events.EVENT_COLOR, Color.rgb(35, 69, 35));
        }

        values.put(CalendarContract.Events.DTSTART, startDateTime.getTimeInMillis());
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

        return eventId;
    }

}
