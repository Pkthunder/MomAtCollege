package androiddev.jared.momatcollege;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jared on 4/15/2015.
 */
public class ClassDbHelper extends SQLiteOpenHelper {
    public static final String[] VAL_NAMES= {"id", "class_name", "location", "teacher_name",
            "teacher_notes", "frequency", "start_date_time", "end_date_time", "class_type"};
    private static final int DB_VERSION = 1;
    public static final String DB_NAME = "MomAtCollegeDb";
    public static final String CLASS_TABLE_NAME = "classDb";
    /*private static final String CLASS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + CLASS_TABLE_NAME + " (" +
            "id int(6) NOT NULL auto_increment, " +
            "class_name text NOT NULL, " +
            "location text NOT NULL, " +
            "teacher_name text NOT NULL, " +
            "teacher_notes text NOT NULL, " +
            "frequency VARCHAR(15) NOT NULL, " +
            "start_date_time datetime NOT NULL, " +
            "end_date_time datetime NOT NULL, " +
            "class_type text NOT NULL, " +
            "PRIMARY KEY (id), " +
            "KEY id (id) );";*/

    private static final String CLASS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + CLASS_TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY, " +
            "class_name text NOT NULL, " +
            "location text NOT NULL, " +
            "teacher_name text NOT NULL, " +
            "teacher_notes text NOT NULL, " +
            "frequency VARCHAR(15) NOT NULL, " +
            "start_date_time datetime NOT NULL, " +
            "end_date_time datetime NOT NULL, " +
            "class_type text NOT NULL);";

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
