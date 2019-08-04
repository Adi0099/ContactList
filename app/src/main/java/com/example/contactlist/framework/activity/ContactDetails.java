package com.example.contactlist.framework.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactlist.R;
import com.example.contactlist.framework.model.DataBaseModel;
import com.example.contactlist.framework.util.DataBaseHelper;
import com.example.contactlist.framework.util.StartActivity;

public class ContactDetails extends AppCompatActivity implements View.OnClickListener {

    EditText name,phone;
    Button update,delete;
    DataBaseHelper db;
    String cid;
    DataBaseModel dataBaseModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.number);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);


        Bundle extras = getIntent().getExtras();
        dataBaseModel = new DataBaseModel();
        if (extras != null) {
            cid = extras.getString("cid");
        }

        db = new DataBaseHelper(getApplicationContext());
        final DataBaseModel dataBaseModel = db.getContact(Long.parseLong(cid));
        name.setText(dataBaseModel.getName());
        name.setSelection(name.getText().length());
        phone.setText(dataBaseModel.getNumber());
        phone.setSelection(phone.getText().length());


        Log.d("details",cid);

        update.setOnClickListener(this);
        delete.setOnClickListener(this);

      }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ContactDetails.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update:
                db.updateContact(name.getText().toString(),phone.getText().toString(),cid);
                StartActivity.startSpecificActivity(this,MainActivity.class);
                Toast.makeText(ContactDetails.this, "Contact Updated", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete:
                db.delete(cid);
                StartActivity.startSpecificActivity(this,MainActivity.class);
                Toast.makeText(ContactDetails.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
