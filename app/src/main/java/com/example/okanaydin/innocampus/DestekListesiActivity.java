package com.example.okanaydin.innocampus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class DestekListesiActivity extends AppCompatActivity {

    private RecyclerView mBlogList;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destek_listesi);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("RefugeeNeeds");

        mBlogList = (RecyclerView) findViewById(R.id.blogList);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Veri, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Veri, BlogViewHolder>(
                Veri.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Veri model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.Title);
                viewHolder.setDescription(model.Description);
                viewHolder.setImage(getApplicationContext(),model.Image);


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //Card ın keyi toast ile veriliyor....

                        //Toast.makeText(getApplicationContext(),post_key,Toast.LENGTH_SHORT).show();

                        Intent destekFormIntent=new Intent(getApplicationContext(),DestekFormActivity.class);
                        destekFormIntent.putExtra("id", post_key.toString());
                        startActivity(destekFormIntent);

                    }
                });

            }
        };


        mBlogList.setAdapter(firebaseRecyclerAdapter);


    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;


        public BlogViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


        }

        public void setTitle(String title) {

             TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setDescription(String desc){

            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx,  String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).placeholder(ContextCompat.getDrawable(ctx, R.mipmap.logo2)).into(post_image);
        }

    }

}
