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
import com.app.pfh.zhihudaily.model.Story;
import com.app.pfh.zhihudaily.utils.PrefsUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class StoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<Story> mStoryList;
    private ImageLoader mImageloader;
    private DisplayImageOptions options;


    public StoryAdapter(Context context) {
        mContext = context;
        mStoryList = new ArrayList<Story>();
        mImageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

    }

    public void addData(List<Story> storyList) {
        mStoryList.addAll(storyList);
        notifyDataSetChanged();
//        Log.e("ZhuhuDaily", "storyAdapter添加数据成功");
    }

    @Override
    public int getCount() {
        return mStoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mStoryList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Story story = mStoryList.get(position);
        MyViewHolder viewHolder;
        View view;
        if (convertView == null) {
            viewHolder = new MyViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_story, parent, false);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.iv_title = (ImageView) view.findViewById(R.id.iv_title);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (MyViewHolder) view.getTag();
        }
        viewHolder.tv_title.setText(mStoryList.get(position).getTitle());
        Boolean isRead = PrefsUtils.getItemState(mContext, story.getId() + "");
        if(isRead){
            viewHolder.tv_title.setTextColor(mContext.getResources().getColor(R.color.lighe_item_text_read));
        }
        mImageloader.displayImage(story.getImages().get(0), viewHolder.iv_title, options);
        return view;
    }

    class MyViewHolder {
        TextView tv_title;
        ImageView iv_title;
    }


}
