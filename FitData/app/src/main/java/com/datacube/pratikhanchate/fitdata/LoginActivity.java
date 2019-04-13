package com.datacube.pratikhanchate.fitdata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {


    EditText edittextEmail;
    EditText editTextPassword;
    Button buttonLogin;
    TextView signupLink;

    DatabaseHelper databaseHelper;
   // UserModel usermodel;

    private static final int REQUEST_CODE=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edittextEmail=(EditText)findViewById(R.id.editText);
        editTextPassword=(EditText)findViewById(R.id.editText2);
        buttonLogin=(Button)findViewById(R.id.button);
        signupLink=(TextView)findViewById(R.id.textView);

        databaseHelper=new DatabaseHelper(this.getApplicationContext());



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Login", "Login button pressed");

                if(databaseHelper.checkUser(edittextEmail.getText().toString().trim(),editTextPassword.getText().toString().trim())){

                    Log.e("LOGIN","Login Successfull");

                  //  usermodel=new UserModel(edittextEmail.getText().toString(),databaseHelper);
                    Intent intent = new Intent(getApplicationContext(), StepCounter.class);
                    intent.putExtra("EMAIL_ID",""+edittextEmail.getText().toString());
                   // intent.putExtra("DBOBJ", (Serializable) databaseHelper);
                    startActivity(intent);
                }else{
                    Log.e("Login","No USer Found");
                }
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Register", "Register button pressed");

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE){
            this.finish();
        }
    }

    public void login(){

        if(!validate()){
            onLoginFailed();
            return;
        }

        edittextEmail.setEnabled(false);

        String email =edittextEmail.getText().toString();
        String password = editTextPassword.getText().toString();



        authenticate();


    }

    private void authenticate() {

        Toast.makeText(getBaseContext(), "Further Authentication", Toast.LENGTH_LONG).show();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }


    public boolean validate() {
        boolean valid = true;

        String email = edittextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edittextEmail.setError("enter a valid email address");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            editTextPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }

}
