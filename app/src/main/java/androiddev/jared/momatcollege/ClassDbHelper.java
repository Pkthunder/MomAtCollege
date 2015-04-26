package androiddev.jared.momatcollege;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jared on 4/15/2015.
 * This class simply is a Class Db Table Help Object
 */
public class ClassDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 4;
    public static final String DB_NAME = "MomAtCollegeDb";

    //////////////////////////////////////////////////////////////////  Class Table ///////////////////////////////////////////////////////////////////////
    public static final String CLASS_TABLE_NAME = "classDb";
    public static final String[] CLASS_FIELDS = {"id", "class_name", "location", "teacher_name",
            "teacher_notes", "frequency", "start_date_time", "end_date_time", "class_type",
            "auto_alarms_bool", "calEventId"};

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
                    CLASS_FIELDS[10] + " BIGINT NOT NULL);";

    public static final String CLASS_SELECT_ALL = "SELECT * FROM " + CLASS_TABLE_NAME;
    public static String classSelectById( int classId ) {
        return "SELECT * FROM " + CLASS_TABLE_NAME + " WHERE id='" + classId + "'";
    }

    public static String formatDaysOfWeek( String daysOfWeek ) {
        //String[] weekday_key = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
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
        //returns HH:mm
        return datetime.split(" ")[1].substring(0,5);
    }

    //////////////////////////////////////////////////////////////////  Task Table ///////////////////////////////////////////////////////////////////////
    public static final String TASK_TABLE_NAME = "taskDb";
    public static final String[] TASK_FIELDS = {"id", "classId", "task_type", "task_name",
            "task_notes", "due_date_time", "task_complete_bool", "calEventId"};

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

    public static final String TASK_SELECT_ALL = "SELECT * FROM " + TASK_TABLE_NAME;
    public static String taskSelectById( int classId ) {
        return "SELECT * FROM " + TASK_TABLE_NAME + " WHERE classId='" + classId + "'";
    }

    //////////////////////////////////////////////////////////////////  Alarm Table ///////////////////////////////////////////////////////////////////////

    public static final String ALARM_TABLE_NAME = "alarmDb";
    public static final String[] ALARM_FIELDS = {"id", "classId", "alarm_name", "alarm_time_hour",
            "alarm_time_minute", "alarm_repeat_days", "alarm_repeat_weekly", "alarm_enabled"};


    private static final String ALARM_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ALARM_TABLE_NAME + " (" +
                    ALARM_FIELDS[0] + " INTEGER PRIMARY KEY," +
                    ALARM_FIELDS[1] + " int NOT NULL," +
                    ALARM_FIELDS[2] + " text NOT NULL," +
                    ALARM_FIELDS[3] + " int NOT NULL," +
                    ALARM_FIELDS[4] + " int NOT NULL," +
                    ALARM_FIELDS[5] + " text NOT NULL," +
                    ALARM_FIELDS[6] + " tinyint(1) NOT NULL," +
                    ALARM_FIELDS[7] + " tinyint(1) NOT NULL" +
                    " );";

    public static final String ALARM_SELECT_ALL = "SELECT * FROM " + ALARM_TABLE_NAME;

    ClassDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

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

//
//    private static final String SQL_DELETE_ALARM =
//            "DROP TABLE IF EXISTS " + Alarm.ALARM_TABLE_NAME;

//    public AlarmDBHelper(Context context) {
//        super(context, DB_NAME, null, DB_VERSION);
//    }

//
    public AlarmModel populateModel(Cursor c) {
        AlarmModel model = new AlarmModel();
        model.id = c.getLong(c.getColumnIndex(ALARM_FIELDS[0]));
        model.classId = c.getLong(c.getColumnIndex(ALARM_FIELDS[1]));
        model.name = c.getString(c.getColumnIndex(ALARM_FIELDS[2]));
        model.timeHour = c.getInt(c.getColumnIndex(ALARM_FIELDS[3]));
        model.timeMinute = c.getInt(c.getColumnIndex(ALARM_FIELDS[4]));
        model.repeatingDays = c.getString(c.getColumnIndex(ALARM_FIELDS[5]));
        model.repeatWeekly = c.getInt(c.getColumnIndex(ALARM_FIELDS[6]));
        model.isEnabled = c.getInt(c.getColumnIndex(ALARM_FIELDS[7]));

        return model;
    }


//    public long updateAlarm(AlarmModel model) {
//        ContentValues values = populateContent(model);
//        return getWritableDatabase().update(Alarm.ALARM_TABLE_NAME, values, Alarm._ID + " = ?", new String[]{String.valueOf(model.id)});
//    }
//
    public AlarmModel getAlarm(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + ALARM_TABLE_NAME + " WHERE " + ALARM_FIELDS[0] + " = " + id;

        Cursor c = db.rawQuery(select, null);

        if (c.moveToNext()) {
            return populateModel(c);
        }

        return null;
    }

    public List<AlarmModel> getAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + ALARM_TABLE_NAME;

        Cursor c = db.rawQuery(select, null);

        List<AlarmModel> alarmList = new ArrayList<AlarmModel>();

        while (c.moveToNext()) {
            alarmList.add(populateModel(c));
        }

        if (!alarmList.isEmpty()) {
            return alarmList;
        }

        return null;
    }
//
//    public int deleteAlarm(long id) {
//        return getWritableDatabase().delete(Alarm.ALARM_TABLE_NAME, Alarm._ID + " = ?", new String[]{String.valueOf(id)});
//    }

}
