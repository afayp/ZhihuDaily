package com.app.pfh.zhihudaily.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.app.pfh.zhihudaily.R;


public class SettingActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("设置");
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                Toast.makeText(SettingActivity.this,"dianl" ,Toast.LENGTH_LONG).show();
                finish();
                break;
        }


        return true;
    }
}
