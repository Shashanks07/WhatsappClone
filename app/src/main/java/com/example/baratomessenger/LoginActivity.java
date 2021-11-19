package com.example.baratomessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {

    EditText edtusername,edtpassword;
    Button btnlogin,btnsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtusername = findViewById(R.id.edtusername);
        edtpassword = findViewById(R.id.edtpassword);
        btnlogin = findViewById(R.id.btnlogin);
        btnsignup = findViewById(R.id.btnsignup);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this,ChatuserActivity.class);
            startActivity(intent);
            finish();
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtusername.getText().toString();
                String password = edtpassword.getText().toString();
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null){
                            if(user.getBoolean("emailVerified")){
                                FancyToast.makeText(LoginActivity.this,"Logged In Successfully...",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                Intent intent = new Intent(LoginActivity.this,ChatuserActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                ParseUser.logOut();
                                FancyToast.makeText(LoginActivity.this,"Verify your Email...",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            }
                        }else{
                            FancyToast.makeText(LoginActivity.this,e.getMessage(),FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }
                    }
                });
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}