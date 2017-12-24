package com.yangxiutian.example.intnet2;

import javax.sound.sampled.*;
import javax.sound.sampled.Mixer.Info;

import java.io.*;

public class Test1 {
	public static void main(String args[]) {
		Info[] mixerInfo = AudioSystem.getMixerInfo();
		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);

		Port portInfo = new Port(TargetDataLine.class, Port.SPEAKER.getName(), false);
		DataLine dataLineInfo = new DataLine(portInfo.getLineClass(), audioFormat);
		Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);

		TargetDataLine targetDataLine = null;
		try {
			targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
			targetDataLine.open(audioFormat);
		} catch (Exception e) {
			System.out.println("E: " + e);
		}
		int numBytesAvailable = targetDataLine.available();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String exitQ = null;

		targetDataLine.start();
		while (true) {
			byte tempBuffer[] = new byte[10000];

			targetDataLine.read(tempBuffer, 0, tempBuffer.length);
			System.out.println(tempBuffer.toString());
			System.out.println("# bytes available = " + numBytesAvailable);

			try {
				exitQ = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error trying read exit code!");
				System.exit(1);
			}
			if (exitQ.charAt(0) == 'e') {
				System.out.println("Goodbye");
				System.exit(1);
			} // type e to exit
		}
	}
}