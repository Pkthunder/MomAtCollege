package androiddev.jared.momatcollege;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
            }
        }

        long nextAlarmId = AlarmManagerHelper.getNextAlarmId(getApplicationContext());
        AlarmManagerHelper.setAlarm(getApplicationContext(), nextAlarmId);

		return super.onStartCommand(intent, flags, startId);
	}
	
}