package com.roy.douapp.ui.fragment.music;

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
 * Created by 1vPy(Roy) on 2017/4/13.
 */

public class MusicFragment extends BaseFragment {
    private static final String TAG = MusicFragment.class.getSimpleName();

    private TabLayout tl_music;
    private ViewPager vp_music;

    private MusicBillFragment mMusicBillFragment;
    private MusicSheetsFragment mMusicSheetsFragment;

    private DouBaseFragmentAdapter mDouBaseFragmentAdapter;

    private List<String> mTitleList = new ArrayList<>();
    private List<Fragment> mFragmentList = new ArrayList<>();

    public static MusicFragment newInstance() {
        Bundle args = new Bundle();
        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        initView();
        initEvent();
        iniData();
    }

    private void findView(View view) {
        tl_music = (TabLayout) view.findViewById(R.id.tl_music);
        vp_music = (ViewPager) view.findViewById(R.id.vp_music);
    }

    private void initView() {
        mMusicBillFragment = MusicBillFragment.newInstance();
        mMusicSheetsFragment = mMusicSheetsFragment.newInstance();
        mFragmentList.add(mMusicBillFragment);
        mFragmentList.add(mMusicSheetsFragment);
        mTitleList.add(getString(R.string.music_bill));
        mTitleList.add(getString(R.string.my_music));

        mDouBaseFragmentAdapter = new DouBaseFragmentAdapter(getChildFragmentManager(), mTitleList, mFragmentList);
        vp_music.setAdapter(mDouBaseFragmentAdapter);
        vp_music.setOffscreenPageLimit(3);

        tl_music.setTabGravity(TabLayout.GRAVITY_FILL);
        tl_music.setTabMode(TabLayout.MODE_FIXED);
        tl_music.setupWithViewPager(vp_music);
        tl_music.setTabsFromPagerAdapter(mDouBaseFragmentAdapter);
        tl_music.setBackgroundResource(android.R.color.white);
        tl_music.setTabTextColors(getResources().getColor(android.R.color.holo_blue_dark), getResources().getColor(android.R.color.holo_blue_light));
    }

    private void initEvent() {

    }

    private void iniData() {

    }


}
