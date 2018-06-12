package com.cd.uap.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.cd.uap.utils.SMSUtils;

@Service
public class SmsService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	/**
	 * 发送验证码
	 * @param phoneNum
	 * @throws ClientException 
	 */
	public SendSmsResponse sendMessage(String phoneNum) throws ClientException {
		//1.生成随机六位数
	    Integer code = (int)((Math.random()*9+1)*100000);
		//2.发送验证码
		SendSmsResponse response = SMSUtils.sendSms(phoneNum, code.toString());
		BoundValueOperations<String, String> boundValueOps = redisTemplate.boundValueOps("regist" + phoneNum);
		if (null != response && "OK".equals(response.getCode())) {
			//3.将验证码信息存储到redis中  格式为regist + phoneNum
			boundValueOps.set(code.toString(), 5, TimeUnit.MINUTES);
		}
		
		System.out.println(boundValueOps.get());
		System.out.println("剩余有效时间： " + boundValueOps.getExpire());
		
		return response;
	}
	
	/**
	 * 校验短信验证码
	 * @param validateCode
	 * @param phoneNum
	 * @return
	 */
	public Boolean checkValidateCode(String validateCode, String phoneNum) {
		
		//从redis中取出短信验证码
		BoundValueOperations<String, String> boundValueOps = redisTemplate.boundValueOps("regist" + phoneNum);
		String currentCode = boundValueOps.get();
			
		if (null == validateCode) {
			return false;
		} else if (validateCode.equals(currentCode)) {
			return true;
		} else {
			return false;
		}
	}
	
}
















