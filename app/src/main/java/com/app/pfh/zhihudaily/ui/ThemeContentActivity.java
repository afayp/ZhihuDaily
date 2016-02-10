package com.app.pfh.zhihudaily.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.app.pfh.zhihudaily.R;
import com.app.pfh.zhihudaily.model.Content;
import com.app.pfh.zhihudaily.model.Story;
import com.app.pfh.zhihudaily.model.Theme_content_story;
import com.app.pfh.zhihudaily.utils.NetUtils;
import com.app.pfh.zhihudaily.utils.UrlUtils;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import cz.msebera.android.httpclient.Header;

/**
 * 主题日报的打开详情页
 */
public class ThemeContentActivity extends AppCompatActivity{

    private Toolbar mToolbar;
    private WebView mWebView;
    private Content content;
    private Theme_content_story story;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_content);
        story = (Theme_content_story) getIntent().getSerializableExtra("story");
        initViews();
        initData();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolbar.setTitle("nima");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    }

    private void initData() {
        if(NetUtils.isConnected(ThemeContentActivity.this)){
            NetUtils.get(UrlUtils.NEWS_CONTENT_URL + story.getId(), new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    parseJson(responseString);
                    Log.e("LatestContentActivity", "解析数据");

                }
            });

        }
    }

    private void parseJson(String response) {
        Gson gson = new Gson();
        content = gson.fromJson(response, Content.class);
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
