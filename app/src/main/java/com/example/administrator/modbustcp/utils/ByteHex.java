package com.example.administrator.modbustcp.utils;

import com.serotonin.util.queue.ByteQueue;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2016/4/3.
 */
public class ByteHex {
    public static ArrayList<String> allData(ByteQueue byteQueue, int len){
        String data = new String();
        ArrayList<String> dataList = new ArrayList<String>();
        for (int i=3;i<byteQueue.size();i++){
            //ByteQueue转换为二进制字符串
            data = Integer.toBinaryString(byteQueue.peek(i)& 0xff);
            if (len-8*(i-3)<8){
                dataList=addData(dataList,backData2(data, len%8));
            }else {
                dataList=addData(dataList,backData(data));
            }
        }
        //倒序list
        Collections.reverse(dataList);
        return dataList;
    }

    private static ArrayList<String> backData(String data){
        ArrayList<String> dataList = new ArrayList<String>();
        for (int i=data.length();i<8;i++){
            data = 0+data;
        }
        for (int i=0;i<data.length();i++){
            dataList.add(data.charAt(i) + "");
        }
        return dataList;
    }

    private static ArrayList<String> backData2(String data,int length){
        ArrayList<String> dataList = new ArrayList<String>();
        for (int i=data.length();i<length;i++){
            data = 0+data;
        }
        for (int i=0;i<data.length();i++){
            dataList.add(data.charAt(i) + "");
        }
        return dataList;
    }

    private static ArrayList<String> addData(ArrayList<String> listB,ArrayList<String> listA){
        for (int i =0;i<listB.size();i++){
            listA.add(listB.get(i));
        }
        return listA;
    }

    public static ArrayList<String> allDataR(ByteQueue byteQueue){
        ArrayList<String> dataList = new ArrayList<String>();
        for (int i=4; i<byteQueue.size(); i=i+2){
            dataList.add(byteQueue.peek(i)+"");
        }
        return dataList;
    }
}
