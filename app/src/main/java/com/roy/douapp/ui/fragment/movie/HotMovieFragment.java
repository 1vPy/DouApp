package com.roy.douapp.ui.fragment.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.douapp.AppConfig;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseFragment;
import com.roy.douapp.http.bean.movie.JsonMovieBean;
import com.roy.douapp.http.bean.movie.Subjects;
import com.roy.douapp.ui.activity.movie.MovieDetailsActivity;
import com.roy.douapp.ui.presenter.Presenter;
import com.roy.douapp.ui.presenter.callback.HotMovieCB;
import com.roy.douapp.ui.presenter.impl.HotMoviePresenterImpl;
import com.roy.douapp.utils.common.ScreenUtils;
import com.roy.douapp.utils.image.ImageUtils;
import com.roy.douapp.widget.SpacesItemDecoration;
import com.yuyh.library.utils.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class HotMovieFragment extends BaseFragment implements HotMovieCB,BaseQuickAdapter.OnItemClickListener{
    private static final String TAG = HotMovieFragment.class.getSimpleName();
    private RecyclerView ryv_movie;
    private TextView tv_tip;

    private View errorView;

    private HMAdapter mHMAdapter;
    private List<Subjects> mSubjectsList = new ArrayList<>();
    private Presenter mPresenter;

    public static HotMovieFragment newInstance() {
        Bundle args = new Bundle();
        HotMovieFragment fragment = new HotMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotmovie, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //showLoadingDialog();
        init();
        mHMAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_movie.getParent());
    }

    private void init() {
        initView();
        initEvent();
        iniData();
    }

    private void findView(View view) {
        ryv_movie = (RecyclerView) view.findViewById(R.id.ryv_movie);
        tv_tip = (TextView) view.findViewById(R.id.tv_tip);
    }

    private void initView() {
        ryv_movie.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), 0);
        ryv_movie.addItemDecoration(spacesItemDecoration);
        mHMAdapter = new HMAdapter();
        ryv_movie.setAdapter(mHMAdapter);
    }

    private void initEvent() {
        mHMAdapter.setOnItemClickListener(this);
    }

    private void iniData() {
        mPresenter = new HotMoviePresenterImpl(getActivity(), this);
        mPresenter.initialized();
    }

    @Override
    public void hotMovie(JsonMovieBean jsonMovieBean) {
        mSubjectsList.addAll(jsonMovieBean.getSubjects());
        mHMAdapter.notifyDataSetChanged();
        tv_tip.setText("");
        //hideLoadingDialog();
    }

    @Override
    public void showError(String msg) {
        if (mSubjectsList.size() <= 0) {
            errorView = LayoutInflater.from(getActivity()).inflate(R.layout.view_error,(ViewGroup)ryv_movie.getParent(),false);
            mHMAdapter.setEmptyView(errorView);
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.initialized();
                    mHMAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_movie.getParent());
                }
            });
        }
        //hideLoadingDialog();
        ToastUtils.getSingleToast(msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.putExtra(AppConfig.MOVIE_ID, mSubjectsList.get(position).getId());
        intent.setClass(getActivity(), MovieDetailsActivity.class);
        startActivity(intent);
    }


    private class HMAdapter extends BaseQuickAdapter<Subjects,BaseViewHolder>{

        public HMAdapter() {
            super(R.layout.item_hotmovie, mSubjectsList);
        }

        @Override
        protected void convert(BaseViewHolder helper, Subjects item) {
            ViewGroup.LayoutParams params = helper.getView(R.id.iv_image).getLayoutParams();
            params.width = (ScreenUtils.getScreenWidthDp(mContext) - ScreenUtils.dipToPx(mContext, 80)) / 3;
            params.height = (int) ((420.0 / 300.0) * params.width);
            helper.getView(R.id.iv_image).setLayoutParams(params);
            ImageUtils.displayImage(mContext, item.getImages().getLarge(), (ImageView) helper.getView(R.id.iv_image));
            helper.setText(R.id.tv_name,item.getTitle());
            if (TextUtils.isEmpty(item.getRating().getAverage() + "")) {
                helper.setText(R.id.tv_rating,"暂无评分");
            } else {
                helper.setText(R.id.tv_rating,"评分：" + String.valueOf(item.getRating().getAverage()));
            }
        }
    }
}
