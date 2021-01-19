package edu.wschina.tallybook.fragment_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.bean.AccountBean;
import edu.wschina.tallybook.bean.TypeBean;
import edu.wschina.tallybook.db.DBManager;
import edu.wschina.tallybook.utils.KeyBoardUtils;

// 记录页面当中的支出模块
public class OutFragment extends BaseRecordFragment {

  @Override
  public void loadDataToGv() {
    super.loadDataToGv();
    // 获取数据库当中的数据源
    List<TypeBean> typeList = DBManager.getTypeList(0);

    mTypeBeanList.addAll(typeList);
    mTypeBaseAdapter.notifyDataSetChanged(); // 更新

    mTypeTv.setText("其他");
    mTypeIv.setImageResource(R.mipmap.ic_qita_fs);
  }

  @Override
  public void saveAccountToDb() {
    mAccountBean.setKind(0);
    DBManager.insertItemToAccountTb(mAccountBean);
  }
}