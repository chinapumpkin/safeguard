package cn.com.jbit.assistant.module.powermanager;

import cn.com.jbit.assistant.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 类说明：电源管理界面
 */
public class BatteryManagerActivity extends Activity {
	
	private static final String Tag = "BatteryManagerActivity";

	private static final int UPDATE_BATTERY_STATUS = 0;
	private static final int EVENT_TICK = 1;

	private static final float[] stateArray = { 0.01f, 0.5f, 1.0f};
	private int state = 0;
	
	//电池状态
	private int status;
	//电源类型
	private int plugType;
	//电池健康程度
	private int health;
	//电压数值
	private int voltage;
	//电池温度
	private int temperature;
	//电池技术细节
	private String technology;
	//电池剩余电量
	private int level;
	//电池总电量
	private int scale;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UPDATE_BATTERY_STATUS:
				//更新电池状态
				setBatteryStatus();
				//更新电池健康状态
				setBatteryHealth();
				//更新电池电压
				setBatteryVoltage();
				//更新电池温度
				setBatteryTemperature();
				//显示电池技术细节
				setBatteryTechnology();
				//显示电量信息
				setBattery();
				break;
			case EVENT_TICK:
				//更新电池启动时间
				updateBatteryStats();
				//1秒后更新启动时间
				sendEmptyMessageDelayed(EVENT_TICK, 1000);
				break;
			}
		}

	};

	/**
	 * 方法说明：显示电量信息
	 */
	private void setBattery() {
		TextView battery = (TextView) findViewById(R.id.battery);
		//获取当前剩余电量百分比
		int batteryRemain = level * 100 / scale;
		//显示电量百分比
		battery.setText(batteryRemain + "%");
		//如果当前手机正在充电中
		if(status == BatteryManager.BATTERY_STATUS_CHARGING){
			ImageView batteryImage = (ImageView) findViewById(R.id.battery_image);
			ImageView charging = (ImageView)findViewById(R.id.charging);
			//显示充电图标
			charging.setVisibility(View.VISIBLE);
			//显示充电动画
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.battery_full);
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();
			//使用Matrix控制显示充电动画
			Matrix matrix = new Matrix();
			float matrixScale = batteryRemain / 100.0f * stateArray[state];
			matrix.postScale(matrixScale, 1.0f);
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
			batteryImage.setImageBitmap(newBitmap);
			state = ++state % 3;
			handler.sendEmptyMessageDelayed(UPDATE_BATTERY_STATUS, 1000);	
		}
		//如果当前不在充电状态，并且电量高于30%
		else if (batteryRemain > 30) {
			ImageView batteryImage = (ImageView) findViewById(R.id.battery_image);
			//使用绿色电量图标
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.battery_full);
			ImageView charging = (ImageView)findViewById(R.id.charging);
			//隐藏充电图标
			charging.setVisibility(View.INVISIBLE);
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();
			//使用Matrix根据电量显示绿色电量图标长度
			Matrix matrix = new Matrix();
			float matrixScale = batteryRemain / 100.0f;
			matrix.postScale(matrixScale, 1.0f);
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
			batteryImage.setImageBitmap(newBitmap);
		}
		//如果当前电量低于30%
		else{
			ImageView batteryImage = (ImageView) findViewById(R.id.battery_image);
			//使用黄色电量图标
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.battery_full_yellow);
			ImageView charging = (ImageView)findViewById(R.id.charging);
			//隐藏充电图标
			charging.setVisibility(View.INVISIBLE);
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();
			//使用Matrix根据电量显示绿色电量图标长度
			Matrix matrix = new Matrix();
			float matrixScale = batteryRemain / 100.0f;
			matrix.postScale(matrixScale, 1.0f);
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
			batteryImage.setImageBitmap(newBitmap);
		}

	}

	private void setBatteryTechnology() {
		TextView batteryTechnology = (TextView) findViewById(R.id.battery_technology);
		batteryTechnology.setText(technology);
	}

	private void updateBatteryStats() {
		TextView bootTime = (TextView) findViewById(R.id.boot_time);
		bootTime.setText(DateUtils.formatElapsedTime(SystemClock.elapsedRealtime() / 1000));
	}

	private void setBatteryTemperature() {
		TextView batteryTemperature = (TextView) findViewById(R.id.battery_temperature);
		batteryTemperature.setText(tenthsToFixedString(temperature) + getString(R.string.battery_info_temperature_units));
	}

	private void setBatteryVoltage() {
		TextView batteryVoltage = (TextView) findViewById(R.id.battery_voltage);
		batteryVoltage.setText(voltage + getString(R.string.battery_info_voltage_units));
	}

	private void setBatteryStatus() {
		TextView batteryStatus = (TextView) findViewById(R.id.battery_status);
		batteryStatus.setText(getBatteryStatus(status, plugType));
	}

	private void setBatteryHealth() {
		TextView batteryHealth = (TextView) findViewById(R.id.battery_health);
		String healthString;
		//电池状态健康
		if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
			healthString = getString(R.string.battery_info_health_good);
		}
		//电池过热
		else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
			healthString = getString(R.string.battery_info_health_overheat);
		}
		//没电了
		else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
			healthString = getString(R.string.battery_info_health_dead);
		}
		//电压过高
		else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
			healthString = getString(R.string.battery_info_health_over_voltage);
		}
		//获取电池状态失败
		else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
			healthString = getString(R.string.battery_info_health_unspecified_failure);
		}
		//未知的电池健康状态
		else {
			healthString = getString(R.string.battery_info_health_unknown);
		}
		batteryHealth.setText(healthString);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_power);

		//注册监听电池状态变化广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		this.registerReceiver(receiver, filter);
		//更新已开机时间
		handler.sendEmptyMessage(EVENT_TICK);
	}
	
	

	@Override
	protected void onDestroy() {
		//反注册点知状态变化广播
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	/**
	 * 类说明：接收电量变化广播
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				//获取剩余电量
				level = intent.getIntExtra("level", 0);
				//获取满点量数值
				scale = intent.getIntExtra("scale", 0);
				//获取电池技术细节
				technology = intent.getStringExtra("technology");
				//获取电池状态
				status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
				//获取电源信息
				plugType = intent.getIntExtra("plugged", 0);
				//获取电池健康度
				health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
				//获取电压信息
				voltage = intent.getIntExtra("voltage", 0);
				//获取电池温度
				temperature = intent.getIntExtra("temperature", 0);
				//更新显示电池信息
				handler.sendEmptyMessage(UPDATE_BATTERY_STATUS);
			}
		}

	};

	/**
	 * 方法说明：
	 */
	public String getBatteryStatus(int status, int pulgType) {
		String statusString;
		if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
			statusString = getResources().getString(R.string.battery_info_status_charging);
			if (plugType > 0) {
				statusString = statusString
						+ " "
						+ getResources().getString(
								(plugType == BatteryManager.BATTERY_PLUGGED_AC) ? R.string.battery_info_status_charging_ac
										: R.string.battery_info_status_charging_usb);
			}
		} else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
			statusString = getResources().getString(R.string.battery_info_status_discharging);
		} else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
			statusString = getResources().getString(R.string.battery_info_status_not_charging);
		} else if (status == BatteryManager.BATTERY_STATUS_FULL) {
			statusString = getResources().getString(R.string.battery_info_status_full);
		} else {
			statusString = getResources().getString(R.string.battery_info_status_unknown);
		}
		return statusString;
	}

	/**
	 * 方法说明：数据格式转换
	 */
	private final String tenthsToFixedString(int x) {
		int tens = x / 10;
		return Integer.toString(tens) + "." + (x - 10 * tens);
	}
}