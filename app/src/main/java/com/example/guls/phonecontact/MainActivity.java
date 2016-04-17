package com.example.guls.phonecontact;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static com.example.guls.phonecontact.R.id.listView;


public class MainActivity extends AppCompatActivity {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    ListView list;
    ArrayList<Person> contacts;
    ListAdapter adapter;
    private static final int CONTACTS_PERMISSIONS_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();


        contacts = new ArrayList<Person>();
        list = (ListView) findViewById(listView);


        getContacts();



        //call person if click on it
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+contacts.get(position).getNumbers()));
                startActivity(intent);
            }
        });




    }
    //runtime permission
    private void requestPermissions() {
        String readPermission = Manifest.permission.READ_CONTACTS;
        String writePermission = Manifest.permission.WRITE_CONTACTS;
        String callPermission = Manifest.permission.CALL_PHONE;
        int hasReadPermission = 0;
        int hasWritePermission =0;
        int hasCallPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasReadPermission = checkSelfPermission(readPermission);
            hasWritePermission = checkSelfPermission(writePermission);
            hasCallPermission = checkSelfPermission(callPermission);
        }

        List<String> permissions = new ArrayList<String>();
        if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(readPermission);
        }
        if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(writePermission);
        }

        if (hasCallPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(callPermission);
        }
        if (!permissions.isEmpty()) {
            String[] params = permissions.toArray(new String[permissions.size()]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(params,CONTACTS_PERMISSIONS_REQUEST);

            }
        } else {
            // We already have permission, so handle as normal


        }
    }


    //get contacts from phone contact lists
    public void getContacts(){


        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        //String NUMBER2 = ContactsContract.CommonDataKinds.Phone.NUMBER;



        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                String phoneNumber = null;

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {


                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {

                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));


                        contacts.add(new Person(name, phoneNumber ,R.drawable.kisi));

                    }

                    phoneCursor.close();


                }

            }


        }
        cursor.close();

        //setting adapter
        adapter = new ListAdapter(this,R.layout.line_layout,contacts);
        if(list != null){
            list.setAdapter(adapter);

        }



    }



    //method for radio button, it is also filter number by vodafone,turkcell,turk telekom

    public void selection(View view) {

        Person person =null;
        String phonenumbers =null;
        ArrayList<Person> selected=new ArrayList<Person>();


        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                if (checked)
                    // if number is turk telekom
                    for (int i=0; i<contacts.size();i++) {
                        person = contacts.get(i);
                        phonenumbers = person.getNumbers();
                        String sub = phonenumbers.substring(1,3);

                        if(sub.contains("50")==true||sub.contains("55")==true ){
                            selected.add(new Person(person.getName(), phonenumbers ,R.drawable.kisi));
                        }

                    }
                adapter = new ListAdapter(this,R.layout.line_layout,selected);
                if(list != null){
                    list.setAdapter(adapter);

                }

                break;
            case R.id.radioButton2:
                if (checked)
                    // if number is turkcell
                    for (int i=0; i<contacts.size();i++) {
                        person = contacts.get(i);
                        phonenumbers = person.getNumbers();
                        String sub = phonenumbers.substring(1,3);

                        if(sub.contains("53")==true ){
                            selected.add(new Person(person.getName(), phonenumbers,R.drawable.kisi));
                        }

                    }
                adapter = new ListAdapter(this,R.layout.line_layout,selected);
                if(list != null){
                    list.setAdapter(adapter);

                }

                break;
            case R.id.radioButton3:
                if (checked)
                    //if number is vodafone
                    for (int i=0; i<contacts.size();i++) {
                        person = contacts.get(i);
                        phonenumbers = person.getNumbers();
                        String sub = phonenumbers.substring(1,3);

                        if(sub.contains("54")==true ){
                            selected.add(new Person(person.getName(), phonenumbers,R.drawable.kisi));
                        }

                    }
                adapter = new ListAdapter(this,R.layout.line_layout,selected);
                if(list != null){
                    list.setAdapter(adapter);

                }
                break;
            //list contacts without any filter
            case R.id.radioButton4:
                if (checked)

                    adapter = new ListAdapter(this, R.layout.line_layout,contacts);
                if(list != null){
                    list.setAdapter(adapter);

                }

                break;

        }


    }
    //with this method, contact list on the listview getting back up on records files
    public void record(ArrayList<Person>contacts){
        Person record =null;


        try {

            File f = new File(getFilesDir()+"/records.txt");

            if (!f.exists())
                f.createNewFile();


                FileOutputStream fOut = new FileOutputStream(f);
                PrintWriter writer = new PrintWriter(fOut);


            //display file saved message


            for (int i=0; i<contacts.size();i++) {
                record = contacts.get(i);

                writer.println(record.getName().toString()+",");
                writer.println(record.getNumbers().toString()+",");
                writer.println(String.valueOf(record.getPictureResourceID())+",");

            }
            writer.flush();

            writer.close();
            fOut.close();


            Toast.makeText(this, "The contents are saved in the file.", Toast.LENGTH_SHORT).show();

        }

        catch (Throwable t) {

            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();

        }


    }

    //to take back up calling record function
    public void backup(View view) {
        record(contacts);
    }

    //for recovery button,when press the button recovery is starting and update listview and phone contact
    public void recovery(View view) {
        File f = new File(getFilesDir()+"/records.txt");

        //checking file exists or not
        if (!f.exists()){
            Toast.makeText(this, "You should take back up!", Toast.LENGTH_SHORT).show();
        }
        else {

            readFile();
        }

    }
    public void readFile() {
        Person person = null;


        try {

                FileInputStream fileInputStream = openFileInput("records.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String temp = ""; //string nesnemiz
                String s = "";



                while ((temp = bufferedReader.readLine()) != null) { // read line by line from file
                    s= s+temp;

                }

                contacts.clear();

                ArrayList<String> aList = new ArrayList<String>(Arrays.asList(s.split(",")));
                for (int i = 0; i < aList.size(); i += 3) {
                    person = new Person(aList.get(i), aList.get(i + 1), Integer.parseInt(aList.get(i + 2)));
                        updateContacts(person);
                        contacts.add(person);

                    }

                Toast.makeText(this, "Update is sucessful!", Toast.LENGTH_SHORT).show();
               // getContacts();
            adapter = new ListAdapter(this, R.layout.line_layout,contacts);
            if(list != null){
                list.setAdapter(adapter);

            }



        } catch (IOException e) {
            e.printStackTrace();
            Log.v("ex", e.toString());
        }



    }

    //updating listview and phone contacts

    public void updateContacts(Person person){
        Cursor cur = managedQuery(ContactsContract.Data.CONTENT_URI, null, null, null, null);

        if ( (null == cur) || (!cur.moveToFirst()) )
            return;

        // For keeping the example simple we consider only the first contact.
        //   Normally you would iterate over all contacts
        String raw_contact_id =cur.getString(cur.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));

        String where = ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                String.valueOf(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME) + " = ?";
        String[] params = new String[]{raw_contact_id,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME};

        // For performance reasons, normally you would define a projection over the columns you need.
        Cursor check = getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, null);

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if((null == check) || (!check.moveToFirst())){
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, person.getName())
                    .build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, person.getNumbers())
                    .build());


        }

        else   {

            ops.add(ContentProviderOperation.newUpdate(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build());


            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(0)})
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, person.getName())
                    .build());
            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(0)})
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, person.getNumbers())
                    .build());



        }

            // Update
            try {
                getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);


            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }




