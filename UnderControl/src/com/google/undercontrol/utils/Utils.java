package com.google.undercontrol.utils;

import com.google.undercontrol.MyAdmin;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
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
	

	
	/**
	 * 锁屏
	 * @param pwd 锁屏密码
	 */
	public static void lockScreen(Context context,String pwd){
		ComponentName cn = new ComponentName(context, MyAdmin.class);
		 DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
		if(dpm.isAdminActive(cn)){
			//设备管理员的api
			dpm.resetPassword(pwd, 0);
			dpm.lockNow();
		}else{
			Toast.makeText(context, "请先激活管理员", 0).show();
		}
	}
}
