package com.datacube.pratikhanchate.fitdata;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    EditText _name;
    EditText _email;
    EditText _password;
    Button _register;

    DatabaseHelper databaseHelper;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _name=(EditText)findViewById(R.id.editTextName);
        _email=(EditText)findViewById(R.id.editTextEmail);
        _password=(EditText)findViewById(R.id.editTextPassword);
        _register= (Button) findViewById(R.id.btn_signup);

        databaseHelper= new DatabaseHelper(this);


        _register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Register","ONclick");

                user =new User();

                if (authenticate()){
                databaseHelper.addUser(user);
                clearTexts();
                getAllUsersInformation();

                    Toast.makeText(getApplicationContext(),"Congrats!, You have been registered successfully!",Toast.LENGTH_LONG).show();

                   Intent intent= new Intent(getApplicationContext(),LoginActivity.class);
                   startActivity(intent);
                }





            }
        });





    }


    public boolean authenticate(){



        if(_name.getText().toString().isEmpty() || _email.getText().toString().isEmpty() || _password.getText().toString().isEmpty() ){

            Toast.makeText(getApplicationContext(),"Please fill all the details",Toast.LENGTH_LONG).show();

            return false;
        }

        if (!isValidEmail(_email.getText().toString())){

            Toast.makeText(getApplicationContext(),"Please enter valid Email Address",Toast.LENGTH_LONG).show();

            return false;

        }

        user.setName(_name.getText().toString());
        user.setEmail(_email.getText().toString());
        user.setPassword(_password.getText().toString());



        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void getAllUsersInformation() {

        List<User> userlist=new ArrayList<User>();

        userlist=databaseHelper.getAllUser();

        for(User user : userlist){
            Log.e("USERS", "Users are "+user.getName());
        }


    }

    public void clearTexts(){
        _name.setText(null);
        _email.setText(null);
        _password.setText(null);
    }
}
