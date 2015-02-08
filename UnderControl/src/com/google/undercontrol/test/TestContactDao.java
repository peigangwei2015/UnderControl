package com.google.undercontrol.test;

import com.google.undercontrol.dao.ContactDao;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestContactDao extends AndroidTestCase {
	private static final String TAG = "TestContactDao";

	/**
	 * 测试查找联系人姓名
	 */
	public void testFindName() {
		ContactDao dao = new ContactDao(getContext());
		String name = dao.findName("5556");
		Log.v(TAG, name);
	}
}
