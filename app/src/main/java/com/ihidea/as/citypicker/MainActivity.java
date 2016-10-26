package com.ihidea.as.citypicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lljjcoder.citypickerview.widget.CalendarPicker;
import com.lljjcoder.citypickerview.widget.SinglePicker;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button go = (Button) findViewById(R.id.go);
        final TextView tvResult = (TextView) findViewById(R.id.tv_result);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CalendarPicker.Builder(MainActivity.this)
                        .setTextView((TextView) v)
                        .build()
                        .show();

//                String[] datas = getResources().getStringArray(R.array.content_data);
//                SinglePicker singlePicker = new SinglePicker.Builder(MainActivity.this)
//                        .initDatas(datas)
//                        .textSize(20)
//                        .build();
//                singlePicker.show();



//                CityPicker cityPicker = new CityPicker.Builder(MainActivity.this).textSize(20)
//                        .onlyShowProvinceAndCity(false)
//                        .confirTextColor("#000000")
//                        .cancelTextColor("#000000")
//                        .province("江苏省")
//                        .city("常州市")
//                        .district("新北区")
//                        .textColor(Color.parseColor("#000000"))
//                        .provinceCyclic(true)
//                        .cityCyclic(false)
//                        .districtCyclic(false)
//                        .visibleItemsCount(7)
//                        .itemPadding(10)
//                        .build();
//
//                cityPicker.show();

            }
        });
    }
}
