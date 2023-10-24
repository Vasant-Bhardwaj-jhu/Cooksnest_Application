package com.example.cooksnest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Launchscreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment browse;
    private Fragment profile;
    private Fragment message;
    private Fragment settings;
    private FragmentTransaction transaction;
    protected static ArrayList<Recipe> myRecipes;
    protected static ArrayList<Recipe> savedRecipes;
    protected static ArrayList<Recipe> allRecipes;
    protected static ArrayList<String> userlist;
    protected static RecipeAdapterPosted aa;
    protected static RecipeAdapterSaved bb;
    protected static userlistadapter cc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    List<String> saved;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchscreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = myPrefs.getString("username", "username");
        NavigationView navView = findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);
        TextView navUsername = header.findViewById(R.id.UsernameView);
        navUsername.setText(username);
        ImageView navProfile = header.findViewById(R.id.NavProfilePic);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl
                ("gs://cooksnest-c462c.appspot.com");
        StorageReference islandRef = storageRef.child("images/" + fAuth.getCurrentUser().getEmail());
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
                Picasso.get().load(finalLocalFile).into(navProfile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        browse = new browse();
        profile = new profile();
        message = new messagelist();
        settings = new settings();
        myRecipes = new ArrayList<Recipe>();
        savedRecipes = new ArrayList<Recipe>();
        allRecipes = new ArrayList<Recipe>();
        userlist = new ArrayList<String>();
        aa = new RecipeAdapterPosted(this, R.layout.recipe_layout_posted, myRecipes);
        bb = new RecipeAdapterSaved(this, R.layout.recipe_layout_saved, savedRecipes);
        cc = new userlistadapter(this, R.layout.user_listadapter, userlist);
        db.collection("recipes").whereNotEqualTo("User", username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Recipe toAdd = new Recipe(document.getData(), document.getId());
                        allRecipes.add(toAdd);
                    }
                } else {
                    Log.d("SAD", "Error getting documents: ", task.getException());
                }
            }
        });
        String email = fAuth.getCurrentUser().getEmail();
        db.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        saved = (List<String>) document.get("messaged");
                        //Log.d(String.valueOf(saved.size()), "sizeofsaved");
                        if (saved == null){
                            saved = new ArrayList<String>();
                        }
                        for (String rec : saved) {
                            Log.d("adding", rec);
                            userlist.add(rec);
                            cc.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, profile);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        aa.notifyDataSetChanged();
        bb.notifyDataSetChanged();
        cc.notifyDataSetChanged();
        super.onResume();
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this , logInActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_browse) {
            setTitle("Browse");
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, browse);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_profile) {
            setTitle("Profile");
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, profile);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_messages) {
            setTitle("Messages");
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, message);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_settings) {
            setTitle("Settings");
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, settings);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void toAddRecipe(View view) {
        Intent intent = new Intent(this , addrecipe.class);
        startActivity(intent);
    }
}
