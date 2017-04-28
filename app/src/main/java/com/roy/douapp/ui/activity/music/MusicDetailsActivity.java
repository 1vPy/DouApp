package com.roy.douapp.ui.activity.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.roy.douapp.AppConfig;
import com.roy.douapp.R;
import com.roy.douapp.base.BaseSwipeBackActivity;
import com.roy.douapp.http.bean.music.musicinfo.JsonMusicInfoBean;
import com.roy.douapp.ui.presenter.callback.MusicDetailsCB;
import com.roy.douapp.ui.presenter.impl.MusicDetailsPresenterImpl;
import com.roy.douapp.utils.image.ImageUtils;
import com.roy.douapp.widget.SheetsPopupWindow;
import com.yuyh.library.utils.toast.ToastUtils;


/**
 * Created by Administrator on 2017/4/14.
 */

public class MusicDetailsActivity extends BaseSwipeBackActivity implements MusicDetailsCB,View.OnClickListener{
    private ImageView ivMusic;
    private TextView tvMusicName;
    private TextView tvShare;
    private TextView tvPublishTime;
    private TextView tvCountryLanguage;
    private TextView tvSinger;
    private TextView tvSongwriting;
    private TextView tvCompose;
    private ImageView ivAlbum;
    private TextView tvAlbumName;
    private ImageView ivArtist;
    private TextView tvArtistName;

    private TextView tvPlayMusic;
    private TextView tvDownloadMusic;

    private SheetsPopupWindow mChoosePopupWindow;

    private MusicDetailsPresenterImpl mPresenter;

    private String mId;

    private String musicId;
    private String musicName;
    private String singerName;
    private String fileLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicdetails);
        showLoadingDialog();
        init();
    }

    private void init(){
        findView();
        initView();
        initData();
        initEvent();
    }

    private void findView(){
        ivMusic = (ImageView) findViewById(R.id.iv_music);
        tvMusicName = (TextView) findViewById(R.id.tv_music_name);
        tvShare = (TextView) findViewById(R.id.tv_share);
        tvPublishTime = (TextView) findViewById(R.id.tv_publish_time);
        tvCountryLanguage = (TextView) findViewById(R.id.tv_country_language);
        tvSinger = (TextView) findViewById(R.id.tv_singer);
        tvSongwriting = (TextView) findViewById(R.id.tv_songwriting);
        tvCompose = (TextView) findViewById(R.id.tv_compose);
        ivAlbum = (ImageView) findViewById(R.id.iv_album);
        tvAlbumName = (TextView) findViewById(R.id.tv_album_name);
        ivArtist = (ImageView) findViewById(R.id.iv_artist);
        tvArtistName = (TextView) findViewById(R.id.tv_artist_name);
        tvPlayMusic = (TextView) findViewById(R.id.tv_play_music);
        tvDownloadMusic = (TextView) findViewById(R.id.tv_download_music);
    }

    private void initView(){
        mToolbar.setTitle(R.string.music_details);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicDetailsActivity.this.finish();
            }
        });
    }

    private void initData(){
        if (getIntent() != null) {
            mId = getIntent().getStringExtra(AppConfig.SONG_ID);
        }
        if (mId != null) {
            mPresenter = new MusicDetailsPresenterImpl(this, this);
            mPresenter.initialized(mId);
        }
    }

    private void initEvent(){
        tvPlayMusic.setOnClickListener(this);
        tvDownloadMusic.setOnClickListener(this);
    }

    @Override
    public void musicDetails(JsonMusicInfoBean jsonMusicInfoBean) {
        mToolbar.setTitle(jsonMusicInfoBean.getSonginfo().getTitle());
        ImageUtils.displayImage(this,jsonMusicInfoBean.getSonginfo().getPicPremium(),ivMusic);
        tvMusicName.setText(jsonMusicInfoBean.getSonginfo().getTitle());
        tvShare.setText(jsonMusicInfoBean.getSonginfo().getShareNum().toString());
        tvPublishTime.setText(jsonMusicInfoBean.getSonginfo().getPublishtime());
        tvCountryLanguage.setText(jsonMusicInfoBean.getSonginfo().getCountry()+"["+jsonMusicInfoBean.getSonginfo().getLanguage()+"]");
        tvSinger.setText(jsonMusicInfoBean.getSonginfo().getAuthor()+"[歌手]");
        tvSongwriting.setText(jsonMusicInfoBean.getSonginfo().getSongwriting()+"[作词]");
        tvCompose.setText(jsonMusicInfoBean.getSonginfo().getCompose()+"[作曲]");
        ImageUtils.displayImage(this,jsonMusicInfoBean.getSonginfo().getAlbum500500(),ivAlbum);
        tvAlbumName.setText(jsonMusicInfoBean.getSonginfo().getAlbumTitle());
        ImageUtils.displayImage(this,jsonMusicInfoBean.getSonginfo().getArtist500500(),ivArtist);
        tvArtistName.setText(jsonMusicInfoBean.getSonginfo().getAuthor());
        musicName = jsonMusicInfoBean.getSonginfo().getTitle();
        singerName = jsonMusicInfoBean.getSonginfo().getAuthor();
        musicId = jsonMusicInfoBean.getSonginfo().getSongId();
        fileLink = jsonMusicInfoBean.getSongurl().getUrl().get(0).getFileLink();
        hideLoadingDialog();
    }

    @Override
    public void showError(String msg) {
        hideLoadingDialog();
        ToastUtils.getSingleToast(msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_play_music:
/*                MusicBean musicBean = new MusicBean();
                musicBean.setMusicName(musicName);
                musicBean.setSingerName(singerName);
                musicBean.setMusicId(musicId);
                DBManager.getInstance(this).insertMusic(musicBean);*/
                mChoosePopupWindow = new SheetsPopupWindow(MusicDetailsActivity.this, this);
                //显示窗口
                mChoosePopupWindow.showAtLocation(MusicDetailsActivity.this.findViewById(R.id.ll_music_detail), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.tv_download_music:
                break;
        }
    }




}
