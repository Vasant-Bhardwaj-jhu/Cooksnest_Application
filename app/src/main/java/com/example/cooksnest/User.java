package com.example.cooksnest;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.List;

public class User {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "TAGG";
    private String username;
    private int numUserRecipes;
    private List<Recipe> userRecipes;
    public User(String name){
        this.username = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

   public List<Recipe> getUserRecipes(){
       CollectionReference collection = db.collection("recipes");
       for(int i = 0; i < numUserRecipes; i++) {
            Recipe myrecipe = (Recipe) collection.document("username" + i).get().getResult().get("recipe");
            //userRecipes.add();
        }
        return null;
    }
}
