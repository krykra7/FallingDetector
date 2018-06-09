package krawczyk.krystian.fallingdetector;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import krawczyk.krystian.fallingdetector.contacts.ContactAdapter;
import krawczyk.krystian.fallingdetector.data.DetectorContract;

public class ContactActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        ContactAdapter.OnContactSelectionListener {

    public static final String ACTIVITY_PREFS = "preferences";
    public static final int NOT_SELECTED_INT = 0;

    private static final int CONTACT_LOADER_ID = 2013;
    private static final String TAG = ContactActivity.class.getSimpleName();

    @BindView(R.id.recyclerview_contacts)
    RecyclerView contactRecyclerView;

    private ContactAdapter contactAdapter;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> getInsertContactDialog().show());

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(this);
        contactRecyclerView.setAdapter(contactAdapter);

        attachSwipeToDelete();

        getSupportLoaderManager().initLoader(CONTACT_LOADER_ID, null, this);
    }

    private MaterialDialog getInsertContactDialog() {
        return new MaterialDialog.Builder(this)
                .title(R.string.contact_dialog_title)
                .customView(R.layout.activity_contact_dialog_form, true)
                .positiveText(R.string.contact_dialog_submit_text)
                .negativeText(R.string.contact_dialog_cancel_text)
                .onPositive(((dialog, which) -> insertNewContact(dialog.getView())))
                .build();
    }

    private void insertNewContact(View dialogView) {
        int number = Integer.valueOf(((EditText) dialogView.findViewById(R.id.et_number_field)).getText().toString());
        String name = ((EditText) dialogView.findViewById(R.id.et_name_field)).getText().toString();
        String surname = ((EditText) dialogView.findViewById(R.id.et_surname_field)).getText().toString();
        String message = ((EditText) dialogView.findViewById(R.id.et_message_field)).getText().toString();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DetectorContract.DetectorEntry.COLUMN_NUMBER, number);
        contentValues.put(DetectorContract.DetectorEntry.COLUMN_NAME, name);
        contentValues.put(DetectorContract.DetectorEntry.COLUMN_SURNAME, surname);
        contentValues.put(DetectorContract.DetectorEntry.COLUMN_MESSAGE, message);
        contentValues.put(DetectorContract.DetectorEntry.COLUMN_SELECTED, NOT_SELECTED_INT);

        Uri uri = getContentResolver().insert(DetectorContract.DetectorEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        }

        getSupportLoaderManager().restartLoader(CONTACT_LOADER_ID, null, this);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mContactData;

            @Override
            protected void onStartLoading() {
                if (mContactData != null) {
                    deliverResult(mContactData);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(DetectorContract.DetectorEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            DetectorContract.DetectorEntry.COLUMN_NAME);
                } catch (Exception ex) {
                    Log.e(TAG, "Failed to load data from database.");
                    ex.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable Cursor data) {
                mContactData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        contactAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        contactAdapter.swapCursor(null);
    }

    private void attachSwipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView
                    .ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (!((ContactAdapter.ContactsAdapterViewHolder) viewHolder).getSelected()) {
                    handleSwipe(viewHolder);
                }
            }
        }).attachToRecyclerView(contactRecyclerView);
    }

    private void handleSwipe(RecyclerView.ViewHolder viewHolder) {
        int contactId = (int) viewHolder.itemView.getTag();

        String stringId = Integer.toString(contactId);
        Uri uri = DetectorContract.DetectorEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        getContentResolver().delete(uri, null, null);

        getSupportLoaderManager().restartLoader(CONTACT_LOADER_ID, null, ContactActivity.this);
    }

    @Override
    public void onSelectionChanged(ContactAdapter.ContactsAdapterViewHolder viewHolder) {
        //todo solve multi selected contacts
        Context context = getApplicationContext();
        SharedPreferences.Editor editor = context.getSharedPreferences(ACTIVITY_PREFS, MODE_PRIVATE).edit();

        editor.putInt(DetectorContract.DetectorEntry.COLUMN_NUMBER, viewHolder.getNumber()).apply();

        int contactId = (int) viewHolder.itemView.getTag();
        Uri uri = DetectorContract.DetectorEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(contactId)).build();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DetectorContract.DetectorEntry.COLUMN_SELECTED, viewHolder.getSelected());

        getContentResolver().update(uri, contentValues, null, null);

        getSupportLoaderManager().restartLoader(CONTACT_LOADER_ID, null, ContactActivity.this);
    }
}
