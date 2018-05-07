package com.animais.sos.sosanimais.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.animais.sos.sosanimais.R;
import com.animais.sos.sosanimais.dao.AnimalDAO;
import com.animais.sos.sosanimais.dao.Db;
import com.animais.sos.sosanimais.model.Animal;
import com.animais.sos.sosanimais.model.Localizacao;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CreateAnimalActivity extends AppCompatActivity {

    private class AnimalSaveTask extends AsyncTask<Animal, Void, Void> {

        @Override
        public Void doInBackground(Animal... params) {
            SQLiteDatabase db = new Db(CreateAnimalActivity.this).getWritableDatabase();
            AnimalDAO dao = new AnimalDAO(db);

            try {
                dao.inserir(params[0]);
            } finally {
                db.close();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void notes) {
            Intent intent = new Intent();

            intent.putExtra("animal", animal);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    private class Conexao extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            try {
                String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+ params[0] + "," + params[1];
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);
                InputStream in = httpclient.execute(request).getEntity().getContent();

                BufferedReader br = null;
                StringBuilder sb = new StringBuilder();

                br = new BufferedReader(new InputStreamReader(in));

                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    line = br.readLine();
                }

                String resposta = sb.toString();

                return resposta;

            } catch ( Exception e ) {
                return "Erro: " + e.getMessage();
            }
        }
    }

    private TextView latitude, longitude;
    private Button btnFoto;
    private LocationManager locationManager;
    private Double valueLongitude, valueLatitude;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_animal);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        latitude = (TextView)findViewById(R.id.lblLatitude);
        longitude = (TextView)findViewById(R.id.lblLongitude);
        this.btnFoto = (Button) findViewById(R.id.btnFoto);
        this.btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Localizacao localizacao = getLocation();
                latitude.setText(localizacao.getLatitude());
                longitude.setText(localizacao.getLongitude());
                if(!latitude.getText().toString().isEmpty() && !longitude.getText().toString().isEmpty()){
                    new Conexao().execute(latitude.getText().toString(), longitude.getText().toString());
                }
            }
        });
    }

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(Location location) {
            valueLongitude = location.getLongitude();
            valueLatitude = location.getLatitude();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    latitude.setText(valueLatitude + "");
                    longitude.setText(valueLongitude + "");
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    // busca as coordenadas conforme a localizacao do GPS
    private Localizacao getLocation() {
        Localizacao localizacao = new Localizacao();
        if (!isLocationEnabled()) {
            mensagemGPS();
        }
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissionCheck == -1){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0
                );
            }
            locationManager.requestLocationUpdates(provider, 2 * 60 * 1000, 10, locationListenerBest);
        }
        return localizacao;
    }

    private boolean isLocationEnabled() {
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(gps_enabled || network_enabled)
            return true;
        else
            return false;
    }

    private void mensagemGPS(){
        if(!isLocationEnabled()){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("GPS - Desativado");
            dialog.setMessage("É necessário realizar a ativação do GPS!");
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    CreateAnimalActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }
}
