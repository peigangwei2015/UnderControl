package com.google.undercontrol.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.undercontrol.domain.ContactInfo;
import com.google.undercontrol.utils.Utils;

public class ContactDao {
	private static final String TAG = "ContactDao";
	private Context context;
	private String baseUri = "content://com.android.contacts";
	private ContentResolver cr;

	public ContactDao(Context context) {
		this.context = context;
		cr = context.getContentResolver();
	}

	/**
	 * 获取所有的联系人
	 * 
	 * @return
	 */
	public List<ContactInfo> getList() {
		List<ContactInfo> list = new ArrayList<ContactInfo>();
		// 查询raw_contacts表 phone_lookup
		String[] raw_contacts_cloumns = { "_id", "display_name" };
		Cursor cursor = cr.query(Uri.parse(baseUri + "/raw_contacts"),
				raw_contacts_cloumns, null, null, null); // 获取手机联系人
		while (cursor.moveToNext()) {
			String _id = cursor.getString(0);
			String display_name = cursor.getString(1);
			// 查询Data表
			String[] data_cloums = { "mimetype", "data1", "_id" };
			Cursor cur2 = cr.query(Uri.parse(baseUri + "/data"), data_cloums,
					"raw_contact_id=?", new String[] { _id }, null);
			while (cur2.moveToNext()) {
				String mimetype = cur2.getString(0);
				// 判断是否是号码
				if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
					String number = cur2.getString(1);
					number = number.replaceAll("\\s|-", "");
					ContactInfo contactInfo = new ContactInfo();
					contactInfo.setName(display_name);
					contactInfo.setNumber(number);
					contactInfo.setId(Integer.parseInt(_id));
					list.add(contactInfo);
				}

			}

		}
		cursor.close();
		return list;
	}

	/**
	 * 根据电话号码查询到名字
	 * @param number
	 *            要查询到的电话号码
	 * @return 查询后的名字，如果无，则返回null
	 */
	public String findName(String number) {
		Uri uri = Uri.parse("content://com.android.contacts/phone_lookup/"+number);
		String name = null;
		number = number.startsWith("+86") ? number.substring(3) : number;
		Cursor cursor = cr.query(uri, new String[]{"display_name"}, null, null, null); // 获取手机联系人
		if (cursor != null && cursor.moveToNext()) {
			name=cursor.getString(0);
		}
		cursor.close();
		return name;
	}
}
