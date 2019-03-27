package com.sushant.timepicker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sushant.timepicker.horizontalLoop.HorizontalLoopView;
import com.sushant.timepicker.horizontalLoop.LoopViewAdapter;
import com.sushant.timepicker.percentHelper.PercentRelativeLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class DateTimeFragment extends Fragment implements View.OnClickListener {

    private List<String> totalHours;
    private List<String> totalMinutes;
    private List<String> totalDays;
    private List<String> totalMonths;

    private HorizontalLoopView monthLoopView, dayLoopView, minutesLoopView, hoursLoopView;
    private String selectedHours, selectedMinutes, selectedMonth, selectedDay;
    private String currentHour, currentMinutes, currentMonth, currentDay;
    private NumberFormat numberFormat;

    private int hoursSelectedPosition, minutesSelectedPosition, monthSelectedPosition, daySelectedPosition;


    private Ranges ranges;

    private static DateTimeFragment dateTimeFragment;
    private TimePickerListener timePickerListener;

    private ScaleButton confirmButton;

    //Customize params
    private TextView selectDateText, selectTimeText;
    private PercentRelativeLayout rootView;


    public static DateTimeFragment newInstance() {

        if (dateTimeFragment == null)
            dateTimeFragment = new DateTimeFragment();
        return dateTimeFragment;


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        timePickerListener = (TimePickerListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timePickerListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        totalHours = DateTimePickerUtils.getHours();
        totalMinutes = DateTimePickerUtils.getMinutes();
        totalMonths = DateTimePickerUtils.getMonths();

        currentHour = DateTimePickerUtils.getCurrentHour();
        currentMinutes = DateTimePickerUtils.getCurrentMinutes();
        currentMonth = DateTimePickerUtils.getCurrentMonth();
        currentDay = DateTimePickerUtils.getCurrentDay();

        totalDays = DateTimePickerUtils.getDays(currentMonth);


        numberFormat = new DecimalFormat("00");


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_picker, container, false);
        monthLoopView = view.findViewById(R.id.month);
        dayLoopView = view.findViewById(R.id.day);
        minutesLoopView = view.findViewById(R.id.minutes);
        hoursLoopView = view.findViewById(R.id.hours);
        confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);

        selectDateText = view.findViewById(R.id.selectDateText);
        selectTimeText = view.findViewById(R.id.selectTimeText);
        rootView = view.findViewById(R.id.rootView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMonth(currentMonth);
        setUpDay(currentDay);
        setUpMinutes(currentMinutes);
        setUpHour(currentHour);
    }

    private void setUpMonth(String currentMonth) {
        totalDays = DateTimePickerUtils.getDays(DateTimePickerUtils.getMonthByNumber(totalMonths.indexOf(currentMonth)));
        monthSelectedPosition = totalMonths.indexOf(currentMonth);
        monthLoopSetUp(monthSelectedPosition);

    }

    private void monthLoopSetUp(final int position) {
        monthLoopView.setLoopViewAdapter(new LoopViewAdapter() {
            @Override
            protected int setCenterIndex() {
                return position;
            }

            @Override
            public int getChildWidth() {
                return (int) (ScreenUtils.getScreenWidth(getActivity()) * 0.2);
            }

            @Override
            public int getItemCount() {
                return totalMonths.size();
            }

            @Override
            public View getView(int position, boolean isCenter) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                View view = getLayoutInflater().inflate(R.layout.item_time, null);
                view.setSelected(isCenter);
                params.width = getChildWidth();
                view.setLayoutParams(params);
                TextView textView = view.findViewById(R.id.item_data);
                textView.setTextSize((float) (ScreenUtils.getScreenWidth(getActivity()) * 0.022));

                if (isCenter) {
                    textView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.unselected_color));
                }
                return view;
            }

            @Override
            public void setData(View scrollView, int position) {
                if (position == -1)
                    return;
                TextView item = scrollView.findViewById(R.id.item_data);
                item.setText(totalMonths.get(position));
            }

            @Override
            public void onSelect(View selectView, int position) {
                selectedMonth = totalMonths.get(position);
                totalDays = DateTimePickerUtils.getDays(selectedMonth);
                int daySelectedPosition = totalDays.indexOf(selectedDay);
                if (daySelectedPosition == -1)
                    daySelectedPosition = 0;
                dayLoopSetUp(daySelectedPosition);
            }
        });
    }

    private void setUpDay(String currentDay) {
        daySelectedPosition = totalDays.indexOf(currentDay);
        dayLoopSetUp(daySelectedPosition);

    }

    private void dayLoopSetUp(final int position) {
        dayLoopView.setLoopViewAdapter(new LoopViewAdapter() {
            @Override
            protected int setCenterIndex() {
                return position;
            }

            @Override
            public int getChildWidth() {
                return (int) (ScreenUtils.getScreenWidth(getActivity()) * 0.2);
            }

            @Override
            public int getItemCount() {
                return totalDays.size();
            }

            @Override
            public View getView(int position, boolean isCenter) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                View view = getLayoutInflater().inflate(R.layout.item_time, null);
                view.setSelected(isCenter);
                params.width = getChildWidth();
                view.setLayoutParams(params);
                TextView textView = view.findViewById(R.id.item_data);
                textView.setTextSize((float) (ScreenUtils.getScreenWidth(getActivity()) * 0.034));

                if (isCenter) {
                    textView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.unselected_color));
                }
                return view;
            }

            @Override
            public void setData(View scrollView, int position) {
                TextView item = scrollView.findViewById(R.id.item_data);
                item.setText(totalDays.get(position));
            }

            @Override
            public void onSelect(View selectView, int position) {
                selectedDay = totalDays.get(position);
            }
        });
    }

    private void setUpMinutes(String currentMinutes) {

        ranges = DateTimePickerUtils.getClosestIndex(numberFormat.format(Integer.parseInt(currentMinutes)));
        minutesSelectedPosition = totalMinutes.indexOf(ranges.value);

        if (minutesSelectedPosition == -1)
            minutesSelectedPosition = 0;
        minuteLoopSetUp(minutesSelectedPosition);
    }

    private void minuteLoopSetUp(final int position) {
        minutesLoopView.setLoopViewAdapter(new LoopViewAdapter() {
            @Override
            protected int setCenterIndex() {
                return position;
            }

            @Override
            public int getChildWidth() {
                return (int) (ScreenUtils.getScreenWidth(getActivity()) * 0.2);
            }

            @Override
            public int getItemCount() {
                return totalMinutes.size();
            }

            @Override
            public View getView(int position, boolean isCenter) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                View view = getLayoutInflater().inflate(R.layout.item_time, null);
                view.setSelected(isCenter);
                params.width = getChildWidth();
                view.setLayoutParams(params);
                TextView textView = view.findViewById(R.id.item_data);
                textView.setTextSize((float) (ScreenUtils.getScreenWidth(getActivity()) * 0.034));

                if (isCenter) {
                    textView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.unselected_color));
                }
                return view;
            }

            @Override
            public void setData(View scrollView, int position) {
                TextView item = scrollView.findViewById(R.id.item_data);
                item.setText(totalMinutes.get(position));
            }

            @Override
            public void onSelect(View selectView, int position) {
                selectedMinutes = totalMinutes.get(position);
            }
        });
    }

    private void setUpHour(String currentHour) {
        hoursSelectedPosition = totalHours.indexOf(numberFormat.format(Integer.parseInt(currentHour)));

        if (ranges.offset == 1) {
            hoursSelectedPosition = hoursSelectedPosition + 1;
            if (hoursSelectedPosition > totalHours.size() - 1) {
                hoursSelectedPosition = 0;
            }
            if (Integer.parseInt(totalHours.get(hoursSelectedPosition)) == 00) {
                daySelectedPosition = daySelectedPosition + 1;

                if (daySelectedPosition > totalDays.size() - 1) {
                    daySelectedPosition = 0;
                    dayLoopSetUp(0);

                    monthSelectedPosition = monthSelectedPosition + 1;

                    if (monthSelectedPosition > totalMonths.size() - 1) {
                        monthSelectedPosition = 0;
                        monthLoopSetUp(0);
                        dayLoopSetUp(0);
                    } else {
                        monthLoopSetUp(monthSelectedPosition);
                        dayLoopSetUp(daySelectedPosition);
                    }
                } else {
                    dayLoopSetUp(daySelectedPosition);
                }


            }


        }

        hourLoopSetUp(hoursSelectedPosition);


    }

    private void hourLoopSetUp(final int position) {
        hoursLoopView.setLoopViewAdapter(new LoopViewAdapter() {
            @Override
            protected int setCenterIndex() {
                return position;
            }

            @Override
            public int getChildWidth() {
                return (int) (ScreenUtils.getScreenWidth(getActivity()) * 0.2);
            }

            @Override
            public int getItemCount() {
                return totalHours.size();
            }

            @Override
            public View getView(int position, boolean isCenter) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                View view = getLayoutInflater().inflate(R.layout.item_time, null);
                view.setSelected(isCenter);
                params.width = getChildWidth();
                view.setLayoutParams(params);
                TextView textView = view.findViewById(R.id.item_data);
                textView.setTextSize((float) (ScreenUtils.getScreenWidth(getActivity()) * 0.034));

                if (isCenter) {
                    textView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.unselected_color));
                }
                return view;
            }

            @Override
            public void setData(View scrollView, int position) {
                TextView item = scrollView.findViewById(R.id.item_data);
                item.setText(totalHours.get(position));
            }

            @Override
            public void onSelect(View selectView, int position) {
                selectedHours = totalHours.get(position);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == confirmButton) {
            if (timePickerListener != null) {
                if (!hoursLoopView.isScrolling() && !minutesLoopView.isScrolling()
                        && !monthLoopView.isScrolling() && !dayLoopView.isScrolling()) {
                    timePickerListener.onDateTimeSelected(selectedMonth, selectedDay, selectedHours, selectedMinutes);

                }
            }


        }
    }


    static class Ranges {
        String value;
        int offset = 0;
    }

    public interface TimePickerListener {
        void onDateTimeSelected(String selectedMonth, String selectedDay, String selectedHours, String selectedMinutes);
    }


    //View Customizations

    /**
     * Date Header Text Changes
     **/

    public void setDateHeaderText(String text) {
        selectDateText.setText(text);
    }

    public void setDateHeaderColor(@ColorInt int color) {
        selectDateText.setTextColor(color);
    }

    public void setBackgroundColor(int color) {
        rootView.setBackgroundColor(color);

    }


    /**
     * Time Header Text Changes
     **/

    public void setTimeHeaderText(String text) {
        selectTimeText.setText(text);
    }

    public void setTimeHeaderColor(@ColorInt int color) {
        selectTimeText.setTextColor(color);
    }


    // Confirm text Changes


    public void setConfirmText(String text) {
        confirmButton.setText(text);
    }

    public void setConfirmTextColor(@ColorInt int color) {
        confirmButton.setTextColor(color);
    }


    public void setConfirmBackground(Drawable drawable) {
        confirmButton.setBackground(drawable);
    }


    //Hours Loop View

    //Minutes Loop View

    //Months Loop View

    //Day Loop View


}
