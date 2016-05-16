package cn.com.jbit.assistant.module.privacymanager;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.adapter.PrivacyAppsAdapter;
import cn.com.jbit.assistant.model.AppPackageInfo;
import cn.com.jbit.assistant.utils.Utils;

public class PrivacyActivity extends Activity {

	private PrivacyAppsAdapter adapter;
	private ListView listView;
	private LinearLayout layout_loading;
	public static PrivacyActivity AppAct=null;
	private List<AppPackageInfo> appPackageInfos;
	private PrivacyModel dao;
	private List<String> lockApps;

	public Handler handler=new Handler(){
		
		public  void handleMessage(Message msg) {
		switch (msg.what){
			//刷新
			case 0:
				if(appPackageInfos!=null && appPackageInfos.size()>0){
					adapter.setList(appPackageInfos,lockApps);
					adapter.notifyDataSetChanged();			
				}
				layout_loading.setVisibility(View.INVISIBLE);
			break;
		}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
	}	
	
	private void initUI(){
		SharedPreferences sp=this.getSharedPreferences("module_lib", MODE_PRIVATE);
		if(!sp.getBoolean("isRegister", false)){
			PrivacyModel.showPWDDialog(PrivacyActivity.this,sp);
		}
		setContentView(R.layout.layout_privacy);
		dao = new PrivacyModel(this);
		AppAct=this;			
		listView=(ListView)findViewById(R.id.listview_show);
		layout_loading=(LinearLayout)findViewById(R.id.layout_loading);
	
		adapter = new PrivacyAppsAdapter(this);			
		listView.setAdapter(adapter);
		initData();	
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String package_name = appPackageInfos.get(position).packageName;
				TranslateAnimation  translate=  new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
				translate.setDuration(500);
				view.startAnimation(translate);
				ImageView iv = (ImageView) view
						.findViewById(R.id.img_lock);
				if(lockApps.contains(package_name)){
					dao.delete(package_name);
					iv.setBackgroundResource(R.drawable.app_unlock);
					lockApps.remove(package_name);
				}else{
					dao.insert(package_name);
					iv.setBackgroundResource(R.drawable.app_lock);
					lockApps.add(package_name);
				}
			}
			
		});
	}

			
	private void initData(){	
		layout_loading.setVisibility(View.VISIBLE);
		new Thread(new Runnable(){
			@Override
			public void run() {
				appPackageInfos=Utils.getAppsList(getApplicationContext());
				//获取已锁应用列表
				lockApps = dao.findAll();
				handler.sendEmptyMessage(0);
			}
		}).start();
	}	
}
