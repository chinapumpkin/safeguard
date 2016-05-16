package cn.com.jbit.assistant.view;

import cn.com.jbit.assistant.R;
import android.app.DatePickerDialog;
import android.content.Context;
/**
 * ��˵����ʱ��ѡ����ʾ�Ի���
 */
public class TrafficDatePickerDialog extends DatePickerDialog {

        public TrafficDatePickerDialog(Context context, int theme,
                        OnDateSetListener callBack, int year, int monthOfYear,
                        int dayOfMonth) {
                super(context, theme, callBack, year, monthOfYear, dayOfMonth);
                
        }
         public TrafficDatePickerDialog(Context context,
                    OnDateSetListener callBack,
                    int year,
                    int monthOfYear, 
                    int dayOfMonth) {
                 super(context, callBack, year, monthOfYear, dayOfMonth);
                 setButton(context.getString(R.string.submit), this);
            }
}