package anis.sofiyanti.com.threes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class ActivityDetail extends AppCompatActivity {

    private BusModel bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(getIntent().hasExtra("bus")){
            Gson gson = new Gson();
            bus = gson.fromJson(getIntent().getStringExtra("bus"), BusModel.class);
        }

        String penumpang = "Penumpang : ";
        penumpang += bus.penumpang + "/40";

        TextView txt_penumpang = findViewById(R.id.txt_penumpang);
        txt_penumpang.setText(penumpang);
        Button btn_done = findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
