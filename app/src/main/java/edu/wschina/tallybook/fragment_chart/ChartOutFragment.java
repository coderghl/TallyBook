package edu.wschina.tallybook.fragment_chart;


import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import edu.wschina.tallybook.bean.BarChartItemBean;
import edu.wschina.tallybook.db.DBManager;

public class ChartOutFragment extends BaseChartFragment {
  private int kind = 0;

  @Override
  public void onResume() {
    super.onResume();
    loadData(mYear, mMonth, kind);
  }

  @Override
  public void setAxisData(int year, int month) {
    List<IBarDataSet> data = new ArrayList<>();

    // 获取这个月每天的支出总金额
    List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);

    // 没有消费
    if (list.size() == 0) {
      item_chart_frag_chart.setVisibility(View.GONE);
      item_chart_frag_tv_no_data.setVisibility(View.VISIBLE);
    } else {
      item_chart_frag_chart.setVisibility(View.VISIBLE);
      item_chart_frag_tv_no_data.setVisibility(View.GONE);

      // 设置有多少根柱子
      List<BarEntry> barEntries = new ArrayList<>();
      for (int i = 0; i < 31; i++) {
        // 初始化每一跟柱子
        BarEntry barEntry = new BarEntry(i, 0.0f);
        barEntries.add(barEntry);
      }

      for (int i = 0; i < list.size(); i++) {
        BarChartItemBean itemBean = list.get(i);
        int day = itemBean.getDay(); // 获取那天进行了消费
        // 根据天数，获取X轴位置
        int xIndex = day - 1;
        BarEntry barEntry = barEntries.get(xIndex);
        barEntry.setY(itemBean.getSumMoney());
      }

      BarDataSet barDataSet = new BarDataSet(barEntries, "");
      barDataSet.setValueTextColor(Color.BLACK); // 字体颜色
      barDataSet.setValueTextSize(8f); // 文字大小
      barDataSet.setColor(Color.RED); // 柱子颜色

      // 设置柱子数据显示格式
      barDataSet.setValueFormatter(new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
          if (value > 0) {
            return value + "";
          }else{
            return "";
          }
        }
      });

      data.add(barDataSet);
      BarData barData = new BarData(data);
      barData.setBarWidth(0.4f); // 设置柱子宽度
      item_chart_frag_chart.setData(barData);
    }
  }

  @Override
  public void setYAxis(int year, int month) {
    // 获取本月收入最高的一天，设定为Y轴最大值
    float maxMoney = DBManager.getMaxMoneyOnDayInMonth(year, month, kind);
    float max = (float) Math.ceil(maxMoney); // 向上取整

    // 设置Y轴
    YAxis yAxis_right = item_chart_frag_chart.getAxisRight();
    yAxis_right.setAxisMaximum(max); // 设置y轴的最大值
    yAxis_right.setAxisMinimum(0f); // 设置y轴的最小值
    yAxis_right.setEnabled(false); // 不显示右边的Y轴

    YAxis yAxis_Left = item_chart_frag_chart.getAxisLeft();
    yAxis_Left.setAxisMaximum(max);
    yAxis_Left.setAxisMinimum(0f);
    yAxis_Left.setEnabled(false);

    // 设置不显示图例
    item_chart_frag_chart.getLegend().setEnabled(false);
  }


  @Override
  public void setData(int year, int month) {
    super.setData(year, month);
    loadData(year, month, kind);
  }
}