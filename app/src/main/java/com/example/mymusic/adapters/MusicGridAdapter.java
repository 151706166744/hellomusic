package com.example.mymusic.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymusic.R;
import com.example.mymusic.activities.AlbumListActivity;
import com.example.mymusic.models.AlbumModel;

import java.util.List;

import static com.example.mymusic.R.id.tv_play_num;

public class MusicGridAdapter extends RecyclerView.Adapter<MusicGridAdapter.ViewHolder> {

    private Context mContext;
    private List<AlbumModel>mDataSource;
    public MusicGridAdapter(Context context,List<AlbumModel>dataSource){
        mContext=context;
        this.mDataSource=dataSource;
    }
    @NonNull
    @Override
    public MusicGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_grid_music,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MusicGridAdapter.ViewHolder holder, int position) {
        AlbumModel albumModel=mDataSource.get(position);

        Glide.with(mContext)
                /*.load("http://img.mp.sohu.com/upload/20170606/9579a5d9545d447e9a8497158e819635_th.png")*/
                .load(albumModel.getPoster())
                .into(ViewHolder.ivIcon);
        ViewHolder.mTvPlayNum.setText(albumModel.getPlayNum());
        ViewHolder.mTvName.setText(albumModel.getName());

        ViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumListActivity.class);
                intent.putExtra(AlbumListActivity.AlBUM_ID,albumModel.getAlbumId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        static ImageView ivIcon;
        static View itemView;
        static  TextView mTvPlayNum,mTvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon=itemView.findViewById(R.id.iv_icon);
            this.itemView=itemView;
            mTvPlayNum=itemView.findViewById(tv_play_num);
            mTvName=itemView.findViewById(R.id.tv_name);
        }
    }
}
