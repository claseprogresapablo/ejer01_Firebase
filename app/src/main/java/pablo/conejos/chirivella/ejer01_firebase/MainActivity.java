package pablo.conejos.chirivella.ejer01_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private TextView lblFrase;
    private TextView txtFrase;
    private Button btnguardar;

    private ArrayList<Persona> personasList;

    private FirebaseDatabase database;

    private DatabaseReference refFrase;
    private DatabaseReference refPersona;
    private DatabaseReference refPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblFrase = findViewById(R.id.lblFrase);
        txtFrase = findViewById(R.id.txtFrase);
        btnguardar = findViewById(R.id.btnGuardar);

        personasList = new ArrayList<>();
        crearPersonas();

        database = FirebaseDatabase.getInstance("https://ejer01firebase-default-rtdb.europe-west1.firebasedatabase.app/"); //URL de la base de datos

        // Referencias
        refFrase = database.getReference("frase"); //aqui se pone la ruta del nodo, si no esta se crea y si no pones nada va a la raiz
        refPersona = database.getReference("persona");
        refPersonas = database.getReference("personasLista");

        //Escritura
        Persona p = new Persona("Pablo", 19);
        //refPersona.setValue(p);

        //refPersonas.setValue(personasList);

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refFrase.setValue(txtFrase.getText().toString());

            }
        });


        //Lecturas
        refPersonas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //Para poder leer collecciones necesitamos un gti
                    GenericTypeIndicator<ArrayList<Persona>> gti = new GenericTypeIndicator<ArrayList<Persona>>() {};
                    ArrayList<Persona> personasList = snapshot.getValue(gti);

                    Toast.makeText(MainActivity.this, "Descargados: "+personasList.size(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refPersona.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Persona p = snapshot.getValue(Persona.class);
                    Toast.makeText(MainActivity.this, p.getNombre(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refFrase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String frase = snapshot.getValue(String.class);
                    lblFrase.setText(frase);
                }else {
                    Toast.makeText(MainActivity.this, "No existen registros", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void crearPersonas() {
        for (int i = 0; i < 1000; i++) {

            personasList.add(new Persona("nombre "+1,20));
        }
    }
}