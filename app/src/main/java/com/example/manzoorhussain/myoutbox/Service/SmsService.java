package com.example.manzoorhussain.myoutbox.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.manzoorhussain.myoutbox.Objects.User;
import com.example.manzoorhussain.myoutbox.Session.MyClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SmsService extends Service {

    public static final int notify = 2500;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    Intent myService;
    int index = 0;
    int lenght = 0;
    List<User.Sheet1Object> users;
    Date lastTime = new Date();
    MyClass myClass;

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    Calendar cal;


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {


        myClass = (MyClass) getApplicationContext();
        users = myClass.getSheet1Objects();
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new

        cal = Calendar.getInstance();
        String mLastTime = dateFormat.format(cal.getTime());
        try {
            lastTime = dateFormat.parse(mLastTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        myService = new Intent(getApplicationContext(), SmsService.class);
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 1000, notify);   //Schedule task

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
    }


    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast

                    if (index < users.size()) {
                        sendSMSMessage();
                    } else {
                        stopService(new Intent(getApplicationContext(), SmsService.class));
                        Toast.makeText(getApplicationContext(),
                                "SMS Sending End..",
                                Toast.LENGTH_LONG).show();
                    }
                    index++;
                }
            });
        }
    }

    protected void sendSMSMessage() {

        String phoneNo = String.valueOf(users.get(index).getMobile_no());
        String message = users.get(index).getMesasege();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, "Dear. " + users.get(index).getName() + "\n" + message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}