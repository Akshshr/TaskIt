package com.first.akashshrivastava.taskit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


public class Task_Activity extends ActionBarActivity {

    public static final String EXTRA = "TaskExtra";
    private Calendar mCal;

    private Task mTask;
    private EditText mtaskNameInput;
    private Button mdateButton;
    private CheckBox mdoneBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_);

         mTask = (Task)getIntent().getSerializableExtra(EXTRA);

        //Creates new task..when its null, this is used for the add task feature in TaskListActivity
        if( mTask == null){
            mTask = new Task();

        }

        //Calendar objects are more dynamic and easier to manipulate than Date objects..
        mCal = Calendar.getInstance();

         mtaskNameInput = (EditText)findViewById(R.id.task_name);
         mdateButton = (Button)findViewById(R.id.task_date);
         mdoneBox = (CheckBox)findViewById(R.id.task_done);

        Button save_Button = (Button)findViewById(R.id.save_button);

        //This uses the task name and sets it on the EditText field..quite simple
        mtaskNameInput.setText(mTask.getName());

        //This is used cause you cant use. setText on a DueDate..So we pass a "no_date" null parameter..
        if (mTask.getDueDate() == null){

            //passes todays date as default..to avoid a crash
            mCal.setTime(new Date());

            mdateButton.setText(getResources().getString(R.string.no_date));
        }else{
            mCal.setTime(mTask.getDueDate());

            updateButton();
        }


        mdoneBox.setChecked(mTask.isDone());

        mdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //This declares a New date picker, and object...Its something, uses a Calender picker
                DatePickerDialog dpf = new DatePickerDialog(Task_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        //this needs to be done..not sure why..setting calendar month tp MONTH basically..
                        mCal.set(Calendar.YEAR, year);
                        mCal.set(Calendar.MONTH, monthOfYear);
                        mCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        updateButton();
                    }
                }, mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.get(Calendar.DAY_OF_MONTH));

                //to finally pop the calendar, the method Show() is called...
                dpf.show();

            }
        });

        save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setName(mtaskNameInput.getText().toString());
                mTask.setDone(mdoneBox.isChecked());
                mTask.setDueDate(mCal.getTime());

                //this sets up the intent to send the task object to another screen..
                Intent i = new Intent();
                i.putExtra(EXTRA, mTask);
                setResult(RESULT_OK, i);

                finish();

            }
        });


    }


    //this function basically updates the chosen date from dpf and then updates it on teh button
    public void updateButton(){
        DateFormat df = DateFormat.getDateInstance();
        mdateButton.setText(df.format(mCal.getTime()));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
