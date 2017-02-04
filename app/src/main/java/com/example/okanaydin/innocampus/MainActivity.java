package com.example.okanaydin.innocampus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Spinner içerisine koyacağımız verileri tanımlıyoruz.
    private String[] iller={"İSTANBUL","ANKARA"};
    private String[] ilceler0={"ADALAR","ARNAVUTKÖY","ATAŞEHİR","AVCILAR","BAğCILAR","BAHÇELİEVLER","BAKIRKÖY","BAŞAKŞEHİR","BAYRAMPAŞA","BEŞİKTAŞ","BEYLİKDÜZÜ","BEYOĞLU","BÜYÜKÇEKMECE","BEYKOZ","ÇATALCA","ÇEKMEKÖY","ESENLER","ESENYURT","EYÜP","FATİH","GAZİOSMANPAŞA","GÜNGÖREN","KADIKÖY","KAĞITHANE","KARTAL","KÜÇÜKÇEKMECE","MALTEPE","PENDİK","SANCAKTEPE","SARIYER","SİLİVRİ","SULTANBEYLİ","SULTANGAZİ","ŞİLE","ŞİŞLİ","TUZLA","ÜSKÜDAR","ÜMRANİYE","ZEYTİNBURNU"};
    private String[] ilceler1={"AKYURT","ALTINDAĞ","AYAŞ","BALA","BEYPAZARI","ÇAMLIDERE","ÇANKAYA","ÇUBUK","ELMADAĞ","ETİMESGUT","EVREN","GÖLBAŞI","GÜDÜL","HAYMANA","KALECİK","KAZAN","KEÇİÖREN","KIZILCAHAMAM","MAMAK","NALLIHAN","POLATLI","PURSAKLAR","SİNCAN","ŞEREFLİKOÇHİSAR","YENİMAHALLE"};

    //Spinner'ları ve Adapter'lerini tanımlıyoruz.
    private Spinner spinnerIller;
    private Spinner spinnerIlceler;
    private ArrayAdapter<String> dataAdapterForIller;
    private ArrayAdapter<String> dataAdapterForIlceler;

    private TextView textID;
    private EditText etHizmet, etMesaj;
    private Button btnGonder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etHizmet=(EditText)findViewById(R.id.etHizmet);
        etMesaj=(EditText)findViewById(R.id.etMesaj);
        btnGonder=(Button)findViewById(R.id.btnGonder);
        textID=(TextView)findViewById(R.id.textID);

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
                    dataAdapterForIlceler = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,ilceler0);
                else if(parent.getSelectedItem().toString().equals(iller[1]))
                    dataAdapterForIlceler = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,ilceler1);

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



        btnGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), etHizmet.getText().toString() + " " + etMesaj.getText().toString(),Toast.LENGTH_LONG).show();

            }
        });

    }
}