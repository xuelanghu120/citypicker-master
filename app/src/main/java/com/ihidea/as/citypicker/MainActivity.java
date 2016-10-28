package com.ihidea.as.citypicker;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.lljjcoder.citypickerview.widget.CityPicker;




public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        WheelView mWheelView = (WheelView) findViewById(R.id.id_district_ll);
        final String[] datas = getResources().getStringArray(R.array.content_data);
//        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(MainActivity.this, datas);
//        mWheelView.setViewAdapter(arrayWheelAdapter);
//        //获取所设置的省的位置，直接定位到该位置
//
//        arrayWheelAdapter.setTextColor(Color.parseColor("#000000"));




        Button go = (Button) findViewById(R.id.go);
        final TextView tvResult = (TextView) findViewById(R.id.tv_result);
        go.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

//                new CalendarPicker.Builder(MainActivity.this)
//                        .setTextView((TextView) v)
//                        .build()
//                        .show();

//                String[] datas = getResources().getStringArray(R.array.content_data);
//                SinglePicker singlePicker = new SinglePicker.Builder(MainActivity.this)
//                        .initDatas(datas)
//                        .textColor(Color.parseColor("#0000ff"))
//                        .cyclicForever(false)
//                        .itemPadding(10)
//                        .textSize(20)
//                        .build();
//                singlePicker.show();



                CityPicker cityPicker = new CityPicker.Builder(MainActivity.this).textSize(20)
                        .showDistrict(true)       //是否显示第三级别
                        .showCity(true)          //是否显示第二级别
                        .initProvinceDatas(datas) //初始化第一级别数据
                        .initcityeDatas(datas)    //初始化第二级别数据
                        .initDistrictDatas(datas)
                        .isRelation(false)           //是否关联转动
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province("江苏省")          //设置第一级别默认值
                        .city("常州市")              //设置第二级别默认值
                        .district("新北区")            //设置第三级别默认值
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(false)          //是否循环转动
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)           //可见个数
                        .itemPadding(20)
                        .setTextView(tvResult)          //设置textview，确定后自动显示数值
                        .build();

                cityPicker.show();

            }
        });
    }
}
