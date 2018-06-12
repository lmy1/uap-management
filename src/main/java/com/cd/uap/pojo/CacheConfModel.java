package com.cd.uap.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CacheConfModel implements java.io.Serializable {

	/**
	 * 缓存时间类
	 */
	private static final long serialVersionUID = 1L;
	private long beginTime;// 缓存开始时间
	private boolean isForever = false;
	@Value("${jwt.access_token.expiration}")
	private int durableTime;// 持续时间

	
	public CacheConfModel() {
		super();
	}

	public CacheConfModel(long beginTime, boolean isForever) {
		super();
		this.beginTime = beginTime;
		this.isForever = isForever;
	}

	public boolean isForever() {
		return isForever;
	}

	public void setForever(boolean isForever) {
		this.isForever = isForever;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public int getDurableTime() {
		return durableTime;
	}

	public void setDurableTime(int durableTime) {
		this.durableTime = durableTime;
	}

	
}
