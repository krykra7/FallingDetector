package krawczyk.krystian.fallingdetector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_contacts)
    Button contactsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupOnClickListener();
    }

    private void setupOnClickListener() {
        contactsButton.setOnClickListener((View view) -> {
            Intent intentToOpenDetails = new Intent(this, ContactActivity.class);
            startActivity(intentToOpenDetails);
        });
    }
}
