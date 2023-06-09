package com.example.finalproject.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.FirebaseOperator;
import com.example.finalproject.Dialog.LongClickTaskDialog;
import com.example.finalproject.NotificationUtils;
import com.example.finalproject.R;
import com.example.finalproject.DataClass.Task;
import com.example.finalproject.ReminderBroadcast;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    static ArrayList<Task> tasks;
    private ArrayList<String> keys;
    public static String ProjectKey;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView taskName,dateTime, description, fullTitle;
        private final CheckBox checkingStatus;
        private final ConstraintLayout layoutExpand;
        private final ConstraintLayout Fulllayout;
        private final TextView clockText;
        public ViewHolder(View view) {
            super(view);
            this.setIsRecyclable(false);
            // Define click listener for the ViewHolder's View
            taskName = (TextView) view.findViewById(R.id.txt_taskTitle);
            dateTime = (TextView) view.findViewById((R.id.txt_deadline));
            checkingStatus = (CheckBox) view.findViewById(R.id.chk_jobCheckingStatus);
            layoutExpand = (ConstraintLayout) view.findViewById(R.id.ly_expandLayout);
            description = (TextView) view.findViewById(R.id.txt_description);
            fullTitle = (TextView) view.findViewById(R.id.txt_fullTitle);
            Fulllayout = (ConstraintLayout) view.findViewById(R.id.ly_fullLayout);
            clockText = (TextView) view.findViewById(R.id.clockText);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(view.getContext()  , ProjectView.class);
//                    intent.putExtra("Project Title",textView.getText().toString());
//                    view.getContext().startActivity(intent);
                    if (layoutExpand.getVisibility() == View.GONE){
                        layoutExpand.setVisibility(View.VISIBLE);
                    }
                    else{
                        layoutExpand.setVisibility(View.GONE);
                    }
//                    if(Fulllayout.getHeight("dp")==80)

                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    LongClickTaskDialog epd = new LongClickTaskDialog(view.getContext(), ProjectKey, taskName.getTag().toString());
                    epd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    epd.show();
                    return true;
                }
            });
        }

        public ConstraintLayout getFulllayout() {
            return Fulllayout;
        }

        public TextView getFullTitle() {
            return fullTitle;
        }

        public CheckBox getCheckingStatus() {
            return checkingStatus;
        }

        public TextView getTaskName() {
            return taskName;
        }

        public TextView getDateTime() {
            return dateTime;
        }

        public ConstraintLayout getLayoutExpand() {
            return layoutExpand;
        }

        public TextView getDescription() {
            return description;
        }
        public TextView getClockText(){
            return clockText;
        }
    }


    public TaskAdapter(Context context, ArrayList<Task> dataSet, ArrayList<String> keys, String ProjectKey) {
        tasks = dataSet;
        this.keys = keys;
        this.ProjectKey = ProjectKey;
        this.context =context;
    }




    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_item_layout,
                        viewGroup,
                        false);

        return new ViewHolder(view);
    }

    public void sortingDatasetByCheckingStatus(){

    }

    public void sortingDatasetByPriority(){
//        for (int i = 0; i < tasks.size(); i++){
//            for (int j = 0; j < tasks.size() - i - 1; j++){
//                int a = tasks.get(j).getPriority();
//                int b = tasks.get(j+1).getPriority();
//                if(a < b){
//                    Task temp = tasks.get(j);
//                    tasks.set(j, tasks.get(j+1));
//                    tasks.set(j+1, temp);
//                }
//            }
//        }
//        for (int i = 0; i < tasks.size(); i++){
//            for (int j = 0; j < tasks.size() - i - 1; j++){
//                boolean a = tasks.get(j).isCheckingStatus();
//                boolean b = tasks.get(j+1).isCheckingStatus();
//                if (a == true && b == false){
//                    Task temp = tasks.get(j);
//                    tasks.set(j,tasks.get(j+1));
//                    tasks.set(j+1, temp);
//                }
//            }
//        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTaskName().setText(tasks.get(position).getTitle());
        viewHolder.getDateTime().setText(tasks.get(position).getEndTime());
        viewHolder.getDescription().setText(tasks.get(position).getDescription());
        viewHolder.getFullTitle().setText(tasks.get(position).getTitle());
        viewHolder.getClockText().setText(tasks.get(position).getOnTime());
        viewHolder.getCheckingStatus().setChecked(tasks.get(position).getCheckingStatus());
        if(tasks.get(position).getCheckingStatus()){
                viewHolder.getFulllayout().setAlpha((float)0.25);
        }
        else{
            viewHolder.getFulllayout().setAlpha((float)1);
        }
        String key = keys.get(position);
        viewHolder.getTaskName().setTag(key);
        if (tasks.get(position).getPriority() == 1){
            viewHolder.getCheckingStatus().setButtonDrawable(R.drawable.low_priority_cb);
        }
        else if (tasks.get(position).getPriority() == 2){
//            viewHolder.getPriorityText().setText( "!!");
//            viewHolder.getPriorityText().setTextColor(context.getResources().getColor(R.color.light_red));
            viewHolder.getCheckingStatus().setButtonDrawable(R.drawable.medium_priority_cb);
        }
        else{
//            viewHolder.getChec
//             viewHolder.getPriorityText().setText( "!!!");
//            viewHolder.getPriorityText().setTextColor(context.getResources().getColor(R.color.light_red));
            //viewHolder.getFulllayout().setBackgroundColor(Color.parseColor("#f04646"));
        }
        viewHolder.getCheckingStatus().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean checking = viewHolder.getCheckingStatus().isChecked();
                FirebaseOperator taskUpdate = new FirebaseOperator();
                taskUpdate.updateTasks(ProjectKey, key, checking);
            }
        });


    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}