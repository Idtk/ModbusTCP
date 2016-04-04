package com.example.administrator.modbustcp.fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.modbustcp.Interface.ResultListener;
import com.example.administrator.modbustcp.R;
import com.example.administrator.modbustcp.model.DataModel;
import com.example.administrator.modbustcp.service.ReadService;
import com.example.administrator.modbustcp.utils.CustomDialog;
import com.example.administrator.modbustcp.utils.CustomDialog1;
import com.example.administrator.modbustcp.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DongBang on 2016/3/31.
 */
public class ReadWriteFragment extends Fragment implements ResultListener,View.OnClickListener{

    private static boolean isTest;
    private static Context context;
    private RecyclerView recyclerView;
    private String TAG =ReadWriteFragment.this.getClass().getSimpleName();
    private View rootView;
    private Toolbar toolbar;
    private TextView textView;
    private Button buttonConnect;
    private Button buttonDisconnect;
    private ArrayList<DataModel> data = new ArrayList<>();
    private MyAdapter mAdapter = null;

    private String ip;
    private int port;
    private int slaveId;
    private int start;
    private int length;
    private int type;

    private int UPDATE = 1;
    private int flag = 0;
    private Intent intentService;
    private ReadService.ServiceBinder mServiceBinder = null;
    private ServiceConnection readConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceBinder = (ReadService.ServiceBinder)service;
            try{
                ((ReadService.ServiceBinder) service).setData(ReadWriteFragment.this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    public static ReadWriteFragment getInstance(boolean isTest ,Context context)
    {
         ReadWriteFragment instance=new ReadWriteFragment();
        instance.isTest=isTest;
        instance.context=context;
        return  instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_read,container,false);
        intentService = new Intent(getActivity(), ReadService.class);
//        getActivity().bindService(intentService, readConnection, Context.BIND_AUTO_CREATE);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Coil_Status.coilStatusRead("192.168.1.158", 502, 1, 100, 10, ReadWriteFragment.this);
            }
        }).start();*/
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_read);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.read_view_recycler);
        textView = (TextView) toolbar.findViewById(R.id.text_title);
        buttonConnect = (Button) toolbar.findViewById(R.id.connect);
        buttonDisconnect = (Button) toolbar.findViewById(R.id.disconnect);
        initToolbar();
        initRecyclerView(recyclerView);
//        setStartService();
        return rootView;
    }

    private void initToolbar(){
        textView.setText("读取数据");
        buttonConnect.setOnClickListener(this);
        buttonDisconnect.setOnClickListener(this);
    }
    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true); // 设置固定大小
        initRecyclerLayoutManager(recyclerView); // 初始化布局
        initRecyclerAdapter(recyclerView); // 初始化适配器
    }

    private void initRecyclerAdapter(RecyclerView recyclerView0)
    {
        mAdapter = new MyAdapter(data);
        recyclerView.setAdapter(mAdapter);
    }


    private void initRecyclerLayoutManager(RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private List<DataModel> mDataModels;
        private List<Integer> mHeights;

        MyAdapter(List<DataModel> dataModels) {
            if (dataModels == null) {
                throw new IllegalArgumentException("DataModel must not be null");
            }
            mDataModels = dataModels;
            mHeights = new ArrayList<>();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_recycler_view, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            DataModel dataModel = mDataModels.get(position);
            if (holder.mTvValue==null)
            {
                LogUtils.d(TAG,"onBindViewHolder mTvAlise 为空");
            }
            holder.mTvAlise.setText(dataModel.getAlise());
            holder.mTvValue.setText(dataModel.getValue()+"");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TAG5", position + "");
                    if (position>0){
                        if (type==3){
                            CustomDialog dialog = new  CustomDialog(getActivity(),R.style.dialog,ip,port,
                                    slaveId, start+position-1,holder.mTvValue.getText().toString());
                            dialog.show();
                        }else if (type ==1){
                            CustomDialog1 dialog1 = new  CustomDialog1(getActivity(),R.style.dialog,ip,port,
                                    slaveId, start+position-1,holder.mTvValue.getText().toString());
                            dialog1.show();
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataModels.size();
        }

        public void removeData(int position) {
            mDataModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * ViewHolder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvAlise; // 别名
        private TextView mTvValue; //值

        public MyViewHolder(View itemView) {
            super(itemView);
            mTvAlise = (TextView) itemView.findViewById(R.id.item_alise);
            mTvValue = (TextView) itemView.findViewById(R.id.item_value);
        }

        public TextView getTvAlise() {
            return mTvAlise;
        }

        public TextView getTvValue() {
            return mTvValue;
        }

    }

    @Override
    public ArrayList<String> result(ArrayList<String> dataList,String ip, int port, int slaveId,
                                    int start, int type) {
        Log.e("回调", dataList.toString());
        this.ip = ip;
        this.port = port;
        this.slaveId = slaveId;
        this.start = start;
        this.type = type;
        data.clear();
        DataModel modelTitle = new DataModel();
        modelTitle.setAlise("地址/Addr");
        modelTitle.setValue("值/Value");
        data.add(modelTitle);
        for (int i = 0; i < dataList.size(); i++) {
            DataModel model = new DataModel();
            int count = start + i;
            model.setAlise(count + "");
            model.setValue(dataList.get(i));
            data.add(model);
        }
        //给handler发送消息
        Message msg = new Message();
        msg.what = UPDATE;
        handler.sendMessage(msg);
        return dataList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.connect:
                Log.e("TAG","连接1");
                if (flag == 0){
                    flag = 1;
                    Log.e("TAG","连接2");
                    getActivity().bindService(intentService, readConnection, Context.BIND_AUTO_CREATE);
                }
                break;
            case R.id.disconnect:
                Log.e("TAG","断开1");
                if (flag == 1) {
                    flag = 0;
                    Log.e("TAG","断开2");
//                    getActivity().stopService(intentService);
                    getActivity().unbindService(readConnection);
                    /*try {
                        getActivity().unbindService(readConnection);
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/
                }
                break;


        }
    }

    @Override
    public void onDestroy() {
        if (flag == 1){
            flag = 0;
            getActivity().unbindService(readConnection);
        }
        /*try {
            getActivity().unbindService(readConnection);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        super.onDestroy();
    }
}
