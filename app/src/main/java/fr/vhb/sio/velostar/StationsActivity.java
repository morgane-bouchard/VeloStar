package fr.vhb.sio.velostar;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    LocationManager locManager;
    Location myLocation=null;
    MyLocationListener myListener=null;
    private final int CODE_DETAIL = 1;
    private static final int MY_PERMISSION_LOCATION = 1;

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


        if (ActivityCompat.checkSelfPermission(StationsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }
        else {
            locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            myListener = new MyLocationListener();
            if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, myListener, null);
            } else if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 100, myListener);
            } else {
                Toast.makeText(StationsActivity.this, "Aucun fournisseur", Toast.LENGTH_LONG).show();
            }
        }
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
                if (myLocation == null){
                    lesStations = Passerelle.getLesStations();
                }
                else {
                    lesStations = Passerelle.getLesStations(myLocation.getLatitude(), myLocation.getLongitude());
                }

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
                R.layout.station_distance, lesStations);
                listViewStations = (ListView) findViewById(R.id.listViewStations);
                listViewStations.setAdapter(adaptateur);
                listViewStations.setOnItemClickListener(new clickStation());
                listViewStations.setOnItemLongClickListener(new longClickStation());
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
            TextView textViewVelosDispo, textViewEmplacementDispo, textViewEtat, textViewName, textViewDistance;
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
            textViewDistance = (TextView) rowView.findViewById(R.id.textViewDistance);

            // affecte le contenu des widgets d'après le contenu de l'élément reçu
            uneStation = values.get(position);
            textViewName.setText(uneStation.getNom());
            textViewEtat.setText(uneStation.getEtat());
            textViewVelosDispo.setText(String.valueOf(uneStation.getNbVelosDisponibles()));
            textViewEmplacementDispo.setText(String.valueOf(uneStation.getNbAttachesDisponibles()));
            if (uneStation.getDistance()!= null )
                textViewDistance.setText(uneStation.getDistance());
            else
                textViewDistance.setText("0");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_geoloc:
                if (ActivityCompat.checkSelfPermission(StationsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    Toast.makeText(StationsActivity.this, "Permission non accordée", Toast.LENGTH_LONG);
                    ActivityCompat.requestPermissions(StationsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_LOCATION);
                } else {
                    Toast.makeText(StationsActivity.this, "Permission accordée", Toast.LENGTH_LONG);
                    // récupérer l'instance du gestionnaire de localisation
                    locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                    myListener = new MyLocationListener();
                    if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, myListener, null);
                    } else if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 100, myListener);
                    } else {
                        Toast.makeText(StationsActivity.this, "Aucun fournisseur", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.action_tri_alpha:
                ComparateurParNom unComparateur = new ComparateurParNom();
                Collections.sort(lesStations, unComparateur);
                adaptateur.notifyDataSetChanged();
                break;
            case R.id.action_tri_distance:
                Collections.sort(lesStations);
                adaptateur.notifyDataSetChanged();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            myLocation = location;
            new AsyncTaskStations().execute();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            Log.i(this.getClass().toString(), "Provider " + provider + " is disabled");

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            Log.i(this.getClass().toString(), "Provider " + provider + " is enabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            Log.i(this.getClass().toString(), "Provider status " + provider + " has changed : " + String.valueOf(status));

        }
    }

    private class longClickStation implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            // Create a Uri from an intent string. Use the result to create an Intent.
            Station uneStation = lesStations.get(position);
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+uneStation.getLatitude()+","+uneStation.getLongitude());

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");

            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent);

            return true;
        }
    }
}
