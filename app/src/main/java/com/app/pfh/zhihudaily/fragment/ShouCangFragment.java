package com.app.pfh.zhihudaily.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.pfh.zhihudaily.R;
import com.app.pfh.zhihudaily.adapter.ShouCangAdapter;
import com.app.pfh.zhihudaily.model.ShouCangModel;
import com.app.pfh.zhihudaily.model.Story;
import com.app.pfh.zhihudaily.model.Theme_content_story;
import com.app.pfh.zhihudaily.ui.LatestContentActivity;
import com.app.pfh.zhihudaily.ui.MainActivity;
import com.app.pfh.zhihudaily.ui.ThemeContentActivity;
import com.app.pfh.zhihudaily.utils.PrefsUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ShouCangFragment extends BaseFragment{

    private ListView mListView;
    private ShouCangAdapter adapter;
    private Realm realm;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) mActivity).setToolbarTitle("收藏");
        //共用这个布局
        View view = inflater.inflate(R.layout.theme_fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.theme_listview);
        adapter = new ShouCangAdapter(mActivity);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShouCangModel model = (ShouCangModel) parent.getAdapter().getItem(position);
                PrefsUtils.changeItemState(mActivity, model.getId() + "", true);
                TextView tv = (TextView) view.findViewById(R.id.tv_title);
                tv.setTextColor(getResources().getColor(R.color.lighe_item_text_read));
                if(model.isLatest()){
                    Intent intent = new Intent(mActivity, LatestContentActivity.class);
                    Story story = new Story(model.getTitle(), model.getId());
                    intent.putExtra("story",story);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(mActivity, ThemeContentActivity.class);
                    Theme_content_story story = new Theme_content_story(model.getTitle(), model.getId());
                    intent.putExtra("story", story);
                    startActivity(intent);
                }

            }
        });
        return view;
    }

    @Override
    protected void initData() {
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ShouCangModel> all = realm.where(ShouCangModel.class).findAll();
        //TODO 排序
        List<ShouCangModel> shouCangModelList = new ArrayList<>();

        for (ShouCangModel model :all){
            shouCangModelList.add(model);
        }
        realm.commitTransaction();
        adapter.addData(shouCangModelList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(realm != null){
            realm.close();
        }
    }
}
