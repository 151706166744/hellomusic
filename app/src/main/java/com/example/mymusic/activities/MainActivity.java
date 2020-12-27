package com.example.mymusic.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.R;
import com.example.mymusic.adapters.MusicGridAdapter;
import com.example.mymusic.adapters.MusicListAdapter;
import com.example.mymusic.helps.RealmHelper;
import com.example.mymusic.models.MusicSourceModel;
import com.example.mymusic.views.GridSpaceItemDecoration;

public class MainActivity extends BaseActivity {
    private RecyclerView mRvGrid,mRvList;
    private MusicGridAdapter mGridAdapter;
    private MusicListAdapter mListAdapter;
    private RealmHelper mRealmHelper;
    private MusicSourceModel mMusicSourceModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }
    private void initData () {
        mRealmHelper = new RealmHelper();
        mMusicSourceModel = mRealmHelper.getMusicSource();
    }
    /**
     * 初始化View
     */
    private void initView () {
        initNavBar(true, "我的音乐", true);

        mRvGrid=findViewById(R.id.rv_grid);
        mRvGrid.setLayoutManager(new GridLayoutManager(this,3));
        /*mRvGrid.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));*/
        /*mRvGrid.addItemDecoration(new DividerItemDecoration(getResources().getDimensionPixelSize(R.dimen.albumMarginSize));*/
        /*取消滑动*/
        mRvGrid.setNestedScrollingEnabled(false);
        mRvGrid.addItemDecoration(new GridSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.albumMarginSize), mRvGrid));
        mGridAdapter = new MusicGridAdapter(this,mMusicSourceModel.getAlbum());
        mRvGrid.setAdapter(mGridAdapter);
        /*
        * 1.假如已经知道列表高度的情况下，可以直接在布局中把recyclerView的高度定义上
        * 2.不知道列表高度的情况下，需要动手计算RecyclerView的高度
        * */
        mRvList = findViewById(R.id.rv_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRvList.setNestedScrollingEnabled(false);
        mListAdapter = new MusicListAdapter(this,mRvList,mMusicSourceModel.getHot());
        mRvList.setAdapter(mListAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealmHelper.close();
    }
}