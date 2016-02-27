package com.app.pfh.zhihudaily.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pfh.zhihudaily.Kanner;
import com.app.pfh.zhihudaily.R;
import com.app.pfh.zhihudaily.adapter.StoryAdapter;
import com.app.pfh.zhihudaily.db.DbHelper;
import com.app.pfh.zhihudaily.model.Before;
import com.app.pfh.zhihudaily.model.Latest;
import com.app.pfh.zhihudaily.model.Story;
import com.app.pfh.zhihudaily.model.TopSotory;
import com.app.pfh.zhihudaily.ui.LatestContentActivity;
import com.app.pfh.zhihudaily.ui.MainActivity;
import com.app.pfh.zhihudaily.utils.NetUtils;
import com.app.pfh.zhihudaily.utils.PrefsUtils;
import com.app.pfh.zhihudaily.utils.UrlUtils;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页（今日新闻）的fragment，有轮播图片，放于MianActivity
 */
public class IndexFragment extends BaseFragment {

    private ListView mListView;
    private Kanner mKanner;
    private StoryAdapter storyAdapter;
    private Boolean isLoading;
    private String mDate;



    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) mActivity).setToolbarTitle("今日热闻");
        View view = inflater.inflate(R.layout.index_fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.lv_news);
        View mHeaderView = inflater.inflate(R.layout.kanner, mListView, false);
        mKanner = (Kanner) mHeaderView.findViewById(R.id.kanner);
        mKanner.setOnItemClickListener(new Kanner.OnItemClickListener() {
            @Override
            public void click(View v, TopSotory entity) {
                Log.d("ZhihuDialy", entity.toString());
                Story story = new Story();
                story.setId(entity.getId());
                story.setTitle(entity.getTitle());
                List<String> images = new ArrayList<String>();
                images.add(entity.getImage());
                story.setImages(images);
                Intent intent = new Intent(getActivity(), LatestContentActivity.class);
                intent.putExtra("story", story);
                startActivity(intent);
            }
        });
//        Log.e("ZhuhuDaily", "添加header");
        mListView.addHeaderView(mHeaderView);
        storyAdapter = new StoryAdapter(mActivity);
        mListView.setAdapter(storyAdapter);
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

                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
                        loadMore(UrlUtils.BEFORE_URL + mDate);
                    }

                }

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story story = (Story) parent.getAdapter().getItem(position);
                PrefsUtils.changeItemState(mActivity, story.getId() + "", true);
                TextView tv = (TextView) view.findViewById(R.id.tv_title);
                tv.setTextColor(getResources().getColor(R.color.lighe_item_text_read));
                Intent intent = new Intent(getActivity(), LatestContentActivity.class);
                intent.putExtra("story",story);
                startActivity(intent);
                //TODO 加点特效
            }
        });
        return view;
    }

    @Override
    protected void initData() {
        loadLatest();

    }

    public void loadLatest() {
        isLoading = true;
        if (NetUtils.isConnected(mActivity)) {
            NetUtils.get(UrlUtils.LATEST_URL, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("Zhihudaily","加载json失败");

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                Log.e("ZhuhuDaily", responseString);
                    DbHelper dbHelper = new DbHelper(mActivity);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + Integer.MAX_VALUE + ",' " + responseString + "')");
                    db.close();
                    parseLatestJson(responseString);
                }
            });
        } else {
            DbHelper dbHelper = new DbHelper(mActivity);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + Integer.MAX_VALUE, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            } else {
                isLoading = false;
            }
            cursor.close();
            db.close();
        }
    }
    public void loadMore(String url) {
        isLoading = true;
        if (NetUtils.isConnected(mActivity)) {
            NetUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                Log.e("ZhuhuDaily", responseString);
                    DbHelper dbHelper = new DbHelper(mActivity);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + mDate + ",' " + responseString + "')");
                    db.close();
                    parseBeforeJson(responseString);
                }
            });
        } else {
            DbHelper dbHelper = new DbHelper(mActivity);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + mDate, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            } else {
                db.delete("CacheList", "date < " + mDate, null);
                isLoading = false;
                Toast.makeText(mActivity,"存货已看完，请联网！",Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            db.close();
        }

    }

    private void parseBeforeJson(String responseString) {
        Gson gson = new Gson();
        Before before = gson.fromJson(responseString, Before.class);
        List<Story> storyList = before.getStories();
        storyAdapter.addData(storyList);
        isLoading = false;
    }


    private void parseLatestJson(String responseString) {
        isLoading = false;
        //处理topStory数据
        Gson gson = new Gson();
        Latest latest = gson.fromJson(responseString, Latest.class);
        mDate = latest.getDate();
        List<TopSotory> tops = latest.getTop_stories();
        mKanner.setTopStory(tops);
        //处理story数据
        List<Story> stories = latest.getStories();
        storyAdapter.addData(stories);
    }


}
