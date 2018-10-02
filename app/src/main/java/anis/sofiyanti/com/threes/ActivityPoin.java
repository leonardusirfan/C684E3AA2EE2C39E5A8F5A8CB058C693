package anis.sofiyanti.com.threes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityPoin extends AppCompatActivity {

    private final int POIN_TUKAR = 3500;
    private TextView txt_poin;
    private int poin;
    private LinearLayout layout_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poin);

        layout_loading = findViewById(R.id.layout_loading);
        final ImageView img_tiket = findViewById(R.id.img_tiket);
        TextView txt_tukar_poin;

        txt_poin = findViewById(R.id.txt_poin);
        txt_tukar_poin = findViewById(R.id.txt_tukar_poin);
        Button btn_tukar = findViewById(R.id.btn_tukar);

        String poin_tukar = String.valueOf(POIN_TUKAR) + " Points";
        txt_tukar_poin.setText(poin_tukar);

        final DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference().child("users").child("user").child(AppSharedPreferences.getId(ActivityPoin.this)).child("poin");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                layout_loading.setVisibility(View.GONE);

                poin = dataSnapshot.getValue(Integer.class);
                String stringPoin = String.valueOf(poin)+ " available Points";
                txt_poin.setText(stringPoin);
                if(poin >= POIN_TUKAR){
                    img_tiket.setImageResource(R.drawable.setelah_ditukar);
                }
                else{
                    img_tiket.setImageResource(R.drawable.tiket_tukar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ActivityPoin.this, "Gagal membaca database", Toast.LENGTH_SHORT).show();
            }
        });

        btn_tukar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(poin < POIN_TUKAR){
                    Toast.makeText(ActivityPoin.this, "Poin anda tidak cukup untuk ditukar dengan tiket gratis", Toast.LENGTH_SHORT).show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPoin.this);
                    builder.setTitle("Konfirmasi penukaran poin");
                    builder.setMessage("Yakin ingin menukarkan poin?");
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            layout_loading.setVisibility(View.VISIBLE);
                            database.setValue(poin - POIN_TUKAR, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        layout_loading.setVisibility(View.GONE);
                                        Toast.makeText(ActivityPoin.this, "Pemrosesan database gagal", Toast.LENGTH_SHORT).show();
                                    } else {
                                        layout_loading.setVisibility(View.GONE);
                                        poin -= POIN_TUKAR;
                                        String stringPoin = String.valueOf(poin)+ " available Points";
                                        txt_poin.setText(stringPoin);
                                        img_tiket.setImageResource(R.drawable.tiket_tukar);
                                        Toast.makeText(ActivityPoin.this, "Poin telah ditukarkan", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ActivityPoin.this, ActivityTiket.class));
                                    }
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }
}
