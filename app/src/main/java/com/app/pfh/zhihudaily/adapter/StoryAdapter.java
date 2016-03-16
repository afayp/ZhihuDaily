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
import com.app.pfh.zhihudaily.utils.TimeUtils;
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

    public void addData(List<Story> storyList, String date) {
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
    public int getItemViewType(int position) {
        if ("瞎扯".equals(mStoryList.get(position).getTitle().substring(0, 2))) {
//            Log.e("JustWeather", "item的title：" + story.getTitle() + " item的time：" + mDate);
            return 2;
        }
        return 1;
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
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (MyViewHolder) view.getTag();
        }

//        if (getItemViewType(position)==2){
//            Log.e("JustWeather",story.getTitle().substring(0,2));
//            viewHolder.tv_time.setVisibility(View.VISIBLE);
//            viewHolder.tv_time.setText(getTime(story.getGa_prefix()));
//        }

        viewHolder.tv_title.setText(mStoryList.get(position).getTitle());
        Boolean isRead = PrefsUtils.getItemState(mContext, story.getId() + "");
        if (isRead) {
            viewHolder.tv_title.setTextColor(mContext.getResources().getColor(R.color.lighe_item_text_read));
        }
        mImageloader.displayImage(story.getImages().get(0), viewHolder.iv_title, options);

        return view;
    }

    class MyViewHolder {
        TextView tv_title;
        ImageView iv_title;
        TextView tv_time;
    }

    private String getTime(String date) {
        String time = date.substring(0, 2) + "月" + date.substring(2, 4) + "日";
        return time;
    }

}
