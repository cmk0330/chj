package com.dikai.chenghunjiclient.entity;

import java.util.List;

/**
 * Created by Lucio on 2017/9/22.
 */

public class ResultAssignCarList {
    private ResultCode Message;
    private List<AssignCarBean> Data;

    public ResultCode getMessage() {
        return Message;
    }

    public List<AssignCarBean> getData() {
        return Data;
    }
}
