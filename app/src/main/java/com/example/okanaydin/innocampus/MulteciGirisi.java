package com.example.okanaydin.innocampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class MulteciGirisi extends AppCompatActivity {

    private Button btnMulteci, btnDestekOl;
    private EditText et_id;

    private ArrayList<RefugeeModel> mRefugeeModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multeci_girisi);

        for (int i = 0; i < 10; i++) {
            mRefugeeModels.add(new RefugeeModel("1" + String.valueOf(i % 3) + "34387648" + String.valueOf(i)));
        }
        // 10 34 38 76 480
        // 10 34 38 76 481

        btnMulteci=(Button)findViewById(R.id.btnMulteci);
        btnDestekOl=(Button)findViewById(R.id.btnDestekOl);
        et_id=(EditText)findViewById(R.id.et_id);

        btnMulteci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Login Sağla
                String id = et_id.getText().toString();

                boolean isExists = false;

                //Id girilmemiş ise kullanıcıyı uyarıyoruz.
                if (TextUtils.isEmpty(id)) {
                    Toast.makeText(getApplicationContext(), "Lütfen id giriniz..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (id.length()<11){
                    Toast.makeText(getApplicationContext(),"Id'niz en az 11 haneden oluşmalıdır..",Toast.LENGTH_LONG).show();
                    return;
                } else {

                    for (RefugeeModel refugee: mRefugeeModels) {
                        if (id.equals(refugee.id)) {
                            isExists = true;
                        }
                    }

                    if (isExists) {
                        //ID gönder
                        Intent intent = new Intent(getApplicationContext(),RefugeeFormActivity.class);
                        intent.putExtra("id", et_id.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Geçersiz id..", Toast.LENGTH_SHORT).show();
                    }

                }

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
