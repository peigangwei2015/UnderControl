package com.google.undercontrol.service;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.undercontrol.domain.LocationInfo;
import com.google.undercontrol.domain.MsgType;
import com.google.undercontrol.utils.MsgUtils;
import com.google.undercontrol.utils.MyConstant;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 百度定位服务
 * @author Administrator
 *
 */
public class BDLocationService extends Service implements BDLocationListener {
	private LocationClient mLocationClient;
	private int id;
	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
	    mLocationClient.registerLocationListener( this );    //注册监听函数
	    initLocation();
	}
	/**
	 * 初始化
	 */
	private void initLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
		int span=5000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mLocationClient.start();
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public void onDestroy() {
		mLocationClient.stop();
		mLocationClient=null;
		super.onDestroy();
	}
	@Override
	public void onReceiveLocation(BDLocation location) {
		LocationInfo locationInfo=new LocationInfo();
		locationInfo.setLatitude(location.getLatitude());
		locationInfo.setLongitude(location.getLongitude());
		locationInfo.setNetworkLocationType(location.getNetworkLocationType());
		locationInfo.setRadius((int) location.getRadius());
		locationInfo.setSpeed((int) location.getSpeed());
		locationInfo.setAddress(location.getAddrStr());
		MsgUtils.send(getApplicationContext(), MyConstant.CURRENT_ID, MsgType.LOCAATION,locationInfo);
//		StringBuffer sb = new StringBuffer(256);
//		sb.append("time : ");
//		sb.append(location.getTime());
//		sb.append("\nerror code : ");
//		sb.append(location.getLocType());
//		sb.append("\nlatitude : ");
//		sb.append(location.getLatitude());
//		sb.append("\nlontitude : ");
//		sb.append(location.getLongitude());
//		sb.append("\nradius : ");
//		sb.append(location.getRadius());
//		if (location.getLocType() == BDLocation.TypeGpsLocation){
//			sb.append("\nspeed : ");
//			sb.append(location.getSpeed());
//			sb.append("\nsatellite : ");
//			sb.append(location.getSatelliteNumber());
//			sb.append("\ndirection : ");
//			sb.append("\naddr : ");
//			sb.append(location.getAddrStr());
//			sb.append(location.getDirection());
//		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//			sb.append("\naddr : ");
//			sb.append(location.getAddrStr());
//			//运营商信息
//			sb.append("\noperationers : ");
//			sb.append(location.getOperators());
//		}
//		Log.i("BDLocationService", sb.toString());
	}

}
