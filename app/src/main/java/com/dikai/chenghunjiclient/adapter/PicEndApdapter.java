package com.dikai.chenghunjiclient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dikai.chenghunjiclient.R;
import com.dikai.chenghunjiclient.entity.HotelAdEndImg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Lucio on 2018/9/21.
 */

public class PicEndApdapter  extends RecyclerView.Adapter<PicEndApdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<HotelAdEndImg> list;

    public PicEndApdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.caigoujie_pic_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
//        holder.mLayout.setTag(holder);
//        holder.mLayout.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HotelAdEndImg bean = list.get(position);
        Glide.with(context).load(bean.getEndImgUrl()).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delete(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void addAll(Collection<? extends HotelAdEndImg> collection){
        int size = list.size();
        list.addAll(collection);
        notifyItemRangeInserted(size, collection.size());
    }

    public void refresh(Collection<? extends HotelAdEndImg> collection){
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
//        MyViewHolder holder = (MyViewHolder) v.getTag();
//        int position = holder.getAdapterPosition();
//        mOnItemClickListener.onClick(list.get(position));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView pic;

        public MyViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.pic);
        }
    }
    //
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(String bean);
    }
}