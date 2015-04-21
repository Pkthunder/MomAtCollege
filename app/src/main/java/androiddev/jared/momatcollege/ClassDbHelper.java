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
    private static final int DB_VERSION = 2;
    public static final String DB_NAME = "MomAtCollegeDb";

    //////////////////////////////////////////////////////////////////  Class Table ///////////////////////////////////////////////////////////////////////
    public static final String CLASS_TABLE_NAME = "classDb";
    public static final String[] CLASS_FIELDS = {"id", "class_name", "location", "teacher_name",
            "teacher_notes", "frequency", "start_date_time", "end_date_time", "class_type",
            "auto_alarms_bool"};

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
                    CLASS_FIELDS[9] + " tinyint(1) NOT NULL);";

    public static final String CLASS_SELECT_ALL = "SELECT * FROM " + CLASS_TABLE_NAME;

    //////////////////////////////////////////////////////////////////  Task Table ///////////////////////////////////////////////////////////////////////
    public static final String TASK_TABLE_NAME = "taskDb";
    public static final String[] TASK_FIELDS = {"id", "classId", "task_type", "task_name",
            "task_notes", "due_date_time", "task_complete_bool"};

    private static final String TASK_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TASK_TABLE_NAME + " (" +
                    TASK_FIELDS[0] + " INTEGER PRIMARY KEY, " +
                    TASK_FIELDS[1] + " int NOT NULL, " +
                    TASK_FIELDS[2] + " tinyint(4) NOT NULL, " +
                    TASK_FIELDS[3] + " text NOT NULL, " +
                    TASK_FIELDS[4] + " text NOT NULL, " +
                    TASK_FIELDS[5] + " datetime NOT NULL, " +
                    TASK_FIELDS[6] + " tinyint(1) NOT NULL);";


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
                    " )";


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
//        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_ALARM);
        onCreate(db);
    }

//
//    private static final String SQL_DELETE_ALARM =
//            "DROP TABLE IF EXISTS " + Alarm.ALARM_TABLE_NAME;

//    public AlarmDBHelper(Context context) {
//        super(context, DB_NAME, null, DB_VERSION);
//    }

//
//    public AlarmModel populateModel(Cursor c) {
//        AlarmModel model = new AlarmModel();
//        model.id = c.getLong(c.getColumnIndex(Alarm._ID));
//        model.name = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_NAME));
//        model.timeHour = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_HOUR));
//        model.timeMinute = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE));
//        model.repeatWeekly = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY)) == 0 ? false : true;
//        model.alarmTone = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TONE)) != "" ? Uri.parse(c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TONE))) : null;
//        model.isEnabled = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_ENABLED)) == 0 ? false : true;
//
//        String[] repeatingDays = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS)).split(",");
//        for (int i = 0; i < repeatingDays.length; ++i) {
//            model.setRepeatingDay(i, repeatingDays[i].equals("false") ? false : true);
//        }
//
//        return model;
//    }
//
//    public ContentValues populateContent(AlarmModel model) {
//        ContentValues values = new ContentValues();
//        values.put(Alarm.COLUMN_NAME_ALARM_NAME, model.name);
//        values.put(Alarm.COLUMN_NAME_ALARM_TIME_HOUR, model.timeHour);
//        values.put(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE, model.timeMinute);
//        values.put(Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY, model.repeatWeekly);
//        values.put(Alarm.COLUMN_NAME_ALARM_TONE, model.alarmTone != null ? model.alarmTone.toString() : "");
//        values.put(Alarm.COLUMN_NAME_ALARM_ENABLED, model.isEnabled);
//
//        String repeatingDays = "";
//        for (int i = 0; i < 7; ++i) {
//            repeatingDays += model.getRepeatingDay(i) + ",";
//        }
//        values.put(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS, repeatingDays);
//
//        return values;
//    }
//
//
//    public long updateAlarm(AlarmModel model) {
//        ContentValues values = populateContent(model);
//        return getWritableDatabase().update(Alarm.ALARM_TABLE_NAME, values, Alarm._ID + " = ?", new String[]{String.valueOf(model.id)});
//    }
//
//    public AlarmModel getAlarm(long id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String select = "SELECT * FROM " + Alarm.ALARM_TABLE_NAME + " WHERE " + Alarm._ID + " = " + id;
//
//        Cursor c = db.rawQuery(select, null);
//
//        if (c.moveToNext()) {
//            return populateModel(c);
//        }
//
//        return null;
//    }
//
//    public List<AlarmModel> getAlarms() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String select = "SELECT * FROM " + Alarm.ALARM_TABLE_NAME;
//
//        Cursor c = db.rawQuery(select, null);
//
//        List<AlarmModel> alarmList = new ArrayList<AlarmModel>();
//
//        while (c.moveToNext()) {
//            alarmList.add(populateModel(c));
//        }
//
//        if (!alarmList.isEmpty()) {
//            return alarmList;
//        }
//
//        return null;
//    }
//
//    public int deleteAlarm(long id) {
//        return getWritableDatabase().delete(Alarm.ALARM_TABLE_NAME, Alarm._ID + " = ?", new String[]{String.valueOf(id)});
//    }

}
