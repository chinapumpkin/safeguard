package cn.com.jbit.assistant.module.appsmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.adapter.AppInstalledAdapter;
import cn.com.jbit.assistant.interfaces.IAppInstalledInterface;
import cn.com.jbit.assistant.model.AppPackageInfo;
import cn.com.jbit.assistant.utils.Constants;
import cn.com.jbit.assistant.utils.Utils;
/**
 * 类说明：应用管理界面
 */
public class AppsManagerActivity extends Activity implements IAppInstalledInterface{

	private AppPackageInfo appPackageInfo;
	public List<AppPackageInfo> appPackageInfos=null;
	public List<AppPackageInfo> appThirdInfos=null;
	public List<AppPackageInfo> appSysInfos=null;
	private AppInstalledAdapter adapter;
	private ListView listView;
	private LinearLayout layout_loading;
	private Button show_all,show_third_apps,show_sys_apps;	
	private int flagNum=0;//当前显示项 
	private TextView tv_internal;
	private String size_internal;
	public static AppsManagerActivity AppAct=null;

	public Handler handler=new Handler(){
		
		public  void handleMessage(Message msg) {
		switch (msg.what){
			//刷新
			case 0:
				if(appPackageInfos!=null && appPackageInfos.size()>0){
					adapter.setList(appPackageInfos);
					adapter.notifyDataSetChanged();			
				}				
				tv_internal.setText(tv_internal.getText().toString()+size_internal+"");
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
		setContentView(R.layout.layout_apps);
		AppAct=this;			
		listView=(ListView)findViewById(R.id.listview_show);
		show_all=(Button)findViewById(R.id.show_all);
		show_third_apps=(Button)findViewById(R.id.show_third_apps);
		show_sys_apps=(Button)findViewById(R.id.show_sys_apps);
		layout_loading=(LinearLayout)findViewById(R.id.layout_loading);
		tv_internal=(TextView)findViewById(R.id.tv_internal);
		
		adapter = new AppInstalledAdapter(this);	
		adapter.setAppInstalledInterface(this);		
		listView.setAdapter(adapter);
		initData();
		
		show_all.setOnClickListener(listener);
		show_third_apps.setOnClickListener(listener);
		show_sys_apps.setOnClickListener(listener);				
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){			   
				case R.id.show_all:
					if(adapter!=null && flagNum!=Constants.APPS_ALL){
						adapter.setList(appPackageInfos);
						adapter.notifyDataSetChanged();
						flagNum=Constants.APPS_ALL;
					}
					break;
				case R.id.show_third_apps:
					if(adapter!=null && flagNum!=Constants.APPS_THIRD){
						adapter.setList(appThirdInfos);
						adapter.notifyDataSetChanged();
						flagNum=Constants.APPS_THIRD;
					}
					break;
				case R.id.show_sys_apps:
					if(adapter!=null && flagNum!=Constants.APPS_SYS){
						adapter.setList(appSysInfos);
						adapter.notifyDataSetChanged();
						flagNum=Constants.APPS_SYS;
					}
					break;
			}
		}
	};
	/**
	 * 方法说明：初始化数据
	 */	
	private void initData(){	
		layout_loading.setVisibility(View.VISIBLE);
		new Thread(new Runnable(){
			@Override
			public void run() {
				appPackageInfos=Utils.getAppsList(getApplicationContext());
				setCustomInfos(appPackageInfos);				
				size_internal=Utils.getInternalAvailSize(AppAct);
				handler.sendEmptyMessage(0);
			}
		}).start();
	}
	/**
	 * 方法说明：区分系统及非系统应用
	 */
	private void setCustomInfos(List<AppPackageInfo> list){
		appSysInfos=new ArrayList<AppPackageInfo>();
		appThirdInfos=new ArrayList<AppPackageInfo>();		
		for(AppPackageInfo info:list){
				if(info.isSysFlag){
					appSysInfos.add(info);
				}else{
					appThirdInfos.add(info);
				}
		}
	}
	/**
	 * 方法说明：打开应用
	 */
	@Override
	public void setOpenOnClick(int pos, View v) {
		appPackageInfo=appPackageInfos.get(pos);
		Utils.openPackage(AppAct, appPackageInfo.packageName);
	}
	/**
	 * 方法说明：卸载应用
	 */
	@Override
	public void setUninstallOnClick(final int pos, View v) {		
		appPackageInfo=appPackageInfos.get(pos);
		new AlertDialog.Builder(AppAct)
		.setTitle(R.string.dialog_title)
		.setMessage(R.string.dialog_apkUninstall)
		.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Utils.uninstallApk(AppAct,appPackageInfo.packageName,0);
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}	


	/**
	 * 方法说明：卸载结果返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==0){
			//卸载结束 无论是否成功都更新列表
			initData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}

