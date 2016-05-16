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
 * 类说明：资源管理界面
 */
public class SDCardActivity extends Activity {

	private ListView listView;
	private String currentPath;	

	public Handler handler=new Handler(){
		
		public  void handleMessage(Message msg) {
			switch (msg.what){
				//刷新视图			
				case 0:
					initData(currentPath);
				break;
				//查看目录			
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
	 * 方法说明：初始化UI及数据
	 */
	private void init(){
		setContentView(R.layout.layout_sdcard);
		listView=(ListView)findViewById(R.id.listview_show);
		//判断SDCard状态
		if(Utils.getSDCardStatus()){
			currentPath=Utils.getSDCardPath();
			initData(currentPath);
		}else{
			Toast.makeText(this, R.string.sdcard_no_sdcard, Toast.LENGTH_LONG).show();
			this.finish();			
		}		
		
		//长按事件
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
		//点击事件
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
	 * 方法说明：初始化文件列表
	 */
	private void initData(String path){
	
		List<Map<String,Object>> data = Utils.getListData(path);
		String[] from = {"img", "name"};
		int[] to = {R.id.img, R.id.name};
		SimpleAdapter spAdapter = new SimpleAdapter(this,data,R.layout.layout_sdcard_item,from,to);
		listView.setAdapter(spAdapter);
	}
}