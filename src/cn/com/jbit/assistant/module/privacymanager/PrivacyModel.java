package cn.com.jbit.assistant.module.privacymanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.db.AppPrivacyDBHelper;
import cn.com.jbit.assistant.utils.Constants;

/**
 * 类名：隐私逻辑处理
 *
 */
public class PrivacyModel {
	
	private Context context;
	private AppPrivacyDBHelper helper;	

	public PrivacyModel(Context context) {
		this.context = context;
		helper = new AppPrivacyDBHelper(context);
	}
	
	public static void showPWDDialog(final Activity context,final SharedPreferences sp){
		LinearLayout view = (LinearLayout) context.getLayoutInflater().inflate(R.layout.privacy_modify_pwd, null);
		final EditText et_oldpwd = (EditText) view.findViewById(R.id.et_oldpwd);
		final EditText et_newpwd = (EditText) view.findViewById(R.id.et_newpwd);
		final EditText et_repeatpwd = (EditText) view.findViewById(R.id.et_repeatpwd);
		new AlertDialog.Builder(context).setTitle(R.string.modify_pwd)
		.setView(view).setCancelable(false).setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					String oldPWD=et_oldpwd.getText().toString().trim();
					String newPWD=et_newpwd.getText().toString().trim();
					String repeatPWD=et_repeatpwd.getText().toString().trim();
					if(TextUtils.isEmpty(oldPWD)){
						Toast.makeText(context, R.string.pwd_no_null, Toast.LENGTH_SHORT).show();
					}else if(TextUtils.isEmpty(newPWD)|| TextUtils.isEmpty(repeatPWD) || !newPWD.equals(repeatPWD)){
						Toast.makeText(context, R.string.pwd_check_err, Toast.LENGTH_SHORT).show();
					}else{
						if(oldPWD.equals(sp.getString("pwd", "")) || (!sp.getBoolean("isRegister", false) && oldPWD.equals("0"))){
							Editor editor=sp.edit();
							editor.putString("pwd", newPWD);
							editor.putBoolean("isRegister", true);
							editor.commit();
							dialog.dismiss();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}

	/**
	 * 根据包名查找程序是否锁保护
	 */
	public boolean find(String package_name) {
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select package_name from privacy where package_name=?",
					new String[] { package_name });
			if (cursor.moveToFirst()) {
				result = true;
			}
			cursor.close();
			db.close();
		}
		return result;
	}
	
	/**
	 * 添加锁保护程序记录
	 */
	public void insert(String package_name) {
		if (find(package_name)) {
			return;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("insert into privacy (package_name) values (?);",
					new Object[] { package_name});
			context.getContentResolver().notifyChange(Constants.PRIVACYURL, null); //通知更新，实现触发器原理
			db.close();
		}
	}


	/**
	 * 删除锁保护程序记录
	 */
	public void delete(String package_name){
		SQLiteDatabase db = helper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from privacy where package_name =?", new Object[]{package_name});
			context.getContentResolver().notifyChange(Constants.PRIVACYURL, null);
			db.close();
		}
	}
	
	/**
	 * 查找全部锁保护程序记录
	 */
	public List<String> findAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<String> packageList = new ArrayList<String>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select package_name from privacy", null);
			while (cursor.moveToNext()) {
				String package_name = cursor.getString(0);
				packageList.add(package_name);
				package_name = null;				
			}
			cursor.close();
			db.close();
		}
		return packageList;
	}
	
}
