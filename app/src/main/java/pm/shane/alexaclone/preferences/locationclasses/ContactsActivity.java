package pm.shane.alexaclone.preferences.locationclasses;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;


import java.util.ArrayList;

import pm.shane.alexaclone.DBHandler;
import pm.shane.alexaclone.R;
import pm.shane.alexaclone.preferences.SecurityPreferenceFragment;

public class ContactsActivity extends AppCompatActivity {

    private DBHandler db;
    public static final int RQS_PICK_CONTACT = 9;
    private static final String TAG = SecurityPreferenceFragment.class.getSimpleName();
    private static ArrayList<String> contactNames;
    private static ArrayAdapter<String> adapter;
    @Override
    protected void onStart(){
        super.onStart();
        if(db == null){
            db = new DBHandler(this);

        }


        contactNames = db.getContactNames();
        adapter.clear();
        adapter.addAll(contactNames);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        if(db == null){
            db = new DBHandler(this);

        }


        contactNames = db.getContactNames();




        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactNames);


        Button add = (Button) findViewById(R.id.addContactsButton);
        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, RQS_PICK_CONTACT);
            }
        });

        ListView view = (ListView) findViewById(R.id.contactsListView);

        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {


                String item = adapter.getItem(pos);

                if(db == null){
                    db = new DBHandler(getApplicationContext());

                }

                db.deleteContact(item);

                adapter.remove(item);









                return true;
            }
        });


        view.setAdapter(adapter);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RQS_PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                String number = "";
                String name = "";
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                cursor.moveToFirst();
                String hasPhone = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                if (hasPhone.equals("1")) {
                    Cursor phones = getContentResolver().query
                            (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        number = phones.getString(phones.getColumnIndex
                                (ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[-() ]", "");

                        name = phones.getString(phones.getColumnIndex((ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

                    }
                    phones.close();
                    //Do something with number
                    Log.e(TAG,"number is " + number + name);

                    if(db == null){
                        db = new DBHandler(getApplicationContext());

                    }

                    if(!contactNames.contains(name)){
                        db.addContacts(name,number);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "This contact has no phone number", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        }
    }

}
