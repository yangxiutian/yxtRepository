package com.yangxiutian.music;

import java.io.File;  
import java.io.IOException;  
  
import javax.sound.sampled.AudioFormat;  
import javax.sound.sampled.AudioInputStream;  
import javax.sound.sampled.AudioSystem;  
import javax.sound.sampled.DataLine;  
import javax.sound.sampled.SourceDataLine;

import com.yangxiutian.Desktop;  
   
public class PlayApe {  
   
    public static void main(String[] args) {  
        final PlayApe player = new PlayApe ();  
//        player.play("D:/xiaomi.ogg");  
        player.play("H:\\Music\\张含韵\\2005-我很张含韵\\CDImage.ape");  
//        player.play(Desktop.value+"李倩 - 一瞬间.wav");  
    }  
   
    public void play(String filePath) {  
        final File file = new File(filePath);  
   
        try {  
            final AudioInputStream in = AudioSystem.getAudioInputStream(file);  
               
            final AudioFormat outFormat = getOutFormat(in.getFormat());  
            final DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);  
   
            final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);  
   
            if (line != null) {  
                line.open(outFormat);  
                line.start();  
                stream(AudioSystem.getAudioInputStream(outFormat, in), line);  
                line.drain();  
                line.stop();  
            }  
   
        } catch (Exception e) {  
            throw new IllegalStateException(e);  
        }  
    }  
   
    private AudioFormat getOutFormat(AudioFormat inFormat) {  
        final int ch = inFormat.getChannels();  
        final float rate = inFormat.getSampleRate(); 
        AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
        System.out.println("inFormat:" + inFormat);
        System.out.println("outFormat:" + outFormat);
        return outFormat;  
    }  
   
    private void stream(AudioInputStream in, SourceDataLine line)  
        throws IOException {  
        final byte[] buffer = new byte[4];  
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {  
            line.write(buffer, 0, n);  
        }  
    }  
}