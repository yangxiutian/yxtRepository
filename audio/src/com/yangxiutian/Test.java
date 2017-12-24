package com.yangxiutian;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.yangxiutian.music.AudioFormatUtil;
import com.yangxiutian.music.PlayData;

public class Test {
	volatile int i;
	public static void main(String[] args) throws Exception {
		good();
//		test();
		
//		play();
//		play2();
	}
	static void play2() throws LineUnavailableException{
		Info[] infos = AudioSystem.getMixerInfo();
		Info captInfo = null;//能获取系统音频的info
		for (Info info : infos) {
			try {
//				String name = info.getName();
				String name = info.toString();
				name = new String(name.getBytes("ISO-8859-1"), "GBK");
//				name = new String(name.getBytes("US-ASCII"), "GBK");
				
				System.out.println(info.getName()+":"+name);
				
				Mixer mixer = AudioSystem.getMixer(info);
				
//				DataLine.Info lineInfo = new DataLine.Info(SourceDataLine.class, getDefFormat());
				DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, getDefFormat());
				
				boolean isSupported = AudioSystem.isLineSupported(lineInfo);
				
				boolean isSupported2 = (mixer.isLineSupported(lineInfo));
				
				System.out.println("----" + isSupported + "," + isSupported2);
				if("Ö÷ÉùÒô²¶»ñÇý".equals(info.getName()) && isSupported && isSupported2){//主声音捕获驱
					captInfo = info;
					
					AudioSystem.getLine(mixer.getLineInfo());///
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		Mixer mixer = AudioSystem.getMixer(captInfo);
		TargetDataLine target = null;
//		AudioSystem.isLineSupported(captInfo);
		
		
		TargetDataLine line;
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, 
		    getDefFormat()); // format is an AudioFormat object
		if (!AudioSystem.isLineSupported(mixer.getLineInfo())) {
		    // Handle the error.
		    }
		    // Obtain and open the line.
		try {
		    line = (TargetDataLine) AudioSystem.getLine(mixer.getLineInfo());
		    line.open(getDefFormat());
		    
		    System.out.println("=========");
		} catch (LineUnavailableException ex) {
		        // Handle the error.
		    //... 
		}
		
		
//		try {
//			target = AudioSystem.getTargetDataLine(getDefFormat(), mixer.getMixerInfo());
//			DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, getDefFormat());
//			
//			target.open();
//			
//			target.start();
//			byte[] buffer = new byte[1024*10];//缓存10k数据
//			for (int i = 0; i < 100; i++) {
//				target.read(buffer, 0, buffer.length);
//				System.out.println(Arrays.toString(buffer));
//			}
//			
//			
//		} catch (LineUnavailableException e) {
//			e.printStackTrace();
//		}
	}
	
	static AudioFormat getDefFormat(){
		Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float sampleRate = 44100.0f;//采样率
		int sampleSizeInBits = 16;//采样位深
		int channels = 2;//声道数
		int frameSize = sampleSizeInBits/8 * channels;//帧字节数
//		float frameRate = sampleRate*channels;
		float frameRate = sampleRate;//帧速率
		boolean bigEndian = true;//大尾数法
		Map properties = new HashMap();
		AudioFormat fmt = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian, properties);
		return fmt;
	}
	
	static void play() throws Exception{
//		AudioInputStream ais=AudioSystem.getAudioInputStream( 
//				new File(Desktop.value+"李倩 - 一瞬间.wav"));
		//每秒的样本数、每个样本中的位数、声道数、是否有符号、big-endian(true)/little-endian(false) 
//		AudioFormat fmt= ais.getFormat() ;
		
		Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float sampleRate = 44100.0f;//采样率
		int sampleSizeInBits = 16;//采样位深
		int channels = 2;//声道数
		int frameSize = 4;//帧字节数
//		float frameRate = sampleRate*channels;
		float frameRate = sampleRate;//帧速率
		boolean bigEndian = true;//大尾数法
		Map properties = new HashMap();
		AudioFormat fmt = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian, properties);
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, fmt);
		
		System.out.println(AudioFormatUtil.getSupportedFormats(SourceDataLine.class));
		
		SourceDataLine obj=(SourceDataLine) AudioSystem.getLine(info);
		obj.open(fmt, 176400);
		
		double frequency = 880.0d;//频率，单位Hz
		int bytes = 2;
		byte[] startData = new byte[1024 * bytes];
		obj.write(startData, 0, startData.length);//需要一定的数据才能启动
		obj.start();
		
		Thread.sleep(1000);
		
		long start = System.currentTimeMillis();
		
		long time = 1000;//时间（ms）
		
		//电话按键音
		int[] rows = new int[]{1209, 1366, 1477, 1633};
		int[] cols = new int[]{697, 770, 852, 941};
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < cols.length; j++) {
				int row = rows[i];
				int col = cols[j];
				PlayData.playFreq(fmt, row, col, obj, time);
				obj.start();//yxt add
				
			}
		}
		
		
		int[] frequencys = new int[]{1000
				/*262,//低1　DO 
				277,//#1　DO#
				294,//低2　RE
				311,//#2 RE#
				330,//低 3 M
				349,//低 4 FA
				370,//# 4 FA#
				392,//低 5 SO
				415,//# 5 SO#
				440,//低 6 LA
				466,//# 6 
				494,//低 7 SI
				523,//中 1 DO
				554,//# 1 DO#
				587,//中 2 RE
				622,//# 2 RE#
				659,//中 3 M
				698,//中 4 FA
				
				740,//# 4 FA#
				784,//中 5 SO
				831,//# 5 SO#
				880,//中 6 LA
				932,//# 6 
				988,//中 7 SI
				1046,//高 1 DO
				1109,//# 1 DO#
				1175,//高 2 RE
				1245,//# 2 RE#
				1318,//高 3 M
				1397,//高 4 FA
				1480,//# 4 FA#
				1568,//高 5 SO
				1661,//# 5 SO#
				1760,//高 6 LA
				1865,//# 6 
				1967 //高 7 SI
*/		}; 
//		for (int i = 0; i < frequencys.length; i++) {
//			PlayData.playFreq(fmt, frequencys[i], obj, time);
//			obj.start();//yxt add
			
//		}
		System.out.println("time:" + (System.currentTimeMillis() - start) + "ms.");
	}
	static void test() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream ais=AudioSystem.getAudioInputStream( 
				new File(Desktop.value+"李倩 - 一瞬间.wav"));
		//每秒的样本数、每个样本中的位数、声道数、是否有符号、big-endian(true)/little-endian(false) 
		AudioFormat fmt= ais.getFormat() ;
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, fmt);
		SourceDataLine obj=(SourceDataLine) AudioSystem.getLine(info);
		obj.open(fmt, 176400);
		
		AudioBuffer ab=new AudioBuffer(ais);
		
		
		byte[] buff=new byte[1024];
		int num;
		int idx=0;
		ab.pos(1000000);//设置帧索引
		while((num=ab.back(buff))!=-1){//反向播放
			obj.write(buff, 0, num);
			obj.start();
			System.out.println(idx+++","+buff[0]+","+ab.isBack());
		}
		while((num=ab.read(buff))!=-1){//正向播放
			obj.write(buff, 0, num);
			obj.start();
			System.out.println(idx+++","+buff[0]+","+ab.isBack());
		}
	}

	static void good() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		 AudioInputStream ais=AudioSystem.getAudioInputStream( 
					new File(Desktop.value+"李倩 - 一瞬间.wav"));
			//每秒的样本数、每个样本中的位数、声道数、是否有符号、big-endian(true)/little-endian(false) 
			AudioFormat fmt= ais.getFormat() ;
			DataLine.Info info=new DataLine.Info(SourceDataLine.class, fmt);
			SourceDataLine obj=(SourceDataLine) AudioSystem.getLine(info);
			obj.open(fmt, 176400);
			
			byte[] buff=new byte[1024];
			int num=0;
			while((num=ais.read(buff))!=-1){
				obj.write(buff, 0, num);
				obj.start();
			}
	}
	
}
