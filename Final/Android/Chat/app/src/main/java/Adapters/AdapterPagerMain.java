package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import Fragments.FragmentChannels;
import Fragments.FragmentDirects;
import Fragments.FragmentGroups;

public class AdapterPagerMain extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public AdapterPagerMain(FragmentManager fm) {
        super(fm);
        fragments.add(new FragmentDirects());
        fragments.add(new FragmentGroups());
        fragments.add(new FragmentChannels());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
