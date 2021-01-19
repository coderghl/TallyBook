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

import edu.wschina.tallybook.utils.KeyBoardUtils;
import edu.wschina.tallybook.utils.RemarkDialog;
import edu.wschina.tallybook.utils.SelectTimeDialog;

/*
 *  支出和收入的基地fragment
 * */
public abstract class BaseRecordFragment extends Fragment {

  public KeyboardView mKeyboardView;
  public EditText mMoneyEt;
  public ImageView mTypeIv;
  public TextView mTypeTv, mRemarkTv, mTimeTv;
  public GridView mTypeGv;
  public KeyBoardUtils mKeyBoardUtils;
  List<TypeBean> mTypeBeanList = new ArrayList<>();
  public TypeBaseAdapter mTypeBaseAdapter;
  public AccountBean mAccountBean; // 将需要插入到记账本当中的数据保存到这个对象中
  private SelectTimeDialog mSelectTimeDialog;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAccountBean = new AccountBean(); // 创建对象
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_base_record, container, false);
    initView(view);
    loadDataToGv();    // 给GridView添加数据
    initTime();    // 初始化时间
    initEvent();

    return view;
  }

  private void initTime() {
    // 一进来的时间是当前系统时间
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    String time = dateFormat.format(date);

    mTimeTv.setText(time);
    mAccountBean.setTime(time);

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    mAccountBean.setYear(year);
    mAccountBean.setMonth(month);
    mAccountBean.setDay(day);
  }

  public void loadDataToGv() {
    mTypeBaseAdapter = new TypeBaseAdapter(getContext(), mTypeBeanList);
    mTypeGv.setAdapter(mTypeBaseAdapter);
  }

  private void initEvent() {
    // 监听键盘确定按钮被点击
    mKeyBoardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
      @Override
      public void onEnsure() {
        // 获取输入金钱
        String moneyStr = mMoneyEt.getText().toString();

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")) {
          Toast.makeText(getActivity(), "请输入信息", Toast.LENGTH_SHORT).show();
          return;
        }

        float money = Float.parseFloat(moneyStr);
        mAccountBean.setMoney(money);

        // 保存在数据库中。
        saveAccountToDb(); // 子类重写，因为插入数据不一样

        Log.d("TAG", "onEnsure: " + mAccountBean.toString());
        // 返回上级页面。
        getActivity().finish();
      }
    });

    // 设置GridView 每一项的点击事件
    mTypeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 适配器刷新
        mTypeBaseAdapter.setSelectPosition(position);
        mTypeBaseAdapter.notifyDataSetChanged();

        // 顶部也需要刷新
        mTypeIv.setImageResource(mTypeBeanList.get(position).getSelectImageId());
        mTypeTv.setText(mTypeBeanList.get(position).getTypeName());

        // accountBean也需要刷新
        mAccountBean.setTypeName(mTypeBeanList.get(position).getTypeName());
        mAccountBean.setSelectImageId(mTypeBeanList.get(position).getSelectImageId());
      }
    });

    mRemarkTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 点击显示备注弹框
        RemarkDialog dialog = new RemarkDialog(getContext());
        dialog.show();

        mKeyBoardUtils.hindKeyBoard(); // 关闭自定义软键盘

        // 监听确定按钮
        dialog.setOnEnsureListener(new RemarkDialog.OnEnsureListener() {
          @Override
          public void onEnsure(String data) {
            mAccountBean.setRemark(data); // 将备注信息存储到对象中
            mRemarkTv.setText(data); // 将页面刷新
            dialog.cancel();
            mKeyBoardUtils.showKeyBoard();
          }

          @Override
          public void no() {
            mKeyBoardUtils.showKeyBoard();
          }
        });

      }
    });

    mTimeTv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        selectTime();
      }
    });
  }

  // 选择时间
  private void selectTime() {
    mSelectTimeDialog = new SelectTimeDialog(getContext());
    // 监听点击变化
    mSelectTimeDialog.setOnSelectListener(new SelectTimeDialog.OnSelectListener() {
      @Override
      public void yes(String time, int year, int month, int day) {
        // 更改值
        mTimeTv.setText(time);
        // 设置到数据对象
        mAccountBean.setTime(time);
        mAccountBean.setYear(year);
        mAccountBean.setMonth(month);
        mAccountBean.setDay(day);
      }
    });
    mSelectTimeDialog.show();
  }

  public abstract void saveAccountToDb();

  private void initView(View view) {
    mMoneyEt = view.findViewById(R.id.frag_record_et_money);
    mKeyboardView = view.findViewById(R.id.frag_record_key_board);
    mTypeIv = view.findViewById(R.id.frag_record_type_iv);
    mTypeTv = view.findViewById(R.id.frag_record_type_tv);
    mRemarkTv = view.findViewById(R.id.frag_record_tv_remark);
    mTimeTv = view.findViewById(R.id.frag_record_tv_time);
    mTypeGv = view.findViewById(R.id.frag_record_gv);

    // 让自定义键盘显示出来
    mKeyBoardUtils = new KeyBoardUtils(mKeyboardView, mMoneyEt);
    mKeyBoardUtils.showKeyBoard();
  }

}