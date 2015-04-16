package androiddev.jared.momatcollege;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewClass extends ActionBarActivity {

    private TextView mClassName;
    private TextView mTeacherName;
    private TextView mLocation;
    private TextView mfrequency;
    private TextView moffice_hours;
    private TextView mphone_number;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class);

        //TODO: Read from database and get values to plug into template

        //Get class selected from MainActivity
        Intent intent = getIntent();
        String text = intent.getStringExtra("listItem");

        //Displays all the information about the class selected
        mClassName = (TextView) findViewById(R.id.class_name);
        mClassName.setText(mClassName.getText() + text);

        mTeacherName = (TextView) findViewById(R.id.teacherName);
        mTeacherName.setText(mTeacherName.getText() + " Guanling Chen");

        mLocation = (TextView) findViewById(R.id.location);
        mLocation.setText(mLocation.getText() + " Olsen 402");

        mfrequency = (TextView) findViewById(R.id.frequency);
        mfrequency.setText(mfrequency.getText() + "Mon Wed 10-11");

        moffice_hours = (TextView) findViewById(R.id.office_hours);
        moffice_hours.setText(moffice_hours.getText() + " Fri 12:30-2");

        mphone_number = (TextView) findViewById(R.id.phone_number);
        mphone_number.setText(mphone_number.getText() + " (978) 934-1212");

        //select the list
        lv = (ListView) findViewById(R.id.class_info_list);

        //Populates Class with dummy assignments
        final List<String> class_list = new ArrayList<String>();
        class_list.add("Assignment 1");
        class_list.add("Assignment 2");
        class_list.add("Assignment 3");
        class_list.add("Assignment 4");
        class_list.add("Assignment 5");
        class_list.add("Exam 1");
        class_list.add("Assignment 6");
        class_list.add("Assignment 7");
        class_list.add("Assignment 8");
        class_list.add("Assignment 9");
        class_list.add("Assignment 10");
        class_list.add("Exam 2");


        //list adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, class_list);

        lv.setAdapter(arrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //TODO: When each menu item is clicked, navigate to that Homework or Exam info
                //this could also be a popup that gives necessary info

                AlertDialog.Builder alert = new AlertDialog.Builder(ViewClass.this);

                //Loops through array and saves every element into x
                String x = "";
                for( int i=0 ; i<class_list.size() ; i++ ){
                    x = x + class_list.get(i) + "\n";
                }

                // displays x (Just the list of Assignments) as the alert body
                alert.setMessage(x);

                // sets title to be the assignment selected
                String assignNo = ((TextView)view).getText().toString();
                alert.setTitle(assignNo);
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface inter, int arg){
                        //Doing nothing here just dismisses dialog
                    }
                });
                alert.setCancelable(true);
                alert.create().show();


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //TODO: Switch cases will add a new homework test
        switch (item.getItemId()) {
            case R.id.add_a_class:
                mainActivity();
                return true;
            case R.id.view_schedule:
                schedule();
                return true;
            case R.id.add_a_homework:
                homework();
                // yo yo yo!!!!!!
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void mainActivity(){
        Intent intent = new Intent(ViewClass.this, MainActivity.class);
        startActivity(intent);
    }

    public void schedule(){
        //TODO: Intent to navigate to view schedule

    }

    public void homework(){
        //TODO: Intent to navigate to add a homework

        Intent intent = new Intent(ViewClass.this, AddHomework.class);
        startActivity(intent);

    }

}
