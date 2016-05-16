package cn.com.jbit.assistant.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.interfaces.IAppInstalledInterface;
import cn.com.jbit.assistant.model.AppPackageInfo;

/**
 * 类说明：已安装应用数据适配器
 */
public class AppInstalledAdapter extends BaseAdapter{

	private LayoutInflater mInflater;	
	private AppPackageInfo appPackageInfo;
	private List<AppPackageInfo> appPackageInfos=null;
	IAppInstalledInterface appInstalledInterface;	
	private Context context;
	
	
	public AppInstalledAdapter(Context context){
		this.context=context;
		this.mInflater = LayoutInflater.from(context);	
	}
	//设置数据集
	public void setList(List<AppPackageInfo> appPackageInfos){
		this.appPackageInfos=appPackageInfos;
	}
	
	//设置接口
	public void setAppInstalledInterface(IAppInstalledInterface appInstalledInterface){
		this.appInstalledInterface=appInstalledInterface;
	}	
	@Override
	public int getCount() {
		if(appPackageInfos==null){
			return 0;
		}
		return appPackageInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		

		final int pos=position;
		ViewHolder holder = null;			
		if (convertView == null) {
			
			holder=new ViewHolder();  
			
			convertView = mInflater.inflate(R.layout.apps_item, null);
			holder.appinstalled_icon = (ImageView)convertView.findViewById(R.id.appinstalled_icon);
			holder.appinstalled_name = (TextView)convertView.findViewById(R.id.appinstalled_name);
			holder.appinstalled_size = (TextView)convertView.findViewById(R.id.appinstalled_size);
			holder.appinstalled_version = (TextView)convertView.findViewById(R.id.appinstalled_version);
			holder.appinstalled_open = (Button)convertView.findViewById(R.id.appinstalled_open);
			holder.appinstalled_uninstall = (Button)convertView.findViewById(R.id.appinstalled_uninstall);

			convertView.setTag(holder);			
		}else {			
			holder = (ViewHolder)convertView.getTag();
		}	
		
		appPackageInfo=appPackageInfos.get(position);			
		holder.appinstalled_icon.setBackgroundDrawable(appPackageInfo.appIcon);
		holder.appinstalled_name.setText(appPackageInfo.appName);		
		holder.appinstalled_size.setText(context.getString(R.string.app_size)+appPackageInfo.appSize);	
		holder.appinstalled_version.setText(context.getString(R.string.app_version)+appPackageInfo.appVersion);
		//是否是系统应用
		if(appPackageInfo.isSysFlag){
			holder.appinstalled_uninstall.setEnabled(false);			
		}else{
			holder.appinstalled_uninstall.setEnabled(true);
		}
		
		//打开应用监听事件
		holder.appinstalled_open.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				appInstalledInterface.setOpenOnClick(pos, v);
			}
		});	
		
		//卸载应用监听事件
		holder.appinstalled_uninstall.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				appInstalledInterface.setUninstallOnClick(pos, v);
			}
		});			
	
		return convertView;
	}	
	
	public class ViewHolder{
	ImageView appinstalled_icon;
	TextView appinstalled_name;
	TextView appinstalled_size;
	TextView appinstalled_version;
	Button appinstalled_open;
	Button appinstalled_uninstall;
	}
}

