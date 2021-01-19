package edu.wschina.tallybook.fragment_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.bean.TypeBean;

public class TypeBaseAdapter extends BaseAdapter {
  private Context mContext;
  private List<TypeBean> mData;
  private int selectPosition = 0; // 选中的位置，默认第0个被选中

  public TypeBaseAdapter(Context context, List<TypeBean> data) {
    mContext = context;
    mData = data;
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

  // 这个适配器因为不需要滑动，所以不需要写复用

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record_frag_gv, parent, false);

    // 查找当前布局当中的控件
    ImageView iv = convertView.findViewById(R.id.item_record_frag_iv);
    TextView tv = convertView.findViewById(R.id.item_record_frag_tv);

    // 获取指定位置数据源
    TypeBean typeBean = mData.get(position);
    tv.setText(typeBean.getTypeName());

    // 判断当前位置是否被选中
    if (selectPosition == position) {
      iv.setImageResource(typeBean.getSelectImageId());
    }else{
      iv.setImageResource(typeBean.getImageId());
    }

    return convertView;
  }

  public int getSelectPosition() {
    return selectPosition;
  }

  public void setSelectPosition(int selectPosition) {
    this.selectPosition = selectPosition;
  }
}
