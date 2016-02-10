package com.app.pfh.zhihudaily.ui;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.app.pfh.zhihudaily.R;
import com.app.pfh.zhihudaily.fragment.IndexFragment;
import com.app.pfh.zhihudaily.fragment.ThemeFragment;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mCurType = "latest";
    private int mCurId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        IndexFragment indexFragment = new IndexFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content_framelayout, indexFragment);
        transaction.commit();

    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawerlayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationview);
        mToolbar.setTitle("首页");
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawer, mToolbar, R.string.abc_action_bar_home_description,
                R.string.abc_action_bar_home_description_format);
        mActionBarDrawerToggle.syncState();
        mDrawer.setDrawerListener(mActionBarDrawerToggle);
        mNavigationView.setNavigationItemSelectedListener(new NavigationListener());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 自动刷新
                refresh();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        initNavigationHeader();
    }

    //为NavigationView的header设置点击事件
    private void initNavigationHeader() {
        //TODO 点击没有效果
        View headerView = mNavigationView.getHeaderView(0);
        View loginView = headerView.findViewById(R.id.header_login);
        TextView shoucang = (TextView) headerView.findViewById(R.id.header_shoucang);
        TextView download = (TextView) headerView.findViewById(R.id.header_download);
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击登录哟",Toast.LENGTH_SHORT);
            }
        });
        shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击收藏哟",Toast.LENGTH_SHORT);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"点击下载哟",Toast.LENGTH_SHORT);
            }
        });
    }

    //TODO 1.根据fragmenttype判断刷新,2.每次new一个fragment合适吗。。
    private void refresh() {
        if (mCurType.equals("latest")) {
            IndexFragment indexFragment = new IndexFragment();
            replaceFragment(indexFragment);
        }else {
            ThemeFragment themeFragment = ThemeFragment.newInstance(mCurType,mCurId);
            replaceFragment(themeFragment);
        }
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        transaction.replace(R.id.content_framelayout,fragment);
        transaction.commit();
    }



    class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            //采用了navigationview貌似item不能变更，那就只能用固定的主题日报了

            item.setCheckable(true);
            mDrawer.closeDrawers();
            int itemId = item.getItemId();
            int[] itemIds = {R.id.latest,R.id.tuijianribao,R.id.richangxinlixue,R.id.dianyingribao,R.id.buxuwuliao,R.id.shejiribao,R.id.dagongsiribao,
                    R.id.caijingribao,R.id.hulianwanganquan,R.id.kaishiyouxi,R.id.yinyueribao,R.id.dongmanribao,R.id.tiyuribao};
            int[] ids = {-1,12, 13, 3, 11, 4, 5, 6, 10, 2, 7, 9, 8};//item对应的theme的id
            String[] titles = {"latest","用户推荐日报", "日常心理学", "电影日报", "不许无聊", "设计日报",
                    "大公司日报", "财经日报", "互联网安全", "开始游戏", "音乐日报", "动漫日报", "体育日报"};
            int position = Arrays.binarySearch(itemIds,itemId);
            if(position == 0){
                IndexFragment indexFragment = new IndexFragment();
                replaceFragment(indexFragment);
                mCurId = -1;
                mCurType = "latest";
            }else {
                Log.e("ZhuhuDaily" , "点击了MenuItem"+position);
                ThemeFragment themeFragment = ThemeFragment.newInstance(titles[position], ids[position]);
                replaceFragment(themeFragment);
                mCurId = ids[position];
                mCurType = titles[position];
            }
            return true;
        }
    }


        //toolbar上的item
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);
            return true;
        }

        //toolbaritem点击事件
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.moshi:
                    break;
                case R.id.setting:
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        public void setSwipeRefreshEnable(boolean enable) {
            mSwipeRefreshLayout.setEnabled(enable);
        }

        public void setToolbarTitle(String title) {
            mToolbar.setTitle(title);
        }



}
