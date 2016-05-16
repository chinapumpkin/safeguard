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
 * 类说明：欢迎界面  开门动画
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
		//加载开门动画
		Animation leftOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_left);
		Animation rightOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_right);
		//左布局向左移动
		leftLayout.setAnimation(leftOutAnimation);
		//右布局向右移动
		rightLayout.setAnimation(rightOutAnimation);		
		//设置动画监听器
		leftOutAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation){	
				
			}
			@Override
			public void onAnimationRepeat(Animation animation){
			}			
			@Override
			public void onAnimationEnd(Animation animation){
				//结束动画时，隐藏布局
				leftLayout.setVisibility(View.GONE);
				rightLayout.setVisibility(View.GONE);
				//切换到主界面
				Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
				LoadingActivity.this.startActivity(intent);
				overridePendingTransition(0, 0);
				LoadingActivity.this.finish();
			}
		});
	}
}