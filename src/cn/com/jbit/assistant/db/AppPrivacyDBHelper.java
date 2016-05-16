package cn.com.jbit.assistant.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ��˵������������¼���ݿ�
 */
public class AppPrivacyDBHelper extends SQLiteOpenHelper {

	public AppPrivacyDBHelper(Context context) {
		super(context, "privacy.db", null, 1);
	}

	/**
	 * ���ݿ��һ�α�����
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table privacy (_id integer primary key autoincrement, package_name text)");
	}

	/**
	 * ���ݿ�İ汾���
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
