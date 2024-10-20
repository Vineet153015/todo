package com.example.todo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonAdd;
    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private ArrayList<Task> taskList;
    private TaskDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        recyclerView = findViewById(R.id.recyclerView);

        dbHelper = new TaskDatabaseHelper(this);
        taskList = dbHelper.getAllTasks();

        // Initialize RecyclerView and Adapter
        todoAdapter = new TodoAdapter(taskList, dbHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(todoAdapter);

        // Add button click listener
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editTextTask.getText().toString();
                if (!TextUtils.isEmpty(task)) {
                    dbHelper.addTask(task);
                    taskList.clear();
                    taskList.addAll(dbHelper.getAllTasks());
                    todoAdapter.notifyDataSetChanged();
                    editTextTask.setText("");  // Clear input field
                }
            }
        });
    }
}
