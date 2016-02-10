package com.app.pfh.zhihudaily.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pfh.zhihudaily.R;
import com.app.pfh.zhihudaily.model.ThemeContent;
import com.app.pfh.zhihudaily.model.Theme_content_story;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class ThemeAdapter extends BaseAdapter {
    private Context mContext;
    private List<Theme_content_story> mThemeContentStoryList;
    private ImageLoader mImageloader;
    private DisplayImageOptions options;


    public ThemeAdapter(Context context) {
        mContext = context;
        mThemeContentStoryList = new ArrayList<Theme_content_story>();
        mImageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public void addData(List<Theme_content_story> list) {
        mThemeContentStoryList.addAll(list);
        Log.e("ZhuhuDaily", "加载theme数据，adapter添加数据");
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mThemeContentStoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mThemeContentStoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("ZhuhuDaily", "加载theme数据，getview开始");
        MyViewHOlder holder;
        if (convertView == null) {
            holder = new MyViewHOlder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_theme, parent, false);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_title);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_title);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHOlder) convertView.getTag();
        }
        Theme_content_story story = mThemeContentStoryList.get(position);
        Log.e("ZhuhuDaily", story.toString());
        holder.tv.setText(story.getTitle());
        if(story.getImages() == null){
            holder.iv.setVisibility(View.GONE);
        }else {
            mImageloader.displayImage(story.getImages().get(0), holder.iv, options);
        }

        Log.e("ZhuhuDaily", "加载theme数据，getview成功");
        return convertView;
    }


    class MyViewHOlder {
        TextView tv;
        ImageView iv;
    }
}
