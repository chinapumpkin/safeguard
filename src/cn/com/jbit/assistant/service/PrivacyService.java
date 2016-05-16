package cn.com.jbit.assistant.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import cn.com.jbit.assistant.interfaces.IService;
import cn.com.jbit.assistant.module.privacymanager.PrivacyModel;
import cn.com.jbit.assistant.module.privacymanager.PrivacyPwdActivity;
import cn.com.jbit.assistant.utils.Constants;
/**
 * 类说明：隐私管理  程序锁保护服务
 */
public class PrivacyService extends Service{
	public static final String TAG = "PrivacyService";
	private ActivityManager am;
	private PrivacyModel dao;
	private Intent intent;
	private boolean runningFlag=false;//运行标识 
	private List<String> packageList;
	private MyBinder myBinder;
	private LockScreenReceiver receiver;
	private List<String> lockApps;
	private MyObserver observer;
	
	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}
	/**
	 * 类说明：自定义Binder 实现IService接口
	 */
	private class MyBinder extends Binder implements IService{

		@Override
		public void setTempUnlock(String package_name) {
			addTempUnlock(package_name);
		}		
	}
	/**
	 * 类说明：接收锁屏广播
	 */
	private class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {			
			packageList.clear();
		}		
	}

	/**
	 * 方法说明：临时停止保护
	 */
	public void addTempUnlock(String package_name) {
		packageList.add(package_name);
		Log.i(TAG, "tempUnlockApp packageList:"+packageList.toString());
	}

	@Override
	public void onCreate() {
		
		initReceiver();
		myBinder = new MyBinder();
		packageList = new ArrayList<String>();		
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		dao = new PrivacyModel(this);	
		
		lockApps = dao.findAll();//获取所有锁定的包名
		intent = new Intent(this, PrivacyPwdActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		runningFlag=true;
		super.onCreate();		
				
		//注册数据库内容变化的观察者
		observer = new MyObserver(new Handler());
		getContentResolver().registerContentObserver(Constants.PRIVACYURL, true, observer);
		startWatchDog();
	}
	/**
	 * 方法说明：注册广播
	 */
	private void initReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.setPriority(1000);		
		receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
	}
	
	/**
	 * 方法说明：监听进程 实现锁保护
	 */
	private void startWatchDog(){
		new Thread() {
			public void run() {
				while (runningFlag) {
					try {
						String currentApp = am.getRunningTasks(1).get(0).topActivity
								.getPackageName();
						//Log.i(TAG, "packageList:"+packageList.toString()+" lockApps:"+lockApps.toString());
						if (!packageList.contains(currentApp) && lockApps.contains(currentApp)) {
						    //弹出解锁界面
							intent.putExtra("package_name", currentApp);
							startActivity(intent);
						}
						Thread.sleep(300);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		runningFlag = false;
		super.onDestroy();
		try{
			unregisterReceiver(receiver);
			getContentResolver().unregisterContentObserver(observer);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * 方法说明：数据库的内容发生改变的时候,使用观察者实现触发器
	 */
	private class MyObserver  extends ContentObserver{

		public MyObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			lockApps = dao.findAll();
		}
		
	}
}
