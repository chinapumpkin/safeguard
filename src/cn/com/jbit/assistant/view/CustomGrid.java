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
 * ��˵�����Զ���GridView
 */
public class CustomGrid extends GridView {
 
	private String TAG = "CustomGrid";
    
    private int dragPosition; //��ʼ��ק��λ�� 
    
    private int dropPosition; //������ק��λ��
    
    private int dragPointX; //GridView�����item���Ͻǵ�X�᷽���ƫ����
    
    private int dragPointY; //GridView�����item���Ͻǵ�Y�᷽���ƫ����
    
    private int dragOffsetX;//��Ļ��GridView��X�᷽���ƫ����
    
    private int dragOffsetY;//��Ļ��GridView��Y�᷽���ƫ����
    
    private ImageView dragImageView; //�϶�item��preview
    
    private View dragView;//��ʼ��ק��Item
    
    private WindowManager windowManager;//���ڹ�����
    
    private WindowManager.LayoutParams windowParams;//���ڲ���   

    
    public CustomGrid(Context context)
    {
        super(context);
    }
    
    public CustomGrid(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    /**
     * �ڴ���onTouchEvent()֮ǰ�������أ�ͳһ��ظ���Touch�¼�
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
				
				int x = (int)ev.getX();//�����������GridView��X������
	            int y = (int)ev.getY();
	            Log.d(TAG, "�����������GridView��X������ x=" + x + "�������������Ļ��X���� getRawx=" + ev.getRawX());
	            Log.d(TAG, "�����������GridView��Y������ y=" + y + "�������������Ļ��Y���� getRawY=" + ev.getRawY());
	            
	            dragPosition = dropPosition = arg2;
	            //��ȡ��ǰItem
	            View itemView = (View)getChildAt(dragPosition - getFirstVisiblePosition());
	            //�����������item���Ͻǵ�ƫ��
	            Log.d(TAG, "itemview.getLeft=" + itemView.getLeft() + ",itemview.getTop=" + itemView.getTop());
	            
	            dragPointX = x - itemView.getLeft();//Item���Ͻ������GridView��X������
	            dragPointY = y - itemView.getTop();//Item���Ͻ������GridView��Y������
	            
	            //item ���Ͻǵ�����.(x,y)
	            dragOffsetX = (int)(ev.getRawX() - x);//GridView����Ļ��X��ƫ����
	            dragOffsetY = (int)(ev.getRawY() - y);//GridView����Ļ��Y��ƫ����
	            
	            Log.d(TAG, "dragPointX=" + dragPointX + ",dragPointY=" + dragPointY + ",dragOffsetX=" + dragOffsetX
	                + ",dragOffsetY=" + dragOffsetY);

	            //����cache����������һ��bitmap
	            itemView.destroyDrawingCache();
	            itemView.setDrawingCacheEnabled(true);
	            itemView.setDrawingCacheBackgroundColor(0x424850);
	            Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());
	            
	            //����item��Ԥ��ͼ
	            startDrag(bm, x, y);
				//���ص�ǰѡ��item
				itemView.setVisibility(View.INVISIBLE);	
				dragView=itemView;
	            return false;

			};
		});
		return super.onInterceptTouchEvent(ev);
	}
    
    /**
     * ������ק
     */
    private void startDrag(Bitmap bm, int x, int y)
    {    	    	
        stopDrag();//����״̬
        windowParams = new WindowManager.LayoutParams();        
        windowParams.gravity = Gravity.TOP | Gravity.LEFT; //��ʼ��������Ϊ����
        //ItemԤ�����Ͻ��������Ļ������ 
        windowParams.x = x - dragPointX + dragOffsetX;//ʵ���ϣ�x-dragPointx = getLeft(),��Item���Ͻ���GridView��ƫ��+GridView����Ļ��X��ƫ����
        windowParams.y = y - dragPointY + dragOffsetY;
        //windowParams.alpha=0.6f;
        //���ÿ�͸� 
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(bm);        
        windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE); 
        //���������ItemԤ��
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
     * �ƶ��и��´�����ʾ
     */
    private void onDrag(int x, int y)
    {
        if (dragImageView != null)
        {
        	windowParams.alpha = 0.8f;    
        	//��קItem���Ͻ��������Ļ������ 
            windowParams.x = x - dragPointX + dragOffsetX;
            windowParams.y = y - dragPointY + dragOffsetY;
            //���´�����ʾ
            windowManager.updateViewLayout(dragImageView, windowParams);
        }
    }  

    
    /**
     * ֹͣ��ק������Item
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
     * ������ק���Ƴ�Ԥ��ͼ���ͷ���Դ
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