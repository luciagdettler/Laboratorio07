package dam.isi.frsf.utn.edu.ar.laboratorio07;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
/**
 * Created by Usuario on 10/2/2017.
 */
public class ReclamoActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    private final static int CODIGO_RESULTADO_ALTA_RECLAMO=0;
    private ArrayList <Reclamo> listaReclamos=new ArrayList<Reclamo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo);
        SupportMapFragment mapFragmet = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragmet.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        actualizarMapa();
        myMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng punto) {
                Intent i = new Intent(ReclamoActivity.this, AltaReclamoActivity.class);
                i.putExtra("coordenadas", punto);
                startActivityForResult(i, CODIGO_RESULTADO_ALTA_RECLAMO);
            }
        });
    }

    public void actualizarMapa(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},9999);
            return;
        }
        myMap.setMyLocationEnabled(true);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CODIGO_RESULTADO_ALTA_RECLAMO && resultCode==RESULT_OK){
            Reclamo nuevoReclamo = (Reclamo) data.getExtras().get("resultado");
            myMap.addMarker(new MarkerOptions()
                    .position(new LatLng(nuevoReclamo.getLatitud(),nuevoReclamo.getLongitud()))
                    .title("Reclamo de "+nuevoReclamo.getEmail())
                    .snippet(nuevoReclamo.getTitulo())
            );


            listaReclamos.add(nuevoReclamo);
        }
    }
}
