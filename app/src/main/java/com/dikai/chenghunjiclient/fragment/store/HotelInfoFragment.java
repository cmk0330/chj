package com.dikai.chenghunjiclient.fragment.store;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dikai.chenghunjiclient.R;
import com.dikai.chenghunjiclient.adapter.store.HotelPicAdapter;
import com.dikai.chenghunjiclient.bean.BeanGetCase;
import com.dikai.chenghunjiclient.entity.ResultGetCase;
import com.dikai.chenghunjiclient.entity.ResultNewSupInfo;
import com.dikai.chenghunjiclient.util.Constants;
import com.dikai.chenghunjiclient.util.EventBusBean;
import com.dikai.chenghunjiclient.util.NetWorkUtil;
import com.dikai.chenghunjiclient.view.MyLoadRecyclerView;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotelInfoFragment extends Fragment {

    private String supID;
    private MyLoadRecyclerView mRecycler;
    private HotelPicAdapter mAdater;

    public HotelInfoFragment() {
        // Required empty public constructor
    }

    public static HotelInfoFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id",id);
        HotelInfoFragment fragment = new HotelInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hotel_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        supID = getArguments().getString("id");
//        intro = (TextView) view.findViewById(R.id.fragment_hotel_info_intro);
//        address = (TextView) view.findViewById(R.id.fragment_hotel_info_address);
//        num = (TextView) view.findViewById(R.id.fragment_hotel_info_num);
        mRecycler = (MyLoadRecyclerView) view.findViewById(R.id.fragment_hotel_info_recycler);
        mRecycler.setLoadChangeListener(new MyLoadRecyclerView.OnLoadChangeListener() {
            @Override
            public void onRefresh() {
                getCase();
            }

            @Override
            public void onLoadMore() {}
        });
        mAdater = new HotelPicAdapter(getContext());
        mAdater.setSupId(supID);
        mRecycler.setAdapter(mAdater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCase();
    }

    /**
     * 获取案例
     */
    private void getCase(){
        NetWorkUtil.setCallback("HQOAApi/CaseInfoInfoList",
                new BeanGetCase(supID),
                new NetWorkUtil.CallBackListener() {
                    @Override
                    public void onFinish(final String respose) {
                        mRecycler.stopLoad();
                        Log.e("返回值",respose);
                        try {
                            ResultGetCase result = new Gson().fromJson(respose, ResultGetCase.class);
                            if ("200".equals(result.getMessage().getCode())) {
                                mAdater.refresh(result.getData());
                                if(result.getData().size()>0){
                                    mRecycler.setHasData(true);
                                }else {
                                    mRecycler.setHasData(false);
                                }
                            } else {
                                Toast.makeText(getContext(), result.getMessage().getInform(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("json解析出错",e.toString());
                            //Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(final String e) {
                        Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//
//    /**
//     * 事件总线刷新
//     * @param bean
//     */
//    @Subscribe
//    public void onEventMainThread(final EventBusBean bean) {
//        getActivity().runOnUiThread(new Runnable() {
//            public void run() {
//                if(bean.getType() == Constants.NEW_SUP_INFO){
//                    setData(bean.getNewSupInfo());
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }

}
