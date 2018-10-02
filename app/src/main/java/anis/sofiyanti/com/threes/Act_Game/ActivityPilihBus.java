package anis.sofiyanti.com.threes.Act_Game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import anis.sofiyanti.com.threes.R;

public class ActivityPilihBus extends AppCompatActivity {

    private int BUS_BIRU = 0;
    private int BUS_MERAH = 1;
    private int selected = BUS_MERAH;

    private ImageView img_bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_bus);

        Button btn_merah, btn_biru, btn_play;
        img_bus = findViewById(R.id.img_bus);
        btn_merah = findViewById(R.id.btn_merah);
        btn_biru = findViewById(R.id.btn_biru);
        btn_play = findViewById(R.id.btn_play);

        btn_merah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_bus.setImageResource(R.drawable.pilih_merah);
                selected = BUS_MERAH;
            }
        });

        btn_biru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_bus.setImageResource(R.drawable.pilih_biru);
                selected = BUS_BIRU;
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(selected == 0 ? "BIRU":"MERAH");
            }
        });
    }
}
