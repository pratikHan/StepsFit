package com.datacube.pratikhanchate.fitdata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class leaderboardActivity extends AppCompatActivity {

    TextView _txtView;
    User user;
    UserModel userModel;
    String result="";

    List<User> userList;
DatabaseHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        _txtView=(TextView)findViewById(R.id.textViewData);
        userList=new ArrayList<User>();

        Intent _intent =getIntent();
        String email=((Intent) _intent).getStringExtra("EMAIL");
        dbhelper=new DatabaseHelper(this);

        userModel=new UserModel(email,dbhelper);

        userList=userModel.retrieveAllUsersData();

        Log.d("DISPLAY","D1");

        for(User user : userList){
            Log.d("USer List","USERS"+user.getEmail()+"......."+user.getPassword());
            Log.d("USer ListA","Steps"+user.getSteps()+"......."+user.getMilestones());

            if(!user.getName().isEmpty()){
            result= result+"Name : "+user.getName() +"\n"+
                    "Total Steps :"+user.getSteps()+"\n"+
                    "Distance Walked :"+user.getMiles()+"\n"+
                    "Milestones Achieved :"+user.getMilestones()+"\n\n";

            }

        }

        _txtView.setText(result);

    }
}
