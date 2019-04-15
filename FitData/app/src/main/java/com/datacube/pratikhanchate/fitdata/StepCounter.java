package com.datacube.pratikhanchate.fitdata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.datacube.pratikhanchate.fitdata.locationModule.AppLocationService;
import com.datacube.pratikhanchate.fitdata.locationModule.LocationAddress;


import java.util.Date;
import java.util.Calendar;



public class StepCounter extends AppCompatActivity {

    TextView username;
    TextView currentSteps;
    TextView idleTime;
    TextView milestones_achieved;
    TextView totalSteps;
    TextView currentLocation;
    Button leaderboard;
    Boolean isServiceStopped;
    Date current_time;

    UserModel userModel;
    DatabaseHelper dbhelper;
    String email_id;

    int milestones=0;
    int _steps=0;
    int _totalsteps=0;

    AppLocationService appLocationService;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        username=(TextView)findViewById(R.id.txtUserName);
        currentSteps=(TextView)findViewById(R.id.txtcurrentSteps);
       // idleTime=(TextView)findViewById(R.id.txtidletime);
        milestones_achieved=(TextView)findViewById(R.id.txtmilestones);
        totalSteps=(TextView)findViewById(R.id.txttotalsteps);
        currentLocation=(TextView)findViewById(R.id.txtlocation);

        Intent prev_intent=getIntent();
        dbhelper=new DatabaseHelper(this);
        email_id=prev_intent.getStringExtra("EMAIL_ID");
        userModel=new UserModel(email_id,dbhelper);

        leaderboard=(Button)findViewById(R.id.btnleaderboard);
        current_time=Calendar.getInstance().getTime();


        getlocationService();
        Log.d("StepCounter", "Current Date :"+current_time.toString());

        initViews();



        intent = new Intent(this, StepCountingService.class);

        init();



        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), leaderboardActivity.class);
                intent.putExtra("EMAIL",email_id);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!isServiceStopped) {


            updateInDatabase();
            // call unregisterReceiver - to stop listening for broadcasts.
            unregisterReceiver(broadcastReceiver);
            // stop Service.
            stopService(new Intent(getBaseContext(), StepCountingService.class));
            isServiceStopped = true;
        }
    }

    void init(){

        isServiceStopped=true;

        startService(new Intent(getBaseContext(), StepCountingService.class));
        // register our BroadcastReceiver by passing in an IntentFilter. * identifying the message that is broadcasted by using static string "BROADCAST_ACTION".
        registerReceiver(broadcastReceiver, new IntentFilter(StepCountingService.BROADCAST_ACTION));
        isServiceStopped = false;
    }

    private BroadcastReceiver broadcastReceiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateViews(intent);
        }
    };

    private void updateViews(Intent intent) {
        // retrieve data out of the intent.

        String b_counted_steps=String.valueOf(intent.getStringExtra("Counted_Step"));
        String b_steps=String.valueOf(intent.getStringExtra("Detected_Step"));

        int final_steps=Integer.parseInt(b_steps)+_steps;

         if(final_steps != Integer.parseInt(currentSteps.getText().toString())){
             isMilestoneAchieved(final_steps);
             currentSteps.setText(""+final_steps);
         }



    }


    public void isMilestoneAchieved(int steps){

        double feet = steps*2.5;

        if(feet % 100 == 0 && feet != 0){
            milestones=milestones+1;
            Log.d("Milestones Achieved","Miles"+milestones);
            milestones_achieved.setText(""+milestones);
        }

        convertToMiles(steps);
    }

    public void initViews(){
        User user= userModel.getUserData(email_id);

        Log.e("DEBUG","X11"+user.getName()+" ..Steps"+user.getSteps());



            currentSteps.setText(""+0);
            totalSteps.setText(""+0);


            username.setText(user.getName());
            currentSteps.setText(user.getSteps());
          //  idleTime.setText(user.getTotal_idleTime());
            milestones_achieved.setText(user.getMilestones());
            totalSteps.setText(user.getMiles());
            milestones= Integer.parseInt(milestones_achieved.getText().toString());




            if(user.getMilestones().isEmpty() || user.getSteps().isEmpty()){

                    Log.e("DEBUG","X1");
                    _steps=0;
                    currentSteps.setText(""+_steps);
                    milestones_achieved.setText(""+0);
                }else{
                    _steps =Integer.parseInt(currentSteps.getText().toString());

            }



    }




    public void convertToMiles(int steps){

        double miles = ((double)steps/2112);
        String _miles=""+miles;
        String res=_miles.substring(0,5);
        totalSteps.setText(""+res+"miles");

        }

    private void updateInDatabase(){
        User user =new User();
        user.setName(username.getText().toString());
        user.setSteps(currentSteps.getText().toString());
        user.setMilestones(milestones_achieved.getText().toString());
      //  user.setTotal_idleTime(idleTime.getText().toString());
        user.setLocation(currentLocation.getText().toString());
        user.setMiles(totalSteps.getText().toString());


        userModel.updateUserInformation(user);



    }

    private void getlocationService(){

        //for location Service...
        appLocationService=new AppLocationService(StepCounter.this);
        Location location = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());
        } else {

            Log.e("StepsCounter","LocationService");
            showSettingsAlert();
        }

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }


            currentLocation.setText(locationAddress);


            Log.e("StepsCounter","Location Address"+locationAddress);
        }
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StepCounter.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        StepCounter.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }



}
