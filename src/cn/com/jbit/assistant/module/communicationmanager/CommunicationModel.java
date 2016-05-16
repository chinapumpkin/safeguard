package cn.com.jbit.assistant.module.communicationmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import cn.com.jbit.assistant.model.SmsInfo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;
/**
 * 类说明：短信备份、还原逻辑
 */
public class CommunicationModel {

	public static boolean flag = true;

	/**
	 * 把短信用xml文件的方式储存起来
	 * @param context上下文
	 * @param file 要生成的xml文件的文件对象
	 * @param pd 进度条
	 */
	public static void backupSms(Context context, File file, ProgressDialog pd)
			throws Exception {
		Uri uri = Uri.parse("content://sms/");		
		FileOutputStream fos = new FileOutputStream(file);
		
		XmlSerializer serializer = Xml.newSerializer();//XML构造器
		serializer.setOutput(fos, "UTF-8");
		ContentResolver resolver = context.getContentResolver();
		//备份短信
		Cursor cursor = resolver.query(uri, null, null, null, null);
				
		int max = cursor.getCount();
		pd.setMax(max);
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "sms_list");
		serializer.attribute(null, "total", max+""); //一共有多少条短信
		int total = 0;
		while (cursor.moveToNext()) {
			if (!flag) {
				throw new RuntimeException("用户停止");
			}
			Thread.sleep(80);
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			String address = cursor.getString(cursor.getColumnIndex("address"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String type = cursor.getString(cursor.getColumnIndex("type"));
			String body = cursor.getString(cursor.getColumnIndex("body"));			
			serializer.startTag(null, "sms");
			serializer.attribute(null, "id", id);
			serializer.startTag(null, "address");
			serializer.text(address);
			serializer.endTag(null, "address");

			serializer.startTag(null, "date");
			serializer.text(date);
			serializer.endTag(null, "date");

			serializer.startTag(null, "type");
			serializer.text(type);
			serializer.endTag(null, "type");

			serializer.startTag(null, "body");
			serializer.text(body);
			serializer.endTag(null, "body");
			serializer.endTag(null, "sms");
			total++;
			pd.setProgress(total);
		}
		serializer.endTag(null, "sms_list");
		serializer.endDocument();
		cursor.close();
		fos.flush();
		fos.close();
	}

	/**
	 * 短信的还原
	 * @param context上下文
	 * @param file 文件对象
	 * @param pd 进度条
	 * @throws Exception
	 */
	public static void recoverySms(Context context, File file, ProgressDialog pd)
			throws Exception {
		Uri uri = Uri.parse("content://sms/");
		FileInputStream fis = new FileInputStream(file);
		 //删除存在短信
		//context.getContentResolver().delete(uri, null, null);		
		XmlPullParser parser = Xml.newPullParser();//PULL解析
		parser.setInput(fis, "UTF-8");
		int type = parser.getEventType();
		SmsInfo info = null;
		int count=0;
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if("sms_list".equals(parser.getName())){
					int max  = Integer.parseInt(parser.getAttributeValue(0));
					pd.setMax(max);
				}
				if ("sms".equals(parser.getName())) {// 一条短信开始了
					info = new SmsInfo();
				} else if ("body".equals(parser.getName())) {
					info.setBody(parser.nextText());
				} else if ("date".equals(parser.getName())) {
					info.setDate(parser.nextText());
				} else if ("type".equals(parser.getName())) {
					info.setType(parser.nextText());
				} else if ("address".equals(parser.getName())) {
					info.setAddress(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if ("sms".equals(parser.getName())) {// 一条短信结束了
					ContentValues values  = new ContentValues();
					values.put("date", info.getDate());
					values.put("address", info.getAddress());
					values.put("type", info.getType());
					values.put("body", info.getBody());
					//还原短信
					context.getContentResolver().insert(uri, values);
					count++;
					pd.setProgress(count);
				}
				break;
			}
			type = parser.next();
		}
	}
}
