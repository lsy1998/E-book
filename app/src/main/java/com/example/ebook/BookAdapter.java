package com.example.ebook;

import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.LinkedList;
import java.util.List;

public class BookAdapter extends BaseAdapter {
    private LinkedList<Book> mData;
    private Context mContext;

    public BookAdapter(LinkedList<Book> mData, Context mContext) {
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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.booklist_item,parent,false);
        ImageView img_icon = (ImageView) convertView.findViewById(R.id.ItemImage);
        TextView txt_aName = (TextView) convertView.findViewById(R.id.ItemTitle);

        img_icon.setBackgroundResource(mData.get(position).getImageId());
        txt_aName.setText(mData.get(position).getName());

        return convertView;
    }
//    private Context mContext;
//    private LayoutInflater mLayoutInflater;
//
//
//    public BookAdapter(Context context) {
//        super(context , null , 0);
//        mContext = context;
//        mLayoutInflater = LayoutInflater.from(mContext);
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//
//        View itemView = mLayoutInflater.inflate(R.layout.booklist_item , viewGroup, false );
//        if (itemView != null) {
//            ViewHolder vh = new ViewHolder();
//            vh.imageView = itemView.findViewById(R.id.ItemImage);
//            //vh.tvArtist = itemView.findViewById(R.id.ItemText);
//            vh.tvName = itemView.findViewById(R.id.ItemTitle);
//            vh.divider = itemView.findViewById(R.id.divider );
//            itemView.setTag(vh);
//            return itemView;
//        }
//        return null;
//    }
//    public class ViewHolder {
//        ImageView imageView ;
//        TextView tvArtist ;
//        TextView tvName;
//        View divider ;
//    }
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        ViewHolder vh = (ViewHolder) view.getTag();
////        int titleIndex = cursor.getColumnIndex(
////                MediaStore.Audio.Media.TITLE);
////        int artistIndex = cursor.getColumnIndex(
////                MediaStore.Audio.Media.ARTIST);
////        String title = cursor. getString(titleIndex );
////        String artist = cursor. getString(artistIndex );
//        int position = cursor. getPosition ();
//        if (vh != null) {
//            vh.tvName .setText( "剑来" );
//            vh.tvArtist .setText("烽火戏诸侯" );
//            vh.imageView.setBackgroundResource(R.drawable.books);
//        }
//    }
}
