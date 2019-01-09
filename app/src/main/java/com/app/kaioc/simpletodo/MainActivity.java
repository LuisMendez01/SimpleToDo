package com.app.kaioc.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //read from file if it exists, if not then list will be empty
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems = findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        //mock data
//        items.add("First item");
//        items.add("XXX");
//        items.add("Loco");

        setupListViewListener();
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
                Log.i("MainActivity", "Item removed from list postion: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();//writing to the file todo.txt all items in the list
                return true;
            }
        });
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
        } catch (IOException e) {
            Log.e("Main Activity writeItems()", "error reading file!", e);
        }
    }
}
