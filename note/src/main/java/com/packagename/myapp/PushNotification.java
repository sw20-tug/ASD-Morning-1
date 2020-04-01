package com.packagename.myapp;

import java.io.Serializable;


import org.springframework.stereotype.Service;

@Service
public class PushNotification implements Serializable {

    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            return "Hello anonymous user";
        } else {
            return "Hello " + name;
        }
    }

    public String save(String name, String text) {
        if (name == null || name.isEmpty()) {
            return "Please enter a filename";
        } else {
            //LoadStore.saveToDatabase(name, text);
            return "stored as: " + name;
        }
    }

}
