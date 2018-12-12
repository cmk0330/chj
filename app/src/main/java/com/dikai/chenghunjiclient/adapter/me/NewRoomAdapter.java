package com.dikai.chenghunjiclient.adapter.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dikai.chenghunjiclient.R;
import com.dikai.chenghunjiclient.entity.RoomBean;
import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Lucio on 2018/11/19.
 */

public class NewRoomAdapter  extends RecyclerView.Adapter<NewRoomAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<RoomBean> list;

    public NewRoomAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new_room_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.mLayout.setTag(holder);
        holder.mLayout.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RoomBean bean = list.get(position);
        holder.name.setText(bean.getBanquetHallName());
        holder.area.setText("面积  "+bean.getAcreage()+"㎡");
        holder.height.setText("层高  "+bean.getHeight()+"m");
        holder.tableNum.setText("最多容纳"+bean.getMaxTableCount()+"桌");
        Glide.with(context).load(bean.getHotelLogo()).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void delete(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void addAll(Collection<? extends RoomBean> collection){
        int size = list.size();
        list.addAll(collection);
        notifyItemRangeInserted(size, collection.size());
    }

    public void refresh(Collection<? extends RoomBean> collection){
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
        MyViewHolder holder = (MyViewHolder) v.getTag();
        int position = holder.getAdapterPosition();
        mOnItemClickListener.onClick(position, list.get(position));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView area;
        private TextView height;
        private TextView tableNum;
        private SelectableRoundedImageView pic;
        private LinearLayout mLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            area = (TextView) itemView.findViewById(R.id.area);
            height = (TextView) itemView.findViewById(R.id.height);
            tableNum = (TextView) itemView.findViewById(R.id.table_num);
            pic = (SelectableRoundedImageView) itemView.findViewById(R.id.img);
            mLayout = (LinearLayout) itemView.findViewById(R.id.bottom_layout);
        }
    }
    //
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(int position, RoomBean bean);
    }
}