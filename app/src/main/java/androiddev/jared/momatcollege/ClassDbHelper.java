package androiddev.jared.momatcollege;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    ClassDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CLASS_TABLE_CREATE);
        db.execSQL(TASK_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CLASS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);

        onCreate(db);
    }

}
