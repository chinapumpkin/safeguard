package cn.com.jbit.assistant.utils;

import android.net.Uri;
/**
 * 类说明：常量定义
 */
public class Constants {

	public static int screenHeight=0;
	public static int screenWidth=0;
	public static float screenDensity=0;
	
	public static final int INDEX_COMMUNICATION_MANAGER=0;  //通讯管理
	public static final int INDEX_APPS_MANAGER=1;           //应用管理
	public static final int INDEX_PRIVACY_MANAGER=2;        //隐私管理
	public static final int INDEX_FILES_MANAGER=3;          //资源管理
	public static final int INDEX_POWER_MANAGER=4;          //电源管理	
	public static final int INDEX_TRAFFIC_MANAGER=5;        //流量管理
	

	public static final int APPS_ALL=0;//所有应用
	public static final int APPS_THIRD=1;//非系统应用
	public static final int APPS_SYS=2;//系统应用
	//隐私数据库URI
	public static final Uri PRIVACYURL = Uri.parse("content://cn.com.jbit.assistant/privacy");
}
