package com.google.undercontrol.utils;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
public class Utils {
	private static final String TAG = "Utils";
	/**
	 * 判断是否是一个合法的IP地址
	 * 
	 * @param strIP
	 *            要检测的IP
	 * @return 如果是IP地址则返回true，否则返回false
	 */
	public static boolean isIPAddress(String strIP) {
		String regIP = "^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$";
		if (!TextUtils.isEmpty(strIP) && strIP.matches(regIP)) {
			return true;
		}
		return false;
	}
	/**
	 * 打印结果集
	 * @param cur
	 */
	public static void printCursor(Cursor cur) {
		if (cur != null && cur.getCount() > 0) {
			while (cur.moveToNext()) {
				for (int i = 0; i < cur.getColumnCount(); i++) {
					String name = cur.getColumnName(i);
					String value = cur.getString(i);
					Log.v(TAG, "第"+cur.getPosition()+"行                  "+name+"===="+value);
				}
			}
		}
	}
}
