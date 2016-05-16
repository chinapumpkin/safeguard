package cn.com.jbit.assistant.module.filesmanager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.utils.Utils;
import cn.com.jbit.assistant.view.FileDialog;

/**
 * ��˵������Դ�������
 */
public class SDCardActivity extends Activity {

	private ListView listView;
	private String currentPath;	

	public Handler handler=new Handler(){
		
		public  void handleMessage(Message msg) {
			switch (msg.what){
				//ˢ����ͼ			
				case 0:
					initData(currentPath);
				break;
				//�鿴Ŀ¼			
				case 1:
					initData(currentPath);
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		init();		
	}
	
	/**
	 * ����˵������ʼ��UI������
	 */
	private void init(){
		setContentView(R.layout.layout_sdcard);
		listView=(ListView)findViewById(R.id.listview_show);
		//�ж�SDCard״̬
		if(Utils.getSDCardStatus()){
			currentPath=Utils.getSDCardPath();
			initData(currentPath);
		}else{
			Toast.makeText(this, R.string.sdcard_no_sdcard, Toast.LENGTH_LONG).show();
			this.finish();			
		}		
		
		//�����¼�
		listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String[] fileArray=new String[]{getString(R.string.file_show),getString(R.string.file_delete)};
				HashMap<String,Object> itemMap = (HashMap<String,Object>) parent.getItemAtPosition(position);
				String path=(String)itemMap.get("path");
				if(path!=null && !"".equals(path)){
					File file=new File(path);
					if(file.isDirectory()){
						currentPath=path;
					}					
					FileDialog dialog=new FileDialog(SDCardActivity.this,file,fileArray,handler);
					dialog.show();
				}
				return false;
			}
			
		});
		//����¼�
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String,Object> itemMap = (HashMap<String,Object>) parent.getItemAtPosition(position);
				String path=(String)itemMap.get("path");
				if(path!=null && !"".equals(path)){
					File file=new File(path);
					if(file.isDirectory()){
						currentPath=path;
						initData(currentPath);					
					}else{
						Utils.openFile(SDCardActivity.this, file);
					}
				}
			}			
		});
	}

	/**
	 * ����˵������ʼ���ļ��б�
	 */
	private void initData(String path){
	
		List<Map<String,Object>> data = Utils.getListData(path);
		String[] from = {"img", "name"};
		int[] to = {R.id.img, R.id.name};
		SimpleAdapter spAdapter = new SimpleAdapter(this,data,R.layout.layout_sdcard_item,from,to);
		listView.setAdapter(spAdapter);
	}
}