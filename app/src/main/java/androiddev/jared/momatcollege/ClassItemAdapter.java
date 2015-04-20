package androiddev.jared.momatcollege;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jared on 4/19/2015.
 * Custom Adapter to display multiple items in our list view
 */
public class ClassItemAdapter extends ArrayAdapter<ClassListItem> {
    private final Context context;
    private final ArrayList<ClassListItem> classArrayList;

    public ClassItemAdapter(Context context, ArrayList<ClassListItem> classArrayList) {
        super(context, R.layout.class_list, classArrayList);

        this.context = context;
        this.classArrayList = classArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View classList = inflater.inflate(R.layout.class_list, parent, false);

        TextView classNameView = (TextView) classList.findViewById(R.id.listViewClassName);
        TextView classDaysView = (TextView) classList.findViewById(R.id.listViewDaysOfWeek);

        classNameView.setText(classArrayList.get(position).getClassName());
        classDaysView.setText(classArrayList.get(position).getDaysOfWeek());

        return classList;
    }
}
