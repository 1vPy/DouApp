package com.roy.douapp.ui.activity.common;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.douapp.AppConfig;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseSwipeBackActivity;
import com.roy.douapp.http.bean.movie.Casts;
import com.roy.douapp.http.bean.movie.Directors;
import com.roy.douapp.http.bean.movie.JsonMovieBean;
import com.roy.douapp.http.bean.movie.Subjects;
import com.roy.douapp.ui.activity.movie.MovieDetailsActivity;
import com.roy.douapp.ui.presenter.SearchMoviePresenter;
import com.roy.douapp.ui.view.SearchMovieView;
import com.roy.douapp.ui.presenter.impl.SearchMoviePresenterImpl;
import com.roy.douapp.utils.common.LogUtils;
import com.roy.douapp.utils.common.ScreenUtils;
import com.roy.douapp.utils.image.ImageUtils;
import com.roy.douapp.widget.SpacesItemDecoration;
import com.yuyh.library.utils.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class SearchActivity extends BaseSwipeBackActivity implements SearchMovieView {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private RecyclerView ryv_search;
    private TextView tv_tip;

    private SearchRecyclerAdapter mSearchRecyclerAdapter;

    private SearchMoviePresenter mPresenter;

    private List<Subjects> mSubjectsList = new ArrayList<>();
    private String mQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        showLoadingDialog();
        init();
    }

    private void init() {
        findView();
        initView();
        initData();
        initEvent();
    }

    private void findView() {
        ryv_search = (RecyclerView) findViewById(R.id.ryv_search);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
    }

    private void initView() {
        mToolbar.setTitle(R.string.search_results);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
        ryv_search.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(ScreenUtils.dipToPx(this, 10), ScreenUtils.dipToPx(this, 10), ScreenUtils.dipToPx(this, 10), 0);
        ryv_search.addItemDecoration(spacesItemDecoration);
        mSearchRecyclerAdapter = new SearchRecyclerAdapter(this);
        ryv_search.setAdapter(mSearchRecyclerAdapter);
    }

    private void initData() {
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            mQuery = getIntent().getStringExtra(SearchManager.QUERY);
            LogUtils.log(TAG, "searchKey:" + mQuery, LogUtils.DEBUG);
        }
        if (!mQuery.isEmpty()) {
            mPresenter = new SearchMoviePresenterImpl(this, this);
            mPresenter.initialized(mQuery);
        }
    }

    private void initEvent() {
    }

    @Override
    public void searchMovie(JsonMovieBean jsonMovieBean) {
        mSubjectsList.addAll(jsonMovieBean.getSubjects());
        LogUtils.log(TAG, "mSubjectsList:" + mSubjectsList.size(), LogUtils.DEBUG);
        if (mSubjectsList.size() <= 0) {
            tv_tip.setText(getString(R.string.no_result));
        }
        mSearchRecyclerAdapter.notifyDataSetChanged();
        hideLoadingDialog();
    }

    @Override
    public void showError(String msg) {
        hideLoadingDialog();
        ToastUtils.getSingleToast(msg, Toast.LENGTH_LONG).show();
    }


    public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.ItemViewHolder> {

        private Context mContext;

        public SearchRecyclerAdapter(Context context) {
            mContext = context;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            public ItemViewHolder(View itemView) {
                super(itemView);
            }

            RelativeLayout rl_root;
            ImageView iv_image;
            TextView tv_name;
            TextView tv_type;
            TextView tv_directors;
            TextView tv_stars;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemViewHolder viewHolder = null;
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_search, parent, false);
            viewHolder = new ItemViewHolder(view);
            viewHolder.rl_root = (RelativeLayout) view.findViewById(R.id.rl_root);
            viewHolder.iv_image = (ImageView) view.findViewById(R.id.iv_image);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.tv_directors = (TextView) view.findViewById(R.id.tv_directors);
            viewHolder.tv_stars = (TextView) view.findViewById(R.id.tv_stars);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            ViewGroup.LayoutParams params = holder.iv_image.getLayoutParams();
            int width = ScreenUtils.getScreenWidthDp(mContext);
            int ivWidth = (width - ScreenUtils.dipToPx(mContext, 80)) / 3;
            params.width = ivWidth;
            double height = (420.0 / 300.0) * ivWidth;
            params.height = (int) height;
            holder.iv_image.setLayoutParams(params);
            ImageUtils.displayImage(mContext, mSubjectsList.get(position).getImages().getLarge(), holder.iv_image);

            holder.tv_name.setText(mSubjectsList.get(position).getTitle());

            if (mSubjectsList.get(position).getGenres() != null && mSubjectsList.get(position).getGenres().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : mSubjectsList.get(position).getGenres()) {
                    stringBuilder.append(s + "/");
                }
                holder.tv_type.setText("类型：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
            }

            if (mSubjectsList.get(position).getDirectors() != null && mSubjectsList.get(position).getDirectors().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Directors s : mSubjectsList.get(position).getDirectors()) {
                    stringBuilder.append(s.getName() + "/");
                }
                holder.tv_directors.setText("导演：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
            }

            if (mSubjectsList.get(position).getCasts() != null && mSubjectsList.get(position).getCasts().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Casts s : mSubjectsList.get(position).getCasts()) {
                    stringBuilder.append(s.getName() + "/");
                }
                holder.tv_stars.setText("主演：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
            }
            holder.rl_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(AppConfig.MOVIE_ID, mSubjectsList.get(position).getId());
                    intent.setClass(mContext, MovieDetailsActivity.class);
                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return mSubjectsList == null ? 0 : mSubjectsList.size();
        }

    }
}
