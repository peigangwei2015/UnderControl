package com.google.undercontrol.test;

import java.util.List;

import com.google.undercontrol.dao.CallLogDao;
import com.google.undercontrol.domain.CallLogInfo;

import android.test.AndroidTestCase;

public class TestCallLogDao extends AndroidTestCase {
	public void testGetList(){
		CallLogDao dao=new CallLogDao(getContext());
		List<CallLogInfo> list = dao.getList(555);
		for (CallLogInfo callLogInfo : list) {
			System.out.println(callLogInfo);
		}
	}
}
