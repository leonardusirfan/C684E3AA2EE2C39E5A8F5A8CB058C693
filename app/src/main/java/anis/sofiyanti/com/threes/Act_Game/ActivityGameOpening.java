package anis.sofiyanti.com.threes.Act_Game;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import anis.sofiyanti.com.threes.R;

public class ActivityGameOpening extends AppCompatActivity {

    private Runnable loading;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_opening);

        final int LOADING_DURATION = 3000;
        handler = new Handler();
        loading = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ActivityGameOpening.this, ActivityGameMain.class));
                finish();
            }
        };
        handler.postDelayed(loading, LOADING_DURATION);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(loading);
    }
}
