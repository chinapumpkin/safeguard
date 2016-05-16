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
 * ��˵������Դ�������
 */
public class BatteryManagerActivity extends Activity {
	
	private static final String Tag = "BatteryManagerActivity";

	private static final int UPDATE_BATTERY_STATUS = 0;
	private static final int EVENT_TICK = 1;

	private static final float[] stateArray = { 0.01f, 0.5f, 1.0f};
	private int state = 0;
	
	//���״̬
	private int status;
	//��Դ����
	private int plugType;
	//��ؽ����̶�
	private int health;
	//��ѹ��ֵ
	private int voltage;
	//����¶�
	private int temperature;
	//��ؼ���ϸ��
	private String technology;
	//���ʣ�����
	private int level;
	//����ܵ���
	private int scale;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UPDATE_BATTERY_STATUS:
				//���µ��״̬
				setBatteryStatus();
				//���µ�ؽ���״̬
				setBatteryHealth();
				//���µ�ص�ѹ
				setBatteryVoltage();
				//���µ���¶�
				setBatteryTemperature();
				//��ʾ��ؼ���ϸ��
				setBatteryTechnology();
				//��ʾ������Ϣ
				setBattery();
				break;
			case EVENT_TICK:
				//���µ������ʱ��
				updateBatteryStats();
				//1����������ʱ��
				sendEmptyMessageDelayed(EVENT_TICK, 1000);
				break;
			}
		}

	};

	/**
	 * ����˵������ʾ������Ϣ
	 */
	private void setBattery() {
		TextView battery = (TextView) findViewById(R.id.battery);
		//��ȡ��ǰʣ������ٷֱ�
		int batteryRemain = level * 100 / scale;
		//��ʾ�����ٷֱ�
		battery.setText(batteryRemain + "%");
		//�����ǰ�ֻ����ڳ����
		if(status == BatteryManager.BATTERY_STATUS_CHARGING){
			ImageView batteryImage = (ImageView) findViewById(R.id.battery_image);
			ImageView charging = (ImageView)findViewById(R.id.charging);
			//��ʾ���ͼ��
			charging.setVisibility(View.VISIBLE);
			//��ʾ��綯��
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.battery_full);
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();
			//ʹ��Matrix������ʾ��綯��
			Matrix matrix = new Matrix();
			float matrixScale = batteryRemain / 100.0f * stateArray[state];
			matrix.postScale(matrixScale, 1.0f);
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
			batteryImage.setImageBitmap(newBitmap);
			state = ++state % 3;
			handler.sendEmptyMessageDelayed(UPDATE_BATTERY_STATUS, 1000);	
		}
		//�����ǰ���ڳ��״̬�����ҵ�������30%
		else if (batteryRemain > 30) {
			ImageView batteryImage = (ImageView) findViewById(R.id.battery_image);
			//ʹ����ɫ����ͼ��
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.battery_full);
			ImageView charging = (ImageView)findViewById(R.id.charging);
			//���س��ͼ��
			charging.setVisibility(View.INVISIBLE);
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();
			//ʹ��Matrix���ݵ�����ʾ��ɫ����ͼ�곤��
			Matrix matrix = new Matrix();
			float matrixScale = batteryRemain / 100.0f;
			matrix.postScale(matrixScale, 1.0f);
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
			batteryImage.setImageBitmap(newBitmap);
		}
		//�����ǰ��������30%
		else{
			ImageView batteryImage = (ImageView) findViewById(R.id.battery_image);
			//ʹ�û�ɫ����ͼ��
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.battery_full_yellow);
			ImageView charging = (ImageView)findViewById(R.id.charging);
			//���س��ͼ��
			charging.setVisibility(View.INVISIBLE);
			int height = bitmap.getHeight();
			int width = bitmap.getWidth();
			//ʹ��Matrix���ݵ�����ʾ��ɫ����ͼ�곤��
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
		//���״̬����
		if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
			healthString = getString(R.string.battery_info_health_good);
		}
		//��ع���
		else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
			healthString = getString(R.string.battery_info_health_overheat);
		}
		//û����
		else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
			healthString = getString(R.string.battery_info_health_dead);
		}
		//��ѹ����
		else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
			healthString = getString(R.string.battery_info_health_over_voltage);
		}
		//��ȡ���״̬ʧ��
		else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
			healthString = getString(R.string.battery_info_health_unspecified_failure);
		}
		//δ֪�ĵ�ؽ���״̬
		else {
			healthString = getString(R.string.battery_info_health_unknown);
		}
		batteryHealth.setText(healthString);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_power);

		//ע��������״̬�仯�㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_BATTERY_CHANGED);
		this.registerReceiver(receiver, filter);
		//�����ѿ���ʱ��
		handler.sendEmptyMessage(EVENT_TICK);
	}
	
	

	@Override
	protected void onDestroy() {
		//��ע���֪״̬�仯�㲥
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	/**
	 * ��˵�������յ����仯�㲥
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
				//��ȡʣ�����
				level = intent.getIntExtra("level", 0);
				//��ȡ��������ֵ
				scale = intent.getIntExtra("scale", 0);
				//��ȡ��ؼ���ϸ��
				technology = intent.getStringExtra("technology");
				//��ȡ���״̬
				status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
				//��ȡ��Դ��Ϣ
				plugType = intent.getIntExtra("plugged", 0);
				//��ȡ��ؽ�����
				health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);
				//��ȡ��ѹ��Ϣ
				voltage = intent.getIntExtra("voltage", 0);
				//��ȡ����¶�
				temperature = intent.getIntExtra("temperature", 0);
				//������ʾ�����Ϣ
				handler.sendEmptyMessage(UPDATE_BATTERY_STATUS);
			}
		}

	};

	/**
	 * ����˵����
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
	 * ����˵�������ݸ�ʽת��
	 */
	private final String tenthsToFixedString(int x) {
		int tens = x / 10;
		return Integer.toString(tens) + "." + (x - 10 * tens);
	}
}