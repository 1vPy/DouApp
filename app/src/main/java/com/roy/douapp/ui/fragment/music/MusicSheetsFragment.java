package com.roy.douapp.ui.fragment.music;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseFragment;
import com.roy.douapp.http.bean.music.playlist.MusicSheets;
import com.roy.douapp.utils.common.ScreenUtils;
import com.roy.douapp.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 1vPy(Roy) on 2017/4/21.
 */

public class MusicSheetsFragment extends BaseFragment {
    private LinearLayout ll_local_music;
    private LinearLayout ll_download_music;
    private LinearLayout ll_recent_music;
    private LinearLayout ll_love_music;

    private RecyclerView ryv_music_sheets;

    private View music_sheets_footer;

    private MSAdapter mMSAdapter;

    private List<MusicSheets> mMusicSheetsList = new ArrayList<>();


    public static MusicSheetsFragment newInstance() {

        Bundle args = new Bundle();

        MusicSheetsFragment fragment = new MusicSheetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_muiscsheets, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void findView(View view) {
        ll_local_music = (LinearLayout) view.findViewById(R.id.ll_local_music);
        ll_download_music = (LinearLayout) view.findViewById(R.id.ll_download_music);
        ll_recent_music = (LinearLayout) view.findViewById(R.id.ll_recent_music);
        ll_love_music = (LinearLayout) view.findViewById(R.id.ll_love_music);
        ryv_music_sheets = (RecyclerView) view.findViewById(R.id.ryv_music_sheets);
    }

    private void init() {
        initView();
        initData();
    }

    private void initView() {
        ryv_music_sheets.setLayoutManager(new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mMSAdapter = new MSAdapter();
        music_sheets_footer = LayoutInflater.from(getActivity()).inflate(R.layout.music_sheets_footer_view, null);
        mMSAdapter.setFooterView(music_sheets_footer);
        ryv_music_sheets.setAdapter(mMSAdapter);
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            mMusicSheetsList.add(new MusicSheets(i, "歌单" + (i + 1), (i + 1) * 5));
        }
        mMSAdapter.notifyDataSetChanged();
    }

    private class MSAdapter extends BaseQuickAdapter<MusicSheets, BaseViewHolder> {

        public MSAdapter() {
            super(R.layout.item_musicsheets, mMusicSheetsList);
        }

        @Override
        protected void convert(BaseViewHolder helper, MusicSheets item) {
            helper.setText(R.id.tv_sheets_name, item.getSheetsName());
            helper.setText(R.id.tv_song_count,String.valueOf(item.getSongCount()));
        }
    }
}
