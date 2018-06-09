package krawczyk.krystian.fallingdetector;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import krawczyk.krystian.fallingdetector.service.DetectorService;

//Todo: move send sms code to service
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_contacts)
    Button contactsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupOnClickListener();
        requestPermission();
//        runService();
    }

    private void setupOnClickListener() {
        contactsButton.setOnClickListener((View view) -> {
            Intent intentToOpenDetails = new Intent(this, ContactActivity.class);
            startActivity(intentToOpenDetails);
        });
    }

    private void runService() {
        Intent intent = new Intent(this, DetectorService.class);
        startService(intent);
    }

    private void testSendSms() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(Integer.toString(731147978), null, "test", null, null);
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
        }
    }
}
