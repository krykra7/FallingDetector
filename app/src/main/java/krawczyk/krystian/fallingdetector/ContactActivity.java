package krawczyk.krystian.fallingdetector;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import krawczyk.krystian.fallingdetector.data.DetectorContract;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> getInsertContactDialog().show());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        Uri uri = getContentResolver().insert(DetectorContract.DetectorEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
