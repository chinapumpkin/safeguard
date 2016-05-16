package cn.com.jbit.assistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 类说明：程序锁记录数据库
 */
public class AppPrivacyDBHelper extends SQLiteOpenHelper {

	public AppPrivacyDBHelper(Context context) {
		super(context, "privacy.db", null, 1);
	}

	/**
	 * 数据库第一次被创建
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table privacy (_id integer primary key autoincrement, package_name text)");
	}

	/**
	 * 数据库的版本变更
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
