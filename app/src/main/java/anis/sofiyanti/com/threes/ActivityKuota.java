package anis.sofiyanti.com.threes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityKuota extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private final String koridor = "koridor1";
    private ArrayList<HalteModel> halte1 = new ArrayList<>();
    private ArrayList<HalteModel> halte2 = new ArrayList<>();
    private ArrayList<BusModel> bus  = new ArrayList<>();

    private ArrayList<Marker> currentHalte = new ArrayList<>();
    
    private RecyclerView rv_halte;
    private LinearLayout layout_loading;
    
    private String[] rute = {"Mangkang - Penggaron", "Penggaron - Mangkang"};
    private boolean rute_balik = false;

    private double lat_start;
    private double long_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kuota);

        layout_loading = findViewById(R.id.layout_loading);
        //Inisialisasi list view halte
        rv_halte = findViewById(R.id.rv_halte);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        SlidingUpPanelLayout layout = findViewById(R.id.main_layout);
        layout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {}

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState.equals(SlidingUpPanelLayout.PanelState.ANCHORED)){
                    mMap.setPadding(0,0,0,600);
                }
                else if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                    mMap.setPadding(0,0,0,150);
                }
            }
        });

        Button btn_rute = findViewById(R.id.btn_rute);
        btn_rute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt_rute = findViewById(R.id.txt_rute);
                if(!rute_balik){
                    txt_rute.setText(rute[1]);
                    inisialisasiHalte(halte2);
                    rute_balik = true;
                }
                else{
                    txt_rute.setText(rute[0]);
                    inisialisasiHalte(halte1);
                    rute_balik = false;
                }

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentHalte.get(0).getPosition().latitude, currentHalte.get(0).getPosition().longitude), 15.0f));
            }
        });

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.child("halte").child(koridor).child("mangkang_penggaron").getChildren()) {
                    halte1.add(snapshot.getValue(HalteModel.class));
                }

                for (DataSnapshot snapshot : dataSnapshot.child("halte").child(koridor).child("penggaron_mangkang").getChildren()) {
                    halte2.add(snapshot.getValue(HalteModel.class));
                }

                for (DataSnapshot snapshot : dataSnapshot.child("buses").getChildren()) {
                    bus.add(snapshot.getValue(BusModel.class));
                }

                lat_start = halte1.get(0).latitude;
                long_start = halte1.get(0).longitude;
                Collections.sort(halte1, new Comparator<HalteModel>() {
                    @Override
                    public int compare(HalteModel o1, HalteModel o2) {
                        return Double.compare(Math.sqrt(Math.pow(lat_start - o1.latitude, 2) + (Math.pow(long_start - o1.longitude, 2))),Math.sqrt(Math.pow(lat_start - o2.latitude, 2) + (Math.pow(long_start - o2.longitude, 2))));
                    }
                });

                lat_start = halte2.get(0).latitude;
                long_start = halte2.get(0).longitude;
                Collections.sort(halte2, new Comparator<HalteModel>() {
                    @Override
                    public int compare(HalteModel o1, HalteModel o2) {
                        return Double.compare(Math.sqrt(Math.pow(lat_start - o1.latitude, 2) + (Math.pow(long_start - o1.longitude, 2))),Math.sqrt(Math.pow(lat_start - o2.latitude, 2) + (Math.pow(long_start - o2.longitude, 2))));
                    }
                });

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                if(mapFragment != null){
                    mapFragment.getMapAsync(ActivityKuota.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ActivityKuota.this, "Gagal membaca daftar halte", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mMap != null){
            layout_loading.setVisibility(View.GONE);
            mMap.setPadding(0,0,0, 150);

            mMap.clear();
            inisialisasiHalte(halte1);
            inisialisasiBus(bus);

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    BusModel b = (BusModel) marker.getTag();
                    if(b != null){
                        Gson gson = new Gson();
                        Intent i = new Intent(ActivityKuota.this, ActivityDetail.class);
                        i.putExtra("bus", gson.toJson(b));
                        startActivity(i);
                    }
                    return false;
                }
            });

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentHalte.get(0).getPosition().latitude, currentHalte.get(0).getPosition().longitude), 15.0f));
        }
    }

    private void inisialisasiBus(ArrayList<BusModel> bus){
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pilih_merah), (int)getResources().getDimension(R.dimen.gmap_marker_bus_width), (int)getResources().getDimension(R.dimen.gmap_marker_bus_height), false));

        for(BusModel b : bus){
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(b.latitude, b.longitude))
                    .title("Bus")
                    .icon(icon));
            m.setTag(b);
        }
    }

    private void inisialisasiHalte(ArrayList<HalteModel> halte){

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shelter), (int)getResources().getDimension(R.dimen.gmap_marker_width), (int)getResources().getDimension(R.dimen.gmap_marker_height), false));

        for(Marker m : currentHalte){
            m.remove();
        }
        currentHalte.clear();

        for(HalteModel h : halte){
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(h.getLatitude(), h.getLongitude()))
                    .title(h.getNama())
                    .icon(icon));
            currentHalte.add(m);
        }

        HalteAdapter adapter = new HalteAdapter(halte, mMap);

        rv_halte.setLayoutManager(new LinearLayoutManager(this));
        rv_halte.setItemAnimator(new DefaultItemAnimator());
        rv_halte.setAdapter(adapter);
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, (int)getResources().getDimension(R.dimen.gmap_marker_width), (int)getResources().getDimension(R.dimen.gmap_marker_height));
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
