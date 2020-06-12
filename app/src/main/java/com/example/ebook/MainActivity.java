package com.example.ebook;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.graphics.Color.rgb;
import static com.example.ebook.FindBooksFragment.bookName;
import static com.example.ebook.FindBooksFragment.mySQLiteOpenHelper;
import static com.example.ebook.FindBooksFragment.db;
import static com.example.ebook.MyInfoFragment.parseJSONWithJSONObject;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;
    private SearchView mSearchView;
    private Context context;
    GridLayout gridLayout;
    Button button;
    View layout;
    FindBooksFragment findBooksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioButton radioButton=(RadioButton)findViewById(R.id.bookshell);
        radioButton.setChecked(true);
        context=this;
        mySQLiteOpenHelper = new MySQLiteOpenHelper(MainActivity.this);
        db = mySQLiteOpenHelper.getWritableDatabase();
        checkPermission();
        Log.e("运行", "开始运行" );
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ArrayList<Fragment> datas = new ArrayList<>();
        datas.add(new FindBooksFragment());
        datas.add(new MyInfoFragment());
        mSectionsPagerAdapter.setData(datas);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTabRadioGroup = (RadioGroup)findViewById(R.id.tabs_rg);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e("点击", "设置点击事件" );
        Log.e("3333","33333");
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        button =new Button(context);
        Log.e("1111","11111");
        findBooksFragment=new FindBooksFragment();
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
                RadioButton radioButton1 = (RadioButton) mTabRadioGroup.getChildAt(position+1);
                radioButton.setBackgroundColor(rgb(213,213,213));
                radioButton.setBackgroundResource(R.drawable.broder_buttom);
                radioButton1.setBackgroundColor(rgb(213, 213, 213));
                radioButton.setChecked(true);
                radioButton1.setChecked(false);

            }else {
                RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
                RadioButton radioButton1 = (RadioButton) mTabRadioGroup.getChildAt(position-1);
                radioButton.setBackgroundColor(rgb(213,213,213));
                radioButton.setBackgroundResource(R.drawable.broder_buttom);
                radioButton1.setBackgroundColor(rgb(213, 213, 213));
                radioButton.setChecked(true);
                radioButton1.setChecked(false);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    private void checkPermission() {
        Log.e("权限","开始申请权限" );
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        } else {
//            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e("授权","checkPermission: 已经授权！");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
//        //通过MenuItem得到SearchView
//        //mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView =(SearchView)searchItem.getActionView();
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent=new Intent(MainActivity.this,DownloadBookList.class);
                Bundle bundle = new Bundle();
                bundle.putString("BOOKNAME", s);
                intent.putExtras(bundle);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.e("CSDN_LQR", "TextChange --> " + s);
                return false;
            }
        });
        //MenuItemCompat.getActionView(searchItem);
        return super.onCreateOptionsMenu(menu);
    }

    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                Toast.makeText(this, "关于", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,About.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void delete1(View view) {
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
Intent intent=new Intent(MainActivity.this,MainActivity.class);
startActivity(intent);
                    }
                }
    }
//    public void reLoadFragView(){
//        /*现将该fragment从fragmentList移除*/
//        if (fragmentList.contains(dashboardFragment)){
//            fragmentList.remove(dashboardFragment);
//        }
//        /*从FragmentManager中移除*/
//        getSupportFragmentManager().beginTransaction().remove(dashboardFragment).commit();
//
//        /*重新创建*/
//        dashboardFragment=new DashboardFragment();
//        /*添加到fragmentList*/
//        fragmentList.add(dashboardFragment);
//
//        /*显示*/
//        showFragment(dashboardFragment,DASHBOARD_FRAGMENT_KEY);
//
//    }
//@Override
//protected void onResume() {
//    super.onResume();
//
//}
}
//        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
//        layout = factory.inflate(R.layout.pageview1, null);
//        gridLayout=(GridLayout)layout.findViewById(R.id.gridvView);
//        pageView1.showBook();
//       showBook();
//        GridLayout gridLayout=(GridLayout)findViewById(R.id.gridvView);

//        button.setText("赶快滚吧");
//        button.setWidth(500);
//        button.setHeight(500);

//    public void openFile(View view) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("text/plain");//设置类型，我这里是任意类型，任意后缀的可以这样写。
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        intent.setDataAndType(uri, "text/plain");
//        startActivityForResult(intent,1);
//        Button button1= new Button(this);
//        button1.setText("qqqqqq");
//        button1.setHeight(500);
//        button1.setWidth(500);
//        ((GridLayout)findBooksFragment.returnView()).addView(button1);
//    }

//    public void jump(View view) {
//        Intent intent=new Intent(MainActivity.this,DemoActivity.class);
//        startActivity(intent);
//    }


//    public  void showBook() {
//        Log.e("增加按钮", "调用");
////        button = new Button(this);
////        button.setText("剑来");
////        button.setHeight(100);
////        button.setWidth(100);
////        button.setBackgroundColor(rgb(0, 0, 0));
////        gridLayout.addView(button);
////        for(int i=0;i<6;i++)
////            for(int j=0;j<5;j++) {
//////                System.out.println("count:"+count);
////                Button btn = new Button(this);
////                btn.setWidth(40);
////                btn.setText("hahah");
//////                count++;
////                GridLayout.Spec rowSpec = GridLayout.spec(i);     //设置它的行和列
////                GridLayout.Spec columnSpec=GridLayout.spec(j);
////                GridLayout.LayoutParams params=new GridLayout.LayoutParams(rowSpec,columnSpec);
////                params.setGravity(Gravity.LEFT);
////                gridLayout.addView(btn,params);
////            }
////gridLayout.setBackgroundColor(rgb(0,0,0));
//    }