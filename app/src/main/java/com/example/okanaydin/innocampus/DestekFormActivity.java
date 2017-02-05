package com.example.okanaydin.innocampus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DestekFormActivity extends AppCompatActivity {

     private String mPost_key = null ;
     private DatabaseReference mDatabase2;

    private ImageView mselectImage;

    private TextView mDestekFormTitle;
    private TextView mDestekFormDesc;
    private TextView cardID;

    private Button mDestekFormBtn;

    private EditText userPhone, userName, userAnswer;

    //Resmi iptal et
    private Uri mimageUri = null ;
    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStogare;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destek_form);

        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("RefugeeNeeds");

        mAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("RefugeeID");

        mselectImage = (ImageView) findViewById(R.id.destekFormGorsel);
        mDestekFormTitle = (TextView) findViewById(R.id.destekFormTitle);
        mDestekFormDesc = (TextView) findViewById(R.id.destekFormDesc);
        userName = (EditText) findViewById(R.id.userName);
        userPhone = (EditText) findViewById(R.id.userPhone);
        userAnswer = (EditText) findViewById(R.id.userAnswer);

        cardID = (TextView) findViewById(R.id.cardID);

        mDestekFormBtn = (Button) findViewById(R.id.destekFormGonderBtn);


        cardID.setText(getIntent().getExtras().getString("RefugeeID"));

        //Galeri den gorsel secme
        mselectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("UserImage/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });

        mDatabase2.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("Title").getValue();
                String post_description = (String) dataSnapshot.child("Description").getValue();
                String post_image = (String) dataSnapshot.child("Image").getValue();
                String post_uid = (String) dataSnapshot.child("RefugeeID").getValue();

                Toast.makeText(getApplicationContext(), post_uid, Toast.LENGTH_SHORT).show();

                mDestekFormTitle.setText(post_title);
                mDestekFormDesc.setText(post_description);

                Picasso.with(DestekFormActivity.this).load(post_image).into(mselectImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Firebase
        mDestekFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("log", "Clicked");
                startPosting();

            }
        });

    }


        private void startPosting() {
            Log.v("Log", "startPosting");
            //mProgress.setMessage("Talebiniz Gönderiliyor ...");
            //mProgress.show();

            final String mUserPhone = userPhone.getText().toString().trim();
            final String mUserName= userName.getText().toString().trim();
            final String mUserAnswer = userAnswer.getText().toString().trim();
            final String refugee_id = cardID.getText().toString().trim();


            mStogare = FirebaseStorage.getInstance().getReference();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("UserSupports");

            //New Post
            if (!TextUtils.isEmpty(mUserPhone) && !TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mUserAnswer) && mimageUri != null ){
                Log.v("Log", "in if");
                StorageReference filePath = mStogare.child("Images").child(mimageUri.getLastPathSegment());

                filePath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.v("Log", "Got image");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        DatabaseReference newPost = mDatabase.push();


                        newPost.child("UserName").setValue(mUserName);
                        newPost.child("UserPhone").setValue(mUserPhone);
                        newPost.child("UserAnswer").setValue(mUserAnswer);
                        newPost.child("RefugeeID").setValue(refugee_id);
                        newPost.child("Destekci").setValue(downloadUrl.toString());
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


    }


