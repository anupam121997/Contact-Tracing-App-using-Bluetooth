package com.example.projectinterface;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class upload_id extends Service {
    DB db;
    public upload_id() {
    }

    public void onCreate() {
        db = new DB(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Cursor res = db.getData();
        if(res.getCount()==0){
            Log.i("upload_idService","Database is Empty");
            //Toast.makeText(getApplicationContext(), "No Entry Exists", Toast.LENGTH_SHORT).show();
        }
        while(res.moveToNext()){
            DatabaseReference databaseReference= FirebaseDatabase.getInstance("https://coprotect-691e5-default-rtdb.firebaseio.com/").getReference("ID");
            databaseReference.child(res.getString(0)).setValue(res.getString(0));
            Log.i("upload_idService","ID Uploaded");
            Toast.makeText(getApplicationContext(), "Inserted into Firebase", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

}