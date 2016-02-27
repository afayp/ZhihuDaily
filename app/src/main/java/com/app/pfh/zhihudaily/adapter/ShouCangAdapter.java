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
import com.app.pfh.zhihudaily.model.ShouCangModel;
import com.app.pfh.zhihudaily.model.Theme_content_story;
import com.app.pfh.zhihudaily.utils.PrefsUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class ShouCangAdapter extends BaseAdapter {
    private Context mContext;
    private List<ShouCangModel> mList;
    private ImageLoader mImageloader;
    private DisplayImageOptions options;


    public ShouCangAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<ShouCangModel>();
        mImageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public void addData(List<ShouCangModel> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
        ShouCangModel model = mList.get(position);
        holder.tv.setText(model.getTitle());
        Boolean isRead = PrefsUtils.getItemState(mContext, model.getId() + "");
        if(isRead){
            holder.tv.setTextColor(mContext.getResources().getColor(R.color.lighe_item_text_read));
        }
        if(model.getImage() == null){
            holder.iv.setVisibility(View.GONE);
        }else {
            mImageloader.displayImage(model.getImage(), holder.iv, options);
        }

        return convertView;
    }


    class MyViewHOlder {
        TextView tv;
        ImageView iv;
    }
}
