package edu.wschina.tallybook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.wschina.tallybook.R;

// 历史账单界面对话框当中gridView对应的适配器
public class CalendarAdapter extends BaseAdapter {

  private Context mContext;
  private List<String> mData = new ArrayList<>();
  private int year;
  public int selectPosition = -1;

  public CalendarAdapter(Context context, int year) {
    mContext = context;
    this.year = year;

    loadData(year);
  }

  private void loadData(int year) {
    for (int i = 1; i < 13; i++) {
      String data = year + "/" + i;
      mData.add(data);
    }
  }

  @Override
  public int getCount() {
    return mData.size();
  }

  @Override
  public Object getItem(int position) {
    return mData.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_history_calendar, parent, false);
    TextView targetTextView = convertView.findViewById(R.id.item_dialog_gv_tv);

    targetTextView.setText(mData.get(position));
    targetTextView.setBackgroundColor(Color.parseColor("#515151"));
    targetTextView.setTextColor(Color.WHITE);

    // 选中
    if (position == selectPosition) {
      targetTextView.setBackgroundColor(Color.parseColor("#006400"));
      targetTextView.setTextColor(Color.WHITE);
    }

    return convertView;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
    mData.clear();
    loadData(year);
    notifyDataSetChanged();
  }
}
