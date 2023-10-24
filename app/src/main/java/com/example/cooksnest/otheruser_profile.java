package com.example.cooksnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class otheruser_profile extends AppCompatActivity {

    protected static RecipeAdapterOthers aa;
    protected static ArrayList<Recipe> otherrecipesarray;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otheruser_profile);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Context context = getApplicationContext();
        Intent intent = getIntent();
        ListView otherRecipes = (ListView) findViewById(R.id.otheruserrecipes);
        otherrecipesarray = new ArrayList<Recipe>();
        aa = new RecipeAdapterOthers(this, R.layout.recipe_layout_others, otherrecipesarray);
        TextView usernamedisplay = findViewById(R.id.otherprofileusername);
        ImageView ProfilePicture = findViewById(R.id.OtherProfilePic);
        ImageButton BackButton = findViewById(R.id.imageButton);
        BackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        otherRecipes.setAdapter(aa);
        username = intent.getStringExtra("username");
        usernamedisplay.setText(username);
        FloatingActionButton starttexting = findViewById(R.id.starttexting);
        starttexting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("OnClickError", "Reached OnClick for message");
                Context context = getApplicationContext();
                SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                String fromUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();//set sender email here
                String toUser = username;//set recipient username here
                String fromUser = myPrefs.getString("username", "username");//set sender username here
                Log.d("OnClickError", toUser + " " + fromUser);
                db.collection("users").whereEqualTo("username", toUser).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("Tag", document.getId() + " => " + document.getData());
                                        String toUserEmail = document.getId();
                                        db.collection("users").document(toUserEmail).update
                                                ("messaged",FieldValue.arrayUnion(fromUser));
                                        db.collection("users").document(fromUserEmail).update
                                                ("messaged", FieldValue.arrayUnion(toUser));
                                        Intent intent = new Intent(context, messaging.class);
                                        intent.putExtra("Username", username);
                                        startActivity(intent);

                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            otherrecipesarray.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReferenceFromUrl
                                        ("gs://cooksnest-c462c.appspot.com");
                                StorageReference islandRef = storageRef.child("images/" + document.getId());
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
                                        Picasso.get().load(finalLocalFile).into(ProfilePicture);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });
                            }
                            db.collection("recipes")
                                    .whereEqualTo("User", username)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                otherrecipesarray.clear();
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    document.getId();
                                                    Recipe toAdd = new Recipe(document.getData(), document.getId());
                                                    otherrecipesarray.add(toAdd);
                                                    aa.notifyDataSetChanged();
                                                }
                                            } else {
                                                Log.d("SAD", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.d("SAD", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void textlaunch(View view) {}
}