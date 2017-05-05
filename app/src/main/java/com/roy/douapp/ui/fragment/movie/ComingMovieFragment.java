package com.roy.douapp.ui.fragment.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.roy.douapp.http.bean.movie.Casts;
import com.roy.douapp.http.bean.movie.Directors;
import com.roy.douapp.http.bean.movie.JsonMovieBean;
import com.roy.douapp.http.bean.movie.Subjects;
import com.roy.douapp.ui.activity.movie.MovieDetailsActivity;
import com.roy.douapp.ui.presenter.ComingMoviePresenter;
import com.roy.douapp.ui.view.ComingMovieView;
import com.roy.douapp.ui.presenter.impl.ComingMoviePresenterImpl;
import com.roy.douapp.utils.common.LogUtils;
import com.roy.douapp.utils.common.ScreenUtils;
import com.roy.douapp.utils.image.ImageUtils;
import com.roy.douapp.widget.SpacesItemDecoration;
import com.yuyh.library.utils.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class ComingMovieFragment extends BaseFragment implements ComingMovieView, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ComingMovieFragment.class.getSimpleName();
    //
    private SwipeRefreshLayout srl_movie;
    private RecyclerView ryv_movie;
    private TextView tv_tip;

    private View errorView;

    // ComingMovieRecyclerAdapter mComingMovieRecyclerAdapter;
    private CMAdapter mCMAdapter;
    private List<Subjects> mSubjectsList = new ArrayList<>();

    private ComingMoviePresenter mPresenter;
    private boolean isLoadMore = false;
    private int pageCount;

    public static ComingMovieFragment newInstance() {

        Bundle args = new Bundle();

        ComingMovieFragment fragment = new ComingMovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comingmovie, container, false);
        findView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //showLoadingDialog();
        init();
        mCMAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) ryv_movie.getParent());
    }

    private void init() {
        initView();
        initData();
        initEvent();
    }

    private void findView(View view) {
        srl_movie = (SwipeRefreshLayout) view.findViewById(R.id.srl_movie);
        ryv_movie = (RecyclerView) view.findViewById(R.id.ryv_movie);
        tv_tip = (TextView) view.findViewById(R.id.tv_tip);
    }

    private void initData() {
        mPresenter = new ComingMoviePresenterImpl(getActivity(), this);
        mPresenter.initialized();
    }


    private void initView() {
        ryv_movie.setLayoutManager(new LinearLayoutManager(getActivity()));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), 0);
        ryv_movie.addItemDecoration(spacesItemDecoration);
        mCMAdapter = new CMAdapter();
        ryv_movie.setAdapter(mCMAdapter);
    }

    private void initEvent() {
        srl_movie.setOnRefreshListener(this);
        mCMAdapter.setOnItemClickListener(this);
        mCMAdapter.setOnLoadMoreListener(this, ryv_movie);
    }

    @Override
    public void comingMovie(JsonMovieBean jsonMovieBean) {
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
            pageCount = 0;
            mSubjectsList.clear();
            mCMAdapter.setNewData(mSubjectsList);
        }
        if (mCMAdapter.isLoading()) {
            mCMAdapter.loadMoreComplete();
            LogUtils.log(TAG, "total:" + jsonMovieBean.getTotal() + "/current:" + (mSubjectsList.size() + jsonMovieBean.getSubjects().size()), LogUtils.DEBUG);
            if (jsonMovieBean.getTotal() <= (mSubjectsList.size() + jsonMovieBean.getSubjects().size())) {
                mCMAdapter.loadMoreEnd();
            }
        }
        mSubjectsList.addAll(jsonMovieBean.getSubjects());
        mCMAdapter.notifyDataSetChanged();
        tv_tip.setText("");
        //hideLoadingDialog();
    }

    @Override
    public void showError(String msg) {
        if (srl_movie.isRefreshing()) {
            srl_movie.setRefreshing(false);
        }
        if (mCMAdapter.isLoading()) {
            pageCount--;
            mCMAdapter.loadMoreFail();
        }
        if (mSubjectsList.size() <= 0) {
            errorView = LayoutInflater.from(getActivity()).inflate(R.layout.view_error,(ViewGroup)srl_movie.getParent(),false);
            mCMAdapter.setEmptyView(errorView);
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.initialized();
                    mCMAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) srl_movie.getParent());
                }
            });
        }
        //hideLoadingDialog();
        ToastUtils.getSingleToast(msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        mPresenter.initialized();
    }

    @Override
    public void onLoadMoreRequested() {
        pageCount++;
        isLoadMore = true;
        mPresenter.loadMore(pageCount * AppConfig.PAGE_SIZE, AppConfig.PAGE_SIZE);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.putExtra(AppConfig.MOVIE_ID, mSubjectsList.get(position).getId());
        intent.setClass(getActivity(), MovieDetailsActivity.class);
        startActivity(intent);
    }


    private class CMAdapter extends BaseQuickAdapter<Subjects, BaseViewHolder> {

        public CMAdapter() {
            super(R.layout.item_comingmovie, mSubjectsList);
        }

        @Override
        protected void convert(BaseViewHolder helper, Subjects item) {
            ViewGroup.LayoutParams params = helper.getView(R.id.iv_image).getLayoutParams();
            params.width = (ScreenUtils.getScreenWidthDp(mContext) - ScreenUtils.dipToPx(mContext, 80)) / 3;
            params.height = (int) ((420.0 / 300.0) * params.width);
            helper.getView(R.id.iv_image).setLayoutParams(params);

            ImageUtils.displayImage(mContext, item.getImages().getLarge(), (ImageView) helper.getView(R.id.iv_image));

            helper.setText(R.id.tv_name, item.getTitle());

            if (item.getGenres() != null && item.getGenres().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : item.getGenres()) {
                    stringBuilder.append(s + "/");
                }
                helper.setText(R.id.tv_type, "类型：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
            }

            if (item.getDirectors() != null && item.getDirectors().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Directors s : item.getDirectors()) {
                    stringBuilder.append(s.getName() + "/");
                }
                helper.setText(R.id.tv_directors, "导演：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
            }

            if (item.getCasts() != null && item.getCasts().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Casts s : item.getCasts()) {
                    stringBuilder.append(s.getName() + "/");
                }
                helper.setText(R.id.tv_stars, "主演：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
            }

        }
    }


}
