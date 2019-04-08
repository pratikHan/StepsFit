package com.datacube.pratikhanchate.fitdata;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "UserManager.db";
    private static final int DATABASE_VERSION=1;
    private static  final String DATABASE_TABLE= "table_user";
    SQLiteDatabase db;


    //user columns
    static final String USER_ID="user_id";
    static final String USER_NAME="name";
    static final String EMAIL="email";
    static final String CONTACT_NUMBER="phoneNumber";
    static final String PASSWORD="password";
    static final String STEPS="steps";
    static final String IDLE_TIME="total_idleTime";
    static final String LOCATION_HOME="Location_home";
    static final String LOCATION_OFFICE="Location_office";


    private String CREATE_USER_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
            + USER_ID + " TEXT,"
            + USER_NAME + " TEXT,"
            + EMAIL + " TEXT,"
            + CONTACT_NUMBER + "TEXT,"
            + PASSWORD + " TEXT,"
            + STEPS + "TEXT,"
            + IDLE_TIME +"TEXT,"
            + LOCATION_HOME + "TEXT,"
            + LOCATION_OFFICE + "TEXT"
            + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + DATABASE_TABLE;

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context){

        if(sInstance==null){
            sInstance= new DatabaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }

   public DatabaseHelper(Context context){

       super(context,DATABASE_NAME,null, DATABASE_VERSION);
       Log.e("DB","Oncreate1");
       db=getWritableDatabase();
   }

    @Override
    public void onCreate(SQLiteDatabase db) {

       Log.e("DB","Oncreate");
       try {
           db.execSQL(CREATE_USER_TABLE);
           Log.e("DB", "TABLE created successfully");
       }

       catch (Exception e){
           Log.e("Exceptions",e.getMessage());
       }




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       db.execSQL(DROP_USER_TABLE);
       onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, user.getName());
        values.put(EMAIL, user.getEmail());
        values.put(PASSWORD, user.getPassword());

        /*  values.put(CONTACT_NUMBER,user.getPhoneNumber());
        values.put(STEPS,user.getSteps());
        values.put(IDLE_TIME,user.getTotal_idleTime());
        values.put(LOCATION_HOME,user.getLocation_home());
        values.put(LOCATION_OFFICE,user.getLocation_office());*/

        // Inserting Row
        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }



    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(DATABASE_TABLE, USER_NAME + " = ?",
                new String[]{String.valueOf(user.getName())});
        db.close();
    }


    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                USER_NAME,
                EMAIL,
                PASSWORD
        };
        // sorting orders
        String sortOrder =
                USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(DATABASE_TABLE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }



    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                USER_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = EMAIL + " = ?" + " AND " + PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(DATABASE_TABLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

}
