package com.lljjcoder.citypickerview.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lljjcoder.citypickerview.R;
import com.lljjcoder.citypickerview.widget.wheel.OnWheelChangedListener;
import com.lljjcoder.citypickerview.widget.wheel.WheelView;
import com.lljjcoder.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;



public class SinglePicker implements CanShow, OnWheelChangedListener {
    private boolean mTvTitleVisible = false;
    private TextView mTvTitle;
    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private WheelView mWheelView;


    private TextView mTvOK;

    private TextView mTvCancel;

    /**
     * 所有数据
     */
    private String[] mDatas;


    /**
     * 当前数据的名称
     */
    protected String mCurrentName;


    private OnItemClickListener listener;
    private CharSequence defaultName = null;

    public interface OnItemClickListener {
        void onSelected(String  selected);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
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
     * 滚轮是否循环滚动
     */
    private boolean isCyclicForever = true;


    /**
     * item间距
     */
    private int padding = 10;


    /**
     * Color.BLACK
     */
    private String cancelTextColorStr = "#000000";


    /**
     * Color.BLUE
     */
    private String confirmTextColorStr = "#0000FF";


    private SinglePicker(Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.visibleItems = builder.visibleItems;
        this.isCyclicForever = builder.isCyclicForever;

        this.context = builder.mContext;
        this.padding = builder.padding;
        this.mDatas = builder.datas;

        this.confirmTextColorStr = builder.confirmTextColorStr;

        this.cancelTextColorStr = builder.cancelTextColorStr;

        this.mTvTitleVisible = builder.mTvTitleVisible;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_singlepicker, null);


        mWheelView = (WheelView) popview.findViewById(R.id.id_province);

        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);
        //中间titile
        mTvTitle = (TextView) popview.findViewById(R.id.tv_title);
        if (mTvTitleVisible) {
            mTvTitle.setVisibility(View.VISIBLE);
        } else {
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

        //设置取消按钮文字颜色
        if (!TextUtils.isEmpty(this.cancelTextColorStr)) {
            mTvCancel.setTextColor(Color.parseColor(this.cancelTextColorStr));
        }

        // 添加change事件
        mWheelView.addChangingListener(this);

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
                listener.onSelected(mCurrentName);
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
         * 是否循环滚动
         */
        private boolean isCyclicForever = false;


        private Context mContext;

        /**
         * item间距
         */
        private int padding = 10;


        /**
         * Color.BLACK
         */
        private String cancelTextColorStr = "#000000";


        /**
         * Color.BLUE
         */
        private String confirmTextColorStr = "#0000FF";


        private boolean mTvTitleVisible;
        private String builderDefaultName;
        private String[] datas;

        public Builder(Context context) {
            this.mContext = context;
        }


        /**
         * 确认按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder confirTextColor(String color) {
            this.confirmTextColorStr = color;
            return this;
        }


        /**
         * 取消按钮文字颜色
         *
         * @param color
         * @return
         */
        public Builder cancelTextColor(String color) {
            this.cancelTextColorStr = color;
            return this;
        }

        /**
         * item文字颜色
         *
         * @param textColor
         * @return
         */
        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setTvTitleVisible(boolean isVisible) {
            this.mTvTitleVisible = isVisible;
            return this;
        }

        /**
         * item文字大小
         *
         * @param textSize
         * @return
         */
        public Builder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 滚轮显示的item个数
         *
         * @param visibleItems
         * @return
         */
        public Builder visibleItemsCount(int visibleItems) {
            this.visibleItems = visibleItems;
            return this;
        }

        /**
         * 滚轮是否循环滚动
         * @param isCyclicForever
         * @return
         */
        public Builder cyclicForever(boolean isCyclicForever) {
            this.isCyclicForever = isCyclicForever;
            return this;
        }


        /**
         * item间距
         *
         * @param itemPadding
         * @return
         */
        public Builder itemPadding(int itemPadding) {
            this.padding = itemPadding;
            return this;
        }
        /**
         * 默认的显示数据，一般配合定位，使用
         * @param defaultName
         * @return
         */
        public Builder defaultName(String defaultName) {
            this.builderDefaultName = defaultName;
            return this;
        }

        public Builder initDatas(String[] datas){
            this.datas = datas;
            return this;
        }

        public SinglePicker build() {
            SinglePicker cityPicker = new SinglePicker(this);
            return cityPicker;
        }

    }


    private void setUpData() {
        int provinceDefault = -1;
        if (mDatas.length==0){
            popwindow.dismiss();
            return;
        }
        if (!TextUtils.isEmpty(defaultName) && mDatas.length > 0) {
            for (int i = 0; i < mDatas.length; i++) {
                if (mDatas[i].contains(defaultName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(context, mDatas);
        mWheelView.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mWheelView.setCurrentItem(provinceDefault);
        }
        // 设置可见条目数量
        mWheelView.setVisibleItems(visibleItems);

        mWheelView.setCyclic(isCyclicForever);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);

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
        mCurrentName = mDatas[newValue];
    }
}