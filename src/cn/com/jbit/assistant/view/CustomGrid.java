package cn.com.jbit.assistant.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import cn.com.jbit.assistant.adapter.CustomGridAdapter;
/**
 * 类说明：自定义GridView
 */
public class CustomGrid extends GridView {
 
	private String TAG = "CustomGrid";
    
    private int dragPosition; //开始拖拽的位置 
    
    private int dropPosition; //结束拖拽的位置
    
    private int dragPointX; //GridView相对于item左上角的X轴方向的偏移量
    
    private int dragPointY; //GridView相对于item左上角的Y轴方向的偏移量
    
    private int dragOffsetX;//屏幕与GridView的X轴方向的偏移量
    
    private int dragOffsetY;//屏幕与GridView的Y轴方向的偏移量
    
    private ImageView dragImageView; //拖动item的preview
    
    private View dragView;//开始拖拽的Item
    
    private WindowManager windowManager;//窗口管理器
    
    private WindowManager.LayoutParams windowParams;//窗口参数   

    
    public CustomGrid(Context context)
    {
        super(context);
    }
    
    public CustomGrid(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    /**
     * 在触发onTouchEvent()之前进行拦截，统一监控各种Touch事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        Log.d(TAG, "onInterceptTouchEvent" + ev.getAction());
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			return setOnItemLongClickListener(ev);
		}
		return super.onInterceptTouchEvent(ev);
    }
    
        
	private boolean setOnItemLongClickListener(final MotionEvent ev) {
		
		this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				int x = (int)ev.getX();//触摸点相对于GridView的X轴坐标
	            int y = (int)ev.getY();
	            Log.d(TAG, "触摸点相对于GridView的X轴坐标 x=" + x + "触摸点相对于屏幕的X坐标 getRawx=" + ev.getRawX());
	            Log.d(TAG, "触摸点相对于GridView的Y轴坐标 y=" + y + "触摸点相对于屏幕的Y坐标 getRawY=" + ev.getRawY());
	            
	            dragPosition = dropPosition = arg2;
	            //获取当前Item
	            View itemView = (View)getChildAt(dragPosition - getFirstVisiblePosition());
	            //触摸点相对于item左上角的偏移
	            Log.d(TAG, "itemview.getLeft=" + itemView.getLeft() + ",itemview.getTop=" + itemView.getTop());
	            
	            dragPointX = x - itemView.getLeft();//Item左上角相对于GridView的X轴坐标
	            dragPointY = y - itemView.getTop();//Item左上角相对于GridView的Y轴坐标
	            
	            //item 左上角的坐标.(x,y)
	            dragOffsetX = (int)(ev.getRawX() - x);//GridView与屏幕的X轴偏移量
	            dragOffsetY = (int)(ev.getRawY() - y);//GridView与屏幕的Y轴偏移量
	            
	            Log.d(TAG, "dragPointX=" + dragPointX + ",dragPointY=" + dragPointY + ",dragOffsetX=" + dragOffsetX
	                + ",dragOffsetY=" + dragOffsetY);

	            //重置cache，重新生成一个bitmap
	            itemView.destroyDrawingCache();
	            itemView.setDrawingCacheEnabled(true);
	            itemView.setDrawingCacheBackgroundColor(0x424850);
	            Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
	            
	            //建立item的预览图
	            startDrag(bm, x, y);
				//隐藏当前选中item
				itemView.setVisibility(View.INVISIBLE);	
				dragView=itemView;
	            return false;

			};
		});
		return super.onInterceptTouchEvent(ev);
	}
    
    /**
     * 启动拖拽
     */
    private void startDrag(Bitmap bm, int x, int y)
    {    	    	
        stopDrag();//重置状态
        windowParams = new WindowManager.LayoutParams();        
        windowParams.gravity = Gravity.TOP | Gravity.LEFT; //起始方向设置为左上
        //Item预览左上角相对于屏幕的坐标 
        windowParams.x = x - dragPointX + dragOffsetX;//实际上：x-dragPointx = getLeft(),即Item左上角与GridView的偏移+GridView与屏幕的X轴偏移量
        windowParams.y = y - dragPointY + dragOffsetY;
        //windowParams.alpha=0.6f;
        //设置宽和高 
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(bm);        
        windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE); 
        //窗口中添加Item预览
        windowManager.addView(iv, windowParams);
        dragImageView = iv;
        
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (dragImageView != null && dragPosition != AdapterView.INVALID_POSITION)
        {
            
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_UP:
                    int upX = (int)ev.getX();   
                    int upY = (int)ev.getY();  
                    stopDrag(); 
                    onDrop(upX,upY); 
                    break;
           
                case MotionEvent.ACTION_MOVE:
                    int moveX = (int)ev.getX();                 
                    int moveY = (int)ev.getY();                    
                    onDrag(moveX,moveY); 
                    break;           
            }
        }
        return super.onTouchEvent(ev);
    }
    
    /**
     * 移动中更新窗口显示
     */
    private void onDrag(int x, int y)
    {
        if (dragImageView != null)
        {
        	windowParams.alpha = 0.8f;    
        	//拖拽Item左上角相对于屏幕的坐标 
            windowParams.x = x - dragPointX + dragOffsetX;
            windowParams.y = y - dragPointY + dragOffsetY;
            //更新窗口显示
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
    }  

    
    /**
     * 停止拖拽，放下Item
     */
    private void onDrop(int x, int y)
    {
        int tempPosition = pointToPosition(x, y);
        if (tempPosition != AdapterView.INVALID_POSITION)
        {
            dropPosition = tempPosition;
        }
        if (dropPosition != dragPosition)
        {
            CustomGridAdapter adapter = (CustomGridAdapter)this.getAdapter();
            adapter.exchange(dragPosition, dropPosition);
        }else{
        	dragView.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * 结束拖拽，移除预览图，释放资源
     */
    private void stopDrag()
    {
        if (dragImageView != null)
        {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
        
    }
    
} 