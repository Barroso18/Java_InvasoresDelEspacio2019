/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author david
 * 
 * La nave del misterio
 */
public class Nave {
    public Image imagen = null;
    public int x = 0;
    public int y = 0;
    private boolean pulsadoIzquierda = false;
    private boolean pulsadoderecha = false;

    
    public Nave(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/x_wing.png"));
        } catch (IOException ex) {
        }
    }
    public void mueve(){
        if(pulsadoIzquierda== true && x>0 ){
            x-=10;
        }
        if(pulsadoderecha== true && x< VentanaJuego.ANCHOPANTALLA - imagen.getWidth(null)){
            x+=10;
        }
        
    }

    public boolean isPulsadoIzquierda() {
        return pulsadoIzquierda;
    }

    public void setPulsadoIzquierda(boolean pulsadoIzquierda) {
        this.pulsadoIzquierda = pulsadoIzquierda;
        this.pulsadoderecha = false;
    }

    public boolean isPulsadoderecha() {
        return pulsadoderecha;
    }

    public void setPulsadoderecha(boolean pulsadoderecha) {
        this.pulsadoderecha = pulsadoderecha;
        this.pulsadoIzquierda = false;
    }
}
