package com.example.administrator.modbustcp.Interface;

import java.util.ArrayList;

/**
 * Created by DoBest on 2016/4/1.
 */
public interface ResultListener {
    public ArrayList<String> result(ArrayList<String> dataList,String ip, int port, int slaveId,
                                    int start, int type);
}
