package com.yangxiutian.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.yangxiutian.Desktop;
/**
 * 原文件备份
 * @author gztzq
 *
 */
public class DrowWaveBak {

	public static class WaveformGraph extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8570545730079765544L;
		private Deque<Short> deque = new LinkedList<Short>();
		private Timer timer;
		private Image buffered;
		private Image showing;

		public WaveformGraph(int width, int height) {
			setSize(width, height);
			timer = new Timer();
			buffered = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Graphics g = buffered.getGraphics();
					g.setColor(Color.white);//背景色
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(Color.blue);//线的颜色

					g.translate(10, getHeight() / 2);//平移坐标系
					synchronized (deque) {
						if (deque.size() > 1) {
							Iterator<Short> iter = deque.iterator();
							Short p1 = iter.next();
							Short p2 = iter.next();
							int x1 = 0, x2 = 0;
							while (iter.hasNext()) {
								g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p2 * heightRate));

								p1 = p2;
								p2 = iter.next();
								x1 = x2;
								x2 += widthRate;
							}
						}
					}
					g.dispose();

					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							showing = buffered;
							repaint();
							showing = null;
						}
					});
				}
			}, 1000, 1000);//原始值100， 100
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (buffered != null) {
				g.drawImage(buffered, 0, 0, null);
			}
		}
		
		public void put(short v) {
			synchronized (deque) {
				deque.add(v);
				if (deque.size() > 500) {
					deque.removeFirst();
				}
			}
		}

		public void clear() {
			deque.clear();
		}
	}
	static int multiple = 4;//倍数(1~18)
	static float heightRate = multiple;//纵向单位
	static int widthRate = multiple;//横向单位
	public static void main(String[] args) throws Exception {
//		WaveformGraph waveformGraph = new WaveformGraph(500, 300);//原始
		WaveformGraph waveformGraph = new WaveformGraph(500*multiple, 300*multiple);
		waveformGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		waveformGraph.setVisible(true);
		
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = waveformGraph.getWidth();
		int height = waveformGraph.getHeight();
		waveformGraph.setLocation((scrSize.width - width) / 2, (scrSize.height - height) / 2);

		AudioInputStream ais = AudioSystem
				.getAudioInputStream(new File(Desktop.value+"李倩 - 一瞬间.wav"));
		printFormat(ais.getFormat());

		SourceDataLine player = AudioSystem.getSourceDataLine(ais.getFormat());

		player.open();
		player.start();
		byte[] buf = new byte[4];
		int len;
		while ((len = ais.read(buf)) != -1) {

			if (ais.getFormat().getChannels() == 2) {
				if (ais.getFormat().getSampleRate() == 16) {
					waveformGraph.put((short) ((buf[1] << 8) | buf[0]));// 左声道
					// waveformGraph.put((short) ((buf[3] << 8) | buf[2]));//右声道
				} else {
					waveformGraph.put(buf[1]);// 左声道
					waveformGraph.put(buf[3]);// 左声道

					// waveformGraph.put(buf[2]);//右声道
					// waveformGraph.put(buf[4]);//右声道
				}
			} else {
				if (ais.getFormat().getSampleRate() == 16) {
					waveformGraph.put((short) ((buf[1] << 8) | buf[0]));
					waveformGraph.put((short) ((buf[3] << 8) | buf[2]));
				} else {
					waveformGraph.put(buf[1]);
					waveformGraph.put(buf[2]);
					waveformGraph.put(buf[3]);
					waveformGraph.put(buf[4]);
				}
			}

			player.write(buf, 0, len);
		}

		player.close();
		ais.close();
	}

	public static void printFormat(AudioFormat format) {
		System.out.println(format.getEncoding() + " => " + format.getSampleRate() + " hz, "
				+ format.getSampleSizeInBits() + " bit, " + format.getChannels() + " channel, " + format.getFrameRate()
				+ " frames/second, " + format.getFrameSize() + " bytes/frame");
	}
	// public static void record() throws LineUnavailableException,
	// InterruptedException {
	// AudioFormat audioFormat = new
	// AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000F, 16, 1, 2, 48000F,
	// false);
	// Info recordDevInfo = new DataLine.Info(TargetDataLine.class,
	// audioFormat);
	//
	// final TargetDataLine recordLine = (TargetDataLine)
	// AudioSystem.getLine(recordDevInfo);
	// final SourceDataLine playLine =
	// AudioSystem.getSourceDataLine(audioFormat);
	//
	// recordLine.open(audioFormat, recordLine.getBufferSize());
	// playLine.open(audioFormat, recordLine.getBufferSize());
	//
	// Thread recorder = new Thread() {
	// public void run() {
	// recordLine.start();
	// playLine.start();
	//
	// FloatControl fc = (FloatControl)
	// playLine.getControl(FloatControl.Type.MASTER_GAIN);
	// double value = 2;
	// float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) /
	// Math.log(10.0) * 20.0);
	// fc.setValue(dB);
	//
	// try {
	// AudioInputStream in = new AudioInputStream(recordLine);
	// byte[] buf = new byte[recordLine.getBufferSize()];
	// int len;
	// while((len=in.read(buf)) != -1) {
	// playLine.write(buf, 0, len);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// recordLine.stop();
	// playLine.stop();
	// }
	// };
	// };
	// recorder.start();
	// recorder.join();
	// }
}