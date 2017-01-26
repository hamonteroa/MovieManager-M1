package com.hamonteroa.moviemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.hamonteroa.moviemanager.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private static final String[] mTabsNames = {
            "Movie",
            "Watch List",
            "Favorites"};
    private static final int[] mTabIcons = {
            android.R.drawable.ic_menu_camera,
            android.R.drawable.ic_menu_view,
            android.R.drawable.star_on
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //User.retriveuserInstance(this);
        User.getInstance().print();

        // Start case
        if (User.getInstance().getRequestToken().isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();
        mViewPager.setCurrentItem(1);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
    }

    private void setupViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new MoviePickerFragment(), mTabsNames[0]);
        viewPagerAdapter.addFragment(new WatchlistFragment(), mTabsNames[1]);
        viewPagerAdapter.addFragment(new FavoritesFragment(), mTabsNames[2]);

        this.mViewPager.setAdapter(viewPagerAdapter);
    }

    private void setupTabIcons() {
        for (int i = 0; i < mTabsNames.length; i++) {
            TextView customTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            customTab.setText(mTabsNames[i]);
            customTab.setCompoundDrawablesWithIntrinsicBounds(0, mTabIcons[i], 0, 0);
            mTabLayout.getTabAt(i).setCustomView(customTab);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position); // To show the title
            //return null; // To don't show the title; to show only the icons
        }
    }
}
