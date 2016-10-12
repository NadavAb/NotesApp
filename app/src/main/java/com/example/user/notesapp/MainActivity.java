package com.example.user.notesapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.parse.Parse;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String CLIENT_KEY = "gA0NXcQjqR1IpzXuA9T5Tumz1raIXHG8NOECdx2e";
    private final String APP_ID = "5PtKv9Sn3b0auLerWa8omfcPfnGfDQHkFsbI6V1z";

    private GridView gridView;
    private ParseDataService dataService;

    private ArrayList<String> notes;
    private ArrayList<String> fileNames;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initParse();

        dataService = new ParseDataService();
        notes = new ArrayList<>();
        fileNames = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNoteTask().execute();
            }
        });

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(arrayAdapter);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String noteName = fileNames.get(position);

                new DeleteNoteTask().execute(noteName);
                new LoadNotesTask().execute();

                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
                editIntent.putExtra("FileName", fileNames.get(position));

                startActivity(editIntent);
            }
        });

        new LoadNotesTask().execute();
    }

    private void initParse()
    {
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APP_ID)
                .clientKey(CLIENT_KEY)
                .build());
    }

    @Override
    protected void onResume()
    {
        new LoadNotesTask().execute();

        super.onResume();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadNotesTask extends AsyncTask<Void, Void, String[]>
    {
        @Override
        protected String[] doInBackground(Void... params)
        {
            String[] fileNames = dataService.getNames();

            MainActivity.this.fileNames.clear();
            ArrayList<String> result = new ArrayList<>();

            for (int i = 0; i < fileNames.length; i++)
            {
                MainActivity.this.fileNames.add(fileNames[i]);

                String noteText = dataService.loadNote(fileNames[i]);
                result.add(noteText);
            }

            String[] arrayResult = new String[result.size()];
            result.toArray(arrayResult);

            return arrayResult;
        }

        @Override
        protected void onPostExecute(String[] noteTexts)
        {
            arrayAdapter.clear();

            for(int i = 0; i < noteTexts.length; i++)
                arrayAdapter.add(noteTexts[i]);
        }
    }

    class AddNoteTask extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            return dataService.createNote();
        }

        @Override
        protected void onPostExecute(String fileName)
        {
            Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
            editIntent.putExtra("FileName", fileName);

            startActivity(editIntent);
        }
    }

    class DeleteNoteTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params)
        {
            String fileName = params[0];
            dataService.deleteNote(fileName);

            return null;
        }
    }
}
