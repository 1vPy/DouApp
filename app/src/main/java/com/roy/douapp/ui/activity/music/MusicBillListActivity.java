package com.roy.douapp.ui.activity.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.douapp.AppConfig;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseSwipeBackActivity;
import com.roy.douapp.http.bean.music.billlist.JsonSongListBean;
import com.roy.douapp.http.bean.music.billlist.SongList;
import com.roy.douapp.ui.presenter.callback.MusicBillListCB;
import com.roy.douapp.ui.presenter.impl.MusicBillListPresenterImpl;
import com.roy.douapp.utils.common.LogUtils;
import com.roy.douapp.utils.common.ScreenUtils;
import com.roy.douapp.utils.image.ImageUtils;
import com.roy.douapp.widget.SpacesItemDecoration;
import com.yuyh.library.utils.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class MusicBillListActivity extends BaseSwipeBackActivity implements MusicBillListCB, BaseQuickAdapter.OnItemClickListener {
    private static final String TAG = MusicBillListActivity.class.getSimpleName();

    private CollapsingToolbarLayout collapsing_toolbar;
    private RecyclerView ryv_billlist;
    private ImageView iv_bill_image;

    private View errorView;

    private MBLAdapter mMBLAdapter;

    private MusicBillListPresenterImpl mPresenter;

    private List<SongList> mSongLists = new ArrayList<>();
    private int billType;
    private String billImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicbilllist);
        //showLoadingDialog();
        init();
        mMBLAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_billlist.getParent());
    }

    private void init() {
        findView();
        initView();
        initData();
        initEvent();
    }

    private void findView() {
        collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        iv_bill_image = (ImageView) findViewById(R.id.iv_bill_image);
        ryv_billlist = (RecyclerView) findViewById(R.id.ryv_billlist);
    }

    private void initView() {
        collapsing_toolbar.setTitle(getString(R.string.music_bill));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicBillListActivity.this.finish();
            }
        });
        ryv_billlist.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(ScreenUtils.dipToPx(this, 10), ScreenUtils.dipToPx(this, 10), ScreenUtils.dipToPx(this, 10), 0);
        ryv_billlist.addItemDecoration(spacesItemDecoration);
        mMBLAdapter = new MBLAdapter();
        mMBLAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        ryv_billlist.setAdapter(mMBLAdapter);
    }

    private void initData() {
        if (getIntent() != null) {
            billType = getIntent().getIntExtra(AppConfig.BILL_TYPE, -1);
            billImage = getIntent().getStringExtra(AppConfig.BILL_IMAGE);
            LogUtils.log(TAG, "billType:" + billType, LogUtils.DEBUG);
        }
        if (!(billType < 0)) {
            mPresenter = new MusicBillListPresenterImpl(this, this);
            mPresenter.initialized(billType);
        }

        if (billImage != null) {
            ImageUtils.displayImage(this, billImage, iv_bill_image);
        }
    }

    private void initEvent() {
        mMBLAdapter.setOnItemClickListener(this);
    }

    @Override
    public void musicBillList(JsonSongListBean jsonSongListBean) {
        collapsing_toolbar.setTitle(jsonSongListBean.getBillboard().getName());
        mSongLists.addAll(jsonSongListBean.getSong_list());
        mMBLAdapter.notifyDataSetChanged();
        //hideLoadingDialog();
    }

    @Override
    public void showError(String msg) {
        LogUtils.log(TAG, "mSongLists:" + mSongLists.size(), LogUtils.DEBUG);
        if (mSongLists.size() <= 0) {
            errorView = LayoutInflater.from(this).inflate(R.layout.view_error, (ViewGroup) ryv_billlist.getParent(), false);
            mMBLAdapter.setEmptyView(errorView);
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.initialized(billType);
                    mMBLAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_billlist.getParent());
                }
            });
        }
        //hideLoadingDialog();
        ToastUtils.getSingleToast(msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.putExtra(AppConfig.SONG_ID, mSongLists.get(position).getSong_id());
        intent.setClass(this, MusicDetailsActivity.class);
        startActivity(intent);
    }


    private class MBLAdapter extends BaseQuickAdapter<SongList, BaseViewHolder> {

        public MBLAdapter() {
            super(R.layout.item_musicbilllist, mSongLists);
        }

        @Override
        protected void convert(BaseViewHolder helper, SongList item) {
            ViewGroup.LayoutParams params = helper.getView(R.id.iv_pic).getLayoutParams();
            params.width = (ScreenUtils.getScreenWidthDp(mContext) - ScreenUtils.dipToPx(mContext, 80)) / 5;
            params.height = params.width;
            helper.getView(R.id.iv_pic).setLayoutParams(params);
            ImageUtils.displayImage(mContext, item.getPic_big(), (ImageView) helper.getView(R.id.iv_pic));
            if (helper.getLayoutPosition() < 9) {
                helper.setText(R.id.tv_rank, "0" + (helper.getLayoutPosition() + 1));
            } else {
                helper.setText(R.id.tv_rank, "" + (helper.getLayoutPosition() + 1));
            }
            helper.setText(R.id.tv_name, item.getTitle());
            helper.setText(R.id.tv_artist_name, "歌手：" + item.getAuthor());
        }
    }
}
