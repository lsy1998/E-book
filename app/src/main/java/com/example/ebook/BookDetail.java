package com.example.ebook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.ebook.MyInfoFragment.parseJSONWithJSONObject;

public class BookDetail extends AppCompatActivity {
String bookname;
String Bookcover;
String author;
String type;
String shortIntro;
String lastChapter;
String wordCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Bundle bundle = this.getIntent().getExtras();
        bookname=bundle.getString("BOOKNAME");
        bookcover();
    }

    public void bookcover(){
        new Thread(new bookcover()).start();
    }

    public void downloadBook(View view) {
        try {
//            Looper.prepare();
//            Toast.makeText(BookDetail.this, "开始下载", Toast.LENGTH_SHORT).show();
//            Looper.loop(/);
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://192.168.199.121:8080/downloadBook?" + "bookname=" + bookname)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //请求失败的处理
                    Log.e("下载", "失败" + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        Log.e("下载", "成功");
                        String book=response.body().string();

                        writeData(book);
                        Log.e("onResponse: ",URLEncoder.encode(book,"utf-8") );
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("downloadBook: ","异常" +e.getMessage());
            Looper.prepare();
            Toast.makeText(BookDetail.this, "下载失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    class bookcover implements Runnable{
        @Override
        public void run() {

            try {
                String getUrl = "https://api.zhuishushenqi.com/book/fuzzy-search?" + "query=" + bookname + "&start=0&limit=1";
                Log.e("搜索", "内容" + getUrl);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(getUrl)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        //请求失败的处理
                        Log.e("搜索", "失败" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() == 200) {
                            Log.e("搜索", "成功");
                            InputStream is = null;
                            String resultData = "";
                            is = response.body().byteStream();   //获取输入流
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader bufferReader = new BufferedReader(isr);
                            String inputLine = "";
                            while ((inputLine = bufferReader.readLine()) != null) {
                                resultData += inputLine + "\n";
                            }
                            String bookurl = parseJSONWithJSONObject(resultData, "books");
                            //Log.e("封面为","hahah" + bookurl);
                            String bookurl1 = bookurl.substring(1, bookurl.length() - 1);
                            // Log.e("封面为","hahah" + bookurl1);
                            String coverurl = parseJSONWithJSONObject(bookurl1, "cover");
                            author = parseJSONWithJSONObject(bookurl1, "author");
                            type = parseJSONWithJSONObject(bookurl1, "cat");
                            shortIntro = parseJSONWithJSONObject(bookurl1, "shortIntro");
                            lastChapter=parseJSONWithJSONObject(bookurl1, "lastChapter");
                            wordCount=parseJSONWithJSONObject(bookurl1, "wordCount");
                            Log.e("封面为", "" + coverurl);
                            Log.e("作者为", "" + author);
                            Log.e("类型为", "" + type);
                            Log.e("简介为", "" + shortIntro);


                            String cover = coverurl.substring(7, coverurl.length() - 0);
                            //Log.e("封面为","" + cover);
                            Log.e("返回内容", "post方法取回内容：" + resultData);
                            Bookcover = java.net.URLDecoder.decode(cover, "utf-8");
                            Log.e("解码后", Bookcover);
                            new Thread(new download()).start();
                            handler.obtainMessage(2,bookurl).sendToTarget();
                            //                            Intent intent = new Intent(MainActivity.this,BookList.class);
//                            startActivity(intent);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(BookDetail.this, "获取书籍详情失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }

    private void writeData(String str) {
        String filePath = "/sdcard/EBOOK/";
        String fileName = bookname+".txt";
        writeTxtToFile(str, filePath, fileName);
    }

    // 将字符串写入到文本文件中
    private void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent;
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fis = new FileOutputStream(filePath+fileName);
            OutputStreamWriter osw = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(strContent);
            bw.close();
            Looper.prepare();
            Toast.makeText(this, "下载完成", Toast.LENGTH_SHORT).show();
            Looper.loop();

        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
            //Toast.makeText(this, "下载失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.prepare();
            Toast.makeText(BookDetail.this, "文件写入本地失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

//生成文件

    private File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

//生成文件夹

    private  void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
            Toast.makeText(this, "下载失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class download implements Runnable{
        @Override
        public void run() {
            try{
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Bookcover)
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
                            Log.e("cover",""+is );
                            Bitmap bitmap=BitmapFactory.decodeStream(is);
                            handler.obtainMessage(1,bitmap).sendToTarget();
                        }
                    }
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private Handler handler = new Handler() {
                public void handleMessage (Message msg) {//
                    switch(msg.what)
                    {
                        case 1:
                            Log.e("handle","bitmap" );
                            ImageView iv=(ImageView)findViewById(R.id.cover);
                            iv.setImageBitmap((Bitmap) msg.obj);//
                            break;
                        case 2:
                            TextView booknameTv=(TextView)findViewById(R.id.bookname);
                            TextView authorTv=(TextView)findViewById(R.id.author);
                            TextView typeTv=(TextView)findViewById(R.id.type);
                            TextView shortIntroTv=(TextView)findViewById(R.id.shortIntro);
                            TextView lastChapterTv=(TextView)findViewById(R.id.lastChapter);
                            TextView wordCountTv=(TextView)findViewById(R.id.wordCount);
                            booknameTv.setText(bookname);
                            authorTv.setText(author);
                            typeTv.setText(type);
                            shortIntroTv.setText("        "+shortIntro);
                            lastChapterTv.setText("        "+lastChapter);
                            wordCountTv.setText("总字数："+wordCount+"字");
                            break;

                    }

                }

            };
}
