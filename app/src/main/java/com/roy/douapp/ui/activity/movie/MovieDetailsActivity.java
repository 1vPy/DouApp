package com.roy.douapp.ui.activity.movie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import com.github.clans.fab.FloatingActionButton;
import com.roy.douapp.AppConfig;
import com.roy.douapp.DouKit;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseSwipeBackActivity;
import com.roy.douapp.db.manager.DBManager;
import com.roy.douapp.http.bean.movie.Casts;
import com.roy.douapp.http.bean.movie.DirectorActor;
import com.roy.douapp.http.bean.movie.Directors;
import com.roy.douapp.http.bean.movie.details.JsonDetailBean;
import com.roy.douapp.ui.activity.common.WebViewActivity;
import com.roy.douapp.ui.presenter.callback.MovieDetailsCB;
import com.roy.douapp.ui.presenter.impl.MovieDetailsPresenterImpl;
import com.roy.douapp.utils.common.LogUtils;
import com.roy.douapp.utils.image.ImageUtils;
import com.yuyh.library.utils.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class MovieDetailsActivity extends BaseSwipeBackActivity implements MovieDetailsCB, View.OnClickListener {
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private View errorView;

    private ImageView iv_movie;
    private TextView tv_rating;
    private TextView tv_rating_num;
    private TextView tv_year;
    private TextView tv_type;
    private TextView tv_country;
    private TextView tv_name;
    private TextView tv_original_name;
    private TextView tv_summary;
    private RecyclerView ryv_movie;
    private TextView tv_more_details;
    private TextView tv_buy_tickets;
    private FloatingActionButton fab_collect;

    private MovieDetailsPresenterImpl mPresenter;
    //private MovieDetailsRecyclerAdapter mMovieDetailsRecyclerAdapter;
    private MDAdapter mMDAdapter;

    private JsonDetailBean mJsonDetailBean;

    private String mId;
    private String alt;
    private String schedule_url;
    private List<DirectorActor> mDirectorActors = new ArrayList<>();
    private List<DirectorActor> mDirectorActorList = new ArrayList<>();
    private boolean isCollected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetails);
        showLoadingDialog();
        init();
    }

    private void init() {
        findView();
        initView();
        initEvent();
        initData();
    }

    private void findView() {
        iv_movie = (ImageView) findViewById(R.id.iv_movie);
        tv_rating = (TextView) findViewById(R.id.tv_rating);
        tv_rating_num = (TextView) findViewById(R.id.tv_rating_num);
        tv_year = (TextView) findViewById(R.id.tv_year);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_original_name = (TextView) findViewById(R.id.tv_original_name);
        tv_summary = (TextView) findViewById(R.id.tv_summary);
        ryv_movie = (RecyclerView) findViewById(R.id.ryv_movie);
        tv_more_details = (TextView) findViewById(R.id.tv_more_details);
        tv_buy_tickets = (TextView) findViewById(R.id.tv_buy_tickets);
        fab_collect = (FloatingActionButton) findViewById(R.id.fab_collect);
    }

    private void initView() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailsActivity.this.finish();
            }
        });
        ryv_movie.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mMDAdapter = new MDAdapter();
        ryv_movie.setAdapter(mMDAdapter);

        fab_collect.setEnabled(false);
    }

    private void initEvent() {
        tv_more_details.setOnClickListener(this);
        tv_buy_tickets.setOnClickListener(this);
        fab_collect.setOnClickListener(this);
    }

    private void initData() {
        if (getIntent() != null) {
            mId = getIntent().getStringExtra(AppConfig.MOVIE_ID);
        }
        if (mId != null) {
            mPresenter = new MovieDetailsPresenterImpl(this, this);
            mPresenter.initialized(mId);
        }
        if (DBManager.getInstance(this).searchCollectionByMovieId(mId) != null) {
            fab_collect.setImageResource(R.drawable.star_full);
            isCollected = true;
        }
        DBManager.getInstance(this).closeDB();
    }

    @Override
    public void movieDetails(JsonDetailBean jsonDetailBean) {
        mJsonDetailBean = jsonDetailBean;
        if (!TextUtils.isEmpty(jsonDetailBean.getTitle())) {
            mToolbar.setTitle(jsonDetailBean.getTitle());
            tv_name.setText(jsonDetailBean.getTitle());
        }
        if (jsonDetailBean.getImages() != null) {
            ImageUtils.displayImage(DouKit.getContext(), jsonDetailBean.getImages().getLarge(), iv_movie);
        }
        if (jsonDetailBean.getRating() != null) {
            tv_rating.setText("评分" + jsonDetailBean.getRating().getAverage());
        }
        tv_rating_num.setText(jsonDetailBean.getRatings_count() + "人 评分");
        tv_year.setText(jsonDetailBean.getYear() + " 上映");

        if (jsonDetailBean.getGenres() != null && jsonDetailBean.getGenres().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : jsonDetailBean.getGenres()) {
                stringBuilder.append(s + "/");
            }
            tv_type.setText("类型：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
        }
        if (jsonDetailBean.getCountries() != null && jsonDetailBean.getCountries().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : jsonDetailBean.getCountries()) {
                stringBuilder.append(s + "/");
            }
            tv_country.setText("国家：" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
        }
        tv_original_name.setText(jsonDetailBean.getOriginal_title() + " [原名]");
        tv_summary.setText(jsonDetailBean.getSummary());

        initDirectorActorList(jsonDetailBean);

        alt = jsonDetailBean.getAlt();
        schedule_url = jsonDetailBean.getSchedule_url();


        hideLoadingDialog();
        fab_collect.setEnabled(true);

    }

    private void initDirectorActorList(JsonDetailBean jsonDetailBean) {
        for (Directors directors : jsonDetailBean.getDirectors()) {
            DirectorActor directorActor = new DirectorActor();
            directorActor.setAvatars(directors.getAvatars());
            directorActor.setName(directors.getName());
            directorActor.setId(directors.getId());
            directorActor.setRole(0);
            mDirectorActorList.add(directorActor);
        }

        for (Casts casts : jsonDetailBean.getCasts()) {
            DirectorActor directorActor = new DirectorActor();
            directorActor.setAvatars(casts.getAvatars());
            directorActor.setName(casts.getName());
            directorActor.setId(casts.getId());
            directorActor.setRole(1);
            mDirectorActorList.add(directorActor);
        }
        LogUtils.log(TAG, "size: " + mDirectorActorList.size(), LogUtils.DEBUG);
        mDirectorActors.addAll(mDirectorActorList);
        mMDAdapter.notifyDataSetChanged();

    }

    @Override
    public void showError(String msg) {
/*        if (mDirectorActors.size() <= 0) {
            errorView = LayoutInflater.from(this).inflate(R.layout.view_error,(ViewGroup)iv_movie.getParent(),false);
            mMDAdapter.setEmptyView(errorView);
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.initialized();
                    mMDAdapter.setEmptyView(R.layout.view_loading, (ViewGroup) iv_movie.getParent());
                }
            });
        }*/
        hideLoadingDialog();
        ToastUtils.getSingleToast(msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_more_details:
                openWebView(alt);
                break;
            case R.id.tv_buy_tickets:
                openWebView(schedule_url);
                break;
            case R.id.fab_collect:
                toggleCollect();
                break;
        }
    }

    private void openWebView(String url) {
        Intent intent = new Intent();
        intent.putExtra(AppConfig.URL, url);
        intent.setClass(MovieDetailsActivity.this, WebViewActivity.class);
        startActivity(intent);
    }

    private void toggleCollect() {
        if (isCollected) {
            fab_collect.setImageResource(R.drawable.star_empty);
            DBManager.getInstance(this).deleteCollectionByMovieId(mId);
            isCollected = false;
            ToastUtils.getSingleToast(R.string.collect_deleted, Toast.LENGTH_LONG).show();
        } else {
            fab_collect.setImageResource(R.drawable.star_full);
            DBManager.getInstance(this).insertCollection(mJsonDetailBean);
            isCollected = true;
            ToastUtils.getSingleToast(R.string.collect_succeed, Toast.LENGTH_LONG).show();
        }
        DBManager.getInstance(this).closeDB();
    }

    private class MDAdapter extends BaseQuickAdapter<DirectorActor, BaseViewHolder> {

        public MDAdapter() {
            super(R.layout.item_moviedetails, mDirectorActors);
        }

        @Override
        protected void convert(BaseViewHolder helper, DirectorActor item) {
            if (item.getAvatars() != null) {
                ImageUtils.displayImage(mContext, item.getAvatars().getLarge(), (ImageView) helper.getView(R.id.iv_avatar));
            } else {
                ImageUtils.displayImage(mContext, null, (ImageView) helper.getView(R.id.iv_avatar));
            }
            helper.setText(R.id.tv_name, item.getName());
            switch (item.getRole()) {
                case 0:
                    helper.setText(R.id.tv_role, "[导演]");
                    break;
                case 1:
                    helper.setText(R.id.tv_role, "[演员]");
                    break;
            }
        }
    }
}
