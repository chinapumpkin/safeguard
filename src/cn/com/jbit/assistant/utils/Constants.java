package cn.com.jbit.assistant.utils;

import android.net.Uri;
/**
 * ��˵������������
 */
public class Constants {

	public static int screenHeight=0;
	public static int screenWidth=0;
	public static float screenDensity=0;
	
	public static final int INDEX_COMMUNICATION_MANAGER=0;  //ͨѶ����
	public static final int INDEX_APPS_MANAGER=1;           //Ӧ�ù���
	public static final int INDEX_PRIVACY_MANAGER=2;        //��˽����
	public static final int INDEX_FILES_MANAGER=3;          //��Դ����
	public static final int INDEX_POWER_MANAGER=4;          //��Դ����	
	public static final int INDEX_TRAFFIC_MANAGER=5;        //��������
	

	public static final int APPS_ALL=0;//����Ӧ��
	public static final int APPS_THIRD=1;//��ϵͳӦ��
	public static final int APPS_SYS=2;//ϵͳӦ��
	//��˽���ݿ�URI
	public static final Uri PRIVACYURL = Uri.parse("content://cn.com.jbit.assistant/privacy");
}
