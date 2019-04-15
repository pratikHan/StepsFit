package com.datacube.pratikhanchate.fitdata;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserModel {

    User current_user;
    String user_email;
    DatabaseHelper databaseHelper;
    List<User> userlist;


    public UserModel(String userEmail,DatabaseHelper dbhelper){
        user_email=userEmail;
        databaseHelper=dbhelper;
        getAllUsersInformation();



    }

    public UserModel(){

    }


    public void getAllUsersInformation() {

        userlist=new ArrayList<User>();

        userlist=databaseHelper.getAllUser();

        for(User user : userlist){

            if(user.getEmail().equals(user_email)){
                current_user=user;
            }

        }

        Log.e("USERS", "Current User is "+current_user);
    }

    public User getUserData(String email){

        for(User user : userlist){

            if(user.getEmail().equals(email)){
                return current_user;
            }

        }

        return current_user;
    }

    public void updateUserInformation(User user){

        databaseHelper.updateUser(user);
        Log.d("UserModel","Updated User Information");

    }


    public List<User> retrieveAllUsersData(){


        return userlist;
    }

}
