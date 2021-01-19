package edu.wschina.tallybook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.db.ChartItemBean;

// 账单详情页面ListView适配器
public class ChartItemAdapter extends BaseAdapter {
  private Context mContext;
  private List<ChartItemBean> mDataBeam;
  private LayoutInflater mInflater;

  public ChartItemAdapter(Context context, List<ChartItemBean> dataBeam) {
    mContext = context;
    mDataBeam = dataBeam;
    mInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return mDataBeam.size();
  }

  @Override
  public Object getItem(int position) {
    return mDataBeam.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder = null;

    if (convertView == null) {
      convertView = mInflater.inflate(R.layout.item_chart_frag_lv, parent, false);
      holder = new ViewHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    holder.item_chart_frag_iv.setImageResource(mDataBeam.get(position).getSelectImageId());
    holder.item_chart_frag_tv_title.setText(mDataBeam.get(position).getType());
    holder.item_chart_frag_tv_pert.setText(mDataBeam.get(position).getRatio() + "%");
    holder.item_chart_frag_tv_total.setText("¥" + mDataBeam.get(position).getTotalMoney());

    return convertView;
  }

  class ViewHolder {
    private TextView item_chart_frag_tv_title, item_chart_frag_tv_pert, item_chart_frag_tv_total;
    private ImageView item_chart_frag_iv;

    public ViewHolder(View view) {
      item_chart_frag_tv_title = view.findViewById(R.id.item_chart_frag_tv_title);
      item_chart_frag_tv_pert = view.findViewById(R.id.item_chart_frag_tv_pert);
      item_chart_frag_tv_total = view.findViewById(R.id.item_chart_frag_tv_total);
      item_chart_frag_iv = view.findViewById(R.id.item_chart_frag_iv);
    }
  }
}
