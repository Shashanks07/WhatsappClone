package com.example.baratomessenger;
import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
        .server("https://parseapi.back4app.com")
        .applicationId("qU8sbWijxuA6YynwKJWwShT08vdbKN712llcBVpn")
        .clientKey("50qRt379HM9G8N9oTR3I5mQJ9yu2Iqh8dBvFVqnR")
                .build());
    }
}
