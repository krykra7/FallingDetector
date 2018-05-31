package krawczyk.krystian.fallingdetector.contacts;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import krawczyk.krystian.fallingdetector.R;
import krawczyk.krystian.fallingdetector.data.DetectorContract;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactsAdapterViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public ContactAdapter(@NonNull Context context) {
        this.mContext = context;
    }

    public void swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return;
        }

        if (cursor != null) {
            this.mCursor = cursor;
            this.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ContactsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.contact_list_item, parent, false);

        return new ContactsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapterViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(DetectorContract.DetectorEntry._ID);
        int numberIndex = mCursor.getColumnIndex(DetectorContract.DetectorEntry.COLUMN_NUMBER);
        int nameIndex = mCursor.getColumnIndex(DetectorContract.DetectorEntry.COLUMN_NAME);
        int surnameIndex = mCursor.getColumnIndex(DetectorContract.DetectorEntry.COLUMN_SURNAME);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        int number = mCursor.getInt(numberIndex);
        String name = mCursor.getString(nameIndex);
        String surname = mCursor.getString(surnameIndex);

        holder.itemView.setTag(id);
        holder.contactNameTv.setText(name);
        holder.contactSurnameTv.setText(surname);
        holder.contactNumberTv.setText(String.valueOf(number));
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    class ContactsAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_contact_name)
        TextView contactNameTv;
        @BindView(R.id.tv_contact_surname)
        TextView contactSurnameTv;
        @BindView(R.id.tv_contact_number)
        TextView contactNumberTv;

        ContactsAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
