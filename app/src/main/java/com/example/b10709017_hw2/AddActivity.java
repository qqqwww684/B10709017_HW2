package com.example.b10709017_hw2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;
    private Button newguestbutton;
    private Button cancelguestbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        mNewGuestNameEditText = (EditText) this.findViewById(R.id.person_name_edit_text);
        mNewPartySizeEditText = (EditText) this.findViewById(R.id.party_count_edit_text);
        newguestbutton=(Button)this.findViewById(R.id.add_guest_button);
        cancelguestbutton=(Button)this.findViewById(R.id.cancel_guest_button);
        cancelguestbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        newguestbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNewGuestNameEditText.getText().length()==0 || mNewPartySizeEditText.getText().length()==0){return;}
                Context context = AddActivity.this;
                Class destinationActivity = MainActivity.class;
                Intent startMainActivityIntent = new Intent(context, destinationActivity);
                startMainActivityIntent.putExtra("GuestName",mNewGuestNameEditText.getText().toString());
                startMainActivityIntent.putExtra("PartyCount",mNewPartySizeEditText.getText().toString());
                startActivity(startMainActivityIntent);
            }
        });


        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
