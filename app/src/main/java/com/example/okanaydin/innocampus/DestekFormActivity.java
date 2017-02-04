package com.example.okanaydin.innocampus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DestekFormActivity extends AppCompatActivity {

     private String mPost_key = null ;
     private DatabaseReference mDatabase2;

    private ImageView mDestekFormImage;
    private TextView mDestekFormTitle;
    private TextView mDestekFormDesc;
    private TextView cardID;

     FirebaseAuth mAuth;

    private Button mDestekFormBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destek_form);


       mDatabase2 = FirebaseDatabase.getInstance().getReference().child("RefugeeNeeds");

       mAuth=FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("RefugeeID");

        mDestekFormImage = (ImageView) findViewById(R.id.destekFormGorsel);
        mDestekFormTitle = (TextView) findViewById(R.id.destekFormTitle);
        mDestekFormDesc = (TextView) findViewById(R.id.destekFormDesc);
        cardID=(TextView)findViewById(R.id.cardID);

        mDestekFormBtn =(Button) findViewById(R.id.destekFormGonderBtn);


        cardID.setText(getIntent().getExtras().getString("RefugeeID"));

// /*
        mDatabase2.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String ) dataSnapshot.child("Title").getValue();
                String post_description = (String ) dataSnapshot.child("Description").getValue();
                String post_image = (String ) dataSnapshot.child("Image").getValue();
                String post_uid = (String ) dataSnapshot.child("RefugeeID").getValue();

                Toast.makeText(getApplicationContext(),post_uid,Toast.LENGTH_SHORT).show();

                mDestekFormTitle.setText(post_title);
                mDestekFormDesc.setText(post_description);

                Picasso.with(DestekFormActivity.this).load(post_image).into(mDestekFormImage);


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

     //   */
        mDestekFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });


    }
}
