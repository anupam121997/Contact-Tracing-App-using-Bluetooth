package com.example.projectinterface;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class Bluetooth extends Service {
    StringBuffer buffer = new StringBuffer();
    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

    DB db;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        bluetoothAdapter.startDiscovery();
        db = new DB(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        IntentFilter myfilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myreceiver,myfilter);

        final String CHANNEL_ID = "exampleServiceChannel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Example Service Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

        Intent inten = new Intent(Bluetooth.this, HomeScreen.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, inten, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("CoProtect")
                .setContentText("Stay Safe from COVID-19")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }

    BroadcastReceiver myreceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                //Toast.makeText(getApplicationContext(),"  RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();
                if(rssi >= -85) {
                    //Toast.makeText(context, "Device discovered", Toast.LENGTH_SHORT).show();
                    Log.i("BluetoothService", String.valueOf(device.getAddress()));
                    Log.i("BluetoothService", String.valueOf(rssi));
                    String data = String.valueOf(device.getAddress());

                    Boolean checkinsertdata = db.insertid(data.substring(0, data.length() - 5));
                    if (checkinsertdata)
                        Log.i("BluetoothService","New Entry Inserted");
                        //Toast.makeText(getApplicationContext(), "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    else
                        Log.i("BluetoothService","New Entry Not Inserted");
                    //Toast.makeText(getApplicationContext(), "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(myreceiver);
        this.stopSelf();
    }

}