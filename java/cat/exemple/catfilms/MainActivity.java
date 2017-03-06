package cat.exemple.catfilms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import cat.exemple.catfilms.Adapters.ListCinemaAdapter;
import cat.exemple.catfilms.Adapters.ListFilmAdapter;
import cat.exemple.catfilms.control.CinemaParseXML;
import cat.exemple.catfilms.control.Connections;
import cat.exemple.catfilms.control.FilmParseXML;
import cat.exemple.catfilms.model.Cinema;
import cat.exemple.catfilms.model.Film;
import cat.exemple.catfilms.view.Cerca;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Button btFilms;
    private TextView txvFilmsSize;
    private Spinner spTria;
    private SharedPreferences prefs;
    private String updated;
    private Context context;
    private Realm realm;
    private ListFilmAdapter filmAdapter;
    private ListCinemaAdapter cineAdapter;
    private ListView  lsvData;

    public enum SelectedData {
        FILM, CINEMA
    }

    private SelectedData selectedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        realm = Realm.getDefaultInstance();

        lsvData = (ListView) findViewById(R.id.lsvFilms);
        txvFilmsSize = (TextView) findViewById(R.id.txvFilms);
        spTria = (Spinner) findViewById(R.id.spTria);
        spTria.setEnabled(false);
        spTria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        final RealmResults<Film> pelis = realm.where(Film.class).findAll();
                        filmAdapter = new ListFilmAdapter(context, pelis);
                        if(pelis.size()>0) lsvData.setAdapter(filmAdapter);
                        selectedData = SelectedData.FILM;
                        filmAdapter.notifyDataSetChanged();

                        break;
                    case 1:
                        final RealmResults<Cinema> cines = realm.where(Cinema.class).findAll();
                        cineAdapter = new ListCinemaAdapter(context, cines);
                        if(cines.size()>0) lsvData.setAdapter(cineAdapter);
                        selectedData = SelectedData.CINEMA;
                        cineAdapter.notifyDataSetChanged();

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prefs = getSharedPreferences(getString(R.string.file_prefs),MODE_PRIVATE);
        String DEFAULT_DATA = "2011-08-31T00:00:00";
        updated = prefs.getString(getString(R.string.uptaded_data), DEFAULT_DATA);
        Log.i("FILMS", updated);
        txvFilmsSize.setText(updated);

        registerForContextMenu(lsvData);

        new DownloadTask().execute(Links.URL_FILMS, Links.URL_CINEMES);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menucontext, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menuopcio3:
                System.out.println("esborra pos: " + info.position + " id: " + info.id);
                deleteItem(info.position);
                break;
            case R.id.menuopcio4:
                System.out.println("editar");
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void deleteItem(int pos) {
        if(selectedData == SelectedData.CINEMA) {
            //esborrar cinema
            System.out.println("esborrem cinema");
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Cinema> cine = realm.where(Cinema.class).findAll();
            realm.beginTransaction();
            cine.get(pos).deleteFromRealm();
            realm.commitTransaction();
            cineAdapter.notifyDataSetChanged();
        }else {
            //esborra films
            System.out.println("esborrem peli");
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Film> cine = realm.where(Film.class).findAll();
            realm.beginTransaction();
            cine.get(pos).deleteFromRealm();
            realm.commitTransaction();
            filmAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = null;
        int msg = 0;
        switch (item.getItemId()) {
            case R.id.menuopcio1:
                msg = 1;
                i = new Intent(this, Cerca.class);
                break;
            case R.id.menuopcio2:
                msg = 2;
                i = new Intent(this, Cerca.class);
                break;
        }
        i.putExtra("spin", msg);
        startActivity(i);
        return true;
    }

    // Implementation of AsyncTask used to download update date and xml data.
    private class DownloadTask extends AsyncTask<String, Void, Void> {
        Boolean connexio;
        ConnectivityManager cm;
        Boolean updated;

        @Override
        protected void onPreExecute() {
            // Comprovem la connexió
            cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo activieNetwork = cm.getActiveNetworkInfo();
            connexio = activieNetwork != null && activieNetwork.isConnectedOrConnecting();
            if(!connexio) {
                Toast t = Toast.makeText(context, "No hi ha connexió a internet",Toast.LENGTH_LONG);
                t.show();
            }
        }
        @Override
        protected Void doInBackground(String... urls) {
            //Log.d("MAIN","URL: " + urls[0]);
            if(connexio) {
                try {
                    updated = saveUpdatedPrefs(Connections.readDatefromNetwork(urls[0]));
                    if(!updated) {
                        //deleteFilms();
                        loadXmlFromNetwork(urls[0]);
                        loadXmlFromNetwork(urls[1]);
                    }
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            String msg="";
            spTria.setEnabled(true);
            spTria.post(new Runnable() {
                @Override
                public void run() {
                    spTria.setSelection(0,true);
                }
            });

        }
    }

    private void deleteFilms() {
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Film> pelis = realm.where(Film.class).findAll();
        //Not updated and some films. Need delete old saved films
        Log.i("FILMS","num pelis:" + pelis.size());
        if(pelis.size()>0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    pelis.deleteAllFromRealm();
                }
            });
        }

    }

    private void loadXmlFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        // Instantiate the parser
        try {
            stream = Connections.downloadUrl(urlString);
            //get CINEMES from parser
            switch(urlString) {
                case Links.URL_CINEMES:
                    CinemaParseXML cinemaParseXML = new CinemaParseXML();
                    cinemaParseXML.parse(stream, this);
                    break;
                case Links.URL_FILMS:
                    FilmParseXML filmParseXML = new FilmParseXML();
                    filmParseXML.parse(stream, this);
                    break;
                case Links.URL_CICLES:
                    //CicleParseXML cicleParseXML = new CicleParseXML();
                    //cicleParseXML.parse(stream, this);
                    break;
                case Links.URL_SESSIONS:
                    //SessioParseXML sessioParseXML = new SessioParseXML();
                    //sessioParseXML.parse(stream, this);
                    break;
            }


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }


    }

    private Boolean saveUpdatedPrefs(String data) {
        boolean act = false;
        // Update data in prefs
        SharedPreferences.Editor editor = prefs.edit();
        if(!data.equals(updated)) {
            Log.d("FIMLS","no actualitzat");
            editor.putString(getString(R.string.uptaded_data),data);
            editor.commit();
            //txvFilmsSize.setText(data);
            act = false;
        }
        else act=true;
        return act;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

}
