package com.example.a14persistenciahilosmusica;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        leeRecord();
    }

    // guardamos el record con un SharedPreferences
    private void guardaRecord() {
        // Guardamos el record
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor miEditor = datos.edit();
        miEditor.putInt("RECORD", record);
        miEditor.apply();
    }

    // recupero el record
    private void leeRecord() {
        // leemos el bundle
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        record = datos.getInt("RECORD", 0);
        TextView caja = (TextView) findViewById(R.id.record);
        caja.setText("Record: " + record);
    }

    public void ayuda(View vista) {
        // el boton de la interface ayuda, cuando pulsemos sobre el (onClick) llama a este
        // metodo, que crea una intencion que ejecutara la clase AyudaActividad.
        // Por último iniciamos la intención.
        Intent intencion = new Intent(this, AyudaActividad.class);
        startActivity(intencion);
    }

    public void dificultad(View vista) {
        // recogemos en dific el texto del boton pulsado
        String dific = (String) ((Button) vista).getText();
        int dificultad = 1;
        if (dific.equals(getString(R.string.medio))) dificultad = 2;
        if (dific.equals(getString(R.string.dificil))) dificultad = 3;

        Intent in = new Intent(this, Gestion.class);
        in.putExtra("DIFICULTAD", dificultad);
        /* en lugar de startActivity uso startActivityForResult. Esta comienza la actividad y
        recoge el resultado de una actividad que ya halla finalizado.
        parametros: in, el intento
                    1, el codigo de solicitud si >= 0, este código se devolverá en
                    onActivityResult() cuando finalice la actividad.
         */
        // startActivity(in);
        startActivityForResult(in, 1);
    }

    @Override
    protected void onActivityResult(int peticion, int codigo, @Nullable Intent puntuacion) {
        // compruebo que la peticion sea la mia y que el codigo sea que todo termino bien
        if (peticion != 1 || codigo != RESULT_OK) return;

        int resultado = puntuacion.getIntExtra("PUNTUACION", 0);

        if (resultado > record) {
            record = resultado;
            // lo mostramos en el textview
            TextView caja = (TextView) findViewById(R.id.record);
            caja.setText("Record: " + resultado);
            // guardamos el record
            guardaRecord();
        }
        String puntuacionPartida = "Tu puntuacion es " + resultado;
        Toast miToast = Toast.makeText(this, puntuacionPartida, Toast.LENGTH_SHORT);
        miToast.setGravity(Gravity.CENTER, 0, 0);
        miToast.show();

        super.onActivityResult(peticion, codigo, puntuacion);
    }

    private int record;
}
