package UtilityClasses;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.saboorsalaam.veed10.R;

import DynamicGridView.DynamicGridView;
import ObservableScrollView.CacheFragmentStatePagerAdapter;

/**
 * Created by Saboor Salaam on 6/21/2015.
 */
public class SelectiveViewPager extends ViewPager {
    private boolean paging = true;
    private DynamicGridView dynamicGridView;

    public SelectiveViewPager(Context context) {
        super(context);
    }

    public SelectiveViewPager(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public void setGridViewListeners(DynamicGridView dynamicGridView)
    {
        /*
        this.dynamicGridView = dynamicGridView;
        dynamicGridView.setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        dynamicGridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                requestDisallowInterceptTouchEvent(false);
                paging = true;
            }
        });
        */
    }


    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        if(true)
        {
            return super.onInterceptTouchEvent(e);
        }
        return false;
    }
    public void setPaging(boolean p){ paging = p; }

}
