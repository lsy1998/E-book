package com.example.ebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;

public class PageView1 extends AppCompatActivity {
//GridLayout gridLayout;
//    Button btn = new Button(PageView1.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("oncreate","被调用");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view1);

    }
//    public void showBook(){
//        for(int i=0;i<6;i++)
//            for(int j=0;j<5;j++) {
////                System.out.println("count:"+count);
//
//                btn.setWidth(40);
//                btn.setText("hahah");
////                count++;
//                GridLayout.Spec rowSpec = GridLayout.spec(i);     //设置它的行和列
//                GridLayout.Spec columnSpec=GridLayout.spec(j);
//                GridLayout.LayoutParams params=new GridLayout.LayoutParams(rowSpec,columnSpec);
//                params.setGravity(Gravity.LEFT);
//                gridLayout.addView(btn,params);
//            }
//    }
}
