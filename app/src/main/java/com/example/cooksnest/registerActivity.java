package com.example.cooksnest;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerActivity extends AppCompatActivity {
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private static final String TAG = "EmailPassword";
    EditText emailText, usernameText, passwordText, confirmPasswordText;
    ImageView imageView;
    TextView textView, signInButton;
    Button registerButton;
    FirebaseAuth fAuth;
    private SharedPreferences myPrefs;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z]).{8,}";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = findViewById(R.id.emailText);
        usernameText = findViewById(R.id.viewusername);
        passwordText = findViewById(R.id.passwordText);
        confirmPasswordText = findViewById(R.id.confirmPasswordText);
        textView = findViewById(R.id.UsernameView);
        signInButton = findViewById(R.id.signInButton);
        registerButton = findViewById(R.id.registerButton);

        // Initialize Firebase Auth
        fAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), logInActivity.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();
                String confirmPassword = confirmPasswordText.getText().toString().trim();
                String username = usernameText.getText().toString().trim();
                if (username.contains("_")) {
                    usernameText.setError("Usernames cannot have underscores in them.");
                }
                if (TextUtils.isEmpty(email)) {
                    emailText.setError("Please fill in email!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordText.setError("Please fill in password!");
                    return;
                }
                if (!password.matches(confirmPassword)) {
                    confirmPasswordText.setError("Passwords don't match!");
                    return;
                }
                if (!isValidPassword(password)) {
                    passwordText.setError("Please ensure your password is at least 8 characters, contains an uppercase, a lower case and a number.");
                    return;
                }

                db.collection("users").whereEqualTo("username", username).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(getApplicationContext(), "Username already taken",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            firebaseAuthenticator(email, password, username );
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });
    }

    public void firebaseAuthenticator(String email, String password, String username){
        // Register the user in firebase
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && isServicesOK()) {
                    // Sign in success, go to the map
                    Log.d(TAG, "createUserWithEmail:success");
                    Toast.makeText(registerActivity.this, "User Created.", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = fAuth.getCurrentUser();
                    Intent intent = new Intent(registerActivity.this, logInActivity.class);

                    Map<String, Object> map = new HashMap<>();
                    map.put("username", username);
                    map.put("messaged", new ArrayList<String>());

                    db.collection("users").document(email).set(map);


                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    emailText.setError("createUserWithEmail:failure " + task.getException());
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(registerActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
        public boolean isServicesOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(registerActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(registerActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "Cannot make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}