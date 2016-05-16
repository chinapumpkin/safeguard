package cn.com.jbit.assistant.view;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.utils.Utils;

/**
 * ��˵������Դ���� �����¼���ʾ�Ի���
 */
public class FileDialog extends DefaultDialog {

	private Activity activity;
	private File file;
	private Handler handler;
	
	public FileDialog(Activity activity,  File file, String[] items,Handler handler) {
		super(activity, items,false);
		this.activity = activity;
		this.file = file;
		this.handler=handler;
	}
	protected void doItems(int which){			
		switch (which) {
		case 0:
			// �鿴
			if(file.isDirectory()){
				 handler.sendEmptyMessage(1);
			}else{
				Utils.openFile(activity, file);
			}			
			break;
		case 1:
			// ����ɾ����ʾ�Ի���
			new AlertDialog.Builder(activity)
				.setTitle(R.string.file_delete_tip)// ����
				.setPositiveButton(R.string.submit,new DialogInterface.OnClickListener() {// ȷ��
						@Override
						public void onClick(DialogInterface dialog, int which) {
							 try{
								 if(file.exists()&&file.delete()){
									 handler.sendEmptyMessage(0);
								 }								 
							 }catch(Exception e){
								 e.printStackTrace();
							 }
							}
				}).setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {// ȡ��
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// �ر���ʾ�Ի���
							dialog.dismiss();
						}
				}).show();
			break;		
		}
	}
	
	@Override
	protected void doPositive() {
	}
	
}
