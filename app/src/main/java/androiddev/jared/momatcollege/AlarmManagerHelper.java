package androiddev.jared.momatcollege;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

public class AlarmManagerHelper extends BroadcastReceiver {

    public static String TAG = AlarmManagerHelper.class.getSimpleName();

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME_HOUR = "timeHour";
    public static final String TIME_MINUTE = "timeMinute";
    public static final String IS_ENABLED = "isEnabled";

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarms(context);
    }

    public static void setAlarms(Context context) {
        cancelAlarms(context);

        ClassDbHelper dbHelper = new ClassDbHelper(context);

        List<AlarmModel> alarms =  dbHelper.getAlarms();

//        Log.i(TAG, "JUST BEFORE WEEKLY CHECK");

        int numOfAlarms = 0;
        for (AlarmModel alarm : alarms) {
            if (alarm.isEnabled == 1) {
                numOfAlarms ++;
                Log.i(TAG, "numOfAlarms = " + numOfAlarms);


//                Log.i(TAG, "isEnabled = " + Integer.toString(alarm.isEnabled));

//                Log.i(TAG, alarm.name);
//                Log.i(TAG, "Hour = " + Integer.toString(alarm.timeHour));
//                Log.i(TAG, "Minute = " + Integer.toString(alarm.timeMinute));
//                Log.i(TAG, "Days = " + alarm.repeatingDays);
//                Log.i(TAG, "classID = " + Long.toString(alarm.classId));
//                Log.i(TAG, "sub = " + alarm.repeatingDays.substring(1,2));

                PendingIntent pIntent = createPendingIntent(context, alarm);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
                calendar.set(Calendar.MINUTE, alarm.timeMinute);
                calendar.set(Calendar.SECOND, 00);

                Log.i(TAG, "Current time is : " + calendar.get(Calendar.HOUR_OF_DAY));

                //TODO: Use a switch bruh

                for( int i = 0; i < 6; i++){
                    if( alarm.repeatingDays.substring(i,i+1).equals("1")){
                        switch(i){
                            case 0: //MON
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                                setAlarm(context, calendar, pIntent);
                                break;

                            case 1: //TUES
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                                setAlarm(context, calendar, pIntent);
                                break;

                            case 2: //WEDNES
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                                setAlarm(context, calendar, pIntent);
                                break;

                            case 3://THURS
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                                setAlarm(context, calendar, pIntent);
                                break;

                            case 4://FRI
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                                setAlarm(context, calendar, pIntent);
                                break;

                            case 5://SAT
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                                setAlarm(context, calendar, pIntent);
                                break;

                            case 6://SUN
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                                setAlarm(context, calendar, pIntent);
                                break;
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);

    }

    public static void cancelAlarms(Context context) {
        ClassDbHelper dbHelper = new ClassDbHelper(context);

        List<AlarmModel> alarms =  dbHelper.getAlarms();

        if (alarms != null) {
            for (AlarmModel alarm : alarms) {
                if (alarm.isEnabled == 1) {
                    PendingIntent pIntent = createPendingIntent(context, alarm);

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pIntent);
                }
            }
        }
    }

    private static PendingIntent createPendingIntent(Context context, AlarmModel alarm) {
        //Log.i(TAG, "PENDING INTENT");
        Intent intent = new Intent(context, AlarmService.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Log.i(TAG, "alarm id is equal: " + Long.toString(alarm.id));
        intent.putExtra("id", alarm.id);
        intent.putExtra(NAME, alarm.name);
        intent.putExtra(TIME_HOUR, alarm.timeHour);
        intent.putExtra(TIME_MINUTE, alarm.timeMinute);
        intent.putExtra(IS_ENABLED, alarm.isEnabled);
        intent.putExtra("classId", alarm.classId);

        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
