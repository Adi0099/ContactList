package com.example.contactlist.framework.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.contactlist.R;
import com.example.contactlist.framework.model.DataBaseModel;
import com.example.contactlist.framework.adapter.Adapter;
import com.example.contactlist.framework.util.DataBaseHelper;
import com.example.contactlist.framework.util.ProgressDialogUtil;
import com.example.contactlist.framework.util.RecyclerTouchListener;
import com.example.contactlist.framework.util.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CONTACT =0 ;
    RecyclerView rvContacts;
    ProgressDialog progressDialog;
    List<DataBaseModel> contactVOList = new ArrayList();
    private DataBaseHelper db;
    String id,name,phoneNumber;
    Button sync;
    Adapter contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        sync = (Button)findViewById(R.id.syc);
        contactVOList.clear();
        db = new DataBaseHelper(this);
        contactVOList.addAll(db.getAllContact());
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForContactPermission();

            }
        });

        rvContacts.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvContacts, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                DataBaseModel dataBaseModel = new DataBaseModel();
                dataBaseModel = contactVOList.get(position);
                Log.d("click",dataBaseModel.getCid());
                Toast.makeText(MainActivity.this, dataBaseModel.getCid(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ContactDetails.class);
                intent.putExtra("cid",dataBaseModel.getCid());
                startActivity(intent);
                finish();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        contactAdapter = new Adapter(contactVOList, getApplicationContext());
        rvContacts.setLayoutManager(new WrapContentLinearLayoutManager(getApplicationContext()));
        rvContacts.setAdapter(contactAdapter);

        contactAdapter.notifyDataSetChanged();
    }

    private void getAllContacts() {

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

//                    contactVO = new Model();
//                    contactVO.setContactName(name);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        contactVO.setContactNumber(phoneNumber);
                    }

//                    DataBaseModel dataBaseModel  = new DataBaseModel();
//                    dataBaseModel.setCid(id);
//                    dataBaseModel.setName(name);
//                    dataBaseModel.setNumber(phoneNumber);
                    phoneCursor.close();

//                    contactVOList.add(dataBaseModel);
                }
                Log.d("test",".....");
                db.insert(id,name,phoneNumber);

            }

        }
    }

    public void askForContactPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                new LoadContacts().execute();// Execute the async task

            }
        }
        else{
            new LoadContacts().execute();// Execute the async task

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new LoadContacts().execute();// Execute the async task

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "No permission for contacts", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // Async task to load contacts
    private class LoadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

             getAllContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            contactVOList.addAll(db.getAllContact());
            contactAdapter.notifyDataSetChanged();
            // Hide dialog if showing
            progressDialog.cancel();
            Toast.makeText(MainActivity.this, "Contact List updated Successfully", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Show Dialog
            progressDialog = ProgressDialogUtil.showProgressDialog(MainActivity.this,  getResources().getString(R.string.wait));
            contactVOList.clear();

        }

    }

}