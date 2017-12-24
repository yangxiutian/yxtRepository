package com.yangxiutian.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;

import sk.mlib.Complex;
import sk.mlib.FFT;
/**
 * 支持双声道
 * @author gztzq
 *
 */

public class DrowWave2 extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8570545730079765544L;
		private Deque<Short> dequeL = new LinkedList<Short>();
		private Deque<Short> dequeR = new LinkedList<Short>();
		private Timer timer;
		private Image buffered;
		private Image showing;
		private boolean isRun = true;
		private List<Short> fftBuff = new ArrayList<Short>();
		
		public DrowWave2(int width, int height) {
			setSize(width, height);
//			timer = new Timer();
//			buffered = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
//			timer.schedule(new TimerTask() {
//				@Override
//				public void run() {
////					drow();
//				}
//			}, 100, 1000);//原始值100， 100：第一个参数是延迟时间，第二个参数是间隔时间（延时m毫秒后再以间隔为n毫秒的频率往复调用）
			
		}
		//绘制波形
		private void drow(Graphics g){
//			Graphics g = buffered.getGraphics();
			Color beforeColor = g.getColor();
			
			g.setColor(Color.black);//背景色
			g.fillRect(0, 0, getWidth(), getHeight());

			g.translate(10, getHeight() / 2);//平移坐标系
			synchronized (dequeL) {
				if (dequeL.size() > 1) {
					g.setColor(Color.red);//线的颜色
					Iterator<Short> iter = dequeL.iterator();
					Short p1 = iter.next();
					Short p2 = iter.next();
					
					
					int x1 = 0, x2 = 0;
					while (iter.hasNext()) {
						//画线
						g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p2 * heightRate));
						
						//苗点
//						g.drawOval(x1, (int) (p1 * heightRate), 1, 1);
//						g.drawOval(x2, (int) (p2 * heightRate), 1, 1);
						
						//画线（立体声）
//						g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p3 * heightRate));
//						g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p2 * heightRate));
						
						p1 = p2;
						p2 = iter.next();
						x1 = x2;
						x2 += widthRate;
						
					}
				}
				if (dequeR.size() > 1) {
					g.setColor(Color.blue);//线的颜色
					Iterator<Short> iter = dequeR.iterator();
					Short p1 = iter.next();
					Short p2 = iter.next();
					
					int x1 = 0, x2 = 0;
					while (iter.hasNext()) {
						//画线
						g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p2 * heightRate));
						
						//苗点
//						g.drawOval(x1, (int) (p1 * heightRate), 1, 1);
//						g.drawOval(x2, (int) (p2 * heightRate), 1, 1);
						
						//画线（立体声）
//						g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p3 * heightRate));
//						g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p2 * heightRate));
						
						p1 = p2;
						p2 = iter.next();
						x1 = x2;
						x2 += widthRate;
					}
				}
			}
			g.dispose();//释放绘图对象

//			SwingUtilities.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					showing = buffered;
//					repaint();
//					showing = null;
//				}
//			});
			
			try {
				Thread.sleep(100);//30~100
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			g.setColor(beforeColor);
			repaint();
		}
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (buffered != null) {
//				g.drawImage(buffered, 0, 0, null);
			}
			if(isRun){
				drow(g);
			}
		}
		public void stop(){
			this.isRun = false;
		}
		public void start(){
			if(!this.isRun){
				this.isRun = true;
				drow(getGraphics());//必须手段调用drow，否则paint不会被调用
			}
		}
//		public void update(Graphics g)  
//		{  
//			super.update(g);
//		} 
		
		public void put(short v, short v2) {
			synchronized (dequeL) {
				dequeL.add(v);
				dequeR.add(v2);
				if (dequeL.size() > 2048) {//原始值500
//					drow();//绘制波形
					
					dequeL.removeFirst();
					dequeR.removeFirst();
					
				}
			}
		}

		public void clear() {
			dequeL.clear();
			dequeR.clear();
		}
		static short mergByte(byte h, byte l){
			return (short) ( h<<8 | l);
		}
		static int multiple = 4;//倍数(1~18)
		static float heightRate = multiple;//纵向单位
		static int widthRate = multiple;//横向单位
		public static void main(String[] args) throws Exception {
//			WaveformGraph waveformGraph = new WaveformGraph(500, 300);//原始
			DrowWave2 waveformGraph = new DrowWave2(500*multiple, 300*multiple);
			waveformGraph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			waveformGraph.setVisible(true);
			
			Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = waveformGraph.getWidth();
			int height = waveformGraph.getHeight();
			waveformGraph.setLocation((scrSize.width - width) / 2, (scrSize.height - height) / 2);

			File path = new File("H:\\Music\\");
			File[] files = path.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return file.isFile()
							&& (file.getName().endsWith(".mp3")
									|| file.getName().endsWith(".flac")
									|| file.getName().endsWith(".ape")
									);
				}
			});//所有的音乐文件
			
			AudioChart chart = new AudioChart(500*multiple, 300*multiple);
//			AudioChart chart = new AudioChart();
			chart.setPeaksEnabled(true);
			chart.setPeakColor(Color.red);
			waveformGraph.add(chart);
//			chart.setVisible(true);
			
//			KJScopeAndSpectrumAnalyser jk = new KJScopeAndSpectrumAnalyser();
//			waveformGraph.add(jk);
			
			
			while(true){//循环随机播放文件
				Random random = new Random();
				int randomIdx = random.nextInt(files.length);
				File randomFile = files[randomIdx];//随机播放指定目录下的音频文件
				System.out.println("开始播放：" + randomFile.getName());
				AudioInputStream inAis = AudioSystem.getAudioInputStream(randomFile);
				AudioFormat outFormat = getOutFormat(inAis.getFormat());//格式转换
				AudioInputStream ais = AudioSystem.getAudioInputStream(outFormat, inAis);//流转换【解码】
	//			printFormat(ais.getFormat());
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);  
				
	//			SourceDataLine player = AudioSystem.getSourceDataLine(format);//原先的写法
				SourceDataLine player = (SourceDataLine) AudioSystem.getLine(info);
				chart.startDSP(player);
				
				player.open();
				player.start();
				byte[] buf = new byte[4];
				int len;
				while ((len = ais.read(buf)) != -1) {
	
					if (ais.getFormat().getChannels() == 2) {
						if (ais.getFormat().getSampleSizeInBits() == 16) {
	//						buf[0] = 0;
	//						buf[2] = 0;
							
							waveformGraph.put((short) (buf[1]), (short) (buf[3]));// 左声道, 右声道[ok]
							if(chart.getDisplayMode() != AudioChart.DISPLAY_MODE_OFF){
								chart.writeDSP(buf);
								waveformGraph.stop();
							}else{
								waveformGraph.start();
							}
							
//							jk.process(new float[]{(float) (buf[0]),(float) (buf[1])},
//									new float[]{(float) (buf[2]),(float) (buf[3])}, 4);
							
							
							
	//						System.out.println("低位字节,高位字节=电平值：" + buf[0] + "," + buf[1] + "=" + mergByte(buf[3], buf[1]));
//							waveformGraph.put(mergByte(buf[3], buf[1]), mergByte(buf[2], buf[0]));// 左声道, 右声道[ok]
	//						waveformGraph.put((short) (buf[0]), (short) (buf[2]));// 左声道, 右声道
	//						waveformGraph.put((short) (buf[1]<<8 | buf[0]), (short) (buf[3]<<8|buf[2]));// 左声道, 右声道
							
	//						waveformGraph.put((short) (buf[0]));// 左声道 (buf[1] << 8)| 
	//						 waveformGraph.put((short) ((buf[3] << 8) | buf[2]));//右声道
						} else {
	//						waveformGraph.put(buf[1]);// 左声道
	//						waveformGraph.put(buf[3]);// 左声道
	
							// waveformGraph.put(buf[2]);//右声道
							// waveformGraph.put(buf[4]);//右声道
						}
					} else {
						if (ais.getFormat().getSampleRate() == 16) {
	//						waveformGraph.put((short) ((buf[1] << 8) | buf[0]));
	//						waveformGraph.put((short) ((buf[3] << 8) | buf[2]));
						} else {
	//						waveformGraph.put(buf[1]);
	//						waveformGraph.put(buf[2]);
	//						waveformGraph.put(buf[3]);
	//						waveformGraph.put(buf[4]);
						}
					}
	
					player.write(buf, 0, len);
				}
				player.close();
				ais.close();
			
			}
			
//			waveformGraph.stop();
			
			
		}
		//需要转换一下AudioFormat
		private static AudioFormat getOutFormat(AudioFormat inFormat) {  
	        final int ch = inFormat.getChannels();  
	        final float rate = inFormat.getSampleRate(); 
	        AudioFormat outFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
	        System.out.println("inFormat:" + inFormat);
	        System.out.println("outFormat:" + outFormat);
	        return outFormat;  
	    }  
		
		public static void printFormat(AudioFormat format) {
			System.out.println(format);
		}
	}
	