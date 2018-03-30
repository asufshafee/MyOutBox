package com.example.manzoorhussain.myoutbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class ChooseLeads extends AppCompatActivity {

    CheckBox checkBoxExcel,checkBoxContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_leads);

        checkBoxExcel=findViewById(R.id.cbExcelSheet);
        checkBoxContact=findViewById(R.id.cbContact);
        checkBoxExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseLeads.this,ContactFromExcel.class);
                startActivity(intent);
            }
        });
        checkBoxContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseLeads.this,ContactFromPhoneContacts.class);
                startActivity(intent);
            }
        });
    }
}
