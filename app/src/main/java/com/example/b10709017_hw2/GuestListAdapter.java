package com.example.b10709017_hw2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.b10709017_hw2.data.Contract;

public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    public SharedPreferences sharedPreferences;
    private Resources r;

    public GuestListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        r=context.getResources();
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }


    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;

        String name = mCursor.getString(mCursor.getColumnIndex(Contract.WaitlistEntry.COLUMN_GUEST_NAME));
        int partySize = mCursor.getInt(mCursor.getColumnIndex(Contract.WaitlistEntry.COLUMN_PARTY_SIZE));
        long id = mCursor.getLong(mCursor.getColumnIndex(Contract.WaitlistEntry._ID));
        holder.nameTextView.setText(name);

        GradientDrawable background=(GradientDrawable)holder.partySizeTextView.getBackground();
        background.setColor(Color.parseColor(sharedPreferences.getString(r.getString(R.string.pref_color_key),r.getString(R.string.pref_color_red_value))));

        holder.partySizeTextView.setText(String.valueOf(partySize));
        holder.itemView.setTag(id);
    }

    class GuestViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView partySizeTextView;

        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }

    }
}
