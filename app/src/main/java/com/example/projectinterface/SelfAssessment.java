package com.example.projectinterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

public class SelfAssessment extends AppCompatActivity {
    AlertDialog.Builder builder;
    Button submit;
    CheckBox cough,fever,breathing, loss_of_sense,none_of_these_1,none_of_these_2,none_of_these_3,diabetes,hypertension,lung,heart,kidney,inst_1,inst_2;
    RadioButton radioButton1,radioButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_assessment);

        submit=findViewById(R.id.button);
        builder=new AlertDialog.Builder(SelfAssessment.this);
        cough=findViewById(R.id.checkBox);
        fever=findViewById(R.id.checkBox2);
        breathing=findViewById(R.id.checkBox3);
        loss_of_sense=findViewById(R.id.checkBox4);
        none_of_these_1=findViewById(R.id.checkBox5);
        none_of_these_2=findViewById(R.id.checkBox12);
        none_of_these_3=findViewById(R.id.checkBox17);
        diabetes=findViewById(R.id.checkBox9);
        hypertension=findViewById(R.id.checkBox8);
        lung=findViewById(R.id.checkBox11);
        heart=findViewById(R.id.checkBox6);
        kidney=findViewById(R.id.checkBox10);
        inst_1=findViewById(R.id.checkBox15);
        inst_2=findViewById(R.id.checkBox16);

        radioButton1=findViewById(R.id.radia_id);
        radioButton2=findViewById(R.id.radia_id2);


//        if(weight>=10)
//        {
//            startService(startupload);
//        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weight=0;
                if(cough.isChecked())
                {
                    weight += 1;
                }
                if(fever.isChecked())
                {
                    weight += 1;
                }
                if(breathing.isChecked())
                {
                    weight += 2;
                }
                if(loss_of_sense.isChecked())
                {
                    weight += 3;
                }
                if(none_of_these_1.isChecked())
                {
                    weight += 0;
                }
                if(none_of_these_2.isChecked())
                {
                    weight += 0;
                }
                if(none_of_these_3.isChecked())
                {
                    weight += 0;
                }
                if(diabetes.isChecked())
                {
                    weight += 2;
                }
                if(hypertension.isChecked())
                {
                    weight += 1;
                }
                if(lung.isChecked())
                {
                    weight += 3;
                }
                if(heart.isChecked())
                {
                    weight += 3;
                }
                if(kidney.isChecked())
                {
                    weight += 1;
                }
                if(inst_1.isChecked())
                {
                    weight += 5;
                }
                if(inst_2.isChecked())
                {
                    weight += 7;
                }
                if(radioButton1.isChecked())
                {
                    weight += 2;
                }

                if(weight>=10)
                {

                    builder.setMessage("You are at high risk.")
                            .setCancelable(false)
                            .setPositiveButton("Proceed",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(getApplicationContext(),"You clicked yes on alert box",Toast.LENGTH_LONG).show();
                                    Intent startupload=new Intent(SelfAssessment.this,upload_id.class);
                                    startService(startupload);
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.setTitle("Self Assessment Status");
                    alert.show();
/*                    Intent first = new Intent();
                    first.putExtra("Results", "OK");
                    setResult(RESULT_OK,first);
                    //finish();*/
                }
                else
                {
                    Toast.makeText(SelfAssessment.this,"Congrats! You are safe.",Toast.LENGTH_LONG).show();
/*                    Intent first = new Intent();
                    first.putExtra("Results", "NOT OK");
                    setResult(RESULT_OK,first);
                    finish();*/
                }

                cough.setChecked(false);
                fever.setChecked(false);
                breathing.setChecked(false);
                loss_of_sense.setChecked(false);
                none_of_these_1.setChecked(false);
                none_of_these_2.setChecked(false);
                none_of_these_3.setChecked(false);
                diabetes.setChecked(false);
                hypertension.setChecked(false);
                lung.setChecked(false);
                heart.setChecked(false);
                kidney.setChecked(false);
                inst_1.setChecked(false);
                inst_2.setChecked(false);
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
            }
        });
    }
}