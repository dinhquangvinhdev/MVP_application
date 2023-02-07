package com.example.mvp_application.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerListener();
        initPresenter();

    }

    private void initPresenter() {
        if(numberPresenter == null){
            numberPresenter = new NumberPresenter();
            numberPresenter.setView(this);
        }
    }

    private void registerListener() {
        binding.btnClick.setOnClickListener(this);
    }

    @Override
    public void updateText(int number) {
        binding.tvNumber.setText(String.valueOf(number));
    }

    @Override
    public void showNotification(String notification) {
        Toast.makeText(this ,notification , Toast.LENGTH_SHORT).show();
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
            default:
                Log.e("bibi", "wrong id catch listener in MainActivity");
                break;
        }
    }
}