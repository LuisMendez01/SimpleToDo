package com.app.kaioc.simpletodo.customAdapterPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.kaioc.simpletodo.R;

import java.util.ArrayList;

public class NotesAdapter extends ArrayAdapter<Note> {

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);////change to whatever class u working on - Notes is my class
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {////item_notes is from item_notes.xml the file name I created
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_notes, parent, false);
        }
        // Lookup view for data population
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvHome = convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        tvName.setText(note.name);
        tvHome.setText(note.hometown);
        // Return the completed view to render on screen
        return convertView;
    }
}

