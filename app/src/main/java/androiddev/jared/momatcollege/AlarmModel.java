package androiddev.jared.momatcollege;

import android.database.Cursor;

public class AlarmModel {

	public long id = -2;
	public int timeHour;
	public int timeMinute;
	public String repeatingDays;
	public int repeatWeekly;
	public String name;
    public Long classId;
	public int isEnabled;
    public int isAfterClass;
    public int day;

    public AlarmModel(Cursor c){
        id = c.getLong(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[0]));
        classId = c.getLong(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[1]));
        name = c.getString(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[2]));
        timeHour = c.getInt(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[3]));
        timeMinute = c.getInt(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[4]));
        repeatingDays = c.getString(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[5]));
        repeatWeekly = c.getInt(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[6]));
        isEnabled = c.getInt(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[7]));
        isAfterClass = c.getInt(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[8]));
        day = c.getInt(c.getColumnIndex(ClassDbHelper.ALARM_FIELDS[9]));
    }

}
