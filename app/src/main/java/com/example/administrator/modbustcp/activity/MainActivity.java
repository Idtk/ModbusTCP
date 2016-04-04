package com.example.administrator.modbustcp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.modbustcp.R;
import com.example.administrator.modbustcp.fragment.Fragment_Setting;
import com.example.administrator.modbustcp.fragment.ReadWriteFragment;
import com.example.administrator.modbustcp.utils.StatusBarCompat;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Fragment_Setting fragment_setting;
    private ReadWriteFragment readWriteFragment;
    private String[] bottomTitles = {"设置","读写","关于"};
    private int[] bottomDrawable = {R.drawable.setting_tab, R.drawable.read_tab,
            R.drawable.attribute_tab};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置状态栏颜色
        StatusBarCompat.compat(this, getResources().getColor(R.color.white));
        initView();
    }

    private void initView(){
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);

        View view1= LayoutInflater.from(this).inflate(R.layout.tab_item,null);
        ImageView imageView1 = (ImageView) view1.findViewById(R.id.tab_imageView);
        TextView textView1 = (TextView) view1.findViewById(R.id.tab_textView);
        imageView1.setImageResource(bottomDrawable[0]);
        textView1.setText(bottomTitles[0]);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view1), 0);

        View view2= LayoutInflater.from(this).inflate(R.layout.tab_item,null);
        ImageView imageView2 = (ImageView) view2.findViewById(R.id.tab_imageView);
        TextView textView2 = (TextView) view2.findViewById(R.id.tab_textView);
        imageView2.setImageResource(bottomDrawable[1]);
        textView2.setText(bottomTitles[1]);
        tabLayout.addTab(tabLayout.newTab().setCustomView(view2), 1);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                hideFragment(fragmentTransaction);
                switch (tab.getPosition()) {
                    case 0:
                        if (fragment_setting == null) {
                            fragment_setting = new Fragment_Setting();
                            fragmentTransaction.add(R.id.main_fragment, fragment_setting);
                        } else {
                            fragmentTransaction.show(fragment_setting);
                        }
                        break;
                    case 1:
                        if (readWriteFragment==null){
                            readWriteFragment= ReadWriteFragment.getInstance(true,getApplicationContext());
                            fragmentTransaction.add(R.id.main_fragment,readWriteFragment);
                        }else
                        {
                            fragmentTransaction.show(readWriteFragment);
                        }
                        break;
                }
                fragmentTransaction.commit();//提交事务
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).select();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction){

        if (fragment_setting != null){
            fragmentTransaction.hide(fragment_setting);
        }
        if (readWriteFragment != null){
            fragmentTransaction.hide(readWriteFragment);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
