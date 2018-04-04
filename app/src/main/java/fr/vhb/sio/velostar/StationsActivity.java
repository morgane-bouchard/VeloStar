package fr.vhb.sio.velostar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Classe gérant la page affichant la liste des stations
 */
public class StationsActivity extends AppCompatActivity {
    ArrayList<Station> lesStations;
    VelosStarAdapter adaptateur;
    ListView listViewStations;
    private final int CODE_DETAIL = 1;

    /**
     * Appelée lors de la création de l'activité
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        initialisations();
    }

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
                adaptateur = new VelosStarAdapter(StationsActivity.this,
                R.layout.station, lesStations);
                listViewStations = (ListView) findViewById(R.id.listViewStations);
                listViewStations.setAdapter(adaptateur);
                listViewStations.setOnItemClickListener(new clickStation());
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stations, menu);
        return true;
    }

    private class VelosStarAdapter extends ArrayAdapter<Station> {
            private final Context context;
            private final ArrayList<Station> values;
        private final int resource;

        public VelosStarAdapter(Context context, int resource, ArrayList<Station> values) {
            super(context, resource, values);
            this.resource = resource;
            this.context = context;
            this.values = values;
        }

        @Override
        /**
         * Redéfinit la méthode getView qui construit la vue pour l'élément situé à
         * la position spécifiée
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater;
            View rowView;
            TextView textViewVelosDispo, textViewEmplacementDispo, textViewEtat, textViewName, textViewCoordonnees;
            Station uneStation;

            // demande d'obtention d'un désérialisateur de layout xml,
            // càd un objet qui sait transformer un fichier xml en objet View
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // demande à désérialiser le fichier xml identifié par l'id de ressource en objet View
            // rowView est alors un objet regroupant les vues définies dans le layout d'une ligne
            rowView = inflater.inflate(this.resource, parent, false);
            // récupère chaque widget du layout d'un élément
            textViewName = (TextView) rowView.findViewById(R.id.textViewName);
            textViewEtat = (TextView) rowView.findViewById(R.id.textViewEtat);
            textViewVelosDispo = (TextView) rowView.findViewById(R.id.textViewVelosDispo);
            textViewEmplacementDispo = (TextView) rowView.findViewById(R.id.textViewEmplacementDispo);
            textViewCoordonnees = (TextView) rowView.findViewById(R.id.textViewCoordonnees);

            // affecte le contenu des widgets d'après le contenu de l'élément reçu
            uneStation = values.get(position);
            textViewName.setText(uneStation.getNom());
            textViewEtat.setText(uneStation.getEtat());
            textViewVelosDispo.setText(String.valueOf(uneStation.getNbVelosDisponibles()));
            textViewEmplacementDispo.setText(String.valueOf(uneStation.getNbAttachesDisponibles()));
            textViewCoordonnees.setText(uneStation.getLatitude().toString() + ";" + uneStation.getLongitude().toString());
            return rowView;

        }
    }

    private class clickStation implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent uneIntention;
            uneIntention = new Intent(StationsActivity.this, TroisStationsActivity.class);

            Station uneStation = lesStations.get(position);
            uneIntention.putExtra("stationBase", uneStation);
            startActivityForResult(uneIntention, CODE_DETAIL);
        }
    }
}
