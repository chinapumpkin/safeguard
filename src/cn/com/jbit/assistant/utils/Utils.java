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
 * 类说明：工具类
 */
public class Utils {

	/**
	 * 保存主界面排序
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
	 * 获取主界面排序
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
	 * 检查是否存在SDCard
	 * @return
	 */
	public static boolean getSDCardStatus(){
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取SDCard根路径
	 */
	public static String getSDCardPath(){
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		if(rootPath!= null){
			return rootPath;
		}
		return null;
	}
	

	
	/**
	 * 获取SDCard下列表数据
	 * @param path
	 * @return
	 */
	public static List<Map<String,Object>> getListData(String path){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	 	//获取文件夹目录数组
	 	File pfile = new File(path);
	 	File[] files = Utils.getFilsOrder(pfile);
	 	if(pfile.getParent()!=null && !"/mnt".equals(pfile.getParent())){
			Map<String,Object> root=new HashMap<String,Object>();
    		root.put("img",R.drawable.lastdir);
    		root.put("name","返回上级目录");
    		root.put("path",pfile.getParent());
    		list.add(root);
		}
	    if(files!=null&&files.length>0){	
		    for(File file:files){
		    	Map<String,Object> folder=new HashMap<String,Object>();
		    	if(file.isDirectory()){
		    		//获取文件夹目录结构
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
	 * 文件名字排序（按ASC码排序)
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
	 * 获取已安装应用信息列表
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
	 * 格式转换应用大小 单位"M"
	 */
	public static String getSize(long size) {
		return new DecimalFormat("0.##").format(size * 1.0 / (1024 * 1024)) + "M";
	}
	
	/**
	 * 获取系统中已安装应用的包信息
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getAllApkList(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pm = context.getPackageManager();

		// 获取系统中已安装应用的包信息
		List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);
		for (int i = 0; i < packageInfoList.size(); i++) {
			PackageInfo packageInfo = (PackageInfo) packageInfoList.get(i);
			//不添加本应用包信息
			if (!packageInfo.packageName.equals(context.getPackageName())) {
				//移除不可打开界面的应用
				//Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageInfo.packageName);
				//if(intent!=null){
					apps.add(packageInfo);
				//}				
			}
		}
		return apps;
	}	

	/**
	 * 打开应用
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
	 * 卸载程序 
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
	 * 获取手机的内部存储的可用空间
	 */
	public static String getInternalAvailSize(Context context) {
		StatFs statFs = new StatFs("/data");
		long blockSize = statFs.getBlockSize();
		long availableBlocks = statFs.getAvailableBlocks();
		return Formatter.formatFileSize(context, blockSize*availableBlocks);
	}
	
	/**
	 * 打开文件	 
	 * @param context
	 * @param aFile
	 */
	public static void openFile(Context context, File aFile) {

		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		//取得文件名
		String fileName = aFile.getName();
		String end = getFileType(fileName).toLowerCase();
		if (aFile.exists()) {
			// 根据不同的文件类型来打开文件
			if (checkEndsInArray(end, context.getResources().getStringArray(R.array.image))) {
				intent.setDataAndType(Uri.fromFile(aFile), "image/*");
			} else if (checkEndsInArray(end, context.getResources().getStringArray(R.array.apk))) {
				// 实际是安装
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
				// 打开如pdf等
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
	 * 获取文件类型 
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
	 * 检查checkItsEnd 是否在endings数组中 
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
