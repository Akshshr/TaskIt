package com.first.akashshrivastava.taskit;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


public class TaskListActivity extends ActionBarActivity {

    ListView taskListView;

    private static final String TAG = "TaskListActivity";

    private static final int EDIT_TASK_REQUEST = 10;

    private static final int CREATE_TASK_REQUEST = 20;

    private ArrayList<Task> mtasks;

    private  int mLastPositionClicked;



    //This is declared as the Adapter needs to access the change in task
    private TaskAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        mtasks = new ArrayList<Task>();

        mtasks.add(new Task());
        mtasks.get(0).setName("Task 1");
        mtasks.get(0).setDueDate(new Date());

        mtasks.add(new Task());
        mtasks.get(1).setName("Task 2");
        mtasks.get(1).setDone(true);

        mtasks.add(new Task());
        mtasks.get(2).setName("Task 3");
        mtasks.get(2).setDone(true);


        taskListView = (ListView)findViewById(R.id.Task_List);

        mAdapter = new TaskAdapter(mtasks);

        taskListView.setAdapter(mAdapter);
        //taskListView.setAdapter(new TaskAdapter(mtasks));

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //This lastPositionClicked is used when we come back from another activity to update the List view
                mLastPositionClicked = position;

                Intent i = new Intent(TaskListActivity.this, Task_Activity.class);
                Task task = (Task)parent.getAdapter().getItem(position);
                i.putExtra(Task_Activity.EXTRA, task);

                //This is used when an Activity is Re-started again, coming back in screens
                startActivityForResult(i, EDIT_TASK_REQUEST);

            }
        });

        //Makes sure the position is correct
        taskListView.getSelectedItemPosition();

        registerForContextMenu(taskListView);


    }

    //This brings back the intent from..Using get serializable
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //The IF CONDITIONS are not necessary, its only a measure when we have multiple screens calling on one screen..
        // So the number 10, 20 declared only are used to
        //check the int value and if it is 10 in this case..we get the data...THIS IS JUST A SAFETY MEASURE
        // here the two cases are accessed to control which screen the activity is being handed from

        if (requestCode == EDIT_TASK_REQUEST){
            if (resultCode == RESULT_OK){
                Task task = (Task)data.getSerializableExtra(Task_Activity.EXTRA);
                mtasks.set(mLastPositionClicked, task);
                mAdapter.notifyDataSetChanged();

            }
        }else if(requestCode == CREATE_TASK_REQUEST){
            if (resultCode == RESULT_OK){
                Task task = (Task)data.getSerializableExtra(Task_Activity.EXTRA);
                mtasks.add(task);
                mAdapter.notifyDataSetChanged();

            }
        }
    }

    private class TaskAdapter extends ArrayAdapter<Task>{

        TaskAdapter(ArrayList<Task> tasks) {
            super(TaskListActivity.this, R.layout.task_list_row, R.id.task_item_name,  tasks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView =  super.getView(position, convertView, parent);


            Task task = getItem(position);
            TextView taskName = (TextView)convertView.findViewById(R.id.task_item_name);
            taskName.setText(task.getName());


            CheckBox doneBox = (CheckBox)convertView.findViewById(R.id.check_item);
            doneBox.setChecked(task.isDone());

            return convertView;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_task) {
            Intent i = new Intent(TaskListActivity.this, Task_Activity.class);
            startActivityForResult(i, CREATE_TASK_REQUEST);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.menu_task_list_context, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //not sure why..too tired..something about ids..
        int id = item.getItemId();

        if (id == R.id.delete_task){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            mtasks.remove(menuInfo.position);
            mAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onContextItemSelected(item);
    }
}
