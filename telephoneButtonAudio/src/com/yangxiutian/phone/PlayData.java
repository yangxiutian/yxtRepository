package com.yangxiutian.phone;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
/**
 * 按一定的频率产生波形，并输出到声卡【这个类的主要代码源于互联网】
 * @author gztzq
 *
 */
public class PlayData {
	/**
	 * 
	 * @param frequency 波形1的频率（Hz）
	 * @param frequency2 波形2的频率（Hz）
	 * @throws LineUnavailableException
	 */
	public static void playFreq(double frequency, double frequency2) throws LineUnavailableException
	{
		long start = System.currentTimeMillis();
		Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float sampleRate = 44100.0f;
		int sampleSizeInBits = 16;
		int channels = 2;
		int frameSize = 4;
		float frameRate = sampleRate;
		boolean bigEndian = true;
		AudioFormat audioFormat = new AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, bigEndian);
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, audioFormat);
		SourceDataLine sourceDataLine=(SourceDataLine) AudioSystem.getLine(info);
		sourceDataLine.open(audioFormat, 176400);
		
		int sampleSizeInBytes = audioFormat.getSampleSizeInBits() / 8;//每个样本数据的字节数，位数转换字节数是除8
		//Math.pow(2.0, 19.0)表示2的19次方
		byte audioBuffer[] = new byte[(int)Math.pow(2.0, 13.0) * channels * sampleSizeInBytes];//15-19
		System.out.println("播放频率信息：" + frequency + "," + frequency2);
		System.out.println("音频格式信息：" + audioFormat);
		for ( int i = 0; i < audioBuffer.length; i+=sampleSizeInBytes*channels )
		{
			int wave = (int) (127.0 * Math.sin( 2.0 * Math.PI * frequency * i / (sampleRate * sampleSizeInBytes * channels) )  );
			int wave2 = (int) (127.0 * Math.sin( 2.0 * Math.PI * frequency2 * i / (sampleRate * sampleSizeInBytes * channels) )  );

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
					//存放平均值
					int waveAvg = (wave + wave2) / 2;
					audioBuffer[i] = (byte) (waveAvg);
					audioBuffer[i+1] = (byte)(waveAvg >>> 8);
					
					audioBuffer[i+2] = (byte) (waveAvg);
					audioBuffer[i+3] = (byte)(waveAvg >>> 8);
				}
			}
		}
		System.out.println("总音频数据字节数：" + audioBuffer.length);

		sourceDataLine.write(audioBuffer, 0, audioBuffer.length);
		sourceDataLine.start();
		System.out.println("播放总时间：" + (System.currentTimeMillis() - start) + "ms.");
		
	}
}
