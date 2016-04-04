package com.example.administrator.modbustcp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.modbustcp.R;


/**
 * Created by DoBest on 2016/3/30.
 */
public class Fragment_Setting extends Fragment {

    private View view;
    private Toolbar toolbar_setting;
    private TextView title_text;

    private LinearLayout read_ip;
    private TextView read_ip_text;
    private static EditText read_ip_edit;

    private LinearLayout read_port;
    private TextView read_port_text;
    private static EditText read_port_edit;

    private LinearLayout read_slaveId;
    private TextView read_slaveId_text;
    private static EditText read_slaveId_edit;

    private LinearLayout read_start;
    private TextView read_start_text;
    private static EditText read_start_edit;

    private LinearLayout read_length;
    private TextView read_length_text;
    private static EditText read_length_edit;

    private Spinner function_spinner;
//    private static TextView spinner_text;

    private static int type=3;
    private static final String[] spinner_string={"01 Coil Status [0x]","02 Input Status [1x]",
            "03 Holding Register [4x]","04 Input Registers [3x]"};
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initView(container);
        return view;
    }

    private void initView(ViewGroup container){
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_setting,container,false);

        toolbar_setting = (Toolbar)view.findViewById(R.id.toolbar_setting);
        title_text = (TextView)toolbar_setting.findViewById(R.id.text_title);

        read_ip = (LinearLayout)view.findViewById(R.id.read_ip);
        read_ip_text = (TextView)read_ip.findViewById(R.id.textView);
        read_ip_edit = (EditText)read_ip.findViewById(R.id.editText);

        read_port = (LinearLayout)view.findViewById(R.id.read_port);
        read_port_text = (TextView)read_port.findViewById(R.id.textView);
        read_port_edit = (EditText)read_port.findViewById(R.id.editText);

        read_slaveId = (LinearLayout)view.findViewById(R.id.read_slaveId);
        read_slaveId_text = (TextView)read_slaveId.findViewById(R.id.textView);
        read_slaveId_edit = (EditText)read_slaveId.findViewById(R.id.editText);

        read_start = (LinearLayout)view.findViewById(R.id.read_start);
        read_start_text = (TextView)read_start.findViewById(R.id.textView);
        read_start_edit = (EditText)read_start.findViewById(R.id.editText);

        read_length = (LinearLayout)view.findViewById(R.id.read_length);
        read_length_text = (TextView)read_length.findViewById(R.id.textView);
        read_length_edit = (EditText)read_length.findViewById(R.id.editText);

        function_spinner = (Spinner)view.findViewById(R.id.function_spinner);
//        spinner_text = (TextView)function_spinner.findViewById(R.id.spinner_text);

        settingView();
    }

    private void settingView(){

        title_text.setText("设置");

        read_ip_text.setText("IP地址/IP");
        read_ip_edit.setText("192.168.1.100");

        read_port_text.setText("端口号/Port");
        read_port_edit.setText("502");

        read_slaveId_text.setText("客户端ID/SlaveId");
        read_slaveId_edit.setText("1");

        read_start_text.setText("设备地址/Addr");
        read_start_edit.setText("100");

        read_length_text.setText("字节长度/Bytes");
        read_length_edit.setText("1");

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item,spinner_string);
        function_spinner.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        function_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position + 1;
//                Toast.makeText(getActivity(),type+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        function_spinner.setSelection(2);
    }

    public static String getRead_ip_value() {
        return read_ip_edit.getText().toString();
    }

    public static int getRead_port_value() {
        return Integer.parseInt(read_port_edit.getText().toString());
    }

    public static int getRead_slaveId_value(){
        return Integer.parseInt(read_slaveId_edit.getText().toString());
    }

    public static int getRead_start_value(){
        return Integer.parseInt(read_start_edit.getText().toString());
    }

    public static int getRead_length_value(){
        return Integer.parseInt(read_length_edit.getText().toString());
    }

    public static int getFunction_type(){
        return type;
    }
}
