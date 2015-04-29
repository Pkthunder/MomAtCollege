//correct package name here

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import java.util.Calendar;

public class ProximityIntentReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        String s = "Gangstaswag";

        Log.d(s, "gangstaSwag");

        if (entering) {
            Log.d(getClass().getSimpleName(), "entering");
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Proximity Alert!")
                    .setContentText(String.format("You are entering your point of interest at %d:%d",
                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                            Calendar.getInstance().get(Calendar.MINUTE)))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_menu_notifications)
                            //.setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .build();

            notificationManager.notify(NOTIFICATION_ID, notification);

        }
        else {

            Log.d(getClass().getSimpleName(), "exiting");
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            Notification notification = new Notification.Builder(context)
                    .setContentTitle("Proximity Alert!")
                    .setContentText(String.format("You are exiting your point of interest at %d:%d",
                            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                            Calendar.getInstance().get(Calendar.MINUTE)))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_menu_notifications)
                            //.setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .build();

            notificationManager.notify(NOTIFICATION_ID, notification);
        }

    }
}
