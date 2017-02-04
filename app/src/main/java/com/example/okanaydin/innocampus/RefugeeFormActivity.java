package com.example.okanaydin.innocampus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class RefugeeFormActivity extends AppCompatActivity {

    //Spinner
    private String[] iller={"İSTANBUL","ANKARA"};
    private String[] ilceler0={"ADALAR","ARNAVUTKÖY","ATAŞEHİR","AVCILAR","BAğCILAR","BAHÇELİEVLER","BAKIRKÖY","BAŞAKŞEHİR","BAYRAMPAŞA","BEŞİKTAŞ","BEYLİKDÜZÜ","BEYOĞLU","BÜYÜKÇEKMECE","BEYKOZ","ÇATALCA","ÇEKMEKÖY","ESENLER","ESENYURT","EYÜP","FATİH","GAZİOSMANPAŞA","GÜNGÖREN","KADIKÖY","KAĞITHANE","KARTAL","KÜÇÜKÇEKMECE","MALTEPE","PENDİK","SANCAKTEPE","SARIYER","SİLİVRİ","SULTANBEYLİ","SULTANGAZİ","ŞİLE","ŞİŞLİ","TUZLA","ÜSKÜDAR","ÜMRANİYE","ZEYTİNBURNU"};
    private String[] ilceler1={"AKYURT","ALTINDAĞ","AYAŞ","BALA","BEYPAZARI","ÇAMLIDERE","ÇANKAYA","ÇUBUK","ELMADAĞ","ETİMESGUT","EVREN","GÖLBAŞI","GÜDÜL","HAYMANA","KALECİK","KAZAN","KEÇİÖREN","KIZILCAHAMAM","MAMAK","NALLIHAN","POLATLI","PURSAKLAR","SİNCAN","ŞEREFLİKOÇHİSAR","YENİMAHALLE"};

    //Spinner Adapter
    private Spinner spinnerIller;
    private Spinner spinnerIlceler;
    private ArrayAdapter<String> dataAdapterForIller;
    private ArrayAdapter<String> dataAdapterForIlceler;

    //Kullanici ID
    private TextView textID;

    //Gorsel ekle
    private ImageButton mselectImage;

    //Ihtiyac Belirtme
    private EditText mPostTitle, mPostDesc;

    //Gonderme
    private Button mSubmitBtn;


    private Uri mimageUri = null ;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStogare;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refugee_form);

        mStogare = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mPostTitle=(EditText)findViewById(R.id.titleField);
        mPostDesc=(EditText)findViewById(R.id.descField);
        mSubmitBtn=(Button)findViewById(R.id.submitBtn);
        textID=(TextView)findViewById(R.id.textID);
        mselectImage=(ImageButton)findViewById(R.id.gorsel);


        //Galeri den gorsel secme
        mselectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });


        mProgress = new ProgressDialog(this);

        //Kullanıcı ID cekme
        textID.setText(getIntent().getExtras().getString("id"));

        //xml kısmında hazırladığımğız spinnerları burda tanımladıklarımızla eşleştiriyoruz.
        spinnerIller = (Spinner) findViewById(R.id.spinner);
        spinnerIlceler = (Spinner) findViewById(R.id.spinner2);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForIller = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, iller);
        dataAdapterForIlceler = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ilceler0);

        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForIlceler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerIller.setAdapter(dataAdapterForIller);
        spinnerIlceler.setAdapter(dataAdapterForIlceler);

        // Listelerden bir eleman seçildiğinde yapılacakları tanımlıyoruz.
        spinnerIller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Hangi il seçilmişse onun ilçeleri adapter'e ekleniyor.
                if(parent.getSelectedItem().toString().equals(iller[0]))
                    dataAdapterForIlceler = new ArrayAdapter<String>(RefugeeFormActivity.this, android.R.layout.simple_spinner_item,ilceler0);
                else if(parent.getSelectedItem().toString().equals(iller[1]))
                    dataAdapterForIlceler = new ArrayAdapter<String>(RefugeeFormActivity.this, android.R.layout.simple_spinner_item,ilceler1);

                dataAdapterForIlceler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIlceler.setAdapter(dataAdapterForIlceler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerIlceler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Seçilen il ve ilçeyi ekranda gösteriyoruz.
                Toast.makeText(getBaseContext(), ""+spinnerIller.getSelectedItem().toString()+" - "+parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //Database Post islemini tetikleme
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();

                Toast.makeText(getApplicationContext(), mPostTitle.getText().toString() + " " + mPostDesc.getText().toString(),Toast.LENGTH_LONG).show();

            }
        });

    }


    private void startPosting() {

        mProgress.setMessage("Talebiniz Gönderiliyor ...");
        mProgress.show();

        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val= mPostDesc.getText().toString().trim();

        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mimageUri != null){

            StorageReference filePath = mStogare.child("Görseller").child(mimageUri.getLastPathSegment());

            filePath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("Hangi Hizmete İhtiyacı Var ? ").setValue(title_val);
                    newPost.child("Ne Yapılmasını İstiyor ? ").setValue(desc_val);
                    newPost.child("Görsel").setValue(downloadUrl.toString());

                    mProgress.dismiss();

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            mimageUri = data.getData();
            mselectImage.setImageURI(mimageUri );

        }
    }
}
