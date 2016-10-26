## citypicker Android Studio实现

### 前言
在实际的项目中需要使用到省市区三级联动的功能，在网上找来找去，都没有找到一个合适的库， 所以自己就封装了一个，不需要自己添加数据源，直接引用即可，一行代码搞定城市选择。怎么简单，怎么方便，怎么来，就是这么任性！

### 亮点
无需自己配置省市区域的数据，不需要再进行解析之类的繁杂操作，只需引用即可，结果返回省市区和邮编等四项数据信息，如果不满意样式的话可以自己修改源码！

### 效果预览
#### 应用在实际项目中效果
![](http://img.blog.csdn.net/20160513153736550)

![](http://img.blog.csdn.net/20160513153748475)

![](http://img.blog.csdn.net/20160513153756003)



### APK下载

![这里写图片描述](http://img.blog.csdn.net/20161013143043278)

1. [fir下载apk演示](http://fir.im/r3dp)


----------
###  V0.4.0版本更新内容（2016.10.24）
#### 更新内容

 1. 新增只显示省市两级联动设置
 2. 新增确认和取消按钮文字颜色的设置
 3. 新增设置默认省市区的设置


#### gradle引用
```
    compile 'liji.library.dev:citypickerview:0.4.0'

```
#### 代码示例（v0.4.0）

```
 CityPicker cityPicker = new CityPicker.Builder(MainActivity.this).textSize(20) //滚轮文字的大小
                        .onlyShowProvinceAndCity(true) //显示省市两级联动
                        .confirTextColor("#000000") //设置确认按钮文字颜色
                        .cancelTextColor("#000000") //设置取消按钮文字颜色
                        .province("江苏省") //设置默认省
                        .city("常州市") //设置默认城市
                        .district("新北区") //设置默认地区（县）
                        .textColor(Color.parseColor("#000000")) //滚轮文字的颜色
                        .provinceCyclic(true) //省份滚轮是否循环显示
                        .cityCyclic(false) //城市滚轮是否循环显示
                        .districtCyclic(false) //地区（县）滚轮是否循环显示
                        .visibleItemsCount(7) //滚轮显示的item个数
                        .itemPadding(10) //滚轮item间距
                        .build();
                        
                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                       //省份
		               String province = citySelected[0];
		               //城市
		               String city = citySelected[1];
		               //区县（如果设定了两级联动，那么该项返回空）
		               String district = citySelected[2];
		               //邮编
		               String code = citySelected[3]; 
                    }
                });
```

#### 属性说明

> onlyShowProvinceAndCity(boolean flag)

 省市两级联动，boolean 类型，默认false，三级联动


>  confirTextColor

 确认按钮文字的颜色 ，int 类型，默认为#0000FF（BLUE）
 
 >  cancelTextColor

 取消按钮文字的颜色 ，int 类型，默认为#000000（BLACK）

>  province

 设置默认省 ，String类型，默认为江苏省

>  city

 设置默认市 ，String 类型，默认为常州市

>  district

 设置默认区（县），String 类型，默认为新北区

----------

###  V0.3.0版本更新内容（2016.10.13）
#### 更新内容

 1. 新增item间距属性
 2. 拆分省市区三个滚轮循环显示为分别是否循环滚动
 3. 优化代码结构

#### gradle引用
```
    compile 'liji.library.dev:citypickerview:0.3.0'

```
#### 代码示例（v0.3.0）

```
CityPicker cityPicker = new CityPicker.Builder(context).textSize(20)//滚轮文字的大小
                        .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                        .provinceCyclic(true)//省份滚轮是否循环显示
                        .cityCyclic(false)//城市滚轮是否循环显示
                        .districtCyclic(false)//地区（县）滚轮是否循环显示
                        .visibleItemsCount(7)//滚轮显示的item个数
                        .itemPadding(10)//滚轮item间距
                        .build();
                
                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                       //省份
		               String province = citySelected[0];
		               //城市
		               String city = citySelected[1];
		               //区县
		               String district = citySelected[2];
		               //邮编
		               String code = citySelected[3]; 
                    }
                });
```

#### 属性说明

 

> textSize

 滚轮文字的大小，int 类型，默认为18


>  textColor   

 滚轮文字的颜色 ，int 类型，默认为0xFF585858

> visibleItemsCount

滚轮显示的item个数，int 类型，默认为5个

>    provinceCyclic

省份滚轮是否循环显示，boolean 类型，默认为true

>    cityCyclic

城市滚轮是否循环显示，boolean 类型，默认为true

>    districtCyclic

地区（县）滚轮是否循环显示，boolean 类型，默认为true

> itemPadding

滚轮item间距，默认为5dp



#### 结果返回
只需传入Context便可获取选择的省市区域的信息，结果返回四项，可根据自己的实际需求进行选择。

 1. citySelected[0]：表示：省份信息
 2. citySelected[1]：表示：城市信息
 3. citySelected[2]：表示：区县信息
 4. citySelected[3]：表示：邮编信息


----------


###  V0.2.0版本更新内容（2016.05.16）
#### 更新内容
1. 滚轮是否循环滚动
2. 新增文字颜色修改
3. 新增文字大小修改
4. 新增滚轮内容可见数量

#### gradle引用
```
    compile 'liji.library.dev:citypickerview:0.2.0'

```
#### 代码示例（v0.2.0）
```
 CityPickerView cityPickerView = new CityPickerView(MainActivity.this);
        cityPickerView.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                 
            }
        });
        cityPickerView.setTextColor(Color.BLUE);//新增文字颜色修改
        cityPickerView.setTextSize(20);//新增文字大小修改
        cityPickerView.setVisibleItems(5);//新增滚轮内容可见数量
        cityPickerView.setIsCyclic(true);//滚轮是否循环滚动
        cityPickerView.show();
```
#### 结果返回
只需传入Context便可获取选择的省市区域的信息，结果返回四项，可根据自己的实际需求进行选择。

 1. citySelected[0]：表示：省份信息
 2. citySelected[1]：表示：城市信息
 3. citySelected[2]：表示：区县信息
 4. citySelected[3]：表示：邮编信息


----------


### V0.1.0版本更新内容
1. 直接传入context获取省市区信息









### 数据来源
数据来源主要有2种方式，可根据喜好选择哪种方式使用。

#### xml数据
![](http://img.blog.csdn.net/20160512153839068)

#### json数据
![](http://img.blog.csdn.net/20160512153906553)



### 关于我
[我的个人博客](http://crazyandcoder.github.io/about/)

### 感谢

- [http://blog.csdn.net/wulianghuan/article/details/41549189](http://blog.csdn.net/wulianghuan/article/details/41549189)





