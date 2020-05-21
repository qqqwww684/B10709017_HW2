package com.example.b10709017_hw2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.b10709017_hw2.data.Contract;
import com.example.b10709017_hw2.data.DbHelper;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private GuestListAdapter mAdapter;
    private SQLiteDatabase mDb;
    private RecyclerView waitlistRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waitlistRecyclerView = (RecyclerView)findViewById(R.id.all_guests_list_view);
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DbHelper dbHelper = new DbHelper(this);
        mDb = dbHelper.getWritableDatabase();

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        loadColorFromPreferences(sharedPreferences);
//        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Cursor cursor = getAllGuests();
        mAdapter = new GuestListAdapter(this, cursor);
        waitlistRecyclerView.setAdapter(mAdapter);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("GuestName") && intentThatStartedThisActivity.hasExtra("PartyCount")) {
            String GuestName = intentThatStartedThisActivity.getStringExtra("GuestName");
            String PartyCount= intentThatStartedThisActivity.getStringExtra("PartyCount");
            int partycount=Integer.parseInt(PartyCount);
            addNewGuest(GuestName, partycount);
            mAdapter.swapCursor(getAllGuests());
        }


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("警告視窗")
                        .setMessage("是否確定要刪除?")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long id = (long) viewHolder.itemView.getTag();
                                removeGuest(id);
                                mAdapter.swapCursor(getAllGuests());
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                }).create().show();
            }
        }).attachToRecyclerView(waitlistRecyclerView);
    }

    private long addNewGuest(String name, int partySize) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(Contract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);
        return mDb.insert(Contract.WaitlistEntry.TABLE_NAME, null, cv);
    }




    private void loadColorFromPreferences(SharedPreferences sharedPreferences) {
       // waitlistRecyclerView.setBackgroundColor(Color.parseColor(sharedPreferences.getString(getString(R.string.pref_color_key),getString(R.string.pref_color_red_value))));
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_color_key))) {
            loadColorFromPreferences(sharedPreferences);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private Cursor getAllGuests() {
        return mDb.query(
                Contract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }
    private boolean removeGuest(long id) {
        return mDb.delete(Contract.WaitlistEntry.TABLE_NAME, Contract.WaitlistEntry._ID + "=" + id, null) > 0;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent startSettingsActivity = new Intent(this, SettingActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        else if(id == R.id.action_add){
            Intent startAddActivity = new Intent(this, AddActivity.class);
            startActivity(startAddActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
