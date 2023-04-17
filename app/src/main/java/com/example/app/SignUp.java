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


public class SignUp extends AppCompatActivity {

    TextView login;
    EditText username,pass,confirm_pass, email, mobile_number;
    Button signup;
    String namepattern="^[A-Za-z]\\\\w{5,29}$";
    String pattern_email= "^([a-zA-Z\\d_\\-]+)@([a-zA-Z\\d_\\-]+)\\\\.([a-zA-Z]{2,5}) ";
    ProgressDialog pd;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.already);
        username=findViewById(R.id.un);
        pass=findViewById(R.id.edp);
        confirm_pass=findViewById(R.id.ed_cp);
        email=findViewById(R.id.em);
        mobile_number=findViewById(R.id.mob);
        pd=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this,MainActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                PerformAuthentication();
            }
        });
    }

    private void PerformAuthentication()
    {
        String un=username.getText().toString();
        String pas=pass.getText().toString();
        String cfp=confirm_pass.getText().toString();
        String ema=email.getText().toString();
        String mobi=mobile_number.getText().toString();

        if(!un.matches(namepattern))
        {
            username.setError("Please enter valid username");
        }
        else if (pas.isEmpty() || pas.length()<6)
        {
            pass.setError("Please enter valid password");
        } else if (!pas.equals(cfp) )
        {
            confirm_pass.setError("Password doesn't match both fields");
        } else if (!ema.isEmpty() || ema.matches(pattern_email))
        {
            email.setError("Please enter valid email");
        } else if (mobi.length() != 0)
        {
            mobile_number.setError("Please enter valid mobile number");
        }

        else
        {
            pd.setMessage("Please wait while registering");
            pd.setTitle("login");
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            mAuth.createUserWithEmailAndPassword(un , pas).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        pd.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(SignUp.this, "Registration Successful!",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        pd.dismiss();
                        Toast.makeText(SignUp.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserToNextActivity()
    {
        Intent intent=new Intent(SignUp.this, JoinCreate.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}