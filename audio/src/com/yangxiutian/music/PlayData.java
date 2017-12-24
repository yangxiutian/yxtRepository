package com.yangxiutian.music;

import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class PlayData {
	static DrowWavePanel wavePanel = new DrowWavePanel();
	public static void playFreq(AudioFormat audioFormat, double frequency, double frequency2, SourceDataLine sourceDataLine, long time)
	{
		double sampleRate = audioFormat.getSampleRate();
		int sampleSizeInBytes = audioFormat.getSampleSizeInBits() / 8;//每个样本数据的字节数，位数转换字节数是除8
		int channels = audioFormat.getChannels();
		//Math.pow(2.0, 19.0)表示2的19次方
		byte audioBuffer[] = new byte[(int)Math.pow(2.0, 15.0) * channels * sampleSizeInBytes];//15-19
//		byte audioBuffer[] = new byte[getByteLenByTime(audioFormat, time)];
		System.out.println(audioFormat);
//		int level = (int) Math.pow(2.0, audioFormat.getSampleSizeInBits()) - 1;//量化电平最大值，原始值127
		int level = 127;//量化电平最大值，原始值127
		for ( int i = 0; i < audioBuffer.length; i+=sampleSizeInBytes*channels )
		{
			int wave = (int) (level * Math.sin( 2.0 * Math.PI * frequency * i / (sampleRate * sampleSizeInBytes * channels) )  );
			int wave2 = (int) (level * Math.sin( 2.0 * Math.PI * frequency2 * i / (sampleRate * sampleSizeInBytes * channels) )  );

//			wave = (wave > 0 ? 127 : -127);
			if ( channels == 1 )
			{
				if ( sampleSizeInBytes == 1 )
				{
					audioBuffer[i] = (byte) (wave);
				}

				else if ( sampleSizeInBytes == 2 )
				{
					audioBuffer[i] = (byte) (wave);
					audioBuffer[i+1] = (byte)(wave >>> 8);
				}
			}

			else if ( channels == 2 )
			{
				if ( sampleSizeInBytes == 1 )
				{
					audioBuffer[i] = (byte) (wave);
					audioBuffer[i+1] = (byte) (wave);
				}

				else if ( sampleSizeInBytes == 2 )
				{
					audioBuffer[i] = (byte) (wave);
					audioBuffer[i+1] = (byte)(wave >>> 8);

					audioBuffer[i+2] = (byte) (wave2);//第二个声道存放第二个频率
					audioBuffer[i+3] = (byte)(wave2 >>> 8);
				}
			}
		}
//		System.out.println(audioBuffer[0]);
//		System.out.println(Arrays.toString(audioBuffer));
		System.out.println(audioBuffer.length);
		
		sourceDataLine.write(audioBuffer, 0, audioBuffer.length);
		
	}
	public static void playFreq(double frequency, double frequency2) throws LineUnavailableException
	{

		long start = System.currentTimeMillis();
		Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float sampleRate = 44100.0f;
		int sampleSizeInBits = 16;
		int channels = 2;
		int frameSize = 4;
//		float frameRate = sampleRate*channels;
		float frameRate = sampleRate;
		boolean bigEndian = false;
		AudioFormat audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, audioFormat);
		SourceDataLine sourceDataLine=(SourceDataLine) AudioSystem.getLine(info);
		sourceDataLine.open(audioFormat, 176400);
		
		int sampleSizeInBytes = audioFormat.getSampleSizeInBits() / 8;//每个样本数据的字节数，位数转换字节数是除8
		//Math.pow(2.0, 19.0)表示2的19次方
		byte audioBuffer[] = new byte[(int)Math.pow(2.0, 13.0) * channels * sampleSizeInBytes];//15-19
//		byte audioBuffer[] = new byte[getByteLenByTime(audioFormat, time)];
		System.out.println("播放频率信息：" + frequency + "," + frequency2);
		System.out.println("音频格式信息：" + audioFormat);
//		int level = (int) Math.pow(2.0, audioFormat.getSampleSizeInBits()) - 1;//量化电平最大值，原始值127
		int level = 127;//量化电平最大值，原始值127
		for ( int i = 0; i < audioBuffer.length; i+=sampleSizeInBytes*channels )
		{
			int wave = (int) (level * Math.sin( 2.0 * Math.PI * frequency * i / (sampleRate * sampleSizeInBytes * channels) )  );
			int wave2 = (int) (level * Math.sin( 2.0 * Math.PI * frequency2 * i / (sampleRate * sampleSizeInBytes * channels) )  );

//			wave = (wave > 0 ? 127 : -127);
			if ( channels == 1 )
			{
				if ( sampleSizeInBytes == 1 )
				{
					audioBuffer[i] = (byte) (wave);
				}

				else if ( sampleSizeInBytes == 2 )
				{
					audioBuffer[i] = (byte) (wave);
					audioBuffer[i+1] = (byte)(wave >>> 8);
				}
			}

			else if ( channels == 2 )
			{
				if ( sampleSizeInBytes == 1 )
				{
					audioBuffer[i] = (byte) (wave);
					audioBuffer[i+1] = (byte) (wave);
				}

				else if ( sampleSizeInBytes == 2 )
				{
//					audioBuffer[i] = (byte) (wave);
//					audioBuffer[i+1] = (byte)(wave >>> 8);
//
//					audioBuffer[i+2] = (byte) (wave2);//第二个声道存放第二个频率
//					audioBuffer[i+3] = (byte)(wave2 >>> 8);
					
					//存放平均值
					int waveAvg = (wave + wave2) / 2;
					if(audioFormat.isBigEndian()){//大头模式
						audioBuffer[i] = (byte) (waveAvg);
						audioBuffer[i+1] = (byte)(waveAvg >>> 8);
						
						audioBuffer[i+2] = (byte) (waveAvg);
						audioBuffer[i+3] = (byte)(waveAvg >>> 8);
					}else{//小头模式
						audioBuffer[i] = (byte)(waveAvg >>> 8);
						audioBuffer[i+1] = (byte) (waveAvg);
						System.out.println("低位字节,高位字节：" + audioBuffer[i] + "," + audioBuffer[i+1]);
						
						audioBuffer[i+2] = (byte)(waveAvg >>> 8);
						audioBuffer[i+3] = (byte) (waveAvg);
					}
					wavePanel.put((short)wave, (short)wave2);
				}
			}
		}
//		System.out.println(audioBuffer[0]);
//		System.out.println(Arrays.toString(audioBuffer));
		System.out.println("总音频数据字节数：" + audioBuffer.length);

		sourceDataLine.write(audioBuffer, 0, audioBuffer.length);
		sourceDataLine.start();
		System.out.println("播放总时间：" + (System.currentTimeMillis() - start) + "ms.");
		
	}
	//根据时间获取到数据的字节数，时间单位是毫秒
	static int getByteLenByTime(AudioFormat audioFormat, long time){
		int sampleSizeInBytes = audioFormat.getSampleSizeInBits() / 8;//每个样本数据的字节数，位数转换字节数是除8
		float sampleRate = audioFormat.getSampleRate();//每秒的样本数（频率）
		float sampleRateMicSec = sampleRate / 1000;//1毫秒的样本数
		int oneMicSecLen = (int) (sampleSizeInBytes * sampleRateMicSec);//1豪秒的字节数
		return (int) (oneMicSecLen * time);
	}
}
