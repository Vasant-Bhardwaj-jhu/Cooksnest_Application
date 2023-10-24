package com.example.cooksnest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link messagelist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class messagelist extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Launchscreen myact;
    Context cntx;
    private ListView userList;


    // TODO: Rename and change types of parameters
    public messagelist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment messagelist.
     */
    // TODO: Rename and change types and number of parameters
    public static messagelist newInstance(String param1, String param2) {
        messagelist fragment = new messagelist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.fragment_messagelist, container, false);
        TextView messageName = myview.findViewById(R.id.username2);
        myact = (Launchscreen) getActivity();
        cntx = getActivity().getApplicationContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        messageName.setText(myPrefs.getString("username", "username") + "'s messages");
        userList = (ListView) myview.findViewById(R.id.userlist);
        myact.cc.notifyDataSetChanged();
        userList.setAdapter(myact.cc);
        Log.d(String.valueOf(myact.cc.getCount()), "CC num");
        return myview;
    }

    @Override
    public void onResume() {
        myact.cc.notifyDataSetChanged();
        myact.userlist.clear();
        String email = fAuth.getCurrentUser().getEmail();
        db.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> saved = (List<String>) document.get("messaged");
                        //Log.d(String.valueOf(saved.size()), "sizeofsaved");
                        if (saved == null){
                            saved = new ArrayList<String>();
                        }
                        for (String rec : saved) {
                            Log.d("adding", rec);
                            myact.userlist.add(rec);
                            myact.cc.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        super.onResume();
    }

}