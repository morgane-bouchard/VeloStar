package fr.vhb.sio.velostar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
import java.util.ArrayList;
/**
 * Cette classe fait le lien entre les services web et l'application
 * Elle utilise le modèle DOM pour parcourir le document XML
 * Ce modèle fait partie du JDK (et également du SDK Android)
 */
public class Passerelle {

    /** Membres privés */
    private static String _urlStations = "https://data.explore.star.fr/api/records/1.0/search/?dataset=vls-stations-etat-tr&rows=100";


    /**
     * Fournit une liste de stations à partir des données fournies par le service web de VeloStar
     * @return ArrayList<Station>
     * @throws Exception
     */
    public static ArrayList<Station> getLesStations() throws Exception
    {
        JSONObject unObjetJSON;
        ArrayList<Station> lesStations;
        // initialisation d'une liste de stations
        lesStations = new ArrayList<Station>();
        try
        {
            String uneChaineUri = _urlStations;
            unObjetJSON = loadResultJSON(uneChaineUri);

            // récupération du tableau JSON nommé records
            JSONArray arrayStations = unObjetJSON.getJSONArray("records");

			/* Exemple de données obtenues pour une station :
               "fields": {
                    "etat": "En fonctionnement",
                    "lastupdate": "2017-03-19T00:13:03+00:00",
                    "nombrevelosdisponibles": 15,
                    "nombreemplacementsactuels": 30,
                    "nom": "République",
                    "nombreemplacementsdisponibles": 15,
                    "idstation": 1,
                    "coordonnees": [
                    48.1100259201,
                    -1.6780371631
                    ]
               }
            */

            // parcours du tableau json
            for (int i = 0 ; i < arrayStations.length() ; i++)
            {
                // création de l'élement courant à chaque tour de boucle
                Station uneStation = getStation(arrayStations.getJSONObject(i).getJSONObject("fields"));
                lesStations.add(uneStation);
            }
            return lesStations;		// retourne l'objet Carte
        }
        catch (Exception ex)
        {	Log.e("Passerelle", "Erreur exception : " + ex.toString());
            throw (ex);
        }
    }

    public static ArrayList<Station> getLesStations(Double latitude, Double longitude) throws Exception {
        String urlTroisStations = "https://data.explore.star.fr/api/records/1.0/search/?dataset=vls-stations-etat-tr&rows=100&start=1&geofilter.distance=";
        urlTroisStations += latitude + "%2C+" + longitude + "%2C5000";
        JSONObject unObjetJSON;
        ArrayList<Station> lesStations;
        // initialisation d'une liste de stations
        lesStations = new ArrayList<Station>();
        try
        {
            String uneChaineUri = urlTroisStations;
            unObjetJSON = loadResultJSON(uneChaineUri);

            // récupération du tableau JSON nommé records
            JSONArray arrayStations = unObjetJSON.getJSONArray("records");

            // parcours du tableau json
            for (int i = 0 ; i < arrayStations.length() ; i++)
            {
                // création de l'élement courant à chaque tour de boucle
                Station uneStation = getStation(arrayStations.getJSONObject(i).getJSONObject("fields"));
                lesStations.add(uneStation);
            }
            return lesStations;		// retourne la liste des stations
        }
        catch (Exception ex)
        {	Log.e("Passerelle", "Erreur exception : " + ex.toString());
            throw (ex);
        }
    }

    public static ArrayList<Station> getLesTroisStations(Double latitude, Double longitude) throws Exception
    {
        String urlTroisStations = "https://data.explore.star.fr/api/records/1.0/search/?dataset=vls-stations-etat-tr&rows=3&start=1&geofilter.distance=";
        urlTroisStations += latitude + "%2C+" + longitude + "%2C5000";
        JSONObject unObjetJSON;
        ArrayList<Station> lesStations;
        // initialisation d'une liste de stations
        lesStations = new ArrayList<Station>();
        try
        {
            String uneChaineUri = urlTroisStations;
            unObjetJSON = loadResultJSON(uneChaineUri);

            // récupération du tableau JSON nommé records
            JSONArray arrayStations = unObjetJSON.getJSONArray("records");

			/* Exemple de données obtenues pour une station :
               "fields": {
                    "etat": "En fonctionnement",
                    "lastupdate": "2017-03-19T00:13:03+00:00",
                    "nombrevelosdisponibles": 15,
                    "nombreemplacementsactuels": 30,
                    "nom": "République",
                    "nombreemplacementsdisponibles": 15,
                    "idstation": 1,
                    "coordonnees": [
                    48.1100259201,
                    -1.6780371631
                    ]
               }
            */

            // parcours du tableau json
            for (int i = 0 ; i < arrayStations.length() ; i++)
            {
                // création de l'élement courant à chaque tour de boucle
                Station uneStation = getStation(arrayStations.getJSONObject(i).getJSONObject("fields"));
                lesStations.add(uneStation);
            }
            return lesStations;		// retourne l'objet Carte
        }
        catch (Exception ex)
        {	Log.e("Passerelle", "Erreur exception : " + ex.toString());
            throw (ex);
        }
    }

    private static Station getStation(JSONObject courant) throws Exception {
        Station uneStation;
        // lecture des propriétés
        int id = courant.getInt("idstation");
        String name = courant.getString("nom");
        String state = courant.getString("etat");
        int bikesavailable = courant.getInt("nombrevelosdisponibles");
        int slots = courant.getInt("nombreemplacementsactuels");
        Double latitude = courant.getJSONArray("coordonnees").getDouble(0);
        Double longitude = courant.getJSONArray("coordonnees").getDouble(1);
        String etat = courant.getString("etat");
        boolean booleanState = state.equals("En fonctionnement");
        if (courant.has("dist"))
        {
            String distance = courant.getString("dist");
            uneStation = new Station(id, name, booleanState, (slots - bikesavailable), bikesavailable, latitude, longitude, etat, distance);
        }
        else {
            uneStation = new Station(id, name, booleanState, (slots - bikesavailable), bikesavailable, latitude, longitude, etat);
        }
        // ajoute la station à la collection
        return uneStation;
    }

    /**
     * Fournit le flux XML reçu suite à l'appel du service web localisé à l'uri spécifié
     * @param unURL
     * @return Document
     * @throws Exception
     */
    private static JSONObject loadResultJSON(String unURL) throws Exception{
        JSONObject unJsonObject = null;
        String leResultat;

        URL url = new URL(unURL);
        // établissement d'une requête GET pour demander le jeu de données exprimé dans unURL
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        // exécution de la requête récupération de la réponse dans un flux en lecture (InputStream)
        InputStream unFluxEnEntree = con.getInputStream();

        // conversion du flux en chaîne
        leResultat = convertStreamToString(unFluxEnEntree);

        // transformation de la chaîne en objet JSON
        JSONObject json = new JSONObject(leResultat);
        return json;
    }

    /**
     * Conversion d'un flux
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        }
        catch (IOException e) {
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
            }
        }
        return sb.toString();
    }
}