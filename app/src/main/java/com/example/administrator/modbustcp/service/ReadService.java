package com.example.administrator.modbustcp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.modbustcp.Interface.ResultListener;
import com.example.administrator.modbustcp.fragment.Fragment_Setting;
import com.example.administrator.modbustcp.modbus.Coil_Status;
import com.example.administrator.modbustcp.modbus.Holding_Register;
import com.example.administrator.modbustcp.modbus.Input_Registers;
import com.example.administrator.modbustcp.modbus.Input_Status;
import com.example.administrator.modbustcp.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DoBest on 2016/3/31.
 */
public class ReadService extends Service{

    private String TAG = ReadService.class.getName();
    private ServiceBinder mBinder = new ServiceBinder();
    private int type = Fragment_Setting.getFunction_type();
    private String ip = Fragment_Setting.getRead_ip_value();
    private int port = Fragment_Setting.getRead_port_value();
    private int slaveId = Fragment_Setting.getRead_slaveId_value();
    private int start = Fragment_Setting.getRead_start_value();
    private int length = Fragment_Setting.getRead_length_value();
    private Timer timer = new Timer();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public class ServiceBinder extends Binder{
        public void setData(final ResultListener resultListener){
//            requestDate(resultListener);
//            Coil_Status.coilStatusRead("192.168.1.158", 502, 1, 100, 10, resultListener);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    requestDate(resultListener);
                }
            }, 0, 1000); //每隔1秒
            ToastUtil.showToast(ReadService.this, "开始连接", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public boolean stopService(Intent name) {
        Log.e(TAG,"stopService");
        return super.stopService(name);
    }

    private void requestDate(ResultListener resultListener){
        switch (type){
            case 1:
                Coil_Status.coilStatusRead(ip, port, slaveId, start, length, resultListener);
                Log.e("TAG", ip + port + slaveId + start + length+type);
                break;
            case 2:
                Input_Status.inputStatusRead(ip, port, slaveId, start, length, resultListener);
                Log.e("TAG", ip + port + slaveId + start + length+type);
                break;
            case 3:
                Holding_Register.holdingRegisterRead(ip, port, slaveId, start, length, resultListener);
                Log.e("TAG", ip + port + slaveId + start + length+type);
                break;
            case 4:
                Input_Registers.inputRegisterRead(ip, port, slaveId, start, length, resultListener);
                Log.e("TAG", ip + port + slaveId + start + length+type);
                break;
        }
    }
    private void stopTimer() {
//        timer.cancel();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopTimer();
        Log.e("TAG", "停止定时器");
        Toast.makeText(this, "断开连接", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }
}
