package com.google.undercontrol.utils;

import android.content.Context;
import android.content.Intent;

/**
 * @author Administrator 信息帮助类
 */
public class MsgUtils {
	/**
	 * 发送信息
	 * 
	 * @param id
	 *            接受者ID
	 * @param msg
	 *            信息内容
	 */
	public static String send(Context context, int id, String type, Object data) {
		StringBuilder msg = new StringBuilder();
		msg.append("{ type:"+"\""+type+"\"");
		if (data != null) {
			msg.append(",data:");
			msg.append(JsonUtils.obj2json(data));
		}
		msg.append("}");
		Intent intent = new Intent();
		intent.setAction(MyConstant.ANYCHAT_SERVICE_RECEIVE_ACTION);
		intent.putExtra("command", MyConstant.SEND);
		intent.putExtra("id", id);
		intent.putExtra("type", type);
		intent.putExtra("msg", msg.toString());
		context.sendBroadcast(intent);
		return msg.toString();
	}
	/**
	 * 发送下载信息
	 * @param context 上下文
	 * @param id 接受者ID
	 * @param path 文件路径
	 */
	public static void download(Context context, int id, String path) {
		Intent intent = new Intent();
		intent.setAction(MyConstant.ANYCHAT_SERVICE_RECEIVE_ACTION);
		intent.putExtra("command", MyConstant.DOWNLOAD_FILE);
		intent.putExtra("id", id);
		intent.putExtra("path", path);
		context.sendBroadcast(intent);
	}
	
	/**
	 * 发送信息
	 * 
	 * @param id
	 *            接受者ID
	 * @param msg
	 *            信息内容
	 */
	public static String send(Context context, int id, String type) {
		return send(context, id, type, null);
	}
}
