package com.yangxiutian;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
/**
 * 依托java.nio.ByteBuffer类 根据操作其postion 和 limit实现音频的反向播放</br>
 * 本类目前只简单的处理PCM数据 有望以后写处理mp3的。
 * <p>在read和back方法中有很多判断，遇到性能瓶颈时可从这些地方优化。</p>
 * @author yxt
 * @version 1.0 2012-9-20 0:08:08
 */
public class AudioBuffer {
	private ByteBuffer data;//缓冲主体数据
	private AudioFormat fmt;//音频格式
	/**
	 * 构造一定容量的音频缓冲
	 * @param cap 容量
	 * @throws IOException 
	 */
	public AudioBuffer(AudioInputStream ais) throws IOException{
		this.fmt=ais.getFormat();
		byte[] bs=new byte[ais.available()];//这种读法肯能有点不可取  全部依赖内存来装载数据 不过这样才方便做反向读取
		ais.read(bs);
		data=ByteBuffer.wrap(bs); 
	}
	/**
	 * 正序得到数据帧</br>
	 * 如果剩下的数据帧的字节数不足b.length则此次读取不会有任何数据被读到，</br>
	 * 所以b不宜太大
	 * 
	 * @return
	 */
	public int read(byte[] b){
		if(b.length%fmt.getFrameSize()!=0){
			throw new IllegalArgumentException("缓冲字节非整数帧:"+b.length+"%"+fmt.getFrameSize()+"!=0");
		}
		initRead();
		try {
			data.get(b);
		} catch (BufferUnderflowException e) {
			System.out.println(data);
			return -1;
		}
		return b.length;
	}
	/**
	 * 反序得到数据帧 </br>
	 * 如果剩下的帧字节数不足b.length 也会被读取到b中 这区别于read()方法
	 * @param b
	 * @return
	 */
	public int back(byte[] b){
		if(b.length%fmt.getFrameSize()!=0){
			throw new IllegalArgumentException("缓冲字节非整数帧:"+b.length+"%"+fmt.getFrameSize()+"!=0");
		}
		initBack();
		int num=b.length;
		for (int i = 0; i < b.length; ) {
			byte[] frameData=new byte[fmt.getFrameSize()];
			ByteBuffer bb=getFrameForBuffer();
			if(bb==null){
				return -1;//返回null时表示读取完成
			}
			bb.get(frameData);
			System.arraycopy( frameData, 0,b, i, frameData.length);//数组copy错了
			i+=frameData.length;
		}
		return num;
	} 
	//准备正向读取
	private void initRead() {
		if(data.limit()-data.position()==fmt.getFrameSize()){
			data.limit(data.capacity());
		}
	}
	//准备反向读取
	private void initBack() {
		if(data.limit()-data.position()!=fmt.getFrameSize()){
			data.limit(data.position()+fmt.getFrameSize());
		}
	}
	//用ByteBuffer去得数据
	private ByteBuffer getFrameForBuffer(){
		ByteBuffer bb=data.slice();//得到可见元素
		try{
		data.position(data.position()-fmt.getFrameSize());//向前移帧 以备下次取值
		data.limit(data.limit()-fmt.getFrameSize());
		}catch(IllegalArgumentException e){//元素取完时会抛出此异常
			System.out.println(data);
			return null;
		}
		
		return bb;
	}
	/**
	 * 定位播放
	 * @param idx 帧的索引（从0开始）
	 */
	public void pos(int idx){
		if(idx<0||idx>data.capacity()/fmt.getFrameSize()-1){
			throw new IndexOutOfBoundsException("帧索引超过范围："+idx+"---[0,"+data.capacity()/fmt.getFrameSize()+")");
		}
		data.position(idx*fmt.getFrameSize());
	} 
	/**
	 * 返回帧索引
	 * @return
	 */
	public int pos(){
		return data.position()/4;
	}
	/**
	 * 是否反向播放，即正在被调用的是否是back方法，是返回true否则返回false</br>
	 * 没有线程安全，若两个方法同时被调用则都不能正常读取数据
	 * @return
	 */
	public boolean isBack(){
		return data.limit()-data.position()==fmt.getFrameSize()?true:false;
	}
	/**
	 * 返回音频数据字节数
	 * @return
	 */
	public int length(){
		return data.capacity();
	}
}
