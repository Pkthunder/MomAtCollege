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


        for (AlarmModel alarm : alarms) {
            if (alarm.isEnabled == 1) {

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

                //TODO: Use a switch brah
                for( int i = 0; i < 7; i++){
                    if( alarm.repeatingDays.substring(i,i+1).equals("1")){
                        if(i==0){//MON
//                            Log.i(TAG, "MON");
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==1){//TUES
//                            Log.i(TAG, "TUES");
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==2){//WEDNES
//                            Log.i(TAG, "WED");
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==3){//THURS
//                            Log.i(TAG, "THURS");
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==4){//FRI
//                            Log.i(TAG, "FRI");
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==5){//SAT
//                            Log.i(TAG, "SAT");
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==6) {//SUN
//                            Log.i(TAG, "SUN");
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                    }

                }
            }
        }
    }

    @SuppressLint("NewApi")
    private static void setAlarm(Context context, Calendar calendar, PendingIntent pIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }
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
        //Log.i(TAG, "alarm id is equal: " + Long.toString(alarm.id));
        intent.putExtra(ID, alarm.id);
        intent.putExtra(NAME, alarm.name);
        intent.putExtra(TIME_HOUR, alarm.timeHour);
        intent.putExtra(TIME_MINUTE, alarm.timeMinute);
        intent.putExtra(IS_ENABLED, alarm.isEnabled);

        return PendingIntent.getService(context, (int) alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
