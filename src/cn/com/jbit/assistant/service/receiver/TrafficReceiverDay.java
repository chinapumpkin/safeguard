package cn.com.jbit.assistant.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.TrafficStats;
import cn.com.jbit.assistant.db.TrafficDataSupport;
import cn.com.jbit.assistant.module.trafficmanager.TrafficActivity;

/**
 * 方法说明：获取定时器广播保存流量
 */
public class TrafficReceiverDay extends BroadcastReceiver {
	private long grx = 0, gtx = 0, rx = 0, tx = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals("SAVE_LIULIANG_EVERYDAY")) {
			getFellow(context);
			TrafficDataSupport minsert = new TrafficDataSupport(context);
			minsert.insertNow(grx, TrafficActivity.RXG, TrafficActivity.NORMAL);
			minsert.insertNow(gtx, TrafficActivity.TXG, TrafficActivity.NORMAL);
			minsert.insertNow(rx, TrafficActivity.RX, TrafficActivity.NORMAL);
			minsert.insertNow(tx, TrafficActivity.TX, TrafficActivity.NORMAL);
		}
	}

	/**
	 * 方法说明：获取当前使用的手机流量
	 */
	private void getFellow(Context context) {
		TrafficDataSupport minsert = new TrafficDataSupport(context);
		Cursor rcursor = minsert.selectNow(TrafficActivity.RXG,
				TrafficActivity.SHUTDOWN);
		Cursor tcursor = minsert.selectNow(TrafficActivity.TXG,
				TrafficActivity.SHUTDOWN);
		if (rcursor.moveToNext()) {
			int rnumbor = rcursor.getColumnIndex("liuliang");
			grx = rcursor.getLong(rnumbor);
		}
		grx = grx + TrafficStats.getMobileRxBytes();
		if (tcursor.moveToNext()) {
			int tnumbor = tcursor.getColumnIndex("liuliang");
			gtx = tcursor.getLong(tnumbor);
		}
		gtx = gtx + TrafficStats.getMobileTxBytes();

		Cursor mrcursor = minsert.selectNow(TrafficActivity.RX,
				TrafficActivity.SHUTDOWN);
		Cursor mtcursor = minsert.selectNow(TrafficActivity.TX,
				TrafficActivity.SHUTDOWN);
		if (mrcursor.moveToNext()) {
			int numberRx = mrcursor.getColumnIndex("liuliang");
			rx = mrcursor.getLong(numberRx);
		}
		rx = rx + TrafficStats.getTotalRxBytes();
		if (mtcursor.moveToNext()) {
			int numberTx = mtcursor.getColumnIndex("liuliang");
			tx = mtcursor.getLong(numberTx);
		}
		tx = tx + TrafficStats.getTotalTxBytes();
		mrcursor.close();
		mtcursor.close();
		rcursor.close();
		tcursor.close();
		TrafficDataSupport.db.close();
	}

}
