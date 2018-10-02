package anis.sofiyanti.com.threes;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ActivityLauncher extends AppCompatActivity {

    private final int SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Jika pertama kali aplikasi dibuka
        if(!AppSharedPreferences.isFirstTime(this)){
            //Jika user sudah login dan belum melakukan logout
            if(AppSharedPreferences.isLoggedIn(ActivityLauncher.this)){
                //Masuk ke activity main
                Intent intent = new Intent(ActivityLauncher.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
            //Jika user belum login
            else{
                //Masuk ke activity login
                Intent intent = new Intent(ActivityLauncher.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        }
        else{
            //Jika user baru pertama kali membuka aplikasi, tampilkan logo opening
            setContentView(R.layout.activity_launcher);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ImageView img_logo = findViewById(R.id.img_logo);
                    img_logo.setImageResource(R.drawable.logo_blu);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.activity_launcher_opening);

                            Button btn_start = findViewById(R.id.btn_start);
                            btn_start.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //buat flag bahwa user sudah masuk pertama kali
                                    AppSharedPreferences.setFirstTime(ActivityLauncher.this);

                                    //Masuk ke aktivitas login
                                    Intent intent = new Intent(ActivityLauncher.this, ActivityLogin.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                }
                            });
                        }
                    }, SPLASH_SCREEN_DELAY);
                }
            }, SPLASH_SCREEN_DELAY);
        }

    }
}
