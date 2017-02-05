package com.example.okanaydin.innocampus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static com.example.okanaydin.innocampus.R.id.textID;

public class RefugeeSupportActivity extends AppCompatActivity {

    //Istek Kuyrugu
    RequestQueue kuyrukDestek;

    //Il Ilce ArrayList
    private ArrayList<String> ilListesiDestek = new ArrayList<>();
    private ArrayList<String> ilceListesiDestek = new ArrayList<>();


    //Spinner Adapter
    private Spinner spinnerIllerDestek;
    private Spinner spinnerIlcelerDestek;
    private ArrayAdapter<String> dataAdapterForIllerDestek;
    private ArrayAdapter<String> dataAdapterForIlcelerDestek;

    //Hangi il konumunda oldugu

    private int indisDestek;


    //Kullanici ID
    private TextView textIDDestek;

    //Gorsel ekle
    private ImageButton mselectImageDestek;

    //Ihtiyac Belirtme
    private EditText mPostTitleDestek, mPostDescDestek;

    //Gonderme
    private Button mSubmitBtnDestek;


    private Uri mimageUriDestek = null ;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStogareDestek;
    private ProgressDialog mProgressDestek;
    private DatabaseReference mDatabaseDestek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refugee_form);
        kuyrukDestek= Volley.newRequestQueue(getApplicationContext());

        mStogareDestek = FirebaseStorage.getInstance().getReference();
        mDatabaseDestek = FirebaseDatabase.getInstance().getReference().child("RefugeeSupports");

        mPostTitleDestek=(EditText)findViewById(R.id.titleField);
        mPostDescDestek=(EditText)findViewById(R.id.descField);
        mSubmitBtnDestek=(Button)findViewById(R.id.submitBtn);
        textIDDestek=(TextView)findViewById(textID);
        mselectImageDestek=(ImageButton)findViewById(R.id.gorsel);


        //Galeri den gorsel secme
        mselectImageDestek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });


        mProgressDestek = new ProgressDialog(this);

        //Kullanıcı ID cekme
//        textIDDestek.setText(getIntent().getExtras().getString("id"));

        //xml kısmında hazırladığımğız spinnerları burda tanımladıklarımızla eşleştiriyoruz.
        spinnerIllerDestek = (Spinner) findViewById(R.id.spinner);
        spinnerIlcelerDestek = (Spinner) findViewById(R.id.spinner2);

        //Spinner'lar için adapterleri hazırlıyoruz.
        dataAdapterForIllerDestek = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ilListesiDestek);
        dataAdapterForIlcelerDestek = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ilceListesiDestek);

        //Listelenecek verilerin görünümünü belirliyoruz.
        dataAdapterForIllerDestek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterForIlcelerDestek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Hazırladğımız Adapter'leri Spinner'lara ekliyoruz.
        spinnerIllerDestek.setAdapter(dataAdapterForIllerDestek);
        spinnerIlcelerDestek.setAdapter(dataAdapterForIlcelerDestek);

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
                                ilListesiDestek.add(il);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        dataAdapterForIllerDestek.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Bir hata olustu.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        kuyrukDestek.add(istek);


        spinnerIllerDestek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                indisDestek = i;

                Response.Listener<String> listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray dizi = new JSONArray(response);
                            ilceListesiDestek.clear();
                            for (int i = 0; i < dizi.length(); i++) {

                                String ilce = dizi.getString(i);
                                ilceListesiDestek.add(ilce);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dataAdapterForIlcelerDestek.notifyDataSetChanged();
                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Bir hata olustu.", Toast.LENGTH_SHORT).show();
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
                        parametreler.put("il", ilListesiDestek.get(indisDestek));
                        return parametreler;
                    }
                };
                kuyrukDestek.add(istek);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mSubmitBtnDestek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPosting();

                Toast.makeText(getApplicationContext(), mPostTitleDestek.getText().toString() + " " + mPostDescDestek.getText().toString(),Toast.LENGTH_LONG).show();

            }
        });

    }


    private void startPosting() {

        mProgressDestek.setMessage("Talebiniz Gönderiliyor ...");
        mProgressDestek.show();

        final String title_val = mPostTitleDestek.getText().toString().trim();
        final String desc_val= mPostDescDestek.getText().toString().trim();
       // final String refugee_id = textIDDestek.getText().toString().trim();



        //New Post
        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mimageUriDestek != null){

            StorageReference filePath = mStogareDestek.child("ImagesDestek").child(mimageUriDestek.getLastPathSegment());

            filePath.putFile(mimageUriDestek).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newPost = mDatabaseDestek.push();

                    newPost.child("Title").setValue(title_val);
                    newPost.child("Description").setValue(desc_val);
                  //  newPost.child("RefugeeID").setValue(refugee_id);
                    newPost.child("Image").setValue(downloadUrl.toString());
                    // newPost.child("Location").setValue(location);


                    mProgressDestek.dismiss();

                }
            });
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

            mimageUriDestek = data.getData();
            mselectImageDestek.setImageURI(mimageUriDestek );

        }
    }


}
