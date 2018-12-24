package test.omegaware.syllego;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class FirebasePersistenceHandler extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
