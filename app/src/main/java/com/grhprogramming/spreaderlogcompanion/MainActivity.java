package com.grhprogramming.spreaderlogcompanion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd;
    private Button btnSync;

    //Declarations for the list of jobs in the que
    ArrayList<DataModel> dataModels;
    ListView listView;
    private static CustomAdapter adapter;
    private TextView empty_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.main_add_button);
        btnSync = findViewById(R.id.main_sync_button);

        // Text for empty list
        empty_list = findViewById(R.id.list_empty);

        // For list of jobs in que
        listView = (ListView) findViewById(R.id.list);

        dataModels = new ArrayList<DataModel>();

        DbHandler db = new DbHandler(MainActivity.this);

        // If no jobs are in the database then display no jobs in que in the list area else list the jobs in the list
        if (db.getQueryCount() > 0) {
            // Hide list empty text
            empty_list.setVisibility(View.GONE);

            // Fetch all the jobs in the database
            dataModels = db.getAll();

            //dataModels.add(new DataModel("4/2/2023", "Brown Dale", "Home", "Calcitic Lime", "142"));
            //dataModels.add(new DataModel("4/2/2023", "Demick Mann", "Cow Pasture", "Fertilizer", "153"));

            adapter = new CustomAdapter(dataModels, getApplicationContext());

            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    DataModel dataModel = dataModels.get(position);
                    // Snackbar.make(view, "Job Id: " + dataModel.getId(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Delete Job!");
                    builder.setMessage("Are you sure you want to delete this job?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.DeleteJob(Integer.parseInt(dataModel.getId()));
                            refreshHome();
                        }
                    });
                    builder.setNegativeButton("No", null);
                    AlertDialog dialog= builder.create();
                    dialog.show();

                    return false;
                }
            });
        }
        else {
            // Display list is empty
            empty_list.setVisibility(View.VISIBLE);
        }


        // End List

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intAddJob = new Intent(view.getContext(), AddJob.class);
                startActivity(intAddJob);
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSyncJobs = new Intent(view.getContext(), SyncData.class);
                startActivity(intSyncJobs);
            }
        });
    }

    public void refreshHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}

// TODO: Change theme colors. Dialog box to delete job has red writing. That needs to be a grey color