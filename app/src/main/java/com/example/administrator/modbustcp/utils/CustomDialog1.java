package com.example.administrator.modbustcp.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.modbustcp.R;
import com.example.administrator.modbustcp.modbus.Coil_Status;

/**
 * Created by Administrator on 2016/4/4.
 */
public class CustomDialog1 extends Dialog {
    private Context context;
    private String ip;
    private int port;
    private int slaveId;
    private int start;
    private String value;
    private boolean mBoolean;

    public CustomDialog1(Context context, String ip, int port, int slaveId, int start, String value) {
        super(context);
        this.context = context;
        this.ip = ip;
        this.port = port;
        this.slaveId = slaveId;
        this.start = start;
        this.value = value;
        values(value);
    }

    public CustomDialog1(Context context, int themeResId, String ip, int port, int slaveId, int start, String value) {
        super(context, themeResId);
        this.context = context;
        this.context = context;
        this.ip = ip;
        this.port = port;
        this.slaveId = slaveId;
        this.start = start;
        this.value = value;
        values(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = LayoutInflater.from(context).inflate(R.layout.custom_dialog1, null);

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

        RadioGroup write_value = (RadioGroup)layout.findViewById(R.id.write_value_rg);
        RadioButton write_value_on = (RadioButton) write_value.findViewById(R.id.on_button);
        RadioButton write_value_off = (RadioButton) write_value.findViewById(R.id.off_button);

        Button write_button = (Button) layout.findViewById(R.id.write_button);


        write_ip_text.setText("IP地址/IP");
        write_ip_edit.setText(ip);

        write_port_text.setText("端口号/Port");
        write_port_edit.setText(""+port);

        write_slaveId_text.setText("客户端ID/SlaveId");
        write_slaveId_edit.setText(""+slaveId);

        write_start_text.setText("设备地址/Addr");
        write_start_edit.setText("" + start);

        if (mBoolean){
            write_value_on.setChecked(true);
        }else {
            write_value_off.setChecked(true);
        }

        write_value.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
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

        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String ipW = write_ip_edit.getText().toString();
                    final int portW = Integer.parseInt(write_port_edit.getText().toString());
                    final int slavedIdW = Integer.parseInt(write_slaveId_edit.getText().toString());
                    final int startW = Integer.parseInt(write_start_edit.getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Coil_Status.coilStatusWrite(ipW, portW, slavedIdW, startW, mBoolean);
                        }
                    }).start();
                }
                catch(Exception e) {
                    Toast.makeText(context,"参数错误",Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.setContentView(layout);
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
}
