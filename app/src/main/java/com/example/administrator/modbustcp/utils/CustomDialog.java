package com.example.administrator.modbustcp.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.modbustcp.Interface.ResultListener;
import com.example.administrator.modbustcp.R;
import com.example.administrator.modbustcp.modbus.Coil_Status;
import com.example.administrator.modbustcp.modbus.Holding_Register;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/4.
 */
public class CustomDialog extends Dialog implements ResultListener {
    private Context context;
    private String ip;
    private int port;
    private int slaveId;
    private int start;
    private String value;
    private boolean mBoolean;
    private int type;
    private int TOAST = 2;

    static class MyHandler extends Handler{
        CustomDialog handlerCustomDialog;
        Context context;
        MyHandler(CustomDialog customDialog,Context context){
            handlerCustomDialog = customDialog;
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (handlerCustomDialog != null){
                switch (msg.what){
                    case 2:
                        ToastUtil.showToast(context, msg.obj.toString(), Toast.LENGTH_SHORT);
                        break;
                }
            }
        }
    }

    MyHandler handler = new MyHandler(CustomDialog.this,context);

    public CustomDialog(Context context,String ip, int port, int slaveId, int start, String value, int type) {
        super(context);
        this.context = context;
        this.ip = ip;
        this.port = port;
        this.slaveId = slaveId;
        this.start = start;
        this.type = type;
        this.value = value;
        valueType(type,value);
    }

    public CustomDialog(Context context, int themeResId, String ip, int port, int slaveId, int start, String value,int type) {
        super(context, themeResId);
        this.context = context;
        this.context = context;
        this.ip = ip;
        this.port = port;
        this.slaveId = slaveId;
        this.start = start;
        this.type = type;
        this.value = value;
        valueType(type,value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);
        //使用下面屏蔽的引入父视图的方式，可使用addView(view)方法
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.custom_dialog, null);

        LinearLayout write_ip = (LinearLayout)layout.findViewById(R.id.write_ip);
        TextView write_ip_text = (TextView)write_ip.findViewById(R.id.textView);
        final EditText write_ip_edit = (EditText)write_ip.findViewById(R.id.editText);

        LinearLayout write_port = (LinearLayout)layout.findViewById(R.id.write_port);
        TextView write_port_text = (TextView)write_port.findViewById(R.id.textView);
        final EditText write_port_edit = (EditText)write_port.findViewById(R.id.editText);

        LinearLayout write_slaveId = (LinearLayout)layout.findViewById(R.id.write_slaveId);
        TextView write_slaveId_text = (TextView)write_slaveId.findViewById(R.id.textView);
        final EditText write_slaveId_edit = (EditText)write_slaveId.findViewById(R.id.editText);

        LinearLayout write_start = (LinearLayout)layout.findViewById(R.id.write_start);
        TextView write_start_text = (TextView)write_start.findViewById(R.id.textView);
        final EditText write_start_edit = (EditText)write_start.findViewById(R.id.editText);

        write_ip_text.setText("IP地址/IP");
        write_ip_edit.setText(ip);

        write_port_text.setText("端口号/Port");
        write_port_edit.setText(""+port);

        write_slaveId_text.setText("客户端ID/SlaveId");
        write_slaveId_edit.setText(""+slaveId);

        write_start_text.setText("设备地址/Addr");
        write_start_edit.setText(""+start);


        //动态布局部分
        LinearLayout write_value = (LinearLayout) getLayoutInflater().inflate(R.layout.edit_item, null);
        TextView write_value_text = (TextView)write_value.findViewById(R.id.textView);
        final EditText write_value_edit = (EditText)write_value.findViewById(R.id.editText);
        write_value_text.setText("发送的值/Bytes");
        write_value_edit.setText(value);

        Button write_button = new Button(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 192);
        write_button.setLayoutParams(params);
        write_button.setPadding(16, 8, 16, 8);
        write_button.setGravity(Gravity.CENTER);
        write_button.setTextSize(20);
        write_button.setText("发送");

        RadioGroup write_value2 = (RadioGroup)getLayoutInflater().inflate(R.layout.radio_group_item,null);
        RadioButton write_value_on = (RadioButton) write_value2.findViewById(R.id.on_button);
        RadioButton write_value_off = (RadioButton) write_value2.findViewById(R.id.off_button);
        if (mBoolean){
            write_value_on.setChecked(true);
        }else {
            write_value_off.setChecked(true);
        }

        write_value2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.off_button:
                        mBoolean = false;
                        break;
                    case R.id.on_button:
                        mBoolean = true;
                        break;
                }
                Log.e("TAG5", checkedId + ":" + group);
            }
        });

        switch (type){
            case 1:
                layout.addView(write_value2,params);
                break;
            case 3:
                layout.addView(write_value, params);
        }
        layout.addView(write_button, params);

        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String ipW = write_ip_edit.getText().toString();
                    final int portW = Integer.parseInt(write_port_edit.getText().toString());
                    final int slavedIdW = Integer.parseInt(write_slaveId_edit.getText().toString());
                    final int startW = Integer.parseInt(write_start_edit.getText().toString());
                    final int valueW = Integer.parseInt(write_value_edit.getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            switch (type){
                                case 1:
                                    Coil_Status.coilStatusWrite(ipW, portW, slavedIdW, startW, mBoolean,CustomDialog.this);
                                    break;
                                case 3:
                                    Holding_Register.holdingRegisterWrite(ipW, portW, slavedIdW, startW, valueW, CustomDialog.this);
                            }
                        }
                    }).start();
                }
                catch(Exception e) {
                    ToastUtil.showToast(context, "参数错误", Toast.LENGTH_SHORT);
                }
            }
        });

        this.setContentView(layout);
    }

    private void valueType(int type,String value){
        switch (type){
            case 1:
                values(value);
                break;
            case 3:
                this.value = value;
        }
    }
    private void values(String value){
        switch (Integer.parseInt(value)){
            case 1:
                mBoolean = true;
                break;
            case 0:
                mBoolean = false;
        }
    }

    @Override
    public ArrayList<String> result(ArrayList<String> dataList, String ip, int port, int slaveId, int start, int type) {
        return null;
    }

    @Override
    public void onToast(String string) {
        //给handler发送消息
        Message msg = new Message();
        msg.what = TOAST;
        msg.obj = string;
        handler.sendMessage(msg);
    }
}
