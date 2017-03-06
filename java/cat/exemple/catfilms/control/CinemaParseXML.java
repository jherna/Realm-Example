/*
Copyright (C) <2015>  <Jordi Hernandez>
Twitter: @jordikarate 
Web: http://www.jordihernandez,cat

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package cat.exemple.catfilms.control;
import cat.exemple.catfilms.model.Cinema;
import io.realm.Realm;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class CinemaParseXML {
	// We don't use namespaces
    private static final String ns = null;
    private Context mcontext;

    public List<Cinema> parse(InputStream in, Context c) throws XmlPullParserException, IOException {
    	mcontext = c;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            Log.d("CINEMA","parsejar cinemes");
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    
    private List<Cinema> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Cinema> entries = new ArrayList<Cinema>();
       
        
        parser.require(XmlPullParser.START_TAG, ns, "dataroot");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("CINEMES")) {
                entries.add(readEntry(parser));
               
            } else {
                skip(parser);
            }
        }  
        return entries;
    }
    
 // Parses the contents of an CINEMA. If it encounters a ID, NOM, ADREÇA, LOCALITAT, or COMARCA tag, hands them off
 // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Cinema readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, ns, "CINEMES");
        //Per evitar que si un tag no hi és el Cinema no tingui cap camp a null
    	String IdCinema = "--";
    	String NomCinema = "--";
    	String Adreca = "--";
    	String Localitat = "--";
    	String Comarca = "--";
        String Provincia = "--";
    	while (parser.next() != XmlPullParser.END_TAG) {
    		if (parser.getEventType() != XmlPullParser.START_TAG) {
    			continue;
    		}
    		String name = parser.getName();
    		if (name.equals("CINEID")) {
        	 IdCinema = readIdCinema(parser);
    		} else if (name.equals("CINENOM")) {
             NomCinema = readNomCinema(parser);
    		} else if (name.equals("CINEADRECA")) {
             Adreca = readAdreca(parser);
    		} else if (name.equals("LOCALITAT")) {
        	 Localitat = readLocalitat(parser);
    		} else if (name.equals("COMARCA")) {
        	 Comarca = readComarca(parser);
    		} else if (name.equals("PROVINCIA")) {
             Provincia = readProvincia(parser);
            } else {
             skip(parser);
    		}
    	}

        Cinema noucine = new Cinema(IdCinema, NomCinema, Adreca, Localitat, Comarca, Provincia);
    	Log.i("CINEMA",noucine.toString());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Cinema rCinema = realm.copyToRealmOrUpdate(noucine);
        realm.commitTransaction();
    	return noucine ;
    }

    private String readProvincia(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "PROVINCIA");
        String Prov = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "PROVINCIA");
        return Prov;
    }


    //Processes title tags in the feed.
    private String readIdCinema(XmlPullParser parser) throws IOException, XmlPullParserException {
    	parser.require(XmlPullParser.START_TAG, ns, "CINEID");
    	String IdCinema = readText(parser);
    	parser.require(XmlPullParser.END_TAG, ns, "CINEID");
    	return IdCinema;
    }

    private String readNomCinema(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "CINENOM");
	    String title = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "CINENOM");
	    return title;
    }
    
    private String readAdreca(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "CINEADRECA");
	    String title = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "CINEADRECA");
	    return title;
    }
    
    private String readLocalitat(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "LOCALITAT");
	    String title = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "LOCALITAT");
	    return title;
    }

    private String readComarca(XmlPullParser parser) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "COMARCA");
	    String title = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "COMARCA");
	    return title;
    }
 
    //For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
    	String result = "";
    	if (parser.next() == XmlPullParser.TEXT) {
    		result = parser.getText();
    		parser.nextTag();
    	}
    	return result;
    }

	
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
     }
}
