package cat.exemple.catfilms;

import android.app.Application;
import android.util.Log;

import cat.exemple.catfilms.model.Migration;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jordi on 27/01/17.
 */

public class CatFilmsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("catfilms.realm")
                .schemaVersion(0)
                .migration(new Migration())
                .build();


        Realm.setDefaultConfiguration(config);

        Log.d("start", "CatFilmsApp");
    }
}
