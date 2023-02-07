package com.example.mvp_application.presenter;

public interface NumberContract {
    interface View{
        void updateText(int number);
        void showNotification(String notification);
    }

    interface Presenter{
        void handleNumberIsTen(int number);
        void runMThread();
        void stopMThread();
    }
}
