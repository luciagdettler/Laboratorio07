package dam.isi.frsf.utn.edu.ar.laboratorio07;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Usuario on 23/11/2016.
 */
public class AltaReclamoActivity extends AppCompatActivity {

    private EditText titulo;
    private EditText telefono;
    private EditText email;
    private Button reclamar;
    private Button cancelar;
    LatLng ubicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.alta_reclamo_activity);

        titulo=(EditText) findViewById(R.id.tit);
        telefono=(EditText) findViewById(R.id.tel);
        email=(EditText) findViewById(R.id.mail);
        reclamar=(Button)findViewById(R.id.reclamar);
        cancelar=(Button)findViewById(R.id.cancelar);

        Bundle extras = getIntent().getExtras();
        ubicacion = (LatLng) extras.get("coordenadas");

        reclamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reclamo rec = new Reclamo();

                rec.setTitulo(titulo.getText().toString());
                rec.setTelefono(telefono.getText().toString());
                rec.setEmail(email.getText().toString());
                rec.setLongitud(ubicacion.longitude);
                rec.setLatitud(ubicacion.latitude);

                Intent returnIntent=new Intent();
                returnIntent.putExtra("resultado",rec);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
