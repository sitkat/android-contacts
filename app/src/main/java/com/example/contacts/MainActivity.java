package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.provider.UserDictionary;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static boolean READ_CONTACTS_GRANTED = false;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int hasReadContactPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            READ_CONTACTS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }
        if (READ_CONTACTS_GRANTED) {
            getContacts();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                READ_CONTACTS_GRANTED = true;
            }
        }
        if (READ_CONTACTS_GRANTED) {
            getContacts();
        } else {
            Toast.makeText(this, "Требуется установить разрешения", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void getContacts() {
//        ContentResolver contentResolver = getContentResolver();
//        Cursor cursor = contentResolver.query(MediaStore.Downloads.INTERNAL_CONTENT_URI, null, null, null, null);
//        ArrayList<String> contacts = new ArrayList<String>();
//        try {
//            if(cursor!=null){
//                while (cursor.moveToNext()) {
//                    String contact = cursor.getString(cursor.getColumnIndex(MediaStore.Downloads.DOWNLOAD_URI)+1);
//                    contacts.add(contact);
//                }
//                cursor.close();
//            }
//        }
//        catch (Exception e)
//        {
//            if(cursor!=null){
//                while (cursor.moveToNext()) {
//                    @SuppressLint("Range") String contact = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
//                    contacts.add(contact);
//                }
//                cursor.close();
//            }
//        }
        String contact = Environment.getExternalStorageDirectory().toString()+"/Pictures";
        AssetManager mgr = getAssets();
        ArrayList<String> contacts = new ArrayList<String>();

        try {
            String list[] = mgr.list(contact);
            contacts.add(contact);

            if (list != null)
                for (int i=0; i<list.length; ++i)
                {
                    Log.e("FILE:", contact +"/"+ list[i]);
                }

        } catch (IOException e) {
            Log.v("List error:", "can't list" + contact);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contacts);

        ListView contactList = findViewById(R.id.listView);
        contactList.setAdapter(adapter);
    }
}