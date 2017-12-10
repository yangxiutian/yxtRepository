package com.yangxiutian.phone;
/**
 * 每个电话按键的声音是由两个固定频率的声音波形混合成的（此知识源于互联网），电话按键对应的声音波形频率如下表：
 * <style>
 * td { text-align:center; border:2px solid #a1a1a1; background:#dddddd; width:10px; height:10px; border-radius:5%; -moz-border-radius:50%} 
 * </style>
 * <table border="1">
 * 	<tr>
 * 		<th>&nbsp;</th> <th>1209 Hz</th><th>1336 Hz</th> <th>1447 Hz</th> <th>1633 Hz</th>
 * 	</tr>
 * 	<tr>
 * 		<th>697 Hz</th> <td>1</td> <td>2</td> <td>3</td> <td>A</td>
 * 	</tr>
 * 	<tr>
 * 		<th>770 Hz</th> <td>4</td> <td>5</td> <td>6</td> <td>B</td>
 * 	</tr>
 *  <tr>
 * 		<th>852 Hz</th> <td>7</td> <td>8</td> <td>9</td> <td>C</td>
 * 	</tr>
 * 	<tr>
 * 		<th>941 Hz</th> <td>*</td> <td>0</td> <td>#</td> <td>D</td>
 * 	</tr>
 * </table>
 * 
 * @author gztzq
 *
 */
public class Frequency {
	//电话按键音 频率
	private static int[] cols = new int[]{1209, 1366, 1477, 1633};
	private static int[] rows = new int[]{697, 770, 852, 941};
	/**
	 * 根据数字键获取相应行频率
	 * @param num 表示键的顺序，从0~15
	 * @return
	 */
	public static int getFrequencyRow(int num){
		return rows[num/4];
	}
	/**
	 * 根据数字键获取相应行列频率
	 * @param num 表示键的顺序，从0~15
	 * @return
	 */
	public static int getFrequencyCol(int num){
		return cols[num%4];
	}
}
