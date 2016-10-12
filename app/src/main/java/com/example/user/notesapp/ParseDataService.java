package com.example.user.notesapp;

import android.content.Context;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ParseDataService implements DataService
{
    public ParseDataService()
    {
    }

    @Override
    public String createNote()
    {
        ParseObject noteObject = new ParseObject("Note");
        noteObject.add("text", "");
        noteObject.add("name", noteObject.getObjectId());
        noteObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e)
            {
                e.getMessage();
            }
        });

        return noteObject.getObjectId();
    }

    @Override
    public void editNote(String name, final String text)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
        query.getInBackground(name, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e)
            {
                object.add("text", text);
                object.saveInBackground();
            }
        });
    }

    @Override
    public String loadNote(String name)
    {
        final ArrayList<String> result = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
        query.getInBackground(name, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                String text = object.getString("text");
                result.add(text);
            }
        });

        return result.get(0);
    }

    @Override
    public void deleteNote(String name)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
        query.getInBackground(name, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                object.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e)
                    {
                    }
                });
            }
        });
    }

    @Override
    public String[] getNames()
    {
        final ArrayList<String> namesList = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Note");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects == null)
                    return;

                for (ParseObject obj : objects) {
                    String name = obj.getString("name");
                    namesList.add(name);
                }
            }
        });

        String[] namesArr = new String[namesList.size()];
        namesList.toArray(namesArr);

        return namesArr;
    }
}
