package cn.com.jbit.assistant.module.trafficmanager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.db.TrafficDataSupport;
import cn.com.jbit.assistant.view.TrafficDatePickerDialog;

public class TrafficActivity extends Activity {
	private TextView mg_up_total, mg_down_total, mg_total, mUp_total,
			mDown_total, mliuliang_total, date_start_textview,
			date_over_textview, title;
	private Button date_start_btn, date_over_btn, search_btn, show_now_button;
	private int id_number_r = 0, id_number_t = 0;
	public static final String RXG = "rxg";
	public static final String TXG = "txg";
	public static final String RX = "rx";
	public static final String TX = "tx";
	public static final String SHUTDOWN = "d";
	public static final String NORMAL = "n";

	public static final String flag = "first";
	public static final String flagname = "nomber1";
	private TrafficDataSupport minsert = new TrafficDataSupport(this);
	private Calendar calendar = Calendar.getInstance();
	private Calendar mcalendar = Calendar.getInstance();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_traffic);
		init();
		showdata();
	}
	

	 
	 // 两个日期之间进行搜索
	private void init() {
		mg_down_total = (TextView) findViewById(R.id.g_down_edit);
		mg_up_total = (TextView) findViewById(R.id.g_up_edit);
		mg_total = (TextView) findViewById(R.id.g_total_edit);
		mUp_total = (TextView) findViewById(R.id.total_up_edit);
		mDown_total = (TextView) findViewById(R.id.total_down_edit);
		mliuliang_total = (TextView) findViewById(R.id.liuliang_total_edit);
		title = (TextView) findViewById(R.id.liuliang_biaoti);
		date_over_btn = (Button) findViewById(R.id.date_over_btn);
		date_start_btn = (Button) findViewById(R.id.date_start_btn);
		search_btn = (Button) findViewById(R.id.search_button);
		show_now_button=(Button)findViewById(R.id.shownow_button);
		show_now_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				title.setText(getResources().getString(R.string.now_liuliang_total));
			showdata();	
			}
		});
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		date_over_textview = (TextView) findViewById(R.id.date_over);
		date_over_textview.setText(sdf.format(new Date()));
		date_start_textview = (TextView) findViewById(R.id.date_start);
		date_start_textview.setText(sdf.format(new Date()));
		
		date_over_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectdate(date_over_textview);

			}
		});
		date_start_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectdate(date_start_textview);

			}
		});
		search_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search_result();

			}
		});
	}

	
	 // 两个日期之间进行搜索
	private void search_result() {
		long g3down = 0, g3up = 0, g3total = 0, rxdown = 0, txup = 0, alltotal = 0;
		String startdate = date_start_textview.getText().toString();
		String overdate = date_over_textview.getText().toString();
		Cursor r3gst = minsert.selectBettweenstart(startdate, overdate, RXG);
		Cursor r3gov = minsert.selectBettweenstop(startdate, overdate, RXG);
		Cursor t3gst = minsert.selectBettweenstart(startdate, overdate, TXG);
		Cursor t3gov = minsert.selectBettweenstop(startdate, overdate, TXG);

		title.setText(startdate + "至" + overdate + "的流量");
		if (r3gst.moveToNext()) {
			int number = r3gst.getColumnIndex("liuliang");
			g3down = r3gst.getLong(number);
			int number_id_r3s = r3gst.getColumnIndex("id");
			id_number_r = r3gst.getInt(number_id_r3s);
			if (r3gov.moveToNext()) {
				int number_id_r3o = r3gov.getColumnIndex("id");
				id_number_t = r3gov.getInt(number_id_r3o);
				if (id_number_r == id_number_t) {

				} else {
					int number1 = r3gov.getColumnIndex("liuliang");
					g3down = r3gov.getLong(number1) - g3down;
				}
			}

			g3down = g3down / 1024 / 1024;
			mg_down_total.setText(g3down + "MB");
		}
		if (t3gst.moveToNext()) {
			int number3 = t3gst.getColumnIndex("liuliang");
			g3up = t3gst.getLong(number3);
			int number_id_t3s = t3gst.getColumnIndex("id");
			id_number_r = t3gst.getInt(number_id_t3s);

			if (t3gov.moveToNext()) {
				int number_id_t3o = t3gov.getColumnIndex("id");
				id_number_t = t3gov.getInt(number_id_t3o);
				if (id_number_r == id_number_t) {
				} else {
					int number4 = t3gov.getColumnIndex("liuliang");
					g3up = t3gov.getLong(number4) - g3up;
				}
			}

			g3up = g3up / 1024 / 1024;
			mg_up_total.setText(g3up + "MB");
			g3total = g3down + g3up;
			mg_total.setText(g3total + "MB");

		}
		Cursor rst = minsert.selectBettweenstart(startdate, overdate, RX);
		Cursor rov = minsert.selectBettweenstop(startdate, overdate, RX);
		Cursor tst = minsert.selectBettweenstart(startdate, overdate, TX);
		Cursor tov = minsert.selectBettweenstop(startdate, overdate, TX);

		if (rst.moveToNext()) {
			int number6 = rst.getColumnIndex("liuliang");
			rxdown = rst.getLong(number6);
			int number_id_rs = rst.getColumnIndex("id");
			id_number_r = rst.getInt(number_id_rs);

			if (rov.moveToNext()) {
				int number_id_ro = rov.getColumnIndex("id");
				id_number_t = rst.getInt(number_id_ro);
				if (id_number_r == id_number_t) {
				} else {
					int number7 = rov.getColumnIndex("liuliang");
					rxdown = rov.getLong(number7) - rxdown;

				}
			}

			rxdown = rxdown / 1024 / 1024;
		}
		mDown_total.setText(rxdown + "MB");
		if (tst.moveToNext()) {
			int number9 = tst.getColumnIndex("liuliang");
			txup = tst.getLong(number9);
			int number_id_ts = tst.getColumnIndex("id");
			id_number_r = tst.getInt(number_id_ts);
			if (tov.moveToNext()) {
				int number_id_to = tst.getColumnIndex("id");
				id_number_t = tst.getInt(number_id_to);
				if (id_number_r == id_number_t) {
				} else {
					int number10 = tov.getColumnIndex("liuliang");
					txup = tov.getLong(number10) - txup;
				}
			}

			txup = txup / 1024 / 1024;
		}
		mUp_total.setText(txup + "MB");
		alltotal = rxdown + txup;
		mliuliang_total.setText(alltotal + "MB");
		r3gst.close();
		t3gst.close();
		t3gst.close();
		t3gov.close();
		rst.close();
		rov.close();
		tst.close();
		tov.close();
		TrafficDataSupport.dbStart.close();
		TrafficDataSupport.dbStop.close();
	}

	 
	 // 方法说明：显示时间
	private void selectdate(final TextView tex) {
		new TrafficDatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String monthstr, daystr;
						monthOfYear = monthOfYear + 1;
						if (monthOfYear < 10) {
							monthstr = "" + 0 + monthOfYear;
						} else {
							monthstr = "" + monthOfYear;
						}
						if (dayOfMonth < 10) {
							daystr = "" + 0 + dayOfMonth;
						} else {
							daystr = "" + dayOfMonth;
						}
						tex.setText(year + "-" + monthstr + "-" + daystr);

					}
				}, mcalendar.get(Calendar.YEAR), mcalendar.get(Calendar.MONTH),
				mcalendar.get(Calendar.DAY_OF_MONTH)).show();
	}

	// 显示当前流量
	private void showdata() {
		long grx = 0, gtx = 0, rx = 0, tx = 0;
		Cursor rcursor = minsert.selectNow(RXG, SHUTDOWN);
		Cursor tcursor = minsert.selectNow(TXG, SHUTDOWN);
		if (rcursor.moveToNext()) {
			int rnumbor = rcursor.getColumnIndex("liuliang");
			grx = rcursor.getLong(rnumbor);
			grx = (grx + TrafficStats.getMobileRxBytes()) / 1024 / 1024;
			mg_down_total.setText(grx + "MB");
		}
		if (tcursor.moveToNext()) {
			int tnumbor = tcursor.getColumnIndex("liuliang");
			gtx = tcursor.getLong(tnumbor);
			gtx = (gtx + TrafficStats.getMobileTxBytes()) / 1024 / 1024;
			mg_up_total.setText(gtx + "MB");
		}
		long g_total = grx + gtx;
		mg_total.setText(g_total + "MB");
		Cursor mrcursor = minsert.selectNow(RX, SHUTDOWN);
		Cursor mtcursor = minsert.selectNow(TX, SHUTDOWN);
		if (mrcursor.moveToNext()) {
			int numberRx = mrcursor.getColumnIndex("liuliang");
			rx = mrcursor.getLong(numberRx);
			rx = (rx + TrafficStats.getTotalRxBytes()) / 1024 / 1024;
			mDown_total.setText(rx + "MB");
		}
		if (mtcursor.moveToNext()) {
			int numberTx = mtcursor.getColumnIndex("liuliang");
			tx = mtcursor.getLong(numberTx);
			tx = (tx + TrafficStats.getTotalTxBytes()) / 1024 / 1024;
			mUp_total.setText(tx + "MB");
		}
		long z_total = rx + tx;
		mliuliang_total.setText(z_total + "MB");
		mrcursor.close();
		mtcursor.close();
		rcursor.close();
		tcursor.close();
		TrafficDataSupport.db.close();
	}

}