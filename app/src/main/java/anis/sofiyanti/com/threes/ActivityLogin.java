package anis.sofiyanti.com.threes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityLogin extends AppCompatActivity {

    private EditText txt_username, txt_password;
    private DatabaseReference database;
    private LinearLayout layout_loading;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout_loading = findViewById(R.id.layout_loading);
        TextView btn_daftar = findViewById(R.id.btn_daftar);
        Button btn_masuk = findViewById(R.id.btn_masuk);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivityRegister.class));
            }
        });

        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = txt_username.getText().toString();
                final String password = txt_password.getText().toString();

                if(username.equals("") || password.equals("")){
                    Toast.makeText(ActivityLogin.this, "Username atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(isNetworkAvailable()){
                        layout_loading.setVisibility(View.VISIBLE);
                        isLoading = true;

                        database = FirebaseDatabase.getInstance().getReference();
                        //Exception jika data tidak ada
                        database.child("users").child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                System.out.println(dataSnapshot);
                                if(dataSnapshot.hasChild(username)) {
                                    if(password.equals(dataSnapshot.child(username).child("password").getValue(String.class))){
                                        layout_loading.setVisibility(View.INVISIBLE);
                                        isLoading = false;

                                        //khusus user anissofy ketika login poin = 3700
                                        if(username.equals("anissofy")){
                                            database.child("users").child("user").child(username).child("poin").setValue(3700);
                                        }

                                        Intent i = new Intent(ActivityLogin.this, ActivityMain.class);
                                        AppSharedPreferences.login(ActivityLogin.this, username);
                                        startActivity(i);
                                        finish();
                                    }
                                    else{
                                        layout_loading.setVisibility(View.INVISIBLE);
                                        isLoading = false;
                                        Toast.makeText(ActivityLogin.this, "Password salah", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    layout_loading.setVisibility(View.INVISIBLE);
                                    isLoading = false;
                                    Toast.makeText(ActivityLogin.this, "Username tidak ditemukan", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                layout_loading.setVisibility(View.INVISIBLE);
                                isLoading = false;
                                Toast.makeText(getApplicationContext(), "Gagal membaca database!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Tidak tersambung kedalam jaringan!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager != null){
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        if(!isLoading){
            super.onBackPressed();
        }
    }
}
