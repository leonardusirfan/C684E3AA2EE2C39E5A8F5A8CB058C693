package anis.sofiyanti.com.threes;

public class HalteModel {
    public String nama;
    public double latitude;
    public double longitude;

    public HalteModel(){

    }

    public HalteModel(String nama, double latitude, double longitude){
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNama(){
        return nama;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }
}
