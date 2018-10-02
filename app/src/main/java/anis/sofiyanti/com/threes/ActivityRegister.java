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

public class ActivityRegister extends AppCompatActivity {

    EditText txt_username, txt_password, txt_ulangi_password;
    private DatabaseReference database;
    private LinearLayout layout_loading;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        layout_loading = findViewById(R.id.layout_loading);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        txt_ulangi_password = findViewById(R.id.txt_ulangi_password);

        TextView btn_masuk = findViewById(R.id.btn_masuk);
        Button btn_daftar = findViewById(R.id.btn_daftar);
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = txt_username.getText().toString();
                final String password = txt_password.getText().toString();
                String ulangi_password = txt_ulangi_password.getText().toString();

                if(username.equals("") || password.equals("") || ulangi_password.equals("")){
                    Toast.makeText(ActivityRegister.this, "Username atau Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                else if(username.length() < 5){
                    Toast.makeText(ActivityRegister.this, "Panjang username minimal 5 karakter", Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 5){
                    Toast.makeText(ActivityRegister.this, "Panjang password minimal 5 karakter", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(ulangi_password)){
                    Toast.makeText(ActivityRegister.this, "Password yang dimasukkan tidak sama", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(isNetworkAvailable()){
                        layout_loading.setVisibility(View.VISIBLE);
                        isLoading = true;

                        database = FirebaseDatabase.getInstance().getReference();
                        database.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    layout_loading.setVisibility(View.INVISIBLE);
                                    isLoading = false;

                                    Toast.makeText(ActivityRegister.this, "Username sudah terdaftar!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    layout_loading.setVisibility(View.INVISIBLE);
                                    isLoading = false;

                                    database.child("users").child("user").child(username).child("password").setValue(password);
                                    database.child("users").child("user").child(username).child("poin").setValue(0);

                                    Intent i = new Intent(ActivityRegister.this, ActivityMain.class);
                                    AppSharedPreferences.login(ActivityRegister.this, username);
                                    startActivity(i);

                                    Toast.makeText(ActivityRegister.this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                layout_loading.setVisibility(View.INVISIBLE);
                                isLoading = false;
                                Toast.makeText(getApplicationContext(), "Gagal register!", Toast.LENGTH_LONG).show();
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
