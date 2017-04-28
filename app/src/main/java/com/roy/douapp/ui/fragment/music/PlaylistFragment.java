package com.roy.douapp.ui.fragment.music;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.roy.douapp.DouKit;
import com.roy.douapp.R;
import com.roy.douapp.db.manager.DBManager;
import com.roy.douapp.http.bean.music.playlist.MusicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class PlaylistFragment extends DialogFragment {
    private TextView tv_playlist_clear;
    private ListView lv_play_list;


    private PlaylistAdapter mPlaylistAdapter;
    private List<MusicBean> mMusicBeanList = new ArrayList<>();


    public static PlaylistFragment newInstance() {
        Bundle args = new Bundle();
        PlaylistFragment fragment = new PlaylistFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置从底部弹出
        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);
        View view = inflater.inflate(R.layout.fragment_playlist, container);
        findView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * 0.6);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);

    }

    private void findView(View view) {
        tv_playlist_clear = (TextView) view.findViewById(R.id.tv_playlist_clear);
        lv_play_list = (ListView) view.findViewById(R.id.lv_play_list);
    }

    private void init(){
        initView();
        initData();
    }

    private void initView() {
        mPlaylistAdapter = new PlaylistAdapter();
        lv_play_list.setAdapter(mPlaylistAdapter);
    }

    private void initData() {
        if (DBManager.getInstance(DouKit.getContext()).searchMusic() != null) {
            mMusicBeanList.addAll(DBManager.getInstance(DouKit.getContext()).searchMusic());
            mPlaylistAdapter.notifyDataSetChanged();
        }



    }



    class PlaylistAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMusicBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMusicBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_playlist,null);
                viewHolder = new ViewHolder();
                viewHolder.tv_music_name = (TextView) convertView.findViewById(R.id.tv_music_name);
                viewHolder.tv_singer_name = (TextView) convertView.findViewById(R.id.tv_singer_name);
                viewHolder.tv_remove = (TextView) convertView.findViewById(R.id.tv_remove);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_music_name.setText(mMusicBeanList.get(position).getMusicName());
            viewHolder.tv_singer_name.setText(mMusicBeanList.get(position).getSingerName());
            viewHolder.tv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBManager.getInstance(DouKit.getContext()).deleteMusic(mMusicBeanList.get(position).getMusicId());
                    mMusicBeanList.remove(position);
                }
            });
            return convertView;
        }

        public class ViewHolder{
            TextView tv_music_name;
            TextView tv_singer_name;
            TextView tv_remove;
        }
    }
}
