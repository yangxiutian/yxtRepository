package com.yangxiutian.music;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
/**
 * 封装成面板
 * @author gztzq
 *
 */
public class DrowWavePanel extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8570545730079765544L;
	private Deque<Short> dequeL = new LinkedList<Short>();
	private Deque<Short> dequeR = new LinkedList<Short>();
	private long time = 1000;//刷新时间间隔
	
	static int multiple = 2;//倍数(1~18)
	static float heightRate = multiple;//纵向单位
	static int widthRate = multiple;//横向单位
	
	public DrowWavePanel() {
		setSize(500*multiple, 300*multiple);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = getWidth();
		int height = getHeight();
		setLocation((scrSize.width - width) / 2, (scrSize.height - height) / 2);
		setVisible(true);
	}
	//绘制波形
	private void drow(Graphics g){
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
					g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p2 * heightRate));//苗点
//					g.drawOval(x1, (int) (p1 * heightRate), 1, 1);
//					g.drawOval(x2, (int) (p2 * heightRate), 1, 1);
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
					g.drawLine(x1, (int) (p1 * heightRate), x2, (int) (p2 * heightRate));//苗点
//					g.drawOval(x1, (int) (p1 * heightRate), 1, 1);
//					g.drawOval(x2, (int) (p2 * heightRate), 1, 1);
					
					p1 = p2;
					p2 = iter.next();
					x1 = x2;
					x2 += widthRate;
				}
			}
		}
		g.dispose();//释放绘图对象

		try {
			Thread.sleep(time);//30~100
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		g.setColor(beforeColor);
		repaint();
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drow(g);
	}
	
	public void put(short v, short v2) {
		synchronized (dequeL) {
			dequeL.add(v);
			dequeR.add(v2);
			if (dequeL.size() > 500) {//原始值500
				dequeL.removeFirst();
				dequeR.removeFirst();
			}
		}
	}

	public void clear() {
		dequeL.clear();
		dequeR.clear();
	}
}
