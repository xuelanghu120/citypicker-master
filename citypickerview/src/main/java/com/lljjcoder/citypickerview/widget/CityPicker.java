package com.lljjcoder.citypickerview.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lljjcoder.citypickerview.R;
import com.lljjcoder.citypickerview.model.CityModel;
import com.lljjcoder.citypickerview.model.DistrictModel;
import com.lljjcoder.citypickerview.model.ProvinceModel;
import com.lljjcoder.citypickerview.utils.XmlParserHandler;
import com.lljjcoder.citypickerview.widget.wheel.OnWheelChangedListener;
import com.lljjcoder.citypickerview.widget.wheel.WheelView;
import com.lljjcoder.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 省市区三级选择
 * 作者：liji on 2015/12/17 10:40
 * 邮箱：lijiwork@sina.com
 */
public class CityPicker implements CanShow, OnWheelChangedListener {
    ;
    private boolean mTvTitleVisible
            ;
    private TextView mTvTitle;
    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mViewProvince;

    private WheelView mViewCity;

    private WheelView mViewDistrict;

    private TextView mTvOK;

    private TextView mTvCancel;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    private String[] mCities;
    private  String[] districtDatas;

    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;

    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    private OnCityItemClickListener listener;
    private String[] mAreas;

    public interface OnCityItemClickListener {
        void onSelected(String... citySelected);
    }

    public void setOnCityItemClickListener(OnCityItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;

    private int textSize = DEFAULT_TEXT_SIZE;

    /**
     * 滚轮显示的item个数
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;

    /**
     * 省滚轮是否循环滚动
     */
    private boolean isProvinceCyclic = true;

    /**
     * 市滚轮是否循环滚动
     */
    private boolean isCityCyclic = true;

    /**
     * 区滚轮是否循环滚动
     */
    private boolean isDistrictCyclic = true;

    /**
     * item间距
     */
    private int padding = 5;

    //    /**
    //     * 取消按钮文字颜色
    //     */
    //    private int cancelTextColor = Color.BLACK;

    /**
     * Color.BLACK
     */
    private String cancelTextColorStr = "#000000";

    //    /**
    //     *
    //     * 确认按钮文字颜色
    //     */
    //    private int confirmTextColor = Color.BLUE;

    /**
     * Color.BLUE
     */
    private String confirmTextColorStr = "#0000FF";

    /**
     * 第一次默认的显示省份，一般配合定位，使用
     */
    private String defaultProvinceName = "江苏";

    /**
     * 第一次默认得显示城市，一般配合定位，使用
     */
    private String defaultCityName = "常州";

    /**
     * 第一次默认得显示，一般配合定位，使用
     */
    private String defaultDistrict = "新北区";

    /**
     * 两级联动
     */
    private boolean showProvinceAndCity = false;

    private boolean showCity = true;
    private boolean isRelation =  true;
    private TextView outsideTextView;
    private String[] provinceDatas;
    private String[] cityDatas;


    private CityPicker(Builder builder) {
        this.provinceDatas = builder.provinceDatas;
        this.cityDatas = builder.cityDatas;
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.visibleItems = builder.visibleItems;
        this.isProvinceCyclic = builder.isProvinceCyclic;
        this.isDistrictCyclic = builder.isDistrictCyclic;
        this.isCityCyclic = builder.isCityCyclic;
        this.context = builder.mContext;
        this.padding = builder.padding;
        this.outsideTextView = builder.textView;

        //        this.confirmTextColor = builder.confirmTextColor;
        this.confirmTextColorStr = builder.confirmTextColorStr;
        //        this.cancelTextColor = builder.cancelTextColor;
        this.cancelTextColorStr = builder.cancelTextColorStr;

        this.defaultDistrict = builder.defaultDistrict;
        this.defaultCityName = builder.defaultCityName;
        this.defaultProvinceName = builder.defaultProvinceName;

        this.showProvinceAndCity = builder.showProvinceAndCity;

        this.mTvTitleVisible = builder.mTvTitleVisible;
        this.showCity = builder.showCity;
        this.isRelation = builder.isRelation;
        this.districtDatas = builder.districtDatas;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_citypicker, null);



        mViewProvince = (WheelView) popview.findViewById(R.id.id_province);
        mViewCity = (WheelView) popview.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) popview.findViewById(R.id.id_district);
        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);
        //中间titile
        mTvTitle = (TextView) popview.findViewById(R.id.tv_title);
        if (mTvTitleVisible){
            mTvTitle.setVisibility(View.VISIBLE);
        }else {
            mTvTitle.setVisibility(View.GONE);
        }

        popwindow = new PopupWindow(popview, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popwindow.setBackgroundDrawable(new ColorDrawable(0x80000000));
        popwindow.setAnimationStyle(R.style.AnimBottom);
        popwindow.setTouchable(true);
        popwindow.setOutsideTouchable(true);
        popwindow.setFocusable(true);

        //设置确认按钮文字颜色
        if (!TextUtils.isEmpty(this.confirmTextColorStr)) {
            mTvOK.setTextColor(Color.parseColor(this.confirmTextColorStr));
        }
        //        else {
        //            mTvOK.setTextColor(this.confirmTextColor);
        //        }

        //设置取消按钮文字颜色
        if (!TextUtils.isEmpty(this.cancelTextColorStr)) {
            mTvCancel.setTextColor(Color.parseColor(this.cancelTextColorStr));
        }
        //        else {
        //            mTvCancel.setTextColor(this.cancelTextColor);
        //        }

        //只显示省市两级联动
        if (this.showProvinceAndCity) {
            mViewDistrict.setVisibility(View.GONE);
            if (!showCity){
                mViewCity.setVisibility(View.GONE);
            }
        } else {
            mViewDistrict.setVisibility(View.VISIBLE);
        }

        //初始化城市数据
        initProvinceDatas(context);

        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showProvinceAndCity &&  listener!=null) {
                    listener.onSelected(mCurrentProviceName, mCurrentCityName, "", mCurrentZipCode);
                } else if (listener!=null){
                    listener.onSelected(mCurrentProviceName, mCurrentCityName, mCurrentDistrictName, mCurrentZipCode);
                }
                if (outsideTextView!=null){
                    outsideTextView.setText(mCurrentProviceName+" "+mCurrentCityName+" "+mCurrentDistrictName);
                }
                hide();
            }
        });

    }

    public static class Builder {
        /**
         * Default text color
         */
        public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

        /**
         * Default text size
         */
        public static final int DEFAULT_TEXT_SIZE = 18;

        // Text settings
        private int textColor = DEFAULT_TEXT_COLOR;

        private int textSize = DEFAULT_TEXT_SIZE;

        /**
         * 滚轮显示的item个数
         */
        private static final int DEF_VISIBLE_ITEMS = 5;

        // Count of visible items
        private int visibleItems = DEF_VISIBLE_ITEMS;

        /**
         * 省滚轮是否循环滚动
         */
        private boolean isProvinceCyclic = false;

        /**
         * 市滚轮是否循环滚动
         */
        private boolean isCityCyclic = false;

        /**
         * 区滚轮是否循环滚动
         */
        private boolean isDistrictCyclic = false;

        private Context mContext;

        /**
         * item间距
         */
        private int padding = 5;

        //        /**
        //         * 取消按钮文字颜色
        //         */
        //        private int cancelTextColor = Color.BLACK;

        /**
         * Color.BLACK
         */
        private String cancelTextColorStr = "#000000";

        //        /**
        //         *
        //         * 确认按钮文字颜色
        //         */
        //        private int confirmTextColor = Color.BLUE;

        /**
         * Color.BLUE
         */
        private String confirmTextColorStr = "#0000FF";

        /**
         * 第一次默认的显示省份，一般配合定位，使用
         */
        private String defaultProvinceName = "江苏";

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         */
        private String defaultCityName = "常州";

        /**
         * 第一次默认得显示，一般配合定位，使用
         */
        private String defaultDistrict = "新北区";

        /**
         * 两级联动
         */
        private boolean showProvinceAndCity = false;
        private boolean mTvTitleVisible;
        private TextView textView;
        private boolean showCity = true;
        private boolean isRelation = true;
        private String[] provinceDatas;
        private String[] cityDatas;
        private String[] districtDatas;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder showCity(boolean showCity) {
            this.showCity = showCity;
            return this;
        }

        public Builder isRelation(boolean isRelation) {
            this.isRelation = isRelation;
            return this;
        }

        public Builder initProvinceDatas(String[] provinceDatas) {
            this.provinceDatas = provinceDatas;
            return this;
        }

        public Builder initDistrictDatas(String[] districtDatas) {
            this.districtDatas = districtDatas;
            return this;
        }

        public Builder initcityeDatas(String[] cityDatas) {
            this.cityDatas = cityDatas;
            return this;
        }

        /**
         * 是否只显示省市两级联动  改动或来，所以true的时候其实不显示
         * @param flag
         * @return
         */
        public Builder showDistrict(boolean flag) {
            this.showProvinceAndCity = !flag;
            return this;
        }
        public Builder setTextView(TextView textView){
            this.textView = textView;
            return this;
        }

        /**
         * 第一次默认的显示省份，一般配合定位，使用
         * @param defaultProvinceName
         * @return
         */
        public Builder province(String defaultProvinceName) {
            this.defaultProvinceName = defaultProvinceName;
            return this;
        }

        /**
         * 第一次默认得显示城市，一般配合定位，使用
         * @param defaultCityName
         * @return
         */
        public Builder city(String defaultCityName) {
            this.defaultCityName = defaultCityName;
            return this;
        }

        /**
         * 第一次默认地区显示，一般配合定位，使用
         * @param defaultDistrict
         * @return
         */
        public Builder district(String defaultDistrict) {
            this.defaultDistrict = defaultDistrict;
            return this;
        }

        //        /**
        //         * 确认按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder confirTextColor(int color) {
        //            this.confirmTextColor = color;
        //            return this;
        //        }

        /**
         * 确认按钮文字颜色
         * @param color
         * @return
         */
        public Builder confirTextColor(String color) {
            this.confirmTextColorStr = color;
            return this;
        }

        //        /**
        //         * 取消按钮文字颜色
        //         * @param color
        //         * @return
        //         */
        //        public Builder cancelTextColor(int color) {
        //            this.cancelTextColor = color;
        //            return this;
        //        }

        /**
         * 取消按钮文字颜色
         * @param color
         * @return
         */
        public Builder cancelTextColor(String color) {
            this.cancelTextColorStr = color;
            return this;
        }

        /**
         * item文字颜色
         * @param textColor
         * @return
         */
        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setTvTitleVisible(boolean isVisible){
            this.mTvTitleVisible = isVisible;
            return this;
        }

        /**
         * item文字大小
         * @param textSize
         * @return
         */
        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 滚轮显示的item个数
         * @param visibleItems
         * @return
         */
        public Builder visibleItemsCount(int visibleItems) {
            this.visibleItems = visibleItems;
            return this;
        }

        /**
         * 省滚轮是否循环滚动
         * @param isProvinceCyclic
         * @return
         */
        public Builder provinceCyclic(boolean isProvinceCyclic) {
            this.isProvinceCyclic = isProvinceCyclic;
            return this;
        }

        /**
         * 市滚轮是否循环滚动
         * @param isCityCyclic
         * @return
         */
        public Builder cityCyclic(boolean isCityCyclic) {
            this.isCityCyclic = isCityCyclic;
            return this;
        }

        /**
         * 区滚轮是否循环滚动
         * @param isDistrictCyclic
         * @return
         */
        public Builder districtCyclic(boolean isDistrictCyclic) {
            this.isDistrictCyclic = isDistrictCyclic;
            return this;
        }

        /**
         * item间距
         * @param itemPadding
         * @return
         */
        public Builder itemPadding(int itemPadding) {
            this.padding = itemPadding;
            return this;
        }

        public CityPicker build() {
            CityPicker cityPicker = new CityPicker(this);
            return cityPicker;
        }

    }

    private void setUpData() {
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(defaultProvinceName) && mProvinceDatas.length > 0) {
            for (int i = 0; i < mProvinceDatas.length; i++) {
                if (mProvinceDatas[i].contains(defaultProvinceName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(context, mProvinceDatas);
        mViewProvince.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
        }else {
            mCurrentProviceName = mProvinceDatas[0];
            mViewProvince.setCurrentItem(0);
        }
        // 设置可见条目数量
        mViewProvince.setVisibleItems(visibleItems);
        mViewCity.setVisibleItems(visibleItems);
        mViewDistrict.setVisibleItems(visibleItems);
        mViewProvince.setCyclic(isProvinceCyclic);
        mViewCity.setCyclic(isCityCyclic);
        mViewDistrict.setCyclic(isDistrictCyclic);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);

        updateCities();
//        updateAreas();
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas(Context context) {
        if (provinceDatas !=null && provinceDatas.length!=0){
            mProvinceDatas = provinceDatas;
            return;
        }
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(),
                                districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        finally {

        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        if (showProvinceAndCity){
            return;
        }
        if (districtDatas!=null && districtDatas.length!=0){
            mAreas = cityDatas;
        }else {
            mAreas = mDistrictDatasMap.get(mCurrentCityName);
        }
        if (isRelation){
            int pCurrent = mViewCity.getCurrentItem();
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];//修改到changelistener中
        }



        if (mAreas == null) {
            mAreas = new String[] { "" };
        }

        int districtDefault = -1;
        if (!TextUtils.isEmpty(defaultDistrict) && mAreas.length > 0) {
            for (int i = 0; i < mAreas.length; i++) {
                if (mAreas[i].contains(defaultDistrict)) {
                    districtDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter districtWheel = new ArrayWheelAdapter<String>(context, mAreas);
        // 设置可见条目数量
        districtWheel.setTextColor(textColor);
        districtWheel.setTextSize(textSize);
        mViewDistrict.setViewAdapter(districtWheel);
        if (-1 != districtDefault) {
            mViewDistrict.setCurrentItem(districtDefault);
        }
        else {
            mCurrentDistrictName= mAreas[0];
            mViewDistrict.setCurrentItem(0);

        }
        districtWheel.setPadding(padding);
        //获取第一个区名称
        if (isRelation){

            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {

        if (cityDatas!=null && cityDatas.length!=0){
            mCities = cityDatas;
        }else {
            mCities = mCitisDatasMap.get(mCurrentProviceName);
        }
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];//修改到changelistener中

        if (mCities == null) {
            mCities = new String[] { "" };
        }

        int cityDefault = -1;
        if (!TextUtils.isEmpty(defaultCityName) && mCities.length > 0) {
            for (int i = 0; i < mCities.length; i++) {
                if (mCities[i].contains(defaultCityName)) {
                    cityDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter cityWheel = new ArrayWheelAdapter<String>(context, mCities);
        // 设置可见条目数量
        cityWheel.setTextColor(textColor);
        cityWheel.setTextSize(textSize);
        mViewCity.setViewAdapter(cityWheel);
        if (-1 != cityDefault) {
            mViewCity.setCurrentItem(cityDefault);
        } else {
            mCurrentCityName = mCities[0];
            mViewCity.setCurrentItem(0);
        }

        cityWheel.setPadding(padding);
        updateAreas();
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        if (!isShow()) {
            setUpData();
            popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return popwindow.isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            mCurrentProviceName = mProvinceDatas[newValue];
            if (isRelation){
                updateCities();
            }
//            updateCities();
        } else if (wheel == mViewCity) {
            mCurrentCityName = mCities[newValue];
            if (isRelation){
                updateAreas();
            }
//            updateAreas();
        } else if (wheel == mViewDistrict) {
            if (isRelation){
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
                Log.d("mCurrentDistrictName",mCurrentDistrictName);
                mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
                Log.d("mCurrentDistrictName",mCurrentZipCode);
            }else {
                mCurrentDistrictName = mAreas[newValue];
            }
        }
    }
}
