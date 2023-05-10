package com.grhprogramming.spreaderlogcompanion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Declarations for the list of jobs in the que
    ArrayList<DataModel> dataModels;
    ListView listView;
    private CustomAdapter adapter;
    private TextView empty_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Action Bar color and title
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#800000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        getSupportActionBar().setTitle("Overview");

        // Text for empty list
        empty_list = findViewById(R.id.list_empty);

        // For list of jobs in que
        listView = findViewById(R.id.list);

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
    // Set the menu items on the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_add:
                Intent iAdd = new Intent(this, AddJob.class);
                startActivity(iAdd);
                return true;

            case R.id.nav_sync:
                Intent iSync = new Intent(this, SyncData.class);
                startActivity(iSync);
                return true;

            case R.id.nav_settings:
                Intent iSettings = new Intent(this, UserProfile.class);
                startActivity(iSettings);
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

}
