package cn.com.jbit.assistant.interfaces;

import android.view.View;
/**
 * 接口说明：已安装应用点击事件接口
 */
public interface IAppInstalledInterface {
	void setOpenOnClick(int pos,View v);
	void setUninstallOnClick(int pos,View v);
}

