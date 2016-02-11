package com.app.pfh.zhihudaily.ui;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.app.pfh.zhihudaily.R;
import com.app.pfh.zhihudaily.db.WebDbHelper;
import com.app.pfh.zhihudaily.model.Content;
import com.app.pfh.zhihudaily.model.Story;
import com.app.pfh.zhihudaily.utils.HttpUtils;
import com.app.pfh.zhihudaily.utils.NetUtils;
import com.app.pfh.zhihudaily.utils.UrlUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import cz.msebera.android.httpclient.Header;

/**
 * 今日热闻的打开详情页
 */

public class LatestContentActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    private Story story;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private WebView mWebView;
    private ImageView imageView;
    private ImageLoader imageloader;
    private Content content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_content);
        story = (Story) getIntent().getSerializableExtra("story");
        initViews();
        initData();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        mCollapsingToolbarLayout.setTitle(story.getTitle());
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.TRANSPARENT);//抽缩后设置为透明
//        mCollapsingToolbarLayout.setContentScrimColor();
//        mCollapsingToolbarLayout.setStatusBarScrimColor();
        imageView = (ImageView) findViewById(R.id.imageview);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setTextZoom(20);
//        mWebView.getSettings().setBlockNetworkImage(false);
//        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        imageloader = ImageLoader.getInstance();

    }

    private void initData() {
        if(NetUtils.isConnected(LatestContentActivity.this)){
            NetUtils.get(UrlUtils.NEWS_CONTENT_URL + story.getId(), new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    WebDbHelper dbHelper = new WebDbHelper(LatestContentActivity.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    responseString = responseString.replaceAll("'", "''");
                    db.execSQL("replace into Cache(newsId,json) values(" + story.getId() + ",'" + responseString + "')");
                    db.close();
                    parseJson(responseString);
                    Log.e("LatestContentActivity", "解析数据");

                }
            });
        }else {
            WebDbHelper dbHelper = new WebDbHelper(LatestContentActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from Cache where newsId = " + story.getId(), null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
            }
            cursor.close();
            db.close();
        }
    }

    private void parseJson(String response) {
        Gson gson = new Gson();
        content = gson.fromJson(response, Content.class);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageloader.displayImage(content.getImage(),imageView,options);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);


    }


    //重写onBackPressed()
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO 加点动画效果
    }

    //toolbar上的item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_toolar_menu, menu);
        ShareActionProvider mShareActionProvider = (ShareActionProvider)
        MenuItemCompat.getActionProvider(menu.findItem(R.id.share));
//       mShareActionProvider.setShareIntent();
        return true;
    }


}
