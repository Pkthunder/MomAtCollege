package androiddev.jared.momatcollege;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jared on 4/15/2015.
 * This class simply is a Class Db Table Help Object
 */
public class ClassDbHelper extends SQLiteOpenHelper {
    public static final String[] VAL_NAMES = {"id", "class_name", "location", "teacher_name",
            "teacher_notes", "frequency", "start_date_time", "end_date_time", "class_type"};
    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "MomAtCollegeDb";
    public static final String CLASS_TABLE_NAME = "classDb";

    private static final String CLASS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + CLASS_TABLE_NAME + " (" +
            VAL_NAMES[0] + " INTEGER PRIMARY KEY, " +
            VAL_NAMES[1] + " text NOT NULL, " +
            VAL_NAMES[2] + " text NOT NULL, " +
            VAL_NAMES[3] + " text NOT NULL, " +
            VAL_NAMES[4] + " text NOT NULL, " +
            VAL_NAMES[5] + " VARCHAR(15) NOT NULL, " +
            VAL_NAMES[6] + " datetime NOT NULL, " +
            VAL_NAMES[7] + " datetime NOT NULL, " +
            VAL_NAMES[8] + " text NOT NULL);";

    ClassDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CLASS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing!
    }

}
