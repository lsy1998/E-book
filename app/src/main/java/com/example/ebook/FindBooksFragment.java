package com.example.ebook;



import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bifan.txtreaderlib.bean.TxtMsg;
import com.bifan.txtreaderlib.interfaces.ILoadListener;
import com.bifan.txtreaderlib.main.TxtConfig;
import com.bifan.txtreaderlib.ui.HwTxtPlayActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.ID;
import static com.example.ebook.MyInfoFragment.parseJSONWithJSONObject;

public class FindBooksFragment extends Fragment {

    public static MySQLiteOpenHelper mySQLiteOpenHelper;
    public static SQLiteDatabase db;
    View rootView;
    View popup;
    Button view;
    GridLayout gridLayout;
    PopupWindow popupWindow;
    Button deleteBtn;
    String str;
    int count = 0;
    public static String bookName;
    String Bookcover;
   AlertDialog alertDialog;
    public FindBooksFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("findbook", "被调用");
        rootView = inflater.inflate(R.layout.pageview1, container, false);
        LayoutInflater factory = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        popup = factory.inflate(R.layout.popupwindow, null);
        gridLayout = (GridLayout) rootView.findViewById(R.id.gridvView);
        deleteBtn = (Button) popup.findViewById(R.id.deleteBtn);
        Log.e("2222", "22222");
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext());
        db = mySQLiteOpenHelper.getWritableDatabase();
        showBook();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//选择文件返回
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String chooseFilePath = GetPath.getInstance(getContext()).getChooseFileResultPath(uri);
            Log.e("选择文件", "选择文件返回：" + chooseFilePath);
            String[] strs = chooseFilePath.split("/");
            String str = strs[strs.length - 1];
            String[] strs1 = str.split(".txt");
            Log.e("书名", strs1[0]);
            insert(strs1[0], chooseFilePath);
        }
    }
    int a=-1;
    int b=2;
    int c=0;
    int d=0;




//    public void showBook(){
//        Cursor cursor = db.query("books", null, null, null, null, null, null);
////        int g = -1;
//        while (cursor.moveToNext()) {
//            Log.e("cursor", "有数据");
//            a+=1;
////            Log.e("g", g / 3 + "");
//            System.out.println("count:" + count);
////            DisplayMetrics displayMetrics = new DisplayMetrics();
//            final Button btn = new Button(getContext());
//            Resources resources = getContext().getResources();
//            DisplayMetrics dm = resources.getDisplayMetrics();
//            Log.e( "showBook:屏幕宽度 ",dm.widthPixels+"" );
//            btn.setWidth(dm.widthPixels/3);
//            btn.setHeight((int)((dm.widthPixels/3)*1.2));
//            btn.setBackgroundResource(R.drawable.ic_book_black_24dp1);
//            TextView textView=new TextView(getContext());
//            textView.setWidth(dm.widthPixels/3);
//            textView.setHeight((int)((dm.widthPixels/3)*0.2));
//            textView.setText(cursor.getString(1));
//            textView.setGravity(1);
//
//
//
//            count++;
//            GridLayout.Spec rowSpec = GridLayout.spec((a/ 3)*2);
//            Log.e( "showBook:行 ",a / 3+"" );//设置它的行和列
//            GridLayout.Spec columnSpec = GridLayout.spec((a % 3));
//            Log.e( "showBook:列 ",a % 3+"" );
//            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
//            GridLayout.Spec rowSpec1 = GridLayout.spec(((a/ 3)+1)*2);
//            Log.e( "showBook:行 ",(a / 3)+1+"" );//设置它的行和列
//            GridLayout.Spec columnSpec1 = GridLayout.spec(a % 3);
//            Log.e( "showBook:列 ",a % 3+"" );
//            GridLayout.LayoutParams params1 = new GridLayout.LayoutParams(rowSpec1, columnSpec1);
//            params.setGravity(Gravity.LEFT);
//            params1.setGravity(Gravity.LEFT);
//            gridLayout.addView(btn, params);
//            gridLayout.addView(textView, params1);
//        }
//        cursor.close();
//        final Button btn = new Button(getContext());
//        Resources resources = getContext().getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
//        Log.e( "showBook:屏幕宽度 ",dm.widthPixels+"" );
//        btn.setWidth(dm.widthPixels/3);
//        btn.setBackgroundResource(R.drawable.ic_add_black_24dp2);
//       // btn.setBackgroundColor(Color.parseColor("#ffa52f"));
//        btn.setHeight((int)((dm.widthPixels/3)*1.2));
////        TextView textView=new TextView(getContext());
////        textView.setWidth(dm.widthPixels/3);
////        textView.setHeight((int)((dm.widthPixels/3)*0.2));
//////        textView.setText(cursor.getString(1));
////        textView.setGravity(1);
//
//
////        btn.setText("添加");
//        GridLayout.Spec rowSpec = GridLayout.spec(((a +1)/ 3)*2);     //设置它的行和列
//        GridLayout.Spec columnSpec = GridLayout.spec((a+1) % 3 );
//        Log.e( "showBook:列 ",(a+1) % 3+"" );
////        GridLayout.Spec rowSpec1 = GridLayout.spec(((a +1)/ 3)+1);     //设置它的行和列
////        GridLayout.Spec columnSpec1 = GridLayout.spec((a+1) % 3 );
//        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
////        GridLayout.LayoutParams params1 = new GridLayout.LayoutParams(rowSpec1, columnSpec1);
//        params.setGravity(Gravity.LEFT);
////        params1.setGravity(Gravity.LEFT);
//        gridLayout.addView(btn, params);
////        gridLayout.addView(textView, params1);
//        for (int j = 0; j < gridLayout.getChildCount(); j++) {
//            if((j+4)%3==0){
//                j+=3;
//            }
//            if (j == gridLayout.getChildCount() - 1) {
//                final Button bt = (Button) gridLayout.getChildAt(j);
//                bt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        bt.setText("打开文件");
//                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                        intent.setType("text/plain");
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        startActivityForResult(intent, 1);
//                    }
//                });
//            } else {
//                final Button bt = (Button) gridLayout.getChildAt(j);
//                Log.e("showBook: aaaaa ",j+"" );
//                bt.setOnClickListener( new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////
//                        if (true) {
//                            String str=bt.getText().toString();
//                            Log.e("游标书名", str);
//                            String path="";
//                            Cursor cursor1 = db.query("books", null, null, null, null, null, null);
//                            while (cursor1.moveToNext()) {
//                                Log.e("游标", "有数据");
//                                Log.e("游标", str);
//                                Log.e("游标", cursor1.getString(1));
//                                Log.e("游标", cursor1.getInt(0) + "");
//                                if (str.equals(cursor1.getString(1))) {
//                                    Log.e("相等", "相等");
//                                    path = cursor1.getString(2);
//                                    Log.e("path为", path);
//
//                                }
//                            }
//                            cursor1.close();
//                            TxtConfig.saveIsOnVerticalPageMode(getContext(),false);
//                                HwTxtPlayActivity.loadTxtFile(getContext(), path);
//                            }
//                        }
//                    }
//                );
//
//            }
//        }
//
//        for (int j = 0; j < gridLayout.getChildCount(); j++) {
//            if((j+1)%3==0){
//                j+=3;
//            }
//            if (j == gridLayout.getChildCount() - 1) {
//                final Button bt = (Button) gridLayout.getChildAt(j);
//                bt.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        int id = -1;
//                        Log.e("按钮内容", bt.getText().toString());
//                        Log.e("按钮id", id + "");
//                        str = bt.getText().toString();
//                        initDialog();
//                        return true;
//                    }
//
//                });
//            } else {
//                final Button bt = (Button) gridLayout.getChildAt(j);
//                bt.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        Log.e("按钮内容", bt.getText().toString());
//                        bookName = bt.getText().toString();
//                        initDialog();
//                        return true;
//                    }
//                });
//            }
//        }
//
//    }

    public void showBook(){
        Cursor cursor = db.query("books", null, null, null, null, null, null);
        int g = -1;
        while (cursor.moveToNext()) {
            Log.e("cursor", "有数据");
            g++;
            Log.e("g", g / 3 + "");
            System.out.println("count:" + count);
//            DisplayMetrics displayMetrics = new DisplayMetrics();
            final Button btn = new Button(getContext());
            Resources resources = getContext().getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Log.e( "showBook:屏幕宽度 ",dm.widthPixels+"" );
            btn.setWidth(dm.widthPixels/3);
            btn.setHeight((int)((dm.widthPixels/3)*1.2));
            btn.setBackgroundResource(R.drawable.ic_book_black_24dp1);
            btn.setText(cursor.getString(1));
            count++;
            GridLayout.Spec rowSpec = GridLayout.spec(g / 3);
            Log.e( "showBook:行 ",g / 3+"" );//设置它的行和列
            GridLayout.Spec columnSpec = GridLayout.spec(g % 3);
            Log.e( "showBook:列 ",g % 3+"" );
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
            params.setGravity(Gravity.LEFT);
            gridLayout.addView(btn, params);
        }
        cursor.close();
        final Button btn = new Button(getContext());
        Resources resources = getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Log.e( "showBook:屏幕宽度 ",dm.widthPixels+"" );
        btn.setWidth(dm.widthPixels/3);
        btn.setBackgroundResource(R.drawable.ic_add_black_24dp2);
        // btn.setBackgroundColor(Color.parseColor("#ffa52f"));
        btn.setHeight((int)((dm.widthPixels/3)*1.2));
//        btn.setText("添加");
        GridLayout.Spec rowSpec = GridLayout.spec((g +1)/ 3);     //设置它的行和列
        GridLayout.Spec columnSpec = GridLayout.spec((g+1) % 3 );
        Log.e( "showBook:列 ",(g+1) % 3+"" );
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        params.setGravity(Gravity.LEFT);
        gridLayout.addView(btn, params);
        for (int j = 0; j < gridLayout.getChildCount(); j++) {
            if (j == gridLayout.getChildCount() - 1) {
                final Button bt = (Button) gridLayout.getChildAt(j);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        bt.setText("打开文件");
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("text/plain");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, 1);
                    }
                });
            } else {
                final Button bt = (Button) gridLayout.getChildAt(j);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (true) {
                            String str=bt.getText().toString();
                            Log.e("游标书名", str);
                            String path="";
                            Cursor cursor1 = db.query("books", null, null, null, null, null, null);
                            while (cursor1.moveToNext()) {
                                Log.e("游标", "有数据");
                                Log.e("游标", str);
                                Log.e("游标", cursor1.getString(1));
                                Log.e("游标", cursor1.getInt(0) + "");
                                if (str.equals(cursor1.getString(1))) {
                                    Log.e("相等", "相等");
                                    path = cursor1.getString(2);
                                    Log.e("path为", path);

                                }
                            }
                            cursor1.close();
                            TxtConfig.saveIsOnVerticalPageMode(getContext(),false);
                            HwTxtPlayActivity.loadTxtFile(getContext(), path);
//                            }
                        }
                    }
                });
            }
        }

        for (int j = 0; j < gridLayout.getChildCount(); j++) {
            if (j == gridLayout.getChildCount() - 1) {
                final Button bt = (Button) gridLayout.getChildAt(j);
                bt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int id = -1;
                        Log.e("按钮内容", bt.getText().toString());
                        Log.e("按钮id", id + "");
                        str = bt.getText().toString();
                        showPopupWindow(bt);
                        return true;
                    }

                });
            } else {
                final Button bt = (Button) gridLayout.getChildAt(j);
                bt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.e("按钮内容", bt.getText().toString());
                        bookName = bt.getText().toString();
                        showPopupWindow(bt);
                        return true;
                    }
                });
            }
        }
    }





    public void initDialog()
    {
        //创建AlertDialog的构造器的对象
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        //设置构造器标题
        builder.setTitle("提示");
        //构造器对应的图标
        builder.setIcon(R.drawable.logo);
        //构造器内容,为对话框设置文本项(之后还有列表项的例子)
        builder.setMessage("是否删除？");
        //为构造器设置确定按钮,第一个参数为按钮显示的文本信息，第二个参数为点击后的监听事件，用匿名内部类实现
        builder.setPositiveButton("是", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //第一个参数dialog是点击的确定按钮所属的Dialog对象,第二个参数which是按钮的标示值
                int id=-1;
                String str=bookName;
                Cursor cursor1 = db.query("books", null, null, null, null, null, null);
                while (cursor1.moveToNext()) {
                    Log.e("游标", "有数据");
                    Log.e("游标", str);
                    Log.e("游标", cursor1.getString(1));
                    Log.e("游标", cursor1.getInt(0) + "");
                    if (str.equals(cursor1.getString(1))) {
                        Log.e("相等", "相等");
                        id = cursor1.getInt(0);
                        Log.e("id为", id + "");
                        String where = "_ID= ?";
                        String[] whereValue = {id + ""};
                        db.delete("books", where, whereValue);
                        Intent intent=new Intent(getContext(),MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        //为构造器设置取消按钮,若点击按钮后不需要做任何操作则直接为第二个参数赋值null
        builder.setNegativeButton("否",null);
        //为构造器设置一个比较中性的按钮，比如忽略、稍后提醒等
        //builder.setNeutralButton("",null);
        //利用构造器创建AlertDialog的对象,实现实例化
        alertDialog=builder.create();
        alertDialog.show();
    }

    public void insert(String name, String path) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("path", path);
        db.insert("books", null, values);
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        Log.e("数据库", "保存成功");
    }

    private void showPopupWindow(View view) {
        Button button=(Button)view;
        final String str=(String)button.getText();
        Log.e("按钮内容",str );
        Log.e("弹窗","准备弹出窗口" );
        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.popupwindow, null);
//        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT , true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.rgb(0,0,0)));
        // 设置好参数之后再show
        popupWindow.showAsDropDown(view,0,790);
    }

}



