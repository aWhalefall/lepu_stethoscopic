package com.core.lib.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.lib.ui.wheel.ArrayWheelAdapter;
import com.core.lib.ui.wheel.NumericWheelAdapter;
import com.core.lib.ui.wheel.OnWheelChangedListener;
import com.core.lib.ui.wheel.WheelView;
import com.core.lib.utils.DateType;
import com.core.lib.utils.DensityUtil;
import com.core.lib.utils.main.AppManager;
import com.core.lib.utils.main.UtilityBase;
import com.lib.custom.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDialog {

    public static final int START_YEAR = 1900;
    public static boolean isEvaluate = false;
    private boolean clickDismiss = true;
    private Activity mActivity;
    private View dialogView;
    private Dialog mDialog;
    private int width;
    private int height;

    public MyDialog(Activity activity) {
        mActivity = activity;
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        dialogView = inflater.inflate(R.layout.my_dialog, null);
        int w1 = ((android.app.Activity) mActivity).getWindowManager()
                .getDefaultDisplay().getWidth();
        int w2 = ((android.app.Activity) mActivity).getWindowManager()
                .getDefaultDisplay().getHeight();
        width = (int) (Math.min(w1, w2) * 0.85);
        Button btnCancel = (Button) dialogView.findViewById(R.id.DialogButton1);
        btnCancel.setBackgroundResource(R.drawable.btn_cancel_ok_selector);
        Button btnOk = (Button) dialogView.findViewById(R.id.DialogButton2);
        btnOk.setBackgroundResource(R.drawable.btn_cancel_ok_selector);
    }

    public boolean isClickDismiss() {
        return clickDismiss;
    }

    public void setClickDismiss(boolean clickDismiss) {
        this.clickDismiss = clickDismiss;
    }

    public View getView() {
        return dialogView;
    }


    /**
     * 关闭圆角功能
     */
    @SuppressLint("ResourceAsColor")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setNocorner() {
        LinearLayout linerlayout_btn = (LinearLayout) dialogView.findViewById(R.id.linerlayout_btn);
        linerlayout_btn.setBackgroundResource(R.drawable.top);
    }

    /**
     * 是否回退按键
     *
     * @param backPressed
     * @return
     */
    public Dialog create(final OnClickListener backPressed) {
        mDialog = new Dialog(mActivity, R.style.Theme_Lib_MyDialog) {
            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                if (backPressed != null) {
                    backPressed.onClick(null);
                    return true;
                }
                return super.onKeyDown(keyCode, event);
            }
        };
        //initStyle();
        mDialog.setContentView(dialogView);
        LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = width;
        if (height > 0)
            params.height = height;

        mDialog.setContentView(dialogView, params);
        mDialog.getWindow().setWindowAnimations(
                R.style.Theme_Lib_Push_Down_Up_Dialog);
        // setCanceledOnTouchOutside(false);
        return mDialog;
    }

    /**
     * 设置按钮color
     *
     * @param color
     */

    public void setBtnColor(int color) {
        Button btnCancel = (Button) dialogView.findViewById(R.id.DialogButton1);
        btnCancel.setTextColor(color);
        Button btnOk = (Button) dialogView.findViewById(R.id.DialogButton2);
        btnOk.setTextColor(color);
    }


    /**
     * 设置标题的两条竖线隐藏
     */
    public void setTitleLineHidden() {
       dialogView.findViewById(R.id.imgview_line1).setVisibility(View.GONE);
       dialogView.findViewById(R.id.imgview_line2).setVisibility(View.GONE);
    }


    private void initStyle() {

        // Button button1 = (Button)
        // (dialogView.findViewById(R.id.DialogButton1));
        // Button button2 = (Button)
        // (dialogView.findViewById(R.id.DialogButton2));
        // ImageView middleDivider = (ImageView) dialogView
        // .findViewById(R.id.DialogButtonDivider);
        //
        // if (button1.getVisibility() == View.VISIBLE
        // && button2.getVisibility() == View.VISIBLE) {
        // middleDivider.setVisibility(View.VISIBLE);
        // } else {
        // middleDivider.setVisibility(View.GONE);
        // }
    }

    public void showMyDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public MyDialog setDialogCustomWidthHeight() {

        if (mDialog != null) {
            int w1 = ((android.app.Activity) mActivity).getWindowManager()
                    .getDefaultDisplay().getWidth();
            int w2 = ((android.app.Activity) mActivity).getWindowManager()
                    .getDefaultDisplay().getHeight();
            int ww = (int) (Math.min(w1, w2) * 0.8);
            int hh = (int) (Math.max(w1, w2) * 0.8);
            mDialog.getWindow().setLayout(ww, hh);
        }

        return this;
    }

    public MyDialog setIcon(int resId) {
        ImageView icon = (ImageView) (dialogView
                .findViewById(R.id.DialogTitleIcon));
        icon.setImageResource(resId);
        // icon.setVisibility(View.VISIBLE);
        // 暂时隐藏图片
        dialogView.findViewById(R.id.DialogTitle).setVisibility(View.VISIBLE);
        return this;
    }

    public MyDialog setTitle(int resId) {
        TextView title = (TextView) (dialogView
                .findViewById(R.id.DialogTitleText));
        title.setText(resId);
        title.setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.DialogTitle).setVisibility(View.VISIBLE);
        return this;
    }

    public MyDialog setTitle(String text) {
        TextView title = (TextView) (dialogView
                .findViewById(R.id.DialogTitleText));
        title.setText(text);
        title.setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.DialogTitle).setVisibility(View.VISIBLE);
        return this;
    }

    public MyDialog setTitleFill() {
        LinearLayout linearLayout_fileTitle = (LinearLayout) (dialogView
                .findViewById(R.id.lineer_DialogTitleLayout));
        linearLayout_fileTitle.setVisibility(View.VISIBLE);
        return this;
    }

    public MyDialog setMessage(int resId) {
        TextView text = (TextView) (dialogView
                .findViewById(R.id.DialogContentText));
        text.setText(resId);
        text.setVisibility(View.VISIBLE);
        return this;
    }

    public MyDialog setMessage(String message) {
        TextView text = (TextView) (dialogView
                .findViewById(R.id.DialogContentText));
        text.setText(message);
        text.setVisibility(View.VISIBLE);
        return this;
    }

    public MyDialog addView(View view) {
        LinearLayout content = (LinearLayout) (dialogView
                .findViewById(R.id.DialogContent));
        if (isEvaluate) {
            LinearLayout layout = (LinearLayout) (dialogView
                    .findViewById(R.id.DialogTitleLayout));
            layout.setBackgroundResource(R.color.lib_color_transparent);
            content.setBackgroundResource(R.color.lib_color_transparent);
        }
        content.addView(view);
        return this;
    }

    private void hideEditTextSofeBoard() {
        EditText editText = (EditText) dialogView
                .findViewById(R.id.DialogContentEditText);
        if (editText != null && editText.getVisibility() == View.VISIBLE) {
            editText.clearFocus();
            ((InputMethodManager) editText.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    editText.getWindowToken(), 0);
        }

        EditText editText2 = (EditText) dialogView
                .findViewById(R.id.DialogContentEditText2);
        if (editText2 != null && editText2.getVisibility() == View.VISIBLE) {
            editText2.clearFocus();
            ((InputMethodManager) editText2.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    editText.getWindowToken(), 0);
        }
    }

    public MyDialog setNegativeButton(int resId, final OnClickListener listener) {
        Button button = (Button) (dialogView.findViewById(R.id.DialogButton1));
        button.setText(resId);
        button.setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.DialogButton).setVisibility(View.VISIBLE);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(dialogView);

                hideEditTextSofeBoard();

                if (mDialog != null && clickDismiss)
                    mDialog.dismiss();
            }
        });
        return this;
    }

    public MyDialog setPositiveButton(int resId, final OnClickListener listener) {
        Button button = (Button) (dialogView.findViewById(R.id.DialogButton2));
        button.setText(resId);
        button.setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.DialogButton).setVisibility(View.VISIBLE);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(dialogView);

                hideEditTextSofeBoard();

                if (mDialog != null && clickDismiss)
                    mDialog.dismiss();
            }
        });
        return this;
    }


    public MyDialog setBackground(int resId) {
        dialogView.setBackgroundResource(resId);
        return this;
    }

    public MyDialog setHeight(int pix) {
        height = pix;
        return this;
    }

    public MyDialog setCanceledOnTouchOutside(boolean cancel) {
        if (mDialog != null)
            mDialog.setCanceledOnTouchOutside(cancel);

        return this;
    }

    public MyDialog setCancelable(boolean cancel) {
        if (mDialog != null)
            mDialog.setCancelable(cancel);

        return this;
    }

    /**
     * 单个 item listview
     *
     * @param items
     * @param listener
     * @return
     */
    public MyDialog setSimpleItems(String[] items,
                                   final DialogInterface.OnClickListener listener) {
        ListView list = (ListView) (dialogView
                .findViewById(R.id.DialogContentList));
        list.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity,
                R.layout.list_simple, items);
        list.setAdapter(adapter);
        list.setFocusable(false);
        list.setCacheColorHint(0);
        list.setDivider(mActivity.getResources().getDrawable(
                R.color.lib_color_listview_divider));
        list.setDividerHeight(1);
        list.setChoiceMode(ListView.CHOICE_MODE_NONE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (listener != null)
                    listener.onClick(null, arg2);

                if (mDialog != null && clickDismiss)
                    mDialog.dismiss();
            }

        });
        return this;
    }

    /**
     * 可以单选 item
     *
     * @param items
     * @param checkedItem
     * @param listener
     * @return
     */
    public MyDialog setSingleChoiceItems(String[] items, int checkedItem,
                                         final DialogInterface.OnClickListener listener) {
        ListView list = (ListView) (dialogView
                .findViewById(R.id.DialogContentList));
        list.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity,
                R.layout.list_single_choice, items);
        list.setAdapter(adapter);
        list.setFocusable(false);
        list.setCacheColorHint(0);
        list.setDivider(mActivity.getResources().getDrawable(
                R.color.lib_color_listview_divider));
        list.setDividerHeight(1);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setItemChecked(checkedItem, true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (listener != null)
                    listener.onClick(null, arg2);

            }
        });
        return this;
    }

    /**
     * 填充 ListAdapter 的子类
     * arrayAdapter，SimpleAdapter ，CursorAdapter
     *
     * @param adapter
     * @param listener
     * @return
     */
    public MyDialog setListView(ListAdapter adapter,
                                final DialogInterface.OnClickListener listener) {
        ListView list = (ListView) (dialogView
                .findViewById(R.id.DialogContentList));
        list.setVisibility(View.VISIBLE);
        list.setAdapter(adapter);
        list.setFocusable(false);
        list.setCacheColorHint(0);
        list.setDivider(mActivity.getResources().getDrawable(
                R.color.lib_color_listview_divider));
        list.setDividerHeight(1);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (listener != null)
                    listener.onClick(null, arg2);

            }
        });
        return this;
    }

    /**
     * 嵌套gridview
     *
     * @param adapter
     * @param listener
     * @param numColumns
     * @param verticalSpacing
     * @param horizontalSpacing
     * @return
     */
    public MyDialog setGridView(ListAdapter adapter,
                                final DialogInterface.OnClickListener listener, int numColumns,
                                int verticalSpacing, int horizontalSpacing) {
        CustomGridView gridView = (CustomGridView) (dialogView
                .findViewById(R.id.DialogContentGrid));
        gridView.setVisibility(View.VISIBLE);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(numColumns);
        gridView.setVerticalSpacing(verticalSpacing);
        gridView.setHorizontalSpacing(horizontalSpacing);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (listener != null)
                    listener.onClick(null, arg2);

            }
        });
        return this;
    }

    public EditText getCurrentEditText() {
        EditText editText = (EditText) (dialogView
                .findViewById(R.id.DialogContentEditText));
        return editText;
    }

    public EditText getCurrentEditText2() {
        EditText editText2 = (EditText) (dialogView
                .findViewById(R.id.DialogContentEditText2));
        return editText2;
    }

    /*
     * index: 0 editext1 1 edittext2
     */
    public MyDialog setEditText(int index, String text, String hintText,
                                int inputType, InputFilter[] filters) {
        int rid = 0;
        if (index == 0) {
            rid = R.id.DialogContentEditText;
        } else if (index == 1) {
            rid = R.id.DialogContentEditText2;
        } else {
            rid = R.id.DialogContentEditText;
        }

        final EditText editText = (EditText) (dialogView.findViewById(rid));
        editText.setVisibility(View.VISIBLE);
        editText.setText(text);
        editText.setHint(hintText);
        editText.setInputType(inputType);
        if (inputType == InputType.TYPE_TEXT_FLAG_MULTI_LINE) {
            editText.setGravity(Gravity.TOP);
            editText.setSingleLine(false);
            editText.setHorizontallyScrolling(false);
        }
        editText.setFilters(filters);
        editText.setSelection(text.length());
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                ((InputMethodManager) editText.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE)).showSoftInput(editText,
                        InputMethodManager.SHOW_FORCED);
            }
        }, 50);

        return this;
    }

    /**
     * 设置Html 格式样式
     *
     * @param html
     * @return
     */
    public MyDialog setHtml(String html) {
        TextView linkText = (TextView) (dialogView
                .findViewById(R.id.DialogContentHtml));
        linkText.setVisibility(View.VISIBLE);
        linkText.setText(Html.fromHtml(html));
        linkText.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    /**
     * 带年月日的 2015 年
     *
     * @param date
     * @param type
     * @return
     */
    public MyDialog setDateTimePicker(Date date, DateType type) {
        LinearLayout timeLayout = (LinearLayout) (dialogView
                .findViewById(R.id.DialogDateTimePicker));
        timeLayout.setVisibility(View.VISIBLE);

        int year = 0;
        int month = 0;
        int day = 0;

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        if (date != null)
            calendar.setTime(date);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        if (type == DateType.YEAR_MONTH_DAY) {
            day = calendar.get(Calendar.DATE);
        }

        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        // 年
        final WheelView wv_year = (WheelView) dialogView
                .findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, currentYear));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

        // 月
        final WheelView wv_month = (WheelView) dialogView
                .findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(month);

        // 日
        final WheelView wv_day = (WheelView) dialogView.findViewById(R.id.day);
        wv_day.setCyclic(true);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }
        wv_day.setLabel("日");
        wv_day.setCurrentItem(day - 1);

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big
                        .contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(wv_month
                        .getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0)
                            || year_num % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                            .getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 显示的是 年月日 还是 年月
        if (type == DateType.YEAR_MONTH_DAY) {
            wv_day.setVisibility(View.VISIBLE);
        } else {
            wv_day.setVisibility(View.GONE);
        }

        if (type == DateType.YEAR) {
            wv_year.setVisibility(View.VISIBLE);
            wv_month.setVisibility(View.GONE);
            wv_day.setVisibility(View.GONE);
        }
        // 根据屏幕密度来指定选择器字体的大小
        int textSize = UtilityBase.getTextNumberSize(mActivity);

        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;

        return this;
    }

    /**
     * 时间滚轮 小时-分钟
     *
     * @param date
     * @return
     */
    public MyDialog setTimePicker(Date date) {
        LinearLayout timeLayout = (LinearLayout) (dialogView
                .findViewById(R.id.DialogTimePicker));
        timeLayout.setVisibility(View.VISIBLE);

        int hour = 0;
        int minutes = 0;

        Calendar calendar = Calendar.getInstance();

        if (date != null)
            calendar.setTime(date);

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);

        // 时
        final WheelView wv_hour = (WheelView) dialogView
                .findViewById(R.id.hour);
        wv_hour.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hour.setCyclic(true);// 可循环滚动
        wv_hour.setLabel("时");// 添加文字
        wv_hour.setCurrentItem(hour);// 初始化时显示的数据

        // 分
        final WheelView wv_minutes = (WheelView) dialogView
                .findViewById(R.id.minute);
        wv_minutes.setAdapter(new NumericWheelAdapter(0, 59));
        wv_minutes.setCyclic(true);
        wv_minutes.setLabel("分");
        wv_minutes.setCurrentItem(minutes);

        // 根据屏幕密度来指定选择器字体的大小
        int textSize = UtilityBase.getTextNumberSize(mActivity);

        wv_hour.TEXT_SIZE = textSize;
        wv_minutes.TEXT_SIZE = textSize;

        return this;
    }

    public MyDialog setSimpleSelectPicker(int currentSelectIndex, int minRange,
                                          int maxRange, String rightText) {
        LinearLayout timeLayout = (LinearLayout) (dialogView
                .findViewById(R.id.DialogSimpleSelectPicker));
        timeLayout.setVisibility(View.VISIBLE);

        final WheelView wv_content = (WheelView) dialogView
                .findViewById(R.id.simpleSelectPicker);
        wv_content.setAdapter(new NumericWheelAdapter(minRange, maxRange));
        wv_content.setCyclic(true);// 可循环滚动
        wv_content.setLabel(rightText);// 添加文字
        wv_content.setCurrentItem(currentSelectIndex);// 初始化时显示的数据

        int textSize = UtilityBase.getTextNumberSize(mActivity);
        wv_content.TEXT_SIZE = textSize;

        return this;
    }

    public MyDialog setSimpleStrsSelectPicker(String[] strs,int currentSelectIndex) {
        LinearLayout timeLayout = (LinearLayout) (dialogView
                .findViewById(R.id.DialogSimpleSelectPicker));
        timeLayout.setVisibility(View.VISIBLE);

        final WheelView wv_content = (WheelView) dialogView
                .findViewById(R.id.simpleSelectPicker);
        wv_content.setAdapter(new ArrayWheelAdapter(strs));
      //  wv_content.setCyclic(true);// 可循环滚动
        wv_content.setCurrentItem(currentSelectIndex);// 初始化时显示的数据

        int textSize = UtilityBase.getTextNumberSize(mActivity);
        wv_content.TEXT_SIZE = textSize;

        return this;
    }

    /**
     * 双滚轮
     *
     * @param leftCurrentIndex
     * @param rightCurrentIndex
     * @param leftRange
     * @param rightRange
     * @param leftText
     * @param rightText
     * @return
     */
    public MyDialog setDoubleSelectPicker(int leftCurrentIndex,
                                          int rightCurrentIndex, int[] leftRange, int[] rightRange,
                                          String leftText, String rightText) {
        LinearLayout timeLayout = (LinearLayout) (dialogView
                .findViewById(R.id.DialogDoubleSelectPicker));
        timeLayout.setVisibility(View.VISIBLE);
        final WheelView wv_left = (WheelView) dialogView
                .findViewById(R.id.leftSelectPicker);
        wv_left.setItemColor("#ff0000");
        wv_left.setItemCurrentColor("#ffff00");
        wv_left.setAdapter(new NumericWheelAdapter(leftRange[0], leftRange[1]));
        wv_left.setCyclic(true);// 可循环滚动
        wv_left.setLabel(leftText);// 添加文字
        wv_left.setCurrentItem(leftCurrentIndex);// 初始化时显示的数据

        final WheelView wv_right = (WheelView) dialogView
                .findViewById(R.id.rightSelectPicker);
        wv_right.setAdapter(new NumericWheelAdapter(rightRange[0],
                rightRange[1]));
        wv_right.setCyclic(true);// 可循环滚动
        wv_right.setLabel(rightText);// 添加文字
        wv_right.setCurrentItem(rightCurrentIndex);// 初始化时显示的数据

        int textSize = UtilityBase.getTextNumberSize(mActivity);
        wv_left.TEXT_SIZE = textSize;
        wv_right.TEXT_SIZE = textSize;

        return this;
    }


    /**
     * 双列带标题
     */
    public  HashMap<Integer,WheelView> setDoubleSelectPicker() {
        HashMap<Integer,WheelView> bloodWheelMap=new HashMap<Integer, WheelView>();
        LinearLayout timeLayout = (LinearLayout) (dialogView
                .findViewById(R.id.linerlayout_biserial_wheel));
        timeLayout.setVisibility(View.VISIBLE);
        final WheelView wv_left = (WheelView) dialogView
                .findViewById(R.id.wheelview_content_left);
        wv_left.setItemColor("#444444");
        wv_left.setItemCurrentColor("#444444");
        wv_left.setAdapter(new NumericWheelAdapter(30,47,10));
        wv_left.setCyclic(true);// 可循环滚动
        wv_left.setCurrentItem(0);// 初始化时显示的数据
        wv_left.setDefaultItemHeight(100);
        wv_left.setLabel("mmHg");
        wv_left.setDefaultItemWidht(100);
        wv_left.setVisibleItems(7);
        bloodWheelMap.put(1,wv_left);

        final WheelView wv_right = (WheelView) dialogView
                .findViewById(R.id.wheelview_content_right);
        wv_right.setItemColor("#444444");
        wv_right.setItemCurrentColor("#444444");
        wv_right.setAdapter(new NumericWheelAdapter(20, 33,10));
        wv_right.setCyclic(true);// 可循环滚动
        wv_right.setCurrentItem(0);// 初始化时显示的数据
        wv_right.setDefaultItemHeight(100);
        wv_right.setDefaultItemWidht(100);
        wv_right.setVisibleItems(7);
        wv_right.setLabel("mmHg");
        bloodWheelMap.put(2,wv_right);

        int textSize = UtilityBase.getTextNumberSize(mActivity);
        wv_left.TEXT_SIZE = textSize;
        wv_right.TEXT_SIZE = textSize;

        return bloodWheelMap;
    }

    /**
     * 进行优化 双滚轮
     *
     * @param currentindex 默认显示第几个
     * @param mininum      最小数字
     * @param steptotal    滚轮滚动次数
     * @param steplengh    每次滚动步长
     * @return
     */
    public Map<Integer, WheelView> setOptimizedDoubleSelectPicker(int currentindex,
                                                                  int mininum, int steptotal, int steplengh) {

        Map<Integer, WheelView> wheelViewMap = new HashMap<Integer, WheelView>();
        LinearLayout timeLayout = (LinearLayout) (dialogView
                .findViewById(R.id.linerlayout_optimize_wheel));
        timeLayout.setVisibility(View.VISIBLE);
        WheelView wv_left = (WheelView) dialogView
                .findViewById(R.id.wheelview_content_title);
        wv_left.setAdapter(new ArrayWheelAdapter(new String[]{
                "主食类"}));
        wv_left.setCyclic(false);// 可循环滚动
        wv_left.setDefaultItemHeight(100);
        wv_left.setDefaultItemWidht(300);
        wv_left.setItemCurrentColor("#9c9c9c");
        wv_left.setVisibleItems(7);
        wheelViewMap.put(0, wv_left);

        WheelView wv_center = (WheelView) dialogView
                .findViewById(R.id.wheelview_content_value);
        wv_center.setAdapter(new NumericWheelAdapter(mininum, steptotal, steplengh));
        wv_center.setDefaultItemHeight(100);
        wv_center.setDefaultItemWidht(300);
        wv_center.setItemCurrentColor("#9c9c9c");
        wv_center.setCyclic(true);// 可循环滚动
        wv_center.setCurrentItem(currentindex);// 初始化时显示的数据
        wv_center.setVisibleItems(7);
        wheelViewMap.put(1, wv_center);


        WheelView wv_right = (WheelView) dialogView
                .findViewById(R.id.wheelview_content_unit);
        wv_right.setAdapter(new ArrayWheelAdapter(new String[]{
                "160千卡",
                "",
                "选择",
        }));
        wv_right.setCyclic(false);// 可循环滚动
        wv_right.setItemCurrentColor("#9c9c9c");
        wv_right.setDefaultItemHeight(100);
        wv_right.setDefaultItemWidht(300);
        wv_right.setVisibleItems(7);
        wv_right.setCurrentItem(2);// 初始化时显示的数据
        wheelViewMap.put(2, wv_right);


        int textSize = UtilityBase.getTextNumberSize(mActivity);
        wv_left.TEXT_SIZE = textSize;
        wv_right.TEXT_SIZE = textSize;
        wv_center.TEXT_SIZE = textSize;
        return wheelViewMap;
    }

    /**
     * s
     *
     * @param currentindex
     * @param mininum
     * @param steptotal
     * @param steplengh
     * @return
     */
    public Map<Integer, WheelView> setSportDoubleSelectPicker(int currentindex,
                                                              int mininum, int steptotal, int steplengh, String sportType,String tip) {
        Map<Integer, WheelView> wheelViewMap = new HashMap<Integer, WheelView>();

        int textSize = UtilityBase.getTextNumberSize(mActivity);

        WheelView wv_center = (WheelView) dialogView
                .findViewById(R.id.wheelview_content_value);
        wv_center.setAdapter(new NumericWheelAdapter(mininum, steptotal, steplengh));
        wv_center.setDefaultItemHeight(100);
        wv_center.setDefaultItemWidht(300);
        wv_center.setItemCurrentColor("#9c9c9c");
        wv_center.setCyclic(true);// 可循环滚动
        wv_center.setCurrentItem(currentindex);// 初始化时显示的数据
        wv_center.setVisibleItems(7);
        wheelViewMap.put(1, wv_center);

        LinearLayout timeLayout = (LinearLayout) (dialogView
                .findViewById(R.id.linerlayout_optimize_wheel));
        timeLayout.setVisibility(View.VISIBLE);
        WheelView wv_left = (WheelView) dialogView
                .findViewById(R.id.wheelview_content_title);
          wv_left.setVisibility(View.GONE);
        LinearLayout linearLayout= (LinearLayout) dialogView.findViewById(R.id.left_title);
        TextView textview_sporttitle= (TextView) dialogView.findViewById(R.id.textview_sporttitle);
        textview_sporttitle.setHeight(93);
        textview_sporttitle.setText(sportType);
        textview_sporttitle.setTextSize(18);
        TextView textview_sporttips= (TextView) dialogView.findViewById(R.id.textview_sporttips);
        textview_sporttips.setHeight(70);
        textview_sporttips.setTextSize(12);
        textview_sporttips.setText(tip);
        linearLayout.setVisibility(View.VISIBLE);
        wv_center.getCacheItem();
//        wv_left.setAdapter(new ArrayWheelAdapter(new String[]{
//        portType}));
//        wv_left.setCyclic(false);// 可循环滚动
//        wv_left.setDefaultItemHeight(100);
//        wv_left.setDefaultItemWidht(300);
//        wv_left.setItemCurrentColor("#9c9c9c");
//        wv_left.setVisibleItems(7);
//        wheelViewMap.put(0, wv_left);


        WheelView wv_right = (WheelView) dialogView
                .findViewById(R.id.wheelview_content_unit);
        wv_right.setAdapter(new ArrayWheelAdapter(new String[]{
                "分钟",
        }));
        wv_right.setCyclic(false);// 可循环滚动
        wv_right.setItemCurrentColor("#9c9c9c");
        wv_right.setDefaultItemHeight(100);
        wv_right.setDefaultItemWidht(300);
        wv_right.setVisibleItems(7);
        wv_right.setCurrentItem(2);// 初始化时显示的数据
        wheelViewMap.put(2, wv_right);


        //wv_left.TEXT_SIZE = textSize;
        wv_right.TEXT_SIZE = textSize;
        wv_center.TEXT_SIZE = textSize;
        return wheelViewMap;
    }

    /*
     * 设置进度显示 长条带百分比
     */
    public MyDialog setProgressLayout(Drawable drawable) {
        RelativeLayout progressLayout = (RelativeLayout) dialogView
                .findViewById(R.id.DialogProgressLayout);
        progressLayout.setVisibility(View.VISIBLE);

        ProgressBar progressBar = (ProgressBar) dialogView
                .findViewById(R.id.DialogProgressBar);
        if (drawable != null) {
            progressBar.setProgressDrawable(drawable);
        }
        return this;
    }

    public ProgressBar getProgressBar() {
        ProgressBar progressBar = (ProgressBar) dialogView
                .findViewById(R.id.DialogProgressBar);
        return progressBar;
    }

    public TextView getProgressBarTextView() {
        TextView progressBarTextView = (TextView) dialogView
                .findViewById(R.id.DialogProgressBarTextView);
        return progressBarTextView;
    }

    public void dismiss() {
        if (mDialog != null)
            mDialog.dismiss();
    }

    public boolean isShowing() {
        boolean flag = false;
        if (mDialog != null) {
            flag = mDialog.isShowing();
        }
        return flag;
    }
}
