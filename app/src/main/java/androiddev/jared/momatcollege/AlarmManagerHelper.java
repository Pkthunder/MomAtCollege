package androiddev.jared.momatcollege;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

public class AlarmManagerHelper extends BroadcastReceiver {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TIME_HOUR = "timeHour";
    public static final String TIME_MINUTE = "timeMinute";

    @Override
    public void onReceive(Context context, Intent intent) {
        setAlarms(context);
    }

    public static void setAlarms(Context context) {
        //cancelAlarms(context);

        ClassDbHelper dbHelper = new ClassDbHelper(context);

        List<AlarmModel> alarms =  dbHelper.getAlarms();

        for (AlarmModel alarm : alarms) {
            if (alarm.isEnabled == 1) {

                PendingIntent pIntent = createPendingIntent(context, alarm);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
                calendar.set(Calendar.MINUTE, alarm.timeMinute);
                calendar.set(Calendar.SECOND, 00);

                for( int i = 0; i < 7; i++){
                    if( alarm.repeatingDays.substring(i,i) == "1"){
                        if(i==0){
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==1){
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==2){
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==3){
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==4){
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==5){
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            setAlarm(context, calendar, pIntent);
                        }
                        if(i==6) {
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

    private static PendingIntent createPendingIntent(Context context, AlarmModel model) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(ID, model.id);
        intent.putExtra(NAME, model.name);
        intent.putExtra(TIME_HOUR, model.timeHour);
        intent.putExtra(TIME_MINUTE, model.timeMinute);

        return PendingIntent.getService(context, (int) model.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
