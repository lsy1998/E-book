package com.example.ebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class UploadBookAdapter extends BaseAdapter {

    private LinkedList<UploadBook> mData;
    private Context mContext;

    public UploadBookAdapter(LinkedList<UploadBook> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.upload_book_list_item,parent,false);
        ImageView img_icon = (ImageView) convertView.findViewById(R.id.ItemImage);
        TextView txt_aName = (TextView) convertView.findViewById(R.id.ItemTitle);
        TextView path=(TextView)convertView.findViewById(R.id.ItemPath);
//        Button button=(Button)convertView.findViewById(R.id.upload);

        img_icon.setBackgroundResource(mData.get(position).getImageId());
        txt_aName.setText(mData.get(position).getName());
        path.setText(mData.get(position).getPath());
//        button.setText("上传");
        return convertView;
    }

}
