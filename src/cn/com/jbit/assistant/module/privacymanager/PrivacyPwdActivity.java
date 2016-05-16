package cn.com.jbit.assistant.module.privacymanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.interfaces.IService;
import cn.com.jbit.assistant.service.PrivacyService;

public class PrivacyPwdActivity extends Activity {
	private TextView tv_name;
	private ImageView img_logo;
	private EditText et_pwd;
	private Button btn_login,btn_pwd;
	private PackageManager pm;
	private Intent serviceIntent;
	private IService iService;
	private MyConn conn;
	private String package_name;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_privacy_pwd);
		sp=this.getSharedPreferences("module_lib", MODE_PRIVATE);

		tv_name = (TextView) findViewById(R.id.tv_name);
		img_logo = (ImageView)findViewById(R.id.img_logo);
		et_pwd = (EditText)findViewById(R.id.et_pwd);
		btn_login = (Button)findViewById(R.id.btn_login);
		btn_pwd = (Button)findViewById(R.id.btn_pwd);
				
		Intent intent = getIntent();
		package_name = intent.getStringExtra("package_name");
		pm = getPackageManager();
		serviceIntent = new Intent(this,PrivacyService.class);
		conn = new MyConn();
		bindService(serviceIntent, conn, BIND_AUTO_CREATE);		
		
		try {
			PackageInfo info = pm.getPackageInfo(package_name, 0);
			img_logo.setImageDrawable(info.applicationInfo.loadIcon(pm));
			tv_name.setText(info.applicationInfo.loadLabel(pm));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		btn_login.setOnClickListener(listener);
		btn_pwd.setOnClickListener(listener);
	}
	
	private View.OnClickListener listener=new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.btn_login:
					submitUnlock();
				break;
				case R.id.btn_pwd:
					PrivacyModel.showPWDDialog(PrivacyPwdActivity.this,sp);
				break;
			}
		}
	};

	/**
	 * 进程间调用Service方法
	 *
	 */
	private class MyConn implements ServiceConnection{

		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i("PrivacyPwdActivity", "onServiceConnected......");
			iService = (IService) service;
		}
		public void onServiceDisconnected(ComponentName name) {
			
		}		
	}
	
	/**
	 * 提交解锁
	 */
	public void submitUnlock() {
		String pwd = et_pwd.getText().toString().trim();
		if(TextUtils.isEmpty(pwd)){
			Toast.makeText(PrivacyPwdActivity.this, R.string.pwd_no_null, Toast.LENGTH_SHORT).show();
			return ;
		}else{
			if(pwd.equals(sp.getString("pwd", ""))){				
				//临时停止保护
				iService.setTempUnlock(package_name);
				finish();
			}else {
				Toast.makeText(PrivacyPwdActivity.this, R.string.pwd_err, 0).show();
				return ;
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			return true; //屏蔽后退键
		}		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{
			unbindService(conn);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
