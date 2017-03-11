package cat.exemple.catfilms.model;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by jordi on 12/03/17.
 */

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if(oldVersion == 0) {
            Log.d("Migration", "actualitzant a la versió 1");
            RealmObjectSchema cinemaSchema = schema.get("Cinema");
            cinemaSchema.addIndex("Provincia");
            oldVersion++;
        }
    }
}
