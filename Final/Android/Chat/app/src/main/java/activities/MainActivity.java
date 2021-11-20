package activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.content.ContextCompat;

import Adapters.AdapterPagerMain;
import CustomControls.MyUnswipeableViewPager;
import ir.ncis.chat.ActivityEnhanced;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class MainActivity extends ActivityEnhanced {
    private TabLayout tabPages;
    private MyUnswipeableViewPager vpPages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadViews();

        AdapterPagerMain adapter = new AdapterPagerMain(getSupportFragmentManager());
        vpPages.setAdapter(adapter);
        vpPages.setOffscreenPageLimit(2);
        vpPages.setCurrentItem(0);
        tabPages.setupWithViewPager(vpPages);
        Tab tabDirects = tabPages.getTabAt(0);
        Tab tabGroups = tabPages.getTabAt(1);
        Tab tabChannels = tabPages.getTabAt(2);
        if (tabDirects != null) {
            tabDirects.setText(R.string.label_directs);
        }
        if (tabGroups != null) {
            tabGroups.setText(R.string.label_groups);
        }
        if (tabChannels != null) {
            tabChannels.setText(R.string.label_channels);
        }
        tabPages.setTabTextColors(ContextCompat.getColor(App.CONTEXT, R.color.primaryLight), ContextCompat.getColor(App.CONTEXT, R.color.white));
    }

    private void loadViews() {
        tabPages = findViewById(R.id.tabPages);
        vpPages = findViewById(R.id.vpPages);
    }

    @Override
    public void onBackPressed() {
        checkedExit();
    }
}
