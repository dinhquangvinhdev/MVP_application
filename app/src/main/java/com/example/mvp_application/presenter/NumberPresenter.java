package com.example.mvp_application.presenter;

import android.util.Log;

import com.example.mvp_application.model.MConst;

public class NumberPresenter implements NumberContract.Presenter{

    private NumberContract.View view;
    private MLooperThread mLooperThread;

    public void setView(NumberContract.View view){
        this.view = view;
    }

    @Override
    public void handleNumberIsTen(int number) {
        //get model in here but in this example it do not need it
        if(number == 10){
            view.toastNotification("Congratulation");
        }
        view.toastNotification("Failed\nmuahahahaha");
    }

    @Override
    public void runMThread() {
        mLooperThread = new MLooperThread();
        mLooperThread.start();
    }

    @Override
    public void stopMThread() {
        if (mLooperThread.isAlive()) {
            mLooperThread.stopThread();
        }
        MConst.resetNumber();
        view.updateText(MConst.NUMBER);
    }

    private class MLooperThread extends Thread{
        private Boolean isRunning = true;

        public void run() {
            while (isRunning){
                try {
                    Log.d("bibi", "going here");
                    view.updateText(MConst.getAndIncreaseNUMBER());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isRunning = false;
                }
            }
        }

        public void stopThread(){
            isRunning = false;
            interrupt();
        }
    }
}
