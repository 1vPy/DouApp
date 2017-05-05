package com.roy.douapp.ui.view;

import com.roy.douapp.http.bean.collection.MovieCollection;

import java.util.List;

/**
 * Created by 1vPy(Roy) on 2017/5/5.
 */

public interface MovieCollectionView extends BaseView{
    void MovieCollection(List<MovieCollection> movieCollectionList);
}
