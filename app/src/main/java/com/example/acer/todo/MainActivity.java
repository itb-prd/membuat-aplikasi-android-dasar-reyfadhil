package com.example.acer.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<String>();
        readItems();
        update();//Memanggil method update
    }

    void update ( ){
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();//Memanggil method setupListViewListener (Mengubah dan menghapus list)
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   final View item, final int pos, long id) {
                        AlertDialog.Builder theDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        theDialogBuilder.setTitle("OPTION");
                        theDialogBuilder.setMessage("Choose an action");

                        theDialogBuilder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder theDialogA = new AlertDialog.Builder(MainActivity.this);
                                theDialogA.setTitle("EDIT");
                                final EditText inputField = new EditText(MainActivity.this);
                                theDialogA.setView(inputField);

                                theDialogA.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String task = inputField.getText().toString();
                                        items.set(pos,task);
                                        writeItems();
                                        update();
                                    }
                                });
                                theDialogA.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        update();
                                    }
                                });

                                AlertDialog theDial = theDialogA.create();
                                theDial.show();
                            }
                        });
                        theDialogBuilder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(pos);
                                writeItems();
                                update();
                            }
                        });
                        AlertDialog theDialog = theDialogBuilder.create();
                        theDialog.show();
                        return true;
                    }
                }
        );

    }

    public void onAddItem(View v) { //Menuliskan input user
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void readItems() { //Memasukan input user ke dalam file
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() { //Mencetak input user ke monitor
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

