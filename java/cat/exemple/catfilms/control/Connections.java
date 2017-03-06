package cat.exemple.catfilms.control;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jordi on 27/01/17.
 */

public class Connections {

    // Read and return the date of generated file
    public static String readDatefromNetwork(String url) throws IOException {
        String updated = null;
        Boolean act;

        InputStream stream = downloadUrl(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        String line = in.readLine();
        if (line != null) {
            line = in.readLine();
            if (line != null) {
                updated = line.substring(line.indexOf("generated")+11,line.indexOf("generated") + 30);
                Log.d("FILMS", updated);
            }
        }
        return updated;
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
    public static InputStream downloadUrl(String urlString) throws IOException {
        //Log.d("MAIN","downloadUrl " + urlString);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        return conn.getInputStream();
    }
}

