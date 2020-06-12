package com.example.ebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BookArrayAdapter extends ArrayAdapter<Book> {
    private int resourceId;
    public BookArrayAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Book hero=getItem(position); //获得当前项的Hero数据
        View view;
        ViewHolder viewHolder; //使用ViewHolder优化 ListView
        if (convertView==null){ //使用convertView重复使用查找加载好的布局
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);//使用布局填充器为子项加载我们传入的子布局「hero_item」
            viewHolder=new ViewHolder();
            viewHolder.heroImage= (ImageView) view.findViewById(R.id.ItemImage);//查找
            viewHolder.heroTitle= (TextView) view.findViewById(R.id.ItemTitle);
            view.setTag(viewHolder);//把ViewHolder储存在View里面

        }else {
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.heroImage.setImageResource(hero.getImageId()); //设置数据
        viewHolder.heroTitle.setText(hero.getName());
        return view;
    }
    class ViewHolder{
        ImageView heroImage;
        TextView heroTitle;
    }
}
