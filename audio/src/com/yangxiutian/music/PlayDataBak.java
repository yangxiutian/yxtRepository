package com.yangxiutian.music;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

public class PlayDataBak {
	public static void playFreq(AudioFormat audioFormat, double frequency, SourceDataLine sourceDataLine)
	{
		double sampleRate = audioFormat.getSampleRate();
		int sampleSizeInBytes = audioFormat.getSampleSizeInBits() / 8;//每个样本数据的字节数，位数转换字节数是除8
		int channels = audioFormat.getChannels();
		//Math.pow(2.0, 19.0)表示2的19次方
		byte audioBuffer[] = new byte[(int)Math.pow(2.0, 19.0) * channels * sampleSizeInBytes];
		System.out.println(audioFormat);
		for ( int i = 0; i < audioBuffer.length; i+=sampleSizeInBytes*channels )
		{
			int wave = (int) (127.0 * Math.sin( 2.0 * Math.PI * frequency * i / (sampleRate * sampleSizeInBytes * channels) )  );

			//wave = (wave > 0 ? 127 : -127);
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

					audioBuffer[i+2] = (byte) (wave);
					audioBuffer[i+3] = (byte)(wave >>> 8);
				}
			}
		}
//		System.out.println(audioBuffer[0]);
//		System.out.println(Arrays.toString(audioBuffer));
		System.out.println(audioBuffer.length);
		
		sourceDataLine.write(audioBuffer, 0, audioBuffer.length);
		
	}
}
