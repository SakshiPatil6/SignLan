package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity
{
    TextView sign;
    EditText username,pass;
    Button login;
    String namepattern="^[A-Za-z]\\\\w{5,29}$";

    ProgressDialog pd;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign=findViewById(R.id.signup);
        login=findViewById(R.id.login);
        username=findViewById(R.id.un);
        pass=findViewById(R.id.ed2);
        pd=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PerformLogin();

            }
        });

    }

    private void PerformLogin()
    {
        String un=username.getText().toString();
        String ed2=pass.getText().toString();

        if(!un.matches(namepattern))
        {
            username.setError("Please enter valid username");
        }
        else if (ed2.isEmpty() || ed2.length()<6) {
            pass.setError("Please enter valid password");
        }
        else
        {
            pd.setMessage("Please wait while login in");
            pd.setTitle("login");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            mAuth.signInWithEmailAndPassword(un,ed2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        pd.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this, "Login Successful!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(MainActivity.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
       }

    private void sendUserToNextActivity()
    {
        Intent intent=new Intent(MainActivity.this, JoinCreate.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}