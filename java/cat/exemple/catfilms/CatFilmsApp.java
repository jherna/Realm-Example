package cat.exemple.catfilms;

import android.app.Application;
import android.util.Log;
import io.realm.Realm;

/**
 * Created by jordi on 27/01/17.
 */

public class CatFilmsApp extends Application {

    //public static final String AUTH_URL = "http:/10.0.8.227:9080/auth";
    //public static final String REALM_URL = "realm://10.0.8.227:9080/~/realmtasks";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Log.d("start", "CatFilmsApp");
    }
}
