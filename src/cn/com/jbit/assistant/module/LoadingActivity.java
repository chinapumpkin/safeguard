package cn.com.jbit.assistant.module;

import cn.com.jbit.assistant.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * ��˵������ӭ����  ���Ŷ���
 */
public class LoadingActivity extends Activity{
	

	private LinearLayout leftLayout,rightLayout,animLayout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        init();
    }
    
	private void init(){
		animLayout = (LinearLayout) findViewById(R.id.animLayout);
		leftLayout  = (LinearLayout) findViewById(R.id.leftLayout);
		rightLayout  = (LinearLayout) findViewById(R.id.rightLayout);
		
		animLayout.setBackgroundResource(R.drawable.bg);
		//���ؿ��Ŷ���
		Animation leftOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left);
		Animation rightOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
		//�󲼾������ƶ�
		leftLayout.setAnimation(leftOutAnimation);
		//�Ҳ��������ƶ�
		rightLayout.setAnimation(rightOutAnimation);		
		//���ö���������
		leftOutAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation){	
				
			}
			@Override
			public void onAnimationRepeat(Animation animation){
			}			
			@Override
			public void onAnimationEnd(Animation animation){
				//��������ʱ�����ز���
				leftLayout.setVisibility(View.GONE);
				rightLayout.setVisibility(View.GONE);
				//�л���������
				Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
				LoadingActivity.this.startActivity(intent);
				overridePendingTransition(0, 0);
				LoadingActivity.this.finish();
			}
		});
	}
}