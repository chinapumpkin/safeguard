package cn.com.jbit.assistant.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.interfaces.IAppInstalledInterface;
import cn.com.jbit.assistant.model.AppPackageInfo;
/**
 * 类说明：隐私管理 应用列表数据适配器
 */
public class PrivacyAppsAdapter extends BaseAdapter{

	private LayoutInflater mInflater;	
	private AppPackageInfo appPackageInfo;
	private List<AppPackageInfo> appPackageInfos=null;
	private List<String> lockApps;
	
	
	public PrivacyAppsAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);	
	}
	
	public void setList(List<AppPackageInfo> appPackageInfos,List<String> lockApps){
		this.appPackageInfos=appPackageInfos;
		this.lockApps=lockApps;
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

		ViewHolder holder = null;	
		if (convertView == null) {			
			holder=new ViewHolder(); 
			convertView = mInflater.inflate(R.layout.privacy_item, null);
			holder.img_logo = (ImageView)convertView.findViewById(R.id.img_logo);
			holder.tv_app_name = (TextView)convertView.findViewById(R.id.tv_app_name);
			holder.img_lock = (ImageView)convertView.findViewById(R.id.img_lock);
			convertView.setTag(holder);			
		}else {			
			holder = (ViewHolder)convertView.getTag();
		}		
		appPackageInfo=appPackageInfos.get(position);			
		holder.img_logo.setImageDrawable(appPackageInfo.appIcon);
		holder.tv_app_name.setText(appPackageInfo.appName);
		if(lockApps.contains(appPackageInfo.packageName)){
			holder.img_lock.setBackgroundResource(R.drawable.app_lock);
		}else{
			holder.img_lock.setBackgroundResource(R.drawable.app_unlock);
		}	
		return convertView;
	}	
	
	public class ViewHolder{
	ImageView img_logo;
	TextView tv_app_name;
	ImageView img_lock;
	}
	
	
}

