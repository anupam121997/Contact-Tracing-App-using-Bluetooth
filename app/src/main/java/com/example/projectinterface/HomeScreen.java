package com.example.projectinterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

    private Button upload,LogOut,info;
    private TextView status;
    String stringMac = "";
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        bluetoothAdapter.startDiscovery();
        Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,0);
        startActivity(i);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);

        try {
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface networkInterface:networkInterfaceList){
                if (networkInterface.getName().equalsIgnoreCase("wlan0")){
                    for (int j=0; j<networkInterface.getHardwareAddress().length;j++){
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[j] & 0xFF);

                        if (stringMacByte.length() == 1){
                            stringMacByte = "0" + stringMacByte;
                        }

                        stringMac = stringMac + stringMacByte.toUpperCase() + ":";
                    }
                    break;
                }
            }

            stringMac = stringMac.substring(0,stringMac.length()-6);
            Log.i("HomeScreen",stringMac);
            db = new DB(getApplicationContext());
            boolean checkinsertdata = db.insertid(stringMac);
            if (checkinsertdata)
                Log.i("HomeScreen","Self Id Inserted");
            else
                Log.i("HomeScreen","Self Id Not Inserted");

        } catch (SocketException e) {
            e.printStackTrace();
        }

        Intent startintent=new Intent(HomeScreen.this,Bluetooth.class);
        startService(startintent);

        status = findViewById(R.id.Status);

        info = findViewById(R.id.btn2);

        upload = findViewById(R.id.btn1);

        LogOut  = findViewById(R.id.bLogOut);

        LogOut.setOnClickListener((v) -> {
            Intent stopintent1 = new Intent(HomeScreen.this,Bluetooth.class);
            stopService(stopintent1);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LogIn.class));
            finish();
        });

        info.setOnClickListener((v) -> {

            startActivity(new Intent(HomeScreen.this, CovidUpdate.class));
//            Toast.makeText(HomeScreen.this,"Button pressed", Toast.LENGTH_SHORT).show();
        });

        upload.setOnClickListener((v) ->{
//            startService(startupload);
            startActivity(new Intent(HomeScreen.this, SelfAssessment.class));
/*            Toast.makeText(HomeScreen.this,"Button pressed", Toast.LENGTH_SHORT).show();
            Intent second = new Intent(HomeScreen.this, SelfAssessment.class);
            startActivityForResult(second,555);*/
        });

    }

    protected boolean checkConnection(){
        ConnectivityManager connectivitymanager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        return networkinfo != null;
    }

/*    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==555 && resultCode==RESULT_OK)
        {
            String precautions_result=data.getStringExtra("Results");
            if(precautions_result.equals("OK")) {
                status = findViewById(R.id.Status);
                status.setText("In Contact With COVID Positive!!");
                status.setBackgroundColor(Color.parseColor("#fc0303"));
            }
        }
    }
*/
    @Override
    protected void onStart() {
        super.onStart();

        if(checkConnection()){
            DatabaseReference reference = FirebaseDatabase.getInstance("https://coprotect-691e5-default-rtdb.firebaseio.com/").getReference("ID");
            Query query = reference.orderByKey().equalTo(stringMac);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        //username found
                        Toast.makeText(getApplicationContext(),"Mac address found ",Toast.LENGTH_SHORT).show();
                        status.setText("In Contact With COVID Positive!!");
                        status.setBackgroundColor(Color.parseColor("#fc0303"));
                        Log.i("HomeScreen","+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                    }else{
                        // username not found
                        Toast.makeText(getApplicationContext(),"Mac address not found ",Toast.LENGTH_SHORT).show();
                        Log.i("HomeScreen","----------------------------------------------------------------");
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i("HomeScreen","*********************************************");
                }
            });
        }
        else{
            Toast.makeText(this, "Internet is not Connected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stopintent = new Intent(this, upload_id.class);
        stopService(stopintent);
        Log.i("HomeScreen", "Upload Service Ended");
    }
}