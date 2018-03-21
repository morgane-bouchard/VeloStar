package fr.vhb.sio.velostar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Classe gérant la page affichant la liste des stations
 */
public class StationsActivity extends AppCompatActivity {
    ArrayList<Station> lesStations;
    ArrayAdapter<Station> unAdaptateur;
    ListView listViewStations;

    /**
     * Appelée lors de la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        initialisations();
    }
	/*private void initialisations() {
		Intent uneIntention;

		uneIntention = getIntent();
		try {
			lesStations = Passerelle.getLesStations();
			unAdaptateur = new ArrayAdapter<Station>(this, android.R.layout.simple_list_item_1, lesStations);
			listViewStations = (ListView) findViewById(R.id.listViewStations);
			listViewStations.setAdapter(unAdaptateur);
		}
		catch (Exception ex) {
			Toast.makeText(this, "Erreur de récupération de la liste des stations" + " " + ex.toString(), Toast.LENGTH_LONG).show();
		}
	}*/

    /**
     * Procède à toutes les initialisations
     * - lance un thread séparé pour récupérer la liste des stations
     */
    private void initialisations() {
        Intent uneIntention;

        uneIntention = getIntent();
        new AsyncTaskStations().execute();
    }

    /**
     *
     * Classe interne pour définir le thread séparé
     */
    private class AsyncTaskStations extends AsyncTask <Void, Void, Object> {
        /*
         * Définit le traitement à faire en arrière-plan
         * Récupère la liste des stations du district demandé ou une exception
         */
        @Override
        public Object doInBackground (Void... inutilise) {
            try {
                lesStations = Passerelle.getLesStations();
            }
            catch (Exception ex) {
                return ex;
            }
            return lesStations;
        }
        /**
         * Définit le traitement à faire une fois le traitement en arrière-plan terminé
         * dont les initialisations des composants graphiques
         */
        @Override
        protected void onPostExecute (Object result) {
            if ( result instanceof Exception ) {
                Toast.makeText(StationsActivity.this, getResources().getString(R.string.erreurStations)
                        + " " + result.toString(), Toast.LENGTH_LONG).show();
            }
            else {
                unAdaptateur = new ArrayAdapter<Station>(StationsActivity.this,
                        android.R.layout.simple_list_item_1, lesStations);
                listViewStations = (ListView) findViewById(R.id.listViewStations);
                listViewStations.setAdapter(unAdaptateur);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stations, menu);
        return true;
    }

}
