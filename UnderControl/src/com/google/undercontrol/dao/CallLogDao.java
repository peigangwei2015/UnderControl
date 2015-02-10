package com.google.undercontrol.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.undercontrol.domain.CallLogInfo;

/**
 *对通话记录的操作
 * @author Administrator
 */
public class CallLogDao {
	private Context context;
	private ContentResolver cr;
	private int num=20;

	public CallLogDao(Context context) {
		this.context = context;
		cr=context.getContentResolver();
	}
	/**
	 * 获取通话记录列表
	 * @return
	 */
	public List<CallLogInfo> getList(int offset){
		List<CallLogInfo> list=new ArrayList<CallLogInfo>();
		Uri uri=Uri.parse("content://call_log/calls");
		String[] columns={"_id","number","name","duration","type","date"};
		Cursor cur = cr.query(uri, columns, null, null, "date desc limit "+num+" offset "+offset);
		while (cur!=null && cur.moveToNext()) {
			CallLogInfo cInfo=new CallLogInfo();
			cInfo.setId(cur.getInt(0));
			cInfo.setNumber(cur.getString(1));
			cInfo.setName(cur.getString(2));
			cInfo.setDuration(cur.getInt(3));
			cInfo.setType(cur.getInt(4));
			cInfo.setDate(cur.getLong(5));
			if (cInfo.getNumber()!=null) {
				list.add(cInfo);
			}
		}
		cur.close();
//		Utils.printCursor(cur);
		return list;
	}
	/**
	 * 删除通话记录
	 * @param id
	 * @return
	 */
	public boolean delete(int id) {
		Uri uri=Uri.parse("content://call_log/calls");
		int res = cr.delete(uri, "_id=?", new String[]{String.valueOf(id)});
		return res>0?true:false;
	}
	
	
}
