package com.ordercar.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * * 订单编码码生成器
 */
public class OrderUtils {

	/**
	 * 根据id进行加密+加随机数组成固定长度编码
	 */
	public static String toCode(Integer userId) {
		return getDateTime()+userId;
	}

	/**
	 * 生成时间戳
	 */
	private static String getDateTime() {
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}
}