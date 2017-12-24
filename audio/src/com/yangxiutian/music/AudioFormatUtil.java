package com.yangxiutian.music;

import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
/**
 * <pre>
 * 本机测试支持：
 * [PCM_SIGNED 8000.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian
 * PCM_SIGNED 16000.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian
 * PCM_SIGNED 44100.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian
 * PCM_SIGNED 8000.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian
 * PCM_SIGNED 16000.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian
 * PCM_SIGNED 44100.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian
 * PCM_SIGNED 8000.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian
 * PCM_SIGNED 16000.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian
 * PCM_SIGNED 44100.0 Hz, 16 bit, mono, 2 bytes/frame, little-endian]
 * </pre>
 * @author gztzq
 *
 */
public class AudioFormatUtil {
	public static Vector<AudioFormat> getSupportedFormats(Class<?> dataLineClass) {
	    /*
	     * These define our criteria when searching for formats supported
	     * by Mixers on the system.
	     */
	    float sampleRates[] = { (float) 8000.0, (float) 16000.0, (float) 44100.0 };
	    int channels[] = { 1, 2 };
	    int bytesPerSample[] = { 2 };

	    AudioFormat format;
	    DataLine.Info lineInfo;

//	    SystemAudioProfile profile = new SystemAudioProfile(); // Used for allocating MixerDetails below.
	    Vector<AudioFormat> formats = new Vector<AudioFormat>();

	    for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
	        for (int a = 0; a < sampleRates.length; a++) {
	            for (int b = 0; b < channels.length; b++) {
	                for (int c = 0; c < bytesPerSample.length; c++) {
	                    format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
	                            sampleRates[a], 8 * bytesPerSample[c], channels[b], bytesPerSample[c],
	                            sampleRates[a], false);
	                    lineInfo = new DataLine.Info(dataLineClass, format);
	                    if (AudioSystem.isLineSupported(lineInfo)) {
	                        /*
	                         * TODO: To perform an exhaustive search on supported lines, we should open
	                         * TODO: each Mixer and get the supported lines. Do this if this approach
	                         * TODO: doesn't give decent results. For the moment, we just work with whatever
	                         * TODO: the unopened mixers tell us.
	                         */
	                        if (AudioSystem.getMixer(mixerInfo).isLineSupported(lineInfo)) {
	                            formats.add(format);
	                        }
	                    }
	                }
	            }
	        }
	    }
	    return formats;
	}
}
