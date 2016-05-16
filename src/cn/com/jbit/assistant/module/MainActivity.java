package cn.com.jbit.assistant.module;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.adapter.CustomGridAdapter;
import cn.com.jbit.assistant.module.appsmanager.AppsManagerActivity;
import cn.com.jbit.assistant.module.communicationmanager.CommunicationActivity;
import cn.com.jbit.assistant.module.filesmanager.SDCardActivity;
import cn.com.jbit.assistant.module.powermanager.BatteryManagerActivity;
import cn.com.jbit.assistant.module.privacymanager.PrivacyActivity;
import cn.com.jbit.assistant.module.trafficmanager.TrafficActivity;
import cn.com.jbit.assistant.service.PrivacyService;
import cn.com.jbit.assistant.utils.Constants;
import cn.com.jbit.assistant.utils.Utils;
import cn.com.jbit.assistant.view.CustomGrid;
/**
 * ��˵����������
 */
public class MainActivity extends Activity {
    
	private String TAG = "MainActivity";
	private CustomGrid gridView;
	private ArrayList<Integer> data=null;
	private SharedPreferences sp=null;
	private static final int size=6;
	private Intent PrivacyServiceIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gridView = (CustomGrid)findViewById(R.id.gridview);
		sp=this.getSharedPreferences("grid_data", MODE_PRIVATE);
		
		
		//������������ʾģ���б�	
		data=Utils.getGridData(sp, size);
		if(data==null){
			data = new ArrayList<Integer>();
			for (int i = 0; i < size; i++) {
				data.add(i);
			}
		}
		
		CustomGridAdapter adapter = new CustomGridAdapter(this, data);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(listener);
		PrivacyServiceIntent = new Intent(this, PrivacyService.class);
		startService(PrivacyServiceIntent);

	}
	
	private OnItemClickListener listener =new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch(data.get(position)){
				//ͨѶ����
				case Constants.INDEX_COMMUNICATION_MANAGER:
					startAct(CommunicationActivity.class);
				break;
				//Ӧ�ù���
				case Constants.INDEX_APPS_MANAGER:
					startAct(AppsManagerActivity.class);					
				break;
				//��˽����
				case Constants.INDEX_PRIVACY_MANAGER:
					startAct(PrivacyActivity.class);
				break;
				//��Դ����
				case Constants.INDEX_FILES_MANAGER:
					startAct(SDCardActivity.class);
				break;
				//��Դ����
				case Constants.INDEX_POWER_MANAGER:
					startAct(BatteryManagerActivity.class);
				break;
				//��������
				case Constants.INDEX_TRAFFIC_MANAGER:
					startAct(TrafficActivity.class);
				break;
			}	
		}
	};
	
	/**
	 * ������Ӧ�Ĺ���ģ��
	 * @param cls
	 */
	private void startAct(Class<?> cls){
		Intent intent=new Intent(MainActivity.this,cls);
		MainActivity.this.startActivity(intent);
	}


	@Override
	protected void onPause() { 
		Utils.saveGridData(sp, data);
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}