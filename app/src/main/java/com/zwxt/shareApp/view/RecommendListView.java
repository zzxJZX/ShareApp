package com.zwxt.shareApp.view;

/**
 * Created by Administrator on 2018/8/21.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class RecommendListView extends ListView {
    public RecommendListView(Context context) {
        super(context);
    }

    public RecommendListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
