package androiddev.jared.momatcollege;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button add_a_class_button = (Button) findViewById(R.id.add_a_class);
        add_a_class_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //DONE: Navigate to Add a class
                Intent intent = new Intent( MainActivity.this , AddClass.class );
                startActivity(intent);

            }
        });

        Button view_schedule_button = (Button) findViewById(R.id.view_schedule);
        view_schedule_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Intent intent = new Intent( MainActivity.this, AddCalendar.class );
                startActivity(intent);

            }
        });

        Button set_alarms_button = (Button) findViewById(R.id.set_alarms);
        set_alarms_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmManagerHelper.setAlarms(getApplicationContext());
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        lv = (ListView) findViewById(R.id.class_list);
        ArrayList<ClassListItem> classList = new ArrayList<ClassListItem>();

        //Query Database for class List
        if (!populateListView(classList)) {
            Toast.makeText(getApplicationContext(), "No classes have been added!", Toast.LENGTH_LONG).show();
        }

        ClassItemAdapter arrayAdapter = new ClassItemAdapter(this, classList);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TODO: Navigate to view class passing all pertinent information

                // Sends Class Information (right now just the title) to the ViewClass Activity
                Intent intent = new Intent(MainActivity.this, ViewClass.class);
                ClassListItem item = (ClassListItem) lv.getItemAtPosition(position);
                intent.putExtra("listItem", item.getClassName());
                intent.putExtra("classId", item.getClassId());

                startActivity(intent);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //TODO: Switch cases will add a class or view the schedule

        switch (item.getItemId()) {
            case R.id.add_a_class:
                addAClass();
                return true;
            case R.id.view_Schedule:
                schedule();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    public void addAClass(){
        Intent intent = new Intent(MainActivity.this, AddClass.class);
        startActivity(intent);
    }

    public void schedule(){
        //DONE: Intent to navigate to view the schedule
        Intent intent = new Intent(MainActivity.this, AddCalendar.class);
        startActivity(intent);
    }

    private boolean populateListView(ArrayList<ClassListItem> classList) {
        ClassDbHelper mHelper = new ClassDbHelper(getApplicationContext());
        SQLiteDatabase mDb = mHelper.getWritableDatabase();

        Cursor c = mDb.rawQuery(ClassDbHelper.CLASS_SELECT_ALL, null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[1]));
                String daysOfWeek = c.getString(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[5]));
                int classId = c.getInt(c.getColumnIndex(ClassDbHelper.CLASS_FIELDS[0]));
                daysOfWeek = formatDaysOfWeek(daysOfWeek);
                classList.add(new ClassListItem(name, daysOfWeek, classId));
                c.moveToNext();
            }
        } else {
            return false;
        }

        return true;
    }

    private String formatDaysOfWeek( String daysOfWeek ) {
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
}
