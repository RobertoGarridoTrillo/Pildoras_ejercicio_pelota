package com.example.a14persistenciahilosmusica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

public class Gestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        botes = 0;
        golpeo = MediaPlayer.create(this, R.raw.golpeobalon);
        fin = MediaPlayer.create(this, R.raw.finaljuego);

        // Recogo los datos guardados desde MainActivity con un Bundle
        Bundle extras = getIntent().getExtras();
        dificultad = extras.getInt("DIFICULTAD");
        //
        partida = new Partida(getApplicationContext(), dificultad);
        setContentView(partida); // coloca el contexto en la vista
        // comienza el runnable
        temporizador = new Handler();
        // le doy un retraso inicial de 2 segundos
        temporizador.postDelayed(elHilo, 2000);
    }


    private Runnable elHilo = new Runnable() {
        @Override
        public void run() {
            if (partida.movimientoBola()) fin();
            else {
                // elimina el contenido de ImageView y llama de nuevo a onDraw
                partida.invalidate();
            // determina cada cuanto tiempo tiene que "borrar y pintar"
            temporizador.postDelayed(elHilo, 1000 / FPS);
        }
            }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (partida.toque(x, y)) {
            botes++;
            // sonido
            golpeo.start();
        }
        return false;
    }


    public void fin() {

        // sonido
        fin.start();
        //Remove any pending posts of callbacks and sent messages whose obj is token.
        // If token is null, all callbacks and messages will be removed.
        try { // sacado de internet
            temporizador.removeCallbacks(elHilo);
            temporizador.removeCallbacksAndMessages(null);
            temporizador = null;
            elHilo = null;
        } catch (Exception e){
            System.out.println("Error: "+e.toString());
        }

        // guardo el record en un bundle
        Intent in = new Intent();
        in.putExtra("PUNTUACION", botes);
        System.out.println("nombre " + in.getExtras().toString());
        System.out.println("PUNTUACION " + botes);
        // incluimos el codigo de resultado que nos pide onActivityResult para saber que
        // ha finalizado correctamente (mas el Intent) que se me olvido
        setResult(RESULT_OK, in);
        finish();// mata la actividad actual
    }

    // campos de clase
    private Partida partida; // instancia de la clase partida
    private int dificultad;
    private int FPS = 20; // Fotogramas por segundos
    private Handler temporizador; // permite enviar y procesar mensajes y objetos Runnable con hilos
    private int botes; // guarda el numero de rebotes
    private MediaPlayer golpeo;
    private MediaPlayer fin;
}
