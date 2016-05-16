
package cn.com.jbit.assistant.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 类说明：流量记录数据库
 */
public class TrafficDataSupport extends SQLiteOpenHelper {

	public static SQLiteDatabase db;
	public static SQLiteDatabase dbStart;
	public static SQLiteDatabase dbStop;
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public TrafficDataSupport(Context context) {
		// 创建名为liuliangdata的数据库
		super(context, "liuliangdata", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建名为liuliangtable的数据表
		String sql = "create table liuliangtable (id integer primary key autoincrement,date datetime not null,liuliang integer,type text,history text)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exits liuliangtable ";
		db.execSQL(sql);
		onCreate(db);
	}

    /**
     * 
     * 方法说明：查询最新的数据
     *
     * @param type
     * @return
     */
	public Cursor selectNow(String type, String history) {
		db = getReadableDatabase();
		String sql = "select * from liuliangtable where id in (select max(id) from liuliangtable where type=? and history=?)";
		Cursor nowcursor = db.rawQuery(sql, new String[] { type, history });
		return nowcursor;
	}
	
	/**
	 * 
	 * 方法说明：查询两个时间段之间的数据
	 *
	 * @param datestart
	 * @param datestop
	 * @param type
	 * @return
	 */
public Cursor selectBettweenstart(String datestart,String datestop ,String type){
	
	dbStart=getReadableDatabase();
   String sql="select * from liuliangtable where id in (select min(id) from liuliangtable where type=? and date between datetime(?) and datetime(?))";
	Cursor cursor = dbStart.rawQuery(sql, new String[]{type,datestart,datestop});
   return cursor;
	
}
/**
 * 
 * 方法说明：两个时间段之间最大的数据
 *
 * @param datestart
 * @param datestop
 * @param type
 * @return
 */
public Cursor selectBettweenstop(String datestart,String datestop ,String type){
	
	dbStop=getReadableDatabase();
	String sql="select * from liuliangtable where id in (select max(id)from liuliangtable where type=? and date between datetime(?) and datetime(?))";
	Cursor cursor = dbStop.rawQuery(sql, new String[]{type, datestart, datestop});
	return cursor;
	
}
/**
 * 
 * 方法说明：插入数据
 *
 * @param liuliang
 * @param type
 * @param history
 */
	public void insertNow( long  liuliang, String type, String history) {
		SQLiteDatabase db = getWritableDatabase();
		String insertstr="insert into liuliangtable(date,liuliang,type,history) values(datetime('now'),?,?,?) ";
		db.execSQL(insertstr, new Object[]{liuliang, type, history});
		db.close();
	}
	
}
