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
    //(mWM params are added when outerMost is added to the mWM instance)
    //outerMost is also used to hack-ily add a border

    @Override
    public IBinder onBind(Intent intent) {
        //Not used
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Entered onCreate");

        //initialize WindowManager instance
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);

        //initialize TextView to be displayed
        mTV = new TextView(this);
        mTV.setText("MomAtCollege: End of Class!");
        mTV.setBackgroundColor(Color.rgb(35, 69, 35));
        mTV.setTextColor(Color.parseColor("#00FF00"));
        mTV.setTextSize(22);
        mTV.setGravity(Gravity.CENTER);

        //initialize outer most linear layout
        //used for a hacky border and
        //allows both inner and window manager params to both exist
        outerMost = new LinearLayout(this);
        outerMost.setOrientation(LinearLayout.VERTICAL);
        outerMost.setBackgroundColor(Color.parseColor("#00FF00"));
        outerMost.setPadding(8, 8, 8, 8);

        //initialize inner layout and layout params
        //contains both the TextView and the Button Layout
        innerLayout = new LinearLayout(this);
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setBackgroundColor(Color.rgb(35, 69, 35));
        LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        innerLayout.setPadding(0, 0, 0, 15);

        //initialize button layout and layout params
        //contains both cancel and add task buttons
        //contained within inner layout
        buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        //initializes add task button and layout params
        addTask = new Button(this);
        addTask.setText("Add Task");
        addTask.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams addTaskParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                .4f
        );

        //initializes add cancel and layout params
        cancel = new Button(this);
        cancel.setText("Cancel");
        cancel.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                .4f
        );

        //add the buttons to the button layout
        buttonLayout.addView(cancel, cancelParams);
        buttonLayout.addView(addTask, addTaskParams);

        //add the TextView and Button layout to the inner layout
        innerLayout.addView(mTV);
        innerLayout.addView(buttonLayout, buttonLayoutParams);

        //add the inner layout to the out layout
        //this is where the inner layout params are added
        outerMost.addView(innerLayout, innerParams);

        //create instance of WindowManager Params
        //then add params to the param object
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

        //add the outer layout to the window manager
        //WindowManager Params are added here
        mWM.addView(outerMost, params);

        //used to prevent duplicate FloatingPrompts
        bActive = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //retrieve the classId from the intent
        final long classId = intent.getLongExtra("classId", 0);

        //error checking - prevents display if
        //there is already an active FloatingPrompt
        //if the classId failed to be passed
        if (bActive || classId == 0) {
            Log.i(TAG, "Returned Early --- ERROR");
            this.stopSelf();
        }

        //stores classId within object
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

        //add task button click handler
        //navigates to the AddTask Activity
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

        //cancel button click handler
        //removes the FloatingPrompt from the screen
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopThisNonSense();
            }
        });

        //service will persist after app process is ended
        //system will re-start the service and re-send the initial
        //intent, so the classId can be retrieved
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //removes all current views from WindowManager instance
        if (outerMost != null) {
            buttonLayout.removeView(cancel);
            buttonLayout.removeView(addTask);
            innerLayout.removeView(buttonLayout);
            innerLayout.removeView(mTV);
            outerMost.removeView(innerLayout);
            mWM.removeView(outerMost);
        }

        //Create notification after user clicks on FloatingPrompt
        //both buttons end the service, therefore this will be called
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

    //ends the service
    final void stopThisNonSense() {
        bActive = false;
        this.stopSelf();
    }
}
