package com.example.user.notesapp;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Random;

public class LocalDataService implements DataService
{
    private Context context;

    public LocalDataService(Context context)
    {
        this.context = context;
    }

    @Override
    public String createNote()
    {
        String name = "n";
        Random rnd = new Random();

        for (int i = 0; i < 16; i++)
        {
            char c = (char)(rnd.nextInt(42) + 48);
            name += c;
        }

        try
        {
            FileOutputStream outputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            outputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return name;
    }

    @Override
    public void editNote(String name, String text)
    {
        try
        {
            FileOutputStream fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);

            writer.write(text);
            writer.close();
            fileOutputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String loadNote(String name)
    {
        String text = "";

        try
        {
            FileInputStream fileInputStream = context.openFileInput(name);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            StringBuilder stringBuilder = new StringBuilder();

            while (line != null)
            {
                stringBuilder.append(line);
                stringBuilder.append("\n");

                line = reader.readLine();
            }

            text = stringBuilder.toString();

            reader.close();
            inputStreamReader.close();
            fileInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return text;
    }

    @Override
    public void deleteNote(String name)
    {
        context.deleteFile(name);
    }

    @Override
    public String[] getNames()
    {
        String[] files = context.fileList();
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < files.length; i++)
        {
            if(files[i].startsWith("n"))
                result.add(files[i]);
        }

        String[] arrayResult = new String[result.size()];
        result.toArray(arrayResult);

        return arrayResult;
    }
}
