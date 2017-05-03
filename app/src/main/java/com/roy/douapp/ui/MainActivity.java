package com.roy.douapp.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.roy.douapp.DouApplication;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseActivity;
import com.roy.douapp.ui.activity.common.LoginActivity;
import com.roy.douapp.ui.activity.movie.MovieCollectionActivity;
import com.roy.douapp.ui.activity.common.SystemSettingActivity;
import com.roy.douapp.ui.activity.music.MusicPlayActivity;
import com.roy.douapp.ui.adapter.DouBaseFragmentAdapter;
import com.roy.douapp.ui.fragment.movie.MovieFragment;
import com.roy.douapp.ui.fragment.music.MusicFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener,
        RadioGroup.OnCheckedChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout dl_main;
    private ViewPager vp_main;
    private NavigationView nv_main;
    private Button btn_system_setting;
    private Button btn_personsl_center;
    private RadioGroup rg_mian;
    private RadioButton rb_movie;
    private RadioButton rb_music;
    private RadioButton rb_sport;

    private SearchView mSearchView;

    private MovieFragment mMovieFragment;
    private MusicFragment mMusicFragment;

    private SmoothActionBarDrawerToggle mActionBarDrawerToggle;
    //private ActionBarDrawerToggle mActionBarDrawerToggle;

    private DouBaseFragmentAdapter mDouBaseFragmentAdapter;

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    private int mCurrentFragment;
    private InputMethodManager mImm ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startActivity(new Intent(this,SplashActivity.class));
        setContentView(R.layout.activity_main);
        init();
    }


    private void init() {
        findView();
        initView();
        initEvent();
    }

    private void findView() {
        dl_main = (DrawerLayout) findViewById(R.id.dl_main);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        nv_main = (NavigationView) findViewById(R.id.nv_main);
        btn_system_setting = (Button) findViewById(R.id.btn_system_setting);
        btn_personsl_center = (Button) findViewById(R.id.btn_personsl_center);
        rg_mian = (RadioGroup) findViewById(R.id.rg_mian);
        rb_movie = (RadioButton) findViewById(R.id.rb_movie);
        rb_music = (RadioButton) findViewById(R.id.rb_music);
        rb_sport = (RadioButton) findViewById(R.id.rb_sport);
    }


    private void initView() {
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mToolbar.setBackgroundResource(R.color.material_red);
        nv_main.inflateMenu(R.menu.sidebar_menu);
        nv_main.setItemIconTintList(getResources().getColorStateList(android.R.color.black));
        nv_main.setItemBackgroundResource(android.R.color.transparent);
        nv_main.setItemTextColor(getResources().getColorStateList(android.R.color.black));
/*        mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, dl_main, mToolbar, R.string.open, R.string.close);*/
        mActionBarDrawerToggle =
                new SmoothActionBarDrawerToggle(this, dl_main, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        dl_main.addDrawerListener(mActionBarDrawerToggle);

        mMovieFragment = MovieFragment.newInstance();
        mMusicFragment = MusicFragment.newInstance();
        mFragmentList.add(mMovieFragment);
        mFragmentList.add(mMusicFragment);
        mDouBaseFragmentAdapter = new DouBaseFragmentAdapter(getSupportFragmentManager(), mTitleList, mFragmentList);
        vp_main.setAdapter(mDouBaseFragmentAdapter);
        vp_main.setOffscreenPageLimit(3);

    }

    private void initEvent() {
        btn_system_setting.setOnClickListener(this);
        btn_personsl_center.setOnClickListener(this);
        nv_main.setNavigationItemSelectedListener(this);
        vp_main.addOnPageChangeListener(this);
        rg_mian.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_system_setting:
                mActionBarDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, SystemSettingActivity.class));
                    }
                });
                //startActivity(new Intent(this, SystemSettingActivity.class));
                hiddenDrawer();
                break;
            case R.id.btn_personsl_center:
                mActionBarDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                hiddenDrawer();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu_recommend:
                break;
            case R.id.nav_menu_collection:
                mActionBarDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, MovieCollectionActivity.class));
                    }
                });
                break;
            case R.id.nav_menu_theme:
                break;
            case R.id.nav_menu_feedback:
                break;
            case R.id.nav_menu_about:
                break;
        }

        item.setChecked(true);
        hiddenDrawer();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.movie_search).getActionView();
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                mSearchView.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.music_play:
                startActivity(new Intent(MainActivity.this, MusicPlayActivity.class));
                break;
        }
        return true;
    }

    private void hiddenDrawer() {
/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dl_main.closeDrawers();
            }
        }, 500);*/
        dl_main.closeDrawers();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                rg_mian.check(R.id.rb_movie);
                rb_movie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                rb_music.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                rb_sport.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                break;
            case 1:
                rg_mian.check(R.id.rb_music);
                rb_music.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                rb_movie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                rb_sport.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                break;
            case 2:
                rg_mian.check(R.id.rb_sport);
                rb_sport.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                rb_movie.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                rb_music.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_movie:
                mCurrentFragment = 0;
                break;
            case R.id.rb_music:
                mCurrentFragment = 1;
                break;
            case R.id.rb_sport:
                mCurrentFragment = 2;
                break;
        }

        vp_main.setCurrentItem(mCurrentFragment, true);
    }


    public class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {
        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

}
