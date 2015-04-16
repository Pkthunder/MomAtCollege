package androiddev.jared.momatcollege;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button view_schedule_button = (Button) findViewById(R.id.add_a_class);
        view_schedule_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Navigate to Calendar to view the schedule
                Intent intent = new Intent( MainActivity.this , AddClass.class );
                startActivity(intent);

            }
        });

        lv = (ListView) findViewById(R.id.class_list);

        // Dummy data to populate Class List
        List<String> class_list = new ArrayList<String>();
        class_list.add("Probability and Statistics");
        class_list.add("Android Development");
        class_list.add("Introduction to Ethics I");
        class_list.add("Graphical User Interfacing II");
        class_list.add("Probability and Statistics");
        class_list.add("Android Development");
        class_list.add("Introduction to Ethics I");
        class_list.add("Graphical User Interfacing II");
        class_list.add("Probability and Statistics");
        class_list.add("Android Development");
        class_list.add("Introduction to Ethics I");
        class_list.add("Graphical User Interfacing II");
        class_list.add("Probability and Statistics");
        class_list.add("Android Development");
        class_list.add("Introduction to Ethics I");
        class_list.add("Graphical User Interfacing II");
        class_list.add("Probability and Statistics");
        class_list.add("Android Development");
        class_list.add("Introduction to Ethics I");
        class_list.add("Graphical User Interfacing II");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, class_list);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //TODO: Navigate to view class passing all pertinent information

                // Sends Class Information (right now just the title) to the ViewClass Activity
                Intent intent = new Intent(MainActivity.this, ViewClass.class);
                String selectedFromList = lv.getItemAtPosition(position).toString();
                intent.putExtra("listItem", selectedFromList);
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
        //TODO: Intent to navigate to view the schedule
        Intent intent = new Intent(MainActivity.this, AddHomework.class);
    }
}
