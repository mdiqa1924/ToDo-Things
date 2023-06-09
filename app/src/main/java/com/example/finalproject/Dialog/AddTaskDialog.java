package com.example.finalproject.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finalproject.DataClass.Task;
import com.example.finalproject.NotificationUtils;
import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class AddTaskDialog extends Dialog implements View.OnClickListener {
    public Activity c;
    public Dialog d;
    public Button yes, no;
    private String ProjectKey;
    public boolean isCategory = true;

    public AddTaskDialog(Activity a, String key) {
        super(a);
        this.c = a;
        ProjectKey = key;


    }

    public AddTaskDialog(Activity a) {
        super(a);
        this.c = a;
        ProjectKey = "QuickTasks";
        isCategory = false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_task_dialog);
        yes = (Button) findViewById(R.id.add_dialog_btn);
        no = (Button) findViewById(R.id.cancel_dialog_btn);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_dialog_btn:
                TextView title = findViewById(R.id.txt_taskTitleInput);
                if (title.getText().toString().isEmpty()){
                    title.setError("Please enter the title");
                    title.requestFocus();
                    Toast.makeText(this.getContext(), "Cannot Add without title", Toast.LENGTH_SHORT).show();
                }
                else{
                    addTask();
                    dismiss();
                    break;
                }
            case R.id.cancel_dialog_btn:
                dismiss();
                break;
            default:
                break;
        }
    }
    private int checkPriority(int id){
        RadioButton radioButton = findViewById(id);
        String text_priority = radioButton.getText().toString();
        switch (text_priority){
            case "high":
            case "cao":
                return 3;
            case "medium":
            case "vừa":
                return 2;
            case "low":
            case "thấp":
                return 1;
            default:
                return 0;
        }
    }
    private void addTask() {
        EditText title_field = findViewById(R.id.txt_taskTitleInput);
        DatePicker datePick = findViewById(R.id.datePicker);
        EditText txtDescription = findViewById(R.id.txt_descriptionEdit);
        String dateFormat = Integer.toString(datePick.getDayOfMonth()) + "-" + Integer.toString(datePick.getMonth() + 1) + "-" + Integer.toString(datePick.getYear());

        //time picker
        TimePicker timePicker = findViewById(R.id.timePicker);
        String timeString = Integer.toString(timePicker.getHour())+':'+ Integer.toString(timePicker.getMinute());


        String title = title_field.getText().toString();
        RadioGroup radGroup = findViewById(R.id.rad_priority);
        int priority_id = radGroup.getCheckedRadioButtonId();
        int priority_number = checkPriority(priority_id);
        String description = txtDescription.getText().toString();
        Task task = new Task(title,dateFormat,timeString,description, priority_number);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        String uid = "";
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }
        if(isCategory == true ) {
            DatabaseReference ref = rootRef.child("User").child(uid).child("projects").child(ProjectKey).child("tasks");
            ref.push().setValue(task);
        }
        else{
            DatabaseReference ref = rootRef.child("User").child(uid).child("QuickTasks");
            ref.push().setValue(task);
        }

        //make notification on time
        NotificationUtils nu = new NotificationUtils(c);
        Calendar onDate = Calendar.getInstance();
        onDate.set(Calendar.DAY_OF_MONTH, datePick.getDayOfMonth());
        onDate.set(Calendar.MONTH, datePick.getMonth());
        onDate.set(Calendar.YEAR, datePick.getYear());
        onDate.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        onDate.set(Calendar.MINUTE, timePicker.getMinute());

        //Toast.makeText(c,onDate.getTimeInMillis() +" vs"+Calendar.getInstance().getTimeInMillis(), Toast.LENGTH_SHORT).show();
        nu.setReminder(onDate.getTimeInMillis()-60000*10,
                title, description, ProjectKey);

    }
}
