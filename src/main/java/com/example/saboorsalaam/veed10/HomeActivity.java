package com.example.saboorsalaam.veed10;



import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;


import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import DynamicGridView.DynamicGridView;
import FloatingActionButton.FloatingActionButton;
import Fragments.AccountFragment;
import Fragments.HomeFragment;
import Fragments.MyChannelsFragment;
import MyUltimateRecyclerView.MyUltimateRecyclerView;
import NiceTabLayout.NiceTabLayout;
import ObservableScrollView.BaseActivity;
import ObservableScrollView.CacheFragmentStatePagerAdapter;
import ObservableScrollView.ObservableScrollViewCallbacks;
import ObservableScrollView.ScrollState;
import ObservableScrollView.ScrollUtils;
import ObservableScrollView.Scrollable;
import ObservableScrollView.ViewPagerTabRecyclerViewFragment;
import UtilityClasses.SelectiveViewPager;


public class HomeActivity extends BaseActivity implements ObservableScrollViewCallbacks {

    private final String[] TITLES = new String[]{"Home", "Channels", "Settings"};
    private final int[] ICONS = new int[]{R.drawable.abc_ic_search_api_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_voice_search_api_mtrl_alpha};

    private View mHeaderView;
    private Toolbar mToolbarView;
    private int mBaseTranslationY;
    private SelectiveViewPager mPager;
    private NavigationAdapter mPagerAdapter;
    private static final String[] tab_colors = {"#009688", "#F44336", "#8BC34A"};
    private int current_tab =0;
    private FloatingActionButton floatingActionButton;
    private boolean isLOLLIPOP = false;
    Animation fadeIn, fadeOut;


    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }

    ValueAnimator mColorAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(200);
        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(200);

        setContentView(R.layout.activity_home);



        final Window window = this.getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        final int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP){
            isLOLLIPOP = true;
            //window.setStatusBarColor(Color.parseColor("#F44336"));
        }

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        //mToolbarView.setNavigationIcon(R.drawable.galaxy_pattern);
        setSupportActionBar(mToolbarView);



        mHeaderView = findViewById(R.id.header);
        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));

        //mHeaderView.setPadding(0,getStatusBarHeight(),0,0);
        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager());

        mPager = (SelectiveViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        //mPager.setPageTransformer(false,new ScaleInOutTransformer());
        mPager.setOffscreenPageLimit(3);
        //mPager.setClipToPadding(true);
        //mPager.setPadding((int) getResources().getDimension(R.dimen.view_extension),0,(int) getResources().getDimension(R.dimen.view_extension),0);

        mColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(tab_colors[0]),Color.parseColor(tab_colors[1]), Color.parseColor(tab_colors[2]));
        mColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //window.setStatusBarColor((Integer)animator.getAnimatedValue());
            }

        });

        mColorAnimation.setDuration((3 - 1) * 10000000000l);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        final NiceTabLayout mNiceTabLayout = (NiceTabLayout) findViewById(R.id.sliding_tabs);

        if(mPager.getCurrentItem() != 1)
        {
            floatingActionButton.hide();
        }


        mNiceTabLayout.setViewPager(mPager);
        mNiceTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int i2) {
                mColorAnimation.setCurrentPlayTime((long)((positionOffset + position)* 10000000000l));
            }

            @Override
            public void onPageSelected(int i) {
                //getSupportActionBar().setTitle(TITLES[i]);

                propagateToolbarState(toolbarIsShown());

                if(i == 1)
                {
                    //showToolbar(250);
                    floatingActionButton.show();
                }else{

                    floatingActionButton.hide();
                }
                current_tab = i;

            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
       propagateToolbarState(toolbarIsShown());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inside_channel_activity, menu);
        return  true;
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbarView.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
            if (firstScroll) {
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        // ObservableXxxViews have same API
        // but currently they don't have any common interfaces.
        adjustToolbar(scrollState, view);
    }


    private void adjustToolbar(ScrollState scrollState, View view) {
        int toolbarHeight = mToolbarView.getHeight();
        final Scrollable scrollView = (Scrollable) ((MyUltimateRecyclerView)view.findViewById(R.id.scroll)).getmRecyclerView();
        //final Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollView == null) {
            return;
        }
        int scrollY = scrollView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (toolbarIsShown() || toolbarIsHidden()) {
                // Toolbar is completely moved, so just keep its state
                // and propagate it to other pages
                propagateToolbarState(toolbarIsShown());
            } else {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }

    private Fragment getFragmentAt(int i) {
        return mPagerAdapter.getItemAt(i);
    }

    private void propagateToolbarState(boolean isShown) {
        int toolbarHeight = mToolbarView.getHeight();

        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            Fragment f = mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            View view = f.getView();
            if (view == null) {
                continue;
            }
            propagateToolbarState(isShown, view, toolbarHeight);
        }
    }

    private void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {
        if(1==1) {
            return;
        }

        Scrollable scrollView = (Scrollable) view.findViewById(R.id.scroll);
        if (scrollView == null) {
            return;
        }
        if (isShown) {
            // Scroll up
            if (0 < scrollView.getCurrentScrollY()) {
                scrollView.scrollVerticallyTo(0);
            }
        } else {
            // Scroll down (to hide padding)
            if (scrollView.getCurrentScrollY() < toolbarHeight) {
                scrollView.scrollVerticallyTo(toolbarHeight);
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -(mToolbarView.getHeight());
    }

    public void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
            //mToolbarView.startAnimation(fadeIn);
        }
        propagateToolbarState(true);
    }

    public void showToolbar(int duration) {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(duration).start();
        }
        propagateToolbarState(true);
    }

    public void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(400).start();
            //mToolbarView.startAnimation(fadeOut);
        }
        propagateToolbarState(false);
    }



    private class NavigationAdapter extends CacheFragmentStatePagerAdapter implements NiceTabLayout.IconTabProvider {

        private int mScrollY;

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        public int getPageIconResId(int position) {
            return ICONS[position];
        }

        @Override
        protected Fragment createItem(int position) {
            // Initialize fragments.
            // Please be sure to pass scroll position to each fragments using setArguments.
            switch (position) {

                case 0: {
                    HomeFragment f = new HomeFragment();
                    if (0 < mScrollY) {
                        Bundle args = new Bundle();
                        args.putInt(ViewPagerTabRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
                        f.setArguments(args);
                    }
                    return f;
                }
                case 1: {
                    MyChannelsFragment f = new MyChannelsFragment();
                    if (0 < mScrollY) {
                        Bundle args = new Bundle();
                        args.putInt(ViewPagerTabRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
                        f.setArguments(args);
                    }
                    return f;
                }
                case 2: {
                    AccountFragment f = new AccountFragment();
                    if (0 < mScrollY) {
                        Bundle args = new Bundle();
                        args.putInt(ViewPagerTabRecyclerViewFragment.ARG_INITIAL_POSITION, 1);
                        f.setArguments(args);
                    }
                    return f;               }
                case 3:

                default:
                    return new HomeFragment();            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    public void fadeViewBetweenColors(final View view, String color1, String color2){
        final float[] from = new float[3],
                to =   new float[3];
        if(color1.length() >= 1 && color2.length() >= 1 && view != null) { //Parameters are correct

            Color.colorToHSV(Color.parseColor(color1), from);   // from white
            Color.colorToHSV(Color.parseColor(color2), to);     // to red

            ValueAnimator anim = ValueAnimator.ofFloat(0, 1);   // animate from 0 to 1
            anim.setDuration(600);                              // for 300 ms

            final float[] hsv = new float[3];                  // transition color
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // Transition along each axis of HSV (hue, saturation, value)
                    hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
                    hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
                    hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();

                    view.setBackgroundColor(Color.HSVToColor(hsv));
                }
            });
            anim.start();
        }
    }

    public SelectiveViewPager getViewPager() {
        return mPager;
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}