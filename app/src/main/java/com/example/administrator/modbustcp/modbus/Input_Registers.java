package com.example.administrator.modbustcp.modbus;

import android.util.Log;

import com.example.administrator.modbustcp.Interface.ResultListener;
import com.example.administrator.modbustcp.utils.ByteHex;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.util.queue.ByteQueue;

import java.util.Arrays;

/**
 * Created by DoBest on 2016/3/28.
 */
public class Input_Registers {

    private static int countRead = 0;
    public static void inputRegisterRead(String ip, int port,int slaveId, int start,int len, ResultListener resultListener){
        IpParameters params = new IpParameters();
        params.setHost(ip);//地址
        params.setPort(port);//端口
        ModbusFactory factory = new ModbusFactory();
        ModbusMaster master = factory.createTcpMaster(params, true);

        // 初始化
        try {
            master.init();
            readInputRegisters(master, ip, port, slaveId, start, len, resultListener);
        } catch (ModbusInitException e) {
            e.printStackTrace();
            if (countRead<3){
                countRead++;
                inputRegisterRead(ip, port, slaveId, start, len, resultListener);
            }else {
                Log.e("TAG","请检查设置后重新连接");
                resultListener.onToast("请检查设置后重新连接");
            }
        } finally {
            master.destroy();
        }
    }

    private static void readInputRegisters(ModbusMaster master, String ip, int port, int slaveId, int start, int len,
                                           ResultListener resultListener) {
        try {
            ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, start, len);
            ReadInputRegistersResponse response = (ReadInputRegistersResponse) master.send(request);
            if (response.isException()) {
                Log.e("TAG1", "Exception response: message=" + response.getExceptionMessage());
                resultListener.onToast("Exception response: message=" + response.getExceptionMessage());
            } else {
                ByteQueue byteQueue= new ByteQueue(12);
                response.write(byteQueue);
                resultListener.result(ByteHex.allDataR(byteQueue),ip, port, slaveId, start, 4);
                Log.e("TAG2", byteQueue + " Size: "+byteQueue.size());
                Log.e("TAG3", Arrays.toString(response.getShortData()));
                short[] list = response.getShortData();
                for(int i = 0; i < list.length; i++){
                    Log.e("TAG4", list[i]+"");
                }
            }
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            if (countRead<3){
                countRead++;
                readInputRegisters(master, ip, port, slaveId, start, len, resultListener);
            }else {
                Log.e("TAG","请检查设置后重新连接");
                resultListener.onToast("请检查设置后重新连接");
            }
        }finally {
            master.destroy();
        }
    }
}
