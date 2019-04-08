package com.datacube.pratikhanchate.fitdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

                user=new User();


                user.setName(_name.getText().toString());
                user.setEmail(_email.getText().toString());
                user.setPassword(_password.getText().toString());

                databaseHelper.addUser(user);

                clearTexts();
                getAllUsersInformation();


            }
        });





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
