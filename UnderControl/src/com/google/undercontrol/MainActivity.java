package com.google.undercontrol;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.undercontrol.service.AnyChatService;
import com.google.undercontrol.utils.MyConstant;

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

		if (TextUtils.isEmpty(serverIP)) {
			intent = new Intent(this, ServerConfigActivity.class);
			startActivity(intent);
			finish();
			return;
		}

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
		
		
	}
}
