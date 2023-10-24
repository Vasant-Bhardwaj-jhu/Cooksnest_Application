package com.example.cooksnest;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;

public class Recipe {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://cooksnest-c462c.appspot.com");

    private int servings;
    private int hours;
    private int minutes;
    private String RecipeName;
    private String Ingredients;
    private String Steps;
    private List<String> Hashtags;
    private String Username;
    private String id;
    private String imagepath;

    private Uri imageUri;

    //Default Constructor
    public Recipe(int servings, int hours, int minutes, String RecipeName, String Ingredients,
                  String Steps, List<String> Hashtags, String Username, String id, String imagepath){
        this.servings = servings;
        this.hours = hours;
        this.minutes = minutes;
        this.RecipeName = RecipeName;
        this.Ingredients = Ingredients;
        this.Steps = Steps;
        this.Hashtags = Hashtags;
        this.Username = Username;
        this.id = id;
        this.imagepath = imagepath;
    }

    public Recipe(Map<String, Object> map, String name) {
        this.servings = (int) (long) map.get("servingSize");
        this.hours = (int) (long) map.get("Hours");
        this.minutes = (int) (long) map.get("Minutes");
        this.RecipeName = (String) map.get("recipeName");
        this.Ingredients = (String) map.get("Ingredients");
        this.Steps = (String) map.get("Steps");
        this.Username = (String) map.get("User");
        this.id = name;
        this.imagepath = (String) map.get("Image_Path");
        this.Hashtags = (List<String>) map.get("Hashtags");
        StorageReference ref = storageReference.child("images/" + (String) map.get("Image_Path"));
        imageUri = Uri.parse(ref.getDownloadUrl().toString());
    }

    //Getters
    public int getServings(){
        return this.servings;
    }

    public int getHours(){
        return this.hours;
    }

    public int getMinutes(){
        return this.minutes;
    }

    public String getRecipeName(){
        return this.RecipeName;
    }

    public String getIngredients(){
        return this.Ingredients;
    }

    public String getSteps(){
        return this.Steps;
    }

    public List<String> getHashtags(){
        return this.Hashtags;
    }

    public String getUsername() {
        return Username;
    }

    public String getId() {
        return id;
    }

    public String getImagepath() {
        return imagepath;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    //Setters
    public void setSteps(String steps) {
        this.Steps = steps;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }

    public void setHashtags(List<String> hashtags) {
        Hashtags = hashtags;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setid(String id) {
        this.id = id;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }


    @Override
    public String toString() {
        return "Recipe{" +
                "servings=" + servings +
                ", hours=" + hours +
                ", minutes=" + minutes +
                ", RecipeName='" + RecipeName + '\'' +
                ", Ingredients='" + Ingredients + '\'' +
                ", Steps='" + Steps + '\'' +
                ", Hashtags=" + Hashtags +
                ", Username='" + Username + '\'' +
                '}';
    }
}
