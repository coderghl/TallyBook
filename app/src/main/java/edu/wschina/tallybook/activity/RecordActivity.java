package edu.wschina.tallybook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.adapter.RecordFragmentPagerAdapter;
import edu.wschina.tallybook.fragment_record.InFragment;
import edu.wschina.tallybook.fragment_record.OutFragment;

public class RecordActivity extends BaseActivity {
  private TabLayout mRecordTabs;
  private ViewPager mRecordVp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_record);
    initView();
  }

  private void initView() {
    // 查看控件
    mRecordTabs = findViewById(R.id.record_tabs);
    mRecordVp = findViewById(R.id.record_vp);

    // 设置加载fragment
    initPager();
  }

  private void initPager() {
    // 初始化ViewPager页面的集合
    List<Fragment> mFragmentList = new ArrayList<>();

    // 创建fragment页面放置到集合中
    OutFragment outFragment = new OutFragment(); // 支出
    InFragment inFragment = new InFragment(); // 收入

    // 放置
    mFragmentList.add(outFragment);
    mFragmentList.add(inFragment);

    // 创建适配器
    RecordFragmentPagerAdapter pagerAdapter = new RecordFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList);

    // 设置适配器
    mRecordVp.setAdapter(pagerAdapter);

    // 将tab和viewpager进行关联
    mRecordTabs.setupWithViewPager(mRecordVp);
  }

  // 点击事件
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.record_iv_back:
        finish();
        break;
    }
  }
}