package com.google.undercontrol.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.google.undercontrol.domain.ConversInfo;
import com.google.undercontrol.domain.SmsInfo;
import com.google.undercontrol.utils.Utils;

/**
 * 对短信的增删改查
 */
public class SmsDao {

	private static final String TAG = "SmsDao";
	private ContactDao contactDao;

	private Context context;

	private ContentResolver cr;
	/**
	 * 读取短信Uri
	 */
	private Uri uri = Uri.parse("content://sms");

	public SmsDao(Context context) {
		this.context = context;
		contactDao = new ContactDao(context);
		cr = context.getContentResolver();
	}




	/**
	 * 获取会话列表
	 * @return
	 */
	public List<ConversInfo> getConversList() {
		List<ConversInfo> list = new ArrayList<ConversInfo>();
		uri = Uri.parse("content://sms/conversations");
		Cursor cur = cr.query(uri, new String[] { "sms.thread_id AS thread_id",
				"sms.body AS body", "groups.msg_count AS count",
				"sms.address AS address", "sms.date AS date" }, null, null,
				"date desc");
		// printCursor(cur);
		while (cur.moveToNext()) {
			ConversInfo conversInfo = new ConversInfo();
			conversInfo.setId(cur.getInt(0));
			conversInfo.setBody(cur.getString(1));
			conversInfo.setCount(cur.getInt(2));
			conversInfo.setAddress(cur.getString(3));
			conversInfo.setDate(cur.getLong(4));
			conversInfo.setName(contactDao.findName(conversInfo.getAddress()));
			// 将Map添加到集合中
			list.add(conversInfo);
		}
		cur.close();
		return list;
	}

	/**
	 * 按照会话ID取短信列表
	 * 
	 * @param thread_id 会话ID
	 * @return 短信信息列表
	 */
	public List<SmsInfo> getSmsList(String thread_id) {
		if (TextUtils.isEmpty(thread_id))return null;
		List<SmsInfo> list = new ArrayList<SmsInfo>();
		String[] columns = { "_id", "address", "date", "read", "body", "type" };
		Cursor cur = cr.query(uri, columns, "thread_id=?",
				new String[] { thread_id }, "date asc");
		 Utils.printCursor(cur);
		while (cur.moveToNext()) {
//			封装对象
			SmsInfo sms = new SmsInfo();
			sms.setId(cur.getInt(0));
			sms.setAddress(cur.getString(1));
			sms.setDate(cur.getLong(2));
			sms.setRead(cur.getInt(3));
			sms.setBody(cur.getString(4));
			sms.setType(cur.getInt(5));
			list.add(sms);
		}
		cur.close();
		return list;
	}

	/**
	 * 插入短信
	 * @param smsInfo
	 * @return
	 */
	public boolean insert(SmsInfo smsInfo) {
		 ContentValues values=new ContentValues();
		 values.put("address", smsInfo.getAddress());
		 values.put("body", smsInfo.getBody());
		 values.put("date", smsInfo.getDate());
		 values.put("type", smsInfo.getType());
		 values.put("read", smsInfo.getRead());
		Uri res = cr.insert(uri, values);
		return res !=null ? true : false;
	}

	/**
	 * 删除短信
	 * @param id 短信ID
	 * @return
	 */
	public boolean delSms(String _id) {
			int res = cr.delete(uri, "_id=?", new String[]{_id});
			return res > -1 ? true : false;
	}
	/**
	 * 删除会话
	 * @param id 会话ID
	 * @return
	 */
	public boolean delConver(String thread_id) {
		int res = cr.delete(uri, "thread_id=?", new String[]{thread_id});
		return res > -1 ? true : false;
	}

}
