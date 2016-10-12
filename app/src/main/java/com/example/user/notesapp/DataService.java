package com.example.user.notesapp;

public interface DataService
{
    String createNote();
    void editNote(String name, String text);
    String loadNote(String name);
    void deleteNote(String name);
    String[] getNames();
}
