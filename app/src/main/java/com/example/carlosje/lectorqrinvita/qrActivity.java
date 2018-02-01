package com.example.carlosje.lectorqrinvita;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;

public class qrActivity extends AppCompatActivity {
    private TextView tv_qr, tv_familia, tv_restan;
    private String codigo;
    private ProgressBar progressBar;
    private EditText et_entran;
    private Button bt_envia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        tv_qr = (TextView) findViewById(R.id.tv_qr);
        tv_familia = (TextView) findViewById(R.id.tv_familia);
        tv_restan = (TextView) findViewById(R.id.tv_restan);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        et_entran=(EditText) findViewById(R.id.et_ingresan);
        progressBar.setVisibility(View.GONE);
        codigo = getIntent().getStringExtra("codigo");
        bt_envia=(Button) findViewById(R.id.bt_envia);

        tv_qr.setText(codigo);
        buscaCodigo();

        tv_qr.setVisibility(View.GONE);



        tv_familia.setVisibility(View.GONE);
        tv_restan.setVisibility(View.GONE);


        progressBar.setVisibility(View.GONE);
        et_entran.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        bt_envia.setVisibility(View.GONE);





    }

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private ValueEventListener eventListener;
    private String familia, restan, total,B_qr,childCodigo;

    public void buscaCodigo(){
        progressBar.setVisibility(View.VISIBLE);



        database.child("qr").child("muestra").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {



                for (DataSnapshot QRSnapshot : snapshot.getChildren()) {



                    familia=QRSnapshot.child("familia").getValue().toString();
                    restan=QRSnapshot.child("restan").getValue().toString();
                    B_qr=QRSnapshot.child("qr").getValue().toString();


                    //Toast toast = Toast.makeText(getApplicationContext(), QRSnapshot.toString(), Toast.LENGTH_SHORT);
                    //toast.show();

                    if(B_qr.equals(codigo)){
                        tv_familia.setText(familia);
                        tv_restan.setText(restan);
                        childCodigo=QRSnapshot.child("codigo").getValue().toString();
                        progressBar.setVisibility(View.INVISIBLE);
                        tv_familia.setVisibility(View.VISIBLE);
                        tv_restan.setVisibility(View.VISIBLE);
                        et_entran.setVisibility(View.VISIBLE);
                        bt_envia.setVisibility(View.VISIBLE);

                    }






                }






            }

            @Override
            public void onCancelled(DatabaseError error) {
                tv_qr.setText(error.toString());
            }
        });


    }

    private String restantes,ingresan;


    public void ingreso(View view) {

        restantes = tv_restan.getText().toString();

        ingresan = et_entran.getText().toString();

        int ingresan_int=Integer.parseInt(ingresan);
        int restan_int=Integer.parseInt(restantes);



        if (ingresan_int > restan_int){

         Toast toast = Toast.makeText(getApplicationContext(),"El número de ingreso es mayor al número de boletos", Toast.LENGTH_LONG);
            toast.show();
            return;

        }else {

            int restanok=restan_int-ingresan_int;


            database.child("qr").child("muestra").child(childCodigo).child("restan").setValue(restanok);
            et_entran.setText("");
            et_entran.setVisibility(View.GONE);
            bt_envia.setVisibility(View.GONE);


        }



    }






}
