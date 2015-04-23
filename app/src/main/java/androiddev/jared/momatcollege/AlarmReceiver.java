package androiddev.jared.momatcollege;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //setAlarms(context);
    }

//    public static void setAlarms(Context context){
//        //getAlarms();
//        //for( each alarm : alarms )
//
//        PendingIntent pi = createPendingIntent(context, alarm);
//    }
//
//    private static PendingIntent createPendingIntent(Context context, AlarmModel alarm){
//        Intent intent = new Intent(context, AlarmService.class);
//        intent.putExtra(ID, alarm.id);
//        intent.putExtra(NAME, alarm.name);
//        intent.putExtra(TIME_HOUR, alarm.timeHour);
//        intent.putExtra(TIME_MINUTE, alarm.timeMinute);
//        intent.putExtra(ALARM_INFO, alarm.alarmInfo);
//
//        return PendingIntent.getService(context, (int) alarm.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }


    public void setAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        long callTime =0;
        // intent.putExtra(array of datetimestrings);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, callTime, pendingIntent);

    }

    //TODO: Dalton
    //Cursor c is a 'pass by reference', pass a Cursor object you want to populate with the alarm data
    //returns false if the query returns an empty set
    private static boolean getAlarmsFromDatabase(Context context, Cursor c) {
        ClassDbHelper mHelper = new ClassDbHelper(context);
        SQLiteDatabase mDb = mHelper.getWritableDatabase();

        c = mDb.rawQuery(ClassDbHelper.ALARM_SELECT_ALL, null);

        return c.moveToFirst();
    }

}
