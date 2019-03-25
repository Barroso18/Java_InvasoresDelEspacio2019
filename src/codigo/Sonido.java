/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author david
 */
public class Sonido {
    public void reproduceAudio(String audio){
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream( getClass().getResource(audio) ));
            clip.loop(0);
        }catch (Exception e) {      
        } 
    }
}
