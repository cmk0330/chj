package com.dikai.chenghunjiclient.adapter.store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dikai.chenghunjiclient.R;
import com.dikai.chenghunjiclient.activity.store.WedPrizeInfoActivity;
import com.dikai.chenghunjiclient.adapter.me.StoreItemAdapter;
import com.dikai.chenghunjiclient.entity.GoodsItemBean;
import com.dikai.chenghunjiclient.entity.GoodsTypeBean;
import com.dikai.chenghunjiclient.entity.ResultGetGoodListHead;
import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Lucio on 2018/5/24.
 */

public class NewWedStoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, StoreItemAdapter.OnItemClickListener {

    private Context context;
    private List<Object> list;

    public NewWedStoreAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.item_invite_wed_layout, parent, false);
            HeadViewHolder holder = new HeadViewHolder(view);
            holder.apply.setTag(holder);
            holder.apply.setOnClickListener(this);
            holder.share.setTag(holder);
            holder.share.setOnClickListener(this);
            holder.record.setTag(holder);
            holder.record.setOnClickListener(this);
            holder.rule.setTag(holder);
            holder.rule.setOnClickListener(this);
            holder.qrcode.setTag(holder);
            holder.qrcode.setOnClickListener(this);
            return holder;
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_wedding_store_layout, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.toAll.setTag(holder);
            holder.toAll.setOnClickListener(this);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.mRecyclerView.setLayoutManager(mLayoutManager);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            GoodsTypeBean bean = (GoodsTypeBean) list.get(position);
            ((MyViewHolder) holder).name.setText(bean.getTypeName());
//            ((MyViewHolder) holder).info.setText(bean.getIntroduction());
            switch (position%6){
                case 1:
                    ((MyViewHolder) holder).bg.setImageResource(R.drawable.jianbian_blue);
                    break;
                case 2:
                    ((MyViewHolder) holder).bg.setImageResource(R.drawable.jianbian_pink);
                    break;
                case 3:
                    ((MyViewHolder) holder).bg.setImageResource(R.drawable.jianbian_purple);
                    break;
                case 4:
                    ((MyViewHolder) holder).bg.setImageResource(R.drawable.jianbian_purple2);
                    break;
                case 5:
                    ((MyViewHolder) holder).bg.setImageResource(R.drawable.jianbian_orange);
                    break;
                default:
                    ((MyViewHolder) holder).bg.setImageResource(R.drawable.jianbian_green);
                    break;
            }
            StoreItemAdapter itemAdapter = new StoreItemAdapter(context);
            itemAdapter.setOnItemClickListener(this);
            ((MyViewHolder)holder).mRecyclerView.setAdapter(itemAdapter);
            itemAdapter.refresh(bean.getData());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delete(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void addAll(Collection<? extends Object> collection){
        int size = list.size();
        list.addAll(collection);
        notifyItemRangeInserted(size, collection.size());
    }

    public void refresh(Collection<? extends Object> collection){
        list = new ArrayList<>();
        list.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onClick(View v) {
        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) v.getTag();
        int position = holder.getAdapterPosition();
        if(holder instanceof HeadViewHolder){
            if(v == ((HeadViewHolder) holder).apply){
                mOnItemClickListener.onClick(0,null);
            }else if(v == ((HeadViewHolder) holder).record){
                mOnItemClickListener.onClick(1,null);
            }else if(v == ((HeadViewHolder) holder).rule){
                mOnItemClickListener.onClick(2,null);
            }else if(v == ((HeadViewHolder) holder).share){
                mOnItemClickListener.onClick(3,null);
            }else if(v == ((HeadViewHolder) holder).qrcode){
                mOnItemClickListener.onClick(4,null);
            }
        }else {
            if(v == ((MyViewHolder) holder).toAll){
                mOnItemClickListener.onClick(5,list.get(position));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0?1:2;
    }

    @Override
    public void onClick(Object bean) {
        Intent intent = new Intent(context, WedPrizeInfoActivity.class);
        intent.putExtra("prizeId",((GoodsItemBean)bean).getCommodityId());
        context.startActivity(intent);
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder{

        private TextView apply;
        private TextView share;
        private LinearLayout record;
        private LinearLayout rule;
        private LinearLayout qrcode;

        public HeadViewHolder(View itemView) {
            super(itemView);
            apply = (TextView) itemView.findViewById(R.id.apply);
            share = (TextView) itemView.findViewById(R.id.share);
            record = (LinearLayout) itemView.findViewById(R.id.record);
            rule = (LinearLayout) itemView.findViewById(R.id.rule);
            qrcode = (LinearLayout) itemView.findViewById(R.id.qr_code);
        }
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView info;
        private TextView toAll;
        private ImageView bg;
        private RecyclerView mRecyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            info = (TextView) itemView.findViewById(R.id.info);
            toAll = (TextView) itemView.findViewById(R.id.all);
            bg = (ImageView) itemView.findViewById(R.id.bg_img);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.list);
        }
    }
    //
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(int type, Object bean);
    }
}