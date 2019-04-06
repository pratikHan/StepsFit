package com.datacube.pratikhanchate.fitdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText _name;
    EditText _email;
    EditText _password;
    Button _register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _name=(EditText)findViewById(R.id.editTextName);
        _email=(EditText)findViewById(R.id.editTextEmail);
        _password=(EditText)findViewById(R.id.editTextPassword);
        _register= (Button) findViewById(R.id.btn_signup);


        _register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Register","ONclick");

                String name= _name.getText().toString();
                String email=_email.getText().toString();
                String pass = _password.getText().toString();


            }
        });
    }
}
