/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author david
 */
public class Disparo {
    public Image imagen = null;
    public int x = 0;
    public int y = 2000; //Al principio el disparo comienza fuera de la pantalla
    public boolean disparado = false;
    
    public Disparo(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/disparo_blaster_starwars.png"));
        } catch (IOException ex) {
        }
    }
    public void mueve(){
        if (disparado == true){
            y-=5;
        }
    }
    
    public void posicionaDisparo(Nave _nave){
        x = _nave.x + _nave.imagen.getWidth(null)/2 - imagen.getWidth(null)/2;
        y = _nave.y - _nave.imagen.getHeight(null)/2;
        
    }
}
