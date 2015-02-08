package com.google.undercontrol.test;

import java.util.List;

import com.google.undercontrol.dao.FileDao;
import com.google.undercontrol.domain.FileInfo;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestFileDao extends AndroidTestCase {
	private static final String TAG = "TestFileDao";

	/**
	 * 测试获取手机内存文件列表
	 */
	public void testGetRomFileList() {
		FileDao dao = new FileDao();
		List<FileInfo> list = dao.getRomFileList();
		for (FileInfo fileInfo : list) {
			Log.v(TAG, fileInfo.toString());
		}
	}
}
