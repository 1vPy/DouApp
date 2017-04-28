package com.roy.douapp.ui.activity.movie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.douapp.AppConfig;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseSwipeBackActivity;
import com.roy.douapp.db.manager.DBManager;
import com.roy.douapp.http.bean.collection.MovieCollection;
import com.roy.douapp.utils.common.LogUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */

public class MovieCollectionActivity extends BaseSwipeBackActivity implements OnSwipeMenuItemClickListener {
    private static final String TAG = MovieCollectionActivity.class.getSimpleName();
    private SwipeMenuRecyclerView sryv_collection;
    private TextView tv_tip;

    private CollectionRecyclerAdapter mCollectionRecyclerAdapter;

    private LoadCollection mLoadCollection;

    private List<MovieCollection> mMovieCollectionList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        init();
    }

    private void init() {
        findView();
        initView();
        initEvent();
    }

    private void findView() {
        sryv_collection = (SwipeMenuRecyclerView) findViewById(R.id.sryv_collection);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
    }

    private void initView() {
        LogUtils.log(TAG, "setTitle", LogUtils.DEBUG);
        mToolbar.setTitle(R.string.local_collection);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieCollectionActivity.this.finish();
            }
        });
        sryv_collection.setLayoutManager(new LinearLayoutManager(MovieCollectionActivity.this));
        mCollectionRecyclerAdapter = new CollectionRecyclerAdapter(MovieCollectionActivity.this);
        sryv_collection.setSwipeMenuCreator(swipeMenuCreator);
        sryv_collection.setAdapter(mCollectionRecyclerAdapter);
    }

    private void initData() {
        mLoadCollection = new LoadCollection();
        mLoadCollection.execute();
    }

    private void initEvent() {
        sryv_collection.setSwipeMenuItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {

            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            int width = 200;
            SwipeMenuItem deleteItem = new SwipeMenuItem(MovieCollectionActivity.this)
                    .setBackgroundDrawable(android.R.color.holo_red_light)
                    .setText(getString(R.string.delete)) // 文字，还可以设置文字颜色，大小等。。
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };


    /**
     * Item的菜单被点击的时候调用。
     *
     * @param closeable       closeable. 用来关闭菜单。
     * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
     * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
     * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView
     *                        #RIGHT_DIRECTION.
     */
    @Override
    public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
        closeable.smoothCloseMenu();// 关闭被点击的菜单。
        if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
            Toast.makeText(MovieCollectionActivity.this, R.string.collect_deleted, Toast.LENGTH_SHORT).show();
        } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
            Toast.makeText(MovieCollectionActivity.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
        }
        if (menuPosition == 0) {// 删除按钮被点击。
            DBManager.getInstance(MovieCollectionActivity.this).deleteCollectionByMovieId(mMovieCollectionList.get(adapterPosition).getMovieId());
            mMovieCollectionList.remove(adapterPosition);
            mCollectionRecyclerAdapter.notifyDataSetChanged();
            if (mMovieCollectionList.size() <= 0) {
                tv_tip.setVisibility(View.VISIBLE);
            } else {
                tv_tip.setVisibility(View.GONE);
            }
        }
    }


    private class LoadCollection extends AsyncTask<Void, Void, List<MovieCollection>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog();
            if (mMovieCollectionList != null) {
                mMovieCollectionList.clear();
            }
        }

        @Override
        protected List<MovieCollection> doInBackground(Void... params) {
            return DBManager.getInstance(MovieCollectionActivity.this).searchCollection();
        }

        @Override
        protected void onPostExecute(List<MovieCollection> movieCollections) {
            super.onPostExecute(movieCollections);
            if (movieCollections != null && movieCollections.size() > 0) {
                mMovieCollectionList.addAll(movieCollections);
                tv_tip.setVisibility(View.GONE);
            } else {
                tv_tip.setVisibility(View.VISIBLE);
            }
            mCollectionRecyclerAdapter.notifyDataSetChanged();
            hideLoadingDialog();
        }
    }

    public class CollectionRecyclerAdapter extends SwipeMenuAdapter<CollectionRecyclerAdapter.ItemViewHolder> {
        private Context mContext;


        public CollectionRecyclerAdapter(Context context){
            mContext = context;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder{

            public ItemViewHolder(View itemView) {
                super(itemView);
            }
            LinearLayout ll_root;
            TextView tv_name;
            TextView tv_type;
            TextView tv_star;

        }

        @Override
        public View onCreateContentView(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_collection,parent,false);
            return view;
        }

        @Override
        public ItemViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
            ItemViewHolder viewHolder = new ItemViewHolder(realContentView);
            viewHolder.ll_root = (LinearLayout) realContentView.findViewById(R.id.ll_root);
            viewHolder.tv_name = (TextView) realContentView.findViewById(R.id.tv_name);
            viewHolder.tv_type = (TextView) realContentView.findViewById(R.id.tv_type);
            viewHolder.tv_star = (TextView) realContentView.findViewById(R.id.tv_star);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            holder.tv_name.setText(mMovieCollectionList.get(position).getMovieName());
            holder.tv_type.setText(mMovieCollectionList.get(position).getMovieType());
            holder.tv_star.setText(mMovieCollectionList.get(position).getMovieStar());
            holder.ll_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(AppConfig.MOVIE_ID, mMovieCollectionList.get(position).getMovieId());
                    intent.setClass(mContext, MovieDetailsActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMovieCollectionList.size();
        }

    }
}
