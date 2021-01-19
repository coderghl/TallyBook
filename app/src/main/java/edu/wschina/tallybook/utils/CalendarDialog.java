package edu.wschina.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.adapter.CalendarAdapter;
import edu.wschina.tallybook.db.DBManager;

public class CalendarDialog extends Dialog implements View.OnClickListener {


  private CalendarAdapter mCalendarAdapter;

  public CalendarDialog(@NonNull Context context, int selectYear, int selectMonth) {
    super(context);
    this.mContext = context;

    this.selectPosition = selectYear;
    this.selectMonth = selectMonth;
  }

  private final Context mContext;

  private GridView dialog_calendar_gv;
  private ImageView dialog_calendar_iv_close;
  private LinearLayout dialog_calendar_layout;

  private List<TextView> hsvViewList = new ArrayList<>(); // 将添加进入线性布局的textview进行统一管理的布局
  private List<Integer> yearList = new ArrayList<>();
  private int selectPosition = -1; // 正在被点击的年份的位置
  private int selectMonth = -1; // 选中月份

  public interface OnRefreshListener {
    void onRefresh(int position, int year, int month);
  }

  private OnRefreshListener mOnRefreshListener;

  public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
    mOnRefreshListener = onRefreshListener;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_history_calendar);
    setSize();
    initView();
  }

  // 设置dialog大小以及显示位置
  private void setSize() {
    Window window = getWindow();
    WindowManager.LayoutParams attributes = window.getAttributes();
    Display display = window.getWindowManager().getDefaultDisplay();
    attributes.width = display.getWidth();
    attributes.gravity = Gravity.TOP;
    window.setBackgroundDrawableResource(android.R.color.transparent);
    window.setAttributes(attributes);
  }

  private void initView() {
    dialog_calendar_gv = findViewById(R.id.dialog_calendar_gv);
    dialog_calendar_iv_close = findViewById(R.id.dialog_calendar_iv_close);
    dialog_calendar_layout = findViewById(R.id.dialog_calendar_layout);

    // 设置点击事件监听
    dialog_calendar_iv_close.setOnClickListener(this);

    // 向横向的ScrollView当中添加View
    addViewToLayout();

    // 初始化gridView
    initGridView();

    // 设置GridView当中每一个item点击事件
    setGridViewListener();
  }

  private void setGridViewListener() {
    dialog_calendar_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCalendarAdapter.selectPosition = position;
        mCalendarAdapter.notifyDataSetChanged();

        int month = position + 1;
        int year = mCalendarAdapter.getYear();

        // 获取到被选中的年份和月份
        mOnRefreshListener.onRefresh(selectPosition, year, month);
        cancel();
      }
    });
  }

  private void initGridView() {
    // 获取被选中的年份
    int selectYear = yearList.get(selectPosition);
    mCalendarAdapter = new CalendarAdapter(getContext(), selectYear);

    if (selectMonth == -1) {
      int month = Calendar.getInstance().get(Calendar.MONTH);
      mCalendarAdapter.selectPosition = month;
    } else {
      mCalendarAdapter.selectPosition = selectMonth - 1;
    }

    dialog_calendar_gv.setAdapter(mCalendarAdapter);
  }

  private void addViewToLayout() {
    yearList = DBManager.getYearListFromAccount(); // 获取数据当中存储了多少年份

    // 如果数据库没有记录，就添加今年的记录
    if (yearList.size() == 0) {
      int year = Calendar.getInstance().get(Calendar.YEAR);
      yearList.add(year);
    }

    // 遍历年份，有几年就添加几年
    for (int i = 0; i < yearList.size(); i++) {
      int year = yearList.get(i);

      View tempView = getLayoutInflater().inflate(R.layout.item_dialog_history_hsv, null);
      dialog_calendar_layout.addView(tempView);

      TextView hsvTv = tempView.findViewById(R.id.item_dialog_history_hsv_tv);
      hsvTv.setText(year + "");
      hsvViewList.add(hsvTv);
    }

    if (selectPosition == -1) {
      selectPosition = hsvViewList.size() - 1; // 设置当前被选中的是最近的年份
    }


    changeHsvTv(selectPosition);    // 将最后一个设置为选中状态
    setHsvTVClickListener(); // 设置每一个View事件
  }

  // 给横向的Scrollview当中每一个TextView设置点击事件
  private void setHsvTVClickListener() {
    for (int i = 0; i < hsvViewList.size(); i++) {
      final int position = i;
      hsvViewList.get(i).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          changeHsvTv(position);
          selectPosition = position;
          // 获取被选中的年份，然后下面的gridView显示数据源也发生变化
          int year = yearList.get(selectPosition);
          mCalendarAdapter.setYear(year);
        }
      });
    }
  }

  // 传入被选中的位置，改变此位置上的背景和文字颜色
  private void changeHsvTv(int selectPosition) {

    // 给所有的textView设置选中样式
    for (int i = 0; i < hsvViewList.size(); i++) {
      TextView hsvText = hsvViewList.get(i);
      hsvText.setBackgroundResource(R.drawable.dialog_btn_bg);
      hsvText.setTextColor(Color.BLACK);
    }

    // 设置选中样式
    TextView selectHsvText = hsvViewList.get(selectPosition);
    selectHsvText.setBackgroundResource(R.drawable.main_recordbtn_bg);
    selectHsvText.setTextColor(Color.WHITE);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.dialog_calendar_iv_close:
        cancel();
        break;
    }
  }
}
