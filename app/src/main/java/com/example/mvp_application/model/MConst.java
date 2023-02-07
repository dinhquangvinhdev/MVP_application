package com.example.mvp_application.model;

public class MConst {
    public static int ID = 0;
    public static int NUMBER = 0;

    public static int getID() {
        return ID++;
    }

    public static int getAndIncreaseNUMBER() {
        return NUMBER++;
    }

    public static void resetNumber(){
        NUMBER = 0;
    }
}
