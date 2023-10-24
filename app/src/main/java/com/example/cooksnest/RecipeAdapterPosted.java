package com.example.cooksnest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapterPosted extends ArrayAdapter<Recipe> {

    int resource;
    Context cntx;
    List<Recipe> data;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RecipeAdapterPosted(Context ctx, int res, List<Recipe> tasks) {
        super(ctx, res, tasks);
        data = tasks;
        cntx = ctx;
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        Recipe it = getItem(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        TextView NameView = (TextView) itemView.findViewById(R.id.Name);

        NameView.setText(it.getRecipeName());

        TextView ServesView = (TextView) itemView.findViewById(R.id.servingSize);

        String servings = String.valueOf(it.getServings());
        ServesView.setText("Serves " + servings +" people");

        TextView HoursView = (TextView) itemView.findViewById(R.id.prepTime);
        ImageView food = itemView.findViewById(R.id.foodpic);
        if (it.getImagepath() != null) {
            //Log.d("path", it.getImagepath());
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://cooksnest-c462c.appspot.com");
            ImageButton viewdetails = itemView.findViewById(R.id.opendetails);
            StorageReference islandRef = storageRef.child("images/" + it.getImagepath());

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
                    Picasso.get().load(finalLocalFile).into(food);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

            Picasso.get().setLoggingEnabled(true);
            String prepTime;
            if (it.getMinutes() >= 10) {
                prepTime = "Time to cook - " + it.getHours() + ":" + it.getMinutes();
            } else {
                prepTime = "Time to cook - " + it.getHours() + ":0" + it.getMinutes();
            }

            HoursView.setText(prepTime);
            ImageButton editrecipe = itemView.findViewById(R.id.editrecipe);
            ImageButton deleterecipe = itemView.findViewById(R.id.deleterecipe);
            viewdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cntx, viewRecipe.class);
                    intent.putExtra("recipename", it.getRecipeName());
                    intent.putExtra("servingsize", it.getServings());
                    intent.putExtra("user", it.getUsername());
                    intent.putExtra("hours", it.getHours());
                    intent.putExtra("mins", it.getMinutes());
                    intent.putExtra("steps", it.getSteps());
                    intent.putExtra("imagepath", it.getImagepath());
                    intent.putExtra("ingredients", it.getIngredients());
                    String HASHTAGLIST = it.getHashtags().toString();
                    HASHTAGLIST = HASHTAGLIST.substring(1, HASHTAGLIST.length() - 1);
                    intent.putExtra("itsHashtags", HASHTAGLIST);
                    cntx.startActivity(intent);
                }
            });
            editrecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(cntx, addrecipe.class);
                    intent.putExtra("recipename", it.getRecipeName());
                    intent.putExtra("servingsize", it.getServings());
                    intent.putExtra("user", it.getUsername());
                    intent.putExtra("hours", it.getHours());
                    intent.putExtra("mins", it.getMinutes());
                    intent.putExtra("steps", it.getSteps());
                    intent.putExtra("imagepath", it.getImagepath());
                    intent.putExtra("ingredients", it.getIngredients());
                    intent.putExtra("id", it.getId());
                    intent.putStringArrayListExtra("Hashtags", (ArrayList<String>)
                            it.getHashtags());
                    cntx.startActivity(intent);
                }
            });
            deleterecipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = it.getId();
                    Log.d(id, "yay");
                    db.collection("recipes").document(id).delete();
                    doStuff();
                    notifyDataSetChanged();
                }
            });
        }
        return itemView;
    }

    private void doStuff() {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        String username = myPrefs.getString("username", "username");
        db.collection("recipes")
                .whereEqualTo("User", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            data.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getId();
                                Recipe toAdd = new Recipe(document.getData(), document.getId());
                                data.add(toAdd);
                                notifyDataSetChanged();

                            }
                        } else {
                            Log.d("SAD", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
