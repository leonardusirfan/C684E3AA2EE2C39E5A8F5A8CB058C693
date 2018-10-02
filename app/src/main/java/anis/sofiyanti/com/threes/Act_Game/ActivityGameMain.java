package anis.sofiyanti.com.threes.Act_Game;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import anis.sofiyanti.com.threes.R;

public class ActivityGameMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main);

        Button btn_play, btn_how, btn_credit;
        btn_play = findViewById(R.id.btn_play);
        btn_how = findViewById(R.id.btn_how);
        btn_credit = findViewById(R.id.btn_credit);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityGameMain.this, ActivityPilihBus.class));
            }
        });

        btn_how.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityGameMain.this, ActivityHowToPlay.class));
            }
        });

        btn_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityGameMain.this, ActivityCredit.class));
            }
        });
    }
}
