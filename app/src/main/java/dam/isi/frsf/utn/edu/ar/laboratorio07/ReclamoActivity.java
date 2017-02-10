package dam.isi.frsf.utn.edu.ar.laboratorio07;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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
        myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {

                final Dialog miDialogo = new Dialog(ReclamoActivity.this);
                miDialogo.setContentView(R.layout.ingresar_distancia);
                miDialogo.setTitle("Reclamos");
                final EditText distanciaReclamo = (EditText) miDialogo.findViewById(R.id.distanciaReclamo);
                Button btnBuscar = (Button) miDialogo.findViewById(R.id.button_Buscar);
                Button btnCancelar = (Button) miDialogo.findViewById(R.id.button_Cancelar);

                btnBuscar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        float[] Resultados = new float[10];
                        ArrayList<Reclamo> listaEncontrados = new ArrayList<Reclamo>();
                        PolylineOptions rectOptions = new PolylineOptions();
                        rectOptions.add(marker.getPosition()).color(Color.BLACK);
                        for(Reclamo i: listaReclamos){
                            if(i.getLatitud()!=marker.getPosition().latitude && i.getLongitud()!=marker.getPosition().longitude) {
                                Resultados = new float[10];
                                Location.distanceBetween(i.getLatitud(),i.getLongitud(),
                                        marker.getPosition().latitude,marker.getPosition().longitude,Resultados);
                                float distanciaCalculada = Resultados[0];
                                if(distanciaCalculada<Float.parseFloat(distanciaReclamo.getText().toString())*1000) {
                                    rectOptions.add(new LatLng(i.getLatitud(),i.getLongitud())).color(Color.BLACK);
                                    listaEncontrados.add(i);
                                }

                            }
                        }
                        rectOptions.add(marker.getPosition()).color(Color.BLACK);
                        Polyline polyline = myMap.addPolyline(rectOptions);
                        myMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));


                        miDialogo.dismiss();
                    }
                });

                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        miDialogo.dismiss();
                    }
                });

                miDialogo.show();


            }
        });
        localizarMiPosicion();
    }
    private void localizarMiPosicion(){
        if (ContextCompat.checkSelfPermission(ReclamoActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},9999);
            return;
        }

        myMap.setMyLocationEnabled(true);


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
