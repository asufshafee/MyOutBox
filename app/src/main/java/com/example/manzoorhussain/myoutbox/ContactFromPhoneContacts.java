package com.example.manzoorhussain.myoutbox;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manzoorhussain.myoutbox.Objects.User;
import com.example.manzoorhussain.myoutbox.Service.SmsService;
import com.example.manzoorhussain.myoutbox.Session.MyClass;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.util.ArrayList;
import java.util.List;

public class ContactFromPhoneContacts extends AppCompatActivity {

    List<User.Sheet1Object> LisObjects;
    EditText Message;
    Boolean Check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_from_phone_contacts);
        LisObjects = new ArrayList<>();
        Message = (EditText) findViewById(R.id.Message);



        findViewById(R.id.Send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Check) {
                    for (User.Sheet1Object sheet1Object : LisObjects) {
                        sheet1Object.setMesasege(Message.getText().toString());
                    }
                    MyClass myClass = (MyClass) getApplicationContext();
                    myClass.setSheet1Objects(LisObjects);

                    if (!isMyServiceRunning(SmsService.class)) {
                        Intent intent = new Intent(getApplicationContext(), SmsService.class);
                        startService(intent);

                        Toast.makeText(getApplicationContext(),
                                "SMS Sending Started..",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Already is Process ", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Import Contacts First", Toast.LENGTH_LONG).show();

                }


            }
        });




        findViewById(R.id.Import).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(getApplicationContext(), ContactPickerActivity.class)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                            .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
                    startActivityForResult(intent, 11);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getData(String contact, int which) {
        return contact.split(";")[which];
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 && resultCode == Activity.RESULT_OK &&
                data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {

            // we got a result from the contact picker

            // process contacts
            List<Contact> contacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            for (Contact contact : contacts) {
                User.Sheet1Object sheet1Object = new User.Sheet1Object();
                sheet1Object.setMobile_no(Long.parseLong(contact.getPhone(1).replaceAll("[^0-9]", "")));
                sheet1Object.setName(contact.getFirstName() + " " + contact.getLastName());
                LisObjects.add(sheet1Object);
                Check=true;
            }

        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
