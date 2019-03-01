package com.zwxt.shareApp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zwxt.shareApp.R;
import com.zwxt.shareApp.adapter.NumericWheelAdapter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BLTimerAlert {

    public interface OnSecondAlertClick {
        void onClick(int hour, int min, int second);
    }

    public interface OnYearAlertClick {
        void onClick(int year, int month, int day, int hour, int min);
    }

    public interface OnDateAlertClick {
        void onClick(int year, int month, int day);
    }

    public static Dialog showSecondAlert(final Context context, int hour, int min, int second, final OnSecondAlertClick alertDo) {
        final Dialog dlg = new Dialog(context, R.style.BLTheme_Dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.bl_hour_time_alert_layout, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        final WheelView hourView = (WheelView) layout.findViewById(R.id.hour_view);
        final WheelView minView = (WheelView) layout.findViewById(R.id.min_view);
        final WheelView secondView = (WheelView) layout.findViewById(R.id.second_view);
        TextView confimButton = (TextView) layout.findViewById(R.id.btn_yes);
        TextView cacelButton = (TextView) layout.findViewById(R.id.btn_close);
        secondView.setVisibility(View.VISIBLE);
        hourView.setVisibleItems(5);
        minView.setVisibleItems(5);
        secondView.setVisibleItems(5);

        hourView.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
        minView.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        secondView.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        hourView.setLabel(context.getString(R.string.alert_hour));
        minView.setLabel(context.getString(R.string.alert_min));
        secondView.setLabel(context.getString(R.string.alert_second));
        hourView.setCyclic(true);
        minView.setCyclic(true);
        secondView.setCyclic(true);
        hourView.setCurrentItem(hour);
        minView.setCurrentItem(min);
        secondView.setCurrentItem(second);

        confimButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDo.onClick(hourView.getCurrentItem(), minView.getCurrentItem(), secondView.getCurrentItem());
                dlg.dismiss();
            }
        });

        cacelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(layout);
        dlg.show();
        return dlg;
    }


    public static Dialog showYearAlert(final Context context, int year, int month, int day, int hour, int min, final OnYearAlertClick alertDo) {
        return showYearAlert(context, 1, false, year, month, day, hour, min, alertDo);
    }

    public static Dialog showYearAlert(final Context context, final int yearSpace, boolean end, int year, int month, int day, int hour, int min, final OnYearAlertClick alertDo) {
        final Dialog dlg = new Dialog(context, R.style.BLTheme_Dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.bl_year_time_alert_layout, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        Calendar calendar = Calendar.getInstance();
        final int mCurrentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);

        final WheelView yearView = (WheelView) layout.findViewById(R.id.wheel_timer_year);
        final WheelView monthView = (WheelView) layout.findViewById(R.id.wheel_timer_month);
        final WheelView dayView = (WheelView) layout.findViewById(R.id.wheel_timer_day);
        final WheelView hourView = (WheelView) layout.findViewById(R.id.wheel_timer_hour);
        final WheelView minView = (WheelView) layout.findViewById(R.id.wheel_timer_min);
        TextView confimButton = (TextView) layout.findViewById(R.id.btn_yes);
        TextView cacelButton = (TextView) layout.findViewById(R.id.btn_close);
        yearView.setVisibleItems(5);
        monthView.setVisibleItems(5);
        dayView.setVisibleItems(5);
        minView.setVisibleItems(5);
        hourView.setVisibleItems(5);

        yearView.setLabel(context.getString(R.string.alert_year));
        monthView.setLabel(context.getString(R.string.alert_month));
        dayView.setLabel(context.getString(R.string.alert_day));
        hourView.setLabel(context.getString(R.string.alert_hour));
        minView.setLabel(context.getString(R.string.alert_min));

        yearView.setCyclic(true);
        monthView.setCyclic(true);
        dayView.setCyclic(true);
        hourView.setCyclic(true);
        minView.setCyclic(true);

        hourView.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
        minView.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));

        String[] monthsBig = {"1", "3", "5", "7", "8", "10", "12"};
        String[] monthsLittle = {"4", "6", "9", "11"};
        final List<String> mMonthBigList = Arrays.asList(monthsBig);
        final List<String> mMonthLittleList = Arrays.asList(monthsLittle);

        if (end) {
            yearView.setAdapter(new NumericWheelAdapter(mCurrentYear - yearSpace, mCurrentYear + yearSpace));
        } else {
            yearView.setAdapter(new NumericWheelAdapter(mCurrentYear, mCurrentYear + yearSpace));
        }

        monthView.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
        if (mMonthBigList.contains(String.valueOf(currentMonth + 1))) {
            dayView.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
        } else if (mMonthLittleList.contains(String.valueOf(currentMonth + 1))) {
            dayView.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
        } else {
            // 闰年
            if ((mCurrentYear % 4 == 0 && mCurrentYear % 100 != 0) || mCurrentYear % 400 == 0)
                dayView.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
            else
                dayView.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
        }

        if (end) {
            yearView.setCurrentItem(yearSpace + year - mCurrentYear);
        } else {
            yearView.setCurrentItem(year - mCurrentYear);
        }

        monthView.setCurrentItem(month);
        dayView.setCurrentItem(day);
        hourView.setCurrentItem(hour);
        minView.setCurrentItem(min);

        yearView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {

                int year_num = newValue + mCurrentYear;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (mMonthBigList.contains(String.valueOf(monthView.getCurrentItem() + 1))) {
                    dayView.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (mMonthLittleList.contains(String.valueOf(monthView.getCurrentItem() + 1))) {
                    dayView.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                        dayView.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        dayView.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        });

        monthView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (mMonthBigList.contains(String.valueOf(month_num))) {
                    dayView.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
                } else if (mMonthLittleList.contains(String.valueOf(month_num))) {
                    dayView.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
                } else {
                    if (((yearView.getCurrentItem()) % 4 == 0 && yearView.getCurrentItem() % 100 != 0) || yearView.getCurrentItem() % 400 == 0)
                        dayView.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
                    else
                        dayView.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
                }
            }
        });

        confimButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDo.onClick(yearView.getCurrentItem() + mCurrentYear - yearSpace + 1, monthView.getCurrentItem(), dayView.getCurrentItem(), hourView.getCurrentItem(), minView.getCurrentItem());
                dlg.dismiss();
            }
        });

        cacelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(layout);
        dlg.show();
        return dlg;
    }

    public static Dialog showDateAlert(final Context context, int year, int month, int day, final OnDateAlertClick alertDo) {
        final Dialog dlg = new Dialog(context, R.style.BLTheme_Dialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.bl_date_time_alert_layout, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        Calendar calendar = Calendar.getInstance();
        final int mCurrentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        final WheelView yearView = (WheelView) layout.findViewById(R.id.wheel_timer_year);
        final WheelView monthView = (WheelView) layout.findViewById(R.id.wheel_timer_month);
        final WheelView dayView = (WheelView) layout.findViewById(R.id.wheel_timer_day);
        TextView confimButton = (TextView) layout.findViewById(R.id.btn_yes);
        TextView cacelButton = (TextView) layout.findViewById(R.id.btn_close);
        yearView.setVisibleItems(5);
        monthView.setVisibleItems(5);
        dayView.setVisibleItems(5);

        yearView.setLabel(context.getString(R.string.alert_year));
        monthView.setLabel(context.getString(R.string.alert_month));
        dayView.setLabel(context.getString(R.string.alert_day));

        yearView.setCyclic(true);
        monthView.setCyclic(true);
        dayView.setCyclic(true);

        String[] monthsBig = {"1", "3", "5", "7", "8", "10", "12"};
        String[] monthsLittle = {"4", "6", "9", "11"};
        final List<String> mMonthBigList = Arrays.asList(monthsBig);
        final List<String> mMonthLittleList = Arrays.asList(monthsLittle);

        yearView.setAdapter(new NumericWheelAdapter(mCurrentYear-18, mCurrentYear ));

        monthView.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));

        dayView.setAdapter(new NumericWheelAdapter(1, currentDay, "%02d"));

		if (mMonthBigList.contains(String.valueOf(month + 1))) {
			dayView.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
		} else if (mMonthLittleList.contains(String.valueOf(month + 1))) {
			dayView.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				dayView.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
			else
				dayView.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
		}

        yearView.setCurrentItem(year - mCurrentYear);

        monthView.setCurrentItem(month);
        dayView.setCurrentItem(day);

        yearView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + mCurrentYear;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (mMonthBigList.contains(String.valueOf(monthView.getCurrentItem() + 1))) {
                    dayView.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (mMonthLittleList.contains(String.valueOf(monthView.getCurrentItem() + 1))) {
                    dayView.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                        dayView.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        dayView.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        });

        monthView.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (mMonthBigList.contains(String.valueOf(month_num))) {
                    dayView.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
                } else if (mMonthLittleList.contains(String.valueOf(month_num))) {
                    dayView.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
                } else {
                    if (((yearView.getCurrentItem()) % 4 == 0 && yearView.getCurrentItem() % 100 != 0) || yearView.getCurrentItem() % 400 == 0)
                        dayView.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
                    else
                        dayView.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
                }
            }
        });

        confimButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDo.onClick(yearView.getCurrentItem() + mCurrentYear - 1 + 1 - 18, monthView.getCurrentItem()+1, dayView.getCurrentItem()+1 );
                dlg.dismiss();
            }
        });

        cacelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(layout);
        dlg.show();
        return dlg;
    }

}
