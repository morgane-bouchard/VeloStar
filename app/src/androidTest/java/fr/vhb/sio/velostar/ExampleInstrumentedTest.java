package fr.vhb.sio.velostar;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void testJson() {
        String unResultat;
        String unId, unNom;
        int unNombreVelosDispos, nbStations;
        JSONObject unJsonObject;
        JSONArray arrayStations;
        try {
// on suppose que la chaîne unResultat a été initialisée avec le résultat JSON
            //unResultat = lireContenuFichier("c:\\temp\\test.json");
            unResultat = "{ \"nhits\" :83, \"records\" : [{\"fields\" : {\"idStation\":\"1\", \"nom\":\"Cucillé\",\"nombrevelosdisponibles\":5}}, {\"fields\" : {\"idStation\":\"15\", \"nom\":\"Gare\", \"nombrevelosdisponibles\":10}}," +
                    "{\"fields\" : {\"idStation\":\"1\", \"nom\":\"République\", \"nombrevelosdisponibles\":10}}, {\"fields\" : {\"idStation\":\"15\", \"nom\":\"Pontchaillou\", \"nombrevelosdisponibles\":15}}]}";
// des 3 stations les plus proches du lycée Basch
            unJsonObject = new JSONObject(unResultat);

// récupère le nombre total de stations
            nbStations = unJsonObject.getInt("nhits");
            Log.i("Test JSON", "Nombre total de stations " + nbStations);

// récupère le tableau des stations
            arrayStations = unJsonObject.getJSONArray("records");
            Log.i("Test JSON", "Nombre de stations obtenues " + arrayStations.length());
            JSONObject uneStation = arrayStations.getJSONObject(0).getJSONObject("fields");
            Log.i("Test JSON", "Id station 1 " + uneStation.getInt("idStation"));
            Log.i("Test JSON", "Nom station 1 " + uneStation.getString("nom"));
            Log.i("Test JSON", "Nb vélos disponibles première station " + uneStation.getInt("nombrevelosdisponibles"));
            uneStation = arrayStations.getJSONObject(1).getJSONObject("fields");
            Log.i("Test JSON", "Id station 2 " + uneStation.getString("idStation"));
            Log.i("Test JSON", "Nom station 2 " + uneStation.getString("nom"));
            Log.i("Test JSON", "Nb vélos disponibles seconde station " + uneStation.getInt("nombrevelosdisponibles"));

            for (int i=0; i < arrayStations.length(); i++) {
                uneStation = arrayStations.getJSONObject(i).getJSONObject("fields");
                Log.i("Test JSON", "Id station 1 " + uneStation.getInt("idStation"));
                Log.i("Test JSON", "Nom station 1 " + uneStation.getString("nom"));
                Log.i("Test JSON", "Nb vélos disponibles première station " + uneStation.getInt("nombrevelosdisponibles"));
            }


        } catch (Exception uneException) {
            System.err.println("Exception levée : " + uneException.toString());
        }
    }
}