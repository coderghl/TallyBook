package edu.wschina.tallybook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.adapter.ChartViewPagerAdapter;
import edu.wschina.tallybook.db.DBManager;
import edu.wschina.tallybook.fragment_chart.ChartInFragment;
import edu.wschina.tallybook.fragment_chart.ChartOutFragment;
import edu.wschina.tallybook.utils.CalendarDialog;

public class ChartActivity extends AppCompatActivity {
  private ViewPager chart_vp;
  private TextView chart_tv_out, chart_tv_in, chart_tv_date;
  private Button chart_btn_out, chart_btn_in;
  private List<Fragment> chartFragment = new ArrayList<>();

  private int year, month;
  private int dialogYearSelectPosition = -1;
  private int dialogMonthSelectPosition = -1;
  private ChartViewPagerAdapter mChartViewPagerAdapter;
  private ChartInFragment mChartInFragment;
  private ChartOutFragment mChartOutFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chart);
    initView();
    initEvent();
    initTime();
    initStatistics(year, month);
    initFragment();
  }


  private void initFragment() {
    // 添加fragment对象
    mChartInFragment = new ChartInFragment();
    mChartOutFragment = new ChartOutFragment();

    // 添加数据到fragment当中
    Bundle bundle = new Bundle();
    bundle.putInt("year", year);
    bundle.putInt("month", month);
    mChartInFragment.setArguments(bundle);
    mChartOutFragment.setArguments(bundle);

    // 将fragment添加到集合中
    chartFragment.add(mChartOutFragment);
    chartFragment.add(mChartInFragment);

    // 设置适配器
    mChartViewPagerAdapter = new ChartViewPagerAdapter(getSupportFragmentManager(), chartFragment);
    chart_vp.setAdapter(mChartViewPagerAdapter);

    // 将fragment加载到activity中

  }

  // 初始化某年某月的收支数据
  private void initStatistics(int year, int month) {
    float inSumMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);// 收入总钱数
    float outSumMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0); // 支出总钱数
    int inCountItemOneMonth = DBManager.getCountItemOneMonth(1, year, month);// 收入总数
    int outCountItemOneMonth = DBManager.getCountItemOneMonth(0, year, month);// 支出总数

    chart_tv_date.setText(year + "年" + month + "月账单");
    chart_tv_out.setText("共" + outCountItemOneMonth + "笔支出：¥" + outSumMoneyOneMonth);
    chart_tv_in.setText("共" + inCountItemOneMonth + "笔收入：¥" + inSumMoneyOneMonth);
  }

  // 初始化时间
  private void initTime() {
    Calendar calendar = Calendar.getInstance();
    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH) + 1;
  }

  // 初始化控件
  private void initView() {
    chart_tv_in = findViewById(R.id.chart_tv_in);
    chart_tv_out = findViewById(R.id.chart_tv_out);
    chart_vp = findViewById(R.id.chart_vp);
    chart_tv_date = findViewById(R.id.chart_tv_date);
    chart_btn_out = findViewById(R.id.chart_btn_out);
    chart_btn_in = findViewById(R.id.chart_btn_in);
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.chart_iv_back:
        finish();
        break;
      case R.id.chart_iv_calendar:
        showCalendarDialog();
        break;
    }
  }

  // 显示日历对话框
  private void showCalendarDialog() {
    CalendarDialog dialog = new CalendarDialog(this, dialogYearSelectPosition, dialogMonthSelectPosition);
    dialog.show();
    dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
      @Override
      public void onRefresh(int position, int year, int month) {
        initStatistics(year, month);
        dialogYearSelectPosition = position;
        dialogMonthSelectPosition = month;

        mChartInFragment.setData(year,month);
        mChartOutFragment.setData(year,month);
      }
    });
  }

  private void initEvent() {
    chart_btn_in.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setButtonStyle(1);
        chart_vp.setCurrentItem(1);
      }
    });

    chart_btn_out.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setButtonStyle(0);
        chart_vp.setCurrentItem(0);
      }
    });

    chart_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        setButtonStyle(position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });
  }

  // 设置按钮样式改变 支出0 收入1
  private void setButtonStyle(int kind) {
    if (kind == 0) {
      chart_btn_out.setBackgroundColor(Color.parseColor("#FF3700B3"));
      chart_btn_in.setBackgroundColor(Color.parseColor("#3949AB"));
    } else if (kind == 1) {
      chart_btn_in.setBackgroundColor(Color.parseColor("#FF3700B3"));
      chart_btn_out.setBackgroundColor(Color.parseColor("#3949AB"));
    }
  }
}