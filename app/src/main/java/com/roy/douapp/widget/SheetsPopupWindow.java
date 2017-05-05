package com.roy.douapp.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.douapp.R;
import com.roy.douapp.http.bean.music.playlist.MusicSheets;
import com.roy.douapp.ui.fragment.music.MusicSheetsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/4/26.
 */

public class SheetsPopupWindow extends PopupWindow {
    private Context mContext;
    private RecyclerView ryvMenuList;
    private LinearLayout llClose;
    private View mMenuView;

    private SPWAdapter mSPWAdapter;

    private List<MusicSheets> mMusicSheetsList = new ArrayList<>();

    public SheetsPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.view_choose_sheets, null);


        init();

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popup_anim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFFFFF);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }


    private void init() {
        findView();
        initView();
        initData();
        initEvent();
    }

    private void findView() {
        ryvMenuList = (RecyclerView) mMenuView.findViewById(R.id.ryv_menu_list);
        llClose = (LinearLayout) mMenuView.findViewById(R.id.ll_close);
    }

    private void initView() {
        ryvMenuList.setLayoutManager(new LinearLayoutManager(mContext));
        mSPWAdapter = new SPWAdapter();
        ryvMenuList.setAdapter(mSPWAdapter);
    }

    private void initData() {
        for (int i = 0; i < 2; i++) {
            mMusicSheetsList.add(new MusicSheets(i, "歌单" + (i + 1), (i + 1) * 5));
        }
        mSPWAdapter.notifyDataSetChanged();
    }

    private void initEvent() {
        llClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private class SPWAdapter extends BaseQuickAdapter<MusicSheets, BaseViewHolder> {

        public SPWAdapter() {
            super(R.layout.item_musicsheets, mMusicSheetsList);
        }

        @Override
        protected void convert(BaseViewHolder helper, MusicSheets item) {
            helper.setText(R.id.tv_sheets_name, item.getSheetsName());
            helper.setText(R.id.tv_song_count,String.valueOf(item.getSongCount()));
        }
    }

}