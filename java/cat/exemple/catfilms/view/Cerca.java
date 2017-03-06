package cat.exemple.catfilms.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

import cat.exemple.catfilms.R;
import cat.exemple.catfilms.model.Cinema;
import cat.exemple.catfilms.model.Film;
import io.realm.Realm;
import io.realm.RealmResults;

public class Cerca extends AppCompatActivity {
    Spinner spn1, spn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca);
        Intent i = getIntent();
        int spin = i.getIntExtra("spin", 0);

        spn1 = (Spinner) findViewById(R.id.spn1);
        spn2 = (Spinner) findViewById(R.id.spn2);

        List<String> llista = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        if(spin == 1) {
            RealmResults<Cinema> cinemes = realm.where(Cinema.class).distinct("Comarca");
            for (Cinema c : cinemes) {
                if (c.getComarca() != null) llista.add(c.getComarca());
            }
        }else {
            RealmResults<Film> films = realm.where(Film.class).distinct("versio");
            for (Film c : films) {
                if (c.getVersio() != null) llista.add(c.getVersio());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, llista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn1.setAdapter(adapter);

    }
}
