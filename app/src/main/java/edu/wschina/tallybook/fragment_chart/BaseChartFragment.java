package edu.wschina.tallybook.fragment_chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.adapter.ChartItemAdapter;
import edu.wschina.tallybook.db.ChartItemBean;
import edu.wschina.tallybook.db.DBManager;

public abstract class BaseChartFragment extends Fragment {
  public ListView frag_chart_lv;
  public TextView item_chart_frag_tv_no_data;
  public BarChart item_chart_frag_chart;
  public int mYear;
  public int mMonth;
  public List<ChartItemBean> mData = new ArrayList<>();
  public ChartItemAdapter mChartItemAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chart, container, false);

    frag_chart_lv = view.findViewById(R.id.frag_chart_lv);


    // 获取activity传递过来的数据
    Bundle arguments = getArguments();
    mYear = arguments.getInt("year");
    mMonth = arguments.getInt("month");


    // 设置适配器
    mChartItemAdapter = new ChartItemAdapter(getContext(), mData);
    frag_chart_lv.setAdapter(mChartItemAdapter);

    // 给ListView添加头部布局
    addListViewTopView();
    return view;
  }

  private void addListViewTopView() {
    // 将布局转化view
    View headerView = getLayoutInflater().inflate(R.layout.item_chart_frag_lv_top, null);
    frag_chart_lv.addHeaderView(headerView);

    // 查找头布局当中包含的控件
    item_chart_frag_chart = headerView.findViewById(R.id.item_chart_frag_chart);
    item_chart_frag_tv_no_data = headerView.findViewById(R.id.item_chart_frag_tv_no_data);

    // 设置柱状图不显示描述
    item_chart_frag_chart.getDescription().setEnabled(false);

    // 设置柱状图内边距
    item_chart_frag_chart.setExtraOffsets(20, 20, 20, 20);

    // 设置柱状图坐标轴
    setAxis(mYear, mMonth);

    // 设置图标数据
    setAxisData(mYear, mMonth);
  }

  // 设置图标数据 ： 需要子类进行调用
  public abstract void setAxisData(int year, int month);

  // 设置柱状图坐标轴X坐标
  public void setAxis(int year, int month) {

    // 设置X轴
    XAxis xAxis = item_chart_frag_chart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X显示在下方
    xAxis.setDrawGridLines(true); // 设置显示网格线
    xAxis.setLabelCount(31); // 设置X轴标签显示个数
    xAxis.setTextSize(12f); // X轴标签大小

    // 设置X轴显示格式
    xAxis.setValueFormatter(new ValueFormatter() {
      @Override
      public String getFormattedValue(float value) {
        int val = (int) value;
        if (val == 0) {
          return month + "-1";
        }
        if (val == 14) {
          return month + "-15";
        }
//               根据不同的月份，显示最后一天的位置
        if (month == 2) {
          if (val == 27) {
            return month + "-28";
          }
        } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
          if (val == 30) {
            return month + "-31";
          }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
          if (val == 29) {
            return month + "-30";
          }
        }
        return "";
      }
    });

    // 设置X轴垂直偏移量
    xAxis.setYOffset(10);

    // 设置Y轴
    setYAxis(year, month);
  }

  /* 设置y轴，因为最高的坐标不确定，所以在子类当中设置*/
  public abstract void setYAxis(int year, int month);

  public void setData(int year, int month) {
    this.mYear = year;
    this.mMonth = month;
    // 清空柱状图数据
    item_chart_frag_chart.clear();
    item_chart_frag_chart.invalidate(); // 重新绘制
    setAxis(year,month);
    setAxisData(year,month);
  }

  public void loadData(int year, int month, int kind) {
    List<ChartItemBean> list = DBManager.getChartList(year, month, kind);
    mData.clear();
    mData.addAll(list);
    mChartItemAdapter.notifyDataSetChanged();
  }
}
