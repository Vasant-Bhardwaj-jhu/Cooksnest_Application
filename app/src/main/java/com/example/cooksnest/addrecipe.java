package com.example.cooksnest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class addrecipe extends AppCompatActivity {
    private Fragment todo;
    private Fragment done;
    private Fragment stats;
    public ListAdapter hashtagA;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReferenceFromUrl("gs://cooksnest-c462c.appspot.com");
    String TAG = "TAGG";
    public int count = 0;
    String username;
    String imagePath;
    private Set<String> hashtagList = new HashSet<String>();
    private SharedPreferences myPrefs;

    private static final int PICK_IMAGE = 100;

    public ImageButton addImage;
    public Uri imageUri;

    private static final String[] HASHTAGS = new String[] {
            "#french", "#Italian", "#Curry", "#Japanese", "#Spanish", "#Korean", "#American", "#finland", "#Chinese"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrecipe);
        Context context = getApplicationContext();
        Intent intent = getIntent();
        String recipename = intent.getStringExtra("recipename");
        String user = intent.getStringExtra("user");
        int hoursint = intent.getIntExtra("hours", 0);
        int minint = intent.getIntExtra("mins", 0);
        int servingsize = intent.getIntExtra("servingsize", 0);
        String stepsstring = intent.getStringExtra("steps");
        String ingredientsstring = intent.getStringExtra("ingredients");
        ArrayList<String> hashtags = new ArrayList<String>();
        if (intent.getStringArrayListExtra("Hashtags") != null) {
            hashtags.addAll(intent.getStringArrayListExtra("Hashtags"));
        }
        hashtagList.addAll(hashtags);
        hashtagA = new ListAdapter(this.getApplicationContext(), R.layout.listadapter
                , hashtags);
        ListView myHashtags = (ListView) findViewById(R.id.listofhashtags);
        myHashtags.setAdapter(hashtagA);
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        count = myPrefs.getInt("numRecipes", 0);
        Log.d("numrecipes", String.valueOf(count));
        //username = myPrefs.getString("username", null);
        //Log.d("username", username);
        AutoCompleteTextView hashtagview = (AutoCompleteTextView)
                findViewById(R.id.hashtagstextview);
        hashtagview.setThreshold(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, HASHTAGS);

        //TextView listofhash = findViewById(R.id.listofhashtags);
        hashtagview.setAdapter(adapter);
        hashtagview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                if(hashtags.contains(selected)){
                    Toast.makeText(context, "You already added this hashtag!",
                            Toast.LENGTH_SHORT).show();
                    hashtagview.setText("");
                    return;
                }

                int pos = Arrays.asList(HASHTAGS).indexOf(selected);
                Log.d(String.valueOf(pos), selected);
                hashtagview.setText("");
                hashtagList.add(selected);
                hashtagA.add(selected);
                hashtagA.notifyDataSetChanged();
            }
        });
        NumberPicker hour = findViewById(R.id.hours);
        hour.setMinValue(0);
        hour.setMaxValue(23);
        NumberPicker min = findViewById(R.id.min);
        min.setMinValue(0);
        min.setMaxValue(59);
        NumberPicker serve = findViewById(R.id.serving);
        serve.setMinValue(1);
        serve.setMaxValue(10);
        hour.setValue(hoursint);
        min.setValue(minint);
        serve.setValue(servingsize);
        EditText recipe = findViewById(R.id.recipename);
        recipe.setText(recipename);
        TextView usernametext = findViewById(R.id.viewusername);
        usernametext.setText(myPrefs.getString("username", "username"));
        EditText steps = findViewById(R.id.Steps);
        steps.setText(stepsstring);
        EditText ingredients = findViewById(R.id.Ingredients);
        ingredients.setText(ingredientsstring);

        addImage = findViewById(R.id.addimage);

        imagePath = intent.getStringExtra("imagepath");
        if (imagePath != null) {


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl
                    ("gs://cooksnest-c462c.appspot.com");
            StorageReference islandRef = storageRef.child("images/" + imagePath);
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
                    Picasso.get().load(finalLocalFile).into(addImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });


        }


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    public void postrecipe(View view) {
        NumberPicker hour = findViewById(R.id.hours);
        int prepHour = hour.getValue();
        NumberPicker min = findViewById(R.id.min);
        int prepMin = min.getValue();
        NumberPicker serve = findViewById(R.id.serving);
        int serverNum = serve.getValue();
        EditText recipe = findViewById(R.id.recipename);
        String recipeName = recipe.getText().toString();
        EditText ingredients = findViewById(R.id.Ingredients);
        String Ingredients = ingredients.getText().toString();
        EditText steps = findViewById(R.id.Steps);
        String Steps = steps.getText().toString();
        TextView Username = findViewById(R.id.viewusername);
        String user = Username.getText().toString();
        List<String> Hashtags = hashtagA.getHashtags();
        if(imagePath == null){
            imagePath = UUID.randomUUID().toString();
        }
        //Recipe fullRecipe = new Recipe(serverNum, prepHour, prepMin, recipeName,
        //       Ingredients, Steps, null, Username.getText().toString());
        if(recipeName.isEmpty() || Ingredients.isEmpty() || Steps.isEmpty() || user.isEmpty()
                /*imageUri != null*/){
            Toast.makeText(this, "Please fill all parts of the recipe",
                    Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> food = new HashMap<>();
            food.put("recipeName", recipeName);
            food.put("User", user);
            food.put("Hours", prepHour);
            food.put("Minutes", prepMin);
            food.put("servingSize", serverNum);
            food.put("Ingredients", Ingredients);
            food.put("Steps", Steps);
            food.put("Image_Path", imagePath);
            food.put("Hashtags", Hashtags);
            //food.put("recipe", fullRecipe);
            Intent in = getIntent();
            String path = in.getStringExtra("id");
            if (path == null) {
                path = user + count;
            }
// Add a new document with a generated ID
            db.collection("recipes")
                    .document(path).set(food);
            Log.d("imagepath", imagePath);
            uploadImage(imagePath);
            count++;
            SharedPreferences.Editor peditor = myPrefs.edit();
            peditor.putInt("numRecipes", count);
            peditor.apply();
            Intent intent = new Intent(this, Launchscreen.class);
            startActivity(intent);
        }
    }

    public void cancel(View view){
        Intent intent = new Intent(this, Launchscreen.class);
        startActivity(intent);
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            addImage.setImageURI(imageUri);
        }
    }

    private void uploadImage(String imagePath) {
        if (imageUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/" + imagePath);

            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(addrecipe.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                        })
                    .addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(addrecipe.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                            });
        }
    }
}