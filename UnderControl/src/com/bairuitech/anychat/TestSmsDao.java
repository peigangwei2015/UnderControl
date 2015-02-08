package com.bairuitech.anychat;

import java.util.List;

import com.google.undercontrol.dao.SmsDao;
import com.google.undercontrol.domain.ConversInfo;

import android.test.AndroidTestCase;

public class TestSmsDao extends AndroidTestCase {
	public void testFormartJson(){
		SmsDao dao=new SmsDao(getContext());
		List<ConversInfo> list = dao.getConversList();
	}

}
