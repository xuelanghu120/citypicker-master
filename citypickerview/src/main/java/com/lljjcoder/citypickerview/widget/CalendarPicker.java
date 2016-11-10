package com.lljjcoder.citypickerview.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lljjcoder.citypickerview.R;

import java.util.Calendar;


public class CalendarPicker implements CanShow {
    private final DatePicker mDatePicker;
    private TextView outsideTextView;
    private boolean mTvTitleVisible = false;
    private TextView mTvTitle;
    private Context context;

    private PopupWindow popwindow;

    private View popview;

    private TextView mTvOK;

    private TextView mTvCancel;

    private OnItemClickListener listener;
    private CharSequence defaultName = null;

    public interface OnItemClickListener {
        void onSelected(int... selected);
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

    private int mYear, mMonth, mDay;
    /**
     * Color.BLACK
     */
    private String cancelTextColorStr = "#0000FF";


    /**
     * Color.BLUE
     */
    private String confirmTextColorStr = "#0000FF";


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private CalendarPicker(Builder builder) {
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;


        this.context = builder.mContext;

        this.confirmTextColorStr = builder.confirmTextColorStr;

        this.cancelTextColorStr = builder.cancelTextColorStr;

        this.mTvTitleVisible = builder.mTvTitleVisible;
        this.outsideTextView = builder.textView;
        this.listener = builder.listener;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_canlendarpicker, null);

        mTvOK = (TextView) popview.findViewById(R.id.tv_confirm);
        mTvCancel = (TextView) popview.findViewById(R.id.tv_cancel);
        mDatePicker = (DatePicker) popview.findViewById(R.id.datePicker);
        //设置时间指针
        mDatePicker.setMaxDate(Calendar.getInstance().getTimeInMillis()); // 最大时间
        mDatePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);



        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
            }
        });

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
                if (listener != null) {
                    listener.onSelected(mYear, mMonth + 1, mDay);
                }
                if (outsideTextView != null) {
                    outsideTextView.setText(mYear + "/" + (mMonth + 1));
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
        private TextView textView;

        private Context mContext;

        /**
         * Color.BLACK
         */
        private String cancelTextColorStr = "#0000FF";


        /**
         * Color.BLUE
         */
        private String confirmTextColorStr = "#0000FF";


        private boolean mTvTitleVisible;


        public Builder(Context context) {
            this.mContext = context;
        }

        public OnItemClickListener listener;

        public Builder setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
            return this;
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

        public Builder setTextView(TextView textView) {
            this.textView = textView;
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


        public Builder setTvTitleVisible(boolean isVisible) {
            this.mTvTitleVisible = isVisible;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public CalendarPicker build() {
            CalendarPicker cityPicker = new CalendarPicker(this);
            return cityPicker;
        }

    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        if (!isShow()) {
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


}