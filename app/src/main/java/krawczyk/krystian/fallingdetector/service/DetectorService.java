package krawczyk.krystian.fallingdetector.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.Objects;

import krawczyk.krystian.fallingdetector.MainActivity;

//Todo: delete test code, add better algorithm and send sms to contact from list

public class DetectorService extends Service implements SensorEventListener {

    private final static int AXIS_X_INDEX = 0;
    private final static int AXIS_Y_INDEX = 1;
    private final static int AXIS_Z_INDEX = 2;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private boolean isFreeFallStarted = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = Objects.requireNonNull(sensorManager).getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI, new Handler());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[AXIS_X_INDEX];
        float y = event.values[AXIS_Y_INDEX];
        float z = event.values[AXIS_Z_INDEX];

        lastAcceleration = currentAcceleration;
        currentAcceleration = (float) Math.sqrt((double) (x * x + y * y + z * z));

//        acceleration = currentAcceleration - lastAcceleration;

        if (currentAcceleration <= 3) {
            isFreeFallStarted = true;
        }

        if (isFreeFallStarted) {
            if (currentAcceleration >= 20) {
                showNotification();
                isFreeFallStarted = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void showNotification() {
        final NotificationManager notificationManager = (NotificationManager) this.getSystemService
                (NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Test accelerometer")
                .setTicker("test nie wiem co to")
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(101, notification);
    }
}
