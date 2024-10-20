package com.example.todo;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private ArrayList<Task> taskList;
    private TaskDatabaseHelper dbHelper;

    public TodoAdapter(ArrayList<Task> taskList, TaskDatabaseHelper dbHelper) {
        this.taskList = taskList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TodoViewHolder holder, final int position) {
        final Task task = taskList.get(position);

        // Set task description
        holder.textViewTask.setText(task.getDescription());

        // Strike-through text if task is completed
        if (task.isCompleted()) {
            holder.textViewTask.setPaintFlags(holder.textViewTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.textViewTask.setPaintFlags(holder.textViewTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // To avoid triggering the listener when RecyclerView binds the view
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);

        // Set the checkbox state
        holder.checkBoxCompleted.setChecked(task.isCompleted());

        // Handle task completion
        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the task in the database
            dbHelper.updateTask(task.getId(), isChecked ? 1 : 0);

            // Update the task object
            task.setCompleted(isChecked);

            // Strike-through text if task is marked as completed
            if (isChecked) {
                holder.textViewTask.setPaintFlags(holder.textViewTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.textViewTask.setPaintFlags(holder.textViewTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            // Notify the adapter that the item has changed
            notifyItemChanged(position);
        });

        // Handle task deletion on long press
        holder.itemView.setOnLongClickListener(v -> {
            // Delete task from the database
            dbHelper.deleteTask(task.getId());

            // Remove task from the list
            taskList.remove(position);

            // Notify RecyclerView about the item removal
            notifyItemRemoved(position);
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTask;
        CheckBox checkBoxCompleted;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
        }
    }
}


