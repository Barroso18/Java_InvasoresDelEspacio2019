/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

/**
 *
 * @author David
 * 
 * 
 */
public class VentanaJuego extends javax.swing.JFrame {
    
    //Declaramos dos variables estaticas
    static int ANCHOPANTALLA = 600;
    static int ALTOPANTALLA = 450;
    
    //Numero de marcianos que van a aparecer
    int filas = 5;
    int columnas = 10; 
    
    BufferedImage buffer = null; 
    Sonido sonido = new Sonido();
    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();
//    Marciano miMarciano = new Marciano();
    Marciano [][] listaMarcianos = new Marciano[filas][columnas];
    boolean direccionMarcianos = false;
    //Contador sirve para decidir que imagen del marciano toca poner
    int contador = 0;
    //Imagen para cargar el spritsheet con todos los sprites del juego
    BufferedImage plantilla = null;
    Image [][] imagenes;
    
    //Declaro una variable para contar las bajas 
    int bajas = 0;
    int puntuacion = 0;
    int puntuacion_maxima = 0;
    
    Timer temporizador = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            bucleDelJuego();
        }
    });
    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        
        //Para cargar el archivo de imagenes
        //Primero la ruta del archivo
        //Segundo el numero de filas que tiene de imagenes
        //Tercero el numero de columnas que tiene de imagenes
        //Cuarto lo que mide de ancho el sprite
        //Quinto lo que mide de alto el sprite
        //Sexto la escala a la que estan los sprites si pones 2 sera 1/2 es decir a la mitad o castear
        imagenes = cargaImagenes("/imagenes/invaders2.png", 5, 4, 64,64,0.5);
        
        miDisparo.imagen = imagenes[2][3];
        setSize(ANCHOPANTALLA,ALTOPANTALLA);
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA,ALTOPANTALLA);
        buffer.createGraphics();
        //Aqui empieza el juego
        temporizador.start();
        
        //Inicializo la posicion inicial de la nave
        miNave.imagen = imagenes[4][5];
        miNave.x = ANCHOPANTALLA /2 - miNave.imagen.getWidth(this)/2;
        miNave.y = ALTOPANTALLA - miNave.imagen.getHeight(this)-40;
        
        
        //1º el numero de fila que estoy creando
        //2º el 
        
        creaFilaDeMarcianos(0,0,2);
        creaFilaDeMarcianos(1,0,2);
        creaFilaDeMarcianos(2,0,2);
        creaFilaDeMarcianos(3,0,2);
        creaFilaDeMarcianos(4,0,2);
        creaFilaDeMarcianos(5,0,2);
        creaFilaDeMarcianos(6,0,2);
        creaFilaDeMarcianos(7,0,2);
        
    }
    
    private void creaFilaDeMarcianos(int numeroFila, int spriteFila, int spriteColumna){
        for(int j=0; j<columnas; j++){
           listaMarcianos[numeroFila][j] = new Marciano();
           listaMarcianos[numeroFila][j].imagen1 = imagenes[spriteFila][spriteColumna];
           listaMarcianos[numeroFila][j].imagen2 = imagenes[spriteFila][spriteColumna+1];
           listaMarcianos[numeroFila][j].x = j*(15 + listaMarcianos[numeroFila][j].imagen1.getWidth(null));
           listaMarcianos[numeroFila][j].y = numeroFila*(10 + listaMarcianos[numeroFila][j].imagen1.getHeight(null));
        }        
    }
    
    /*
        Este metodo va servir para cargar el array de imagenes del sprite sheet . 
        Devolvera un array de 2 dimensiones con las imagenes tal y como esta en el sprite
    */
    
    private Image[][] cargaImagenes (String nombreArchivo, int numFilas, int numColumnas, int ancho, int alto, double escala){
        try {
            plantilla = ImageIO.read(getClass().getResource(nombreArchivo));
        }catch (IOException ex) {}
        Image[][] arrayImagenes = new Image[numFilas][numColumnas];
        //Cargo la imagenes de forma individual en cada imagen del array de imagenes 
        for(int i=0; i<numFilas; i++){
            for(int j=0; j<numColumnas; j++){
                arrayImagenes[i][j] = plantilla.getSubimage(j*ancho, i*alto, ancho, alto);
                double altoEscalado= (int)alto*escala;
                double anchoEscalado= (int)ancho*escala;
                arrayImagenes[i][j]= arrayImagenes[i][j].getScaledInstance((int)anchoEscalado,(int)altoEscalado, Image.SCALE_SMOOTH);
            }
        }
        //La ultima fila del spritesheet solo mide 32 de alto, asi que hay que hacerlo aparte
//        for(int j=0; j<4; j++){
//            imagenes[20+j] = plantilla.getSubimage(j*64, 5*64, 64, 32);
//        }
        
        return arrayImagenes;
    }
    
    private void bucleDelJuego(){
        //Gobierna el redibujado de los objetos en el jPanel1
        //Primero borro todo lo que hay en el buffer
        contador++;
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
        
        ////////////////////////////////////////////////////////////////////////
        //redibujamos aqui cada elemento
        g2.drawImage(miDisparo.imagen, miDisparo.x, miDisparo.y, null);
        g2.drawImage(miNave.imagen, miNave.x, miNave.y, null);
        pintaMarcianos(g2);
        chequeaColision();
        miNave.mueve();
        miDisparo.mueve();
        ////////////////////////////////////////////////////////////////////////
        //********************  Fase final, se dibuja*************************//
        //******************** el buffer de golpe en el jPanel****************//
        
        
        g2 = (Graphics2D) jPanel1.getGraphics();
        g2.drawImage(buffer, 0, 0, null);

    }
    
    private void chequeaColision(){
        //Declaramos unas variables de tipo rectangulo
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        
        rectanguloDisparo.setFrame( miDisparo.x,
                                    miDisparo.y,
                                    miDisparo.imagen.getWidth(null), 
                                    miDisparo.imagen.getHeight(null)
                                    );
        
        for(int i = 0; i<filas; i++){
            for (int j = 0; j<columnas; j++){
                if(listaMarcianos[i][j].vivo){
                    rectanguloMarciano.setFrame(listaMarcianos[i][j].x,
                                                listaMarcianos[i][j].y,
                                                listaMarcianos[i][j].imagen1.getWidth(null),
                                                listaMarcianos[i][j].imagen1.getHeight(null)
                                                );
                    if(rectanguloDisparo.intersects(rectanguloMarciano)){
                        listaMarcianos[i][j].vivo = false;
                        miDisparo.posicionaDisparo(miNave);
                        miDisparo.disparado = false;
                        miDisparo.y = 1000;
                        // Cuando un marciano muere se suma una baja
                        bajas++;
                        //Cuando un marciano muere suena
                        sonido.reproduceAudio("/Sonidos/invaderkilled.wav");
                    }
                }
            }
        }
    }
    
    private void cambiaDireccionMarcianos(){
        for(int i = 0; i<filas; i++){
            for (int j = 0; j<columnas; j++){
                listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX()*-1);
            }
        }
    }
    
    private void pintaMarcianos(Graphics2D _g2){
        
        int anchoMarcianos = listaMarcianos[0][0].imagen1.getWidth(null);
        for(int i = 0; i<filas; i++){
            for(int j = 0; j<columnas; j++){
                if(listaMarcianos[i][j].vivo){
                    listaMarcianos[i][j].mueve();
                    //Chequeo si el marciano ha chocado con la pared 
                    // para cambiar la direccion de todos los marcianos 
                    if(listaMarcianos[i][j].x + anchoMarcianos == ANCHOPANTALLA || 
                            listaMarcianos[i][j].x + anchoMarcianos == 0){
                         direccionMarcianos = true;
                    }

                    if(contador<50){
                         _g2.drawImage(   listaMarcianos[i][j].imagen1, 
                                          listaMarcianos[i][j].x, 
                                          listaMarcianos[i][j].y, 
                                          null);
                    }
                    else if(contador < 100){
                         _g2.drawImage(   listaMarcianos[i][j].imagen2, 
                                      listaMarcianos[i][j].x, 
                                      listaMarcianos[i][j].y, 
                                      null);
                    }
                    else contador = 0;
                }
            }
        }
        if(direccionMarcianos == true ){
            cambiaDireccionMarcianos();
            direccionMarcianos = false;
        }
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 593, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
    switch (evt.getKeyCode()){
        case KeyEvent.VK_LEFT: 
            miNave.setPulsadoIzquierda(true); 
            break;
        case KeyEvent.VK_RIGHT: 
            miNave.setPulsadoderecha(true); 
            break;
        case KeyEvent.VK_SPACE: 
            miDisparo.posicionaDisparo(miNave); 
            miDisparo.disparado = true;
            sonido.reproduceAudio("/Sonidos/shoot.wav");
            break;
    }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
    switch (evt.getKeyCode()){
        case KeyEvent.VK_LEFT: miNave.setPulsadoIzquierda(false); break;
        case KeyEvent.VK_RIGHT: miNave.setPulsadoderecha(false); break;
    }
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
