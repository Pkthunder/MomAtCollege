package androiddev.jared.momatcollege;

/**
 * Created by Jared on 4/19/2015.
 * Class Item for Custom List Adapter
 */
public class ClassListItem {

    private final String className;
    private final String daysOfWeek;
    private final int classId;

    ClassListItem( String name, String days, int id ) {
        super();
        this.className = name;
        this.daysOfWeek = days;
        this.classId = id;
    }

    public String getClassName() { return className; }
    public String getDaysOfWeek() { return daysOfWeek; }
    public int getClassId() { return classId; }

}
