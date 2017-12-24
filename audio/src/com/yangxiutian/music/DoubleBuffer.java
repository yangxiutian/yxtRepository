package com.yangxiutian.music;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * 双缓冲解决闪屏的例子
 * 
 * @author gztzq
 *
 */
public class DoubleBuffer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8361789937065981511L;
	public paintThread pT;// 绘图线程
	public int ypos = -80; // 小圆左上角的纵坐标

	private Image iBuffer;
	private Graphics gBuffer;

	public DoubleBuffer()// 构造函数
	{
		pT = new paintThread(this);
		this.setResizable(false);
		this.setSize(300, 300); // 设置窗口的首选大小
		this.setVisible(true); // 显示窗口
		pT.start();// 绘图线程启动
	}

	public void paint(Graphics scr) // 重载绘图函数
	{
//		scr.fillRect(0, 0, this.getSize().width, this.getSize().height);
		scr.setColor(Color.RED);// 设置小圆颜色
		scr.fillOval(90, ypos, 80, 80); // 绘制小圆
	}

	public void update(Graphics scr) {
		if (iBuffer == null) {
			iBuffer = createImage(this.getSize().width, this.getSize().height);
			gBuffer = iBuffer.getGraphics();
		}
		gBuffer.setColor(getBackground());
		gBuffer.fillRect(0, 0, this.getSize().width, this.getSize().height);
		paint(gBuffer);
		scr.drawImage(iBuffer, 0, 0, this);
	}

	public static void main(String[] args) {
		DoubleBuffer DB = new DoubleBuffer();// 创建主类的对象
		DB.addWindowListener(new WindowAdapter()// 添加窗口关闭处理函数
		{
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}

class paintThread extends Thread// 绘图线程类
{
	DoubleBuffer DB;

	public paintThread(DoubleBuffer DB) // 构造函数
	{
		this.DB = DB;
	}

	public void run()// 重载run()函数
	{
		while (true)// 线程中的无限循环
		{
			try {
				sleep(30); // 线程休眠30ms
			} catch (InterruptedException e) {
			}
			DB.ypos += 5; // 修改小圆左上角的纵坐标
			if (DB.ypos > 300) // 小圆离开窗口后重设左上角的纵坐标
				DB.ypos = -80;
			DB.repaint();// 窗口重绘
		}
	}
}