package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Dialog.AddResetPasswordDialog;
import com.example.finalproject.DataClass.Project;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    private EditText emailTextField;
    private EditText passwordTextField;
    private TextView txt_signUp, txt_forgotPassword;
    private FirebaseAuth mAuth;
    private CheckBox cb_rememberMe;
    ArrayList<String> ProjectNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        txt_signUp = (TextView) findViewById(R.id.txt_signUp);
        emailTextField = findViewById(R.id.login_email);
        passwordTextField = findViewById(R.id.login_password);
        cb_rememberMe = (CheckBox) findViewById(R.id.cb_rememberMe);
        txt_forgotPassword = (TextView) findViewById(R.id.txt_forgotPassword);
        txt_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddResetPasswordDialog reset = new AddResetPasswordDialog(Login.this);
                reset.show();
            }
        });
        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUp();
            }
        });


        //sign in
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    private void goToSignUp(){
        Intent intent = new Intent(this,Signup.class);
        startActivity(intent);
    }

    public void signIn(View view) {
        emailTextField = findViewById(R.id.login_email);
        passwordTextField = findViewById(R.id.login_password);

        String email = emailTextField.getText().toString().trim();
        String password = passwordTextField.getText().toString().trim();
        if (email.isEmpty()){
            emailTextField.setError(getString(R.string.requiredEmail));
            emailTextField.requestFocus();
        }
        else if (password.isEmpty()){
            passwordTextField.setError(getString(R.string.requiredPassword));
            passwordTextField.requestFocus();
        }
        else {

            //if RememberMe checkbox is checked -> store email, password by Paper.book()
            if(cb_rememberMe.isChecked()){
                Paper.book().write(SyncingUser.userEmail, email);
                Paper.book().write(SyncingUser.userPassword, password);
            }


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("signin", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                updateUI(null);
                            }
                        }
                    });
        }
    }
    public void updateUI(FirebaseUser account){
        ProjectNames= new ArrayList<String>();
        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra("projects",ProjectNames);
        if(account != null){
            Toast.makeText(this,getString(R.string.signInSuccessful),Toast.LENGTH_LONG).show();
            startActivity(intent);
        }else {
            Toast.makeText(this,getString(R.string.wrongAccount),Toast.LENGTH_LONG).show();
        }
    }

    ArrayList<String> getProjectFromUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<String> projectNamesArray = new ArrayList<String>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid()).child("projects");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Project project = dsp.getValue(Project.class);
                    projectNamesArray.add(project.getProjectName()); //add result into array list
                }
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("FetchError", "loadPost:onCancelled", databaseError.toException());
            }
        };
        reference.addValueEventListener(postListener);
        return projectNamesArray;
    }
}