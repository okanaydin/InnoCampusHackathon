package com.example.okanaydin.innocampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DestekciGirisi extends AppCompatActivity {

    private EditText etEmail, etParola;
    private Button btnDestekciGirisi, btnUyeOl;
    private TextView textForgot;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destekci_girisi);

        //FirebaseAuth sınıfının referans olduğu nesneleri kullanabilmek için getInstance methodunu kullanıyoruz.
        auth = FirebaseAuth.getInstance();


        etEmail=(EditText)findViewById(R.id.etEmail);
        etParola=(EditText)findViewById(R.id.etParola);
        btnDestekciGirisi=(Button)findViewById(R.id.btnDestekciGirisi);
        btnUyeOl=(Button)findViewById(R.id.btnUyeOl);
        textForgot=(TextView)findViewById(R.id.textForgot);

        //Geçerli bir yetkilendirme olup olmadığını kontrol ediyoruz.
        if(auth.getCurrentUser() == null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        btnDestekciGirisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();
                final String parola = etParola.getText().toString();

                //Email girilmemiş ise kullanıcıyı uyarıyoruz.
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Lütfen emailinizi giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Parola girilmemiş ise kullanıcıyı uyarıyoruz.
                if (TextUtils.isEmpty(parola)) {
                    Toast.makeText(getApplicationContext(), "Lütfen parolanızı giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Firebase üzerinde kullanıcı doğrulamasını başlatıyoruz
                //Eğer giriş başarılı olursa task.isSuccessful true dönecek ve MainActivity e geçilecek
                auth.signInWithEmailAndPassword(email, parola)
                        .addOnCompleteListener(DestekciGirisi.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Tekrar Deneyiniz..",Toast.LENGTH_SHORT).show();
                                    Log.e("Giriş Hatası",task.getException().getMessage());

                                }
                            }
                        });



            }
        });


        btnUyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),UyeOl.class));

            }
        });

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgetPassword.class));
            }
        });

    }
}
