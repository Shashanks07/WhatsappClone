package com.example.baratomessenger;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignupActivity extends AppCompatActivity {

    EditText edtnewusername,edtnewemail,edtnewpassword;
    Button btnnewaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtnewusername = findViewById(R.id.edtnewusername);
        edtnewemail = findViewById(R.id.edtnewemail);
        edtnewpassword = findViewById(R.id.edtnewpassword);
        btnnewaccount = findViewById(R.id.btnnewaccount);

        btnnewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ParseUser user = new ParseUser();
                    user.setUsername(edtnewusername.getText().toString());
                    user.setEmail(edtnewemail.getText().toString());
                    user.setPassword(edtnewpassword.getText().toString());

                    if (edtnewusername.getText().toString() != "" && edtnewemail.getText().toString() != "" && edtnewpassword.getText().toString() != "") {
                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParseUser.logOut();
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(SignupActivity.this)
                                            .setTitle("New User Created")
                                            .setMessage("You need to verify your Email before Log In")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                    dialog.create().show();
                                    dialog.setCancelable(false);
                                } else {
                                    FancyToast.makeText(SignupActivity.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                }
                            }
                        });

                    }else {
                        FancyToast.makeText(SignupActivity.this,"Username/Email/Password is Missing..!",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}