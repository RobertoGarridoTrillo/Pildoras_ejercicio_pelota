package com.example.a14persistenciahilosmusica;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

public class Partida extends androidx.appcompat.widget.AppCompatImageView {

    private int acel;
    private Bitmap pelota, fondo;
    private int tam_pantX, tam_pantY, posX, posY, velX, velY;
    private int tamPelota;
    boolean pelota_sube;


    public Partida(Context contexto, int nivel_dificultad) {

        super(contexto);

        WindowManager manejador_ventana = (WindowManager) contexto.getSystemService(Context.WINDOW_SERVICE);
        Display pantalla = manejador_ventana.getDefaultDisplay();
        Point maneja_coord = new Point(); // integra dos coordenadas

        pantalla.getSize(maneja_coord); // determina el tamaño de la pantalla
        tam_pantX = maneja_coord.x; // tamaño horizontal
        tam_pantY = maneja_coord.y; // tamaño vertical

        // contruye un layout mediante código

        // pintamos el fondo
        BitmapDrawable dibujo_fondo = (BitmapDrawable) ContextCompat.getDrawable(contexto, R.drawable.paisaje_1);
        fondo = dibujo_fondo.getBitmap();
        fondo = Bitmap.createScaledBitmap(fondo, tam_pantX, tam_pantY, false);

        // pintamos la pelota
        BitmapDrawable objetoPelota = (BitmapDrawable) ContextCompat.getDrawable(contexto, R.drawable.pelota_1);
        pelota = objetoPelota.getBitmap();
        tamPelota = tam_pantY / 3;
        pelota = Bitmap.createScaledBitmap(pelota, tamPelota, tamPelota, false);

        posX = tam_pantX / 2 - tamPelota / 2;
        posY = 0 - tamPelota;
        acel = nivel_dificultad * (maneja_coord.y / 400);
    }


    public boolean toque(int x, int y) {

        if (y < tam_pantY / 3) return false;
        if (velY <= 0) return false;
        if (x < posX || x > posX + tamPelota) return false;
        if (y < posY || y > posY + (tamPelota+200) ) return false;

        velY = -velY;

        double desplX = x - (posX + tamPelota / 2);
        desplX = desplX / (tamPelota / 2) * velY / 6;

        velX += (int) desplX;
        return true;
    }


    public boolean movimientoBola() {

        if (posX < 0 - tamPelota) {
            posY = 0 - tamPelota;
            velY = acel;
        }

        posX += velX;
        posY += velY;

        if (posY >= tam_pantY) return true;
        if (posX < 0 || posX + tamPelota > tam_pantX) {
            velX *= -1;
            //return true;
        }
        if (velY < 0) pelota_sube = true;
        if (velY > 0 && pelota_sube) {
            pelota_sube = false;
        }

        velY += acel;
        return false;
    }

    // este es, al fin y al cabo, el metodo principol, que sera llamado tantas veces para pintar
    // la pelota y el fondo

    protected void onDraw(Canvas lienzo) {

        lienzo.drawBitmap(fondo, 0, 0, null);
        //posY = posY+360;
        lienzo.drawBitmap(pelota, posX, posY, null);

    }
}

