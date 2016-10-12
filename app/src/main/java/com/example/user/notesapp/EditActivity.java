package com.example.user.notesapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    private ParseDataService dataService;
    private String fileName;

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.fileName = getIntent().getExtras().getString("FileName");
        this.dataService = new ParseDataService();

        this.editText = (EditText)findViewById(R.id.editText);
        this.editText.setText(dataService.loadNote(fileName));
    }

    @Override
    public void onBackPressed()
    {
        new SaveNoteTask().execute(editText.getText().toString());

        super.onBackPressed();
    }

    class SaveNoteTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params)
        {
            String text = params[0];

            dataService.editNote(fileName, text);

            return null;
        }
    }
}
