package com.example.manzoorhussain.myoutbox.Session;

import android.app.Application;

import com.example.manzoorhussain.myoutbox.Objects.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GeeksEra on 3/30/2018.
 */

public class MyClass extends Application {

    public static String Url = "";
    List<User.Sheet1Object> sheet1Objects = new ArrayList<>();

    public List<User.Sheet1Object> getSheet1Objects() {
        return sheet1Objects;
    }

    public void setSheet1Objects(List<User.Sheet1Object> sheet1Objects) {
        this.sheet1Objects = sheet1Objects;
    }

    public static String getUrl() {
        return Url;
    }

    public static void setUrl(String url) {
        Url = url;
    }
}
