package cn.com.jbit.assistant.module.communicationmanager;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.utils.Utils;
/**
 * 类说明：通讯管理界面
 */
public class CommunicationActivity extends Activity {

	protected static final int BACKUP_SUCCESS = 0;//备份成功
	protected static final int BACKUP_ERROR = 1;//备份失败
	protected static final int RECOVERY_SUCCESS = 2;//还原成功
	protected static final int RECOVERY_ERROR =3;//还原失败
	
	private Button btn_backup_sms,btn_recovery_sms;
	ProgressDialog pd;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case BACKUP_SUCCESS:
				Toast.makeText(getApplicationContext(), R.string.backup_success, 1).show();
				break;
			case BACKUP_ERROR:
				Toast.makeText(getApplicationContext(), R.string.backup_error, 1).show();
				break;
			case RECOVERY_SUCCESS:
				Toast.makeText(getApplicationContext(), R.string.recovery_success, 1).show();
				break;
			case RECOVERY_ERROR:
				Toast.makeText(getApplicationContext(), R.string.recovery_error, 1).show();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		initUI();	
	}
	
	private void initUI(){
		setContentView(R.layout.layout_communication);
		btn_backup_sms=(Button)this.findViewById(R.id.btn_backup_sms);
		btn_recovery_sms=(Button)this.findViewById(R.id.btn_recovery_sms);
		
		pd=new ProgressDialog(this);
		btn_backup_sms.setOnClickListener(listener);
		btn_recovery_sms.setOnClickListener(listener);
	}
	
	private View.OnClickListener listener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.btn_backup_sms:
					showLoading(CommunicationActivity.this,getString(R.string.backup_loading));
					new Thread() {
						public void run() {
							File file = new File(Environment.getExternalStorageDirectory(),"sms_backup.xml");
							try {
								CommunicationModel.flag = true;
								CommunicationModel.backupSms(getApplicationContext(), file, pd);
								Message msg = Message.obtain();
								msg.what = BACKUP_SUCCESS;
								handler.sendMessage(msg);
							} catch (Exception e) {
								e.printStackTrace();
								Message msg = Message.obtain();
								msg.what = BACKUP_ERROR;
								handler.sendMessage(msg);
							} finally {
								pd.dismiss();
							}

						};
					}.start();
					break;
				case R.id.btn_recovery_sms:
					final File backfile = new File(Environment.getExternalStorageDirectory(), "sms_backup.xml");
					pd.setMessage(getString(R.string.recovery_loading));
					pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					pd.show();
					new Thread() {
						public void run() {
							try {
								CommunicationModel.recoverySms(getApplicationContext(),backfile, pd);
								Message msg = Message.obtain();
								msg.what = RECOVERY_SUCCESS;
								handler.sendMessage(msg);
							} catch (Exception e) {
								e.printStackTrace();
								Message msg = Message.obtain();
								msg.what = RECOVERY_ERROR;
								handler.sendMessage(msg);
							}finally{
								pd.dismiss();
							}
						};
					}.start();
					break;				
			}
		}
	};
	
	private void showLoading(Context context,String msg){
		if (!Utils.getSDCardStatus()) {
			Toast.makeText(context, R.string.sdcard_no_sdcard, 0).show();
			return;
		}		
		pd.setTitle(R.string.backup_tip);
		pd.setMessage(msg);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				//当用户关闭对话框的时候 调用的方法.
				CommunicationModel.flag = false;
			}
		});
		pd.show();
	}
}
