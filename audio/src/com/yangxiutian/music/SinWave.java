package com.yangxiutian.music;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

public class SinWave extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7350340325187977993L;
	private static final int NUM = 1500;//每个单位占的点数
	private static final double TWOPI = 2 * 3.1415926;
	private static final int STEP = 5;//步进单位
	private static final int TIME = 50;//步进时间间隔ms
	private float frequency = 2f;//控制频率【一个画面出现的周期数， 控制图像水平伸缩】
	private float amplitude = 0.8f;//控制振幅【y轴的大小，控制图像纵向伸缩】
//	private float phase = (float) (-70 + Math.PI*NUM);//控制相位【x轴的开始位置, 控制图像水平移动】
	private float phase = -70;//控制相位【x轴的开始位置, 控制图像水平移动】
	private float electricalLevel = 0f;//电平【y轴的开始位置, 控制图像垂直移动】
	private int width = 600;
	private int height = 500;
	
	private int margin = 0;//没什么卵用
	
	private int outerWidth = width + margin;
	private int outerHeight = height + margin;
	private int i;
	private int[] xPoint = new int[NUM];
	private int[] yPoint = new int[NUM];
	private int[] yCopyPoints = new int[STEP];
	private Timer timer;

	public SinWave() { // 设置面板 出现的尺寸和位置
		// 下面添加一个时间监听器
		timer = new Timer(TIME, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 在这个时间里面主要做两件事情 先截取一段然后黏在后面
				// 而且做的事情越x 无关
				for (i = 0; i < STEP; i++) {
					yCopyPoints[i] = yPoint[i];
				}
				for (i = 0; i < NUM - STEP; i++) {
					yPoint[i] = yPoint[i + STEP];
				}
				for (i = 0; i < STEP; i++) {
					yPoint[i + NUM - STEP] = yCopyPoints[i];
				}
				repaint();
			}
		});
		// 时间监听事件完成之后现在要做的是加一个组件适配器
		addComponentListener(new ComponentAdapter() {
			// 适配器中新建一个函数 给这个函数提那家一个事件
			public void componentResized(ComponentEvent ce) {
				width = getWidth() - margin;
				height = getHeight() - margin;
				for (i = 0; i < NUM; i++) {
					xPoint[i] = width * i / NUM;
					int y = (int) (height / 2 * (1 - Math.sin(TWOPI * (i + phase) * frequency / NUM) * amplitude));
					y += electricalLevel;
					yPoint[i] = y;
				}
			}
		});

		timer.start();
		
		setBackground(Color.WHITE);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((scrSize.width - outerWidth) / 2, (scrSize.height - outerHeight) / 2);
		setSize(outerWidth, outerHeight);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// 上面是主方法里面的实现下面是画图函数
	public void paint(Graphics g) {
		super.paint(g); // 继承
		g.setColor(Color.RED);
		g.drawLine(0, height / 2, width, height / 2);
		g.setColor(Color.GREEN);
		g.drawPolyline(xPoint, yPoint, NUM);

	}

	public static void main(String[] args) {
		new SinWave();
	}
}
