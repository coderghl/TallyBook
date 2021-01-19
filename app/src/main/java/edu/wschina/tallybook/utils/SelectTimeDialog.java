package edu.wschina.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import edu.wschina.tallybook.R;

// 记录 or 支出 时间对话框
public class SelectTimeDialog extends Dialog {

  private OnSelectListener mListener;
  private Button yes, no;
  private EditText hour, minute;
  private DatePicker mDatePicker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_calendar);
    initView();
    hideDatePickHeader();
    initEvent();
  }

  private void initEvent() {
    yes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mListener != null) {

          // 获取输入和选择的内容
          // 获取时间
          String sHour = hour.getText().toString();
          String sMinute = minute.getText().toString();

          // 获取年月日
          int year = mDatePicker.getYear();
          int month = mDatePicker.getMonth() + 1;
          int day = mDatePicker.getDayOfMonth();

          String monthStr = month + "";
          String dayStr = day + "";

          // 判断月日是否小于10
          if (month < 10) {
            monthStr = "0" + month;
          }

          if (day < 10) {
            dayStr = "0" + day;
          }

          int hour = 0, minute = 0;

          // 判断时间不能为空
          if (!TextUtils.isEmpty(sHour) || !TextUtils.isEmpty(sMinute)) {
            hour = Integer.parseInt(sHour);
            hour %= 24;

            minute = Integer.parseInt(sMinute);
            minute %= 60;

            // 判断是否小于10
            if (hour < 10) {
              sHour = "0" + hour;
            }

            if (minute < 10) {
              sMinute = "0" + minute;
            }

            // 封装
            String timeFormat = year + "年" + monthStr + "月" + dayStr + "日 " + sHour + ":" + sMinute;
            mListener.yes(timeFormat,year,month,day);

            // 关闭对话框
            cancel();
          }else{
            Toast.makeText(getContext(), "请输入完整信息", Toast.LENGTH_SHORT).show();
          }
        }
      }
    });

    no.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cancel();
      }
    });
  }

  public interface OnSelectListener {
    void yes(String time, int year, int month, int day);
  }

  public void setOnSelectListener(OnSelectListener listener) {
    this.mListener = listener;
  }

  private void initView() {
    yes = findViewById(R.id.dialog_time_btn_yes);
    no = findViewById(R.id.dialog_time_btn_no);
    hour = findViewById(R.id.dialog_time_et_hour);
    minute = findViewById(R.id.dialog_time_et_minute);
    mDatePicker = findViewById(R.id.dialog_time_dp);
  }

  // 隐藏datePick头部
  private void hideDatePickHeader() {
    ViewGroup rootView = (ViewGroup) mDatePicker.getChildAt(0); // 获取布局
    View headerView = rootView.getChildAt(0); // 获取布局当中的头部

    // 获取头部控件id
    int headerId = getContext().getResources().getIdentifier("date_picker_header", "id", "android");

    // 如果相等则直接隐藏掉头部
    if (headerId == headerView.getId()) {
      headerView.setVisibility(View.GONE);
    }
  }

  public SelectTimeDialog(@NonNull Context context) {
    super(context);
  }
}
