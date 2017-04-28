package com.roy.douapp.ui.fragment.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import com.roy.douapp.base.BaseFragment;
import com.roy.douapp.http.bean.music.billcategory.Content;
import com.roy.douapp.http.bean.music.billcategory.JsonMusicBillBean;
import com.roy.douapp.ui.activity.music.MusicBillListActivity;
import com.roy.douapp.ui.presenter.Presenter;
import com.roy.douapp.ui.presenter.callback.MusicBillCB;
import com.roy.douapp.ui.presenter.impl.MusicBillPresenterImpl;
import com.roy.douapp.utils.common.ScreenUtils;
import com.roy.douapp.utils.image.ImageUtils;
import com.roy.douapp.widget.SpacesItemDecoration;
import com.yuyh.library.utils.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */

public class MusicBillFragment extends BaseFragment implements MusicBillCB,BaseQuickAdapter.OnItemClickListener {
    private static final String TAG = MusicBillFragment.class.getSimpleName();
    private RecyclerView ryv_bill;

    private View errorView;

    private MBAdapter mMBAdapter;
    private List<Content> mContentList = new ArrayList<>();
    private Presenter mPresenter;

    public static MusicBillFragment newInstance() {
        Bundle args = new Bundle();
        MusicBillFragment fragment = new MusicBillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicbill, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //showLoadingDialog();
        init();
        mMBAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_bill.getParent());
    }

    private void init() {
        initView();
        initEvent();
        iniData();
    }

    private void findView(View view) {
        ryv_bill = (RecyclerView) view.findViewById(R.id.ryv_bill);
    }

    private void initView() {
        ryv_bill.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), 0);
        ryv_bill.addItemDecoration(spacesItemDecoration);
        mMBAdapter = new MBAdapter();
        ryv_bill.setAdapter(mMBAdapter);
    }

    private void initEvent() {
        mMBAdapter.setOnItemClickListener(this);
    }

    private void iniData() {
        mPresenter = new MusicBillPresenterImpl(getActivity(), this);
        mPresenter.initialized();
    }

    @Override
    public void musicBill(JsonMusicBillBean jsonMusicBillBean) {
        mContentList.addAll(jsonMusicBillBean.getContent());
        mMBAdapter.notifyDataSetChanged();
        //hideLoadingDialog();
    }

    @Override
    public void showError(String msg) {
        if (mContentList.size() <= 0) {
            errorView = LayoutInflater.from(getActivity()).inflate(R.layout.view_error,(ViewGroup)ryv_bill.getParent(),false);
            mMBAdapter.setEmptyView(errorView);
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.initialized();
                    mMBAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_bill.getParent());
                }
            });
        }
        //hideLoadingDialog();
        ToastUtils.getSingleToast(msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.putExtra(AppConfig.BILL_TYPE, mContentList.get(position).getType());
        intent.putExtra(AppConfig.BILL_IMAGE,mContentList.get(position).getPic_s260());
        intent.setClass(getActivity(), MusicBillListActivity.class);
        startActivity(intent);
    }

    private class MBAdapter extends BaseQuickAdapter<Content, BaseViewHolder> {

        public MBAdapter() {
            super(R.layout.item_musicbill, mContentList);
        }

        @Override
        protected void convert(BaseViewHolder helper, Content item) {
            ViewGroup.LayoutParams params = helper.getView(R.id.iv_bill).getLayoutParams();
            params.width = (ScreenUtils.getScreenWidthDp(mContext) - ScreenUtils.dipToPx(mContext, 80)) / 3;
            params.height = params.width;
            helper.getView(R.id.iv_bill).setLayoutParams(params);
            ImageUtils.displayImage(mContext, item.getPic_s260(), (ImageView) helper.getView(R.id.iv_bill));

            helper.setText(R.id.tv_bill_name, item.getName());
        }
    }
}
