package androiddev.jared.momatcollege;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * This class simply is an custom extension of SQLiteOpenHelper
 * It contains functions and static strings to access our database
 */
public class ClassDbHelper extends SQLiteOpenHelper {

    //Tag for Log.i calls
    public static String TAG = ClassDbHelper.class.getSimpleName();

    //Statics to identify our database
    private static final int DB_VERSION = 6;
    public static final String DB_NAME = "MomAtCollegeDb";

    //////////////////////////////////////////////////////////////////  Class Table ///////////////////////////////////////////////////////////////////////
    public static final String CLASS_TABLE_NAME = "classDb";

    //List of all data fields inside table
    public static final String[] CLASS_FIELDS = {"id", "class_name", "location", "teacher_name", //0-3
            "teacher_notes", "frequency", "start_date_time", "end_date_time", "class_type", //4-8
            "auto_alarms_bool", "calEventId", "missClassCount", "leftEarlyCount", "longitude", "latitude"}; //9-14

    //Query to be run to create table
    private static final String CLASS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + CLASS_TABLE_NAME + " (" +
                    CLASS_FIELDS[0] + " INTEGER PRIMARY KEY, " +
                    CLASS_FIELDS[1] + " text NOT NULL, " +
                    CLASS_FIELDS[2] + " text NOT NULL, " +
                    CLASS_FIELDS[3] + " text NOT NULL, " +
                    CLASS_FIELDS[4] + " text NOT NULL, " +
                    CLASS_FIELDS[5] + " VARCHAR(15) NOT NULL, " +
                    CLASS_FIELDS[6] + " datetime NOT NULL, " +
                    CLASS_FIELDS[7] + " datetime NOT NULL, " +
                    CLASS_FIELDS[8] + " text NOT NULL, " +
                    CLASS_FIELDS[9] + " tinyint(1) NOT NULL, " +
                    CLASS_FIELDS[10] + " BIGINT NOT NULL, " +
                    CLASS_FIELDS[11] + " INTEGER NOT NULL, " +
                    CLASS_FIELDS[12] + " INTEGER NOT NULL, " +
                    CLASS_FIELDS[13] + " DOUBLE NOT NULL, " +
                    CLASS_FIELDS[14] + " DOUBLE NOT NULL );";

    //Static Strings designed to be used in Db.rawQuery() calls
    public static final String CLASS_SELECT_ALL = "SELECT * FROM " + CLASS_TABLE_NAME;
    public static String classSelectById( int classId ) {
        return "SELECT * FROM " + CLASS_TABLE_NAME + " WHERE id='" + classId + "'";
    }

    //Two helper functions for DateTime string formatting
    public static String formatDaysOfWeek( String daysOfWeek ) {
        String result = "";

        if (daysOfWeek.length() < 4) {
            return daysOfWeek;
        }

        for ( int i=0; i<daysOfWeek.length(); i++ ) {
            result += daysOfWeek.charAt(i);
            ++i;
            result += daysOfWeek.charAt(i);
            if ( i < daysOfWeek.length()-2) {
                result += ", ";
            }
        }
        return result;
    }
    public static String formatTime( String datetime ) {
        //returns HH:mm format
        return datetime.split(" ")[1].substring(0,5);
    }

    //////////////////////////////////////////////////////////////////  Task Table ///////////////////////////////////////////////////////////////////////
    public static final String TASK_TABLE_NAME = "taskDb";

    //List of all data fields inside table
    public static final String[] TASK_FIELDS = {"id", "classId", "task_type", "task_name",
            "task_notes", "due_date_time", "task_complete_bool", "calEventId"};

    //Query to be run to create table
    private static final String TASK_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TASK_TABLE_NAME + " (" +
                    TASK_FIELDS[0] + " INTEGER PRIMARY KEY, " +
                    TASK_FIELDS[1] + " int NOT NULL, " +
                    TASK_FIELDS[2] + " tinyint(4) NOT NULL, " +
                    TASK_FIELDS[3] + " text NOT NULL, " +
                    TASK_FIELDS[4] + " text NOT NULL, " +
                    TASK_FIELDS[5] + " datetime NOT NULL, " +
                    TASK_FIELDS[6] + " tinyint(1) NOT NULL, " +
                    TASK_FIELDS[7] + " BIGINT NOT NULL);";

    //Static Strings designed to be used in Db.rawQuery() calls
    public static final String TASK_SELECT_ALL = "SELECT * FROM " + TASK_TABLE_NAME;
    public static String taskSelectById( int classId ) {
        return "SELECT * FROM " + TASK_TABLE_NAME + " WHERE classId='" + classId + "'";
    }

    //////////////////////////////////////////////////////////////////  Alarm Table ///////////////////////////////////////////////////////////////////////

    public static final String ALARM_TABLE_NAME = "alarmDb";

    //List of all data fields inside table
    public static final String[] ALARM_FIELDS = {"id", "classId", "alarm_name", "alarm_time_hour", //0-3
            "alarm_time_minute", "alarm_repeat_days", "alarm_repeat_weekly", "alarm_enabled", "alarm_isAfterClass", "alarm_day"}; //4-9

    //Query to be run to create table
    private static final String ALARM_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ALARM_TABLE_NAME + " (" +
                    ALARM_FIELDS[0] + " INTEGER PRIMARY KEY," +
                    ALARM_FIELDS[1] + " int NOT NULL," +
                    ALARM_FIELDS[2] + " text NOT NULL," +
                    ALARM_FIELDS[3] + " int NOT NULL," +
                    ALARM_FIELDS[4] + " int NOT NULL," +
                    ALARM_FIELDS[5] + " text NOT NULL," +
                    ALARM_FIELDS[6] + " tinyint(1) NOT NULL," +
                    ALARM_FIELDS[7] + " tinyint(1) NOT NULL," +
                    ALARM_FIELDS[8] + " tinyint(1) NOT NULL," +
                    ALARM_FIELDS[9] + " int NOT NULL" +
                    " );";


    //Constructor required by SQLiteOpenHelper
    ClassDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Overwritten methods from SQLiteOpenHelper
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CLASS_TABLE_CREATE);
        db.execSQL(TASK_TABLE_CREATE);
        db.execSQL(ALARM_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE_NAME);
        onCreate(db);
    }

    //accesses alarm table and returns the alarm with a specific id
    public AlarmModel getAlarm(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + ALARM_TABLE_NAME + " WHERE " + ALARM_FIELDS[0] + " = " + id;

        Cursor c = db.rawQuery(select, null);

        if (c.moveToNext()) {
            return new AlarmModel(c);
        }

        Log.i(TAG, "ALARM ID RETURNED NOTHING");
        return null;
    }

}
