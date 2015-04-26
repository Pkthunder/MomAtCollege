package androiddev.jared.momatcollege;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

	public static String TAG = AlarmService.class.getSimpleName();
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {


        ClassDbHelper dbHelper = new ClassDbHelper(getApplicationContext());
        AlarmModel newAlarm = dbHelper.getAlarm(startId);


        if(newAlarm.isEnabled == 1) {
            //TODO KYLE LOCATION STUFF
            //location 'if' should go here

            Intent alarmIntent = new Intent(getBaseContext(), AlarmScreen.class);
            alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            alarmIntent.putExtras(intent);
            getApplication().startActivity(alarmIntent);
        }
		
		return super.onStartCommand(intent, flags, startId);
	}
	
}