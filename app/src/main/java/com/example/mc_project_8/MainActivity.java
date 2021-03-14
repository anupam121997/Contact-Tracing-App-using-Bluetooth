package com.example.mc_project_8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button scan;
    Button save_data;
    Button uploadData;
    Button check;
    ListView lv;
    ArrayList<String> al=new ArrayList<String>();
    ArrayList<String> list_of_address=new ArrayList<String>();
    ArrayAdapter<String> aa;
    BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    TextView txt;
    String list_of_devices="";
    DatabaseHelper peopleDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan=(Button) findViewById(R.id.btn_1);
        lv=(ListView) findViewById(R.id.listview);
        txt=(TextView) findViewById(R.id.msg_box);
        save_data=(Button) findViewById(R.id.btn_2);
        uploadData=(Button) findViewById(R.id.btn_3);
        check=(Button) findViewById(R.id.btn_4);
        peopleDB = new DatabaseHelper(this);

        Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        i.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,20);
        startActivity(i);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        Log.i("Mac address",macAddress);

        scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //ActivityCompat.requestPermissions(,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
                bluetoothAdapter.startDiscovery();
            }
        });
        save_data.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                    Cursor res = peopleDB.showData();
                    if(res.getCount()==0){
                        Toast.makeText(MainActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    StringBuffer buffer = new StringBuffer();
                    while(res.moveToNext()){
                        buffer.append("User_id :"+res.getString(0)+"\n");
                        //buffer.append("Contact :"+res.getString(1)+"\n");
                        //buffer.append("Date of Birth :"+res.getString(2)+"\n\n");
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("User Entries");
                    builder.setMessage(buffer.toString());
                    builder.show();
                }
        });
        uploadData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Cursor res = peopleDB.showData();
                ArrayList<String> list_of_ids=new ArrayList<String>();
                DatabaseReference databaseReference= FirebaseDatabase.getInstance("https://mc-project-9-default-rtdb.firebaseio.com/").getReference().child("User_ids");
                //StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){

                    list_of_ids.add(res.getString(0));
                    String mac_add=res.getString(0);
                    String other="yes";
                    container c=new container(mac_add,other);
                    databaseReference.child(mac_add).setValue(c);

                }
                //DatabaseReference databaseReference= FirebaseDatabase.getInstance("https://mc-project-9-default-rtdb.firebaseio.com/").getReference().child("user");
                //databaseReference.push().setValue(list_of_ids);
                //databaseReference=databaseReference.child("List");
                //databaseReference.updateChildren(list_of_ids);
                //databaseReference.child("List").setValue(list_of_ids);
            }
        });
        check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance("https://mc-project-9-default-rtdb.firebaseio.com/").getReference().child("User_ids");
                Query query = reference
                        .orderByKey()
                        .equalTo("50:8F:4C:94:74:94");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            //username found
                            Toast.makeText(getApplicationContext(),"Mac address found ",Toast.LENGTH_SHORT).show();
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                        }else{
                            // username not found
                            Toast.makeText(getApplicationContext(),"Mac address not found ",Toast.LENGTH_SHORT).show();
                            System.out.println("--------------------------------------------------------------------");
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        IntentFilter myfilter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(myreceiver,myfilter);
        aa=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,al);
        lv.setAdapter(aa);

    }
    BroadcastReceiver myreceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context,"Device discovered",Toast.LENGTH_LONG).show();
                list_of_devices=list_of_devices+device.getAddress()+"\n";
                System.out.println(device.getAddress());
                list_of_address.add(device.getAddress());

                boolean insertData = peopleDB.addData(device.getAddress());
                if (insertData == true) {
                    Toast.makeText(MainActivity.this, "Data Successfully Inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
                }
                txt.setText(list_of_devices);
                al.add(device.getName());
                aa.notifyDataSetChanged();
            }
        }
    };

}