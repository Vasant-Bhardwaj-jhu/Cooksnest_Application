package com.example.cooksnest;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {
    Context cntx;
    private Launchscreen myact;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    int count;
    private final String TAG = "TAG";
    String username;
    private ListView myRecipes;

    public profile() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static profile newInstance() {
        profile fragment = new profile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

/*    public void toAddRecipe(View view){
        Intent intent = new Intent(this.myact , addrecipe.class);
        startActivity(intent);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //getSavedRecipes();
        View myview = inflater.inflate(R.layout.fragment_profile, container, false);
        myact = (Launchscreen) getActivity();
        cntx = getActivity().getApplicationContext();
        myRecipes = (ListView) myview.findViewById(R.id.myRecipes);
        myRecipes.setAdapter(myact.aa);
        //getSavedRecipes();
        TextView mysaved = myview.findViewById(R.id.savedrecipes);
        TextView myposted = myview.findViewById(R.id.postedrecipes);
        ImageView ProfileImage = myview.findViewById(R.id.ProfilePic);
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
                Picasso.get().load(finalLocalFile).into(ProfileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        myposted.setTypeface(null, Typeface.BOLD);
        myposted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysaved.setTypeface(null, Typeface.NORMAL);
                myposted.setTypeface(null, Typeface.BOLD);
                myRecipes.setAdapter(myact.aa);
            }
        });
        mysaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myposted.setTypeface(null, Typeface.NORMAL);
                mysaved.setTypeface(null, Typeface.BOLD);
                myRecipes.setAdapter(myact.bb);
            }
        });
        myRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selected = myact.myRecipes.get(position);
            }
        });
        Context context = getContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        count = myPrefs.getInt("numRecipes", 0);
        FloatingActionButton addrecipe = myview.findViewById(R.id.addnewrecipe);
        TextView usernametext = myview.findViewById(R.id.viewusername);
        username = myPrefs.getString("username", "username");
        usernametext.setText(username + "'s Page");
        db.collection("recipes")
                .whereEqualTo("User", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            myact.myRecipes.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getId();
                                Recipe toAdd = new Recipe(document.getData(), document.getId());
                                myact.myRecipes.add(toAdd);
                            }
                            myact.aa.notifyDataSetChanged();
                            myact.bb.notifyDataSetChanged();
                            getSavedRecipes();
                        } else {
                            Log.d("SAD", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return myview;
    }

    @Override
    public void onResume() {
        myact.aa.notifyDataSetChanged();
        myact.bb.notifyDataSetChanged();
        super.onResume();
    }

    public void saveRecipe(String id) {

        String email = fAuth.getCurrentUser().getEmail();
        Log.d("SAVE", email);
        db.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("SAVE", "IN");
                        Map<String, Object> res = document.getData();
                        List<String> saved;
                        if (!res.containsKey("savedRecipes")) {
                            saved = new ArrayList<>();
                        } else{
                            saved = (List<String>) res.get("savedRecipes");
                        }
                        if (!saved.contains(id)) {
                            saved.add(id);
                        }
                        Map<String, Object> map = new HashMap<>();
                        map.put("savedRecipes", saved);
                        db.collection("users").document(email).set(map, SetOptions.merge());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void getSavedRecipes() {
        String email = fAuth.getCurrentUser().getEmail();
        Log.d("SAVE", "reached function");
        db.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> saved = (List<String>) document.get("savedRecipes");
                        myact.savedRecipes.clear();
                        if (saved == null){
                            saved = new ArrayList<String>();
                        }
                        for (String rec : saved) {
                            db.collection("recipes").document(rec).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Recipe toAdd = new Recipe(document.getData(), document.getId());
                                            myact.savedRecipes.add(toAdd);
                                            myact.aa.notifyDataSetChanged();
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });

                        }



                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}