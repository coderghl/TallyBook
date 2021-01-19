package edu.wschina.tallybook.fragment_record;

import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.bean.TypeBean;
import edu.wschina.tallybook.db.DBManager;

public class InFragment extends BaseRecordFragment {

  @Override
  public void loadDataToGv() {
    super.loadDataToGv();
    // 获取数据库当中的数据源
    List<TypeBean> typeList = DBManager.getTypeList(1);

    mTypeBeanList.addAll(typeList);
    mTypeBaseAdapter.notifyDataSetChanged(); // 更新

    mTypeTv.setText("其他");
    mTypeIv.setImageResource(R.mipmap.in_qt_fs);
  }

  @Override
  public void saveAccountToDb() {
    mAccountBean.setKind(1);
    DBManager.insertItemToAccountTb(mAccountBean);
  }
}