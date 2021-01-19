package edu.wschina.tallybook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.bean.AccountBean;

public class AccountListAdapter extends BaseAdapter {

  private Context mContext;
  private List<AccountBean> mData;
  private LayoutInflater mInflater;
  private int year, month, day;


  public AccountListAdapter(Context context, List<AccountBean> data) {
    mContext = context;
    mData = data;
    mInflater = LayoutInflater.from(mContext);

    Calendar calendar = Calendar.getInstance();
    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH) + 1;
    day = calendar.get(Calendar.DAY_OF_MONTH);
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
    ViewHolder holder = null;

    if (convertView == null) {
      convertView = mInflater.inflate(R.layout.item_main_lv, parent, false);
      holder = new ViewHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }

    AccountBean bean = mData.get(position);

    holder.item_mainlv_tv_money.setText("¥" + bean.getMoney());
    holder.item_mainlv_tv_note.setText(bean.getRemark());
    holder.itme_mainlv_iv.setImageResource(bean.getSelectImageId());
    holder.item_mainlv_tv_title.setText(bean.getTypeName());

    if (bean.getYear() == year && bean.getDay() == day && bean.getMonth() == bean.getMonth()) {
      String time = bean.getTime().split(" ")[1];
      holder.item_mainlv_tv_time.setText("今天 " + time);
    } else {
      holder.item_mainlv_tv_time.setText(bean.getTime());
    }
    return convertView;
  }

  class ViewHolder {
    private ImageView itme_mainlv_iv;
    private TextView item_mainlv_tv_title, item_mainlv_tv_note, item_mainlv_tv_money, item_mainlv_tv_time;

    public ViewHolder(View view) {
      itme_mainlv_iv = view.findViewById(R.id.itme_mainlv_iv);
      item_mainlv_tv_title = view.findViewById(R.id.item_mainlv_tv_title);
      item_mainlv_tv_note = view.findViewById(R.id.item_mainlv_tv_note);
      item_mainlv_tv_money = view.findViewById(R.id.item_mainlv_tv_money);
      item_mainlv_tv_time = view.findViewById(R.id.item_mainlv_tv_time);
    }
  }
}
