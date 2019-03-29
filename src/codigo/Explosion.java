/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author david
 */
public class Explosion {
    Clip sonidoExplosion;
    Image imagen = null;
    Image imagen2 = null;
        //posición x-y de la explosion
    private int x = 0;
    private int y = 0;
    private int tiempoDeVida = 50;
    
    public int getTiempoDeVida() {
        return tiempoDeVida;
    }

    public void setTiempoDeVida(int tiempoDeVida) {
        this.tiempoDeVida = tiempoDeVida;
    }

    
    public Explosion (){
        try {
            sonidoExplosion = AudioSystem.getClip();
            sonidoExplosion.open(AudioSystem.getAudioInputStream(
                     getClass().getResource("/sonidos/explosion.wav")));
            imagen = ImageIO.read((getClass().getResource("/imagenes/e1.png")));
            imagen2 = ImageIO.read((getClass().getResource("/imagenes/e2.png")));
        } catch (Exception ex) {
        }

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
