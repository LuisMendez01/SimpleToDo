package com.app.kaioc.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.kaioc.simpletodo.dialogFragments.CustomDialogFragment;
import com.app.kaioc.simpletodo.dialogFragments.CustomDialogFragment.CustomDialogListener;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CustomDialogListener {

    //a numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;

    //keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

//    // Construct the data source
//    ArrayList<Note> arrayOfNotes = new ArrayList<Note>();
//    // Create the adapter to convert the array to views
//    NotesAdapter notesAdapter;
//    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //read from file if it exists, if not then list will be empty
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems = findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
//        notesAdapter = new NotesAdapter(this, arrayOfNotes);
//        lvItems = findViewById(R.id.lvItems);
//        lvItems.setAdapter(notesAdapter);

        //mock data
//        arrayOfNotes.add( new Note("First", "item"));
//        arrayOfNotes.add( new Note("Second", "item"));
//        arrayOfNotes.add( new Note("Third", "item"));

        setupListViewListener();
        //showAlertDialog();//This is called when on click listener
    }

    //creates a new dialog fragment
    private void showEditDialog(String item, int pos) {
        FragmentManager fm = getSupportFragmentManager();
        CustomDialogFragment customDialogFragment = CustomDialogFragment.newInstance("Edit Item");

        // Supply num input as an argument.
        Bundle args = new Bundle();
        //get title from CustomDialogFragment in newInstance creation of the obj which is same one
        //I am passing above, so that in CustomDialogFragment in function onViewCreated() title
        //can be get and set there.
        args.putString("title", customDialogFragment.getArguments().getString("title"));
        args.putString("item", item);
        args.putInt("pos", pos);
        customDialogFragment.setArguments(args);

        customDialogFragment.show(fm, "dialog_fragment");//dialog_fragment.xml
    }

    //remember the alert dialog fragment does not need an .xml, it's built-in
//    private void showAlertDialog() {
//        FragmentManager fm = getSupportFragmentManager();
//        MyAlertDialogFragment alertDialog = MyAlertDialogFragment.newInstance("Some title");
//        alertDialog.show(fm, "fragment_alert");
//    }

    // 3. This method is invoked in the activity when the listener is triggered
    // Access the data result passed to the activity here
    @Override
    public void onFinishEditDialog(String inputText, int position) {
        //Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();String updatedItem = data.getExtras().getString(ITEM_TEXT);

        items.set(position, inputText);
        //notify the adapter that the model changed
        itemsAdapter.notifyDataSetChanged();
        //persist the changed model
        writeItems();
        //notify the user the operation completed ok
        Toast.makeText(this, "Item Updated Succesfully", Toast.LENGTH_SHORT).show();
    }


    public void onAddItem(View V){

        EditText etNewItem = findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);//instead of adding to items list and then call itemsAdapter on editing
        etNewItem.setText("");
        writeItems();//write the whole list with new added item
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener(){
        Log.i("MainActivity", "Listener to remove item is created");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list position: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();//writing to the file todo.txt all items in the list
                return true;
            }
        });

        //set up item listener for edit (regular clicks)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showEditDialog(items.get(position), position);//popup dialog fragment

//                //create the new activity
//                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
//                i.putExtra(ITEM_TEXT, items.get(position));
//                i.putExtra(ITEM_POSITION, position);
//
//                startActivityForResult(i, EDIT_REQUEST_CODE);
//                //pass the data being edited
//                //display the activity
            }
        });
    }

    //handle results from EditActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the edit activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            //extract updated item text from the result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //extract the original position of edited item
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model with the new item text at the edited position
            items.set(position, updatedItem);
            //notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();
            //persist the changed model
            writeItems();
            //notify the user the operation completed ok
            Toast.makeText(this, "Item Updated Succesfully", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        try {
            //FileUtils comes with the commons-io dependency we added
            //readLines reads line one by one and returns it as a list of strings
            //Charset for the encoding of the file
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Activity readItems", "error reading file!", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
            Log.d("Main Activity File Path", getDataFile().getAbsolutePath());
        } catch (IOException e) {
            Log.e("Main Activity writeItems()", "error reading file!", e);
        }
    }
}
