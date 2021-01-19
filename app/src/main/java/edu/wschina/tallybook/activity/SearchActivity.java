package edu.wschina.tallybook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.adapter.AccountListAdapter;
import edu.wschina.tallybook.bean.AccountBean;
import edu.wschina.tallybook.db.DBManager;

public class SearchActivity extends AppCompatActivity {
  private ListView search_lv_list;
  private EditText search_et_content;
  private TextView search_tv_emp;
  private List<AccountBean> mData = new ArrayList<>();
  private AccountListAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    initView();
    initEvent();
  }

  private void initEvent() {
    search_et_content.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String message = search_et_content.getText().toString().trim();
        if (message != null && message.length() > 0) {
          List<AccountBean> accountBeans = DBManager.searchInfoRemark(message);
          mData.clear();
          mData.addAll(accountBeans);
          mAdapter.notifyDataSetChanged();
        }else{
          mData.clear();
          mAdapter.notifyDataSetChanged();
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
  }

  private void initView() {
    search_lv_list = findViewById(R.id.search_lv_list);
    search_et_content = findViewById(R.id.search_et_content);
    search_tv_emp = findViewById(R.id.search_tv_emp);

    mAdapter = new AccountListAdapter(this, mData);
    search_lv_list.setAdapter(mAdapter);
    search_lv_list.setEmptyView(search_tv_emp); // 设置无数据时显示的控件
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.search_iv_back:
        finish();
        break;
      case R.id.search_iv_search:
        // 搜索
        String message = search_et_content.getText().toString().trim();

        // 判断是否为空
        if (message != null && message.length() > 0) {
          List<AccountBean> accountBeans = DBManager.searchInfoRemark(message);
          Log.d("TAG", "onClick: " + accountBeans.get(0).getTypeName());
          mData.clear();
          mData.addAll(accountBeans);
          mAdapter.notifyDataSetChanged();
        } else {
          Toast.makeText(this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
          mData.clear();
          mAdapter.notifyDataSetChanged();
        }
        break;
    }
  }
}