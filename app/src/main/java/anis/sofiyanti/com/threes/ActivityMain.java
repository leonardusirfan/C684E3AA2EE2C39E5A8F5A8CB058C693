package anis.sofiyanti.com.threes;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import anis.sofiyanti.com.threes.Act_Game.ActivityGameOpening;

public class ActivityMain extends AppCompatActivity implements OnMapReadyCallback {

    private boolean permission_gained = false;
    private AlertDialog dialog_lokasi;
    private Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }

        Button btn_kuota, btn_poin, btn_game;
        btn_kuota = findViewById(R.id.btn_kuota);
        btn_poin = findViewById(R.id.btn_poin);
        btn_game = findViewById(R.id.btn_game);

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
        builder.setTitle("Akses Lokasi Tidak Menyala");
        builder.setMessage("Aplikasi membutuhkan akses lokasi untuk dapat berjalan dengan benar. Nyalakan sekarang?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog_lokasi = builder.create();

        btn_kuota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMain.this, ActivityKuota.class));
            }
        });

        btn_poin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMain.this, ActivityPoin.class));
            }
        });

        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityMain.this, ActivityGameOpening.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Yakin ingin keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                AppSharedPreferences.logout(ActivityMain.this);
                Intent i = new Intent(ActivityMain.this, ActivityLogin.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Menunjukkan lokasi user
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){
            checkPermission();
            if(permission_gained || Build.VERSION.SDK_INT < 23){
                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(userLocation.getLatitude(), userLocation.getLongitude())).title("Posisi anda"));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), 15.0f));
                    System.out.println("GPS provider : " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
                }
                else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    userLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(userLocation.getLatitude(), userLocation.getLongitude())).title("Posisi anda"));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), 15.0f));
                    System.out.println("Network provider : " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
                }
                else{
                    dialog_lokasi.show();
                }
            }
        }
        else{
            Toast.makeText(ActivityMain.this, "Location Manager Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(ActivityMain.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this, Manifest.permission.ACCESS_FINE_LOCATION)){
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                builder.setTitle("Izin Lokasi");
                builder.setMessage("Aplikasi membutuhkan izin lokasi untuk dapat berjalan dengan benar.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(ActivityMain.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
                    }
                });
                builder.create().show();
            }
            else{
                ActivityCompat.requestPermissions(ActivityMain.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            }
        }
        else{
            permission_gained = true;
        }
    }
}
