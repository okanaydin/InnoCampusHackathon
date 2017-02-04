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
    // private EditText et_sifre;

    //private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multeci_girisi);

      //  auth=FirebaseAuth.getInstance();

        btnMulteci=(Button)findViewById(R.id.btnMulteci);
        btnDestekOl=(Button)findViewById(R.id.btnDestekOl);
        et_id=(EditText)findViewById(R.id.et_id);
//        et_sifre=(EditText)findViewById(R.id.et_multeci_password);



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
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("id", et_id.getText().toString());



/*
                String email2=et_id.getText().toString();
                String parola2=et_sifre.getText().toString();

                //FirebaseAuth ile email,parola parametrelerini kullanarak yeni bir kullanıcı oluşturuyoruz.
                auth.createUserWithEmailAndPassword(email2,parola2)
                        .addOnCompleteListener(MulteciGirisi.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                //İşlem başarısız olursa kullanıcıya bir Toast mesajıyla bildiriyoruz.
                                if (!task.isSuccessful()) {

                                    Toast.makeText(getApplicationContext(), "Yetkilendirme Hatası", Toast.LENGTH_SHORT).show();
                                }

                                //İşlem başarılı ise  MainActivity e yönlendiriyoruz.
                                else {
                                    Toast.makeText(getApplicationContext(),"Tebrikler",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }

                            }
                        });
*/

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
