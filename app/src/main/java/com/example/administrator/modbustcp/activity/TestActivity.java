package com.example.administrator.modbustcp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.administrator.modbustcp.Interface.ResultListener;
import com.example.administrator.modbustcp.R;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;
import com.serotonin.util.queue.ByteQueue;

import java.util.ArrayList;


public class TestActivity extends AppCompatActivity implements ResultListener {



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final short[] shorts = new short[2];
        shorts[0]=1;
        shorts[1]=2;
        final boolean[] values = new boolean[2];
        values[0]=true;
        values[1]=true;
//        modbusWTCP("192.168.1.200",502,1,100,shorts);
//        modbusRTCP("192.168.1.158",502, 100,10);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Coil_Status.coilStatusWrite("192.168.1.158", 502, 1, 100, values);
//                Coil_Status.coilStatusRead("192.168.1.158",502,1,100,10,TestActivity.this);
//                Input_Status.inputStatusRead("192.168.1.158", 502, 1, 100, 10);
//                Holding_Register.holdingRegisterRead("192.168.1.158", 502, 1, 100, 10);
//                Holding_Register.holdingRegisterWrite("192.168.1.158", 502, 1, 100, shorts);
//                Input_Registers.inputRegisterRead("192.168.1.158", 502, 1, 100, 10);
//                Log.e("TAG","===========================");
            }
        }).start();
    }



    public void modbusWTCP(String ip, int port, int slaveId, int start, short[] values) {
        ModbusFactory modbusFactory = new ModbusFactory();
        // 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502
        IpParameters params = new IpParameters();
        params.setHost(ip);
        if (502 != port) {
            params.setPort(port);
        }// 设置端口，默认502
        ModbusMaster tcpMaster = null;
        // 参数1：IP和端口信息 参数2：保持连接激活
        tcpMaster = modbusFactory.createTcpMaster(params, true);
        try {
            tcpMaster.init();
            System.out.println("===============" + 1111111);
        } catch (ModbusInitException e) {
             System.out.println("11111111111111==" + "此处出现问题了啊!");
//             如果出现了通信异常信息，则保存到数据库中
//            CommunityExceptionRecord cer = new CommunityExceptionRecord();
//            cer.setDate(new Date());
            //cer.setIp(ip);
            // cer.setRemark(bgName+"出现连接异常");
            // batteryGroupRecordService.saveCommunityException(cer);
        }
        try {
            WriteRegistersRequest request = new WriteRegistersRequest(slaveId, start, values);
            WriteRegistersResponse response = (WriteRegistersResponse) tcpMaster.send(request);
            if (response.isException())
                System.out.println("Exception response: message=" + response.getExceptionMessage());
            else
                System.out.println("Success");
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        finally {
            tcpMaster.destroy();
        }
    }

    public ByteQueue modbusRTCP(String ip, int port, int start,int readLenth) {
        ModbusFactory modbusFactory = new ModbusFactory();
        // 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502
        IpParameters params = new IpParameters();
        params.setHost(ip);
        if(502!=port){params.setPort(port);}//设置端口，默认502
        ModbusMaster tcpMaster = null;
        tcpMaster = modbusFactory.createTcpMaster(params, true);
        try {
            tcpMaster.init();
            System.out.println("==============="+1111111);
        } catch (ModbusInitException e) {
            Log.e("TAG",e.getMessage());
            return null;
        }
        ModbusRequest modbusRequest=null;
        try {
            modbusRequest = new ReadHoldingRegistersRequest(1, start, readLenth);//功能码03
        } catch (ModbusTransportException e) {
            Log.e("TAG","2");
            e.printStackTrace();
        }
        ModbusResponse modbusResponse=null;
        try {
            modbusResponse = tcpMaster.send(modbusRequest);
        } catch (ModbusTransportException e) {
            Log.e("TAG","3");
            e.printStackTrace();
        }
        ByteQueue byteQueue= new ByteQueue(12);
        modbusResponse.write(byteQueue);
        Log.e("TAG", "功能码:" + modbusRequest.getFunctionCode());
        Log.e("TAG","从站地址:"+modbusRequest.getSlaveId());
        Log.e("TAG","收到的响应信息大小:"+byteQueue.size());
        Log.e("TAG", "收到的响应信息值:" + byteQueue);
        Log.e("TAG", "收到的数值:" + modbusResponse.getExceptionCode());
        Log.e("TAG", "收到的数值:" + modbusResponse.getExceptionMessage());
        Log.e("TAG", "收到的数值:" + modbusResponse.getFunctionCode());
        Log.e("TAG", "收到的数值:" + byteQueue.pop());
        return byteQueue;
    }

    @Override
    public ArrayList<String> result(ArrayList<String> dataList, String ip, int port, int slaveId, int start, int type) {
        return null;
    }

    @Override
    public void onToast(String msg) {

    }
}
