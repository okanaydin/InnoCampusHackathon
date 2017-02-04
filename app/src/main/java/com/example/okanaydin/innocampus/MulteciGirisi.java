package com.example.okanaydin.innocampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MulteciGirisi extends AppCompatActivity {

    private Button btnMulteci, btnDestekOl;
    private EditText et_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multeci_girisi);


        btnMulteci=(Button)findViewById(R.id.btnMulteci);
        btnDestekOl=(Button)findViewById(R.id.btnDestekOl);
        et_id=(EditText)findViewById(R.id.et_id);


        btnMulteci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Login Sağla
                String id = et_id.getText().toString();

                //Id girilmemiş ise kullanıcıyı uyarıyoruz.
                if (TextUtils.isEmpty(id)) {
                    Toast.makeText(getApplicationContext(), "Lütfen id giriniz..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (id.length()<11){
                    Toast.makeText(getApplicationContext(),"Id'niz en az 11 haneden oluşmalıdır..",Toast.LENGTH_LONG).show();
                    return;
                }
                //ID gönder
                Intent intent = new Intent(getApplicationContext(),RefugeeFormActivity.class);
                intent.putExtra("id", et_id.getText().toString());

                startActivity(intent);


            }
        });




        btnDestekOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), DestekciGirisi.class));

            }
        });
    }
}
