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
}
