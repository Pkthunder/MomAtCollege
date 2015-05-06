package androiddev.jared.momatcollege;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.Calendar;

public class AlarmManagerHelper {

    public static String TAG = AlarmManagerHelper.class.getSimpleName();

    //set the next alarm
    public static void setAlarm(Context context, long alarmId) {

        Log.i(TAG, "alarm id = " + alarmId);

        //get alarm from DB
        ClassDbHelper dbHelper = new ClassDbHelper(context);
        AlarmModel alarm = dbHelper.getAlarm(alarmId);

        Calendar mCal = Calendar.getInstance();

        Calendar nextAlarm = Calendar.getInstance();
        nextAlarm.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
        nextAlarm.set(Calendar.MINUTE, alarm.timeMinute);
        nextAlarm.set(Calendar.DAY_OF_WEEK, alarm.day);

        //if alarm is for next week add a week in milliseconds to the date
        if( alarm.day < mCal.get(Calendar.DAY_OF_WEEK )){
            nextAlarm.setTimeInMillis(nextAlarm.getTimeInMillis() + (86400 * 7 * 1000) );
        }

        //create pending intent for alarm
        PendingIntent pIntent = createPendingIntent(context, alarm);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarm.getTimeInMillis(), pIntent);

    }


    //creates a pending intent for each alarm
    private static PendingIntent createPendingIntent(Context context, AlarmModel alarm) {
        Intent intent = new Intent(context, AlarmService.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", alarm.id);
        intent.putExtra("name", alarm.name);
        intent.putExtra("timeHour", alarm.timeHour);
        intent.putExtra("timeMinute", alarm.timeMinute);
        intent.putExtra("isEnabled", alarm.isEnabled);
        intent.putExtra("classId", alarm.classId);

        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    //finds the next alarm that needs to be set (next chronological alarm)
    public static long getNextAlarmId(Context context) {
        Calendar mCal = Calendar.getInstance();
        for ( int i=0; i<7; i++ ) {
            int dayVal = mCal.get(Calendar.DAY_OF_WEEK);
            ClassDbHelper mHelper = new ClassDbHelper(context);
            SQLiteDatabase mDb = mHelper.getReadableDatabase();
            Cursor c = mDb.rawQuery("SELECT * FROM " + ClassDbHelper.ALARM_TABLE_NAME +
                    " WHERE " + ClassDbHelper.ALARM_FIELDS[9] + " = " +
                    String.valueOf(dayVal) + "", null);


            if (!c.moveToFirst()) {

                mCal.set(Calendar.HOUR_OF_DAY, 0);
                mCal.set(Calendar.MINUTE, 0);
                mCal.setTimeInMillis( mCal.getTimeInMillis() + (24*60*60*1000));
                continue;
            }

            long timeDiff = 600000000;
            long currentClosestAlarmId = -1;

            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

                long compareVal = 0;
                long todayMillis = mCal.getTimeInMillis();
                Calendar alarmCal = Calendar.getInstance();
                alarmCal.setTimeInMillis(mCal.getTimeInMillis());
                alarmCal.set(Calendar.HOUR_OF_DAY, c.getInt(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[3])));
                alarmCal.set(Calendar.MINUTE, c.getInt(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[4])));
                compareVal = alarmCal.getTimeInMillis() - todayMillis;

                if (compareVal <= 0) {
                    continue;
                }
                if(timeDiff > compareVal) {
                    timeDiff = compareVal;
                    currentClosestAlarmId = c.getLong(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[0]));
                }

            }
            if (currentClosestAlarmId == -1) {
                mCal.set(Calendar.HOUR_OF_DAY, 0);
                mCal.set(Calendar.MINUTE, 0);
                mCal.setTimeInMillis( mCal.getTimeInMillis() + (24*60*60*1000));
                continue;
            }
            return currentClosestAlarmId;
        }
        return -1;
    }

    //convert day_of_week enum from the AddClass order to Calendar object order
    public static int convertDayToCal( int calendarDay ) {
        switch (calendarDay) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return calendarDay + 2;
            case 6:
                return 1;
        }
        return -1;
    }
}