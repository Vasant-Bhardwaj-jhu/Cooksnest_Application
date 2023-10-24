package com.example.cooksnest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.daimajia.swipe.SwipeLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link browse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class browse extends Fragment {
    public browse() {
        // Required empty public constructor
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Context cntx;
    private Launchscreen myact;
    protected static ArrayList<Recipe> recipelist;
    Recipe showing;
    private Fragment browse;
    private FragmentTransaction transaction;
    Boolean empty;
    public static List<String> filters = new ArrayList<>();
    private static final String[] FILTERS = new String[] {
            "#french", "#Italian", "#Curry", "#Japanese", "#Spanish", "#Korean", "#American", "#finland"
    };
    public ListAdapter filtersAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment browse.
     */
    // TODO: Rename and change types and number of parameters
    public static browse newInstance(String param1, String param2) {
        browse fragment = new browse();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View myView = inflater.inflate(R.layout.fragment_browse, container, false);
        myact = (Launchscreen) getActivity();
        cntx = getActivity().getApplicationContext();
        Context context = getContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //Toast.makeText(context, "CV reached", Toast.LENGTH_SHORT).show();
        SwipeLayout swipeLayout = (SwipeLayout) myView.findViewById(R.id.swipelay);
        FloatingActionButton filterRecipes = myView.findViewById(R.id.filterBrowseRecipes);

        recipelist = myact.allRecipes;
        //filters.add("#french");

        Log.d("LISTSIZE", String.valueOf(recipelist.size()));

        filterRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View popupView = inflater.inflate(R.layout.popup_filter, null);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = false; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                ImageButton exit = popupView.findViewById(R.id.exit_completed);
                Button filterApply = popupView.findViewById(R.id.filterApplication2);
                ArrayList<String> filteredTags = new ArrayList<String>();
                filters.addAll(filteredTags);
                filtersAdapter = new ListAdapter(cntx, R.layout.listadapter, (ArrayList<String>) filters);
                Log.d(String.valueOf(filtersAdapter.getCount()), "count of filtersAdapter");
                ListView myFilters = (ListView) popupView.findViewById(R.id.filtersList);
                myFilters.setAdapter(filtersAdapter);
                filtersAdapter.notifyDataSetChanged();
                AutoCompleteTextView filtersView = (AutoCompleteTextView) popupView.findViewById
                        (R.id.filtersAutoComplete);
                filtersView.setThreshold(0);
                popupWindow.setFocusable(true);
                popupWindow.update();
                //filtersView.setText("text");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (cntx, android.R.layout.simple_dropdown_item_1line, FILTERS);
                filtersView.setAdapter(adapter);
                String filterString = filters.toString();
                filterString = filterString.substring(1, filterString.length() - 1);
                String fullFiltersText = "Filters: " + filterString;
                filtersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("HI", "FROM ONITEMCLICK");
                        String selected = (String) parent.getItemAtPosition(position);
                        if (filters.contains(selected)) {
                            Toast.makeText(cntx, "You already added this hashtag!",
                                    Toast.LENGTH_SHORT).show();
                            filtersView.setText("");
                            return;
                        }
                        int pos = Arrays.asList(FILTERS).indexOf(selected);
                        Log.d(String.valueOf(pos), selected);
                        filtersView.setText("");
                        filters.add(selected);
                        filtersAdapter.notifyDataSetChanged();
                    }
                });
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 100);
                exit.setOnClickListener(view -> popupWindow.dismiss());
                filterApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filtersAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                        updateFilters();
                    }
                });
            }
        });


//set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

//add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, myView.findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int rightOffset, int topOffset) {
                //you are swiping.
                Log.d("Statistics Left:", String.valueOf(rightOffset));
                Log.d("Statistics Top:", String.valueOf(topOffset));
                //Toast.makeText(context, "you are swiping", Toast.LENGTH_SHORT).show();
                if (rightOffset > 400) {
                    if (!empty) {
                        myact.savedRecipes.add(showing);
                        transaction = getFragmentManager().beginTransaction();
                        browse = new browse();
                        transaction.replace(R.id.fragment_container, browse);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        Toast.makeText(cntx, "Saved "
                                + showing.getRecipeName(), Toast.LENGTH_SHORT).show();
                        saveRecipe(showing.getId());
                    } else {
                        Toast.makeText(cntx, "Please wait until more recipes are posted", Toast.LENGTH_SHORT).show();
                    }
                }
                if (rightOffset < -400) {
                    transaction = getFragmentManager().beginTransaction();
                    browse = new browse();
                    transaction.replace(R.id.fragment_container, browse);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                swipeLayout.close();
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
                Log.d("X-Val:", String.valueOf(xvel));
                Log.d("Y-Val:", String.valueOf(yvel));
                //Toast.makeText(context, "you've swiped", Toast.LENGTH_SHORT).show();
                swipeLayout.close();
            }
        });


        return myView;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        View myView = getView();
        if (recipelist.size() != 0) {
            Log.d("IF", "REACHED IF");
            empty = false;
            showing = recipelist.get(0);
            myact.allRecipes.remove(0);
            ImageView image = myView.findViewById(R.id.viewimage);
            TextView recipename = myView.findViewById(R.id.recipenameview);
            TextView username = myView.findViewById(R.id.viewusername);
            username.setOnClickListener(new View.OnClickListener() {
                    Launchscreen myact = (Launchscreen) getActivity();

                @Override
                public void onClick(View v) {
                    Toast.makeText(cntx, "Clicked Name", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this.myact, otheruser_profile.class);
                    String usernamestring = username.getText().toString().substring(4);
                    intent.putExtra("username", usernamestring);
                    startActivity(intent);
                }
            });
            TextView preptime = myView.findViewById(R.id.preptimeview);
            TextView serving = myView.findViewById(R.id.servingview);
            TextView ingredients = myView.findViewById(R.id.ingredientsview);
            TextView Steps = myView.findViewById(R.id.stepsview);
            TextView hashtagview = myView.findViewById(R.id.hashtagbrowse);
            recipename.setText(showing.getRecipeName());
            String HASHTAGLIST = showing.getHashtags().toString();
            HASHTAGLIST = HASHTAGLIST.substring(1, HASHTAGLIST.length() - 1);
            hashtagview.setText(HASHTAGLIST);
            SpannableString content = new SpannableString("By: " + showing.getUsername());
            content.setSpan(new UnderlineSpan(), 4, content.length(), 0);

            username.setText(content);
            preptime.setText("Cook Time:" + showing.getHours() + "hr " + showing.getMinutes() + "min");
            serving.setText("Serves: " + showing.getServings());
            ingredients.setText("Ingredients\n" + showing.getIngredients());
            Steps.setText("Steps\n" + showing.getSteps());
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl
                    ("gs://cooksnest-c462c.appspot.com");
            StorageReference islandRef = storageRef.child("images/" + showing.getImagepath());
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
        } else {
            Log.d("IF", "REACHED ELSE");
            empty = true;
            TextView recipename = myView.findViewById(R.id.recipenameview);
            recipename.setText("No more recipe to browse");
            TextView username = myView.findViewById(R.id.viewusername);
            username.setText("");
            TextView preptime = myView.findViewById(R.id.preptimeview);
            preptime.setText("");
            TextView serving = myView.findViewById(R.id.servingview);
            serving.setText("");
            TextView ingredients = myView.findViewById(R.id.ingredientsview);
            ingredients.setText("");
            TextView Steps = myView.findViewById(R.id.stepsview);
            Steps.setText("");
        }
    }

    public void saveRecipe(String id) {
        String email = fAuth.getCurrentUser().getEmail();
        Log.d("SAVE", email);
        db.collection("users").document(email).get().addOnCompleteListener
                (new OnCompleteListener<DocumentSnapshot>() {
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
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    private void updateFilters() {
        recipelist.clear();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(filters.isEmpty()){
            db.collection("recipes").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(!document.getData().get("User").equals(myPrefs.getString
                                            ("username", ""))){
                                        Log.d("Update Filters",
                                                document.getId() + " => " + document.getData());
                                        recipelist.add(new Recipe(document.getData(), document.getId()));
                                    }

                                }
                                Log.d("LISTSIZE", String.valueOf(recipelist.size()));
                                onResume();
                            } else {
                                //Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            return;
        }
        db.collection("recipes").whereArrayContainsAny("Hashtags", filters).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!document.getData().get("User").equals(myPrefs.getString
                                        ("username", ""))){
                                    Log.d("Update Filters",
                                            document.getId() + " => " + document.getData());
                                    recipelist.add(new Recipe(document.getData(), document.getId()));
                                }

                            }
                            Log.d("LISTSIZE", String.valueOf(recipelist.size()));
                            onResume();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}