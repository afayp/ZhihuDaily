package com.app.pfh.zhihudaily.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pfh.zhihudaily.R;
import com.app.pfh.zhihudaily.adapter.ThemeAdapter;
import com.app.pfh.zhihudaily.db.DbHelper;
import com.app.pfh.zhihudaily.model.ThemeContent;
import com.app.pfh.zhihudaily.model.Theme_content_story;
import com.app.pfh.zhihudaily.ui.MainActivity;
import com.app.pfh.zhihudaily.ui.ThemeContentActivity;
import com.app.pfh.zhihudaily.utils.NetUtils;
import com.app.pfh.zhihudaily.utils.PrefsUtils;
import com.app.pfh.zhihudaily.utils.UrlUtils;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 各主题日报的fragment，放于MainFragment，没有轮播图片
 */
public class ThemeFragment extends BaseFragment {

    private static String mTiitle;
    private static int mId;
    private ListView mListView;
    private View headerView;
    private ImageView mTopImageView;
    private ThemeAdapter themeAdapter;
    private Boolean isLoading;
    private TextView mTopTitle;


    public static ThemeFragment newInstance(String title, int id) {
        mTiitle = title;
        mId = id;
        ThemeFragment fragment = new ThemeFragment();
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) mActivity).setToolbarTitle(mTiitle);
        View view = inflater.inflate(R.layout.theme_fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.theme_listview);
        headerView = inflater.inflate(R.layout.theme_header, mListView, false);
        headerView.setClickable(false);
        mTopImageView = (ImageView) headerView.findViewById(R.id.theme_top_imageview);
        mTopTitle = (TextView) headerView.findViewById(R.id.tv_title);
        //TODO 显示主编的头像，目前获取不到头像
        mListView.addHeaderView(headerView);
        themeAdapter = new ThemeAdapter(mActivity);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //处理下拉刷新和listview的下拉冲突
                if (mListView != null && mListView.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setSwipeRefreshEnable(enable);
//                    //不知道加载更多的url
//                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
//                        loadMore(UrlUtils.BEFORE_URL + mDate);
//                    }
                }

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Theme_content_story story = (Theme_content_story) parent.getAdapter().getItem(position);
                PrefsUtils.changeItemState(mActivity, story.getId() + "", true);
                TextView tv = (TextView) view.findViewById(R.id.tv_title);
                tv.setTextColor(getResources().getColor(R.color.lighe_item_text_read));
                Intent intent = new Intent(mActivity, ThemeContentActivity.class);
                intent.putExtra("story", story);
                startActivity(intent);
                Log.e("ThemeContentActivity", "startactivity");
            }
        });

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        loadLatest();
    }

    private void loadLatest() {
        isLoading = true;
        if (NetUtils.isConnected(mActivity)) {
            NetUtils.get(UrlUtils.THEME_CONTENT_URL + mId, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.e("Zhuhudaily", "拿到json");
                    DbHelper dbHelper = new DbHelper(mActivity);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + (Integer.MAX_VALUE + Integer.parseInt(mId + "")) + ",' " + responseString + "')");
                    db.close();
                    Log.e("Zhuhudaily", "存到数据库");
                    parseLatestJson(responseString);
                }
            });
        } else {
            DbHelper dbHelper = new DbHelper(mActivity);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + (Integer.MAX_VALUE + Integer.parseInt(mId + "")), null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            }
            cursor.close();
            db.close();

        }

    }

    private void parseLatestJson(String responseString) {
        Log.e("ZhuhuDaily", "加载theme数据");
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        Gson gson = new Gson();
        ThemeContent content = gson.fromJson(responseString, ThemeContent.class);
        //初始化headview
        mTopTitle.setText(content.getDescription());
        Log.e("ZhuhuDaily", "加载theme数据，topTxt");
        ImageLoader.getInstance().displayImage(content.getBackground(), mTopImageView, options);
        Log.e("ZhuhuDaily", "加载theme数据，topImg");
        List<Theme_content_story> stories = content.getStories();
        themeAdapter.addData(stories);
        mListView.setAdapter(themeAdapter);
        isLoading = false;
    }


}
