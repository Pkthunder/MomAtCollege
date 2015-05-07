package androiddev.jared.momatcollege;

/**
 * Created by Jared on 4/19/2015.
 * Class Item for Custom List Adapter
 */
//Not much to say about this class. It's simple a ListView that displays 2 strings
//instead of 1 like the default ListView
public class ClassListItem {

    private final String className;
    private final String daysOfWeek;
    private final int classId;
    private final String location;

    ClassListItem( String name, String days, int id, String location ) {
        super();
        this.className = name;
        this.daysOfWeek = days;
        this.classId = id;
        this.location = location;
    }

    public String getClassName() { return className; }
    public String getDaysOfWeek() { return daysOfWeek; }
    public int getClassId() { return classId; }
    public String getLocation() { return location; }

}
