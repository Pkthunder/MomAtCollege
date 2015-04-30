package androiddev.jared.momatcollege;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmService extends Service {

	public static String TAG = AlarmService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

        long ala_id = intent.getLongExtra("id", -1);
        Log.i(TAG, "alarm ID = " + ala_id);


        ClassDbHelper dbHelper = new ClassDbHelper(getApplicationContext());
        AlarmModel newAlarm = dbHelper.getAlarm(ala_id);

        Log.i(TAG, "isEnabled = " + newAlarm.isEnabled );
        Log.i(TAG, "isAfterClass = " + newAlarm.isAfterClass );


        if(newAlarm.isEnabled == 1) {
            if(newAlarm.isAfterClass == 1) {

                //TODO KYLE put the location check here
                //AlarmScreen.class should be addTask dialog
//                Intent alarmIntent = new Intent(getBaseContext(), AlarmScreen.class);
//                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                alarmIntent.putExtras(intent);
//                getApplication().startActivity(alarmIntent);
                long classId = intent.getLongExtra("classId", -1);
                if ( classId == -1 ) {
                    Log.i(TAG, "Internal Error, no classId: " + String.valueOf(classId));
                    Toast.makeText(getApplicationContext(), "Internal Error, no classId", Toast.LENGTH_LONG).show();
                    return super.onStartCommand(intent, flags, startId);
                }

                Intent floatingPrompt = new Intent(this, FloatingPromptService.class);
                floatingPrompt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                floatingPrompt.putExtra("classId",classId);
                getApplication().startService(floatingPrompt);
                return super.onStartCommand(intent, flags, startId);
            }
            else{
                Intent alarmIntent = new Intent(getBaseContext(), AlarmScreen.class);
                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                alarmIntent.putExtras(intent);
                getApplication().startActivity(alarmIntent);

                Context context = getApplicationContext();

                Calendar mCal = Calendar.getInstance();
                mCal.setTimeInMillis(mCal.getTimeInMillis() + (30 * 60 *1000) );

                PendingIntent pIntent = createPendingIntent(context, newAlarm.classId);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, mCal.getTimeInMillis(), pIntent);


            }
        }

        long nextAlarmId = AlarmManagerHelper.getNextAlarmId(getApplicationContext());
        AlarmManagerHelper.setAlarm(getApplicationContext(), nextAlarmId);

		return super.onStartCommand(intent, flags, startId);
	}

    private static PendingIntent createPendingIntent(Context context, long classID) {
        //Intent mIntent = new Intent(context, ProxAlertHelper.class);
        Intent mIntent = new Intent(context, AlarmService.class);

        ClassDbHelper mHelper = new ClassDbHelper(context);
        SQLiteDatabase mDb = mHelper.getWritableDatabase();

        Cursor c = mDb.rawQuery("SELECT * FROM " + ClassDbHelper.CLASS_TABLE_NAME +
                " WHERE " + ClassDbHelper.CLASS_FIELDS[0] + " = " +
                String.valueOf(classID) + "", null);

        if (!c.moveToFirst()) {
            mIntent.putExtra("classId", c.getLong(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[0])));
            mIntent.putExtra("latitude", c.getDouble(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[13])));
            mIntent.putExtra("longitude", c.getDouble(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[14])));
        }

        return PendingIntent.getService(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
	
}