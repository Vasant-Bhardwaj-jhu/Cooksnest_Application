package com.example.cooksnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class viewRecipe extends AppCompatActivity {

    viewRecipe myact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        ImageView image = findViewById(R.id.viewimage);
        TextView recipenametext = findViewById(R.id.recipenameview);
        TextView username = findViewById(R.id.viewusername);
        TextView preptime = findViewById(R.id.preptimeview);
        TextView serving = findViewById(R.id.servingview);
        TextView ingredients = findViewById(R.id.ingredientsview);
        TextView Steps = findViewById(R.id.stepsview);
        TextView hashtagview = findViewById(R.id.hashtagrecipeview);

        Intent intent = getIntent();
        String recipename = intent.getStringExtra("recipename");
        String user = intent.getStringExtra("user");
        int hoursint = intent.getIntExtra("hours", 0);
        int minint = intent.getIntExtra("mins", 0);
        int servingsize = intent.getIntExtra("servingsize", 0);
        String stepsstring = intent.getStringExtra("steps");
        String imagepath = intent.getStringExtra("imagepath");
        String ingredientsstring = intent.getStringExtra("ingredients");
        String hashTagList = intent.getStringExtra("itsHashtags");

        recipenametext.setText(recipename);
        hashtagview.setText(hashTagList);

        ImageButton backButton = findViewById(R.id.imageButton2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SpannableString content = new SpannableString("By: " + user);
        content.setSpan(new UnderlineSpan(), 4, content.length(), 0);
        username.setText(content);
        myact = this;
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myact, otheruser_profile.class);
                String usernamestring = username.getText().toString().substring(4);
                intent.putExtra("username", usernamestring);
                startActivity(intent);
            }
        });
        String prepTime;
        if (minint >= 10) {
            prepTime = "Time to cook - " + hoursint + ":" + minint;
        } else {
            prepTime = "Time to cook - " + hoursint + ":0" + minint;
        }
        preptime.setText(prepTime);
        serving.setText("Serves " + servingsize + " people");
        ingredients.setText(ingredientsstring);
        Steps.setText(stepsstring);
        if (imagepath != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://cooksnest-c462c.appspot.com");
            StorageReference islandRef = storageRef.child("images/" + imagepath);
            File localFile = null;
            try {
                localFile = File.createTempFile("images", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            File finalLocalFile = localFile;
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Picasso.get().load(finalLocalFile).into(image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }
}

