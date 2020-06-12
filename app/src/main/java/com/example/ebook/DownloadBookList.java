package com.example.ebook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.ebook.MyInfoFragment.parseJSONWithJSONObject;

public class DownloadBookList extends AppCompatActivity {
    List<Book> booklist=null;
    String bookname;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_book_list);
        Bundle bundle = this.getIntent().getExtras();
        bookname=bundle.getString("BOOKNAME");

        booklist=new LinkedList<Book>();
        Log.e( "onCreate: 顺序", "1");
        booklist();

        Log.e("onCreate: ","setAdapter" );


        Log.e( "onCreate: 顺序", "3");


    }
    private Handler handler = new Handler() {
        public void handleMessage (Message msg) {//
            switch(msg.what)
            {
                case 1:
                    Log.e("handle","bitmap" );
                    BookAdapter bookAdapter = new BookAdapter((LinkedList<Book>) booklist, DownloadBookList.this);
                    ListView lv = (ListView) findViewById(R.id.downloadLv);
                    lv.setAdapter(bookAdapter);

//        lv.setAdapter(bookAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int i, long l) {
                            Log.e("booklist", "成功");
                            TextView textView = (TextView) arg1.findViewById(R.id.ItemTitle);

                            Intent intent = new Intent(DownloadBookList.this, BookDetail.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("BOOKNAME", textView.getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    break;


            }

        }

    };

    public void booklist(){
        try{
            Log.e("booklist: ","try" );
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.199.121:8080/booklist")
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    //请求失败的处理
                    Log.e("搜索","失败"+e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        Log.e("cover","成功" );
                        String resultData = "";
                        InputStream is = null;
                        is=response.body().byteStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader bufferReader = new BufferedReader(isr);
                        String inputLine = "";
                        while ((inputLine = bufferReader.readLine()) != null) {
                            resultData += inputLine + "\n";
                        }
                        Log.e("返回内容","post方法取回内容：" + resultData);
                        parseJSONWithJSONObject(resultData,"bookname0");

                        for(int i=0;i<1000;i++){
                            String str="bookname"+i;
                            if(parseJSONWithJSONObject(resultData,str)==""){
                                break;
                            }
                            String path=parseJSONWithJSONObject(resultData,str);
                            String name=path.substring(9,path.length()-4);
                            Book book=new Book(name,R.drawable.booklogo);
                            if(bookname.equals("lsy")){
                                booklist.add(book);
                                count++;
                                Log.e("onResponse: ","加入" );
                            }else if(bookname.equals(name)){
                                booklist.add(book);
                                count++;
                                Log.e("onResponse: ","加入" );
                            }

                            Log.e("booklist","jsonparse：" +name);
                        }
                        Log.e( "onCreate: 顺序", "2");
if(count==0){
    Looper.prepare();
    Toast.makeText(DownloadBookList.this, "抱歉，没有这本书", Toast.LENGTH_SHORT).show();
    Looper.loop();
}
                        handler.obtainMessage(1,"").sendToTarget();
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
