package com.google.undercontrol.test;

import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.google.undercontrol.dao.SmsDao;
import com.google.undercontrol.domain.ConversInfo;
import com.google.undercontrol.domain.SmsInfo;

public class TestSmsDao extends AndroidTestCase {
	private static final String TAG = "TestSmsDao";


	/**
	 * 测试获取短信会话列表
	 */
	public void testGetConvers() {
		SmsDao dao = new SmsDao(getContext());
		List<ConversInfo> list = dao.getConversList();
		for (ConversInfo conversInfo : list) {
			Log.v(TAG, conversInfo.toString());
		}
	}

	/**
	 * 测试获取短信列表
	 */
	public void testGetSmsList() {
		SmsDao dao = new SmsDao(getContext());
		List<SmsInfo> list = dao.getSmsList("3");
		for (SmsInfo smsInfo : list) {
			Log.v(TAG, smsInfo.toString());
		}
	}

	/**
	 * 测试删除短信
	 */
	public void testDelSms() {
		SmsDao dao = new SmsDao(getContext());
		boolean res = dao.delSms("2");
		assertTrue(res);
	}

	/**
	 * 测试删除会话
	 */
	public void testDelConver() {
		SmsDao dao = new SmsDao(getContext());
		boolean res = dao.delConver("4");
		assertTrue(res);
	}

	/**
	 * 测试插入短信
	 */
	public void testInsertSms() {
		SmsDao dao = new SmsDao(getContext());
		SmsInfo smsInfo = new SmsInfo();
		smsInfo.setAddress("95533");
		smsInfo.setBody("恭喜你中奖啦！");
		smsInfo.setDate(System.currentTimeMillis());
		smsInfo.setType(1);
		smsInfo.setRead(1);
		dao.insert(smsInfo);
	}
}
