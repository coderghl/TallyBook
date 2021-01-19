package edu.wschina.tallybook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.wschina.tallybook.activity.BaseActivity;
import edu.wschina.tallybook.activity.ChartActivity;
import edu.wschina.tallybook.activity.RecordActivity;
import edu.wschina.tallybook.activity.SearchActivity;
import edu.wschina.tallybook.adapter.AccountListAdapter;
import edu.wschina.tallybook.bean.AccountBean;
import edu.wschina.tallybook.db.DBManager;
import edu.wschina.tallybook.utils.BudgetDialog;
import edu.wschina.tallybook.utils.MoreDialog;

public class MainActivity extends BaseActivity {

  private ListView main_lv;
  // listView头部布局相关控件
  private TextView item_mainlv_top_tv_out, item_mainlv_top_tv_in, item_mainlv_top_tv_budget, item_mainlv_top_tv_day, item_mainlv_top_tv_look_table;
  private ImageView item_mainlv_top_iv_hide;

  // 声明数据源
  private List<AccountBean> mAccountBeans = new ArrayList<>();
  private int year, month, day;
  private AccountListAdapter mAdapter;
  Calendar calendar = Calendar.getInstance();
  private SharedPreferences mSharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();
    initEvent();
  }

  private void initEvent() {
    // 设置listview长按事件
    main_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        // 头部卡片
        if (position == 0) {
          return false;
        }

        int pos = position - 1;
        AccountBean clickBean = mAccountBeans.get(pos); // 获取正在被点击的这条信息

        // 弹出对话框
        showDeleteDialog(clickBean);

        return false;
      }
    });
  }

  // 弹出是否删除某一条记录对话框
  private void showDeleteDialog(AccountBean clickBean) {
    new AlertDialog.Builder(this)
      .setTitle("温馨提示")
      .setMessage("确定删除这条记录吗？")
      .setPositiveButton("确定", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          int targetId = clickBean.getId();

          // 执行删除操作
          DBManager.deleteFromAccount(targetId);
          mAccountBeans.remove(clickBean);
          mAdapter.notifyDataSetChanged(); // 删除后更新数据
          setTopData(); // 改变头布局textView显示内容
        }
      })
      .setNegativeButton("取消", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
      })
      .create()
      .show();
  }

  private void initView() {
    main_lv = findViewById(R.id.main_lv);
    // 给listview添加头部布局
    addHeadView();
    mAdapter = new AccountListAdapter(this, mAccountBeans);
    main_lv.setAdapter(mAdapter);

    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH) + 1;
    day = calendar.get(Calendar.DAY_OF_MONTH);

    mSharedPreferences = getSharedPreferences("budget", Context.MODE_PRIVATE);
  }

  /**
   * 给ListView添加头部布局的方法
   */
  private void addHeadView() {
    View headerView = getLayoutInflater().inflate(R.layout.item_main_lv_top, null);
    main_lv.addHeaderView(headerView);

    // 查找头部具可用控件
    item_mainlv_top_tv_out = headerView.findViewById(R.id.item_mainlv_top_tv_out);
    item_mainlv_top_tv_in = headerView.findViewById(R.id.item_mainlv_top_tv_in);
    item_mainlv_top_tv_budget = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
    item_mainlv_top_tv_day = headerView.findViewById(R.id.item_mainlv_top_tv_day);
    item_mainlv_top_iv_hide = headerView.findViewById(R.id.item_mainlv_top_iv_hide);
    item_mainlv_top_tv_look_table = headerView.findViewById(R.id.item_mainlv_top_tv_look_table);

    item_mainlv_top_iv_hide.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 切换明文显示密文显示
        toggleShow();
      }
    });

    headerView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jumpActivity(ChartActivity.class);
      }
    });

    item_mainlv_top_tv_budget.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showBudgetDialog();
      }
    });

    item_mainlv_top_tv_look_table.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jumpActivity(ChartActivity.class);
      }
    });
  }

  // 设置预算对话框
  private void showBudgetDialog() {
    BudgetDialog budgetDialog = new BudgetDialog(this);

    budgetDialog.setOnEnSureListener(new BudgetDialog.OnEnSureListener() {
      @Override
      public void enSure(float money) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();

        edit.putFloat("bMoney", money);
        edit.commit();

        // 计算剩余
        float outSumMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);

        float afterMoney = money - outSumMoneyOneMonth; // 剩余 == 预算 - 支出
        item_mainlv_top_tv_budget.setText("¥" + afterMoney);


      }
    });
    budgetDialog.show();
  }

  private boolean isShow = true;

  // 点击眼睛切换显示模式
  private void toggleShow() {
    if (isShow) { // 密文 - 》 明文
      PasswordTransformationMethod passwordTransformationMethod = PasswordTransformationMethod.getInstance();  // 显示方法

      item_mainlv_top_tv_out.setTransformationMethod(passwordTransformationMethod);
      item_mainlv_top_tv_budget.setTransformationMethod(passwordTransformationMethod);
      item_mainlv_top_tv_in.setTransformationMethod(passwordTransformationMethod);

      item_mainlv_top_iv_hide.setImageResource(R.mipmap.ih_hide);
      isShow = false;
    } else {
      HideReturnsTransformationMethod hideReturnsTransformationMethod = HideReturnsTransformationMethod.getInstance();

      item_mainlv_top_tv_out.setTransformationMethod(hideReturnsTransformationMethod);
      item_mainlv_top_tv_budget.setTransformationMethod(hideReturnsTransformationMethod);
      item_mainlv_top_tv_in.setTransformationMethod(hideReturnsTransformationMethod);

      item_mainlv_top_iv_hide.setImageResource(R.mipmap.ih_show);
      isShow = true;
    }
  }

  // 当activity获取焦点时，会调用方法
  @Override
  protected void onResume() {
    super.onResume();
    // 加载数据库数据
    loadDBData();
    // 更新listview头部数据
    setTopData();
  }

  private void setTopData() {
    // 获取今日
    float inSumMoneyOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
    float outSumMoneyOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
    String infoOneDay = "今日支出¥" + outSumMoneyOneDay + " " + "收入¥" + inSumMoneyOneDay;
    item_mainlv_top_tv_day.setText(infoOneDay);

    // 获取本月
    float outSumMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
    float inSumMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
    item_mainlv_top_tv_in.setText("¥" + inSumMoneyOneMonth);
    item_mainlv_top_tv_out.setText("¥" + outSumMoneyOneMonth);

    // 设置本月预算剩余
    float bMoney = mSharedPreferences.getFloat("bMoney", 0);
    if (bMoney == 0) {
      item_mainlv_top_tv_budget.setText("¥" + 0);
    } else {
      float resultMoney = bMoney - outSumMoneyOneMonth;
      item_mainlv_top_tv_budget.setText("¥" + resultMoney);
    }
  }

  private void loadDBData() {
    List<AccountBean> oneDayTemp = DBManager.getAccountListOneDayData(year, month, day);
    mAccountBeans.clear();
    mAccountBeans.addAll(oneDayTemp);
    mAdapter.notifyDataSetChanged();
  }

  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.main_iv_search:
        jumpActivity(SearchActivity.class);
        break;
      case R.id.main_btn_edit:
        jumpActivity(RecordActivity.class);
        break;
      case R.id.main_btn_more:
        MoreDialog dialog = new MoreDialog(this);
        dialog.show();
        break;
    }

  }
}