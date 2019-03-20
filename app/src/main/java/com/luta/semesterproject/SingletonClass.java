package com.luta.semesterproject;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SingletonClass {

    private List<String> data = new ArrayList<>();

    public List<String> getData()
    {
        return data;
    }
    public void setData(List<String> data)
    {
        this.data = data;
    }

    private static SingletonClass temp = null;
    public static SingletonClass getInstance() {
        if (temp == null)
            temp = new SingletonClass();
        return temp;
    }

    private SingletonClass(){
        Log.i("singleton","Singleton class created.");
    }
}
