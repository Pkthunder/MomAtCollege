package androiddev.jared.momatcollege;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

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



        if(newAlarm.isEnabled == 1) {
            if(newAlarm.isAfterClass == 1) {
                //Vibrate Code
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);

                //TODO KYLE put the location check here
                //AlarmScreen.class should be addTask dialog
                Intent alarmIntent = new Intent(getBaseContext(), AlarmScreen.class);
                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                alarmIntent.putExtras(intent);
                getApplication().startActivity(alarmIntent);
            }
            else{
                Intent alarmIntent = new Intent(getBaseContext(), AlarmScreen.class);
                alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                alarmIntent.putExtras(intent);
                getApplication().startActivity(alarmIntent);
            }
        }
		
		return super.onStartCommand(intent, flags, startId);
	}
	
}