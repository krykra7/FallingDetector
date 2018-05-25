package krawczyk.krystian.fallingdetector.contacts;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsAdapterViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public ContactsAdapter(@NonNull Context context) {
        this.mContext = context;
    }

    public void swapCursor(Cursor cursor) {
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public ContactsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ContactsAdapterViewHolder extends RecyclerView.ViewHolder {

        public ContactsAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
