package edu.wschina.tallybook.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class RecordFragmentPagerAdapter extends FragmentPagerAdapter {
  private List<Fragment> mFragmentList;
  private String[] titles = {"支出","收入"};

  public RecordFragmentPagerAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList) {
    super(fm);
    this.mFragmentList = fragmentList;
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return mFragmentList.get(position);
  }

  @Override
  public int getCount() {
    return mFragmentList.size();
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return titles[position];
  }
}
