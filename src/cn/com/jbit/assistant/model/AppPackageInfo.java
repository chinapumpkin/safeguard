package cn.com.jbit.assistant.model;

import android.graphics.drawable.Drawable;

/**
 * ��˵����APK��Ϣ
 */
public class AppPackageInfo {
	
	public String packageName;     // ����
	public String appName;         // APK����
	public String appSize;         // ��С
	public String appVersion;      // �汾��
	public int appVersion_code;    // �汾��
	public Drawable appIcon=null;    // ͼ��
	public boolean isSysFlag=false; // ϵͳ��ʶ
	
	@Override
	public String toString() {
		return "AppPackageInfo [packageName=" + packageName + ", appName="
				+ appName + ", appSize=" + appSize + ", appVersion="
				+ appVersion + ", appVersion_code=" + appVersion_code
				+ ", appIcon=" + appIcon + ", isSysFlag=" + isSysFlag + "]";
	}
}
