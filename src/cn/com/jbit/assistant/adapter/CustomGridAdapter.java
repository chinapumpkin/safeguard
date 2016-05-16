package cn.com.jbit.assistant.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.jbit.assistant.R;

/**
 * ��˵�����Զ���GridView����������
 */
public class CustomGridAdapter extends BaseAdapter {

	private static final String TAG="Adapter";
	private Context context;
	private ArrayList<Integer> data;
	private TextView txtItem;
	private ImageView imgItem;
	private String[] array;
	private static final int[] imgList=new int[]{R.drawable.list_0,R.drawable.list_1,R.drawable.list_2,
		R.drawable.list_3,R.drawable.list_4,R.drawable.list_5};

	public CustomGridAdapter(Context mContext, ArrayList<Integer> data) {
		this.context = mContext;
		this.data = data;
		this.array=mContext.getResources().getStringArray(R.array.modules);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * λ���л�
	 * @param startPosition
	 * @param endPosition
	 */
	public void exchange(int startPosition, int endPosition) {
		Log.i(TAG, startPosition + "-->" + endPosition);
		Object startObject = getItem(startPosition);
		if(startPosition < endPosition){
			//��ǰ�����ƶ�������ƶ��λ��endPosition + 1
			data.add(endPosition + 1, (Integer)startObject);
			//�Ƴ�ԭ�ƶ�������������£��ƶ������λ��endPosition
			data.remove(startPosition);
		}else{
			//�ɺ���ǰ�ƶ� ����ƶ��λ��endPosition
			data.add(endPosition,(Integer)startObject);
			//�Ƴ�ԭ�ƶ���
			data.remove(startPosition + 1);
		}
		notifyDataSetChanged();
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
		int index=data.get(position);		
		imgItem = (ImageView) convertView.findViewById(R.id.img_item);		
		txtItem = (TextView) convertView.findViewById(R.id.txt_item);
		imgItem.setImageResource(imgList[index]);
		txtItem.setText(array[index]);
		return convertView;
	}
}
