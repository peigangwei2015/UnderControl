package com.google.undercontrol.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.bairuitech.anychat.AnyChatTextMsgEvent;
import com.google.undercontrol.utils.MyConstant;

/**
 * 用户传送信息的服务
 */
public class AnyChatService extends Service implements AnyChatBaseEvent, AnyChatTextMsgEvent {
	private static final String TAG = "AnyChatService";
	/**
	 * AnyChat通讯对象
	 */
	private AnyChatCoreSDK anyChatSDK;
	/**
	 * 本地视频自动旋转控制
	 */
	private final int LOCALVIDEOAUTOROTATION = 1;
	/**
	 * 服务器端端口
	 */
	private int mServerPort = 8906;
	/**
	 * 进入房间ID
	 */
	private int mSRoomID = 1;
	/**
	 * 服务器端IP
	 */
	private String mServerIP = "192.168.1.101";
	/**
	 * 用户名
	 */
	private String mName = "name";

	private SharedPreferences sp;
	private BroadcastReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 连接服务器
		String serverIP = sp.getString("serverIP", null);
		if (!TextUtils.isEmpty(serverIP))
			connServer();
		// 注册接受信息广播接受者
		regRec();
	}

	/**
	 * 连接服务器
	 */
	public void connServer() {
		// 初始化SDK
		initSDK();
		// 初始化数据
		initData();
		// 连接服务器
		anyChatSDK.Connect(mServerIP, mServerPort);
		// 登陆服务器
		anyChatSDK.Login(mName, mName);
		anyChatSDK.SetTextMessageEvent(this);
	}

	/**
	 * 注册接受信息广播接受者
	 */
	private void regRec() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MyConstant.ANYCHAT_SERVICE_RECEIVE_ACTION);
		receiver = new MyReceiver();
		registerReceiver(receiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mServerIP = sp.getString("serverIP", mServerIP);
		mName = sp.getString("name", mName);
		mServerPort = sp.getInt("serverPort", mServerPort);
	}

	/**
	 * 初始化SDK
	 */
	private void initSDK() {
		if (anyChatSDK == null) {
			anyChatSDK = AnyChatCoreSDK.getInstance(this);
			anyChatSDK.SetBaseEvent(this);
			anyChatSDK.InitSDK(android.os.Build.VERSION.SDK_INT, 0);
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION,
					LOCALVIDEOAUTOROTATION);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消接受信息的广播接受者
		unregisterReceiver(receiver);
		// 退出并销毁
		anyChatSDK.Logout();
		anyChatSDK.Release();

	}

	/**
	 * 连接服务器消息, bSuccess表示是否连接成功
	 */
	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		if (bSuccess) {
			Log.v(TAG, mName + "连接服务器成功");
		}

	}

	/**
	 * 用户登录消息，dwUserId表示自己的用户ID号，dwErrorCode表示登录结果：0 成功，否则为出错代码
	 */
	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		Log.v(TAG, dwUserId + "登陆成功");
		if (dwErrorCode == 0) {
			// 登陆成功，进入房间
			anyChatSDK.EnterRoom(mSRoomID, "");
		}
	}

	/**
	 * 用户进入房间消息，dwRoomId表示所进入房间的ID号，dwErrorCode表示是否进入房间：0成功进入，否则为出错代码
	 */
	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		if (dwErrorCode == 0) {
			Log.v(TAG, mName+"进入"+dwRoomId+"号房间成功");
		}
	}

	/**
	 * 网络断开消息，该消息只有在客户端连接服务器成功之后，网络异常中断之时触发，dwErrorCode表示连接断开的原因
	 */
	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {

	}

	/**
	 * 房间在线用户消息，进入房间后触发一次，dwUserNum表示在线用户数（包含自己），dwRoomId表示房间ID
	 */
	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {

	}

	/**
	 * 用户进入/退出房间消息，dwUserId表示用户ID号，bEnter表示该用户是进入（TRUE）或离开（FALSE）房间
	 */
	@Override
	public void OnAnyChatUserAtRoomMessage(int arg0, boolean arg1) {

	}

	/**
	 * 接受信息的广播接受者
	 */
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String command = intent.getStringExtra("command");
			// 连接服务器
			if ("connServer".equals(command)) {
				connServer();
			} else if (MyConstant.RECONN_SERVER.equals(command)) {
				// 重新连接服务器
				reConnServer();
			}else if (MyConstant.BREAK_SERVER.equals(command)) {
				// 断开连接
				anyChatSDK.Logout();
				anyChatSDK.Release();
				anyChatSDK = null;
			}
		}

	}

	/**
	 * 重新连接服务器
	 */
	public void reConnServer() {
		Log.v(TAG, "重新连接服务器");
		if (anyChatSDK != null) {
			anyChatSDK.Logout();
			anyChatSDK.Release();
			anyChatSDK = null;
		}
		connServer();
	}

	@Override
	public void OnAnyChatTextMessage(int dwFromUserid, int dwToUserid,
			boolean bSecret, String message) {
		Toast.makeText(getApplicationContext(), dwFromUserid+"对我说："+message, 1).show();
		
	}


}
