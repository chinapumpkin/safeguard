package cn.com.jbit.assistant.utils;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.widget.Toast;
import cn.com.jbit.assistant.R;
import cn.com.jbit.assistant.model.AppPackageInfo;

/**
 * ��˵����������
 */
public class Utils {

	/**
	 * ��������������
	 * @param sp
	 * @param data
	 */
	public static void saveGridData(SharedPreferences sp,ArrayList<Integer> data){
		Editor editor=sp.edit();
		int len=data.size();
		for(int i=0;i<len;i++){
			editor.putInt(i+"", data.get(i));			
		}
		editor.commit();		
	}
	
	/**
	 * ��ȡ����������
	 * @param sp
	 * @param len
	 * @return
	 */
	public static ArrayList<Integer> getGridData(SharedPreferences sp,int len){

		if(sp.getInt("1", -1)==-1){
			return null;
		}	
		ArrayList<Integer> data =new ArrayList<Integer>();
		for(int i=0;i<len;i++){
			data.add(sp.getInt(i+"", -1));			
		}
		return data;		
	}
	
	/**
	 * ����Ƿ����SDCard
	 * @return
	 */
	public static boolean getSDCardStatus(){
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		return false;
	}
	
	/**
	 * ��ȡSDCard��·��
	 */
	public static String getSDCardPath(){
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		if(rootPath!= null){
			return rootPath;
		}
		return null;
	}
	

	
	/**
	 * ��ȡSDCard���б�����
	 * @param path
	 * @return
	 */
	public static List<Map<String,Object>> getListData(String path){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	 	//��ȡ�ļ���Ŀ¼����
	 	File pfile = new File(path);
	 	File[] files = Utils.getFilsOrder(pfile);
	 	if(pfile.getParent()!=null && !"/mnt".equals(pfile.getParent())){
			Map<String,Object> root=new HashMap<String,Object>();
    		root.put("img",R.drawable.lastdir);
    		root.put("name","�����ϼ�Ŀ¼");
    		root.put("path",pfile.getParent());
    		list.add(root);
		}
	    if(files!=null&&files.length>0){	
		    for(File file:files){
		    	Map<String,Object> folder=new HashMap<String,Object>();
		    	if(file.isDirectory()){
		    		//��ȡ�ļ���Ŀ¼�ṹ
		    		folder.put("img",R.drawable.folder);
		    		folder.put("name",file.getName());
		    		folder.put("path",file.getPath());
		    		list.add(folder);
		    	}else{
	    			folder.put("img",R.drawable.app_icon);
	    			folder.put("name",file.getName());
	    			folder.put("path",file.getPath());
	    			list.add(folder);
		    	}
		    }
	    }
	    files = null;
		return list;
	 }
	
	/**
	 * �ļ��������򣨰�ASC������)
	 * @param file
	 * @return
	 */
	public static File[] getFilsOrder(File file){
	   File[] files=null;
	   if(file.exists()){
	   files= file.listFiles();
		   File temp;
		   if(files!=null&&files.length>0){
		       for(int i = 0; i < files.length; i++){
		           for(int j = 0; j <files.length-i-1; j++){
			           if(files[j].getName().compareTo(files[j+1].getName()) > 0){
			              temp = files[j];
			              files[j] = files[j+1];
			              files[j+1] = temp;
			           }
		           }
		       }
		   }
	   }
       return files;
	}
	
	/**
	 * ��ȡ�Ѱ�װӦ����Ϣ�б�
	 * @param context
	 * @return
	 */
	public static List<AppPackageInfo> getAppsList(Context context) {
		
		List<AppPackageInfo> appPackageInfos = new ArrayList<AppPackageInfo>();
		try{			
			PackageManager pManager = context.getPackageManager();
			List<PackageInfo> appList = getAllApkList(context);
			AppPackageInfo appPackageInfo;
			String dir;

			for (int i = 0; i < appList.size(); i++) {
				PackageInfo packageInfo = appList.get(i);
				appPackageInfo=new AppPackageInfo();
				appPackageInfo.packageName=packageInfo.packageName;
				appPackageInfo.appVersion=packageInfo.versionName;
				appPackageInfo.appVersion_code=packageInfo.versionCode;
				appPackageInfo.isSysFlag=((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)<=0)?false:true;
				dir = packageInfo.applicationInfo.publicSourceDir;
				appPackageInfo.appSize=getSize(new File(dir).length());
				appPackageInfo.appName=(String) packageInfo.applicationInfo.loadLabel(pManager);
				appPackageInfo.appIcon=packageInfo.applicationInfo.loadIcon(pManager);
						
				appPackageInfos.add(appPackageInfo);
			}		
			
		} catch (Exception e) {
			e.printStackTrace();			
		}			
		return appPackageInfos;
	}
	
	/**
	 * ��ʽת��Ӧ�ô�С ��λ"M"
	 */
	public static String getSize(long size) {
		return new DecimalFormat("0.##").format(size * 1.0 / (1024 * 1024)) + "M";
	}
	
	/**
	 * ��ȡϵͳ���Ѱ�װӦ�õİ���Ϣ
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getAllApkList(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pm = context.getPackageManager();

		// ��ȡϵͳ���Ѱ�װӦ�õİ���Ϣ
		List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);
		for (int i = 0; i < packageInfoList.size(); i++) {
			PackageInfo packageInfo = (PackageInfo) packageInfoList.get(i);
			//����ӱ�Ӧ�ð���Ϣ
			if (!packageInfo.packageName.equals(context.getPackageName())) {
				//�Ƴ����ɴ򿪽����Ӧ��
				//Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
				//if(intent!=null){
					apps.add(packageInfo);
				//}				
			}
		}
		return apps;
	}	

	/**
	 * ��Ӧ��
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean openPackage(Context context, String packageName) {

		try {
			Intent intent = new Intent();
			intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
			if (intent != null) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ж�س��� 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static void uninstallApk(Activity context, String packageName,int requestCode) {
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivityForResult(uninstallIntent, requestCode);
	}

	/**
	 * ��ȡ�ֻ����ڲ��洢�Ŀ��ÿռ�
	 */
	public static String getInternalAvailSize(Context context) {
		StatFs statFs = new StatFs("/data");
		long blockSize = statFs.getBlockSize();
		long availableBlocks = statFs.getAvailableBlocks();
		return Formatter.formatFileSize(context, blockSize*availableBlocks);
	}
	
	/**
	 * ���ļ�	 
	 * @param context
	 * @param aFile
	 */
	public static void openFile(Context context, File aFile) {

		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		//ȡ���ļ���
		String fileName = aFile.getName();
		String end = getFileType(fileName).toLowerCase();
		if (aFile.exists()) {
			// ���ݲ�ͬ���ļ����������ļ�
			if (checkEndsInArray(end, context.getResources().getStringArray(R.array.image))) {
				intent.setDataAndType(Uri.fromFile(aFile), "image/*");
			} else if (checkEndsInArray(end, context.getResources().getStringArray(R.array.apk))) {
				// ʵ���ǰ�װ
				intent.setDataAndType(Uri.fromFile(aFile), "application/vnd.android.package-archive");
			} else if (checkEndsInArray(end, context.getResources().getStringArray(R.array.audio))) {
				intent.setDataAndType(Uri.fromFile(aFile), "audio/*");
			} else if (checkEndsInArray(end, context.getResources().getStringArray(R.array.endingVideo))) {
				intent.setDataAndType(Uri.fromFile(aFile), "video/*");
			} else if (checkEndsInArray(end, context.getResources().getStringArray(R.array.text))) {
				intent.setDataAndType(Uri.fromFile(aFile), "text/*");
			} else if (checkEndsInArray(end, context.getResources().getStringArray(R.array.word))) {
				intent.setDataAndType(Uri.fromFile(aFile), "application/msword");
			} else if (checkEndsInArray(end, context.getResources().getStringArray(R.array.excel))) {
				intent.setDataAndType(Uri.fromFile(aFile), "application/vnd.ms-excel");
			} else if (checkEndsInArray(end, context.getResources().getStringArray(R.array.point))) {
				intent.setDataAndType(Uri.fromFile(aFile), "application/vnd.ms-powerpoint");
			} else if (checkEndsInArray(end, context.getResources().getStringArray(R.array.chm))) {
				intent.setDataAndType(Uri.fromFile(aFile), "application/x-chm");
			} else {
				// ����pdf��
				intent.setDataAndType(Uri.fromFile(aFile), "application/" + end);
			}
			try {
				context.startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(context, R.string.no_find_app, Toast.LENGTH_SHORT).show();
			}

		}
	}
	
	/**
	 * ��ȡ�ļ����� 
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName) {
		if (fileName != null && fileName.contains(".") && fileName.lastIndexOf(".") != fileName.length() - 1) {
			return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		} else {
			return null;
		}
	}
	
	/**
	 * ���checkItsEnd �Ƿ���endings������ 
	 * @param end
	 * @param ends
	 * @return
	 */
	public static boolean checkEndsInArray(String end, String[] ends) {
		for (String aEnd : ends) {
			if (end.equals(aEnd)) {
				return true;
			}
		}
		return false;
	}
}
