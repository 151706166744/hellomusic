package com.example.mymusic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymusic.R;
import com.example.mymusic.activities.PlayMusicActivity;
import com.example.mymusic.models.MusicModel;

import java.util.List;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private Context mContext;
    private View mItemView;
    private RecyclerView mRv;
    private Boolean isCalculationRvHeight;
    private List<MusicModel> mDataSource;

    public MusicListAdapter(Context context,RecyclerView recyclerView,List<MusicModel>dataSource){
        mContext=context;
        mRv=recyclerView;
        this.mDataSource=dataSource;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mItemView=LayoutInflater.from(mContext).inflate(R.layout.item_list_music,parent,false);
        return new ViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setRecyclerViewHeight();

        MusicModel musicModel=mDataSource.get(position);


        Glide.with(mContext)
                /*.load("http://img.mp.sohu.com/upload/20170606/9579a5d9545d447e9a8497158e819635_th.png")*/
                .load(musicModel.getPoster())
                .into(MusicListAdapter.ViewHolder.ivIcon);

        ViewHolder.tvName.setText(musicModel.getName());
        ViewHolder.tvAuthor.setText(musicModel.getAuthor());

        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra(PlayMusicActivity.MUSIC_ID, musicModel.getMusicId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
    /*
    * 1.获取ItemView的高度
    * 2.获取ItemView的数量
    * 3.1x2得到RecyclerView的高度
    * */
    public void setRecyclerViewHeight(){
        if ( mRv==null) return;
        /*isCalculationRvHeight=true;*/
        /*获取ItemView的高度*/
        RecyclerView.LayoutParams itemViewLp = (RecyclerView.LayoutParams) mItemView.getLayoutParams();
        /*获取ItemView的数量*/
        int itemCount = getItemCount();
        /*1x2得到RecyclerView的高度*/
        int recyclerViewHeight = itemViewLp.height * itemCount;
        /*设置RecyclerView的高度*/
        LinearLayout.LayoutParams rvLp=(LinearLayout.LayoutParams)mRv.getLayoutParams();
        rvLp.height=recyclerViewHeight;
        mRv.setLayoutParams(rvLp);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        static View itemView;
        static ImageView ivIcon;
        static  TextView tvName, tvAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
