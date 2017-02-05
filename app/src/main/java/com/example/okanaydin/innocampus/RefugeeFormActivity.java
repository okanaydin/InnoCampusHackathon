package com.example.okanaydin.innocampus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RefugeeFormActivity extends AppCompatActivity {

    //Istek Kuyrugu
    RequestQueue kuyruk;

    //Il Ilce ArrayList
    private ArrayList<String> ilListesi = new ArrayList<>();
    private ArrayList<String> ilceListesi = new ArrayList<>();


    //Spinner Adapter
    private Spinner spinnerIller;
    private Spinner spinnerIlceler;
    private ArrayAdapter<String> dataAdapterForIller;
    private ArrayAdapter<String> dataAdapterForIlceler;

    //Hangi il konumunda oldugu

    private int indis;


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
        kuyruk= Volley.newRequestQueue(getApplicationContext());

        mStogare = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("RefugeeNeeds");

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
        //textID.setText(getIntent().getExtras().getString("id"));

        //xml kısmında hazırladığımğız spinnerları burda tanımladıklarımızla eşleştiriyoruz.
        spinnerIller = (Spinner) findViewById(R.id.spinner);
        spinnerIlceler = (Spinner) findViewById(R.id.spinner2);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForIller = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ilListesi);
        dataAdapterForIlceler = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ilceListesi);

        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForIller.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForIlceler.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerIller.setAdapter(dataAdapterForIller);
        spinnerIlceler.setAdapter(dataAdapterForIlceler);

        // Listelerden bir eleman seçildiginde yapilacaklari tanimliyoruz.
        JsonArrayRequest istek = new JsonArrayRequest(
                "http://yazilim24.com/android/iller.json",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject nesne = response.getJSONObject(i);
                                String il = nesne.getString("il");
                                ilListesi.add(il);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        dataAdapterForIller.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RefugeeFormActivity.this, "Bir hata olustu.", Toast.LENGTH_SHORT).show();
                        // error.printStackTrace();
                    }
                }
        );

        kuyruk.add(istek);


        spinnerIller.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                indis = i;

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray dizi = new JSONArray(response);
                            ilceListesi.clear();
                            for (int i = 0; i < dizi.length(); i++) {

                                String ilce = dizi.getString(i);
                                ilceListesi.add(ilce);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dataAdapterForIlceler.notifyDataSetChanged();
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RefugeeFormActivity.this, "Bir hata olustu.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                };
                // JsonArrayRequest parametrelerini gönderemiyor.
                StringRequest istek = new StringRequest(
                        Request.Method.POST,
                        " http://yazilim24.com/android/ilce.php", listener, errorListener) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parametreler = new HashMap<String, String>();
                        parametreler.put("il", ilListesi.get(indis));
                        return parametreler;
                    }
                };
                kuyruk.add(istek);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
        final String refugee_id = textID.getText().toString().trim();



        //New Post
        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mimageUri != null){

            StorageReference filePath = mStogare.child("Images").child(mimageUri.getLastPathSegment());

            filePath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("Title").setValue(title_val);
                    newPost.child("Description").setValue(desc_val);
                    newPost.child("RefugeeID").setValue(refugee_id);
                    newPost.child("Image").setValue(downloadUrl.toString());
                    // newPost.child("Location").setValue(location);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add){
            startActivity(new Intent(getApplicationContext(), RefugeeSupportActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
