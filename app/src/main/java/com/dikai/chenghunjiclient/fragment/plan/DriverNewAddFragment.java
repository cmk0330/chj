package com.dikai.chenghunjiclient.fragment.plan;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dikai.chenghunjiclient.R;
import com.dikai.chenghunjiclient.adapter.plan.CarNewAddAdapter;
import com.dikai.chenghunjiclient.adapter.plan.NewAddAdapter;
import com.dikai.chenghunjiclient.bean.BeanGetCarNewAdd;
import com.dikai.chenghunjiclient.bean.BeanGetSupOrder;
import com.dikai.chenghunjiclient.bean.BeanOrderAgree;
import com.dikai.chenghunjiclient.bean.CarNewAddAgree;
import com.dikai.chenghunjiclient.entity.CarNewAddBean;
import com.dikai.chenghunjiclient.entity.GetSupOrderBean;
import com.dikai.chenghunjiclient.entity.ResultCarNewAdd;
import com.dikai.chenghunjiclient.entity.ResultGetSupOrder;
import com.dikai.chenghunjiclient.entity.UserInfo;
import com.dikai.chenghunjiclient.util.NetWorkUtil;
import com.dikai.chenghunjiclient.util.UserManager;
import com.dikai.chenghunjiclient.view.MyLoadRecyclerView;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverNewAddFragment extends Fragment {

    private static final int CALL_REQUEST_CODE = 120;
    private MyLoadRecyclerView mRecyclerView;
    private CarNewAddAdapter mAdapter;
    private SpotsDialog mDialog;
    private Intent intent;

    public DriverNewAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_new_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDialog = new SpotsDialog(getContext(), R.style.DialogCustom);
        mRecyclerView = (MyLoadRecyclerView)view.findViewById(R.id.new_add_recycler);
        mAdapter = new CarNewAddAdapter(getContext());
        mAdapter.setCallClickListener(new CarNewAddAdapter.OnCallClickListener() {
            @Override
            public void onClick(int position, boolean isCall, boolean isAgree, CarNewAddBean bean) {
                if(isCall){
                    intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getCaptainPhone().split(",")[1]));
                    request();
                }else {
                    if(isAgree){
                        agree(bean.getScheduleID(), "1");
                    }else {
                        agree(bean.getScheduleID(), "2");
                    }
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadChangeListener(new MyLoadRecyclerView.OnLoadChangeListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("DriverNewAddFragment"," ======== onDestroy()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }

    public void refresh() {
        getList();
    }

    /**
     * 同意拒绝
     */
    private void agree(String orderID, String type) {
        mDialog.show();
        UserInfo userInfo = UserManager.getInstance(getContext()).getUserInfo();
        NetWorkUtil.setCallback("User/UpScheduleAuditDriver",
                new CarNewAddAgree(userInfo.getUserID(), orderID, type),
                new NetWorkUtil.CallBackListener() {
                    @Override
                    public void onFinish(final String respose) {
                        mDialog.dismiss();
                        Log.e("返回值",respose);
                        try {
                            ResultGetSupOrder result = new Gson().fromJson(respose, ResultGetSupOrder.class);
                            if ("200".equals(result.getMessage().getCode())) {
                                Toast.makeText(getContext(), "操作成功！", Toast.LENGTH_SHORT).show();
                                getList();
                            } else {
                                Toast.makeText(getContext(), "" + result.getMessage().getInform(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("json解析出错",e.toString());
                            //Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(final String e) {
                        mDialog.dismiss();
                        Log.e("网络出错",e.toString());
                    }
                });
    }

    /**
     * 获取新增
     */
    private void getList() {
        UserInfo userInfo = UserManager.getInstance(getContext()).getUserInfo();
        NetWorkUtil.setCallback("User/GetDriverTimetableListByDriverID",
                new BeanGetCarNewAdd(userInfo.getUserID(), "3","",""),
                new NetWorkUtil.CallBackListener() {
                    @Override
                    public void onFinish(final String respose) {
                        mRecyclerView.stopLoad();
                        Log.e("返回值",respose);
                        try {
                            ResultCarNewAdd result = new Gson().fromJson(respose, ResultCarNewAdd.class);
                            if ("200".equals(result.getMessage().getCode())) {
                                if(result.getData().size() == 0){
                                    String mark = "队长暂时没有给您做出安排\n" + "请耐心等待";
                                    mRecyclerView.setNoData(mark);
                                    mRecyclerView.setHasData(false);
                                }else {
                                    mRecyclerView.setHasData(true);
                                }
                                mAdapter.refresh(result.getData());
                            } else {
                                Toast.makeText(getContext(), "" + result.getMessage().getInform(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("json解析出错",e.toString());
                            //Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(final String e) {
                        mRecyclerView.stopLoad();
                        Log.e("网络出错",e.toString());
                    }
                });
    }

    private void request() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请CALL_PHONE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == CALL_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "已禁止", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
