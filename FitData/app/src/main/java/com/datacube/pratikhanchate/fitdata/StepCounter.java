package com.datacube.pratikhanchate.fitdata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
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
    Date current_date;

    UserModel userModel;
    DatabaseHelper dbhelper;
    String email_id;

    int milestones=0;
    int _steps=0;
    int _totalsteps=0;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        username=(TextView)findViewById(R.id.txtUserName);
        currentSteps=(TextView)findViewById(R.id.txtcurrentSteps);
        idleTime=(TextView)findViewById(R.id.txtidletime);
        milestones_achieved=(TextView)findViewById(R.id.txtmilestones);
        totalSteps=(TextView)findViewById(R.id.txttotalsteps);
        currentLocation=(TextView)findViewById(R.id.txtlocation);

        Intent prev_intent=getIntent();
        dbhelper=new DatabaseHelper(this);

        email_id=prev_intent.getStringExtra("EMAIL_ID");

        userModel=new UserModel(email_id,dbhelper);

        initViews();



        leaderboard=(Button)findViewById(R.id.btnleaderboard);

        current_date=Calendar.getInstance().getTime();

        Log.d("Date", "Current Date :"+current_date.toString());

        intent = new Intent(this, StepCountingService.class);

        init();



        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), leaderboardActivity.class);
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
     //  currentSteps.setText( b_counted_steps);

        String b_steps=String.valueOf(intent.getStringExtra("Detected_Step"));




         int final_steps=Integer.parseInt(b_steps)+_steps;

         if(final_steps != Integer.parseInt(currentSteps.getText().toString())){
             isMilestoneAchieved(final_steps);
             currentSteps.setText(""+final_steps);
         }


        Log.d("UPDATE VIEWS", "Current :"+b_counted_steps +"Total Steps :"+b_steps);

        Log.d("UPDATE VIEWS", "CALCULATED:"+_steps);




        totalSteps.setText(""+final_steps);




      //  stepCountTxV.setText('"' + String.valueOf(countedStep) + '"' + " Steps Counted");
       // stepDetectTxV.setText("Steps Detected = " + String.valueOf(DetectedStep) + '"');


    }


    public void isMilestoneAchieved(int steps){

        if(steps % 10 == 0 && steps != 0){
            milestones=milestones+1;
            Log.d("Milestones Achieved","Miles"+milestones);
            milestones_achieved.setText(""+milestones);
        }

        convertToKms(steps);
    }

    public void initViews(){
        User user= userModel.getUserData(email_id);

        Log.e("DEBUG","X11"+user.getName()+" ..Steps"+user.getSteps());



            currentSteps.setText(""+0);


            username.setText(user.getName());
            currentSteps.setText(user.getSteps());
            idleTime.setText(user.getTotal_idleTime());
            milestones_achieved.setText(user.getMilestones());
            totalSteps.setText(user.getSteps());
            milestones= Integer.parseInt(milestones_achieved.getText().toString());




            if(user.getMilestones().isEmpty() || user.getSteps().isEmpty()){

                    Log.e("DEBUG","X1");
                    _steps=0;
                    currentSteps.setText(""+_steps);
                    milestones_achieved.setText(""+0);
                }else{
                    _steps =Integer.parseInt(currentSteps.getText().toString());

            }





        //currentLocation;




    }


    public void convertToKms(int steps){

        double kilometers = (double)steps/1312.000;
        double feet = steps*2.5;

        DecimalFormat df=new DecimalFormat("0.00000");

        Log.e("Kilometers","KMVal :"+df.format(kilometers) +"  FEETS"+feet)   ;




    }

    private void updateInDatabase(){
        User user =new User();
        user.setName(username.getText().toString());
        user.setSteps(currentSteps.getText().toString());
        user.setMilestones(milestones_achieved.getText().toString());
        user.setTotal_idleTime(idleTime.getText().toString());
        user.setLocation(currentLocation.getText().toString());


        userModel.updateUserInformation(user);



    }
}
