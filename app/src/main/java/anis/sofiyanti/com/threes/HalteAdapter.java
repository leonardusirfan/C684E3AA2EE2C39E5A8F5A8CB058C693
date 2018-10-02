package anis.sofiyanti.com.threes;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class HalteAdapter extends RecyclerView.Adapter<HalteAdapter.HalteViewHolder> {

    private List<HalteModel> list_halte;
    private GoogleMap googleMap;

    HalteAdapter(List<HalteModel> list_halte, GoogleMap googleMap) {
        this.list_halte = list_halte;
        this.googleMap = googleMap;
    }

    @NonNull
    @Override
    public HalteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HalteViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_halte, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HalteViewHolder halteViewHolder, int i) {
        final HalteModel halte = list_halte.get(i);

        halteViewHolder.txt_halte.setText(halte.getNama());
        if(i == 0){
            halteViewHolder.img_halte.setImageResource(R.drawable.tiga);
        }
        else if(i == list_halte.size() - 1){
            halteViewHolder.img_halte.setImageResource(R.drawable.dua);
        }
        else{
            halteViewHolder.img_halte.setImageResource(R.drawable.satu);
        }

        halteViewHolder.layout_halte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(halte.getLatitude(), halte.getLongitude())));
            }
        });
    }

    public int getItemCount() {
        return list_halte.size();
    }

    class HalteViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_halte;
        private ImageView img_halte;
        private ConstraintLayout layout_halte;

        HalteViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_halte = itemView.findViewById(R.id.txt_halte);
            img_halte = itemView.findViewById(R.id.img_halte);
            layout_halte = itemView.findViewById(R.id.layout_halte);
        }
    }
}

