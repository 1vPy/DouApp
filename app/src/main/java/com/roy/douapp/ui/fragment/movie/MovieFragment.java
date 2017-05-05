package com.roy.douapp.ui.fragment.movie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roy.douapp.R;
import com.roy.douapp.base.BaseFragment;
import com.roy.douapp.ui.adapter.DouBaseFragmentAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 1vPy(Roy) on 2017/2/16.
 */

public class MovieFragment extends BaseFragment {
    private static final String TAG = MovieFragment.class.getSimpleName();

    private TabLayout tl_movie;
    private ViewPager vp_movie;

    private HotMovieFragment mHotMovieFragment;
    private ComingMovieFragment mComingMovieFragment;

    private DouBaseFragmentAdapter mDouBaseFragmentAdapter;

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    public static MovieFragment newInstance() {
        Bundle args = new Bundle();
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init(){
        initView();
        initEvent();
        iniData();
    }

    private void findView(View view){
        tl_movie = (TabLayout) view.findViewById(R.id.tl_movie);
        vp_movie = (ViewPager) view.findViewById(R.id.vp_movie);
    }

    private void initView(){
        mHotMovieFragment = HotMovieFragment.newInstance();
        mComingMovieFragment = ComingMovieFragment.newInstance();
        mFragmentList.add(mHotMovieFragment);
        mFragmentList.add(mComingMovieFragment);
        mTitleList.add(getString(R.string.recent_hit));
        mTitleList.add(getString(R.string.coming_soon));
        mDouBaseFragmentAdapter = new DouBaseFragmentAdapter(getChildFragmentManager(), mTitleList, mFragmentList);
        vp_movie.setAdapter(mDouBaseFragmentAdapter);
        vp_movie.setOffscreenPageLimit(3);

        tl_movie.setTabGravity(TabLayout.GRAVITY_FILL);
        tl_movie.setTabMode(TabLayout.MODE_FIXED);
        tl_movie.setupWithViewPager(vp_movie);
        tl_movie.setTabsFromPagerAdapter(mDouBaseFragmentAdapter);
        tl_movie.setBackgroundResource(android.R.color.white);
        tl_movie.setTabTextColors(getResources().getColor(android.R.color.holo_blue_dark), getResources().getColor(android.R.color.holo_blue_light));
    }

    private void initEvent(){

    }

    private void iniData(){

    }
}
