package com.cd.uap.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectUtil {
	
	/**
	 * 执行单个参数且数据类型为基本数据类型的方法
	 * 
	 * @param fieldName		字段名称
	 * @param fieldValue	字段值
	 * @param object		执行方法的对象
	 * @param methodName	要执行方法的名称
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws Exception
	 */
	public static void invokeMethod(String fieldName, String fieldValue, Object object, String methodName)
			throws Exception {
		Field field = object.getClass().getDeclaredField(fieldName);
		Class<?> fieldType = field.getType();
		Method method = object.getClass().getDeclaredMethod(methodName, fieldType);
		ObjectUtil.convertType(fieldValue, object, fieldType, method);
	}
	
	
	/**
	 * 根据参数类型执行方法
	 * 
	 * @param fieldValue	字段的值
	 * @param object	执行方法的对象
	 * @param fieldType	字段类型的字节码对象
	 * @param method	方法对象
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	public static Object convertType(String fieldValue, Object object, Class<?> fieldType, Method method) throws Exception {
		Object result = null;
		if (fieldType == String.class) {
			result = method.invoke(object, fieldValue);
		} else if (fieldType == double.class || fieldType == Double.class) {
			double val = Double.parseDouble(fieldValue);
			result = method.invoke(object, val);
		} else if (fieldType == float.class || fieldType == Float.class) {
			float val = Float.parseFloat(fieldValue);
			result = method.invoke(object, val);
		} else if (fieldType == long.class || fieldType == Long.class) {
			long val = Long.parseLong(fieldValue);
			result = method.invoke(object, val);
		} else if (fieldType == int.class || fieldType == Integer.class) {
			int val = Integer.parseInt(fieldValue);
			result = method.invoke(object, val);
		} else if (fieldType == short.class || fieldType == Short.class) {
			short val = Short.parseShort(fieldValue);
			result = method.invoke(object, val);
		} else if (fieldType == byte.class || fieldType == Byte.class) {
			byte val = Byte.parseByte(fieldValue);
			result = method.invoke(object, val);
		} else if (fieldType == boolean.class || fieldType == Boolean.class) {
			boolean val = Boolean.parseBoolean(fieldValue);
			result = method.invoke(object, val);
		} else {
			throw new Exception("没有匹配的类型");
		}
		return result;
	}
}