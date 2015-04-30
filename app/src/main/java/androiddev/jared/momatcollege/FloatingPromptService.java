package androiddev.jared.momatcollege;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by Jared on 4/29/2015.
 * This code is based of http://www.piwai.info/chatheads-basics/
 * The purpose of this code is to create activity independent dialog system
 */

public class FloatingPromptService extends Service {
    private static final String TAG = AddClass.class.getSimpleName();

    private boolean bActive = false;
    private WindowManager mWM;
    private TextView mTV;
    private LinearLayout outerMost;
    private LinearLayout buttonLayout;
    private LinearLayout innerLayout;
    private Button addTask;
    private Button cancel;
    private long mClassId;

    //LAYOUT EXPLANATION:
    //WindowManager -> outerMost -> innerLayout -> mTV && (buttonLayout -> 2xButtons)
    //mWM, innerLayout, and buttonLayout have corresponding params objects
    //outerMost was added to allow for both innerLayout and mWM params to co-exist
    //outerMost is also used to hack-ily add a border

    @Override
    public IBinder onBind(Intent intent) {
        //Not used
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Entered onCreate");

        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

        mTV = new TextView(this);
        mTV.setText("MomAtCollege: End of Class!");
        mTV.setBackgroundColor(Color.rgb(35, 69, 35));
        mTV.setTextColor(Color.parseColor("#00FF00"));
        mTV.setTextSize(22);
        mTV.setGravity(Gravity.CENTER);

        outerMost = new LinearLayout(this);
        outerMost.setOrientation(LinearLayout.VERTICAL);
        outerMost.setBackgroundColor(Color.parseColor("#00FF00"));
        outerMost.setPadding(8, 8, 8, 8);

        innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setBackgroundColor(Color.rgb(35, 69, 35));
        LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        innerLayout.setPadding(0, 0, 0, 15);

        buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        addTask = new Button(this);
        addTask.setText("Add Task");
        addTask.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams addTaskParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                .4f
        );

        cancel = new Button(this);
        cancel.setText("Cancel");
        cancel.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                .4f
        );

        buttonLayout.addView(cancel, cancelParams);
        buttonLayout.addView(addTask, addTaskParams);

        innerLayout.addView(mTV);
        innerLayout.addView(buttonLayout, buttonLayoutParams);

        outerMost.addView(innerLayout, innerParams);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.RGBA_8888
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        mWM.addView(outerMost, params);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG, "Entered onCreate");

        final long classId = intent.getLongExtra("classId", 0);
        Log.i(TAG, "classId: " + String.valueOf(classId));

        if (bActive || classId == 0) {
            Log.i(TAG, "Returned Early --- ERROR");
            this.stopSelf();
        }

        mClassId = classId;

        //Vibrate Code
        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 500, 150, 500, 150, 500};
        v.vibrate(pattern, -1);
        //Plays "text" tone
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(FloatingPromptService.this, AddTask.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.putExtra("classId", (int) classId);
                stopThisNonSense();
                startActivity(newIntent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopThisNonSense();
            }
        });

        bActive = true;

        return START_REDELIVER_INTENT;
        //return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (outerMost != null) {
            mWM.removeView(outerMost);
        }

        if ( mClassId != 0 ) {
            //Create Notification
            NotificationCompat.Builder builder =  new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("MomAtCollege: End of Class!")
                    .setContentText("Click here to add any homework")
                    .setAutoCancel(true);

            Intent intent = new Intent(this, AddTask.class);
            intent.putExtra("classId", (int) mClassId);

            PendingIntent pi = PendingIntent.getActivity(
                    this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            );

            builder.setContentIntent(pi);

            NotificationManager mNotMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotMgr.notify(2345, builder.build());
        }
    }

    public boolean isbActive() { return bActive; }

    final void stopThisNonSense() {
        bActive = false;
        this.stopSelf();
    }
}
