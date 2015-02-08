package com.google.undercontrol.test;

import java.util.List;

import com.google.undercontrol.dao.FileDao;
import com.google.undercontrol.dao.SmsDao;
import com.google.undercontrol.domain.ConversInfo;
import com.google.undercontrol.domain.FileInfo;
import com.google.undercontrol.domain.MsgType;
import com.google.undercontrol.utils.JsonUtils;
import com.google.undercontrol.utils.MsgUtils;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestJson extends AndroidTestCase {
	private static final String TAG = "TestJson";

	/**
	 * 测试将对象转换为Json
	 */
	public void testObj2Json() {
		FileDao dao=new FileDao();
		List<FileInfo> list = dao.getRomFileList();
		String json =MsgUtils.send(getContext(), 0,MsgType.FILE_LIST, list);
		Log.v(TAG, json);
	}
}
