package fr.vhb.sio.velostar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TroisStationsActivity extends AppCompatActivity {
    ArrayList<Station> lesStations;
    TroisStationsActivity.TroisStationsAdapter adaptateur;
    ListView listViewStations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trois_stations);
        initialisations();
    }

    private void initialisations() {
        new TroisStationsActivity.AsyncTaskStations().execute();
    }

    private class AsyncTaskStations extends AsyncTask<Double, Void, Object> {
        /*
         * Définit le traitement à faire en arrière-plan
         * Récupère la liste des stations du district demandé ou une exception
         */
        @Override
        public Object doInBackground (Double... inutilise) {
            try {
                Intent uneIntention = getIntent();

                Station uneStation = (Station) uneIntention.getSerializableExtra("stationBase");
                double latitude = uneStation.getLatitude();
                double longitude = uneStation.getLongitude();

                lesStations = Passerelle.getLesStations(latitude, longitude);
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
                Toast.makeText(TroisStationsActivity.this, getResources().getString(R.string.erreurStations)
                        + " " + result.toString(), Toast.LENGTH_LONG).show();
            }
            else {
                adaptateur = new TroisStationsActivity.TroisStationsAdapter(TroisStationsActivity.this,
                        R.layout.station_distance, lesStations);
                listViewStations = (ListView) findViewById(R.id.listViewStations);
                listViewStations.setAdapter(adaptateur);
            }
        }
    }

    private class TroisStationsAdapter extends ArrayAdapter<Station> {
        private final Context context;
        private final ArrayList<Station> values;
        private final int resource;

        public TroisStationsAdapter(Context context, int resource, ArrayList<Station> values) {
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
            TextView textViewVelosDispo, textViewEmplacementDispo, textViewEtat, textViewName, textViewCoordonnees, textViewDistance;
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
            textViewDistance = (TextView) rowView.findViewById(R.id.textViewDistance);

            // affecte le contenu des widgets d'après le contenu de l'élément reçu
            uneStation = values.get(position);
            textViewName.setText(uneStation.getNom());
            textViewEtat.setText(uneStation.getEtat());
            textViewVelosDispo.setText(String.valueOf(uneStation.getNbVelosDisponibles()));
            textViewEmplacementDispo.setText(String.valueOf(uneStation.getNbAttachesDisponibles()));
            textViewCoordonnees.setText(uneStation.getLatitude().toString() + ";" + uneStation.getLongitude().toString());
            textViewDistance.setText(uneStation.getDistance());
            return rowView;

        }
    }
}
