package com.app.pfh.zhihudaily.ui;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.app.pfh.zhihudaily.R;
import com.app.pfh.zhihudaily.db.WebDbHelper;
import com.app.pfh.zhihudaily.model.Content;
import com.app.pfh.zhihudaily.model.ShouCangModel;
import com.app.pfh.zhihudaily.model.Story;
import com.app.pfh.zhihudaily.utils.HttpUtils;
import com.app.pfh.zhihudaily.utils.NetUtils;
import com.app.pfh.zhihudaily.utils.UrlUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

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
    private static final String APP_ID = "wx79113d533cf99dee";
    private IWXAPI api;
    private CoordinatorLayout coordinatorLayout;
    private Bitmap shareBitmap;
    private Realm realm;
    private boolean isShoucang;
    private PopupWindow popupWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_content);
        story = (Story) getIntent().getSerializableExtra("story");
        realm = Realm.getDefaultInstance();
        initViews();
        initData();
//        regToWX();
    }

    private void regToWX(){
        api = WXAPIFactory.createWXAPI(this,APP_ID,true);
        api.registerApp(APP_ID);

    }

    private void initViews() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
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
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //设置不调用第三方游览器
                view.loadUrl(url);
                return true;
            }
        });
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
        imageloader.displayImage(content.getImage(),imageView,options,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                shareBitmap = loadedImage;
            }
        });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mWebView.canGoBack()){
                //返回上一个页面，而不是退出
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    //toolbar上的item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_toolar_menu, menu);
        if(isShoucang()){
            menu.findItem(R.id.shoucang).setIcon(R.mipmap.shoucang_black);
            isShoucang = true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.share:
//                showShareWindow();
//                break;
            case R.id.shoucang:
                saveToShoucang(item);
                break;
        }
        return true;
    }

    private void saveToShoucang(MenuItem item) {
        if(isShoucang){
            //取消收藏
            item.setIcon(R.mipmap.shoucang_white);
            isShoucang = false;
            realm.beginTransaction();
            ShouCangModel model = realm.where(ShouCangModel.class).equalTo("id", story.getId()).findFirst();
            model.removeFromRealm();
            realm.commitTransaction();
            Toast.makeText(this, "取消收藏！", Toast.LENGTH_SHORT).show();
        }else {
            //收藏
            item.setIcon(R.mipmap.shoucang_black);
            isShoucang = true;
            ShouCangModel model = new ShouCangModel(story.getTitle(),story.getImages().get(0),story.getId(),true);
            realm.beginTransaction();
            realm.copyToRealm(model);
            realm.commitTransaction();
            Toast.makeText(this, "收藏成功！", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isShoucang(){
        //判断是否已经收藏过了，改变图标
        ShouCangModel model = realm.where(ShouCangModel.class).equalTo("id", story.getId()).findFirst();
        if (model != null){
            return true;
        }
        return false;
    }

    private void showShareWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.share_window,null);
        ImageView imageViewSession = (ImageView) view.findViewById(R.id.share_WX_Session);
        ImageView imageViewTimeline = (ImageView) view.findViewById(R.id.share_WX_Timeline);
        imageViewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(false);

            }
        });
        imageViewTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(true);

            }
        });
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,true);
//        PopupWindow popupWindow = new PopupWindow(view, 500,
//                500,true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(coordinatorLayout, Gravity.CENTER, 0, 0);

    }

    private void share(boolean isToFriend) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = content.getShare_url();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = content.getTitle();
        msg.description = content.getTitle()+"(分享自@高仿知乎日报App)";
        msg.thumbData = bmToByteArray(shareBitmap,true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = isToFriend?SendMessageToWX.Req.WXSceneTimeline :SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
        if(popupWindow != null){
            popupWindow.dismiss();
        }

//        api.openWXApp();
//        Log.e("ZhihuDialy", "打开微信！");

    }

    private byte[] bmToByteArray(Bitmap bitmap ,boolean needRecycle){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,25,baos);
        if(needRecycle){
            bitmap.recycle();
        }
        byte[] result = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 构建一个唯一标志
     * @author YOLANDA
     * @param type
     * @return
     */
    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : (type + System.currentTimeMillis());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(shareBitmap == null){

            shareBitmap.recycle();
        }
        if(realm != null){
            realm.close();
        }
    }
}
