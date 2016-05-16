package cn.com.jbit.assistant.view;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.utils.Utils;

/**
 * 类说明：资源管理 长按事件提示对话框
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
			// 查看
			if(file.isDirectory()){
				 handler.sendEmptyMessage(1);
			}else{
				Utils.openFile(activity, file);
			}			
			break;
		case 1:
			// 创建删除提示对话框
			new AlertDialog.Builder(activity)
				.setTitle(R.string.file_delete_tip)// 标题
				.setPositiveButton(R.string.submit,new DialogInterface.OnClickListener() {// 确定
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
				}).setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {// 取消
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 关闭提示对话框
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
