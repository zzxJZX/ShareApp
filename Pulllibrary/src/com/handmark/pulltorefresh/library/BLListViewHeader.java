package com.handmark.pulltorefresh.library;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * © 2012 amsoft.cn
 * 名称：AbListViewHeader.java 
 * 描述：下拉刷新的Header View类.
 *
 */
public class BLListViewHeader extends LinearLayout {
	
	/** 上下文. */
	private Context mContext;
	
	/** 主View. */
	private LinearLayout headerView;
	
	/** 箭头图标View. */
	private ImageView arrowImageView;
	
	/** 进度图标View. */
	private ProgressBar headerProgressBar;
	
	/** 箭头图标. */
	
	/** 文本提示的View. */
	private TextView tipsTextview;
	
	/** 时间的View. */
	private TextView headerTimeView;
	
	/** 当前状态. */
	private int mState = -1;

	/** 向上的动画. */
	private Animation mRotateUpAnim;
	
	/** 向下的动画. */
	private Animation mRotateDownAnim;
	
	/** 动画时间. */
	private final int ROTATE_ANIM_DURATION = 180;
	
	/** 显示 下拉刷新. */
	public final static int STATE_NORMAL = 0;
	
	/** 显示 松开刷新. */
	public final static int STATE_READY = 1;
	
	/** 显示 正在刷新.... */
	public final static int STATE_REFRESHING = 2;
	
	/** 保存上一次的刷新时间. */
	private String lastRefreshTime = null;
	
	/**  Header的高度. */
	private int headerHeight;

	/**
	 * 初始化Header.
	 *
	 * @param context the context
	 */
	public BLListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 初始化Header.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public BLListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 初始化View.
	 * 
	 * @param context the context
	 */
	private void initView(Context context) {
		
		mContext  = context;
		
		//顶部刷新栏整体内容
		headerView = new LinearLayout(context);
		headerView.setOrientation(LinearLayout.HORIZONTAL);
		headerView.setGravity(Gravity.CENTER); 
		
		setPadding(headerView, 0, 5, 0, 5);
		
		//显示箭头与进度
		FrameLayout headImage =  new FrameLayout(context);
		arrowImageView = new ImageView(context);
		//从包里获取的箭头图片
		arrowImageView.setImageResource(R.drawable.default_ptr_flip);
		
		//style="?android:attr/progressBarStyleSmall" 默认的样式
		headerProgressBar = new ProgressBar(context,null,android.R.attr.progressBarStyle);
		headerProgressBar.setVisibility(View.GONE);
		
		LinearLayout.LayoutParams layoutParamsWW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWW.gravity = Gravity.CENTER;
		layoutParamsWW.width = scaleValue(mContext, 30);
		layoutParamsWW.height = scaleValue(mContext, 30);
		headImage.addView(arrowImageView,layoutParamsWW);
		headImage.addView(headerProgressBar,layoutParamsWW);
		
		//顶部刷新栏文本内容
		LinearLayout headTextLayout  = new LinearLayout(context);
		tipsTextview = new TextView(context);
		headerTimeView = new TextView(context);
		headTextLayout.setOrientation(LinearLayout.VERTICAL);
		headTextLayout.setGravity(Gravity.CENTER_VERTICAL);
		setPadding(headTextLayout,0, 0, 0, 0);
		LinearLayout.LayoutParams layoutParamsWW2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		headTextLayout.addView(tipsTextview,layoutParamsWW2);
		headTextLayout.addView(headerTimeView,layoutParamsWW2);
		tipsTextview.setTextColor(Color.rgb(107, 107, 107));
		headerTimeView.setTextColor(Color.rgb(107, 107, 107));
		setTextSize(tipsTextview, 16);
		setTextSize(headerTimeView, 14);
		
		LinearLayout.LayoutParams layoutParamsWW3 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWW3.gravity = Gravity.CENTER;
		layoutParamsWW3.rightMargin = scaleValue(mContext, 5);
		
		LinearLayout headerLayout = new LinearLayout(context);
		headerLayout.setOrientation(LinearLayout.HORIZONTAL);
		headerLayout.setGravity(Gravity.CENTER); 
		
		headerLayout.addView(headImage,layoutParamsWW3);
		headerLayout.addView(headTextLayout,layoutParamsWW3);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.BOTTOM;
		//添加大布局
		headerView.addView(headerLayout,lp);
		
		this.addView(headerView,lp);
		//获取View的高度
		measureView(this);
		headerHeight = this.getMeasuredHeight();
		
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
		
		setState(STATE_NORMAL);
	}
	
	private void setTextSize(TextView textView, int textSize) {
	    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
	}
    
	
	private void setPadding(View view, int left, int top, int right, int bottom) {
	    view.setPadding(scaleValue(mContext, left), scaleValue(mContext, top), 
	            scaleValue(mContext, right), scaleValue(mContext, scaleValue(mContext, bottom)));
    }
	
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int scaleValue(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    /**
    * 测量这个view
    * 最后通过getMeasuredWidth()获取宽度和高度.
    * @param view 要测量的view
    * @return 测量过的view
    */
   private void measureView(View view) {
       ViewGroup.LayoutParams p = view.getLayoutParams();
       if (p == null) {
           p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                   ViewGroup.LayoutParams.WRAP_CONTENT);
       }

       int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
       int lpHeight = p.height;
       int childHeightSpec;
       if (lpHeight > 0) {
           childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                   MeasureSpec.EXACTLY);
       } else {
           childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                   MeasureSpec.UNSPECIFIED);
       }
       view.measure(childWidthSpec, childHeightSpec);
   }

	/**
	 * 设置状态.
	 *
	 * @param state the new state
	 */
	public void setState(int state) {
		if (state == mState) return ;
		
		if (state == STATE_REFRESHING) {	
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.INVISIBLE);
			headerProgressBar.setVisibility(View.VISIBLE);
		} else {	
			arrowImageView.setVisibility(View.VISIBLE);
			headerProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch(state){
			case STATE_NORMAL:
				if (mState == STATE_READY) {
					arrowImageView.startAnimation(mRotateDownAnim);
				}
				if (mState == STATE_REFRESHING) {
					arrowImageView.clearAnimation();
				}
				tipsTextview.setText(R.string.pull_to_refresh_pull_label);
				
				if(lastRefreshTime==null){
					lastRefreshTime = getCurrentDate();
					headerTimeView.setText(mContext.getString(R.string.pull_to_refresh_time) + lastRefreshTime);
				}else{
					headerTimeView.setText(mContext.getString(R.string.pull_to_last_refresh_time) + lastRefreshTime);
				}
				
				break;
			case STATE_READY:
				if (mState != STATE_READY) {
					arrowImageView.clearAnimation();
					arrowImageView.startAnimation(mRotateUpAnim);
					tipsTextview.setText(R.string.pull_to_refresh_release_label);
					headerTimeView.setText(mContext.getString(R.string.pull_to_last_refresh_time) + lastRefreshTime);
					lastRefreshTime = getCurrentDate();
					
				}
				break;
			case STATE_REFRESHING:
				tipsTextview.setText(R.string.pull_to_refresh_refreshing_label);
				headerTimeView.setText(mContext.getString(R.string.pull_to_current_refresh_time) + lastRefreshTime);
				break;
				default:
			}
		
		mState = state;
	}
	
	/**
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    public static String getCurrentDate() {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }
    
	/**
	 * 设置header可见的高度.
	 *
	 * @param height the new visiable height
	 */
	public void setVisiableHeight(int height) {
		if (height < 0) height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) headerView.getLayoutParams();
		lp.height = height;
		headerView.setLayoutParams(lp);
	}

	/**
	 * 获取header可见的高度.
	 *
	 * @return the visiable height
	 */
	public int getVisiableHeight() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)headerView.getLayoutParams();
		return lp.height;
	}

	/**
	 * 描述：获取HeaderView.
	 *
	 * @return the header view
	 */
	public LinearLayout getHeaderView() {
		return headerView;
	}
	
	/**
	 * 设置上一次刷新时间.
	 *
	 * @param time 时间字符串
	 */
	public void setRefreshTime(String time) {
		headerTimeView.setText(time);
	}

	/**
	 * 获取header的高度.
	 *
	 * @return 高度
	 */
	public int getHeaderHeight() {
		return headerHeight;
	}
	
	/**
	 * 描述：设置字体颜色.
	 *
	 * @param color the new text color
	 */
	public void setTextColor(int color){
		tipsTextview.setTextColor(color);
		headerTimeView.setTextColor(color);
	}
	
	/**
	 * 描述：设置背景颜色.
	 *
	 * @param color the new background color
	 */
	public void setBackgroundColor(int color){
		headerView.setBackgroundColor(color);
	}

	/**
	 * 描述：获取Header ProgressBar，用于设置自定义样式.
	 *
	 * @return the header progress bar
	 */
	public ProgressBar getHeaderProgressBar() {
		return headerProgressBar;
	}

	/**
	 * 描述：设置Header ProgressBar样式.
	 *
	 * @param indeterminateDrawable the new header progress bar drawable
	 */
	public void setHeaderProgressBarDrawable(Drawable indeterminateDrawable) {
		headerProgressBar.setIndeterminateDrawable(indeterminateDrawable);
	}

	/**
	 * 描述：得到当前状态.
	 *
	 * @return the state
	 */
    public int getState(){
        return mState;
    }

	/**
	 * 设置提示状态文字的大小.
	 *
	 * @param size the new state text size
	 */
	public void setStateTextSize(int size) {
		tipsTextview.setTextSize(size);
	}

	/**
	 * 设置提示时间文字的大小.
	 *
	 * @param size the new time text size
	 */
	public void setTimeTextSize(int size) {
		headerTimeView.setTextSize(size);
	}

	/**
	 * Gets the arrow image view.
	 *
	 * @return the arrow image view
	 */
	public ImageView getArrowImageView() {
		return arrowImageView;
	}

	/**
	 * 描述：设置顶部刷新图标.
	 *
	 * @param resId the new arrow image
	 */
	public void setArrowImage(int resId) {
		this.arrowImageView.setImageResource(resId);
	}
	
    

}
