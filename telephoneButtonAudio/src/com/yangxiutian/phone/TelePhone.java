package com.yangxiutian.phone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
/**
 * 电话按键模拟的主面板类
 * @author gztzq
 *
 */
public class TelePhone extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2318624545887546213L;
	private int width;
	private int height;
	
	private JButton bt1 = new JButton("1");
	private JButton bt2 = new JButton("2");
	private JButton bt3 = new JButton("3");
	private JButton btA = new JButton("A");
	
	private JButton bt4 = new JButton("4");
	private JButton bt5 = new JButton("5");
	private JButton bt6 = new JButton("6");
	private JButton btB = new JButton("B");
	
	private JButton bt7 = new JButton("7");
	private JButton bt8 = new JButton("8");
	private JButton bt9 = new JButton("9");
	private JButton btC = new JButton("C");
	
	private JButton btS = new JButton("*");
	private JButton bt0 = new JButton("0");
	private JButton btJ = new JButton("#");
	private JButton btD = new JButton("D");
	
	private JButton[] buttons = new JButton[]{
			bt1, bt2, bt3, btA,	
			bt4, bt5, bt6, btB,	
			bt7, bt8, bt9, btC,	
			btS, bt0, btJ, btD
	};
	/**
	 * 
	 * @param title
	 */
	public TelePhone(String title){
		super(title);
		setSize(600, 500);
		setBackground(Color.WHITE);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = getWidth();
		height = getHeight();
		this.setLocation((scrSize.width - width) / 2, (scrSize.height - height) / 2);
		
		//加组件，设布局
		this.setLayout(new GridLayout(4,4));
		int idx = 0;
		for (JButton button : buttons) {
			button.setFont(new Font("宋体",Font.BOLD, 80));
			button.setForeground(Color.red);
			
			//加事件
			button.addActionListener(new MyActionListener(idx++));
			
			add(button);
		}
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	static class Frequency{
		//电话按键音 频率
		private static int[] cols = new int[]{1209, 1366, 1477, 1633};
		private static int[] rows = new int[]{697, 770, 852, 941};
		//根据数字键获取相应行频率【nun表示键的顺序，从0~15】
		public static int getFrequencyRow(int num){
			return rows[num/4];
		}
		//根据数字键获取相应行列频率【nun表示键的顺序，从0~15】
		public static int getFrequencyCol(int num){
			return cols[num%4];
		}
	}
	
	class MyActionListener implements ActionListener{
		private int idx;//按键索引
		public MyActionListener(int idx){
			this.idx = idx;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("键位索引："+idx);
			try {
				int row = Frequency.getFrequencyRow(idx);
				int col = Frequency.getFrequencyCol(idx);
				PlayData.playFreq(row, col);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
