package com.roy.douapp.ui.fragment.movie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.roy.douapp.AppConfig;
import com.roy.douapp.DouApplication;
import com.roy.douapp.DouKit;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseFragment;
import com.roy.douapp.http.bean.movie.JsonMovieBean;
import com.roy.douapp.http.bean.movie.Subjects;
import com.roy.douapp.service.LocationService;
import com.roy.douapp.ui.activity.movie.MovieDetailsActivity;
import com.roy.douapp.ui.presenter.HotMoviePresenter;
import com.roy.douapp.ui.view.HotMovieView;
import com.roy.douapp.ui.presenter.impl.HotMoviePresenterImpl;
import com.roy.douapp.utils.common.LogUtils;
import com.roy.douapp.utils.common.ScreenUtils;
import com.roy.douapp.utils.common.SharedPreferencesUtil;
import com.roy.douapp.utils.image.ImageUtils;
import com.roy.douapp.widget.SpacesItemDecoration;
import com.yuyh.library.utils.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/11.
 */

public class HotMovieFragment extends BaseFragment implements HotMovieView,
        BaseQuickAdapter.OnItemClickListener ,
        View.OnClickListener{
    private static final String TAG = HotMovieFragment.class.getSimpleName();
    private RecyclerView ryv_movie;
    private TextView tv_tip;
    private TextView tv_choose_city;

    private View errorView;

    private HMAdapter mHMAdapter;
    private List<Subjects> mSubjectsList = new ArrayList<>();
    private HotMoviePresenter mPresenter;

    private LocationService mLocationService;

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
        //iniData();
        initBaiduLoc();
    }

    private void findView(View view) {
        ryv_movie = (RecyclerView) view.findViewById(R.id.ryv_movie);
        tv_tip = (TextView) view.findViewById(R.id.tv_tip);
        tv_choose_city = (TextView) view.findViewById(R.id.tv_choose_city);
    }

    private void initView() {
        ryv_movie.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), ScreenUtils.dipToPx(getActivity(), 10), 0);
        ryv_movie.addItemDecoration(spacesItemDecoration);
        mHMAdapter = new HMAdapter();
        ryv_movie.setAdapter(mHMAdapter);
        mPresenter = new HotMoviePresenterImpl(getActivity(), this);
    }

    private void initBaiduLoc() {
        mLocationService = ((DouApplication) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        mLocationService.registerListener(mListener);
        //注册监听
        mLocationService.setLocationOption(mLocationService.getDefaultLocationClientOption());
        mLocationService.start();
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            StringBuffer sb = new StringBuffer(256);
            Message msg = Message.obtain();
            LogUtils.log(TAG, "locType:" + location.getLocType(), LogUtils.DEBUG);
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if(location.getLocType() != BDLocation.TypeNetWorkException){
                    sb.append(location.getCityCode());
                    sb.append(location.getCity());
                    msg.what = 0;
                    msg.obj = location;
                    if (!TextUtils.equals(location.getCity(), DouKit.getNowCity())) {
                        SharedPreferencesUtil.getInstance(getActivity()).saveString("NowCity", location.getCity());
                    }
                }else{
                    if (!TextUtils.isEmpty(DouKit.getNowCity())) {
                        msg.what = 1;
                        msg.obj = "定位失败,上次定位为" + DouKit.getNowCity()+"(点击重试)";
                    } else {
                        msg.what = 2;
                        msg.obj = "定位失败,默认定位为北京市(点击重试)";
                    }
                }
            } else {
                if (!TextUtils.isEmpty(DouKit.getNowCity())) {
                    msg.what = 1;
                    msg.obj = "定位失败,上次定位为" + DouKit.getNowCity()+"(点击重试)";
                } else {
                    msg.what = 2;
                    msg.obj = "定位失败,默认定位为北京市(点击重试)";
                }
            }
            mHandler.sendMessage(msg);
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    BDLocation mBDLocation = (BDLocation) msg.obj;
                    tv_choose_city.setText(mBDLocation.getProvince() + "," + mBDLocation.getCity());
                    mPresenter.initialized(mBDLocation.getCity());
                    ToastUtils.showSingleLongToast("定位成功："+DouKit.getNowCity());
                    break;
                case 1:
                    tv_choose_city.setText(msg.obj.toString());
                    mPresenter.initialized(DouKit.getNowCity());
                    ToastUtils.showSingleLongToast("定位失败");
                    break;
                case 2:
                    tv_choose_city.setText(msg.obj.toString());
                    mPresenter.initialized();
                    ToastUtils.showSingleLongToast("定位失败");
                    break;
            }
            mLocationService.stop();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationService.unregisterListener(mListener); //注销掉监听
        mLocationService.stop();
    }

    private void initEvent() {
        mHMAdapter.setOnItemClickListener(this);
        tv_choose_city.setOnClickListener(this);
    }

    private void iniData() {
        mPresenter = new HotMoviePresenterImpl(getActivity(), this);
        mPresenter.initialized();
    }

    @Override
    public void hotMovie(JsonMovieBean jsonMovieBean) {
        mSubjectsList.clear();
        mSubjectsList.addAll(jsonMovieBean.getSubjects());
        mHMAdapter.notifyDataSetChanged();
        tv_tip.setText("");
        //hideLoadingDialog();
    }

    @Override
    public void showError(String msg) {
        if (mSubjectsList.size() <= 0) {
            errorView = LayoutInflater.from(getActivity()).inflate(R.layout.view_error, (ViewGroup) ryv_movie.getParent(), false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choose_city:
                tv_choose_city.setText("定位中...");
                mLocationService.start();
                break;
        }
    }


    private class HMAdapter extends BaseQuickAdapter<Subjects, BaseViewHolder> {

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
            helper.setText(R.id.tv_name, item.getTitle());
            if (TextUtils.isEmpty(item.getRating().getAverage() + "")) {
                helper.setText(R.id.tv_rating, "暂无评分");
            } else {
                helper.setText(R.id.tv_rating, "评分：" + String.valueOf(item.getRating().getAverage()));
            }
        }
    }
}
