package com.example.mvp_application.view;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvp_application.R;
import com.example.mvp_application.databinding.ActivityMainBinding;
import com.example.mvp_application.model.MConst;
import com.example.mvp_application.presenter.NumberContract;
import com.example.mvp_application.presenter.NumberPresenter;

public class MainActivity extends AppCompatActivity implements NumberContract.View, View.OnClickListener {

    private ActivityMainBinding binding;
    private NumberPresenter numberPresenter;
    private boolean status;
    private static final String CHANNEL_ID = "TEST_CHANNEL";
    private static final CharSequence CHANNEL_NAME = "MY_CHANNEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerListener();
        initPresenter();
        registerBroadcast();
    }

    @Override
    protected void onDestroy() {
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MConst.ACTION_NOTIFICATION_BUTTON_CLICK);
        registerReceiver(receiver ,intentFilter);
    }

    private void initPresenter() {
        if(numberPresenter == null){
            numberPresenter = new NumberPresenter();
            numberPresenter.setView(this);
        }
    }

    private void registerListener() {
        binding.btnClick.setOnClickListener(this);
        binding.btnNotification.setOnClickListener(this);
    }

    @Override
    public void updateText(int number) {
        binding.tvNumber.setText(String.valueOf(number));
    }

    @Override
    public void toastNotification(String notification) {
        Toast.makeText(this ,notification , Toast.LENGTH_SHORT).show();
//        this.showNotification();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_click:
                if(status){
                    status = false;
                    numberPresenter.handleNumberIsTen(Integer.parseInt(binding.tvNumber.getText().toString()));
                    numberPresenter.stopMThread();
                    binding.btnClick.setText("Click To Run");
                }else{
                    status = true;
                    //run number and increase it
                    numberPresenter.runMThread();
                    binding.btnClick.setText("Click To Stop");
                }
                break;
            case R.id.btn_notification:
                showNotification();
                break;
            default:
                Log.e("bibi", "wrong id catch listener in MainActivity");
                break;
        }
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID ,
                    CHANNEL_NAME,
                    importance);
            channel.setDescription("This is body i guest");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private PendingIntent onButtonNotificationClick(@IdRes int id){
        Intent intent = new Intent(MConst.ACTION_NOTIFICATION_BUTTON_CLICK);
        intent.putExtra("Extra_button_click", id);
        return PendingIntent.getBroadcast(this , id, intent , PendingIntent.FLAG_ONE_SHOT);
    }

    private void showNotification(){
        //create remote view for set custom file xml for notification
        RemoteViews notificationLayout =
                new RemoteViews(getPackageName(), R.layout.layout_notification);
        //register on click for button
        notificationLayout.setOnClickPendingIntent(R.id.btnAccept,
                onButtonNotificationClick(R.id.btnAccept));
        notificationLayout.setOnClickPendingIntent(R.id.btnDenied,
                onButtonNotificationClick(R.id.btnDenied));
        //create notification
        Notification notification = new NotificationCompat.Builder(this , CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(notificationLayout)
                .build();
        //create channel for notification
        createNotificationChannel();
        //get notification manager and notify my custom notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1 , notification);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //get id button click (it was send from onButtonNotificationClick)
            int id = intent.getIntExtra("Extra_button_click", -1);
            switch (id){
                case R.id.btnAccept:
                    Toast.makeText(getApplicationContext() ,"Accept" , Toast.LENGTH_LONG).show();
                    break;
                case R.id.btnDenied:
                    Toast.makeText(getApplicationContext() ,"Denied" , Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
}