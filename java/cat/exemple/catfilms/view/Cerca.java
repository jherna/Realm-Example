package cat.exemple.catfilms.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.List;

import cat.exemple.catfilms.Adapters.ListCinemaAdapter;
import cat.exemple.catfilms.R;
import cat.exemple.catfilms.model.Cinema;
import cat.exemple.catfilms.model.Film;
import io.realm.Realm;
import io.realm.RealmResults;

public class Cerca extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spn1,spn2;
    private ListView lsvCerca;
    private ListCinemaAdapter listCinemaAdapter;
    private Context context;
    private int spin;
    private List<String> llista2;
    private List<String> llista;
    private ArrayAdapter<String> adapter;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca);
        Intent i = getIntent();
        spin = i.getIntExtra("spin", 0);
        context = getApplicationContext();

        spn1 = (Spinner) findViewById(R.id.spn1);
        spn2 = (Spinner) findViewById(R.id.spn2);
        lsvCerca = (ListView) findViewById(R.id.lsvCerca);

        //llista per el primer spinner i llista2 per els segon spinner
        llista = new ArrayList<>();
        llista2 = new ArrayList<>();

        //Primera càrrega del spinner principal
        realm = Realm.getDefaultInstance();
        if (spin == 1) {
            RealmResults<Cinema> cinemes = realm.where(Cinema.class).distinct("Comarca");
            for (Cinema c : cinemes) {
                if (c.getComarca() != null) llista.add(c.getComarca());
            }
        } else {
            RealmResults<Film> films = realm.where(Film.class).distinct("versio");
            for (Film c : films) {
                if (c.getVersio() != null) llista.add(c.getVersio());
            }
        }

        //Instaciem adapters i els listeners dels spinner
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, llista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn1.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, llista2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn2.setAdapter(adapter);

        spn1.setOnItemSelectedListener(this);
        spn2.setOnItemSelectedListener(this);

        registerForContextMenu(lsvCerca);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spn1:
                if(spin == 1) loadLocalitats();
                else { ; } //TODO càrrega del spinner2 per films
                break;
            case R.id.spn2:
                if(spin == 1) loadCinemesLocalitat(spn2.getSelectedItem().toString());
                break;
            default: Log.d("Cerca", "position default" + position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadLocalitats() {
        RealmResults<Cinema> cinemes = realm.where(Cinema.class)
                .equalTo("Comarca", spn1.getSelectedItem().toString())
                .distinct("Localitat");
        llista2.clear();
        for (Cinema c : cinemes) {
            llista2.add(c.getLocalitat());
        }
        adapter.notifyDataSetChanged();

    }

    private void loadCinemesLocalitat(String sel) {
        RealmResults<Cinema> cinemes = realm.where(Cinema.class)
                .equalTo("Localitat", spn2.getSelectedItem().toString())
                .findAll();
        listCinemaAdapter = new ListCinemaAdapter(cinemes);
        if(cinemes.size()>0) lsvCerca.setAdapter(listCinemaAdapter);
        listCinemaAdapter.notifyDataSetChanged();
    }
}
